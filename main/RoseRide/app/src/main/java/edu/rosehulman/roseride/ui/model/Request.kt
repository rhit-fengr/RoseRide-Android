package edu.rosehulman.roseride.ui.model

import java.sql.Time
import java.sql.Date

data class Request(
    var title: String="",
    var user: User,
    var setOffTime: Time,
    var setOffDate: Date,
    var pickUpAddr: Address,
    var numOfPassengers: Int=1,
    var destinationAddr: Address,
    var sharable: Boolean = false,
    var minPrice: Double=-1.0,
    var maxPrice: Double=-1.0,
    var isSelected: Boolean = false){

}