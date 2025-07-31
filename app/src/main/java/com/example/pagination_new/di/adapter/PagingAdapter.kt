package com.example.pagination_new.di.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagination_new.R
import com.example.pagination_new.databinding.ListItemBinding
import coil.load
import com.example.pagination_new.domain.PagerAdapterClass


class PagingAdapter (): PagingDataAdapter<PagerAdapterClass, PagingAdapter.MVH>(DIFF_CALLBACK) {

   private var onItemClick: OnItemClick? = null
  interface OnItemClick { fun onItemClick(id: Int) }

    inner class MVH ( val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PagerAdapterClass>() {
            override fun areItemsTheSame(oldItem: PagerAdapterClass, newItem: PagerAdapterClass): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PagerAdapterClass, newItem: PagerAdapterClass): Boolean {
               return oldItem == newItem }
        } }

    override fun onBindViewHolder(holder: MVH, position: Int) {



        val item = getItem(position)
        holder.binding.apply { title.text =item?.title

//                              if (item?.poster?.previewUrl != null)  {
//                                  Log.d("Ml","picasso")
//                                  Picasso.get().load(item.poster.previewUrl.toUri()).into(image)
//                              } else {image.setImageResource(R.drawable.no_image) }

                        image.load(item?.image) { crossfade(true); error(R.drawable.no_image) }

                              //val genre_item = item?.genres?.getOrNull(0)?.name ?: "Пусто"
                              genre.text = item?.text_1?.substring(0,10.coerceAtMost(item.text_1.length))
                              val country_item = item?.text_2
                              country.text = country_item?.substring(0,10.coerceAtMost(country_item.length))
                              year.text = item?.text_3
                              rating.text = item?.text_4


//            Log.d("Ml","${item?.poster?.url} --  ${item?.poster?.previewUrl}")
        }
        holder.binding.item.setOnClickListener {
            item?.let { onItemClick?.onItemClick(item.id) }


          //  Toast.makeText(context,"${item?.id}",Toast.LENGTH_LONG).show()
        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MVH {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        val layoutParams = itemView.layoutParams
        layoutParams.width = (parent.width/2.1).toInt()
        itemView.layoutParams = layoutParams

     return MVH(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    }
    fun setOnItemClick (onItemClick: OnItemClick) { this.onItemClick = onItemClick }

}