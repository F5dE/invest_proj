package com.f5de.invest

import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceDialogFragmentCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    private var db: AppDatabase? = null
    private var stockDao: StockDao? = null

    private var toolbar: Toolbar? = null
    private var reloadLanguage: Boolean = false
    private var applyNightMode: Boolean = false


    protected var mPreferencesListener =
        OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == getString(R.string.pref_common_language_key)) {
                reloadLanguage = true
            } else if (key == getString(R.string.pref_common_language_key)) {
                applyNightMode = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?): Unit = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        db = AppDatabase(applicationContext)

        val job = GlobalScope.launch {
            var stock1 = Stock(
                companyToken = "APPL",
                companyName = "Apple",
                money = 1960f,
                stockAmount = 14,
                price = 140f
            )
//            var stock2 = Stock(companyToken = "RIG", companyName = "Transocean", money = 1960f, stockAmount = 14, price = 140f)
//
            db?.stockDao()?.insert(stock1)
//            db?.stockDao()?.insert(stock2)
//            db?.stockDao()?.deleteByToken("AAPL")

            data = db?.stockDao()?.getAll()

            data?.forEach {
                println(it)
            }
        }

        job.join()
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(mPreferencesListener)
        if (applyNightMode) {
            applyNightMode = false
            delegate.localNightMode =
                if (InvestPreferences.getNightModeEnabled(this)) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            delegate.applyDayNight()
            recreate()
        }
        if (reloadLanguage) {
            reloadLanguage = false
            onLanguageChanged()
        }
    }

    protected fun onLanguageChanged() {}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        var data: List<Stock>? = null

        fun newInstance() {}
    }
}