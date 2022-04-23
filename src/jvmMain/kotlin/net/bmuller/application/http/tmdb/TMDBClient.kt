package net.bmuller.application.http.tmdb

import net.bmuller.application.http.BaseHttpClient

interface TMDBClient {
	public suspend fun search(searchTerm: String)
}

class TMDBClientImpl : BaseHttpClient() {
}