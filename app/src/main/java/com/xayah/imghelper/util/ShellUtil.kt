package com.xayah.imghelper.util

import com.topjohnwu.superuser.Shell

class ShellUtil {
    companion object {
        fun ls(path: String): Boolean {
            return Shell.su("ls -i $path").exec().isSuccess
        }

        fun mkdir(path: String): Boolean {
            return Shell.su("mkdir -p $path").exec().isSuccess
        }

        fun unzip(filePath: String, outPath: String) {
            if (mkdir(outPath))
                Shell.su("unzip $filePath -d $outPath").exec()
        }
    }
}