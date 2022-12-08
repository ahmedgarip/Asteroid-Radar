package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.bindAsteroidStatusImage
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.repository.AstroidRepository


    class RepoRecycleAdapter(val onClickListener: OnClickListener) : ListAdapter<AstroidRepository, RepoRecycleAdapter.AstroidViewHolder>(DiffCallback) {

        class AstroidViewHolder(private var binding : AsteroidItemBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(asteroid : AstroidRepository){
                binding.tile.text = asteroid.codename
                binding.detail.text = asteroid.closeApproachDate
                bindAsteroidStatusImage(binding.imageView, asteroid.isPotentiallyHazardous)
                binding.executePendingBindings()

            }



        }
        class OnClickListener(val clickListener: (asteroid: AstroidRepository) -> Unit) {
            fun onClick(asteroid:AstroidRepository) = clickListener(asteroid)
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AstroidViewHolder {
            return AstroidViewHolder(AsteroidItemBinding.inflate(LayoutInflater.from(parent.context)))
        }


        companion object DiffCallback : DiffUtil.ItemCallback<AstroidRepository>(){
            override fun areItemsTheSame(oldItem: AstroidRepository, newItem: AstroidRepository): Boolean {
                return  oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: AstroidRepository, newItem: AstroidRepository): Boolean {
                return oldItem.id == newItem.id
            }

        }

        override fun onBindViewHolder(holder: AstroidViewHolder, position: Int) {
            val asteroid = getItem(position)
            holder.bind(asteroid)
            holder.itemView.setOnClickListener {
                onClickListener.onClick(asteroid)
            }
        }



    }



