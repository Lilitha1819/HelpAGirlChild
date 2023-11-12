package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.data.User
import com.example.myapplication.data.UserState
import com.example.myapplication.fragment.SettingsFragment
import com.example.myapplication.fragment.DonateFragment
import com.example.myapplication.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nav : BottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        val user: User? = UserState.getInstance()?.getUser()
        val email = findViewById<View>(R.id.name) as TextView
        val imageView = findViewById<View>(R.id.image) as ImageView

        email.text = user?.getName()
        imageView.setImageResource(user!!.getImage())
        loadFragment(HomeFragment())
        nav.setOnNavigationItemSelectedListener {
            onNavigationItemSelected(it)
        }
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {

        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.nav_home -> fragment = HomeFragment()
            R.id.nav_settings -> fragment = SettingsFragment()
            R.id.nav_donate -> fragment = DonateFragment()
        }
        return loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        return if (fragment == null) false else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_frame_layout, fragment)
                .commit()
            true
        }
    }
}