package com.f5de.invest

import com.google.gson.annotations.SerializedName

data class Stocks(
    @SerializedName("Symbol")
    var symbol: String,
    @SerializedName("Name")
    var name: String,
    @SerializedName("Last Sale")
    var price: String,
    var amount: Int,
    val type: Int //1 - stock, 2 - bond, 3 - currency, 4 - materials
) {
    override fun toString(): String {
        return this.name
    }
}