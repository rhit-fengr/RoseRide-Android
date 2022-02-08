package edu.rosehulman.roseride.ui.request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import edu.rosehulman.roseride.Constants
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentRequestEditBinding
import edu.rosehulman.roseride.ui.model.Address
import edu.rosehulman.roseride.ui.model.RequestViewModel
import java.text.SimpleDateFormat
import java.sql.Time
import java.sql.Date



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
                    false,
                    minPrice.toDouble(),
                    maxPrice.toDouble()
                )

                updateView()
                findNavController().navigate(R.id.navigation_request_detail)
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
                    val s = String.format("%2d:%2d", picker.hour, picker.minute)
//                    Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show()
                    Snackbar.make(requireView(), s, Snackbar.LENGTH_SHORT).setAction("continue"){
                        findNavController().navigate(R.id.navigation_request_detail)
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
                        findNavController().navigate(R.id.navigation_request_detail)
                    }.setAnchorView(requireActivity().findViewById(R.id.nav_view))
                        .show()
                    binding.dateAnswer.text = s
                }
                picker.show(parentFragmentManager, "tag");
            }

    }

    private fun updateView() {
        Log.d(Constants.TAG,"in detail update view")
        binding.requestName.setText(model.getCurrentRequest().title)
        binding.pickUpAddressAnswer.setText(model.getCurrentRequest().pickUpAddr.toString())
        binding.destinationAddressAnswer.setText(model.getCurrentRequest().destinationAddr.toString())
        binding.dateAnswer.setText(model.getCurrentRequest().setOffDate)
        binding.timeAnswer.setText(model.getCurrentRequest().setOffTime.substring(0,model.getCurrentRequest().setOffTime.length-3))
        binding.minPrice.setText(model.getCurrentRequest().minPrice.toString())
        binding.maxPrice.setText(model.getCurrentRequest().maxPrice.toString())
    }
}