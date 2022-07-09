package net.bmuller.application.lib.error

sealed interface DomainError

object Unauthorized : DomainError

data class Unknown(val description: String, val error: Throwable) : DomainError
data class InvalidBody(val description: String, val error: Throwable) : DomainError

data class EntityNotFound(val entityId: String) : DomainError