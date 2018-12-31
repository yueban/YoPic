package com.yueban.splashyo.data.net

import retrofit2.Response
import timber.log.Timber
import java.util.regex.Pattern

/**
 * @author yueban
 * @date 2018/12/29
 * @email fbzhh007@gmail.com
 */
@Suppress("unused")
sealed class ApiResponse<T> {
    companion object {
        private const val DEFAULT_ERROR_MSG = "unknown error"

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: DEFAULT_ERROR_MSG)
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body, response.headers().get("link"))
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(errorMsg ?: DEFAULT_ERROR_MSG)
            }
        }
    }

    /**
     * empty response
     */
    class ApiEmptyResponse<T> : ApiResponse<T>()

    /**
     * error response
     */
    data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

    /**
     * success response
     */
    data class ApiSuccessResponse<T>(
        val body: T,
        val links: Map<String, String>
    ) : ApiResponse<T>() {

        constructor(body: T, linkHeader: String?) : this(
            body = body,
            links = linkHeader?.extractLinks() ?: emptyMap()
        )

        val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
            links[NEXT_LINK]?.let { next ->
                val matcher = PAGE_PATTERN.matcher(next)
                if (!matcher.find() || matcher.groupCount() != 1) {
                    null
                } else {
                    try {
                        Integer.parseInt(matcher.group(1))
                    } catch (ex: NumberFormatException) {
                        Timber.w("cannot parse next page from $next")
                        null
                    }
                }
            }
        }

        companion object {
            private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
            private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
            private const val NEXT_LINK = "next"

            private fun String.extractLinks(): Map<String, String> {
                val links = mutableMapOf<String, String>()
                val matcher = LINK_PATTERN.matcher(this)

                while (matcher.find()) {
                    val count = matcher.groupCount()
                    if (count == 2) {
                        links[matcher.group(2)] = matcher.group(1)
                    }
                }
                return links
            }
        }
    }
}