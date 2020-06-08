package com.milk.vendor.common.model

data class ProductList(
    val id: Int=0,
    val name: String="",
    val quantity: String="",
    val image : String="",
    val amount : Int=0,
    var count : Int=0
)