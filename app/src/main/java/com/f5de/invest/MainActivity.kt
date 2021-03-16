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
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.viewpager.widget.ViewPager
import com.f5de.invest.ui.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), Controller.Callback {

//    private var stockDao: StockDao? = null

    private var toolbar: Toolbar? = null
    private var appBar: AppBarLayout? = null
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    private var wallet: TextView? = null
    private var freeWallet: TextView? = null
    private var change: TextView? = null
    private var changeImage: ImageView? = null
    private var fab: FloatingActionButton? = null
    private var step: Button? = null
    private lateinit var controller: Controller
    private var dialogFragment = AddDialog()
    private val adapter = ViewPagerAdapter(supportFragmentManager)
    private val SIMULATION_TAG = "simulation"
    private val FINAL_TAG = "final"


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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        wallet = findViewById(R.id.total_money_text)
        freeWallet = findViewById(R.id.free_money_text)
        change = findViewById(R.id.total_money_change)
        changeImage = findViewById(R.id.total_money_image_change)
        controller = Controller.getInstance(this)
        appBar = findViewById(R.id.appBarLayout)
        appBar?.visibility = View.GONE
        fab = findViewById(R.id.fab)
        fab?.visibility = View.GONE
        fab?.setOnClickListener {
            dialogFragment = AddDialog()
            dialogFragment.type = viewPager!!.currentItem
            dialogFragment.show(supportFragmentManager, "addDialog")
        }
        step = findViewById(R.id.step_up)
        step?.setOnClickListener {
            controller.step()
            step?.text = getString(R.string.simulation_step, controller.data.timeStart - controller.timeHandler + 1, controller.data.timeStart)
            if (controller.timeHandler == 0){
                fab?.visibility = View.GONE
                tabLayout?.visibility = View.GONE
                viewPager?.visibility = View.GONE
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<FinalFragment>(R.id.frame, FINAL_TAG)
                    appBar?.visibility = View.GONE
                }
            }

            //TODO show result
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
        adapter.addFragment(StockFragment(), getString(R.string.stock_label))
        adapter.addFragment(CurrencyFragment(), getString(R.string.currency_label))
        adapter.addFragment(BondFragment(), getString(R.string.bond_label))
        adapter.addFragment(MetalsFragment(), getString(R.string.metal_label))

        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)
    }

    override fun refresh() {
        for (i in 0..3){
            val f = adapter.getItem(i)
            if (f.isVisible)
                f.update()
        }
        val tmp = controller.getAll()
        controller.data.stockMoney = tmp.sumBy { (it.price * it.amount).toInt() }.toFloat()
        wallet?.text = controller.data.stockMoney.toString()
        change?.text = ((controller.data.changeMoney * 100).roundToInt() / 100f).toString()
        freeWallet?.text = ((controller.data.freeMoney * 100).roundToInt() / 100f).toString()
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
            val fragment: Fragment? = supportFragmentManager.findFragmentByTag(SIMULATION_TAG)
            fragment?.let {
                remove(fragment)
                addToBackStack("Simulation")
            }
        }
        if (viewPager != null){
            if (viewPager!!.size == 0)
                setUpTabs()
        }else setUpTabs()
        viewPager?.visibility = View.VISIBLE
        tabLayout?.visibility = View.VISIBLE
        findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.VISIBLE
        findViewById<FloatingActionButton>(R.id.fab).visibility = View.VISIBLE
        step?.text = getString(R.string.simulation_step, controller.data.timeStart - controller.timeHandler + 1, controller.data.timeStart)
        freeWallet?.text = controller.data.freeMoney.toString()
        wallet?.text = "0"
        change?.text = "0"
        for (i in 0..3){
            val f = adapter.getItem(i)
            if (f.isVisible)
                f.update()
        }
    }

    override fun restart(){
        supportFragmentManager.popBackStack()
        supportFragmentManager.commit {
            val fragment: Fragment = supportFragmentManager.findFragmentByTag(FINAL_TAG)!!
            remove(fragment)
            addToBackStack("Final")
        }
//        supportFragmentManager.commit {
//            setReorderingAllowed(true)
//            add<SimulationFragment>(R.id.frame, SIMULATION_TAG)
//        }
    }
}