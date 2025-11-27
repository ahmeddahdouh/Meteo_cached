package dz.ahmed.meteo_cahed.data.model

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val message: String, val cause: Throwable? = null, val cachedData: T? = null) :
        ApiResult<T>()

    data class Loading<T>(val cachedData: T? = null) : ApiResult<T>()

    inline fun <R> map(transform: (T) -> R): ApiResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(message, cause, cachedData?.let(transform))
        is Loading -> Loading(cachedData?.let(transform))
    }
}



