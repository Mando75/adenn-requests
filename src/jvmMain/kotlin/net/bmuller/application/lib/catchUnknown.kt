package net.bmuller.application.lib

import arrow.core.Either
import net.bmuller.application.lib.error.Unknown


inline fun <reified A : Any> Either.Companion.catchUnknown(f: () -> A) =
	catch(f).mapLeft { e -> Unknown(e.message ?: "Unknown", e) }