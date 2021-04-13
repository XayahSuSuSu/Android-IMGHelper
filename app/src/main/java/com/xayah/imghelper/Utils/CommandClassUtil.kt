package com.xayah.imghelper.Utils

import android.util.Log
import java.io.*

class CommandClassUtil {
    lateinit var process: Process
    lateinit var os: DataOutputStream
    lateinit var osReader: BufferedReader
    lateinit var osErrorReader: BufferedReader
    var shellMessage = ""
    var errorMessage = ""
    fun startCommandService(isRoot: Boolean): Boolean {
        process = Runtime.getRuntime().exec(if (isRoot) "su" else "sh")
        os = DataOutputStream(process.outputStream)
        osReader = BufferedReader(InputStreamReader(process.inputStream))
        osErrorReader = BufferedReader(InputStreamReader(process.errorStream))
        //检查是否获取到 root 权限
        if (isRoot && !checkRootPermissionInProcess(
                os,
                process.inputStream
            )
        ) {
            return false
        }
        return true
    }

    fun executeCommand(command: String): String {
        Log.d("mCommand", "输入指令 : $command")
        val mReturn = readCommandResult(os, process.inputStream, command)
        Log.d("mCommand", "输出始---------------------------------------------")
        Log.d("mCommand", "$mReturn")
        Log.d("mCommand", "输出尾---------------------------------------------")
        return mReturn
    }

    fun stopCommandService() {
        try {
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            osReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            osErrorReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        process.destroy()
    }

    //读取执行命令后返回的信息
    @Throws(IOException::class)
    private fun readOSMessage(messageReader: BufferedReader): String {
        val content = java.lang.StringBuilder()
        var lineString: String?
//            Log.d("mCommand", "输出始---------------------------------------------")
        while (messageReader.readLine().also { lineString = it } != null) {
//                Log.d("mCommand", "$lineString")
            content.append(lineString).append("\n")
        }
//            Log.d("mCommand", "输出尾---------------------------------------------")
        return content.toString()
    }

    //用 id 命令检查是否获取到 root 权限
    @Throws(IOException::class)
    private fun checkRootPermissionInProcess(
        os: DataOutputStream,
        osReader: InputStream
    ): Boolean {
        val currentUid = readCommandResult(os, osReader, "id")
//            Log.d("mCommand", "currentUid : $currentUid")
        return if (currentUid.contains("uid=0")) {
            Log.d("mCommand", "ROOT: Root access granted")
            true
        } else {
            Log.d("mCommand", "ROOT: Root access rejected")
            false
        }
    }

    //执行一个命令，并获得该命令的返回信息
    @Throws(IOException::class)
    private fun readCommandResult(
        os: DataOutputStream,
        `in`: InputStream,
        command: String
    ): String {
        os.writeBytes(
            """
            $command
            
            """.trimIndent()
        )
        os.flush()
        return readCommandResult(`in`)
    }

    //读取命令返回信息
    @Throws(IOException::class)
    private fun readCommandResult(`in`: InputStream): String {
        val result = java.lang.StringBuilder()

        var available = 1
        while (available > 0) {
            val b = `in`.read()
            result.append(b.toChar())
            available = `in`.available()
        }
        return result.toString()
    }
}