package edu.rosehulman.roseride.ui.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.roseride.databinding.FragmentRequestDetailBinding
import edu.rosehulman.roseride.ui.model.RequestViewModel


class RequestDetailFragment : Fragment(){
    private lateinit var binding: FragmentRequestDetailBinding
    private lateinit var model: RequestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRequestDetailBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(RequestViewModel::class.java)
//        return inflater.inflate(R.layout.fragment_quote_detail, container, false)
        updateView()
        return binding.root
    }

    private fun updateView() {
        binding.requestDetailTitle.text = model.getCurrentRequest().title
        binding.pickUpAddressAnswer.text = model.getCurrentRequest().pickUpAddr.toStringBeautified()
        binding.setOffTime.text = model.getCurrentRequest().setOffDate.toString() + "   " + model.getCurrentRequest().setOffTime.toString().substring(0,  model.getCurrentRequest().setOffTime.toString().length-3)
        binding.destinationAddressAnswer.text = model.getCurrentRequest().destinationAddr.toStringBeautified()
        binding.priceRangeAnswer.text = "$" + model.getCurrentRequest().minPrice + " ~ " + model.getCurrentRequest().maxPrice
        binding.driverAnswer.text = "TBA"
    }
}