package com.xayah.imghelper.Utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class FileUtil(var context: Context) {

    fun isFileExist(path: String): Boolean {
        val mReturn = CommandUtil.executeCommand(
            "ls $path",
            "/",
            true,
            true
        )
        Log.d("FileUtil", "checkFileExist:$mReturn")
        if (mReturn.contains("No such file or directory")) {
            return false
        } else return mReturn.contains(path)
    }

    fun ifToolsExist(fileName: String): Boolean {
        val file = File(context.filesDir.toString() + "/tools/" + fileName)
        return file.exists()
    }

    fun mkDir(path: String) {
        val file = File(path)
        if (!file.exists()) {
            CommandUtil.executeCommand(
                "mkdir $path",
                "/system/bin/",
                true,
                true
            )
        }
    }

    fun mkDirArr(path: MutableList<String>) {
        var index = 0
        for (i in path) {
            path.set(index, "mkdir $i")
            index++
        }
        CommandArrayUtil.executeCommand(
                path,
                "/system/bin/",
                true,
                true
        )
    }

    fun mvFiles(pathBefore: String, pathAfter: String) {
        CommandUtil.executeCommand(
            "mv $pathBefore $pathAfter",
            "/",
            true,
            true
        )


    }

    fun cpFiles(pathBefore: String, pathAfter: String) {
        CommandUtil.executeCommand(
            "cp $pathBefore $pathAfter",
            "/",
            true,
            true
        )
    }

    fun checkToolsDirExist(dirName: String) {
        val file = File(context.filesDir.toString() + "/" + dirName)
        if (!file.exists()) {
            file.mkdir()
        }
    }

    fun moveToolsToFileDir(fileName: String) {
        try {
            val file = File(context.filesDir.toString() + "/tools/", fileName)
            val outStream = FileOutputStream(file)
            val inputStream = context.resources.assets.open("AARCH64/$fileName")
            val buffer = ByteArray(1024)
            var byteCount: Int
            while (inputStream.read(buffer).also { byteCount = it } != -1) { // 循环从输入流读取
                // buffer字节
                outStream.write(buffer, 0, byteCount) // 将读取的输入流写入到输出流
            }
            outStream.flush() // 刷新缓冲区
            inputStream.close()
            outStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun moveFileToEnvDir(fileName: String) {
        try {
            val file = File(context.filesDir.toString() + "/env/", fileName)
            val outStream = FileOutputStream(file)
            val inputStream = context.resources.assets.open("Magisk/$fileName")
            val buffer = ByteArray(1024)
            var byteCount: Int
            while (inputStream.read(buffer).also { byteCount = it } != -1) { // 循环从输入流读取
                // buffer字节
                outStream.write(buffer, 0, byteCount) // 将读取的输入流写入到输出流
            }
            outStream.flush() // 刷新缓冲区
            inputStream.close()
            outStream.close()
            Log.d("TAG", "moveFileToEnvDir: ")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}