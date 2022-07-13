package net.bmuller.application.service

import arrow.core.*
import arrow.core.continuations.either
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import entities.AuthTokenResponse
import entities.UserEntity
import net.bmuller.application.config.Env
import net.bmuller.application.entities.PlexFriendsResponse
import net.bmuller.application.entities.PlexUser
import net.bmuller.application.entities.UserSession
import net.bmuller.application.lib.*
import net.bmuller.application.repository.PlexTVRepository
import net.bmuller.application.repository.UserRepository
import java.util.*

interface IUserAuthService {
	suspend fun validateAuthToken(userId: Int, tokenVersion: Int?): Either<DomainError, Unit>

	suspend fun signInFlow(authToken: String): Either<DomainError, UserEntity>

	fun createJwtToken(user: UserSession): Either<DomainError, AuthTokenResponse>
}

fun userAuthService(
	env: Env, userRepository: UserRepository, plexTVRepository: PlexTVRepository
) = object : IUserAuthService {
	override suspend fun validateAuthToken(userId: Int, tokenVersion: Int?): Either<DomainError, Unit> = either {
		userRepository
			.getUserAuthVersion(userId).bind()
			?.let { authVersion ->
				if (authVersion == tokenVersion) Unit.right()
				else Unauthorized.left()
			}?.bind()
	}.leftIfNull { Unauthorized }

	override suspend fun signInFlow(authToken: String): Either<DomainError, UserEntity> = either {
		val plexUser = getPlexUser(authToken).flatMap { user -> validateUserAccess(user) }.bind()

		return@either getExistingUser(plexUser.id).bind() ?: registerNewUser(plexUser).bind()
	}

	override fun createJwtToken(user: UserSession): Either<DomainError, AuthTokenResponse> = Either.catchUnknown {
		val token = JWT.create()
			.withAudience(env.auth.jwtAudience)
			.withIssuer(env.auth.jwtIssuer)
			.withClaim("userId", user.id)
			.withClaim("plexUsername", user.plexUsername)
			.withClaim("version", user.version)
			.withExpiresAt(Date(System.currentTimeMillis() + env.auth.jwtTokenLifetime))
			.sign(Algorithm.HMAC256(env.auth.jwtTokenSecret))
		AuthTokenResponse(token)
	}

	/**
	 * Fetch user from plex api
	 */
	private suspend fun getPlexUser(authToken: String): Either<DomainError, PlexUser> =
		plexTVRepository.getUser(authToken).mapLeft { e -> Unknown("Could not fetch Plex user data", e.error) }

	/**
	 * Use PlexId to check for an existing user account
	 */
	private suspend fun getExistingUser(plexUserId: Int): Either<DomainError, UserEntity?> {
		return when (val userResult = userRepository.getUserByPlexId(plexUserId)) {
			is Either.Right -> userResult.value.right()
			is Either.Left -> when (userResult.value) {
				// If entity was not found, return null, so we can fall back
				// to registering a new user
				is EntityNotFound -> null.right()
				else -> Unknown("Error fetching user data").left()
			}
		}
	}


	/**
	 * Creates a new user record from an external plex user
	 */
	private suspend fun registerNewUser(plexUser: PlexUser): Either<DomainError, UserEntity> = either {
		val (username, id, token, email) = plexUser
		userRepository.createAndReturnUser(username, id, token, email).bind()
	}.mapLeft { e -> Unknown("Could not register new user", e.error) }

	/**
	 * Validates that a plex user has access to the server
	 */
	private suspend fun validateUserAccess(plexUser: PlexUser): Either<DomainError, PlexUser> = either {
		// They are the server owner, they have access
		val adminUser = userRepository.getAdminUser().bind()
		if (plexUser.id == adminUser.plexId) return@either plexUser

		// Check friends access
		val friends = plexTVRepository.getFriends(adminUser.plexToken).bind()
		val friend = friends.users.find { friend -> friend.id == plexUser.id }
		friendValidator(friend, plexUser).bind()
	}

	/**
	 * Validates the friend exists and has access to server
	 */
	private fun friendValidator(friend: PlexFriendsResponse.User?, plexUser: PlexUser): Either<DomainError, PlexUser> {
		val forbidden = Forbidden("User does not have access to plex server")
		return friend?.let { f ->
			if (f.server.machineIdentifier == env.plex.machineId) plexUser.right()
			else forbidden.left()
		} ?: forbidden.left()
	}
}