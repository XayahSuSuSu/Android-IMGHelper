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
    val dialogUtil = DialogUtil(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img_flash)

        val imgflash_cardView_boot: CardView = findViewById(R.id.imgflash_cardView_boot)
        imgflash_cardView_boot.setOnClickListener {
            dialogUtil.createCommonDialog("选择文件时，请勿在快捷选择中选择文件！", {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*" //无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(intent, 1)
            }, {})
        }

        val imgflash_cardView_recovery: CardView = findViewById(R.id.imgflash_cardView_recovery)
        imgflash_cardView_recovery.setOnClickListener {
            dialogUtil.createCommonDialog("选择文件时，请勿在快捷选择中选择文件！", {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*" //无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(intent, 2)
            }, {})
        }

        val imgflash_cardView_dtbo: CardView = findViewById(R.id.imgflash_cardView_dtbo)
        imgflash_cardView_dtbo.setOnClickListener {
            dialogUtil.createCommonDialog("选择文件时，请勿在快捷选择中选择文件！", {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*" //无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(intent, 3)
            }, {})
        }


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
                            IMGFlash.boot(path)
                            dialogUtil.createCommonDialog("boot刷入成功!", {}, {})
                        } else {
                            dialogUtil.createCommonDialog("请选择正确的IMG格式", {}, {})
                        }
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
                            IMGFlash.recovery(path)
                            dialogUtil.createCommonDialog("recovery刷入成功!", {}, {})
                        } else {
                            dialogUtil.createCommonDialog("请选择正确的IMG格式", {}, {})
                        }
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
                            IMGFlash.dtbo(path)
                            dialogUtil.createCommonDialog("dtbo刷入成功!", {}, {})
                        } else {
                            dialogUtil.createCommonDialog("请选择正确的IMG格式", {}, {})
                        }
                    }
                }
            }
        }
    }
}