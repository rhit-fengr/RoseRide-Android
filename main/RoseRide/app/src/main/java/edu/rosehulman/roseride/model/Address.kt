package edu.rosehulman.roseride.model

data class Address(
    var streetAddress: String="",
    var city: String="",
    var state: String="",
    var country: String="",) {

    // from https://stackoverflow.com/questions/59959101/kotlin-data-class-secondary-constructor-init-block
    private constructor(splitted: List<String>) : this(splitted[0], splitted[1], splitted[2], splitted[3])
    constructor(addr: String) : this(addr.split(", "))

    override fun toString(): String {
        return if (streetAddress.isNotBlank()) "$streetAddress, $city, $state, $country" else ""
    }

    fun toStringBeautified(): String {
        return if (streetAddress.isNotBlank()) "$streetAddress\n$city, $state\n$country" else ""
    }
}