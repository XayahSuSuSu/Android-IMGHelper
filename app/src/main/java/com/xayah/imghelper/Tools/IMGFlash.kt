package com.xayah.imghelper.Tools

import android.util.Log
import com.topjohnwu.superuser.Shell

class IMGFlash() {
    companion object {
        fun boot(imgPath: String) {
            val IMGPartitionPath = Shell.su("ls -l /dev/block/by-name/boot").exec().out[0]
            Log.d("IMGFlash", IMGPartitionPath)
            val link = IMGPartitionPath.split("->")
            val IMGlink = link[1].trim()
            Log.d("IMGFlash", IMGlink)
            Shell.su("dd if=$imgPath of=$IMGlink").exec()
        }

        fun recovery(imgPath: String) {
            val IMGPartitionPath = Shell.su("ls -l /dev/block/by-name/recovery").exec().out[0]
            Log.d("IMGFlash", IMGPartitionPath)
            val link = IMGPartitionPath.split("->")
            val IMGlink = link[1].trim()
            Log.d("IMGFlash", IMGlink)
            Shell.su("dd if=$imgPath of=$IMGlink").exec()
        }

        fun dtbo(imgPath: String) {
            val IMGPartitionPath = Shell.su("ls -l /dev/block/by-name/dtbo").exec().out[0]
            Log.d("IMGFlash", IMGPartitionPath)
            val link = IMGPartitionPath.split("->")
            val IMGlink = link[1].trim()
            Log.d("IMGFlash", IMGlink)
            Shell.su("dd if=$imgPath of=$IMGlink").exec()
        }
    }
}