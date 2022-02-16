package edu.rosehulman.roseride.ui.request

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.Constants
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentRequestAddBinding
import edu.rosehulman.roseride.databinding.FragmentRequestEditBinding
import edu.rosehulman.roseride.model.Address
import edu.rosehulman.roseride.model.Request
import edu.rosehulman.roseride.model.RequestViewModel
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.datepicker.CompositeDateValidator

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator

import com.google.android.material.datepicker.DateValidatorPointBackward

import com.google.android.material.datepicker.DateValidatorPointForward




class RequestAddFragment : Fragment() {
    private lateinit var model: RequestViewModel
    private lateinit var binding: FragmentRequestAddBinding
    private var title = ""
    private var pAddr = "enter address here"
    private var dAddr = "enter address here"
    private var date = SimpleDateFormat("yyyy-MM-dd").format(Date())
    private var time = SimpleDateFormat("HH:mm").format(Date())
    private var minPrice = "-1"
    private var maxPrice = "-1"
    private var sharable = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(requireActivity()).get(RequestViewModel::class.java)

        binding = FragmentRequestAddBinding.inflate(inflater, container, false)
        setupButtons()
        updateView()
        return binding.root
    }

    private fun setupButtons() {
        binding.requestEditSubmit
            .setOnClickListener(){
                if(binding.requestName.text.toString() == "" || binding.pickUpAddressAnswer.text.toString() == "enter address here"
                    || binding.destinationAddressAnswer.text.toString() == "enter address here"
                    || Integer.parseInt(binding.minPrice.text.toString()) < 0|| Integer.parseInt(binding.maxPrice.text.toString()) < 0){
                    Toast.makeText(
                        context,
                        "Fill in all required!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    title = binding.requestName.text.toString()
                    pAddr = binding.pickUpAddressAnswer.text.toString()
                    dAddr = binding.destinationAddressAnswer.text.toString()
                    date = binding.dateAnswer.text.toString()
                    time =
                        binding.timeAnswer.text.toString() // might consider to add a timepicker here instead
                    minPrice = binding.minPrice.text.toString()
                    maxPrice = binding.maxPrice.text.toString()
                    sharable = binding.sharableToggle.isChecked

                    model.addRequest(
                        Request(
                            title,
                            Firebase.auth.uid!!,
                            time + ":00",
                            date,
                            Address(pAddr),
                            1,
                            Address(dAddr),
                            minPrice.toDouble(),
                            maxPrice.toDouble(),
                            sharable
                        )
                    )

                    updateView()
                    findNavController().navigate(R.id.nav_request)
                }
            }
        binding.timeButton
            .setOnClickListener(){
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                        .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
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

                val constraintsBuilderRange = CalendarConstraints.Builder()

//....define min and max for example with LocalDateTime and ZonedDateTime or Calendar
                val dateValidatorMin: DateValidator =
                    DateValidatorPointForward.from(Calendar.getInstance().getTimeInMillis())
                Calendar.getInstance().add(Calendar.YEAR, 1)
//                val dateValidatorMax: DateValidator =
//                    DateValidatorPointBackward.before(Calendar.getInstance().getTimeInMillis())
//                Calendar.getInstance().add(Calendar.YEAR, -1)

                val listValidators = ArrayList<DateValidator>()
                listValidators.add(dateValidatorMin)
                val validators = CompositeDateValidator.allOf(listValidators)
                constraintsBuilderRange.setValidator(validators)
                val picker =
                    MaterialDatePicker.Builder.datePicker()
                        .setCalendarConstraints(constraintsBuilderRange.build())
                        .setTitleText("Select Set-off date")
                        .build()
                picker.addOnPositiveButtonClickListener{
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
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
        Log.d(Constants.TAG,"in detail update view")
        binding.requestName.setText(title)
        binding.pickUpAddressAnswer.setText(pAddr)
        binding.destinationAddressAnswer.setText(dAddr)
        binding.dateAnswer.setText(date)
        binding.timeAnswer.setText(time)
        binding.minPrice.setText(minPrice)
        binding.maxPrice.setText(maxPrice)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data)
            Log.d("what", place.address)
            binding.pickUpAddressAnswer.setText(place.address)
        }
        else if(requestCode == 101 && resultCode == RESULT_OK){
            val place = Autocomplete.getPlaceFromIntent(data)
            binding.destinationAddressAnswer.setText(place.address)
        }
    }

}