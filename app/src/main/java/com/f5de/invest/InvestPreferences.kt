package com.f5de.invest

import android.content.Context

object InvestPreferences : Preferences() {
    fun getNightModeEnabled(context: Context): Boolean {
        return getPreferences(context).getBoolean(context.getString(R.string.pref_common_night_mode_key), false)
    }

    fun putLanguage(context: Context, lang: String?) {
        val editor = getPreferences(context).edit()
        editor.putString(context.getString(R.string.pref_common_language_key), lang)
        editor.apply()
    }

    fun getLanguage(context: Context): String? {
        return getPreferences(context).getString(context.getString(R.string.pref_common_language_key), "")
    }

}