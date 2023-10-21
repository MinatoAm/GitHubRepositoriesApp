package com.example.githubrepositoriesapp.common

sealed class ActionResult<S>(val data: S? = null, val message: String? = null) {
    class Success<S>(data: S?) : ActionResult<S>(data)
    class Error<S>(message: String) : ActionResult<S>(null, message)
    class Loading<S>(data: S? = null) : ActionResult<S>(data)
}
