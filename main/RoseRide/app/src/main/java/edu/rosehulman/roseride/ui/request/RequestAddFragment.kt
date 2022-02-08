package edu.rosehulman.roseride.ui.request

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import edu.rosehulman.roseride.Constants
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentRequestEditBinding
import edu.rosehulman.roseride.ui.model.Address
import edu.rosehulman.roseride.ui.model.Request
import edu.rosehulman.roseride.ui.model.RequestViewModel
import edu.rosehulman.roseride.ui.model.User
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat

class RequestAddFragment : Fragment() {
    private lateinit var model: RequestViewModel
    private lateinit var binding: FragmentRequestEditBinding
    private var title = ""
    private var pAddr = "street, city, zip, state"
    private var dAddr = "street, city, zip, state"
    private var date = "2022-02-01"
    private var time = "00:00"
    private var minPrice = "-1"
    private var maxPrice = "-1"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model =
            ViewModelProvider(requireActivity()).get(RequestViewModel::class.java)

        binding = FragmentRequestEditBinding.inflate(inflater, container, false)
        setupButtons()
        updateView()
        return binding.root
    }

    private fun setupButtons() {
        binding.requestEditSubmit
            .setOnClickListener(){
                title = binding.requestName.text.toString()
                pAddr = binding.pickUpAddressAnswer.text.toString()
                dAddr = binding.destinationAddressAnswer.text.toString()
                date = binding.dateAnswer.text.toString()
                time = binding.timeAnswer.text.toString() // might consider to add a timepicker here instead
                minPrice = binding.minPrice.text.toString()
                maxPrice = binding.maxPrice.text.toString()


                model.addRequest(
                    Request(
                        title,
                        User("Steven","812-223-7777", "fengr@rose-hulman.edu"),
                        time + ":00",
                        date,
                        Address(pAddr),
                        1,
                        Address(dAddr),
                        false,
                        minPrice.toDouble(),
                        maxPrice.toDouble(),
                        false
                    )
                )

                updateView()
                findNavController().navigate(R.id.nav_request)
            }
        binding.timeButton
            .setOnClickListener(){
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(10)
                        .setTitleText("Select Set-off time")
                        .build()
                picker.addOnPositiveButtonClickListener{
//                    val s = String.format("%2d:%2d", picker.hour, picker.minute)

                    var s: String
                    s = if(picker.hour<10){
                        "0"+picker.hour.toString()+":"
                    }else picker.hour.toString()+":"
                    if(picker.minute<10){
                        s = s+"0"+picker.minute.toString()
                    }else s += picker.minute.toString()
                    binding.timeAnswer.text = s
                }
                picker.show(parentFragmentManager, "tag");
            }

        binding.dateButton
            .setOnClickListener(){
                val picker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Set-off date")
                        .build()
                picker.addOnPositiveButtonClickListener{
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                    val s = dateFormatter.format(Date(it+86400000))
                    binding.dateAnswer.text = s
                }
                picker.show(parentFragmentManager, "tag")
            }

    }

    private fun updateView() {
        Log.d(Constants.TAG,"in detail update view")
        binding.requestName.setText(title)
        binding.pickUpAddressAnswer.setText(pAddr)
        binding.destinationAddressAnswer.setText(dAddr)
        binding.dateAnswer.setText(date)
        binding.timeAnswer.setText(time)
        binding.minPrice.setText(minPrice)
        binding.maxPrice.setText(maxPrice)
    }

}