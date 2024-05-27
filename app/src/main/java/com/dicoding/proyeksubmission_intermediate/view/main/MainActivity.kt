package com.dicoding.proyeksubmission_intermediate.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.proyeksubmission_intermediate.R
import com.dicoding.proyeksubmission_intermediate.data.adapter.LoadingStateAdapter
import com.dicoding.proyeksubmission_intermediate.data.adapter.StoryAdapter
import com.dicoding.proyeksubmission_intermediate.databinding.ActivityMainBinding
import com.dicoding.proyeksubmission_intermediate.view.ViewModelFactory
import com.dicoding.proyeksubmission_intermediate.view.language.LanguageActivity
import com.dicoding.proyeksubmission_intermediate.view.maps.MapsActivity
import com.dicoding.proyeksubmission_intermediate.view.upload.UploadStoryActivity
import com.dicoding.proyeksubmission_intermediate.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.app_alt)

        sessionObserver()
        setupRecyclerView()
        uploadStory()
        observePagedStories()
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry() }
            )
        }

        lifecycleScope.launch {
            storyAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                if (loadStates.refresh is LoadState.Error) {
                    val errorState = loadStates.refresh as LoadState.Error
                    handleError(errorState.error)
                }
            }
        }
    }

    private fun sessionObserver() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                if (viewModel.pagedStories.value == null) {
                    viewModel.getStoriesPaged()
                }
            }
        }
    }

    override fun onResume() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                if (viewModel.pagedStories.value == null) {
                    viewModel.getStoriesPaged()
                }
            }
        }
        super.onResume()
    }

    private fun uploadStory() {
        binding.fabUpload.setOnClickListener {
            val intent = Intent(this, UploadStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observePagedStories() {
        viewModel.pagedStories.observe(this) { pagingData ->
            pagingData?.let {
                storyAdapter.submitData(lifecycle, it)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                viewModel.logout()
                true
            }
            R.id.lang -> {
                val intent = Intent(this, LanguageActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
    }
}
