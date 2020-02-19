package com.naemo.scorsese.ui.nav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naemo.scorsese.R
import com.naemo.scorsese.helper.NoSwipePager

class NavigationActivity : AppCompatActivity() {

    private var viewPager: NoSwipePager? = null
    private var pagerAdapter: BottomAppBar? = null
    internal var navigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)



    }
}
