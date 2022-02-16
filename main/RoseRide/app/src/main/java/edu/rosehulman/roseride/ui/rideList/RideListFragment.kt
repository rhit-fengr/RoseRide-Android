package edu.rosehulman.roseride.ui.rideList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.roseride.MainActivity
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.RideAdapter
import edu.rosehulman.roseride.databinding.FragmentRideListBinding
import edu.rosehulman.roseride.ui.requestList.RequestListFragment

class RideListFragment : Fragment() {

    private lateinit var rideListViewModel: RideListViewModel
    lateinit var binding: FragmentRideListBinding
    lateinit var adapter: RideAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rideListViewModel =
            ViewModelProvider(this).get(RideListViewModel::class.java)

        binding = FragmentRideListBinding.inflate(inflater, container, false)

        // TODO: Recycler
        adapter = RideAdapter(this)
        // set recyclerview and adapter properties
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

//        if(MainActivity.driverMode){
//            binding.fab.visibility=View.VISIBLE
//        }else{
//            binding.fab.visibility=View.GONE
//        }


        checkFab()
        checkOne()

        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.nav_ride_add)
        }

        return binding.root
    }

    fun checkOne() {
        adapter.removeListener(fragmentName)
        if(!MainActivity.onlyUser){
            adapter.addAllListener(fragmentName)
        }else{
            adapter.addOneListener(fragmentName)
        }
    }

    fun checkFab() {
        if(MainActivity.driverMode){
            binding.fab.visibility=View.VISIBLE
        }else{
            binding.fab.visibility=View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        checkFab()
        if(MainActivity.driverMode) {
            checkOne()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener(fragmentName)
    }

    companion object{
        const val fragmentName = "RideListFragment"
    }
}