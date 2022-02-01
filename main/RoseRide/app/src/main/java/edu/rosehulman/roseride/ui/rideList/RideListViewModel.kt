package edu.rosehulman.roseride.ui.rideList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RideListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Some rides should show up here"
    }
    val text: LiveData<String> = _text
}