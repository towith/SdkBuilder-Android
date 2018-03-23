package com.willbe.pipeline

object Constant {
    val templateRepoDir = "D:\\my\\lab\\giftapp\\sample"
    val artifactRepoDir = "D:\\my\\lab\\giftapp\\artifact"
    val apkPath = "app/build/outputs/apk/app-debug.apk"

}

enum class Category(value: kotlin.Int) {
    test(1), love(2), friendship(3), family(4)
}

public enum class WidgetType(value: Int) {
    placeHolderText(1);

    val value: Int = value;
}

