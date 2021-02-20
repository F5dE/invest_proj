package com.f5de.invest

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.DialogFragment


class AddDialog : DialogFragment() {

    var type: Int = 0 //stocks

    val LOG_TAG = "myLogs"

    lateinit var controller: Controller
    var stockId = 0
    var tmpStock = UserStocks()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.setTitle(R.string.dialog_label)
        val v: View = inflater.inflate(R.layout.dialog_add, null)
        val typeSpinner: Spinner = v.findViewById(R.id.spinner_type)
        val spinner = v.findViewById<Spinner>(R.id.spinner_main)
        val currentPrice = v.findViewById<TextView>(R.id.dialog_current_price)
        val amount = v.findViewById<EditText>(R.id.dialog_amount)
        val totalPrice = v.findViewById<TextView>(R.id.dialog_total_price)
        tmpStock = UserStocks()
        controller = Controller.getInstance(requireContext())

        ArrayAdapter.createFromResource(requireContext(), R.array.types, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                typeSpinner.adapter = adapter
            }
        typeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                when (id) {
                    0L -> type = 0//Stocks
                    1L -> type = 1//Bond
                    2L -> type = 2//Currency
                    3L -> type = 3//Materials
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) { }
        }

        val spinnerArrayAdapter: ArrayAdapter<UserStocks> = ArrayAdapter<UserStocks>(requireContext(), android.R.layout.simple_spinner_item, controller.allStocks)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
        spinner.adapter = spinnerArrayAdapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                tmpStock.price = controller.allStocks[position].price
                tmpStock.name = controller.allStocks[position].name
                currentPrice.text = tmpStock.price.toString()
                totalPrice.text = (tmpStock.price * tmpStock.amount).toString()
                stockId = position
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    tmpStock.amount = Integer.parseInt(s.toString())
                    totalPrice.text = (tmpStock.price * tmpStock.amount).toString()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        val button = v.findViewById<Button>(R.id.button_add)
        button.setOnClickListener {
            if (tmpStock.amount != 0) {
                controller.addStock(tmpStock)
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