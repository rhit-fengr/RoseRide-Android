package edu.rosehulman.roseride.ui.ride

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.MainActivity
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.databinding.FragmentRideDetailBinding
import edu.rosehulman.roseride.model.RideViewModel
import edu.rosehulman.roseride.model.UserViewModel

class RideDetailFragment : Fragment(){
    private lateinit var binding: FragmentRideDetailBinding
    private lateinit var model: RideViewModel
    private lateinit var model2: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRideDetailBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(RideViewModel::class.java)
        model2 = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
//        return inflater.inflate(R.layout.fragment_quote_detail, container, false)
        updateView()
        setupButtons()
        return binding.root
    }

    private fun setupButtons() {
        binding.rideDeleteBtn.setOnClickListener {
            model.removeCurrentRide()
            findNavController().navigate(R.id.nav_ride)
        }

        binding.rideRequestBtn.setOnClickListener {
            val ride = model.getCurrentRide()
//            model.updateCurrentRide(ride.title,ride.setOffTime,ride.setOffDate,ride.pickUpAddr,ride.addr,ride.passengers.plus(current user here),ride.costPerPerson,ride.numOfSlots,ride.isSelected)
            findNavController().navigate(R.id.nav_ride)
        }

        binding.rideLeaveBtn.setOnClickListener {
            model.removeUserFromRide()
            findNavController().navigate(R.id.nav_ride)
        }

        binding.driverProfile.setOnClickListener {
            model2.checkingUser = model.getCurrentRide().driver
            findNavController().navigate(R.id.nav_other_user)
        }
    }

    private fun updateView() {
        if(!MainActivity.driverMode && model.getCurrentRide().numOfSlots > model.getCurrentRide().passengers.size){
            binding.rideDeleteBtn.visibility=View.GONE
            binding.rideRequestBtn.visibility=View.VISIBLE
            binding.rideLeaveBtn.visibility=View.GONE
        }
        else if (MainActivity.driverMode  && !model.getCurrentRide().sharable) {
            binding.rideDeleteBtn.visibility=View.GONE
            binding.rideRequestBtn.visibility=View.GONE
            binding.rideLeaveBtn.visibility=View.GONE
        }
        else if (!MainActivity.driverMode && model.getCurrentRide().sharable && model.getCurrentRide().passengers.contains(Firebase.auth.uid)){
            binding.rideDeleteBtn.visibility=View.GONE
            binding.rideRequestBtn.visibility=View.GONE
            binding.rideLeaveBtn.visibility=View.VISIBLE
        }
        else{
            binding.rideDeleteBtn.visibility=View.VISIBLE
            binding.rideRequestBtn.visibility=View.GONE
            binding.rideLeaveBtn.visibility=View.GONE
        }
        binding.rideDetailTitle.text = model.getCurrentRide().title
        binding.pickUpAddressAnswer.text = model.getCurrentRide().pickUpAddr.toStringBeautified()
        binding.setOffTime.text = model.getCurrentRide().setOffDate + "   " + model.getCurrentRide().setOffTime.substring(0,  model.getCurrentRide().setOffTime.length-3)
        binding.destinationAddressAnswer.text = model.getCurrentRide().destinationAddr.toStringBeautified()
        binding.priceRangeAnswer.text = "$" + model.getCurrentRide().costPerPerson.toString()
        binding.availableSlots.text =  (model.getCurrentRide().numOfSlots - model.getCurrentRide().passengers.size).toString() + "/" + model.getCurrentRide().numOfSlots.toString()

        val them = Firebase.firestore.collection("users").document(model.getCurrentRide().driver)
        them.get().addOnSuccessListener { document ->
            binding.driverAnswer.text = document.data?.get("name") as String
        }
    }
}