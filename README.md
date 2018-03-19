# Build an android app direct on your android phone.

> This app work as following:
## Call Aapt by jni call ( process android assets)
> 
	aapt p -f --auto-add-overlay -M /data/data/com.willbe.builder/templates/src/main/AndroidManifest.xml -F /data/data/com.willbe.builder/templates/build/resources.ap_ -I /data/user/0/com.willbe.builder/files/android-21/android.jar -A /data/data/com.willbe.builder/templates/src/main/assets -S /data/data/com.willbe.builder/templates/src/main/res -J /data/data/com.willbe.builder/templates/build/generated/source
## Call sun javac tools to compile MainActivity.java
>   
	java -cp /mnt/d/my/lab/Builder/app/build/intermediates/classes/debug:/mnt/d/my/lab/Builder/app/src/main/resources com.willbe.utils.T
     est -verbose  -cp  .:/data/user/0/com.willbe.builder/files/android-21/android.jar  -sourcepath  /data/data/com.willbe.builder/templates/src/main/java:/data/data/com.willbe.builder/templates/build/generated/
     source  -d  /data/data/com.willbe.builder/templates/build/classes  /data/data/com.willbe.builder/templates/src/main/java/com/willbe/myapplication/MainActivity.java
	
## Dex conversion and merging
## Build Apk through android sdklib
## Sign Apk 
## Apk alignment(not implemented yet)
