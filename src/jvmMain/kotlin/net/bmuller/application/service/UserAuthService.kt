package net.bmuller.application.service

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import entities.UserEntity
import net.bmuller.application.config.Env
import net.bmuller.application.entities.AdminUser
import net.bmuller.application.entities.PlexUser
import net.bmuller.application.repository.PlexTVRepository
import net.bmuller.application.repository.UserRepository
import org.koin.java.KoinJavaComponent.inject

sealed class UserAuthErrors {
	data class CouldNotFetchPlexUser(val error: Throwable) : UserAuthErrors()
	data class ErrorFetchingUser(val error: Throwable) : UserAuthErrors()
	data class UserDoesNotHaveServerAccess(val user: PlexUser) : UserAuthErrors()
	data class CouldNotCreateUser(val error: Throwable) : UserAuthErrors()
}

interface IUserAuthService {
	suspend fun validateAuthToken(userId: Int, tokenVersion: Int?): Boolean

	suspend fun signInFlow(authToken: String): Either<UserAuthErrors, UserEntity>
}

fun userAuthService(
	env: Env.Plex,
	adminUser: AdminUser,
	userRepository: UserRepository,
	plexTVRepository: PlexTVRepository
) = object : IUserAuthService {
	override suspend fun validateAuthToken(userId: Int, tokenVersion: Int?): Boolean {
		return userRepository.getUserAuthVersion(userId)
			?.let { authVersion -> authVersion == tokenVersion } ?: false
	}

	override suspend fun signInFlow(authToken: String): Either<UserAuthErrors, UserEntity> = either {
		val plexUser = getPlexUser(authToken).flatMap { user -> validateUserAccess(user) }.bind()

		return@either getExistingUser(plexUser.id).bind() ?: registerNewUser(plexUser).bind()
	}

	/**
	 * Fetch user from plex api
	 */
	private suspend fun getPlexUser(authToken: String): Either<UserAuthErrors, PlexUser> =
		Either.catch { plexTVRepository.getUser(authToken) }
			.mapLeft { error -> UserAuthErrors.CouldNotFetchPlexUser(error) }

	/**
	 * Use PlexId to check for an existing user account
	 */
	private suspend fun getExistingUser(plexUserId: Int): Either<UserAuthErrors, UserEntity?> =
		Either.catch { userRepository.getUserByPlexId(plexUserId) }
			.mapLeft { error -> UserAuthErrors.ErrorFetchingUser(error) }

	/**
	 * Creates a new user record from an external plex user
	 */
	private suspend fun registerNewUser(plexUser: PlexUser): Either<UserAuthErrors, UserEntity> = Either.catch {
		val newUser = UserEntity.createNew(plexUser.username, plexUser.id, plexUser.email)
		return@catch userRepository.createAndReturnUser(plexUser.authToken, newUser)
	}.mapLeft { error -> UserAuthErrors.CouldNotCreateUser(error) }

	/**
	 * Validates that a plex user has access to the server
	 */
	private suspend fun validateUserAccess(plexUser: PlexUser): Either<UserAuthErrors, PlexUser> {
		// They are the server owner, they have access
		if (plexUser.id == adminUser.plexId) return plexUser.right()
		// Check friends access
		val friends = plexTVRepository.getFriends(adminUser.plexToken)
		return friends.users.find { friend -> friend.id == plexUser.id }?.let { friend ->
			if (friend.server.machineIdentifier == env.machineId) plexUser.right()
			else UserAuthErrors.UserDoesNotHaveServerAccess(plexUser).left()
		} ?: UserAuthErrors.UserDoesNotHaveServerAccess(plexUser).left()
	}
}

class UserAuthService : BaseService() {

	private val adminUser: AdminUser by inject(AdminUser::class.java)

	suspend fun validateAuthToken(userId: Int, tokenVersion: Int?): Boolean {
		return userRepository.getUserAuthVersion(userId)
			?.let { authVersion -> authVersion == tokenVersion } ?: false
	}

	suspend fun signInFlow(authToken: String): Either<UserAuthErrors, UserEntity> = either {
		val plexUser = getPlexUser(authToken).flatMap { user -> validateUserAccess(user) }.bind()

		return@either getExistingUser(plexUser.id).bind() ?: registerNewUser(plexUser).bind()
	}

	/**
	 * Fetch user from plex api
	 */
	private suspend fun getPlexUser(authToken: String): Either<UserAuthErrors, PlexUser> =
		Either.catch { plexTVRepository.getUser(authToken) }
			.mapLeft { error -> UserAuthErrors.CouldNotFetchPlexUser(error) }

	/**
	 * Use PlexId to check for an existing user account
	 */
	private suspend fun getExistingUser(plexUserId: Int): Either<UserAuthErrors, UserEntity?> =
		Either.catch { userRepository.getUserByPlexId(plexUserId) }
			.mapLeft { error -> UserAuthErrors.ErrorFetchingUser(error) }

	/**
	 * Creates a new user record from an external plex user
	 */
	private suspend fun registerNewUser(plexUser: PlexUser): Either<UserAuthErrors, UserEntity> = Either.catch {
		val newUser = UserEntity.createNew(plexUser.username, plexUser.id, plexUser.email)
		return@catch userRepository.createAndReturnUser(plexUser.authToken, newUser)
	}.mapLeft { error -> UserAuthErrors.CouldNotCreateUser(error) }

	/**
	 * Validates that a plex user has access to the server
	 */
	private suspend fun validateUserAccess(plexUser: PlexUser): Either<UserAuthErrors, PlexUser> {
		// They are the server owner, they have access
		if (plexUser.id == adminUser.plexId) return plexUser.right()
		// Check friends access
		val friends = plexTVRepository.getFriends(adminUser.plexToken)
		return friends.users.find { friend -> friend.id == plexUser.id }?.let { friend ->
			if (friend.server.machineIdentifier == env.plex.machineId) plexUser.right()
			else UserAuthErrors.UserDoesNotHaveServerAccess(plexUser).left()
		} ?: UserAuthErrors.UserDoesNotHaveServerAccess(plexUser).left()
	}
}