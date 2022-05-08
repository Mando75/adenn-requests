package net.bmuller.application.repository

import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.resources.*
import net.bmuller.application.entities.PlexAccountResponse
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
}

interface PlexTVRepository {
	suspend fun getUser(authToken: String): PlexUser
}

class PlexTVRepositoryImpl : BaseRepository(), PlexTVRepository {

	override suspend fun getUser(authToken: String): PlexUser {
		val response = plex.client.get(PlexTVResources.Users.Account()) {
			header("X-Plex-Token", authToken)
		}
		val account: PlexAccountResponse = response.body()
		return account.user
	}
}