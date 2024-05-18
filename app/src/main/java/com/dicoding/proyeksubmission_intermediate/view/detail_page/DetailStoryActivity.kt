package com.dicoding.proyeksubmission_intermediate.view.detail_page

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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

        val username = intent.getStringExtra("username")
        supportActionBar?.title = username

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val storyId = intent.getStringExtra("storyId")
        Log.d("DetailStoryActivity", "Story ID: $storyId")

        storyId?.let { id ->
            viewModel.getSession().observe(this, Observer { user ->
                if (user != null) {
                    viewModel.loadDetailStory(id)
                } else {
                    Log.e("DetailStoryActivity", "Session not available")
                }
            })
        }
        viewModel.detailStory.observe(this, Observer { result ->
            binding.progressBar2.visibility = View.VISIBLE
            result.onSuccess { response ->
                binding.progressBar2.visibility = View.GONE
                displayDetailStory(response.story)
                supportActionBar?.title = response.story.name
            }.onFailure { error ->
                binding.progressBar2.visibility = View.GONE
                handleError(error)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        binding.progressBar2.visibility = View.GONE
        super.onResume()
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
        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
    }
}