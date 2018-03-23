//package com.willbe.giftapp.appPipe.obj
//
//import com.willbe.giftapp.appPipe.getContext
//import com.willbe.giftapp.libxx.procCall
//import org.apache.commons.lang.SystemUtils
//
//class GradleHandler : Handler {
//    override fun doHandle(context: Context) {
//        var cmd: Array<String>
//        if (SystemUtils.IS_OS_WINDOWS) {
//            cmd = arrayOf("cmd", "/c", "gradlew.bat", "build")
//        } else {
//            cmd = arrayOf("gradlew", "build", "--stacktrace")
//        }
//        procCall(cmd, false, getContext().get().workingDir)
//    }
//}