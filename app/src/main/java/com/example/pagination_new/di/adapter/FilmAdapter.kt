package com.example.pagination_new.di.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.pagination_new.R

class FilmAdapter(val context: Context, val list: List<Pair<Int,String?>>): BaseAdapter() {
    override fun getCount(): Int = list.size?: 0


    override fun getItem(position: Int) = list.get(position)

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {view = LayoutInflater.from(context).inflate(R.layout.film_item,parent,false)} else view = convertView
        viewHolder = ViewHolder(view)



        viewHolder.text.text = "${list.get(position).second}"

      //  view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED)
      //  Log.d("Ml","${view.measuredHeight}")

        return view
    }

    private class ViewHolder(view: View) {
        val text = view.findViewById<TextView>(R.id.nameFilm)
    }
}