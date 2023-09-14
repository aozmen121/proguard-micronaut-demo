-ignorewarnings
-dontshrink
-dontoptimize
-dontwarn

# Entry point to the app.
-keep class com.example.Application { *; }

-keep class !com.example.** { *; }
-keep interface !com.example.** { *; }

# Keep classes and methods related to Micronaut controllers and routes
-keep @io.micronaut.http.annotation.* class * { *; }
-keepclassmembers class * {
    @io.micronaut.http.annotation.* <methods>;
}

# Keep other Micronaut-specific annotations
-keep @io.micronaut.core.annotation.* class * { *; }

# Keep classes and methods related to Jackson
-keep @com.fasterxml.jackson.annotation.* class * { *; }
-keep @com.fasterxml.jackson.databind.annotation.* class * { *; }
-keepclassmembers class * {
    @com.fasterxml.jackson.annotation.** <methods>;
    @com.fasterxml.jackson.databind.annotation.** <methods>;
}

# Android libraries... - Jackson. Keep classes for Jackson.
-keepclassmembers class * {
    @org.codehaus.jackson.annotate.* <methods>;
}

-keepclassmembers class * {
    @com.fasterxml.jackson.annotation.JsonProperty <methods>;
}

-keepattributes SourceFile, LineNumberTable
-keepattributes InnerClasses,Signature,*Annotation*,EnclosingMethod
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault,Annotation,InnerClasses