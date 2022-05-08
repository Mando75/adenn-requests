package net.bmuller.application.repository

import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.resources.*
import net.bmuller.application.entities.PlexAccountResponse
import net.bmuller.application.entities.PlexFriendsResponse
import net.bmuller.application.entities.PlexMediaContainer
import net.bmuller.application.entities.PlexUser

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
	suspend fun getUser(authToken: String): PlexUser
	suspend fun getFriends(authToken: String): PlexMediaContainer
}

class PlexTVRepositoryImpl : BaseRepository(), PlexTVRepository {

	override suspend fun getUser(authToken: String): PlexUser {
		val response = plex.client.get(PlexTVResources.Users.Account()) {
			header("X-Plex-Token", authToken)
		}
		val account: PlexAccountResponse = response.body()
		return account.user
	}

	override suspend fun getFriends(authToken: String): PlexMediaContainer {
		val response = plex.client.get(PlexTVResources.PMS.Friends.All()) {
			header("X-Plex-Token", authToken)
		}
		val friends: PlexFriendsResponse = response.body()
		return friends.mediaContainer
	}
}