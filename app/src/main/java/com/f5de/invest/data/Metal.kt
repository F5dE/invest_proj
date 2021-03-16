package com.f5de.invest.data


data class Metal(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 3,
    override var income: Float = 0f
) : Investment(){
    override fun simulation() {
        price *= (1 - ((-20..20).random()/1000f))
    }
}