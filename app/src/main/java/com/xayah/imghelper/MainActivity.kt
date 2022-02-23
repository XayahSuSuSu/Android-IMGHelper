package com.xayah.imghelper

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.xayah.imghelper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val model: MainViewModel by viewModels()
        binding.viewModel = model
        model.versionName = this.packageManager.getPackageInfo(this.packageName, 0).versionName

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.page_home,
            R.id.page_unpack,
            R.id.page_pack,
            R.id.page_extract
        ).build()
        setSupportActionBar(binding.topAppBar)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.page_home, R.id.page_unpack, R.id.page_pack, R.id.page_extract -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}