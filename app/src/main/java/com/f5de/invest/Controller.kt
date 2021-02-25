package com.f5de.invest

import android.content.Context
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

open class Controller(context: Context) {

    var allStocks: ArrayList<Stockk> = ArrayList()
    var allCurrency: Array<Currency>? = null
    var allBond: Array<Bond>? = null
    var allMetal: Array<Metal>? = null
    val data = Data()
    var timeHandler: Int = 0
    var flag: Boolean = false

    private var callback: Callback? = null

    init {
        callback = context as Callback?
        val builder = GsonBuilder()
        val gson = builder.create()
        var XmlFileInputStream: InputStream = context.resources.openRawResource(R.raw.nasdaq) // getting XML
        var sxml = readTextFile(XmlFileInputStream)
        val stocks = gson.fromJson(sxml, Array<Stocks>::class.java)
        stocks.forEach { allStocks.add(Stockk(name = it.name, price = it.price.substring(1).toFloat(), amount = it.amount, type = it.type)) }

        XmlFileInputStream = context.resources.openRawResource(R.raw.currency)
        sxml = readTextFile(XmlFileInputStream)
        allCurrency = gson.fromJson(sxml, Array<Currency>::class.java)
        XmlFileInputStream = context.resources.openRawResource(R.raw.bond)
        sxml = readTextFile(XmlFileInputStream)
        allBond = gson.fromJson(sxml, Array<Bond>::class.java)
        XmlFileInputStream = context.resources.openRawResource(R.raw.metal)
        sxml = readTextFile(XmlFileInputStream)
        allMetal = gson.fromJson(sxml, Array<Metal>::class.java)
        var i = 1
    }

    fun setSimulation(time: Int) {
        timeHandler = time
        callback?.initiate()
    }

    fun step() {
        flag = true
        data.changeMoney = 0f
        data.data.forEach {
            it.simulation()
            data.changeMoney += (it.price - it.oldPrice) * it.amount
        }
        data.data.forEach { it1 ->
            allStocks.find { it2 ->
                it1.name == it2.name
            }?.price = it1.price
        }
        timeHandler--
        callback?.refresh()
    }

    fun getAll(): ArrayList<UserStocks> {
        return data.data
    }

    fun getData(type: Int): ArrayList<UserStocks> {
        return data.data.filter { it.type == type } as ArrayList<UserStocks>
    }

    fun addStock(stock: UserStocks) {
        val tmp = data.data.find { it.name == stock.name }
        if (tmp != null) {
            tmp.amount += stock.amount
            tmp.price = (tmp.price + stock.price) / 2
            tmp.oldPrice = (tmp.oldPrice + stock.price) / 2
        } else {
            data.data.add(stock)
        }
        data.stockMoney += stock.amount * stock.price
        callback?.refresh()
        //TODO refresh stock fragment
    }

    fun deleteStock(stock: UserStocks) {
        if (flag) {
            data.freeMoney += stock.amount * stock.price
        }
        val tmp = data.data.find { it.name == stock.name }
        if (tmp?.amount == stock.amount) {
            data.data.remove(tmp)
        } else {
            tmp!!.amount -= stock.amount
        }
        callback?.refresh()
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
        fun initiate()
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
