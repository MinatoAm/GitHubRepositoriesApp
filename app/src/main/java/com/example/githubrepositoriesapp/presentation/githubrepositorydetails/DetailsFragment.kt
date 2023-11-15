package com.example.githubrepositoriesapp.presentation.githubrepositorydetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.githubrepositoriesapp.R
import com.example.githubrepositoriesapp.presentation.githubrepositorydetails.model.DetailsUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailsFragment : Fragment() {

    private var id: Int? = 0

    private val viewModel: DetailsFragmentViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt("id", 0)
        }
    }

    private lateinit var clDetailsView: ConstraintLayout
    private lateinit var tvName: TextView
    private lateinit var ivAvatar: ImageView
    private lateinit var tvDescription: TextView
    private lateinit var tvStars: TextView
    private lateinit var tvForks: TextView
    private lateinit var tvLanguage: TextView
    private lateinit var btnOpenGithub: TextView
    private lateinit var pbLoading: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_repositroy_details, container, false)
        id?.let { viewModel.getRepositoryByTagAndId(it) }
        handleBackPress()
        initView(view)
        observeData()
        return view
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateBack()
        }
    }

    private fun observeData() {lifecycleScope.launch {
        viewModel.detailsUiState.collectLatest { uiState ->
            when (uiState) {
                is DetailsUiState.Success -> {
                    with(uiState) {
                        tvName.text = data?.name
                        context?.let {
                            Glide
                                .with(it)
                                .load(data?.owner?.avatarUrl)
                                .centerCrop()
                                .into(ivAvatar)
                        }
                        tvDescription.text = data?.description
                        tvStars.text = data?.stars.toString()
                        tvForks.text = data?.forksCount.toString()
                        tvLanguage.text = data?.language
                        btnOpenGithub.setOnClickListener {
                            val uri =
                                Uri.parse(data?.url)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                    }
                    hideLoading()
                }

                is DetailsUiState.Loading -> {
                    showLoading()
                }

                is DetailsUiState.Error -> {
                    hideLoading()
                    clDetailsView.visibility = View.INVISIBLE
                    Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    }

    private fun initView(view: View) {
        clDetailsView = view.findViewById(R.id.clDetailsView)
        tvName = view.findViewById(R.id.tvRepoName)
        ivAvatar = view.findViewById(R.id.ivAvatar)
        tvDescription = view.findViewById(R.id.tvRepoDescription)
        tvStars = view.findViewById(R.id.tvStars)
        tvForks = view.findViewById(R.id.tvForks)
        tvLanguage = view.findViewById(R.id.tvLanguage)
        btnOpenGithub = view.findViewById(R.id.btnGitHub)
        pbLoading = view.findViewById(R.id.pbLoading)
    }

    private fun navigateBack() {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack()
    }

    private fun showLoading() {
        clDetailsView.visibility = View.INVISIBLE
        pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        clDetailsView.visibility = View.VISIBLE
        pbLoading.visibility = View.INVISIBLE
    }
}