package com.f5de.invest.data

abstract class Investment{
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