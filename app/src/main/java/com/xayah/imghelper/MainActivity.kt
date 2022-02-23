package com.xayah.imghelper

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.xayah.imghelper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val model: MainViewModel by viewModels()
        binding.viewModel = model
        model.versionName = this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }
}