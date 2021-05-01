package com.xayah.imghelper.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.xayah.imghelper.R
import com.xayah.imghelper.Utils.CommandUtil
import com.xayah.imghelper.Utils.CommandUtil.Companion.executeCommand
import com.xayah.imghelper.Utils.DialogUtil
import com.xayah.imghelper.Utils.FileUtil


class MainActivity : AppCompatActivity() {
    lateinit var toolsPath: String
    lateinit var envPath: String
    lateinit var workingPath: String
    lateinit var outPath: String
    lateinit var fileUtil: FileUtil
    val dialogUtil = DialogUtil(this)

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

        val main_cardview_imgflash: CardView = findViewById(R.id.main_cardview_imgflash)
        main_cardview_imgflash.setOnClickListener {
            val intent = Intent(this, IMGFlashActivity::class.java)
            startActivity(intent)
        }

        val main_cardview_dtboPatch: CardView = findViewById(R.id.main_cardview_dtboPatch)
        main_cardview_dtboPatch.setOnClickListener {
            val intent = Intent(this, DtboPatchActivity::class.java)
            startActivity(intent)
        }

        val main_cardview_imgunpack: CardView = findViewById(R.id.main_cardview_imgunpack)
        main_cardview_imgunpack.setOnClickListener {
            dialogUtil.createCommonDialog("敬请期待！", {}, {})
        }

        val main_cardview_imgpack: CardView = findViewById(R.id.main_cardview_imgpack)
        main_cardview_imgpack.setOnClickListener {
            dialogUtil.createCommonDialog("敬请期待！", {}, {})
        }

        val main_cardview_stock: CardView = findViewById(R.id.main_cardview_stock)
        main_cardview_stock.setOnClickListener {
            dialogUtil.createCommonDialog("敬请期待！", {}, {})
        }

        val main_cardview_about: CardView = findViewById(R.id.main_cardview_about)
        main_cardview_about.setOnClickListener {
            dialogUtil.createCommonDialog("敬请期待！", {}, {})
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
                dialogUtil.createCommonDialog("环境尚未配置，是否立刻配置环境？", {
                    dialogUtil.createCustomButtonDialog("请选择配置方式:", "Magisk(推荐)", "Root", {
                        Thread {
                            fileUtil.moveFileToEnvDir("module.prop")
                            fileUtil.mkDir("/data/adb/modules/IMGHelperEnv/")
                            fileUtil.mkDir("/data/adb/modules/IMGHelperEnv/system/")
                            fileUtil.mkDir("/data/adb/modules/IMGHelperEnv/system/bin/")
                            fileUtil.cpFiles(
                                envPath + "module.prop",
                                "/data/adb/modules/IMGHelperEnv/"
                            )
                            fileUtil.cpFiles(
                                toolsPath + "dtc",
                                "/data/adb/modules/IMGHelperEnv/system/bin/"
                            )
                            fileUtil.cpFiles(
                                toolsPath + "mkbootimg",
                                "/data/adb/modules/IMGHelperEnv/system/bin/"
                            )
                            fileUtil.cpFiles(
                                toolsPath + "mkdtimg",
                                "/data/adb/modules/IMGHelperEnv/system/bin/"
                            )
                            fileUtil.cpFiles(
                                toolsPath + "unpack_bootimg",
                                "/data/adb/modules/IMGHelperEnv/system/bin/"
                            )
                            runOnUiThread {
                                dialogUtil.createCommonDialog("安装成功！是否立刻重启生效?", {
                                    CommandUtil.executeCommand(
                                        "reboot",
                                        "/system/bin/",
                                        true,
                                        true
                                    )
                                }, {
                                })
                            }
                        }.start()
                    }, {
                        Thread {
                            fileUtil.cpFiles(toolsPath + "dtc", "system/bin/")
                            fileUtil.cpFiles(toolsPath + "mkbootimg", "system/bin/")
                            fileUtil.cpFiles(toolsPath + "mkdtimg", "system/bin/")
                            fileUtil.cpFiles(
                                toolsPath + "unpack_bootimg",
                                "system/bin/"
                            )
                            prepareForEnv()
                        }.start()
                    })
                }, {})
            }

        }
    }

    private fun prepareForTools() {
        val mDir = mutableListOf<String>()
        mDir.add(workingPath)
        mDir.add(outPath)
        mDir.add("$outPath/extract")
        mDir.add("$workingPath/cache")
        mDir.add("$workingPath/working")
        mDir.add("$workingPath/working/dtc")
        mDir.add("$workingPath/working/mkdtimg")
        mDir.add("$workingPath/working/mkdtimg/dtb")
        mDir.add("$workingPath/working/mkdtimg/dts")
        mDir.add("$workingPath/working/mkbootimg")
        mDir.add("$workingPath/working/unpack_bootimg")
        mDir.add(outPath)
        mDir.add(outPath)
        fileUtil.mkDirArr(mDir)
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
        if (!fileUtil.ifFileExist("Patch.sh","scripts")) {
            fileUtil.moveFileToCertainDir("Patch.sh","scripts","Scripts")
//            executeCommand("chmod 777 Patch.sh", toolsPath, true, true)
            Log.d("MainActivity", "Patch.sh不存在，已转存")
        } else {
            Log.d("MainActivity", "Patch.sh已存在")
        }
    }
}