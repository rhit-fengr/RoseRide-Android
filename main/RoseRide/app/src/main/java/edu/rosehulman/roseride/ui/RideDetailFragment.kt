package edu.rosehulman.roseride.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.roseride.databinding.FragmentRideDetailBinding
import edu.rosehulman.roseride.ui.model.RideViewModel

class RideDetailFragment : Fragment(){
    private lateinit var binding: FragmentRideDetailBinding
    private lateinit var model: RideViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRideDetailBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(RideViewModel::class.java)
//        return inflater.inflate(R.layout.fragment_quote_detail, container, false)
        updateView()
        return binding.root
    }

    private fun updateView() {
        binding.rideDetailTitleText.text = model.getCurrentRide().title
    }
}