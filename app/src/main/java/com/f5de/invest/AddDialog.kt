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
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream


class AddDialog : DialogFragment() {

    var type: Int = 0 //stocks

    val LOG_TAG = "myLogs"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setTitle(R.string.dialog_label)
        val v: View = inflater.inflate(R.layout.dialog_add, null)
        val typeSpinner: Spinner = v.findViewById(R.id.spinner_type)
        val spinner = v.findViewById<Spinner>(R.id.spinner_main)
        val currentPrice = v.findViewById<TextView>(R.id.dialog_current_price)
        val amount = v.findViewById<EditText>(R.id.dialog_amount)
        val totalPrice = v.findViewById<TextView>(R.id.dialog_total_price)
        val builder = GsonBuilder()
        val gson = builder.create()
        ArrayAdapter.createFromResource(requireContext(), R.array.types, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                typeSpinner.adapter = adapter
            }
        typeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                when (id) {
                    0L -> print(1)//Stocks
                    1L -> print(1)//Bond
                    2L -> print(1)//Currency
                    3L -> print(1)//Materials
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

// Create an ArrayAdapter using the string array and a default spinner layout

        val file = File("res/raw/nasdaq.json")
        val XmlFileInputStream: InputStream = resources.openRawResource(R.raw.nasdaq) // getting XML
        val sxml = readTextFile(XmlFileInputStream)
        val stocks = gson.fromJson(sxml, Array<Stocks>::class.java)
        val spinnerArrayAdapter: ArrayAdapter<Stocks> = ArrayAdapter<Stocks>(requireContext(), android.R.layout.simple_spinner_item, stocks)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
        spinner.adapter = spinnerArrayAdapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                currentPrice.text = stocks[position].price
                totalPrice.text = (stocks[position].price.substring(1).toFloat() * Integer.parseInt(amount.text.toString())).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length != 0)
                    totalPrice.text = (currentPrice.text.toString().substring(1).toFloat() * Integer.parseInt(s.toString())).toString()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
//                if (s.length != 0) field2.setText("")
            }
        })

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

    fun readTextFile(inputStream: InputStream): String {
        val outputStream = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var len: Int
        try {
            while (inputStream.read(buf).also { len = it } != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
        }
        return outputStream.toString()
    }
}