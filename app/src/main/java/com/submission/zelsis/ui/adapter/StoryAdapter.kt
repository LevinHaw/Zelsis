package com.submission.zelsis.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.zelsis.data.remote.response.ListStoryItem
import com.submission.zelsis.databinding.ItemStoryBinding
import com.submission.zelsis.ui.detail_story.DetailStoryActivity
import com.submission.zelsis.util.DateFormat

class StoryAdapter: ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback

    }

    class MyViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){
            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.DETAIL_STORY_NAME, story.name)
                    putExtra(DetailStoryActivity.DETAIL_STORY_IMAGE, story.photoUrl)
                    putExtra(DetailStoryActivity.DETAIL_STORY_DATE, story.createdAt)
                    putExtra(DetailStoryActivity.DETAIL_STORY_DESC, story.description)
                }
                itemView.context.startActivity(intent)
            }
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .fitCenter()
                .into(binding.ivImage)

            binding.tvName.text = story.name
            binding.tvDate.text = DateFormat.formatDate(story.createdAt!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: ListStoryItem)
    }
}