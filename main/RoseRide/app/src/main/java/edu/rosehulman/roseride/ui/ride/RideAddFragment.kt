package edu.rosehulman.roseride.ui.ride

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentRideAddBinding
import edu.rosehulman.roseride.model.*
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class RideAddFragment : Fragment() {
    private lateinit var model: RideViewModel
    private lateinit var binding: FragmentRideAddBinding
    private var title = ""
    private var pAddr = "enter address here"
    private var dAddr = "enter address here"
    private var date = "2022-02-01"
    private var time = "00:00"
    private var cost = "-1"
    private var numOfSlots = "1"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model =
            ViewModelProvider(requireActivity()).get(RideViewModel::class.java)

        binding = FragmentRideAddBinding.inflate(inflater, container, false)
        setupButtons()
        updateView()
        return binding.root
    }

    private fun setupButtons() {
        binding.rideAddSubmit
            .setOnClickListener() {
                if(title == "" || pAddr == "enter address here" || dAddr == "enter address here"
                    || Integer.parseInt(cost) < 0){
                    Toast.makeText(
                        context,
                        "Fill in all required!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    title = binding.rideName.text.toString()
                    pAddr = binding.pickUpAddressAnswer.text.toString()
                    dAddr = binding.destinationAddressAnswer.text.toString()
                    date = binding.dateAnswer.text.toString()
                    time =
                        binding.timeAnswer.text.toString() // might consider to add a timepicker here instead
                    cost = binding.costPerPerson.text.toString()
                    numOfSlots = binding.numOfSlots.text.toString()

                    model.addRide(
                        Ride(
                            title,
                            Firebase.auth.uid!!,
                            time + ":00",
                            date,
                            Address(pAddr),
                            //                        "00:00:00",
                            Address(dAddr),
                            listOf(),
                            cost.toDouble(),
                            numOfSlots.toInt(),
                            false
                        )
                    )

                    updateView()
                    findNavController().navigate(R.id.nav_ride)
                }
            }
        binding.timeButton
            .setOnClickListener() {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(10)
                        .setTitleText("Select Set-off time")
                        .build()
                picker.addOnPositiveButtonClickListener {
                    var s: String
                    s = if(picker.hour<10){
                        "0"+picker.hour.toString()+":"
                    }else picker.hour.toString()+":"
                    if(picker.minute<10){
                        s = s+"0"+picker.minute.toString()
                    }else s += picker.minute.toString()
                    binding.timeAnswer.text = s
                }
                picker.show(parentFragmentManager, "tag")
            }

        binding.dateButton
            .setOnClickListener() {
                val picker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Set-off date")
                        .build()
                picker.addOnPositiveButtonClickListener {
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    val s = dateFormatter.format(Date(it+86400000))
                    binding.dateAnswer.text = s
                }
                picker.show(parentFragmentManager, "tag")
            }

        Places.initialize(context, "AIzaSyAKKb9_jLm6QSktSq7hBmPP48Bzcbr4iVg")

        binding.pickUpAddressAnswer.setFocusable(false)
        binding.pickUpAddressAnswer.setOnClickListener() {
            Log.d("pickUpAddress", "Clicked")
            var fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            var intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                fieldList)
                .setTypeFilter(TypeFilter.ADDRESS)
                .setCountry("USA")
                .build(context)

            startActivityForResult(intent, 100);
        }

        binding.destinationAddressAnswer.setFocusable(false)
        binding.destinationAddressAnswer.setOnClickListener() {
            Log.d("pickUpAddress", "Clicked")
            var fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            var intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                fieldList)
                .setTypeFilter(TypeFilter.ADDRESS)
                .setCountry("USA")
                .build(context)

            startActivityForResult(intent, 101);
        }


    }

    private fun updateView() {
        Log.d("RR", "Add Ride update view")
        binding.rideName.setText(title)
        binding.pickUpAddressAnswer.setText(pAddr)
        binding.destinationAddressAnswer.setText(dAddr)
        binding.dateAnswer.setText(date)
        binding.timeAnswer.setText(time)
        binding.costPerPerson.setText(cost)
        binding.numOfSlots.setText(numOfSlots)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data)
            Log.d("what", place.address)
            binding.pickUpAddressAnswer.setText(place.address)
        }
        else if(requestCode == 101 && resultCode == Activity.RESULT_OK){
            val place = Autocomplete.getPlaceFromIntent(data)
            binding.destinationAddressAnswer.setText(place.address)
        }
    }

}