package com.xayah.imghelper.Utils.Tools

import android.util.Log
import com.xayah.imghelper.Utils.CommandUtil

class IMGFlash() {
    companion object{
        fun boot(imgPath: String) {
            val IMGPartitionPath = CommandUtil.executeCommand(
                "ls -l boot",
                "/dev/block/by-name",
                true,
                true
            )
            Log.d("IMGExtractActivity", IMGPartitionPath)
            val link = IMGPartitionPath.split("->")
            val IMGlink = link[1].trim()
            Log.d("IMGExtractActivity", IMGlink)
            CommandUtil.executeCommand(
                "dd if=$imgPath of=$IMGlink",
                "/dev/block/by-name",
                true,
                true
            )
        }

        fun recovery(imgPath: String) {
            val IMGPartitionPath = CommandUtil.executeCommand(
                "ls -l recovery",
                "/dev/block/by-name",
                true,
                true
            )
            Log.d("IMGExtractActivity", IMGPartitionPath)
            val link = IMGPartitionPath.split("->")
            val IMGlink = link[1].trim()
            Log.d("IMGExtractActivity", IMGlink)
            CommandUtil.executeCommand(
                "dd if=$imgPath of=$IMGlink",
                "/dev/block/by-name",
                true,
                true
            )
        }
        fun dtbo(imgPath: String) {
            val IMGPartitionPath = CommandUtil.executeCommand(
                "ls -l dtbo",
                "/dev/block/by-name",
                true,
                true
            )
            Log.d("IMGExtractActivity", IMGPartitionPath)
            val link = IMGPartitionPath.split("->")
            val IMGlink = link[1].trim()
            Log.d("IMGExtractActivity", IMGlink)
            CommandUtil.executeCommand(
                "dd if=$imgPath of=$IMGlink",
                "/dev/block/by-name",
                true,
                true
            )
        }
    }
}