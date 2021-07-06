package com.task.smartdubai.usecase.errors

import com.task.smartdubai.data.error.Error

interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}
