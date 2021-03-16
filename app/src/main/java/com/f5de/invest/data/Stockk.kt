package com.f5de.invest.data


data class Stockk(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 0,
    override var income: Float = 0f
) : Investment(){
    override fun simulation() {
        price *= (1 - ((1..5).random() * (-20..15).random()).toFloat() / 100)
    }
    override fun toString(): String {
        return this.name
    }
}