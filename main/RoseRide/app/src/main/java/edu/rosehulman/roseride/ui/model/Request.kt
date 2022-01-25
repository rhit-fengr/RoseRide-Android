package edu.rosehulman.roseride.ui.model

import java.sql.Time

data class Request(var title: String="", var user: User, var setOffTime: Time, var pickUpAddr: Address, var returnTime: Time, var numOfPassengers: Int=1, var destinationName: String="", var destinationAddr: Address, var sharable: Boolean = false, var minPrice: Double=-1.0, var maxPrice: Double=-1.0 ,var isSelected: Boolean = false){

}