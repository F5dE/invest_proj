package com.f5de.invest

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StockFragment : Fragment() {

    private lateinit var stockCardHolder: LinearLayout

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stockCardHolder = view.findViewById(R.id.stock_card_holder) as LinearLayout
        var card = layoutInflater.inflate(R.layout.card_stock, null)
        stockCardHolder.addView(card, 0)
        card = layoutInflater.inflate(R.layout.card_stock, null)
        val holder = CardHolder(card)
        holder.companyName.text = "Apple"
        holder.money.text = "1800$"
        holder.change.text = "160$"
        holder.change.setTextColor(resources.getColor(R.color.red_500))
        holder.changeImage.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24)
        holder.stockAmount.text = "14"
        holder.currentPrice.text = "128.57$"
        holder.companyImage.setBackgroundResource(R.drawable.ic_apple_logo_black)
        stockCardHolder.addView(card, 1)

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
    }
}