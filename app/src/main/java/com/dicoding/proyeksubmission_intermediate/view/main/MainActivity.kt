package com.dicoding.proyeksubmission_intermediate.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.proyeksubmission_intermediate.R
import com.dicoding.proyeksubmission_intermediate.data.FetchResult
import com.dicoding.proyeksubmission_intermediate.databinding.ActivityMainBinding
import com.dicoding.proyeksubmission_intermediate.view.ViewModelFactory
import com.dicoding.proyeksubmission_intermediate.view.language.LanguageActivity
import com.dicoding.proyeksubmission_intermediate.view.maps.MapsActivity
import com.dicoding.proyeksubmission_intermediate.view.upload.UploadStoryActivity
import com.dicoding.proyeksubmission_intermediate.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.app_alt)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                if (viewModel.stories.value == null) {
                    viewModel.getListStories()
                }
            }
        }

        viewModel.stories.observe(this, Observer { result ->
            when (result) {
                is FetchResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is FetchResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val stories = result.data
                    val adapter = StoryAdapter(stories)
                    binding.recyclerView.adapter = adapter
                }
                is FetchResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    handleError(result.exception)
                }
            }
        })

        UploadStory()
    }

    override fun onResume() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                if (viewModel.stories.value == null) {
                    viewModel.getListStories()
                }
            }
        }
        super.onResume()
    }

    private fun UploadStory() {
        binding.fabUpload.setOnClickListener {
            val intent = Intent(this, UploadStoryActivity::class.java)
            startActivity(intent)
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
