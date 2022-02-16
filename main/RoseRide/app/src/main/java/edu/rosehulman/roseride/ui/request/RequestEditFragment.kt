package edu.rosehulman.roseride.ui.request

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import edu.rosehulman.roseride.Constants
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentRequestEditBinding
import edu.rosehulman.roseride.model.Address
import edu.rosehulman.roseride.model.RequestViewModel
import java.text.SimpleDateFormat
import java.sql.Date
import java.util.*


class RequestEditFragment : Fragment(){
    private lateinit var model: RequestViewModel
    private lateinit var binding: FragmentRequestEditBinding

//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!

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
                var title = binding.requestName.text.toString()
                val pAddr = binding.pickUpAddressAnswer.text.toString()
                var dAddr = binding.destinationAddressAnswer.text.toString()
                var date = binding.dateAnswer.text.toString()
                var time = binding.timeAnswer.text.toString() // might consider to add a timepicker here instead
                var minPrice = binding.minPrice.text.toString()
                var maxPrice = binding.maxPrice.text.toString()

                // TODO: Construct fields to pass in updateCurrentRequest
                model.updateCurrentRequest(
                    title,
                    time + ":00",
                    date,
                    Address(pAddr),
                    1,
                    Address(dAddr),
                    minPrice.toDouble(),
                    maxPrice.toDouble()
                )

                updateView()
                findNavController().navigate(R.id.nav_request_detail)
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
                    var s: String
                    s = if(picker.hour<10){
                        "0"+picker.hour.toString()+":"
                    }else picker.hour.toString()+":"
                    if(picker.minute<10){
                        s = s+"0"+picker.minute.toString()
                    }else s += picker.minute.toString()

//                    Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show()
                    Snackbar.make(requireView(), s, Snackbar.LENGTH_SHORT).setAction("continue"){
                        findNavController().navigate(R.id.nav_request_detail)
                    }.setAnchorView(requireActivity().findViewById(R.id.nav_view))
                        .show()


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
//                    Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show()
                    Snackbar.make(requireView(), s, Snackbar.LENGTH_SHORT).setAction("continue"){
                        findNavController().navigate(R.id.nav_request_detail)
                    }.setAnchorView(requireActivity().findViewById(R.id.nav_view))
                        .show()
                    binding.dateAnswer.text = s
                }
                picker.show(parentFragmentManager, "tag");
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
        val current = model.getCurrentRequest()
        binding.requestName.setText(current.title)
        binding.pickUpAddressAnswer.setText(current.pickUpAddr.toString())
        binding.destinationAddressAnswer.setText(current.destinationAddr.toString())
        binding.dateAnswer.setText(current.setOffDate)
        binding.timeAnswer.setText(current.setOffTime.substring(0,model.getCurrentRequest().setOffTime.length-3))
        binding.minPrice.setText(current.minPrice.toString())
        binding.maxPrice.setText(current.maxPrice.toString())
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