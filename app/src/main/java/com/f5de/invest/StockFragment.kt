package com.f5de.invest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

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
        val card = layoutInflater.inflate(R.layout.card_stock, null)
        stockCardHolder.addView(card, 0)
//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }
}