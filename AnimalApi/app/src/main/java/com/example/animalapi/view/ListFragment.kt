package com.example.animalapi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.example.animalapi.R
import com.example.animalapi.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    lateinit var viewmodel: ListViewModel
    private val animalAdapter = AnimalClickAdapter(arrayListOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProvider(this).get(ListViewModel::class.java)
        rvAnimal.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = animalAdapter

        }
        setRefreshLayoutListener()
        observeViewModel()
        viewmodel.refresh()
    }

    fun setRefreshLayoutListener() {
        refreshLayout.setOnRefreshListener {
            viewmodel.hardRefresh()
            refreshLayout.isRefreshing = false
        }
    }

    fun observeViewModel() {
        viewmodel.animals.observe(viewLifecycleOwner, Observer { animals ->
            animals?.let {
                rvAnimal.visibility = View.VISIBLE
                animalAdapter.updateAnimals(it)
            }
        })
        viewmodel.loadError.observe(viewLifecycleOwner, Observer { istrue ->
            istrue?.let {
                tvError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        viewmodel.loading.observe(viewLifecycleOwner, Observer { istrue ->

            istrue?.let {
                if (istrue) {
                    progressBarLoading.visibility = View.VISIBLE
                    rvAnimal.visibility = View.GONE
                    tvError.visibility = View.GONE
                } else {
                    progressBarLoading.visibility = View.GONE
                }
            }

        })
    }

}
