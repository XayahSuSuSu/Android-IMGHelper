package com.xayah.imghelper.Activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.xayah.imghelper.R
import com.xayah.imghelper.Utils.CommandClassUtil
import com.xayah.imghelper.Utils.CommandUtil
import com.xayah.imghelper.Utils.CommandUtil.Companion.executeCommand
import com.xayah.imghelper.Utils.FileUtil
import com.xayah.imghelper.Utils.ContentUriUtil
import com.xayah.imghelper.Utils.Tools.Dtc
import com.xayah.imghelper.Utils.Tools.Mkdtimg
import com.xayah.imghelper.Utils.Tools.Unpack_bootimg


class MainActivity : AppCompatActivity() {
    lateinit var toolsPath: String
    lateinit var envPath: String
    lateinit var workingPath: String
    lateinit var outPath: String
    lateinit var fileUtil: FileUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolsPath = applicationContext.filesDir.toString() + "/tools/"
        envPath = applicationContext.filesDir.toString() + "/env/"
        workingPath = applicationContext.filesDir.toString()
        outPath = Environment.getExternalStorageDirectory().path + "/IMGHelper"
        fileUtil = FileUtil(this)
        prepareForTools()
        prepareForEnv()

        val main_cardview_imgextract: CardView = findViewById(R.id.main_cardview_imgextract)
        main_cardview_imgextract.setOnClickListener {
            val intent = Intent(this, IMGExtractActivity::class.java)
            startActivity(intent)
        }
//        val button: Button = findViewById
//        val unpackBootimg = Unpack_bootimg(toolsPath)
//        unpackBootimg.unpack("/sdcard/Download/boot.img", "$workingPath/boot_out")

    }

    private fun prepareForEnv() {
        val main_cardView_env: CardView = findViewById(R.id.main_cardView_env)
        val main_imageView_envCheck: ImageView = findViewById(R.id.main_imageView_envCheck)
        val main_textView_envTitle: TextView = findViewById(R.id.main_textView_envTitle)
        var toolNum = 0
        if (fileUtil.isFileExist("system/bin/dtc"))
            toolNum++
        if (fileUtil.isFileExist("system/bin/mkbootimg"))
            toolNum++
        if (fileUtil.isFileExist("system/bin/mkdtimg"))
            toolNum++
        if (fileUtil.isFileExist("system/bin/unpack_bootimg"))
            toolNum++
        Log.d("MainActivity", "toolNum: " + toolNum.toString())
        if (toolNum == 4) {
            runOnUiThread {
                main_imageView_envCheck.setImageResource(R.drawable.ic_check)
                main_textView_envTitle.setText("环境已配置")
                main_cardView_env.setCardBackgroundColor(Color.parseColor("#03d568"))
            }
        } else {
            runOnUiThread {
                main_imageView_envCheck.setImageResource(R.drawable.ic_cancel)
                main_textView_envTitle.setText("环境未配置")
                main_cardView_env.setCardBackgroundColor(Color.parseColor("#e72d2c"))
            }
            val builder = AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("环境尚未配置，是否立刻配置环境？")
                .setCancelable(true)
                .setPositiveButton("立即配置") { _: DialogInterface?, which: Int ->

                    val builder = AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("请选择配置方式:")
                        .setCancelable(true)
                        .setPositiveButton("Magisk(推荐)") { _: DialogInterface?, which: Int ->
                            Thread {
                                fileUtil.moveFileToEnvDir("module.prop")
                                fileUtil.mkDir("/data/adb/modules/IMGHelperEnv/")
                                fileUtil.mkDir("/data/adb/modules/IMGHelperEnv/system/")
                                fileUtil.mkDir("/data/adb/modules/IMGHelperEnv/system/bin/")
                                fileUtil.cpFiles(envPath + "module.prop", "/data/adb/modules/IMGHelperEnv/")
                                fileUtil.cpFiles(toolsPath + "dtc", "/data/adb/modules/IMGHelperEnv/system/bin/")
                                fileUtil.cpFiles(toolsPath + "mkbootimg", "/data/adb/modules/IMGHelperEnv/system/bin/")
                                fileUtil.cpFiles(toolsPath + "mkdtimg", "/data/adb/modules/IMGHelperEnv/system/bin/")
                                fileUtil.cpFiles(toolsPath + "unpack_bootimg", "/data/adb/modules/IMGHelperEnv/system/bin/")
                                prepareForEnv()
                            }.start()
                        }
                        .setNegativeButton("Root") { _: DialogInterface?, which: Int ->
                            Thread {
                                fileUtil.cpFiles(toolsPath + "dtc", "system/bin/")
                                fileUtil.cpFiles(toolsPath + "mkbootimg", "system/bin/")
                                fileUtil.cpFiles(toolsPath + "mkdtimg", "system/bin/")
                                fileUtil.cpFiles(toolsPath + "unpack_bootimg", "system/bin/")
                                prepareForEnv()
                            }.start()
                        }
                        .create()
                    builder.show()
                    builder.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(Color.parseColor("#f88e20"));
                    builder.getButton(DialogInterface.BUTTON_NEGATIVE)
                        .setTextColor(Color.RED);

//                    val mView: View = LayoutInflater.from(this)
//                        .inflate(R.layout.alertdialog_loading, null, false)
//                    val alertdialog_progressBar: ProgressBar = mView.findViewById(R.id.alertdialog_progressBar)
//                    val builder = AlertDialog.Builder(this)
//                        .setView(mView)
//                        .setTitle("提示")
//                        .setMessage("正在安装...")
//                        .setCancelable(false)
//                        .create()
//                    builder.show()
                }
                .setNegativeButton("下次") { _: DialogInterface?, which: Int -> }
                .create()
            builder.show()
            builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            builder.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.BLUE);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    Log.d("MainActivity", "文件路径：" + ContentUriUtil.getPath(this, uri))
                    val unpackBootimg = Unpack_bootimg(toolsPath)
                    unpackBootimg.unpack(
                        ContentUriUtil.getPath(this, uri)!!,
                        "$workingPath/working/unpack_bootimg/out"
                    )
                }
            }
            if (requestCode == 2) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    Log.d("MainActivity", "文件路径：" + ContentUriUtil.getPath(this, uri))
                    val mkdtimg = Mkdtimg(toolsPath)
                    mkdtimg.dump(
                        ContentUriUtil.getPath(this, uri)!!,
                        "$workingPath/working/mkdtimg/dtb/dtb"
                    )
                    val dtb = executeCommand(
                        "find dtb.*",
                        "$workingPath/working/mkdtimg/dtb/",
                        true,
                        true
                    )
