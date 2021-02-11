package com.f5de.invest

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
import kotlin.math.absoluteValue

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StockFragment : Fragment() {

    private lateinit var stockCardHolder: LinearLayout

    var data: ArrayList<Stock>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        data = Controller.data
        return inflater.inflate(R.layout.fragment_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stockCardHolder = view.findViewById(R.id.stock_card_holder) as LinearLayout
        var card = layoutInflater.inflate(R.layout.card_stock, null)
        stockCardHolder.addView(card, 0)
        if (data != null) {
            card = layoutInflater.inflate(R.layout.card_stock, null)
            val holder = CardHolder(card)
            holder.companyName.text = data!![0].name
            val curChange = holder.calculateMoney(data!![0])
            if (curChange < 0) {
                holder.change.text = "${curChange.absoluteValue}$"
                holder.change.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_500))
                holder.changeImage.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24)
            } else {
                holder.change.text = "$curChange$"
                holder.change.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_500))
                holder.changeImage.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24)
            }
            holder.stockAmount.text = data!![0].stockAmount.toString()
            holder.companyImage.setBackgroundResource(R.drawable.ic_apple_logo_black)
            stockCardHolder.addView(card, 1)
        }

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
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

        fun calculateMoney(stock: Stock): Float {
            val curPrice = 128f //getCurrentPrice(stock.companyToken)
            val curMoney = stock.stockAmount.times(curPrice)
            currentPrice.text = "$curPrice$"
            money.text = "$curMoney$"
            return curMoney - stock.money
        }
    }
}