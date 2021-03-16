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
import kotlin.math.roundToInt


class AddDialog : DialogFragment() {

    var type: Int = 0 //stocks
    var spinner: Spinner? = null
    var typeSpinner: Spinner? = null

    lateinit var stockAdapter: ArrayAdapter<Stockk>
    lateinit var currencyAdapter: ArrayAdapter<CharSequence>
    lateinit var bondAdapter: ArrayAdapter<CharSequence>
    lateinit var metalAdapter: ArrayAdapter<CharSequence>
    lateinit var time: LinearLayout
    lateinit var timeAmount: TextView

    val LOG_TAG = "myLogs"

    lateinit var controller: Controller
    var stockId = 0
    var tmpStock: UserStocks = Stockk()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.setTitle(R.string.dialog_label)
        controller = Controller.getInstance(requireContext())
        tmpStock = Stockk()
        val v: View = inflater.inflate(R.layout.dialog_add, null)
        val currentPrice = v.findViewById<TextView>(R.id.dialog_current_price)
        val amount = v.findViewById<EditText>(R.id.dialog_amount)
        val totalPrice = v.findViewById<TextView>(R.id.dialog_total_price)
        val moneyLeft = v.findViewById<TextView>(R.id.dialog_money_left)
        time = v.findViewById(R.id.dialog_time)
        timeAmount = v.findViewById(R.id.dialog_total_time)
        moneyLeft.text = controller.data.freeMoney.toString()

        v.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dialog?.dismiss()
        }

        stockAdapter = ArrayAdapter<Stockk>(requireContext(), android.R.layout.simple_spinner_item, controller.allStocks)
        currencyAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.currency, android.R.layout.simple_spinner_item)
        bondAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.bond, android.R.layout.simple_spinner_item)
        metalAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.metal, android.R.layout.simple_spinner_item)

        spinner = v.findViewById(R.id.spinner_main)
        typeSpinner = v.findViewById(R.id.spinner_type)

        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        metalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        ArrayAdapter.createFromResource(requireContext(), R.array.types, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                typeSpinner?.adapter = adapter
            }
        stockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
        setTypeSpinner(type)
        typeSpinner?.post { typeSpinner?.setSelection(type) }
        typeSpinner?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                setTypeSpinner(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) { }
        }

        setStockSpinner(0)
        tmpStock.type = type

        spinner?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                setStockSpinner(position)
                tmpStock.oldPrice = tmpStock.price
                currentPrice.text = tmpStock.price.toString()
                val tPrice = tmpStock.price * tmpStock.amount
                totalPrice.text = ((tPrice * 100).roundToInt() / 100f).toString()
                moneyLeft.text = (((controller.data.freeMoney - tPrice) * 100).roundToInt() / 100f).toString()
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
                    val tPrice = tmpStock.price * tmpStock.amount
                    totalPrice.text = ((tPrice * 100).roundToInt() / 100f).toString()
                    moneyLeft.text = (((controller.data.freeMoney - tPrice) * 100).roundToInt() / 100f).toString()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        val button = v.findViewById<Button>(R.id.button_add)
        button.setOnClickListener {
            if (tmpStock.amount != 0 && tmpStock.amount * tmpStock.price <= controller.data.freeMoney) {
                controller.data.freeMoney -= tmpStock.amount * tmpStock.price
                controller.addStock(tmpStock)
                dialog?.dismiss()
            } else {
                Toast.makeText(requireContext(), "Enter correct values!", Toast.LENGTH_LONG).show()
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

    fun setTypeSpinner(id: Int){
        when (id) {
            0 -> {//Stocks
                spinner?.adapter = stockAdapter
                tmpStock = Stockk()
                time.visibility = View.GONE
            }
            1 -> {//Currency
                spinner?.adapter = currencyAdapter
                tmpStock = Currency()
                time.visibility = View.VISIBLE
                timeAmount.text = "12"
            }
            2 -> {//Bond
                spinner?.adapter = bondAdapter
                tmpStock = Bond()
                time.visibility = View.VISIBLE
                timeAmount.text = "6"
            }
            3 -> {//Materials
                spinner?.adapter = metalAdapter
                tmpStock = Metal()
                time.visibility = View.GONE
            }
        }
        type = id
    }

    fun setStockSpinner(position: Int){
        when (type){
            0 -> {
                tmpStock.price = controller.allStocks[position].price
                tmpStock.name = controller.allStocks[position].name
            }
            1 -> {
                tmpStock.price = controller.allCurrency!![position].price
                tmpStock.name = controller.allCurrency!![position].name
                tmpStock.income = controller.allCurrency!![position].income
            }
            2 -> {
                tmpStock.price = controller.allBond!![position].price
                tmpStock.name = controller.allBond!![position].name
                tmpStock.income = controller.allBond!![position].income
            }
            3 -> {
                tmpStock.price = controller.allMetal!![position].price
                tmpStock.name = controller.allMetal!![position].name
            }
        }
    }

}