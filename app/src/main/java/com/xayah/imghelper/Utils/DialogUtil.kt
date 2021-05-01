package com.xayah.imghelper.Utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.xayah.imghelper.R


class DialogUtil(var mContext: Context) {
    fun createCommonDialog(message: String, positiveEvent: () -> Unit, negativeEvent: () -> Unit) {
        val builder = AlertDialog.Builder(mContext)
            .setTitle("提示")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("确定") { _: DialogInterface?, which: Int -> positiveEvent() }
            .setNegativeButton("取消") { _: DialogInterface?, which: Int -> negativeEvent() }
            .create()
        builder.window
            ?.setBackgroundDrawableResource(R.drawable.drawable_round_edge)
        builder.show()
//        builder.getButton(AlertDialog.BUTTON_POSITIVE)
//            .setTextColor(Color.parseColor("#f88e20"))
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
        positiveEvent: (AlertDialog) -> Unit,
    ) {
        val builder = AlertDialog.Builder(mContext)
            .setTitle("请稍后")
            .setCancelable(false)
            .create()
        builder.window
            ?.setBackgroundDrawableResource(R.drawable.drawable_round_edge)
        builder.show()
        val mParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mParam.setMargins(
            0,
            mContext.resources.getDimension(R.dimen.mProgressBarDialogTop).toInt(),
            0,
            mContext.resources.getDimension(R.dimen.mProgressBarDialogBottom).toInt()
        )
        builder.addContentView(ProgressBar(mContext), mParam)
        val params: WindowManager.LayoutParams = builder.window!!.attributes
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            params.width = mContext.display!!.width - 200
        } else {
            builder.window!!.windowManager.getDefaultDisplay().getWidth() - 200
        }
        builder.window!!.attributes = params

        positiveEvent(builder)
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
            .setCancelable(false)
            .setPositiveButton(positiveText) { _: DialogInterface?, _: Int -> positiveEvent() }
            .setNegativeButton(negativeText) { _: DialogInterface?, _: Int -> negativeEvent() }
            .create()
        builder.window
            ?.setBackgroundDrawableResource(R.drawable.drawable_round_edge)
        builder.show()
//        builder.getButton(AlertDialog.BUTTON_POSITIVE)
//            .setTextColor(Color.parseColor("#f88e20"))
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

    fun createPositiveButtonDialog(
        message: String,
        positiveText: String,
        positiveEvent: () -> Unit,
    ) {
        val builder = AlertDialog.Builder(mContext)
            .setTitle("提示")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(positiveText) { _: DialogInterface?, _: Int -> positiveEvent() }
            .create()
        builder.window
            ?.setBackgroundDrawableResource(R.drawable.drawable_round_edge)
        builder.show()
//        builder.getButton(AlertDialog.BUTTON_POSITIVE)
//            .setTextColor(Color.parseColor("#f88e20"))
        val params: WindowManager.LayoutParams = builder.window!!.attributes
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            params.width = mContext.display!!.width - 200
        } else {
            builder.window!!.windowManager.getDefaultDisplay().getWidth() - 200
        }
        builder.window!!.attributes = params
    }
}