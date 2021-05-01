package com.xayah.imghelper.Tools

import android.util.Log
import com.topjohnwu.superuser.Shell

class IMGExtract() {
    companion object {
        fun boot(outPutPath: String) {
            val IMGPath = Shell.su("ls -l /dev/block/by-name/boot").exec().out[0]
            Log.d("IMGExtract", IMGPath)
            val link = IMGPath.split("->")
            val IMGlink = link[1].trim()
            Log.d("IMGExtract", IMGlink)
            Shell.su("dd if=$IMGlink of=$outPutPath/boot.img").exec()
        }

        fun recovery(outPutPath: String) {
            val IMGPath = Shell.su("ls -l /dev/block/by-name/recovery").exec().out[0]
            Log.d("IMGExtract", IMGPath)
            val link = IMGPath.split("->")
            val IMGlink = link[1].trim()
            Log.d("IMGExtract", IMGlink)
            Shell.su("dd if=$IMGlink of=$outPutPath/recovery.img").exec()
        }

        fun dtbo(outPutPath: String) {
            val IMGPath = Shell.su("ls -l /dev/block/by-name/dtbo").exec().out[0]
            Log.d("IMGExtract", IMGPath)
            val link = IMGPath.split("->")
            val IMGlink = link[1].trim()
            Log.d("IMGExtract", IMGlink)
            Shell.su("dd if=$IMGlink of=$outPutPath/dtbo.img").exec()
        }
    }
}