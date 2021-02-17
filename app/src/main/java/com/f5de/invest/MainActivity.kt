package com.f5de.invest

import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity(), Controller.Callback {

    private var stockDao: StockDao? = null

    private var toolbar: Toolbar? = null
    private var viewpager: ViewPager? = null
    private var tablayout: TabLayout? = null
    private var wallet: TextView? = null
    private var change: TextView? = null
    private lateinit var controller: Controller
    private val dialogFragment = AddDialog()


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        viewpager = findViewById(R.id.viewPager)
        tablayout = findViewById(R.id.tabLayout)
        wallet = findViewById(R.id.total_money_text)
        change = findViewById(R.id.total_money_change)
        controller = Controller.getInstance(this)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            dialogFragment.show(supportFragmentManager, "addDialog")
        }

        setSupportActionBar(toolbar)

        setUpTabs()
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(mPreferencesListener)
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

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(SimulationFragment(), "Simulation")
        adapter.addFragment(StockFragment(), "Stock")
        adapter.addFragment(CurrencyFragment(), "Currency")
        adapter.addFragment(BondFragment(), "Bond")
        adapter.addFragment(MaterialsFragment(), "Metal")

        viewpager?.adapter = adapter
        tablayout?.setupWithViewPager(viewpager)

    }

    override fun refresh() {
        wallet?.text = controller.getAll().sumBy { (it.price * it.amount).toInt() }.toString()
    }
}