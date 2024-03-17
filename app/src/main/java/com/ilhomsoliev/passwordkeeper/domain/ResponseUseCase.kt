package com.ilhomsoliev.passwordkeeper.domain

sealed class ResponseUseCase<T> {
    data class Success<T>(val value: T) : ResponseUseCase<T>()
    data class Error<T>(val message: ErrorUseCase) : ResponseUseCase<T>()
}

sealed class ErrorUseCase(open val message: String) {
    data object EmptyValue : ErrorUseCase("Empty value")
    data object Unknown : ErrorUseCase("Unknown Error")
    data class OtherError(override val message: String) : ErrorUseCase(message)
}
