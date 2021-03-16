package com.f5de.invest

abstract class UserStocks{
    abstract var name: String
    abstract var price: Float
    abstract var oldPrice: Float
    abstract var amount: Int
    abstract var type: Int //0 - stock, 1 - bond, 2 - currency, 3 - materials
    abstract var income: Float

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
    override var type: Int = 0,
    override var income: Float = 0f
) : UserStocks(){
    override fun simulation() {
        price *= (1 - ((1..5).random() * (-20..15).random()).toFloat() / 100)
    }
    override fun toString(): String {
        return this.name
    }
}

data class Currency(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 1,
    override var income: Float = 0f,
    var time: Int = 12
) : UserStocks(){
    override fun simulation() {
        price = oldPrice * (1 + ((income / 100f) * (13 - time) / 12f))
        time--
    }
}

data class Bond(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 2,
    override var income: Float = 0f,
    var time: Int = 6
) : UserStocks(){
    override fun simulation() {
        time--
        if (time == 0) {
            price = (oldPrice * (1 + income/100f))
        }else
            price *= (1 - ((-10..10).random()/1000f))
    }
}

data class Metal(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 3,
    override var income: Float = 0f
) : UserStocks(){
    override fun simulation() {
        price *= (1 - ((-20..20).random()/1000f))
    }
}