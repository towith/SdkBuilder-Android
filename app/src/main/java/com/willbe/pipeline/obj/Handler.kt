package com.willbe.giftapp.appPipe.obj

import java.io.IOException

interface Handler {
    @Throws(IOException::class)
    fun doHandle(context: Context)
}
