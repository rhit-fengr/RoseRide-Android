package edu.rosehulman.roseride.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import edu.rosehulman.roseride.databinding.FragmentRideEditBinding
import edu.rosehulman.roseride.ui.model.Address
import edu.rosehulman.roseride.ui.model.RideViewModel
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat

class RideEditFragment : Fragment() {
    private lateinit var model: RideViewModel
    private lateinit var binding: FragmentRideEditBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model =
            ViewModelProvider(requireActivity()).get(RideViewModel::class.java)

        binding = FragmentRideEditBinding.inflate(inflater, container, false)
        setupButtons()
        updateView()
        return binding.root
    }

    private fun setupButtons() {
        binding.rideEditSubmit
            .setOnClickListener() {
                var title = binding.rideName.text.toString()
                var pAddr = binding.pickUpAddressAnswer.text.toString()
                var dAddr = binding.destinationAddressAnswer.text.toString()
                var date = binding.dateAnswer.text.toString()
                var time = binding.timeAnswer.text.toString() // might consider to add a timepicker here instead
                var cost = binding.costPerPerson.text.toString()
                var numOfSlots = binding.numOfSlots.text.toString()

                model.updateCurrentRide(
                        title,
                        Time.valueOf(time + ":00"),
                        Date.valueOf(date),
                        Address(pAddr),
                        Address(dAddr),
                        listOf(),
                        cost.toDouble(),
                        numOfSlots.toInt(),
                        false
                )

                updateView()
//                findNavController().navigate(R.id.navigation_ride_edit)
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
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                    val s = dateFormatter.format(Date(it))
                    binding.dateAnswer.text = s
                }
                picker.show(parentFragmentManager, "tag")
            }

    }

    private fun updateView() {
        Log.d("RR", "Ride Edit Page update view")
        val current = model.getCurrentRide()
        binding.rideName.setText(current.title)
        binding.pickUpAddressAnswer.setText(current.pickUpAddr.toString())
        binding.destinationAddressAnswer.setText(current.addr.toString())
        binding.dateAnswer.setText(current.setOffDate.toString())
        binding.timeAnswer.setText(current.setOffTime.toString().substring(0,current.setOffTime.toString().length-3))
        binding.costPerPerson.setText(current.costPerPerson.toString())
        binding.numOfSlots.setText(current.numOfSlots.toString())
    }

}