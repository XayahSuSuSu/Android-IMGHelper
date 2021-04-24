package com.xayah.imghelper.Activity

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.xayah.imghelper.R
import com.xayah.imghelper.Utils.Tools.IMGExtract

class IMGExtractActivity : AppCompatActivity() {
    lateinit var cachePath: String
    lateinit var outPath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imgextract)
        cachePath = applicationContext.filesDir.toString() + "/cache/"
        outPath = Environment.getExternalStorageDirectory().path + "/IMGHelper"
        val imgExtract = IMGExtract(cachePath)
        val imgextract_cardView_boot: CardView = findViewById(R.id.imgextract_cardView_boot)
        imgextract_cardView_boot.setOnClickListener {
            imgExtract.boot("$outPath/extract")
            Toast.makeText(this, "提取成功！存放于:\n$outPath/extract/boot.img", Toast.LENGTH_LONG).show()
            val builder = AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("提取成功！镜像存放于:\n$outPath/extract/boot.img")
                .setCancelable(true)
                .setPositiveButton("好的") { _: DialogInterface?, which: Int -> }
                .create()
            builder.show()
            builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
            builder.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.BLUE)
        }
        val imgextract_cardView_recovery: CardView = findViewById(R.id.imgextract_cardView_recovery)
        imgextract_cardView_recovery.setOnClickListener {
            imgExtract.recovery("$outPath/extract")
            Toast.makeText(this, "提取成功！存放于:\n$outPath/extract/recovery.img", Toast.LENGTH_LONG).show()
            val builder = AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("提取成功！镜像存放于:\n$outPath/extract/recovery.img")
                .setCancelable(true)
                .setPositiveButton("好的") { _: DialogInterface?, which: Int -> }
                .create()
            builder.show()
            builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
            builder.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.BLUE)
        }
        val imgextract_cardView_dtbo: CardView = findViewById(R.id.imgextract_cardView_dtbo)
        imgextract_cardView_dtbo.setOnClickListener {
            imgExtract.dtbo("$outPath/extract")
            Toast.makeText(this, "提取成功！存放于:\n$outPath/extract/dtbo.img", Toast.LENGTH_LONG).show()
            val builder = AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("提取成功！镜像存放于:\n$outPath/extract/dtbo.img")
                    .setCancelable(true)
                    .setPositiveButton("好的") { _: DialogInterface?, which: Int -> }
                    .create()
            builder.show()
            builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
            builder.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(Color.BLUE)
        }
    }

}