package net.bmuller.application.repository

import arrow.core.Either
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.*
import net.bmuller.application.entities.PlexAccountResponse
import net.bmuller.application.entities.PlexFriendsResponse
import net.bmuller.application.entities.PlexUser
import net.bmuller.application.http.PlexClient
import net.bmuller.application.lib.Unknown
import net.bmuller.application.lib.catchUnknown

@Resource("/")
@kotlinx.serialization.Serializable
class PlexTVResources {
	@Resource("users")
	@kotlinx.serialization.Serializable
	class Users(@Suppress("unused") val parent: PlexTVResources = PlexTVResources()) {
		@Resource("account.json")
		@kotlinx.serialization.Serializable
		class Account(@Suppress("unused") val parent: Users = Users())
	}

	@Resource("pms")
	@kotlinx.serialization.Serializable
	class PMS(@Suppress("unused") val parent: PlexTVResources = PlexTVResources()) {
		@Resource("friends")
		@kotlinx.serialization.Serializable
		class Friends(@Suppress("unused") val parent: PMS = PMS()) {
			@Resource("all")
			@kotlinx.serialization.Serializable
			class All(@Suppress("unused") val parent: Friends = Friends())
		}
	}
}

interface PlexTVRepository {
	suspend fun getUser(authToken: String): Either<Unknown, PlexUser>
	suspend fun getFriends(authToken: String): Either<Unknown, PlexFriendsResponse>
}

fun plexTVRepository(plex: PlexClient) = object : PlexTVRepository {
	override suspend fun getUser(authToken: String): Either<Unknown, PlexUser> = Either.catchUnknown {
		val response = plex.client.get(PlexTVResources.Users.Account()) {
			header("X-Plex-Token", authToken)
		}
		val account: PlexAccountResponse = response.body()
		account.user
	}

	override suspend fun getFriends(authToken: String): Either<Unknown, PlexFriendsResponse> = Either.catchUnknown {
		val response = plex.client.get(PlexTVResources.PMS.Friends.All()) {
			header("X-Plex-Token", authToken)
			accept(ContentType.Text.Xml)
		}
		response.body()
	}
}