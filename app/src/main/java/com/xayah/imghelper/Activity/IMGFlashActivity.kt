package com.xayah.imghelper.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.xayah.imghelper.R
import com.xayah.imghelper.Tools.IMGFlash
import com.xayah.imghelper.Utils.ContentUriUtil
import com.xayah.imghelper.Utils.DialogUtil

class IMGFlashActivity : AppCompatActivity() {
    // -------------------Component-------------------
    lateinit var imgflash_cardView_boot: CardView
    lateinit var imgflash_cardView_recovery: CardView
    lateinit var imgflash_cardView_dtbo: CardView

    // -------------------Utils-------------------
    val dialogUtil = DialogUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img_flash)
        bindView() // 绑定组件
        setListener() // 设置监听器
    }

    private fun setListener() {
        imgflash_cardView_boot.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" //无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 1)
        }
        imgflash_cardView_recovery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" //无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 2)
        }
        imgflash_cardView_dtbo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" //无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 3)
        }
    }

    private fun bindView() {
        imgflash_cardView_boot = findViewById(R.id.imgflash_cardView_boot)
        imgflash_cardView_recovery = findViewById(R.id.imgflash_cardView_recovery)
        imgflash_cardView_dtbo = findViewById(R.id.imgflash_cardView_dtbo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    val path = ContentUriUtil.getPath(this, uri)
                    Log.d("MainActivity", "文件路径：$path")
                    if (path != null) {
                        Log.d(
                            "MainActivity",
                            "文件后缀：" + path.substring(path.length - 4, path.length)
                        )
                        if (path.substring(path.length - 4, path.length - 1) == ".img") {
                            dialogUtil.createProgressDialog {
                                Thread {
                                    IMGFlash.boot(path)
                                    it.dismiss()
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog(
                                            "boot刷入成功!",
                                            "好的", {})
                                    }
                                }.start()
                            }
                        } else {
                            dialogUtil.createCommonDialog("请选择正确的IMG格式", {}, {})
                        }
                    } else {
                        dialogUtil.createPositiveButtonDialog("选择文件时，请勿在快捷选择中选择文件！", "好的", {})
                    }
                }
            }
            if (requestCode == 2) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    val path = ContentUriUtil.getPath(this, uri)
                    Log.d("MainActivity", "文件路径：" + path)
                    if (path != null) {
                        Log.d(
                            "MainActivity",
                            "文件后缀：" + path.substring(path.length - 4, path.length)
                        )
                        if (path.substring(path.length - 4, path.length - 1) == ".img") {
                            dialogUtil.createProgressDialog {
                                Thread {
                                    IMGFlash.recovery(path)
                                    it.dismiss()
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog(
                                            "recovery刷入成功!",
                                            "好的", {})
                                    }
                                }.start()
                            }
                        } else {
                            dialogUtil.createCommonDialog("请选择正确的IMG格式", {}, {})
                        }
                    } else {
                        dialogUtil.createPositiveButtonDialog("选择文件时，请勿在快捷选择中选择文件！", "好的", {})
                    }
                }
            }
            if (requestCode == 3) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    val path = ContentUriUtil.getPath(this, uri)
                    Log.d("MainActivity", "文件路径：" + path)
                    if (path != null) {
                        Log.d(
                            "MainActivity",
                            "文件后缀：" + path.substring(path.length - 4, path.length)
                        )
                        if (path.substring(path.length - 4, path.length - 1) == ".img") {
                            dialogUtil.createProgressDialog {
                                Thread {
                                    IMGFlash.dtbo(path)

                                    it.dismiss()
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog(
                                            "dtbo刷入成功!",
                                            "好的", {})
                                    }
                                }.start()
                            }
                        } else {
                            dialogUtil.createCommonDialog("请选择正确的IMG格式", {}, {})
                        }
                    }
                } else {
                    dialogUtil.createPositiveButtonDialog("选择文件时，请勿在快捷选择中选择文件！", "好的", {})
                }
            }
        }
    }
}