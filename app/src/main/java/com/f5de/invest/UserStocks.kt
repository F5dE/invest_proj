package com.f5de.invest

abstract class UserStocks{
    abstract var name: String
    abstract var price: Float
    abstract var oldPrice: Float
    abstract var amount: Int
    abstract var type: Int //0 - stock, 1 - bond, 2 - currency, 3 - materials

    abstract fun simulation()

    override fun toString(): String {
        return this.name
    }
}

data class Stockk(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 0
) : UserStocks(){
    override fun simulation() {
        price *= (1 - ((1..5).random() * (-20..15).random()).toFloat() / 100)
    }
}

data class Currency(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 0,
    var income: Float = 0f
) : UserStocks(){
    override fun simulation() {
        price *= (1 - ((-20..20).random()/1000f))
    }
}

data class Bond(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 0,
    var income: Float = 0f,
    var time: Int = 1
) : UserStocks(){
    override fun simulation() {
        price = (oldPrice * (1 + income))*time/12f
    }
}

data class Metal(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 0
) : UserStocks(){
    override fun simulation() {
        price *= (1 - ((-20..20).random()/1000f))
    }
}