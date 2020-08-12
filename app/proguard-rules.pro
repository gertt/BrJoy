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
-dontpreverify
-repackageclasses ''
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*

-ignorewarnings

#-keep class *{
#public private *;
#}




-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgent
#-keep public class * extends android.preference.Preference
#-keep public class * extends com.android.support:appcompat-v7
#-keep public class * extends com.android.support:cardview-v7
#-keep public class * extends com.actionbarsherlock.app.SherlockListFragment
#-keep public class * extends com.actionbarsherlock.app.SherlockFragment
#-keep public class * extends com.actionbarsherlock.app.SherlockFragmentActivity
#-keep public class * extends android.app.Fragment
#-keep public class com.android.vending.licensing.ILicensingService
# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
#-keepclasseswithmembernames class * {
# native <methods>;
#}

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}

#-keepclasseswithmembers class * {
# public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembers class * {
# public <init>(android.content.Context, android.util.AttributeSet, int);
#}


-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}
-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}
#-keepclassmembers class * implements android.os.Parcelable {
#    static ** CREATOR;
#}
-keepclassmembers class * extends android.app.Activity {
   public void *On*Click(android.view.View);
   public void *on*Click(android.view.View);
}
-keepattributes Signature
-keepattributes Exceptions
# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
 public static <fields>;
}
#-dontskipnonpubliclibraryclasses

-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
#-keep class com.actionbarsherlock.** { *; }
#-keep interface com.actionbarsherlock.** { *; }
# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn org.xmlpull.v1.**
-dontnote org.xmlpull.v1.**
-keep class org.xmlpull.** { *; }
-dontwarn okio.**
-keep class com.caverock.**
-dontwarn com.caverock.**
-keep class com.graphhopper.**
-keep class javax.xml.**
-dontwarn javax.xml.**
-keep class javax.imageio.**
-dontwarn javax.imageio.**
-keep class org.apache.xmlgraphics.**
-dontwarn org.apache.xmlgraphics.**
-keep class java.awt.**
-dontwarn java.awt.**
-keep class com.google.protobuf.**
-dontwarn com.google.protobuf.**
-keep class sun.misc.**
-dontwarn sun.misc.**
-keep class java.lang.management.**
-dontwarn java.lang.management.**
-keep class com.sun.management.**
-dontwarn com.sun.management.**
-keep class org.openstreetmap.**
-dontwarn org.openstreetmap.**
-keep class retrofit2.**


-dontwarn retrofit2.**
#-dontnote retrofit2.**
-dontwarn a.a.a.**

-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
-keep class com.google.inject.** { *; }
-dontwarn com.google.inject.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-keep class org.apache.james.mime4j.** { *; }
-dontwarn org.apache.james.mime4j.**
-keep class javax.inject.** { *; }
-dontwarn javax.inject.**
-keep class retrofit.** { *; }
-dontwarn retrofit.**

-dontwarn com.aerserv.sdk.adapter.**
-dontwarn kotlin.**



-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }
-keep class org.parceler.Parceler$$Parcels

-dontwarn java.nio.file.*
-dontwarn com.squareup.okhttp.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

-keepattributes Signature
-keepattributes Exceptions

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.adscendmedia.sdk.rest.model.** { *; }
-keep class com.adscendmedia.sdk.rest.response.** { *; }



-dontpreverify

-repackageclasses

-allowaccessmodification

-optimizations !code/simplification/arithmetic

-keepattributes *Annotation*

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class android.support.v7.** {
    public protected *;
}

-dontwarn com.google.android.gms.**
-dontwarn javax.xml.**

# AerServ SDK
-keep class com.aerserv.** { *; }
-keepclassmembers class com.aerserv.** { *; }

# For Moat
-dontwarn com.moat.**

# For Adcolony
-dontwarn android.webkit.**
-dontwarn com.adcolony.**
-keep class com.adcolony.sdk.* { *; }

# For Admob by Google
-keep class com.google.android.gms.ads.** { *; }

# For Applovin
-dontwarn com.applovin.**
-keep class com.applovin.** { *; }

# For AppNext
-keep class com.appnext.** { *; }
-dontwarn com.appnext.**

# For Amazon Publisher Services
-dontwarn com.amazon.device.ads.**
-keep class com.amazon.device.ads.** { *; }

# For Chartboost
-dontwarn com.chartboost.sdk.**
-keep class com.chartboost.sdk.** { *; }

# For Audience Network by Facebook
-dontwarn com.facebook.ads.**
-keep class com.facebook.ads.** { *; }

# For InMobi
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**
-dontwarn com.inmobi.**
-keep class com.inmobi.** { *; }

# For Millennial Media by AOL
-dontwarn com.millennialmedia.**
-keep class com.millennialmedia.** { *; }

# For Mopub by Twitter
-dontwarn com.mopub.**
-keep class com.mopub.** { *; }

# For MyTarget
-dontwarn com.my.target.**
-keep class com.my.target.** { *; }

-dontwarn com.aerserv.sdk.**

# For RhythmOne
-dontwarn com.rhythmone.ad.sdk.**
-keep class com.rhythmone.ad.sdk.** { *; }

# For Tremor
-dontwarn com.tremorvideo.sdk.**
-keep class com.tremorvideo.sdk.android.videoad.** { *; }
-dontwarn com.doubleverify.dvsdk.termor.**
-keep class com.doubleverify.dvsdk.termor.** { *; }

# For Unity Ads
-dontwarn com.unity3d.ads.**
-keep class com.unity3d.ads.** { *; }

# For Vungle
-dontwarn com.vungle.publisher.**
-keep class com.vungle.publisher.** { *; }

# For Flurry by Yahoo!
-dontwarn android.support.customtabs.**
-dontwarn com.flurry.android.**
-keep class com.flurry.** { *; }

