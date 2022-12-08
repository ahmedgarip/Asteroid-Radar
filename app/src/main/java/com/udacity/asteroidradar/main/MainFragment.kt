package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.repository.RepositoryAstroid
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.math.log

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
//        ViewModelProvider(this, MainViewModel.Factory(requireActivity().application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        Log.i("days",getNextSevenDaysFormattedDates().toString())

        binding.asteroidRecycler.adapter = RepoRecycleAdapter(RepoRecycleAdapter.OnClickListener{
            viewModel.displayPropertyDetails(it)

        })

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner ,{
            if (it != null){
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
            viewModel.displayPropertyDetailsComplete()
            }
        })

//        binding.activityMainImageOfTheDay.setImageResource()
//        binding.textView.text = viewModel.PictureOfTheDayTitle.toString()
//            viewModel.PictureOfTheDayTitle.toString()
//        viewModel.PictureOfTheDayTitle.observe(viewLifecycleOwner,{
//            binding.textView.text= it
//        })






        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.filterAsteroids(
            when(item.itemId){
                R.id.show_today_menu -> RepositoryAstroid.AsteroidsFilter.SHOW_DAY
                R.id.show_week_menu -> RepositoryAstroid.AsteroidsFilter.SHOW_WEEK
                else -> RepositoryAstroid.AsteroidsFilter.SHOW_ALL
            }
        )
        return true
    }
}
