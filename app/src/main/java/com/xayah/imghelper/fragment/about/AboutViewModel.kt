package com.xayah.imghelper.fragment.about

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {
    fun toAppGitHub(v: View) {
        val uri = Uri.parse("https://github.com/XayahSuSuSu/Android-IMGHelper")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        v.context.startActivity(intent)
    }

    fun toAppAuthorCoolapk(v: View) {
        val uri = Uri.parse("http://www.coolapk.com/u/1394294")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        v.context.startActivity(intent)
    }

    fun toClashGitHub(v: View) {
        val uri = Uri.parse("https://github.com/Kr328/ClashForAndroid")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        v.context.startActivity(intent)
    }
}