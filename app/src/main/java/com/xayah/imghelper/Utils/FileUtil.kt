package com.xayah.imghelper.Utils

import android.content.Context
import android.util.Log
import com.topjohnwu.superuser.Shell
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class FileUtil(private val context: Context) {

    fun isFileExist(path: String): Boolean {
        return Shell.sh("ls $path").exec().out.isNotEmpty()
    }

    fun ifToolsExist(fileName: String): Boolean {
        val file = File(context.filesDir.toString() + "/tools/" + fileName)
        return file.exists()
    }

    fun ifFileExist(fileName: String, dirName: String): Boolean {
        val file = File(context.filesDir.toString() + "/$dirName/" + fileName)
        return file.exists()
    }


    fun chmod(path: String, isRoot: Boolean) {
        if (isRoot) {
            Shell.su("chmod -R 777 $path").exec()
        } else {
            Shell.sh("chmod -R 777 $path").exec()
        }
    }

    fun mkDir(path: String, isRoot: Boolean) {
        if (isRoot) {
            Shell.su("mkdir $path").exec()
            chmod(path, true)
        } else {
            Shell.sh("mkdir $path").exec()
            chmod(path, false)
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

    fun cpFiles(pathBefore: String, pathAfter: String, isRoot: Boolean) {
        if (isRoot) {
            Shell.su("cp $pathBefore $pathAfter").exec()
            chmod(pathAfter, true)
        } else {
            Shell.sh("cp $pathBefore $pathAfter").exec()
            chmod(pathAfter, false)
        }
    }

    fun cpDirAll(pathBefore: String, pathAfter: String) {
        Shell.su("cp $pathBefore/* -R $pathAfter").exec()
        chmod(pathAfter, true)
    }

    fun checkToolsDirExist(dirName: String) {
        val file = File(context.filesDir.toString() + "/" + dirName)
        if (!file.exists()) {
            file.mkdir()
        }
    }

    fun moveAssetsFileToDir(assetsFilePath: String, fileName: String, dir: String) {
        try {
            val file = File(dir, fileName)
            if (!file.exists()) {
                val outStream = FileOutputStream(file)
                val inputStream = context.resources.assets.open(assetsFilePath)
                val buffer = ByteArray(1024)
                var byteCount: Int
                while (inputStream.read(buffer).also { byteCount = it } != -1) { // 循环从输入流读取
                    // buffer字节
                    outStream.write(buffer, 0, byteCount) // 将读取的输入流写入到输出流
                }
                outStream.flush() // 刷新缓冲区
                inputStream.close()
                outStream.close()
                chmod("$dir/$fileName", false)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun moveFileToCertainDir(fileName: String, dirName: String, assetsDirName: String) {
        try {
            checkToolsDirExist(dirName)
            val file = File(context.filesDir.toString() + "/$dirName/", fileName)
            val outStream = FileOutputStream(file)
            val inputStream = context.resources.assets.open("$assetsDirName/$fileName")
            val buffer = ByteArray(1024)
            var byteCount: Int
            while (inputStream.read(buffer).also { byteCount = it } != -1) { // 循环从输入流读取
                // buffer字节
                outStream.write(buffer, 0, byteCount) // 将读取的输入流写入到输出流
            }
            outStream.flush() // 刷新缓冲区
            inputStream.close()
            outStream.close()
            Log.d("FileUtil", "moveFileToCertainDir: ")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun checkToolsUpdate(updatePath: String): Boolean {
        val checkModuleBody = Shell.su("cat /data/adb/modules/IMGHelperEnv/module.prop").exec().out
        val updateModuleBody = Shell.su("cat ${updatePath}/module.prop").exec().out

        val localVersion = checkModuleBody[2].split("=")[1]
        val updateVersion = updateModuleBody[2].split("=")[1]
        Log.d("FileUtil", localVersion)
        Log.d("FileUtil", updateVersion)
        if (localVersion != updateVersion)
            return true
        return false
    }

    fun rm(path: String) {
        Shell.su("rm $path -R").exec()
    }

    fun unzip(filePath: String) {
        Shell.su("unzip $filePath/Magisk.zip -d $filePath/Magisk").exec()
        Log.d("FileUtil", "unzip $filePath -d $filePath/Magisk")
    }

}