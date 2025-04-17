package com.fridge.fridge_server.common

class CustomException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)
