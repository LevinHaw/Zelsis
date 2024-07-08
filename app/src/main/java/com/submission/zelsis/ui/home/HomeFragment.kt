package com.submission.zelsis.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.zelsis.R
import com.submission.zelsis.data.remote.response.ListStoryItem
import com.submission.zelsis.data.remote.retrofit.ApiService
import com.submission.zelsis.databinding.FragmentHomeBinding
import com.submission.zelsis.model.UserModel
import com.submission.zelsis.repository.UserRepository
import com.submission.zelsis.ui.adapter.StoryAdapter
import com.submission.zelsis.util.ViewModelFactory


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        takeName()
        homeViewModel.getAllStory()
        setupAction()
        checkingResult()

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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupAction(){
        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer{
            showLoading(it)
        })
        homeViewModel.stories.observe(viewLifecycleOwner, Observer{ stories ->
            setAdapter(stories)
        })
    }

    private fun setAdapter(stories: List<ListStoryItem>){
        val adapter = StoryAdapter()
        adapter.submitList(stories)
        binding.rvStory.adapter = adapter
        binding.rvStory.layoutManager = LinearLayoutManager(requireContext())
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