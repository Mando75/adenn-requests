package utils.extensions

import react.query.MutateOptions
import react.query.MutationObserverResult
import kotlin.js.Promise

fun <TData, TError, TVariables, TContext> MutationObserverResult<TData, TError, TVariables, TContext>.exec(
	variables: TVariables,
	options: MutateOptions<TData, TError, TVariables, TContext>? = null
): Promise<TData> {
	return this.mutate(variables, options)
}
