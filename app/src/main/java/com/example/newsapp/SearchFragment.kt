package com.example.newsapp

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.ViewModels.SearchViewModel
import com.example.newsapp.databinding.SearchFragmentBinding

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding : SearchFragmentBinding
    private lateinit var newsAdapter : NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        newsAdapter = NewsAdapter(context!!, viewModel.NewsList.value!!.articles)

        val manager = LinearLayoutManager(context)

        binding.newsViewNews.apply {
            layoutManager = manager
            adapter = newsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        binding.searchViewNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(!p0.isNullOrEmpty()){
                    viewModel.fetchNews(p0)
                    viewModel.latest_search = p0
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        viewModel.NewsList.observe(viewLifecycleOwner, Observer {
            newsAdapter.list = it.articles
            newsAdapter.notifyDataSetChanged()
        })

        viewModel.Refreshing.observe(viewLifecycleOwner, Observer {
            binding.searchRefreshView.isRefreshing = it
        })

        binding.searchRefreshView.setOnRefreshListener {
            viewModel.fetchNews(viewModel.latest_search)
        }

        return binding.root
    }

}