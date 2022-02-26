package com.xayah.imghelper.util

import android.content.Context

class Path {
    companion object {
        fun getExternalFilesDir(mContext: Context): String {
            return mContext.filesDir.path
        }
    }
}