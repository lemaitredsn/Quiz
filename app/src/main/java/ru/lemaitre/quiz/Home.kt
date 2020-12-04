package ru.lemaitre.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import java.lang.RuntimeException

class Home : AppCompatActivity() {
    lateinit var bottomNavigationView:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setDefaultFragment()

        bottomNavigationView = navigation

        bottomNavigationView.setOnNavigationItemSelectedListener(
            object:BottomNavigationView.OnNavigationItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    var selectedFragment:Fragment? = null
                    when(item.itemId){
                        R.id.action_category -> selectedFragment = CategoryFragment.newInstance()
                        R.id.action_ranking -> selectedFragment = RankingFragment.newInstance()
                        else -> RuntimeException("unfamouse fragment")
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, selectedFragment!!)
                        .commit()

                    return true
                }
            }
        )

    }

    private fun setDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, CategoryFragment.newInstance())
            .commit()
    }
}