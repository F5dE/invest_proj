package com.f5de.invest.data


data class Deposit(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 1,
    override var income: Float = 0f,
    var time: Int = 12
) : Investment(){
    override fun simulation() {
        price = oldPrice * (1 + ((income / 100f) * (13 - time) / 12f))
        time--
    }
}