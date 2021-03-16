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
import com.f5de.invest.Controller
import com.f5de.invest.data.Investment
import com.f5de.invest.R
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import com.f5de.invest.data.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StockFragment : FragmentEx() {

    private lateinit var stockCardHolder: LinearLayout
    var controller: Controller? = null
    var stocks: ArrayList<Investment> = ArrayList()
    val sellDialog = SellDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock, container, false)
    }

    override fun update(){
        if (controller != null) {
            stocks = controller?.getData(0)!!
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

    fun addCard(userStock: Investment, index: Int) {
        val card = layoutInflater.inflate(R.layout.card_stock, null)
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
        holder.stockAmount.text = "${userStock.amount} акций"
        holder.companyImage.setBackgroundResource(R.drawable.ic_apple_logo_black)
        stockCardHolder.addView(card, index)
        var counter = 0
        card.setOnClickListener {
            counter++
            if (counter == 2) {
                counter = 0
                val bundle = Bundle()
                bundle.putString("stock", userStock.name)
                sellDialog.arguments = bundle
                fragmentManager?.let { it1 -> sellDialog.show(it1, "sellDialog") }
            }
        }
    }

    fun updateCards(stock: ArrayList<Investment>?) {
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
            v.findViewById<TextView>(R.id.stock_card_amount).text = stock[i].amount.toString()
            i++
        }
    }

    private class CardHolder(view: View) {
        val root: CardView = view.findViewById(R.id.stock_card)
        val companyName: TextView = view.findViewById(R.id.stock_card_name)
        val money: TextView = view.findViewById(R.id.stock_card_money)
        val stockAmount: TextView = view.findViewById(R.id.stock_card_amount)
        val currentPrice: TextView = view.findViewById(R.id.stock_card_current_price)
        val change: TextView = view.findViewById(R.id.stock_card_change)
        val changeImage: ImageView = view.findViewById(R.id.stock_card_image_change)
        val companyImage: ImageView = view.findViewById(R.id.stock_card_image_company)

        fun calculateMoney(stock: Investment): Float {
            val curPrice = ((stock.price * 100).roundToInt() / 100f) //getCurrentPrice(stock.companyToken)
            val curMoney = ((stock.amount * curPrice * 100).roundToInt() / 100f)
            currentPrice.text = "$curPrice$"
            money.text = "$curMoney$"
            return curMoney
        }
    }
}