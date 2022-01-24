package edu.rosehulman.roseride.ui.model

data class Address(var streetAddress: String="", var city: String="", var ZIP: String="", var state: String="") {
    override fun toString(): String {
        return if (streetAddress.isNotBlank()) "'$streetAddress', city: '$city', ZIP: '$ZIP'" else ""
    }
}