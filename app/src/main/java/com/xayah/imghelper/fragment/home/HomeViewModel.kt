package com.xayah.imghelper.fragment.home

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.topjohnwu.superuser.Shell
import com.xayah.imghelper.R

class HomeViewModel : ViewModel() {
    val isRoot = Shell.getShell().isRoot

    fun toDTBOFragment(v: View) {
        Navigation.findNavController(v).navigate(R.id.action_page_home_to_page_dtbo)
    }
}