package com.xayah.imghelper.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.xayah.imghelper.R
import com.xayah.imghelper.Tools.IMGExtract
import com.xayah.imghelper.Utils.DialogUtil
import com.xayah.imghelper.Utils.PathUtil

class IMGExtractActivity : AppCompatActivity() {
    // -------------------Component-------------------
    lateinit var imgextract_cardView_boot: CardView
    lateinit var imgextract_cardView_recovery: CardView
    lateinit var imgextract_cardView_dtbo: CardView


    // -------------------Utils-------------------
    val dialogUtil = DialogUtil(this)
    val pathUtil = PathUtil(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img_extract)
        bindView() // 绑定组件
        setListener() // 设置监听器
    }

    private fun setListener() {
        imgextract_cardView_boot.setOnClickListener {
            dialogUtil.createProgressDialog {
                Thread {
                    IMGExtract.boot("${pathUtil.outPath()}/extract")
                    it.dismiss()
                    runOnUiThread {
                        dialogUtil.createPositiveButtonDialog(
                            "提取成功！镜像存放于:\n${pathUtil.outPath()}/extract/boot.img",
                            "好的", {})
                    }
                }.start()
            }
        }
        imgextract_cardView_recovery.setOnClickListener {
            dialogUtil.createProgressDialog {
                Thread {
                    IMGExtract.recovery("${pathUtil.outPath()}/extract")
                    it.dismiss()
                    runOnUiThread {
                        dialogUtil.createPositiveButtonDialog(
                            "提取成功！镜像存放于:\n${pathUtil.outPath()}/extract/recovery.img",
                            "好的", {})
                    }
                }.start()
            }
        }
        imgextract_cardView_dtbo.setOnClickListener {
            dialogUtil.createProgressDialog {
                Thread {
                    IMGExtract.dtbo("${pathUtil.outPath()}/extract")
                    it.dismiss()
                    runOnUiThread {
                        dialogUtil.createPositiveButtonDialog(
                            "提取成功！镜像存放于:\n${pathUtil.outPath()}/extract/dtbo.img",
                            "好的", {})
                    }
                }.start()
            }
        }
    }

    private fun bindView() {
        imgextract_cardView_boot = findViewById(R.id.imgextract_cardView_boot)
        imgextract_cardView_recovery = findViewById(R.id.imgextract_cardView_recovery)
        imgextract_cardView_dtbo = findViewById(R.id.imgextract_cardView_dtbo)

    }

}