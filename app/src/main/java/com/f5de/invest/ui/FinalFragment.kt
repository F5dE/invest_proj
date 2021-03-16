package com.f5de.invest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.f5de.invest.Controller
import com.f5de.invest.R
import com.f5de.invest.data.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


class FinalFragment : Fragment() {

    var controller: Controller? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val finalDays = view.findViewById<TextView>(R.id.final_days)
        val startMoney = view.findViewById<TextView>(R.id.final_start_money)
        val endMoney = view.findViewById<TextView>(R.id.final_end_money)
        val change = view.findViewById<TextView>(R.id.final_change)
        val button = view.findViewById<Button>(R.id.final_restart)
        controller = Controller.getInstance(requireContext())
        finalDays.text = controller?.data?.timeStart.toString()
        startMoney.text = controller?.data?.startMoney.toString()
        endMoney.text = (((controller!!.data.freeMoney + controller!!.data.stockMoney) * 100).roundToInt() / 100f).toString()
        val changeM = controller!!.data.startMoney - controller!!.data.freeMoney - controller!!.data.stockMoney
        change.text = ((changeM.absoluteValue * 100).roundToInt() / 100f).toString()
        if (changeM < 0) {
            change?.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_500))
        } else {
            change?.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_500))
        }
        button.setOnClickListener {
            controller?.restart()
        }
    }


}