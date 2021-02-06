package com.f5de.invest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

class SettingsActivity : AppCompatActivity(), SettingsFragment.Callback{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun applyNightMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        delegate.localNightMode = mode
        delegate.applyDayNight()
    }

    override fun applyLanguage(lng: String) {
//        LocaleUtils.setLocale(applicationContext, lng)
        Locale.setDefault(Locale(lng))
        restartActivity()
    }

    private fun restartActivity() {
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, SettingsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        overridePendingTransition(0, 0)
    }




}