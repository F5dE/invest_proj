package com.f5de.invest

import android.content.Context
import android.content.SharedPreferences
import java.util.*

open class Preferences {
    fun getPreferences(context: Context): SharedPreferences {
        // same as PreferenceManager.getDefaultSharedPreference(), but with special multi process mode
        // http://stackoverflow.com/questions/5946135/difference-between-getdefaultsharedpreferences-and-getsharedpreferences
        return context.getSharedPreferences(
            getDefaultSharedPreferencesName(context),
            Context.MODE_MULTI_PROCESS
        )
    }

    private fun getDefaultSharedPreferencesName(context: Context): String {
        return context.packageName + "_preferences"
    }

    internal fun putStringList(context: Context, id: String, list: ArrayList<String?>) {
        val editor = getPreferences(context).edit()
        editor.putInt(id + "_count", list.size)
        for (i in list.indices) {
            editor.putString(id + "_" + i, list[i])
        }
        editor.apply()
    }

    internal fun getStringList(context: Context, id: String): ArrayList<String?> {
        val preferences = getPreferences(context)
        val result = ArrayList<String?>()
        val count = preferences.getInt(id + "_count", 0)
        for (i in count - 1 downTo 0) {
            result.add(preferences.getString(id + "_" + i, ""))
        }
        return result
    }
}
