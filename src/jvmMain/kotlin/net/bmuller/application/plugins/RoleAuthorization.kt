/*
 *    Code in this file is adapted from an example made available under the following copyright license.
 *    I have adapted it to make it compatible with the KTOR 2.0 plugin API, and my specific application use
 *    cases. Including the original copyright license to give proper credit.
 *
 *    Copyright 2020 Ximedes BV
 *    Copyright 2022 Bryan Muller
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.bmuller.application.plugins

import entities.UserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

typealias Role = UserType

private class RoleAuthenticationConfig {
	var any: Set<Role>? = null
	var all: Set<Role>? = null
	var none: Set<Role>? = null
}

private val WithRoleInterceptor: RouteScopedPlugin<RoleAuthenticationConfig> =
	createRouteScopedPlugin("RoleAuthorizationInterceptors", ::RoleAuthenticationConfig) {
		on(AuthenticationChecked) { call ->
			call.parseUserAuth()
				.mapLeft { call.respond(HttpStatusCode.Unauthorized) }
				.map { session -> setOf(session.role) }
				.map { role ->
					val denyReasons = mutableListOf<String>()
					pluginConfig.all?.let { all ->
						val missing = all - role
						if (missing.isNotEmpty()) {
							denyReasons += "Principal lacks required role(s) ${missing.joinToString(" and ")}"
						}
					}
					pluginConfig.any?.let { any ->
						if (any.none { it in role }) {
							denyReasons += "Principal has none of the sufficient role(s) ${any.joinToString(" or ")}}"
						}
					}
					pluginConfig.none?.let { none ->
						if (none.any { it in role }) {
							denyReasons += "Principal has forbidden role(s) ${
								none.intersect(role).joinToString(" and ")
							}"
						}
					}
					if (denyReasons.isNotEmpty()) {
						val message = denyReasons.joinToString(". ")
						java.util.logging.Logger.getLogger("RoleAuthLogger").warning(message)
						call.respond(HttpStatusCode.Forbidden, "You do not have permission to view this resource")
					}
					return@on
				}
		}
	}


private class RoleAuthorizationRouteSelector(private val description: String) : RouteSelector() {
	override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant

	override fun toString() = "(role-authorization $description)"
}

fun Route.withRole(role: Role, build: Route.() -> Unit): Route = authorizedRoute(all = setOf(role), build = build)

fun Route.withAllRoles(vararg roles: Role, build: Route.() -> Unit): Route =
	authorizedRoute(all = roles.toSet(), build = build)

fun Route.withAnyRole(vararg roles: Role, build: Route.() -> Unit): Route =
	authorizedRoute(any = roles.toSet(), build = build)

fun Route.withoutRoles(vararg roles: Role, build: Route.() -> Unit): Route =
	authorizedRoute(none = roles.toSet(), build = build)

private fun Route.authorizedRoute(
	any: Set<Role>? = null,
	all: Set<Role>? = null,
	none: Set<Role>? = null,
	build: Route.() -> Unit
): Route {
	val description = listOfNotNull(
		any?.let { "anyOf (${any.joinToString(" ")})" },
		all?.let { "allOf (${all.joinToString(" ")})" },
		none?.let { "noneOf (${none.joinToString(" ")})" }
	).joinToString(",")
	val authorizedRoute = createChild(RoleAuthorizationRouteSelector(description))

	authorizedRoute.install(WithRoleInterceptor) {
		this.any = any
		this.all = all
		this.none = none
	}

	authorizedRoute.build()
	return authorizedRoute
}
