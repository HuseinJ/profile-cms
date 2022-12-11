package com.hjusic.api.profileapi.common.result

class Either<out Fail, out Success> private constructor(
    val fail: Fail?,
    val success: Success?
) {
    fun wasSuccess(): Boolean {
        return success != null
    }
    fun wasFailure(): Boolean {
        return fail != null
    }
    companion object{
        fun <L,R> wasSuccess(r: R): Either<L, R>{
            return Either(null, r)
        }

        fun <L,R> wasFailure(l: L): Either<L, R>{
            return Either(l, null)
        }
    }
}