package com.xayah.imghelper.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Tool {
    companion object {
        fun extractAssets(mContext: Context, assetsName: String) {
            try {
                val assets = File(Path.getExternalFilesDir(mContext), assetsName)
                if (!assets.exists()) {
                    val outStream = FileOutputStream(assets)
                    val inputStream = mContext.resources.assets.open(assetsName)
                    val buffer = ByteArray(1024)
                    var byteCount: Int
                    while (inputStream.read(buffer).also { byteCount = it } != -1) {
                        outStream.write(buffer, 0, byteCount)
                    }
                    outStream.flush()
                    inputStream.close()
                    outStream.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}