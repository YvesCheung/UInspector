package com.huya.mobile.uinspector.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.huya.mobile.uinspector.demo.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_recycler_view.view.*

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = mutableListOf<HomeItem>()
        val adapter = HomeAdapter(data)
        home_recycler_view.adapter = adapter
        homeViewModel.data.observe(viewLifecycleOwner, Observer {
            data.clear()
            data.addAll(it)
            adapter.notifyDataSetChanged()
        })
    }

    private inner class HomeAdapter(val data: List<HomeItem>) : RecyclerView.Adapter<HomeVH>() {

        @SuppressLint("InflateParams")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVH =
            HomeVH(LayoutInflater.from(parent.context).inflate(
                R.layout.item_recycler_view, parent, false))

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: HomeVH, position: Int) {
            holder.textView.text = data[position].title
            holder.itemView.setOnClickListener { view ->
                data[position].action(requireActivity(), view)
            }
        }
    }

    private class HomeVH(itemView: View, val textView: TextView = itemView.recycler_text) :
        RecyclerView.ViewHolder(itemView)
}