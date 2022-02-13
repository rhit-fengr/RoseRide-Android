package edu.rosehulman.roseride.ui.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.MainActivity
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentRequestDetailBinding
import edu.rosehulman.roseride.ui.model.RequestViewModel
import edu.rosehulman.roseride.ui.model.Ride
import edu.rosehulman.roseride.ui.model.RideViewModel
import edu.rosehulman.roseride.ui.model.User


class RequestDetailFragment : Fragment(){
    private lateinit var binding: FragmentRequestDetailBinding
    private lateinit var model: RequestViewModel
    private lateinit var rideModel: RideViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRequestDetailBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(RequestViewModel::class.java)
        rideModel = ViewModelProvider(requireActivity()).get(RideViewModel::class.java)
//        return inflater.inflate(R.layout.fragment_quote_detail, container, false)
        updateView()
        setupButtons()
        return binding.root
    }

    private fun updateView() {
        if(!MainActivity.driverMode){
            binding.requestAcceptButton.visibility=View.GONE
            binding.requestDeleteBtn.visibility=View.VISIBLE
        }
        else{
            binding.requestAcceptButton.visibility=View.VISIBLE
            binding.requestDeleteBtn.visibility=View.GONE
        }
        binding.requestDetailTitle.text = model.getCurrentRequest().title
        binding.pickUpAddressAnswer.text = model.getCurrentRequest().pickUpAddr.toStringBeautified()
        binding.setOffTime.text = model.getCurrentRequest().setOffDate + "   " + model.getCurrentRequest().setOffTime.substring(0,  model.getCurrentRequest().setOffTime.length-3)
        binding.destinationAddressAnswer.text = model.getCurrentRequest().destinationAddr.toStringBeautified()
        binding.priceRangeAnswer.text = "$" + model.getCurrentRequest().minPrice + " ~ " + model.getCurrentRequest().maxPrice
        binding.driverAnswer.text = "TBA"
    }

    private fun setupButtons() {
        binding.requestAcceptButton.setOnClickListener {
            val request = model.getCurrentRequest()
            val passengers = listOf(request.user)
            var ride = Ride(
                request.title,
                Firebase.auth.uid!!,
                request.setOffTime,
                request.setOffDate,
                request.pickUpAddr,
                request.destinationAddr,
                passengers,
                request.minPrice,
                request.numOfPassengers,
                false,
                false)
            rideModel.addRide(ride)
            model.removeCurrentRequest()
            findNavController().navigate(R.id.nav_request)
        }

        binding.requestDeleteBtn.setOnClickListener {
            model.removeCurrentRequest()
            findNavController().navigate(R.id.nav_request)
        }
    }
}