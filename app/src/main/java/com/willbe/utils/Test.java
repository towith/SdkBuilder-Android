package com.willbe.utils;

import com.sun.tools.javac.Main;

/**
 *
 *
 *
 aapt p -f --auto-add-overlay -M /data/data/com.willbe.builder/templates/src/main/AndroidManifest.xml -F /data/data/com.willbe.builder/templates/build/resources.ap_ -I /data/user/0/com.willbe.builder/files/android-21/android.jar -A /data/data/com.willbe.builder/templates/src/main/assets -S /data/data/com.willbe.builder/templates/src/main/res -J /data/data/com.willbe.builder/templates/build/generated/source
  -m
 *
 *
 *
 *
 *
 *
 *
 java -cp /mnt/d/my/lab/Builder/app/build/intermediates/classes/debug:/mnt/d/my/lab/Builder/app/src/main/resources com.willbe.utils.T
 est -verbose  -cp  .:/data/user/0/com.willbe.builder/files/android-21/android.jar  -sourcepath  /data/data/com.willbe.builder/templates/src/main/java:/data/data/com.willbe.builder/templates/build/generated/
 source  -d  /data/data/com.willbe.builder/templates/build/classes  /data/data/com.willbe.builder/templates/src/main/java/com/willbe/myapplication/MainActivity.java


 cp -rv /mnt/d/my/lab/Builder/app/src/main/assets/templates/src /data/data/com.willbe.builder/templates/
 */
public class Test {
    public static void main(String[] args) {
        Main.compile(args);
    }
}
