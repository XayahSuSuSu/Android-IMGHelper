package com.xayah.imghelper.Utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.xayah.imghelper.R


class DialogUtil(var mContext: Context) {
    fun createCommonDialog(message: String, positiveEvent: () -> Unit, negativeEvent: () -> Unit) {
        val builder = AlertDialog.Builder(mContext)
            .setTitle("提示")
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton("确定") { _: DialogInterface?, which: Int -> positiveEvent() }
            .setNegativeButton("取消") { _: DialogInterface?, which: Int -> negativeEvent() }
            .create()
        builder.window
            ?.setBackgroundDrawableResource(R.drawable.drawable_round_edge)
        builder.show()
        builder.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(Color.parseColor("#f88e20"));
        builder.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(Color.RED)
        val params: WindowManager.LayoutParams = builder.window!!.attributes
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            params.width = mContext.display!!.width - 200
        } else {
            builder.window!!.windowManager.getDefaultDisplay().getWidth() - 200
        }
        builder.window!!.attributes = params
    }

    fun createProgressDialog(
        message: String,
        positiveEvent: () -> Unit,
        negativeEvent: () -> Unit
    ) {
//        val mView: View = LayoutInflater.from(this)
//            .inflate(R.layout.alertdialog_loading, null, false)
//        val alertdialog_progressBar: ProgressBar = mView.findViewById(R.id.alertdialog_progressBar)
//        val builder = AlertDialog.Builder(this)
//            .setView(mView)
//            .setTitle("提示")
//            .setMessage("正在安装...")
//            .setCancelable(false)
//            .create()
//        builder.show()
    }

    fun createCustomButtonDialog(
        message: String,
        positiveText: String,
        negativeText: String,
        positiveEvent: () -> Unit,
        negativeEvent: () -> Unit
    ) {
        val builder = AlertDialog.Builder(mContext)
            .setTitle("提示")
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(positiveText) { _: DialogInterface?, which: Int -> positiveEvent }
            .setNegativeButton(negativeText) { _: DialogInterface?, which: Int -> negativeEvent }
            .create()
        builder.window
            ?.setBackgroundDrawableResource(R.drawable.drawable_round_edge)
        builder.show()
        builder.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(Color.parseColor("#f88e20"));
        builder.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(Color.RED)
        val params: WindowManager.LayoutParams = builder.window!!.attributes
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            params.width = mContext.display!!.width - 200
        } else {
            builder.window!!.windowManager.getDefaultDisplay().getWidth() - 200
        }
        builder.window!!.attributes = params
    }
}