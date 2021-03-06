package com.example.newsapp

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.newsapp.Models.Category
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.ViewModels.TopHeadlineViewModel
import com.example.newsapp.databinding.TopHeadlineFragmentBinding

class TopHeadlineFragment : Fragment() {

    companion object {
        fun newInstance() = TopHeadlineFragment()
    }

    private lateinit var viewModel: TopHeadlineViewModel
    private lateinit var binding: TopHeadlineFragmentBinding
    private lateinit var newsAdapter: NewsAdapter

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

        val adapter = ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, listOf<String>("Allgemein", "Business", "Entertainment", "Gesundheit", "Wissenschaft", "Sport", "Technik"))
        binding.spinnerCategory.adapter = adapter

        setupObservers()

        setupListeners()

        viewModel.fetchNews()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.NewsList.observe(viewLifecycleOwner, Observer {
            newsAdapter.list = it.articles
            newsAdapter.notifyDataSetChanged()
        })

        viewModel.Refreshing.observe(viewLifecycleOwner, Observer {
            binding.refreshView.isRefreshing = it
        })

        viewModel.Cat.observe(viewLifecycleOwner, Observer {
            if (viewModel.latest_search.isNullOrEmpty()) {
                viewModel.fetchNews()
            } else {
                viewModel.fetchNews(viewModel.latest_search!!)
            }
        })
    }

    private fun setupListeners() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0.isNullOrEmpty()) {
                    viewModel.fetchNews(p0)
                }
                viewModel.latest_search = p0
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
            if (viewModel.latest_search.isNullOrEmpty()) {
                viewModel.fetchNews()
            } else {
                viewModel.fetchNews(viewModel.latest_search!!)
            }
        }

        binding.sortButton.setOnClickListener {
            if (binding.expandleSortView.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(binding.topLayout, AutoTransition())
                binding.expandleSortView.visibility = View.VISIBLE
            } else {
                TransitionManager.beginDelayedTransition(binding.topLayout, AutoTransition())
                binding.expandleSortView.visibility = View.GONE
            }
        }

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                if (viewModel.latest_search.isNullOrEmpty()) {
                    viewModel.fetchNews()
                }else{
                    viewModel.fetchNews(viewModel.latest_search!!)
                }
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            when(p2){
                0 -> viewModel.setCategory(Category.general)
                1 -> viewModel.setCategory(Category.business)
                2 -> viewModel.setCategory(Category.entertainment)
                3 -> viewModel.setCategory(Category.health)
                4 -> viewModel.setCategory(Category.science)
                5 -> viewModel.setCategory(Category.sports)
                6 -> viewModel.setCategory(Category.technology)
            }
            if (viewModel.latest_search.isNullOrEmpty()) {
                viewModel.fetchNews()
            }else{
                viewModel.fetchNews(viewModel.latest_search!!)
            }
        }

    }
}
}