//                    val dtc = Dtc(toolsPath)
//                    dtc.dtb2Dts(
//                        "$workingPath/working/mkdtimg/dtb/dtb.2",
//                        "$workingPath/working/mkdtimg/dts/dts.2")
                    for (eachOne in dtb.split("\n")) {
                        if (eachOne != "") {
                            Log.d("MainActivity", eachOne + "\n")
                            val index = eachOne.split(".")
                            val dtc = Dtc(toolsPath)
                            dtc.dtb2Dts(
                                "$workingPath/working/mkdtimg/dtb/$eachOne",
                                "dts.${index[1]}",
                                "$workingPath/working/mkdtimg/dts/dts.${index[1]}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun prepareForTools() {
        fileUtil.mkDir(workingPath)
        fileUtil.mkDir(outPath)
        fileUtil.mkDir("$outPath/extract")
        fileUtil.mkDir("$workingPath/cache")
        fileUtil.mkDir("$workingPath/working")
        fileUtil.mkDir("$workingPath/working/dtc")
        fileUtil.mkDir("$workingPath/working/mkdtimg")
        fileUtil.mkDir("$workingPath/working/mkdtimg/dtb")
        fileUtil.mkDir("$workingPath/working/mkdtimg/dts")
        fileUtil.mkDir("$workingPath/working/mkbootimg")
        fileUtil.mkDir("$workingPath/working/unpack_bootimg")
        fileUtil.checkToolsDirExist("tools")
        fileUtil.checkToolsDirExist("env")
        if (!fileUtil.ifToolsExist("dtc")) {
            fileUtil.moveToolsToFileDir("dtc")
            executeCommand("chmod 777 dtc", toolsPath, true, true)
            Log.d("MainActivity", "dtc不存在，已转存")
        } else {
            Log.d("MainActivity", "dtc已存在")
        }
        if (!fileUtil.ifToolsExist("mkdtimg")) {
            fileUtil.moveToolsToFileDir("mkdtimg")
            executeCommand("chmod 777 mkdtimg", toolsPath, true, true)
            Log.d("MainActivity", "mkdtimg不存在，已转存")
        } else {
            Log.d("MainActivity", "mkdtimg已存在")
        }
        if (!fileUtil.ifToolsExist("mkbootimg")) {
            fileUtil.moveToolsToFileDir("mkbootimg")
            executeCommand("chmod 777 mkbootimg", toolsPath, true, true)
            Log.d("MainActivity", "mkbootimg不存在，已转存")
        } else {
            Log.d("MainActivity", "mkbootimg已存在")
        }
        if (!fileUtil.ifToolsExist("unpack_bootimg")) {
            fileUtil.moveToolsToFileDir("unpack_bootimg")
            executeCommand("chmod 777 unpack_bootimg", toolsPath, true, true)
            Log.d("MainActivity", "unpack_bootimg不存在，已转存")
        } else {
            Log.d("MainActivity", "unpack_bootimg已存在")
        }
    }
}