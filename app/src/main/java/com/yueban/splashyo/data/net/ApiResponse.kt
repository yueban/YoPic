package com.yueban.splashyo.data.net

import com.yueban.splashyo.util.DEFAULT_ERROR_MSG
import retrofit2.Response

/**
 * @author yueban
 * @date 2018/12/29
 * @email fbzhh007@gmail.com
 */

@Suppress("unused")
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: DEFAULT_ERROR_MSG)
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body)
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
    data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()
}