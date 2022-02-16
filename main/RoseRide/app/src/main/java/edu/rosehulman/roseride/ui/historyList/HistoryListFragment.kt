package edu.rosehulman.roseride.ui.historyList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.roseride.databinding.FragmentHistoryListBinding

import edu.rosehulman.roseride.HistoryAdapter
import edu.rosehulman.roseride.ui.requestList.RequestListFragment


class HistoryListFragment : Fragment() {

    private lateinit var historyListViewModel: HistoryListViewModel
    lateinit var binding: FragmentHistoryListBinding
    lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        historyListViewModel =
            ViewModelProvider(this).get(HistoryListViewModel::class.java)

        binding = FragmentHistoryListBinding.inflate(inflater, container, false)

        // TODO: Recycler
        adapter = HistoryAdapter(this)
        // set recyclerview and adapter properties
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        adapter.addListener(fragmentName)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        adapter.addListener(fragmentName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener(RequestListFragment.fragmentName)
    }


    companion object {
        const val fragmentName = "HistoryListFragment"
    }
}

