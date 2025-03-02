package com.n26.data.common

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

abstract class BaseInteractor(klass: KClass<*>) {
	private val tag = klass.simpleName.orEmpty()
	private val logger = Logger.withTag(tag)

	protected suspend fun <T> execute(
		dispatcher: CoroutineDispatcher,
		params: Map<String, Any?> = emptyMap(),
		readCache: () -> T? = { null },
		writeCache: (T) -> Unit = {},
		forceRefresh: Boolean = false,
		action: suspend () -> Result<T>,
	): Result<T> = withContext(dispatcher) {
		logger.d {
			val message =
				buildString {
					append("Invoking $tag")

					if (params.isNotEmpty()) {
						append(" with")
						params.entries.forEachIndexed { index, (key, value) ->
							append(" $key: $value")
							if (index < params.entries.size - 1) {
								append(",")
							}
						}
					}

					if (forceRefresh) {
						append(" forcing refresh")
					}
				}
			message
		}

		if (!forceRefresh) {
			readCache()?.let { cachedResult ->
				logger.i { "Returning cached value" }
				return@withContext Result.success(cachedResult)
			}
		}

		return@withContext try {
			action()
				.onSuccess { result ->
					logger.d { "Writing new result to cache" }
					writeCache(result)
					logger.i { "Successfully retrieved result" }
				}.onFailure { exception ->
					logger.e(exception) { "Error retrieving result" }
				}
		} catch (e: Exception) {
			logger.e(e) { "Error retrieving result" }
			Result.failure(e)
		}
	}
}
