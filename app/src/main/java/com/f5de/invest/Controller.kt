package com.f5de.invest

import android.content.Context
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

open class Controller(context: Context) {

    var allStocks: ArrayList<UserStocks> = ArrayList()
    val data = Data()

    private var callback: Callback? = null

    init {
        val builder = GsonBuilder()
        val gson = builder.create()
        val XmlFileInputStream: InputStream = context.resources.openRawResource(R.raw.nasdaq) // getting XML
        val sxml = readTextFile(XmlFileInputStream)
        val stocks = gson.fromJson(sxml, Array<Stocks>::class.java)
        callback = context as Callback?
        stocks.forEach { it -> allStocks.add(UserStocks(name = it.name, price = it.price.substring(1).toFloat(), amount = it.amount, type = it.type)) }
    }

    fun start(it: Int) {
        data.data.forEach {
            if (data.changed.find { it1 -> it1.name == it.name } == null)
                data.changed.add(UserStocks(it.name, it.price, it.amount, it.type))
        }
        var i = it - 1
        while (i >= 0) {
            data.changed.filter { it.type == 0 }.forEach { it.price = it.price * (1 - ((1..5).random() * (-20..15).random()).toFloat() / 100) }
            i--
        }
        data.changed.forEach { it1 ->
            allStocks.find { it2 ->
                it1.name == it2.name
            }?.price = it1.price
        }
        callback?.refresh()
    }

    fun getAll(): ArrayList<UserStocks> {
        if (data.changed.isEmpty())
            return data.data
        else
            return data.changed
    }

    fun getData(type: Int): ArrayList<UserStocks> {
        return data.data.filter { it -> it.type == type } as ArrayList<UserStocks>
    }

    fun getStocks(type: Int): ArrayList<UserStocks> {
        if (data.changed.isEmpty())
            return data.data.filter { it -> it.type == type } as ArrayList<UserStocks>
        else
            return data.changed.filter { it -> it.type == type } as ArrayList<UserStocks>
    }

    fun addStock(stock: UserStocks) {
        var tmp = data.data.find { it.name == stock.name }
        if (tmp != null) {
            tmp.amount += stock.amount
            tmp.price = (tmp.price + stock.price) / 2
            tmp = data.changed.find { it.name == stock.name }
            if (tmp != null) {
                tmp.amount += stock.amount
            }
        } else {
            data.data.add(stock)
            callback?.refresh()
        }
        //TODO refresh stock fragment
    }

    fun deleteStock(stock: UserStocks) {
        val tmp = data.data.find { it.name == stock.name }
        if (tmp?.amount == stock.amount) {
            data.data.remove(tmp)
            data.changed.remove(stock)
        } else {
            tmp!!.amount -= stock.amount
        }
        callback?.refresh()
        //TODO refresh stock fragment
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


    interface Callback {
        fun refresh()
    }


    companion object {
        private var instance: Controller? = null

        fun getInstance(context: Context): Controller {
            if (instance == null)  // NOT thread safe!
                instance = Controller(context)

            return instance!!
        }
    }
}
