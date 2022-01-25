package edu.rosehulman.roseride.ui.requestList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.roseride.RequestAdapter
import edu.rosehulman.roseride.databinding.FragmentRequestListBinding
import edu.rosehulman.roseride.ui.model.Request

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
//        adapter.addListener(fragmentName)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        binding.fab.setOnClickListener{
            adapter.addRequest(null)
        }


        return binding.root
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        adapter.removeListener(fragmentName)
//    }
}