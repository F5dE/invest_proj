package com.f5de.invest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.f5de.invest.*
import com.f5de.invest.data.*
import com.f5de.invest.data.Investment
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class BondFragment : FragmentEx() {

    private lateinit var stockCardHolder: LinearLayout
    var controller: Controller? = null
    var stocks: ArrayList<Bond> = ArrayList()
    val sellDialog = SellDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock, container, false)
    }

    override fun update(){
        if (controller != null) {
            stocks = controller?.getData(2)!! as ArrayList<Bond>
            var childs = (view?.findViewById(R.id.stock_card_holder) as LinearLayout).childCount
            updateCards(stocks)
            if (childs < stocks.size) {
                while (childs != stocks.size) {
                    addCard(stocks[childs], childs)
                    childs++
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        update()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Controller.getInstance(requireContext())
        stockCardHolder = view.findViewById(R.id.stock_card_holder) as LinearLayout
        update()
    }

    fun addCard(userStock: Bond, index: Int) {
        val card = layoutInflater.inflate(R.layout.card_bond, null)
        val holder = CardHolder(card)
        holder.companyName.text = userStock.name
        val curChange = holder.calculateMoney(userStock) - stocks.filter { it.name == userStock.name }[0].price * userStock.amount
        if (curChange < 0) {
            holder.change.text = "${curChange.absoluteValue}$"
            holder.change.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_500))
            holder.changeImage.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24)
        } else {
            holder.change.text = "$curChange$"
            holder.change.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_500))
            holder.changeImage.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24)
        }
        holder.stockAmount.text = "${userStock.amount} у.е."
        holder.time.text = userStock.time.toString()
        when(userStock.name){
            "Russia" -> {
                holder.companyImage.setBackgroundResource(R.drawable.ic_flag_of_russia)
            }
            "Germany" -> {
                holder.companyImage.setBackgroundResource(R.drawable.ic_flag_of_germany)
            }
            "USA" -> {
                holder.companyImage.setBackgroundResource(R.drawable.ic_flag_of_the_united_states)
            }
        }
        stockCardHolder.addView(card, index)
        var counter = 0
        card.setOnClickListener {
            counter++
            if (counter == 2) {
                counter = 0
                sellDialog.type = 2
                val bundle = Bundle()
                bundle.putString("stock", userStock.name)
                sellDialog.arguments = bundle
                fragmentManager?.let { it1 -> sellDialog.show(it1, "sellDialog") }
            }
        }
    }

    fun updateCards(stock: ArrayList<Bond>?) {
        var i = 0
        for (v in stockCardHolder.children) {
            if (stock!!.size - 1 < i) {
                stockCardHolder.removeView(v)
                continue
            }
            if (stock[i].amount == 0 || v.findViewById<TextView>(R.id.stock_card_name).text != stock[i].name) {
                stockCardHolder.removeView(v)
                continue
            }
            v.findViewById<TextView>(R.id.stock_card_current_price).text = (((stock[i].price) * 100).roundToInt() / 100f).toString()
            v.findViewById<TextView>(R.id.stock_card_money).text = (((stock[i].price * stock[i].amount) * 100).roundToInt() / 100f).toString()
            val curChange = stock[i].price * stock[i].amount - stock[i].oldPrice * stock[i].amount
            v.findViewById<TextView>(R.id.stock_card_change).text = curChange.absoluteValue.toString()
            if (curChange < 0) {
                v.findViewById<TextView>(R.id.stock_card_change).setTextColor(ContextCompat.getColor(requireContext(), R.color.red_500))
                v.findViewById<ImageView>(R.id.stock_card_image_change).setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24)
            } else {
                v.findViewById<TextView>(R.id.stock_card_change).setTextColor(ContextCompat.getColor(requireContext(), R.color.green_500))
                v.findViewById<ImageView>(R.id.stock_card_image_change).setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24)
            }
            v.findViewById<TextView>(R.id.stock_card_amount).text = "${stock[i].amount} у.е."
            v.findViewById<TextView>(R.id.stock_card_current_time).text = stock[i].time.toString()
            i++
        }
    }

    private class CardHolder(view: View) {
        val root: CardView = view.findViewById(R.id.bond_card)
        val companyName: TextView = view.findViewById(R.id.stock_card_name)
        val money: TextView = view.findViewById(R.id.stock_card_money)
        val stockAmount: TextView = view.findViewById(R.id.stock_card_amount)
        val currentPrice: TextView = view.findViewById(R.id.stock_card_current_price)
        val change: TextView = view.findViewById(R.id.stock_card_change)
        val changeImage: ImageView = view.findViewById(R.id.stock_card_image_change)
        val companyImage: ImageView = view.findViewById(R.id.stock_card_image_company)
        val time: TextView = view.findViewById(R.id.stock_card_current_time)

        fun calculateMoney(stock: Investment): Float {
            val curPrice = ((stock.price * 100).roundToInt() / 100f) //getCurrentPrice(stock.companyToken)
            val curMoney = ((stock.amount * curPrice) * 100).roundToInt() / 100f
            currentPrice.text = "$curPrice$"
            money.text = "$curMoney$"
            return curMoney
        }
    }
}