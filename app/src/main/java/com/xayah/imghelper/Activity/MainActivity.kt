package com.xayah.imghelper.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.topjohnwu.superuser.Shell
import com.xayah.imghelper.R
import com.xayah.imghelper.Utils.DialogUtil
import com.xayah.imghelper.Utils.FileUtil
import com.xayah.imghelper.Utils.PathUtil


class MainActivity : AppCompatActivity() {
    // -------------------Component-------------------
    lateinit var main_cardView_env: CardView
    lateinit var main_cardview_imgextract: CardView
    lateinit var main_cardview_imgflash: CardView
    lateinit var main_cardview_dtboPatch: CardView
    lateinit var main_cardview_imgunpack: CardView
    lateinit var main_cardview_imgpack: CardView
    lateinit var main_cardview_stock: CardView
    lateinit var main_cardview_about: CardView
    lateinit var main_imageView_envCheck: ImageView
    lateinit var main_textView_envTitle: TextView

    // -------------------Utils-------------------
    val fileUtil = FileUtil(this)
    val dialogUtil = DialogUtil(this)
    val pathUtil = PathUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindView() // 绑定组件
        setListener() // 设置监听器
        init() // 初始化环境
    }

    private fun setListener() {
        main_cardview_imgextract.setOnClickListener {
            val intent = Intent(this, IMGExtractActivity::class.java)
            startActivity(intent)
        }

        main_cardview_imgflash.setOnClickListener {
            val intent = Intent(this, IMGFlashActivity::class.java)
            startActivity(intent)
        }

        main_cardview_dtboPatch.setOnClickListener {
            val intent = Intent(this, DtboPatchActivity::class.java)
            startActivity(intent)
        }

        main_cardview_imgunpack.setOnClickListener {
            dialogUtil.createCommonDialog("敬请期待！", {}, {})
        }

        main_cardview_imgpack.setOnClickListener {
            dialogUtil.createCommonDialog("敬请期待！", {}, {})
        }

        main_cardview_stock.setOnClickListener {
            dialogUtil.createCommonDialog("敬请期待！", {}, {})
        }

        main_cardview_about.setOnClickListener {
            dialogUtil.createCommonDialog("敬请期待！", {}, {})
        }
    }

    private fun bindView() {
        main_cardview_imgextract = findViewById(R.id.main_cardview_imgextract)
        main_cardview_imgflash = findViewById(R.id.main_cardview_imgflash)
        main_cardview_dtboPatch = findViewById(R.id.main_cardview_dtboPatch)
        main_cardview_imgunpack = findViewById(R.id.main_cardview_imgunpack)
        main_cardview_imgpack = findViewById(R.id.main_cardview_imgpack)
        main_cardview_stock = findViewById(R.id.main_cardview_stock)
        main_cardview_about = findViewById(R.id.main_cardview_about)
        main_cardView_env = findViewById(R.id.main_cardView_env)
        main_imageView_envCheck = findViewById(R.id.main_imageView_envCheck)
        main_textView_envTitle = findViewById(R.id.main_textView_envTitle)
    }

    private fun init() {
        fileUtil.mkDir(pathUtil.dataPath(), false) // 创建data目录
        fileUtil.mkDir(pathUtil.toolsPath(), false) // 创建工具目录
        fileUtil.mkDir(pathUtil.scriptsPath(), false) // 创建脚本目录
        fileUtil.mkDir(pathUtil.envPath(), false) // 创建环境目录

        fileUtil.mkDir(pathUtil.outPath(), false) // 创建输出目录
        fileUtil.mkDir("${pathUtil.outPath()}/extract", false) // 创建输出目录下的提取目录
        // 释放assets资源
        fileUtil.moveAssetsFileToDir("AARCH64/dtc", "dtc", pathUtil.toolsPath())
        fileUtil.moveAssetsFileToDir("AARCH64/mkdtimg", "mkdtimg", pathUtil.toolsPath())
        fileUtil.moveAssetsFileToDir("AARCH64/mkbootimg", "mkbootimg", pathUtil.toolsPath())
        fileUtil.moveAssetsFileToDir(
            "AARCH64/unpack_bootimg",
            "unpack_bootimg",
            pathUtil.toolsPath()
        )
        fileUtil.moveAssetsFileToDir("Magisk/module.prop", "module.prop", pathUtil.envPath())
        fileUtil.moveAssetsFileToDir("Scripts/Patch.sh", "Patch.sh", pathUtil.scriptsPath())
        // 检查环境是否配置
        if (fileUtil.isFileExist("system/bin/dtc") &&
            fileUtil.isFileExist("system/bin/mkbootimg") &&
            fileUtil.isFileExist("system/bin/mkdtimg") &&
            fileUtil.isFileExist("system/bin/unpack_bootimg")
        ) {
            // 环境已配置
            main_imageView_envCheck.setImageResource(R.drawable.ic_check)
            main_textView_envTitle.setText("环境已配置")
            main_cardView_env.setCardBackgroundColor(Color.parseColor("#03d568"))
        } else {
            // 环境未配置
            main_imageView_envCheck.setImageResource(R.drawable.ic_cancel)
            main_textView_envTitle.setText("环境未配置")
            main_cardView_env.setCardBackgroundColor(Color.parseColor("#e72d2c"))
            dialogUtil.createCommonDialog("环境尚未配置，是否立刻配置环境？", {
                dialogUtil.createCustomButtonDialog("请选择配置方式:", "Magisk(推荐)", "Root", {
                    Thread {
                        fileUtil.mkDir("/data/adb/modules/IMGHelperEnv/", true)
                        fileUtil.mkDir("/data/adb/modules/IMGHelperEnv/system/", true)
                        fileUtil.mkDir("/data/adb/modules/IMGHelperEnv/system/bin/", true)
                        fileUtil.cpFiles(
                            pathUtil.envPath() + "module.prop",
                            "/data/adb/modules/IMGHelperEnv/", true
                        )
                        fileUtil.cpFiles(
                            pathUtil.toolsPath() + "dtc",
                            "/data/adb/modules/IMGHelperEnv/system/bin/", true
                        )
                        fileUtil.cpFiles(
                            pathUtil.toolsPath() + "mkbootimg",
                            "/data/adb/modules/IMGHelperEnv/system/bin/", true
                        )
                        fileUtil.cpFiles(
                            pathUtil.toolsPath() + "mkdtimg",
                            "/data/adb/modules/IMGHelperEnv/system/bin/", true
                        )
                        fileUtil.cpFiles(
                            pathUtil.toolsPath() + "unpack_bootimg",
                            "/data/adb/modules/IMGHelperEnv/system/bin/", true
                        )
                        runOnUiThread {
                            dialogUtil.createCommonDialog("安装成功！是否立刻重启生效?", {
                                Shell.su("reboot").exec()
                            }, {
                            })
                        }
                    }.start()
                }, {
                    Thread {
                        fileUtil.cpFiles(pathUtil.toolsPath() + "dtc", "system/bin/", true)
                        fileUtil.cpFiles(pathUtil.toolsPath() + "mkbootimg", "system/bin/", true)
                        fileUtil.cpFiles(pathUtil.toolsPath() + "mkdtimg", "system/bin/", true)
                        fileUtil.cpFiles(
                            pathUtil.toolsPath() + "unpack_bootimg",
                            "system/bin/", true
                        )
                    }.start()
                })
            }, {})
        }
    }
}