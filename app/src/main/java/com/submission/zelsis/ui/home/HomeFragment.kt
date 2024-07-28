package com.submission.zelsis.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.zelsis.databinding.FragmentHomeBinding
import com.submission.zelsis.ui.adapter.StoryAdapter
import com.submission.zelsis.ui.map.MapsActivity
import com.submission.zelsis.util.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        takeName()
        setupAction()
        checkingResult()

        binding.ibMap.setOnClickListener {
            val intent = Intent(activity, MapsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction(){
        val adapter = StoryAdapter()

        binding.rvStory.adapter = adapter
        binding.rvStory.layoutManager = LinearLayoutManager(requireContext())
        
        homeViewModel.story.observe(viewLifecycleOwner, Observer{
            if (it != null){
                adapter.submitData(lifecycle, it)
            }
        })

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
    }

    private fun checkingResult(){
        homeViewModel.isError.observe(viewLifecycleOwner, Observer{ isError ->
            if (isError){
                Toast.makeText(requireActivity(), "Cannot retrieve story", Toast.LENGTH_SHORT).show()
            } else {
                homeViewModel.message.observe(viewLifecycleOwner, Observer{
                    Log.d(TAG, "Retrieve story success")
                })
            }
        })
    }

    private fun takeName(){
        homeViewModel.name.observe(viewLifecycleOwner, Observer{ name ->
            binding.tvName.text = name
        })
    }

    companion object {
        private const val TAG = "HomeFragment"
    }

}