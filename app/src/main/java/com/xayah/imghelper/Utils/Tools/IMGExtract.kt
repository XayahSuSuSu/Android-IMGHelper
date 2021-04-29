package com.xayah.imghelper.Utils.Tools

import android.util.Log
import com.xayah.imghelper.Utils.CommandUtil

class IMGExtract(var cachePath: String) {
//    init {
//        CommandUtil.executeCommand(
//                "cp /vendor/etc/fstab.qcom $cachePath",
//                "/dev/block/bootdevice/by-name/",
//                true,
//                true
//        )
//    }

//    fun getPartition(partitionType: String): String {
//        var partitionPath = "-1"
//        val fstab_qcom = File(cachePath, "fstab.qcom").readText()
//        val fstab_qcom_list = fstab_qcom.split("\n")
//        var isUseful = false
//        for (eachLine in fstab_qcom_list) {
//            val partitionInfo = Regex("\\s+").split(eachLine)
//            if (isUseful && partitionInfo.size > 1) {
//                Log.d("IMGExtract", "分区位置:" + partitionInfo[0])
//                Log.d("IMGExtract", "分区类型:" + partitionInfo[1])
//                if ("/$partitionType" == partitionInfo[1]) {
//                    partitionPath = partitionInfo[0]
//                }
//            }
//            if (eachLine.contains("#<src>")) {
//                isUseful = true
//            }
//        }
//        return partitionPath
//    }

    fun boot(outPutPath: String) {
        val IMGPath = CommandUtil.executeCommand(
            "ls -l boot",
            "/dev/block/by-name",
            true,
            true
        )
        Log.d("IMGExtractActivity", IMGPath)
        val link = IMGPath.split("->")
        val IMGlink = link[1].trim()
        Log.d("IMGExtractActivity", IMGlink)
        CommandUtil.executeCommand(
            "dd if=$IMGlink of=$outPutPath/boot.img",
            "/dev/block/by-name",
            true,
            true
        )
    }

    fun recovery(outPutPath: String) {
        val IMGPath = CommandUtil.executeCommand(
            "ls -l recovery",
            "/dev/block/by-name",
            true,
            true
        )
        val link = IMGPath.split("->")
        val IMGlink = link[1].trim()
        Log.d("IMGExtractActivity", IMGlink)
        CommandUtil.executeCommand(
            "dd if=$IMGlink of=$outPutPath/recovery.img",
            "/dev/block/by-name",
            true,
            true
        )
    }

    fun dtbo(outPutPath: String) {
        val IMGPath = CommandUtil.executeCommand(
            "ls -l dtbo",
            "/dev/block/by-name",
            true,
            true
        )
        val link = IMGPath.split("->")
        val IMGlink = link[1].trim()
        Log.d("IMGExtractActivity", IMGlink)
        CommandUtil.executeCommand(
            "dd if=$IMGlink of=$outPutPath/dtbo.img",
            "/dev/block/by-name",
            true,
            true
        )
    }

}