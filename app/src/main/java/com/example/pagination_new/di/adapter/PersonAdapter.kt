package com.example.pagination_new.di.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pagination_new.R
import com.example.pagination_new.databinding.PersonItemBinding
import com.example.pagination_new.domain.classesss.film.Person

class PersonAdapter(private val list: List<Person>): RecyclerView.Adapter<PersonAdapter.RecVewHolder> () {

    private var onItemClick: OnItemClick? = null
   interface OnItemClick { fun onItemClick(id: Int) }

    inner class RecVewHolder( val binding: PersonItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecVewHolder {


      //  val itemView = LayoutInflater.from(parent.context).inflate(R.layout.person_item,parent,false)
        return RecVewHolder(PersonItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecVewHolder, position: Int) {


        holder.binding.apply {

            personImage.load(list[position].photo) {crossfade(true); error(R.drawable.no_image) }
            personName.text = if(list[position].name != null) list[position].name else list[position].enName
            personProfessional.text = list[position].profession.dropLast(1) }

        holder.itemView.setOnClickListener { onItemClick?.onItemClick(list[position].id) }

  }
    fun setOnItemClick (onItemClick: OnItemClick) { this.onItemClick = onItemClick }
}