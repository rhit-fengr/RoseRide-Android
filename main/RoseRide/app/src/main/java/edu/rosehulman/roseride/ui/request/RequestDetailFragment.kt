package edu.rosehulman.roseride.ui.request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.MainActivity
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentRequestDetailBinding
import edu.rosehulman.roseride.model.RequestViewModel
import edu.rosehulman.roseride.model.Ride
import edu.rosehulman.roseride.model.RideViewModel


class RequestDetailFragment : Fragment(){
    private lateinit var binding: FragmentRequestDetailBinding
    private lateinit var model: RequestViewModel
    private lateinit var rideModel: RideViewModel
    var numberOfPassengers = 1;

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
        if(!MainActivity.driverMode && model.getCurrentRequest().user.equals(Firebase.auth.uid)){
            binding.sharableTitle.visibility=View.GONE
            binding.spinner2.visibility=View.GONE
            binding.requestAcceptButton.visibility=View.GONE
            binding.requestDeleteBtn.visibility=View.VISIBLE
        }
        else if(!MainActivity.driverMode && !model.getCurrentRequest().user.equals(Firebase.auth.uid)){
            binding.sharableTitle.visibility=View.GONE
            binding.spinner2.visibility=View.GONE
            binding.requestAcceptButton.visibility=View.GONE
            binding.requestDeleteBtn.visibility=View.GONE
        }
        else if(MainActivity.driverMode && !model.getCurrentRequest().sharable){
            binding.sharableTitle.visibility=View.GONE
            binding.spinner2.visibility=View.GONE
            binding.requestAcceptButton.visibility=View.VISIBLE
            binding.requestDeleteBtn.visibility=View.GONE
        }
        else{
            binding.sharableTitle.visibility=View.VISIBLE
            binding.spinner2.visibility=View.VISIBLE
            binding.requestAcceptButton.visibility=View.VISIBLE
            binding.requestDeleteBtn.visibility=View.GONE
        }
        binding.requestDetailTitle.text = model.getCurrentRequest().title
        binding.pickUpAddressAnswer.text = model.getCurrentRequest().pickUpAddr.toStringBeautified()
        binding.setOffTime.text = model.getCurrentRequest().setOffDate + "   " + model.getCurrentRequest().setOffTime.substring(0,  model.getCurrentRequest().setOffTime.length-3)
        binding.destinationAddressAnswer.text = model.getCurrentRequest().destinationAddr.toStringBeautified()
        binding.priceRangeAnswer.text = "$" + model.getCurrentRequest().minPrice + " ~ " + model.getCurrentRequest().maxPrice
        binding.sharableAnswer.text = if (model.getCurrentRequest().sharable) "On" else "Off"
    }

    private fun setupButtons() {
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                numberOfPassengers = position + 1
                Log.d("haaz", numberOfPassengers.toString())
                Log.d("haaz", position.toString())
            }
        }
        binding.requestAcceptButton.setOnClickListener {
            val request = model.getCurrentRequest()
            val passengers = listOf(request.user)
            var ride = Ride(
                title = request.title,
                driver = Firebase.auth.uid!!,
                setOffTime = request.setOffTime,
                setOffDate =  request.setOffDate,
                pickUpAddr = request.pickUpAddr,
                destinationAddr = request.destinationAddr,
                passengers = passengers,
                costPerPerson = request.minPrice,
                numOfSlots = numberOfPassengers,
                sharable = request.sharable,
                isSelected = false)
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