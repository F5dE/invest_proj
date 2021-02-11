package com.f5de.invest

import com.google.gson.annotations.SerializedName

data class Stocks(
    @SerializedName("Symbol")
    var symbol: String,
    @SerializedName("Name")
    var name: String,
    @SerializedName("Last Sale")
    var price: String,
    var amount: Int
) {
    override fun toString(): String {
        return this.name
    }
}