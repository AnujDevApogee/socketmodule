package com.apogee.socketlib.utils

import android.util.Log

object UtilsFiles {

    fun createLogCat(tag: String, msg: String) {
        Log.i(tag, "createLogCat: $msg")
    }
//    fun <T> toJson(t: T): String {
//        return Gson().toJson(t)
//    }


    //    inline fun <reified T> fromJson(str: String): T {
//        return Gson().fromJson(str, T::class.java)
//    }
    fun checkValue(str: String?): Boolean {
        return str.isNullOrEmpty() || str.isBlank() || str == "null"
    }
}
