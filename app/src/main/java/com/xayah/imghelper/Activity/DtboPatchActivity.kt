package com.xayah.imghelper.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.topjohnwu.superuser.Shell
import com.xayah.imghelper.R
import com.xayah.imghelper.Utils.ContentUriUtil
import com.xayah.imghelper.Utils.DialogUtil
import com.xayah.imghelper.Utils.PathUtil

class DtboPatchActivity : AppCompatActivity() {
    // -------------------Component-------------------
    lateinit var textInputEditText_dtboPath: TextInputEditText
    lateinit var textInputEditText_dtboRate: TextInputEditText
    lateinit var dtbo_patch_imageView_copyright: ImageView
    lateinit var floatingActionButton_dtboPatch: ExtendedFloatingActionButton

    // -------------------Utils-------------------
    val dialogUtil = DialogUtil(this)
    val pathUtil = PathUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dtbo_patch)
        bindView() // 绑定组件
        setListener() // 设置监听器
    }

    private fun bindView() {
        textInputEditText_dtboPath = findViewById(R.id.textInputEditText_dtboPath)
        textInputEditText_dtboRate = findViewById(R.id.textInputEditText_dtboRate)
        dtbo_patch_imageView_copyright = findViewById(R.id.dtbo_patch_imageView_copyright)
        floatingActionButton_dtboPatch = findViewById(R.id.floatingActionButton_dtboPatch)
    }

    private fun setListener() {
        textInputEditText_dtboPath.setOnClickListener {
            // 调用文件管理器
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" //无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 1)
        }
        floatingActionButton_dtboPatch.setOnClickListener {
            if (textInputEditText_dtboPath.text.toString()
                    .isEmpty() || textInputEditText_dtboRate.text.toString().isEmpty()
            ) {
                dialogUtil.createPositiveButtonDialog(
                    "请检查DTBO路径或刷新率",
                    "好的",
                    {})
            } else {
                dialogUtil.createProgressDialog {
                    Thread {
                        val mRate = textInputEditText_dtboRate.text.toString()
                        val mShellPath = pathUtil.scriptsPath() + "Patch.sh"
                        val mDtboDirPath = textInputEditText_dtboPath.text.toString()
                            .substring(
                                0,
                                textInputEditText_dtboPath.text.toString().lastIndexOf('/')
                            )
                        val mDtboPath = textInputEditText_dtboPath.text.toString()
                        val mShellDirPath = mShellPath.substring(0, mShellPath.lastIndexOf('/'))
                        Shell.su("cd $mShellDirPath").exec()
                        Shell.su("echo $mRate > Info.txt").exec()
                        Shell.su("cp $mDtboPath $mShellDirPath/dtbo.img").exec()
                        val result = Shell.su("./Patch.sh").exec().out
                        Log.d("DtboPatchActivity", result.toString())
                        it.dismiss()
                        Shell.su("cp dtbo_new.img $mDtboDirPath/dtbo_new.img").exec()
                        runOnUiThread {
                            dialogUtil.createPositiveButtonDialog(
                                "修补成功!\n存放于:\n$mDtboDirPath/dtbo_new.img",
                                "好的",
                                {})
                        }
                    }.start()
                }
            }
        }
        dtbo_patch_imageView_copyright.setOnClickListener {
            dialogUtil.createPositiveButtonDialog(
                "修补脚本修改自酷安 @神裂火织 的dtbo-tools\n已获得作者授权",
                "好的",
                {})
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
                        if (path.substring(path.length - 4, path.length) == ".img") {
                            textInputEditText_dtboPath.setText(path)
                        } else {
                            dialogUtil.createCommonDialog("请选择正确的IMG格式", {}, {})
                        }
                    }
                }
            }
        }
    }

}