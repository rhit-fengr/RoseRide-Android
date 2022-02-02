package edu.rosehulman.roseride.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentRideAddBinding
import edu.rosehulman.roseride.ui.model.*
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class RideAddFragment : Fragment() {
    private lateinit var model: RideViewModel
    private lateinit var binding: FragmentRideAddBinding
    private var title = ""
    private var pAddr = "street, city, zip, state"
    private var dAddr = "street, city, zip, state"
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
                title = binding.rideName.text.toString()
                pAddr = binding.pickUpAddressAnswer.text.toString()
                dAddr = binding.destinationAddressAnswer.text.toString()
                date = binding.dateAnswer.text.toString()
                time = binding.timeAnswer.text.toString() // might consider to add a timepicker here instead
                cost = binding.costPerPerson.text.toString()
                numOfSlots = binding.numOfSlots.text.toString()

                model.addRide(
                    Ride(
                        title,
                        User("Steven", "812-223-7777", "fengr@rose-hulman.edu"),
                        Time.valueOf(time + ":00"),
                        Date.valueOf(date),
                        Address(pAddr),
                        Time(0),
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
                    val s = String.format("%2d:%2d", picker.hour, picker.minute)
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

}