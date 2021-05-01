package com.xayah.imghelper.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.xayah.imghelper.R
import com.xayah.imghelper.Utils.CommandArrayUtil
import com.xayah.imghelper.Utils.ContentUriUtil
import com.xayah.imghelper.Utils.DialogUtil

class DtboPatchActivity : AppCompatActivity() {
    val dialogUtil = DialogUtil(this)
    lateinit var textInputEditText_dtboPath: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dtbo_patch)

        textInputEditText_dtboPath = findViewById(R.id.textInputEditText_dtboPath)
        val textInputEditText_dtboRate: TextInputEditText =
            findViewById(R.id.textInputEditText_dtboRate)
        textInputEditText_dtboPath.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" //无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 1)
        }

        val dtbo_patch_imageView_copyright: ImageView =
            findViewById(R.id.dtbo_patch_imageView_copyright)
        dtbo_patch_imageView_copyright.setOnClickListener {
            dialogUtil.createPositiveButtonDialog(
                "修补脚本修改自酷安 @神裂火织 的dtbo-tools\n已获得作者授权",
                "好的",
                {})
        }

        val floatingActionButton_dtboPatch: ExtendedFloatingActionButton =
            findViewById(R.id.floatingActionButton_dtboPatch)
        floatingActionButton_dtboPatch.setOnClickListener {
            if (textInputEditText_dtboPath.text.toString()
                    .isEmpty() || textInputEditText_dtboRate.text.toString().isEmpty()
            ) {
                dialogUtil.createPositiveButtonDialog(
                    "请检查DTBO路径或刷新率",
                    "好的",
                    {})
            } else {
                Thread{
                    val mRate = textInputEditText_dtboRate.text.toString()
                    val mDtboPatch = mutableListOf<String>()
                    mDtboPatch.add("echo $mRate > Info.txt")
                    val mShellPath = this.filesDir.toString() + "/scripts/Patch.sh"
//                val mDtboDirPath = textInputEditText_dtboPath.text.toString().substring(0,textInputEditText_dtboPath.text.toString().lastIndexOf('/'))
                    val mDtboPath = textInputEditText_dtboPath.text.toString()
                    val mShellDirPath = mShellPath.substring(0, mShellPath.lastIndexOf('/'))
                    mDtboPatch.add("cp $mDtboPath $mShellDirPath/dtbo.img")
                    mDtboPatch.add("chmod 777 $mShellPath")
                    mDtboPatch.add("./Patch.sh")
                    CommandArrayUtil.executeCommand(
                        mDtboPatch,
                        mShellDirPath,
                        true,
                        true
                    )
                }.start()

            }

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