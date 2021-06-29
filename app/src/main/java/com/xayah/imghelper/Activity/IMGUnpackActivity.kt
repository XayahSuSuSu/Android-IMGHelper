package com.xayah.imghelper.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.xayah.imghelper.R
import com.xayah.imghelper.Tools.IMGFlash
import com.xayah.imghelper.Tools.Unpack_bootimg
import com.xayah.imghelper.Utils.ContentUriUtil
import com.xayah.imghelper.Utils.DialogUtil
import com.xayah.imghelper.Utils.PathUtil

class IMGUnpackActivity : AppCompatActivity() {
    // -------------------Component-------------------
    lateinit var imgunpack_cardView_boot: CardView
    lateinit var imgunpack_cardView_recovery: CardView
    lateinit var imgunpack_cardView_dtbo: CardView

    // -------------------Utils-------------------
    val dialogUtil = DialogUtil(this)
    val pathUtil = PathUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img_unpack)
        bindView() // 绑定组件
        setListener() // 设置监听器
    }

    private fun setListener() {
        imgunpack_cardView_boot.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" //无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 1)
        }
        imgunpack_cardView_recovery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" //无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 2)
        }
        imgunpack_cardView_dtbo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" //无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 3)
        }
    }

    private fun bindView() {
        imgunpack_cardView_boot = findViewById(R.id.imgunpack_cardView_boot)
        imgunpack_cardView_recovery = findViewById(R.id.imgunpack_cardView_recovery)
        imgunpack_cardView_dtbo = findViewById(R.id.imgunpack_cardView_dtbo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    val path: String
                    try {
                        path = ContentUriUtil.getPath(this, uri).toString()
                        Log.d("MainActivity", "文件路径：$path")
                        Log.d(
                            "MainActivity",
                            "文件后缀：" + path.substring(path.length - 4, path.length)
                        )
                        if (path.substring(path.length - 4, path.length) == ".img") {
                            dialogUtil.createProgressDialog {
                                Thread {
                                    Unpack_bootimg.unpackIMG(path,"${pathUtil.outPath()}/unpack/boot")
                                    Unpack_bootimg.unpackRamdisk("${pathUtil.outPath()}/unpack/boot")
                                    it.dismiss()
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog(
                                            "boot解包成功!",
                                            "好的", {})
                                    }
                                }.start()
                            }
                        } else {
                            dialogUtil.createCommonDialog("请选择正确的IMG格式", {}, {})
                        }
                    } catch (e: RuntimeException) {
                        e.printStackTrace()
                        dialogUtil.createPositiveButtonDialog("请勿在快捷选择中选择文件！", "好的", {})
                    }
                }
            }
            if (requestCode == 2) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    val path: String
                    try {
                        path = ContentUriUtil.getPath(this, uri).toString()
                        Log.d("MainActivity", "文件路径：$path")
                        Log.d(
                            "MainActivity",
                            "文件后缀：" + path.substring(path.length - 4, path.length)
                        )
                        if (path.substring(path.length - 4, path.length) == ".img") {
                            dialogUtil.createProgressDialog {
                                Thread {
                                    Unpack_bootimg.unpackIMG(path,"${pathUtil.outPath()}/unpack/recovery")
                                    Unpack_bootimg.unpackRamdisk("${pathUtil.outPath()}/unpack/recovery")
                                    it.dismiss()
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog(
                                            "recovery解包成功!",
                                            "好的", {})
                                    }
                                }.start()
                            }
                        } else {
                            dialogUtil.createCommonDialog("请选择正确的IMG格式", {}, {})
                        }
                    } catch (e: RuntimeException) {
                        e.printStackTrace()
                        dialogUtil.createPositiveButtonDialog("请勿在快捷选择中选择文件！", "好的", {})
                    }
                }
            }
            if (requestCode == 3) {
                val uri: Uri? = data?.data
                if (uri != null) {
                    val path: String
                    try {
                        path = ContentUriUtil.getPath(this, uri).toString()
                        Log.d("MainActivity", "文件路径：$path")
                        Log.d(
                            "MainActivity",
                            "文件后缀：" + path.substring(path.length - 4, path.length)
                        )
                        if (path.substring(path.length - 4, path.length) == ".img") {
                            dialogUtil.createProgressDialog {
                                Thread {
                                    it.dismiss()
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog(
                                            "暂不支持!",
                                            "好的", {})
                                    }
                                }.start()
                            }
                        } else {
                            dialogUtil.createCommonDialog("请选择正确的IMG格式", {}, {})
                        }
                    } catch (e: RuntimeException) {
                        e.printStackTrace()
                        dialogUtil.createPositiveButtonDialog("请勿在快捷选择中选择文件！", "好的", {})
                    }
                }
            }
        }
    }
}