package net.bmuller.application.lib

import arrow.core.Either


inline fun <R> Either.Companion.catchUnknown(f: () -> R) =
	catch(f).mapLeft { e -> Unknown(e.message ?: "Unknown", e) }
