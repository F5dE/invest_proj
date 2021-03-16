package com.f5de.invest.data


data class Bond(
    override var name: String = "",
    override var price: Float = 0f,
    override var oldPrice: Float = 0f,
    override var amount: Int = 0,
    override var type: Int = 2,
    override var income: Float = 0f,
    var time: Int = 6
) : Investment(){
    override fun simulation() {
        time--
        if (time == 0) {
            price = (oldPrice * (1 + income/100f))
        }else
            price *= (1 - ((-10..10).random()/1000f))
    }
}
