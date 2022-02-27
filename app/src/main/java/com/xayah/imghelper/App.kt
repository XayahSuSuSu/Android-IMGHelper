package com.xayah.imghelper

import android.app.Application
import android.content.Context
import com.topjohnwu.superuser.Shell
import com.xayah.imghelper.util.Path
import com.xayah.imghelper.util.ShellUtil
import com.xayah.imghelper.util.Tool

class App : Application() {
    companion object {
        init {
            Shell.enableVerboseLogging = BuildConfig.DEBUG;
            Shell.setDefaultBuilder(
                Shell.Builder.create()
                    .setFlags(Shell.FLAG_MOUNT_MASTER)
                    .setTimeout(10)
                    .setInitializers(ScriptInitializer::class.java)
            )
        }
    }

    class ScriptInitializer : Shell.Initializer() {
        override fun onInit(context: Context, shell: Shell): Boolean {
            shell.newJob()
                .add("export PATH=${Path.getExternalFilesDir(context)}/bin:${'$'}PATH")
                .add("source ${Path.getExternalFilesDir(context)}/util_functions.sh")
                .add("mount_partitions")
                .exec()
            return true
        }
    }

    override fun onCreate() {
        super.onCreate()
        Tool.extractAssets(this, "Bin.zip")
        Tool.extractAssets(this, "Patch.sh")
        Tool.extractAssets(this, "util_functions.sh")

        if (!ShellUtil.ls("${Path.getExternalFilesDir(this)}/bin"))
            ShellUtil.unzip(
                "${Path.getExternalFilesDir(this)}/Bin.zip",
                "${Path.getExternalFilesDir(this)}/bin"
            )
    }
}