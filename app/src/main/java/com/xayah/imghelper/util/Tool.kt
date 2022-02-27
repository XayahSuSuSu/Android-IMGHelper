package com.xayah.imghelper.util

import android.content.Context
import com.topjohnwu.superuser.Shell
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

        fun patchDTBO(
            mContext: Context,
            refreshRate: String,
            imgPath: String,
            outPutDir: String
        ): Boolean {
            return Shell.su(
                "cd ${Path.getExternalFilesDir(mContext)}; sh ${
                    Path.getExternalFilesDir(
                        mContext
                    )
                }/Patch.sh $imgPath $outPutDir $refreshRate"
            ).exec().isSuccess
        }

        fun findBlock(partName: String): String {
            val out = Shell.su("echo \$(find_block ${partName}\$SLOT)").exec().out
            return if (out.size == 0) "" else out[0]
        }

        fun flashImage(image: String, block: String) {
            Shell.su("flash_image $image $block").exec()
        }

        fun extractImage(block: String, outPut: String): Boolean {
            return Shell.su("dd if=$block of=$outPut").exec().isSuccess
        }

        fun unpackBoot(boot: String, outPut: String): Boolean {
            return Shell.su("cd $outPut; magiskboot unpack $boot").exec().isSuccess
        }

        fun packBoot(boot: String, outPut: String): Boolean {
            return Shell.su("cd $outPut; magiskboot repack $boot").exec().isSuccess
        }
    }
}