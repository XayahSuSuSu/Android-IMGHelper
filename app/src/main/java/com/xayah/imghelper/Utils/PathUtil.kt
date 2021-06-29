package com.xayah.imghelper.Utils

import android.content.Context
import android.os.Environment


class PathUtil(private val mContext: Context) {
    fun magiskPath(): String {
        return dataPath() + "/Magisk"
    }

    fun scriptsPath(): String {
        return dataPath() + "/scripts"
    }

    fun envPath(): String {
        return dataPath() + "/env"
    }

    fun dataPath(): String {
        return mContext.applicationContext.filesDir.toString()
    }

    fun outPath(): String {
        return Environment.getExternalStorageDirectory().path + "/IMGHelper"
    }
}