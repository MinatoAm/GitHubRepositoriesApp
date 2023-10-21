package com.example.githubrepositoriesapp.presentation.githubrepositories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubrepositoriesapp.R
import com.example.githubrepositoriesapp.data.model.GitHubRepositoryModel

class GitHubRepositoriesAdapter(private val onItemClick: (id: Int?) -> Unit) : PagingDataAdapter<GitHubRepositoryModel, GitHubRepositoriesViewHolder>(createItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubRepositoriesViewHolder {
        return GitHubRepositoriesViewHolder(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: GitHubRepositoriesViewHolder, position: Int) {
        val repository = getItem(position) ?: return
        holder.bind(repository)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_repository
    }

    companion object {
        fun createItemCallback() = object : DiffUtil.ItemCallback<GitHubRepositoryModel>() {
            override fun areItemsTheSame(oldItem: GitHubRepositoryModel, newItem: GitHubRepositoryModel): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: GitHubRepositoryModel, newItem: GitHubRepositoryModel): Boolean = oldItem == newItem
        }
    }
}

class GitHubRepositoriesViewHolder(parent: ViewGroup, val onItemClick: (id: Int?) -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false)
) {
    private val titleTextView: TextView = itemView.findViewById(R.id.tvRepoName)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.tvRepoDescription)
    private val starsTextView: TextView = itemView.findViewById(R.id.tvReposStarsCount)
    private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
    private val clRepositoryItem: ConstraintLayout = itemView.findViewById(R.id.clRepositoryItem)

    fun bind(gitHubRepositoryModel: GitHubRepositoryModel) {
        titleTextView.text = gitHubRepositoryModel.name
        descriptionTextView.text = gitHubRepositoryModel.description
        starsTextView.text = gitHubRepositoryModel.stars.toString()
        Glide
            .with(itemView)
            .load(gitHubRepositoryModel.owner?.avatarUrl)
            .centerCrop()
            .into(ivAvatar)
        clRepositoryItem.setOnClickListener {
            onItemClick(gitHubRepositoryModel.id)
        }
    }
}