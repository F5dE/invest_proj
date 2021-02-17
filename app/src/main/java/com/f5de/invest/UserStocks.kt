package com.f5de.invest

data class UserStocks (
    var name: String = "",
    var price: Float = 0f,
    var amount: Int = 0,
    val type: Int = 0 //0 - stock, 1 - bond, 2 - currency, 3 - materials
){
    override fun toString(): String {
        return this.name
    }
}