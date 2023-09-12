-ignorewarnings
-dontshrink
-dontoptimize
-dontwarn

# Entry point to the app.
-keep class com.example.Application { *; }

-keep class !com.example.** { *; }
-keep interface !com.example.** { *; }

-keepattributes SourceFile, LineNumberTable
-keepattributes InnerClasses,Signature,*Annotation*,EnclosingMethod
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault,Annotation,InnerClasses