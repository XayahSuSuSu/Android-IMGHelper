package com.xayah.imghelper.fragment.home

import androidx.lifecycle.ViewModel
import com.topjohnwu.superuser.Shell

class HomeViewModel : ViewModel() {
    val isRoot = Shell.getShell().isRoot
}