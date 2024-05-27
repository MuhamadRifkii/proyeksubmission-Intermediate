package com.dicoding.proyeksubmission_intermediate.data.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.proyeksubmission_intermediate.data.response.ListStoryItem
import com.dicoding.proyeksubmission_intermediate.databinding.StoryItemBinding
import com.dicoding.proyeksubmission_intermediate.view.detail_page.DetailStoryActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    inner class StoryViewHolder(val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.username.text = story.name
            binding.description.text = story.description
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.image)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra("storyId", story.id)
                val options = androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.image, "image"),
                    Pair(binding.username, "username"),
                    Pair(binding.description, "description"),
                    Pair(binding.cardListStory, "shared_card_view")
                )
                itemView.context.startActivity(intent, options.toBundle())
            }

            itemView.alpha = 0f
            itemView.animate().alpha(1f).setDuration(250).start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}