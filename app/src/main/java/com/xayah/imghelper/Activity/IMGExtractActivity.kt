package com.xayah.imghelper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.cardview.widget.CardView
import com.xayah.imghelper.R
import com.xayah.imghelper.Utils.DialogUtil
import com.xayah.imghelper.Utils.Tools.IMGExtract

class IMGExtractActivity : AppCompatActivity() {
    lateinit var cachePath: String
    lateinit var outPath: String
    val dialogUtil = DialogUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imgextract)
        cachePath = applicationContext.filesDir.toString() + "/cache/"
        outPath = Environment.getExternalStorageDirectory().path + "/IMGHelper"
        val imgExtract = IMGExtract(cachePath)
        val imgextract_cardView_boot: CardView = findViewById(R.id.imgextract_cardView_boot)
        imgextract_cardView_boot.setOnClickListener {
            imgExtract.boot("$outPath/extract")
            dialogUtil.createCommonDialog("提取成功！镜像存放于:\n$outPath/extract/boot.img", {}, {})
        }
        val imgextract_cardView_recovery: CardView = findViewById(R.id.imgextract_cardView_recovery)
        imgextract_cardView_recovery.setOnClickListener {
            imgExtract.recovery("$outPath/extract")
            dialogUtil.createCommonDialog("提取成功！镜像存放于:\n$outPath/extract/recovery.img", {}, {})
        }
        val imgextract_cardView_dtbo: CardView = findViewById(R.id.imgextract_cardView_dtbo)
        imgextract_cardView_dtbo.setOnClickListener {
            imgExtract.dtbo("$outPath/extract")
            dialogUtil.createCommonDialog("提取成功！镜像存放于:\n$outPath/extract/dtbo.img", {}, {})
        }
    }

}