// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("javahelp");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("javahelp")
//      }
//    }


#include <jni.h>

extern "C" jstring Java_com_javahelp_frontend_util_auth_SharedPreferencesAuthInformationProvider_getKey(JNIEnv* env, jobject thiz)
{
    return env->NewStringUTF("B374A26A71490437AA024E4FADD5B497");
}