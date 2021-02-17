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

    private val data: ArrayList<UserStocks> = ArrayList()
    private var changed: ArrayList<UserStocks> = ArrayList()
    var allStocks: ArrayList<UserStocks> = ArrayList()

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
        data.forEach { changed.add(UserStocks(it.name, it.price, it.amount, it.type)) }
        var i = it - 1
        while (i >= 0) {
            changed.filter { it.type == 0 }.forEach { it.price = it.price * (1 - ((0..5).random() * (-3..3).random()).toFloat() / 100) }
            i--
        }
    }

    fun getAll(): ArrayList<UserStocks> {
        if (changed.isEmpty())
            return data
        else
            return changed
    }

    fun getData(type: Int): ArrayList<UserStocks> {
        return data.filter { it -> it.type == type } as ArrayList<UserStocks>
    }

    fun getStocks(type: Int): ArrayList<UserStocks> {
        if (changed.isEmpty())
            return data.filter { it -> it.type == type } as ArrayList<UserStocks>
        else
            return changed.filter { it -> it.type == type } as ArrayList<UserStocks>
    }

    fun addStock(stock: UserStocks) {
        data.add(stock)
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
