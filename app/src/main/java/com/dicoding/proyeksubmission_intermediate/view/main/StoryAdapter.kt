package com.dicoding.proyeksubmission_intermediate.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.proyeksubmission_intermediate.data.response.ListStoryItem
import com.dicoding.proyeksubmission_intermediate.databinding.StoryItemBinding
import com.dicoding.proyeksubmission_intermediate.view.detail_page.DetailStoryActivity

class StoryAdapter(private val listStory: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.username.text = story.name
            binding.description.text = story.description
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = listStory[position]
        holder.bind(story)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intent.putExtra("storyId", story.id)
            val options = androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation(
                holder.itemView.context as Activity,
                androidx.core.util.Pair(holder.binding.image, "image"),
                androidx.core.util.Pair(holder.binding.username, "username"),
                androidx.core.util.Pair(holder.binding.description, "description"),
                androidx.core.util.Pair(holder.binding.cardListStory, "shared_card_view")
            )
            holder.itemView.context.startActivity(intent, options.toBundle())
        }

        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(250).setStartDelay((position * 100).toLong()).start()
    }

    override fun getItemCount(): Int = listStory.size
}