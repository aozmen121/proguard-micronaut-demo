-ignorewarnings
-dontshrink
-dontoptimize
-dontwarn

# Entry point to the app.
-keep class com.example.Application { *; }

-keep class !com.example.** { *; }
-keep interface !com.example.** { *; }

# Seems to fix the serialization issues but not sure if this unobfuscates important IP
-keepclassmembers class * {
    *** get*();
    void set*(***);
}

-keep class com.example.** {
    <fields>;
}

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
    @com.fasterxml.jackson.annotation.* <fields>;
    @com.fasterxml.jackson.annotation.* <methods>;
    @com.fasterxml.jackson.databind.annotation.* <fields>;
    @com.fasterxml.jackson.databind.annotation.* <methods>;
}

# Keep Metadata annotations so they can be parsed at runtime.
-keep class kotlin.reflect.jvm.internal.**
-keep class kotlin.Metadata { *; }

# Keep implementations of service loaded interfaces
# R8 will automatically handle these these in 1.6+
-keep interface kotlin.reflect.jvm.internal.impl.builtins.BuiltInsLoader
-keep class * implements kotlin.reflect.jvm.internal.impl.builtins.BuiltInsLoader { public protected *; }
-keep interface kotlin.reflect.jvm.internal.impl.resolve.ExternalOverridabilityCondition
-keep class * implements kotlin.reflect.jvm.internal.impl.resolve.ExternalOverridabilityCondition { public protected *; }

# Keep attributes needed for proper functionality
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses,Signature,RuntimeVisible,*Annotation*,EnclosingMethod
