package com.xayah.imghelper.Utils

import android.util.Log
import java.io.*

class CommandArrayUtil {
    companion object {
        fun executeCommand(
            commandArr: MutableList<String>,
            path: String,
            isRoot: Boolean,
            checkPermission: Boolean
        ): String {
            var process: Process? = null
            var os: DataOutputStream? = null
            var osReader: BufferedReader? = null
            var osErrorReader: BufferedReader? = null
            var shellMessage = ""
            var errorMessage = ""
            try {
                //如果需要 root 权限则执行 su 命令，否则执行 sh 命令
                process = Runtime.getRuntime().exec(if (isRoot) "su" else "sh")
                os = DataOutputStream(process.outputStream)
                osReader = BufferedReader(InputStreamReader(process.inputStream))
                osErrorReader = BufferedReader(InputStreamReader(process.errorStream))

                //检查是否获取到 root 权限
                if (checkPermission && isRoot && !checkRootPermissionInProcess(
                        os,
                        process.inputStream
                    )
                ) {
                    return "Permission denied"
                }
                os.writeBytes("cd $path\n");
                for (i in commandArr){
                    os.writeBytes("$i\n");
                    os.flush()
                    Log.d("mCommand", "输入指令 : $i")
                }
                os.writeBytes("exit\n")
                os.flush()
                val processResult: Int
                shellMessage = readOSMessage(osReader)
                errorMessage = readOSMessage(osErrorReader)
//                processResult = process.waitFor()
//                Log.d("mCommand", "processResult : $processResult")
                Log.d("mCommand", "输出始---------------------------------------------")
                Log.d("mCommand", "$shellMessage")
                Log.d("mCommand", "输出尾---------------------------------------------")
                Log.d("mCommand", "errorMessage : $errorMessage")
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                if (os != null) {
                    try {
                        os.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (osReader != null) {
                    try {
                        osReader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (osErrorReader != null) {
                    try {
                        osErrorReader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                process?.destroy()
            }
            if (shellMessage.isBlank()) {
                return errorMessage
            } else {
                return shellMessage
            }
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
}