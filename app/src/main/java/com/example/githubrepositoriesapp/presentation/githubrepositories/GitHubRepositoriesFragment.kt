package com.example.githubrepositoriesapp.presentation.githubrepositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepositoriesapp.R
import com.example.githubrepositoriesapp.presentation.githubrepositories.adapter.GitHubRepositoriesAdapter
import com.example.githubrepositoriesapp.presentation.githubrepositorydetails.DetailsFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GitHubRepositoriesFragment : Fragment() {

    private val gitHubRepositoriesAdapter = GitHubRepositoriesAdapter { repoId ->
        repoId?.let {
            onRepositoryItemClick(
                it
            )
        }
    }
    private val viewModel: GitHubRepositoriesFragmentViewModel by viewModel()

    private lateinit var rvRepositories: RecyclerView
    private lateinit var pbLoadingMain: ProgressBar
    private lateinit var pbLoadingBottom: ProgressBar

    private fun onRepositoryItemClick(id: Int) {

        val fragment = DetailsFragment()
        val bundle = Bundle()
        bundle.putInt("id", id)
        fragment.arguments = bundle

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_github_repositories_list, container, false)
        initView(view)
        observeData()
        return view
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.showData.collectLatest {
                gitHubRepositoriesAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun initView(view: View) {
        pbLoadingMain = view.findViewById(R.id.pbLoadingMain)
        pbLoadingBottom = view.findViewById(R.id.pbLoadingBottom)
        rvRepositories = view.findViewById(R.id.rvRepositories)
        rvRepositories.layoutManager = LinearLayoutManager(context)
        rvRepositories.adapter = gitHubRepositoriesAdapter
        gitHubRepositoriesAdapter.addLoadStateListener(::loadState)
    }

    private fun loadState(combinedLoadStates: CombinedLoadStates) {

        when (combinedLoadStates.refresh) {
            is LoadState.Loading -> {
                showMainLoading()
                hideBottomLoading()
            }

            is LoadState.Error -> {
                val errorState = combinedLoadStates.refresh as LoadState.Error
                val errorMessage = errorState.error.message
                hideBMainLoading()
                hideBottomLoading()
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }

            is LoadState.NotLoading -> {
                hideBottomLoading()
                hideBMainLoading()
            }
        }
        when (combinedLoadStates.append) {
            is LoadState.Loading -> {
                showBottomLoading()
            }

            is LoadState.Error -> {
                hideBottomLoading()
            }

            is LoadState.NotLoading -> {
                hideBottomLoading()
            }
        }
    }

    private fun hideBMainLoading() {
        pbLoadingMain.visibility = View.INVISIBLE
        rvRepositories.visibility = View.VISIBLE
    }

    private fun showMainLoading() {
        pbLoadingMain.visibility = View.VISIBLE
        rvRepositories.visibility = View.INVISIBLE
    }

    private fun hideBottomLoading() {
        pbLoadingBottom.visibility = View.INVISIBLE
    }

    private fun showBottomLoading() {
        pbLoadingBottom.visibility = View.VISIBLE
    }
}