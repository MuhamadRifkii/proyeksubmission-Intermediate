package com.dicoding.proyeksubmission_intermediate.view.detail_page

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dicoding.proyeksubmission_intermediate.data.response.Story
import com.dicoding.proyeksubmission_intermediate.databinding.ActivityDetailStoryBinding
import com.dicoding.proyeksubmission_intermediate.view.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding : ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("storyId")
        Log.d("DetailStoryActivity", "Story ID: $storyId")

        storyId?.let { viewModel.loadDetailStory(it) }

        viewModel.detailStory.observe(this, Observer { result ->
            result.onSuccess { response ->
                displayDetailStory(response.story)
            }.onFailure { error ->
                handleError(error)
            }
        })
    }

    private fun displayDetailStory(story: Story) {
        story.let {
            binding.username.text = story.name
            binding.description.text = story.description

            story.photoUrl?.let { url ->
                Glide.with(this)
                    .load(url)
                    .into(binding.image)
            }
        }
    }

    private fun handleError(error: Throwable) {
        // Implementasi logika untuk menangani kesalahan
    }
}