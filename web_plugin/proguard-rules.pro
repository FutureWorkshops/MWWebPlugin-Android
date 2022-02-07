# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-repackageclasses com.futureworkshops.mobileworkflow.plugin.web

-keep class * extends com.futureworkshops.mobileworkflow.model.step.Step { public *;  protected *; }
-keep class * extends com.futureworkshops.mobileworkflow.model.step.PluginStep { public *;  protected *; }


-keep class * extends com.futureworkshops.mobileworkflow.model.result.AnswerResult { public *;  protected *; }

-keep class * extends com.futureworkshops.mobileworkflow.model.step.PluginStep { public *; protected *; }
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.futureworkshops.mobileworkflow.plugin.web.step.* { public *;  protected *; }

-keep class * extends com.futureworkshops.mobileworkflow.domain.PluginFactory { public *;  protected *; }
-keep class * extends com.futureworkshops.mobileworkflow.domain.DeserializeStep { public *;  protected *; }

-keepclassmembers class **.R$* {
       public static <fields>;
}

-keep class * extends android.os.Parcelable { public *;  protected *; }

-keep class com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration { public *;  protected *; }

