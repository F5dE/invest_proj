package com.f5de.invest.ui

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.f5de.invest.Controller
import com.f5de.invest.data.Investment
import com.f5de.invest.R
import kotlin.math.roundToInt
import com.f5de.invest.data.*

class SellDialog : DialogFragment() {

    var type: Int = 0 //stocks

    val LOG_TAG = "myLogs"

    lateinit var controller: Controller
    var stockId = 0
    var tmpStock: Investment = Stockk()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.setTitle(R.string.dialog_label)
        val v: View = inflater.inflate(R.layout.dialog_sell, null)
        val dialogName = v.findViewById<TextView>(R.id.dialog_name)
        val currentPrice = v.findViewById<TextView>(R.id.dialog_current_price)
        val possible = v.findViewById<TextView>(R.id.dialog_possible_amount)
        val amount = v.findViewById<EditText>(R.id.dialog_amount)
        val totalPrice = v.findViewById<TextView>(R.id.dialog_total_price)
        tmpStock = Stockk()
        controller = Controller.getInstance(requireContext())
        val name = arguments?.get("stock").toString()
        val tmp = controller.getData(type).find { it.name == name }!!
        tmpStock.name = name
        tmpStock.price = tmp.price
        tmpStock.amount = 0
        tmpStock.type = tmp.type
        dialogName.text = name
        currentPrice.text = tmpStock.price.toString()
        possible.text = tmp.amount.toString()
        amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    tmpStock.amount = Integer.parseInt(s.toString())
                    totalPrice.text = (((tmpStock.price * tmpStock.amount) * 100).roundToInt() / 100f).toString()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (s.length != 0) field2.setText("")
            }
        })

        val button = v.findViewById<Button>(R.id.button_add)
        button.setOnClickListener {
            if (tmpStock.amount != 0 && tmp.amount >= tmpStock.amount) {
                controller.deleteStock(tmpStock)
                dialog?.dismiss()
            } else {
                Toast.makeText(requireContext(), "Enter correct amount!", Toast.LENGTH_SHORT).show()
            }
        }

        return v
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d(LOG_TAG, "Dialog 1: onDismiss")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Log.d(LOG_TAG, "Dialog 1: onCancel")
    }
}