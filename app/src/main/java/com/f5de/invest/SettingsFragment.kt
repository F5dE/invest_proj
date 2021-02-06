package com.f5de.invest

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsFragment : PreferenceFragmentCompat()  {

    private var callback: Callback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as Callback?
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<SwitchPreferenceCompat>(getString(R.string.pref_common_night_mode_key))?.setOnPreferenceChangeListener { _, newValue ->
            callback?.applyNightMode(if (newValue as Boolean) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            true
        }
//        findPreference<Preference>(getString(R.string.pref_common_language_key))?.setOnPreferenceChangeListener { _, newValue ->
//            callback?.applyLanguage(newValue as String)
//            true
//        }
    }


    interface Callback {
        fun applyNightMode(mode: Int)
        fun applyLanguage(language: String)
    }
}