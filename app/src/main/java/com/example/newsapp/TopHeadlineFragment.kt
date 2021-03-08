package com.example.newsapp

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.Models.NewsAdapter
import com.example.newsapp.ViewModels.TopHeadlineViewModel
import com.example.newsapp.databinding.TopHeadlineFragmentBinding
import kotlinx.coroutines.flow.collect

class TopHeadlineFragment : Fragment() {

    companion object {
        fun newInstance() = TopHeadlineFragment()
    }

    private lateinit var viewModel: TopHeadlineViewModel
    private lateinit var binding : TopHeadlineFragmentBinding
    private lateinit var newsAdapter : NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopHeadlineFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(TopHeadlineViewModel::class.java)

        newsAdapter = NewsAdapter(context!!, viewModel.NewsList.value!!.articles)
        val manager = LinearLayoutManager(context)

        binding.newsView.apply {
            layoutManager = manager
            adapter = newsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setItemViewCacheSize(20)
        }

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(!p0.isNullOrEmpty()){
                    viewModel.fetchNews(p0)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        binding.searchView.setOnCloseListener {
            viewModel.fetchNews()
            false
        }

        binding.refreshView.setOnRefreshListener {
            viewModel.fetchNews()
        }

        viewModel.NewsList.observe(viewLifecycleOwner, Observer {
            newsAdapter.list = it.articles
            newsAdapter.notifyDataSetChanged()
        })

        viewModel.Refreshing.observe(viewLifecycleOwner, Observer {
            binding.refreshView.isRefreshing = it
        })

        viewModel.fetchNews()

        return binding.root
    }

}