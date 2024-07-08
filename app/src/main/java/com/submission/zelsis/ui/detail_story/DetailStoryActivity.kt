package com.submission.zelsis.ui.detail_story

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.submission.zelsis.R
import com.submission.zelsis.databinding.ActivityDetailStoryBinding
import com.submission.zelsis.ui.home.HomeActivity
import com.submission.zelsis.util.DateFormat

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBindDetailStory()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this@DetailStoryActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupBindDetailStory(){
        val storyName = intent.getStringExtra(DETAIL_STORY_NAME)
        val storyImage = intent.getStringExtra(DETAIL_STORY_IMAGE)
        val storyDesc = intent.getStringExtra(DETAIL_STORY_DESC)
        val storyDate = intent.getStringExtra(DETAIL_STORY_DATE)

        binding.tvUsername.text = storyName

        Glide.with(this)
            .load(storyImage)
            .fitCenter()
            .into(binding.ivStory)

        binding.tvDesc.text = storyDesc
        binding.tvDate.text = DateFormat.formatDate(storyDate!!)
    }

    companion object {
        const val DETAIL_STORY_IMAGE = "detail_story_image"
        const val DETAIL_STORY_NAME = "detail_story_name"
        const val DETAIL_STORY_DESC = "detail_story_desc"
        const val DETAIL_STORY_DATE = "detail_story_date"
    }
}