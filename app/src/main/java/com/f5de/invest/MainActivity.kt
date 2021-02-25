package com.f5de.invest

import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), Controller.Callback {

    private var stockDao: StockDao? = null

    private var toolbar: Toolbar? = null
    private var viewpager: ViewPager? = null
    private var tablayout: TabLayout? = null
    private var wallet: TextView? = null
    private var change: TextView? = null
    private var changeImage: ImageView? = null
    private var fab: FloatingActionButton? = null
    private lateinit var controller: Controller
    private val dialogFragment = AddDialog()
    private val adapter = ViewPagerAdapter(supportFragmentManager)
    private val SIMULATION_TAG = "simulation"


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
        changeImage = findViewById(R.id.total_money_image_change)
        controller = Controller.getInstance(this)
        findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.GONE
        fab = findViewById<FloatingActionButton>(R.id.fab)
        fab?.visibility = View.GONE
        fab?.setOnClickListener {
            dialogFragment.show(supportFragmentManager, "addDialog")
        }
        findViewById<Button>(R.id.step_up).setOnClickListener {
            controller.step()
            fab?.visibility = View.GONE
            if (controller.timeHandler == 0){
                fab?.visibility = View.VISIBLE
                controller.flag = false
                //TODO show result
            }
        }

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<SimulationFragment>(R.id.frame, SIMULATION_TAG)
            }
        }
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
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
        adapter.addFragment(StockFragment(), "Stock")
        adapter.addFragment(CurrencyFragment(), "Currency")
        adapter.addFragment(BondFragment(), "Bond")
        adapter.addFragment(MaterialsFragment(), "Metal")

        viewpager?.adapter = adapter
        tablayout?.setupWithViewPager(viewpager)
    }

    override fun refresh() {
        for (f in supportFragmentManager.fragments){
            f.onResume()
        }
        val tmp = controller.getAll()
        wallet?.text = tmp.sumBy { (it.price * it.amount).toInt() }.toString()
        change?.text = ((controller.data.changeMoney * 100).roundToInt() / 100f).toString()
        if (controller.data.changeMoney < 0) {
            change?.setTextColor(ContextCompat.getColor(this, R.color.red_500))
            changeImage?.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24)
        } else {
            change?.setTextColor(ContextCompat.getColor(this, R.color.green_500))
            changeImage?.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24)
        }
    }

    override fun initiate() {
        supportFragmentManager.commit {
            val fragment: Fragment = supportFragmentManager.findFragmentByTag(SIMULATION_TAG)!!
            remove(fragment)
            addToBackStack("Simulation")
        }
        setUpTabs()
        findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.VISIBLE
        findViewById<FloatingActionButton>(R.id.fab).visibility = View.VISIBLE
    }
}