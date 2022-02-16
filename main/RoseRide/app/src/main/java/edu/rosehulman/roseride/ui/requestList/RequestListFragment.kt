package edu.rosehulman.roseride.ui.requestList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.roseride.MainActivity.Companion.driverMode
import edu.rosehulman.roseride.MainActivity.Companion.onlyUser
import edu.rosehulman.roseride.R
import edu.rosehulman.roseride.RequestAdapter
import edu.rosehulman.roseride.databinding.FragmentRequestListBinding

class RequestListFragment : Fragment() {

    private lateinit var requestListViewModel: RequestListViewModel
    lateinit var binding: FragmentRequestListBinding
    lateinit var adapter: RequestAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requestListViewModel =
            ViewModelProvider(this).get(RequestListViewModel::class.java)

        binding = FragmentRequestListBinding.inflate(inflater, container, false)

        // TODO: Recycler
        adapter = RequestAdapter(this)
        // set recyclerview and adapter properties
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

//        if(driverMode){
//            adapter.addAllListener(fragmentName)
//            binding.fab.visibility=View.GONE
//        }else{
//            adapter.addOneListener(fragmentName)
//            binding.fab.visibility=View.VISIBLE
//        }

        checkFab()
        checkOne()

        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.nav_request_add)
        }



        return binding.root
    }

    fun checkOne() {
        if(!onlyUser){
            adapter.addAllListener(fragmentName)
        }else{
            adapter.addOneListener(fragmentName)
        }
    }

    fun checkFab() {
        if(driverMode){
            binding.fab.visibility=View.GONE
        }else{
            binding.fab.visibility=View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener(fragmentName)
    }

    companion object{
        const val fragmentName = "RequestListFragment"
    }
}