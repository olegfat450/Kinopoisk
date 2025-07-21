package com.example.pagination_new.di.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagination_new.R
import com.example.pagination_new.databinding.ListItemBinding
import com.example.pagination_new.domain.classess.Doc
import coil.load


class PagingAdapter (val context: Context): PagingDataAdapter<Doc, PagingAdapter.MVH>(DIFF_CALLBACK) {

   private var onItemClick: OnItemClick? = null
  interface OnItemClick { fun onItemClick(id: Int)}

    inner class MVH ( val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Doc>() {
            override fun areItemsTheSame(oldItem: Doc, newItem: Doc): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Doc, newItem: Doc): Boolean {
               return oldItem == newItem }
        } }

    override fun onBindViewHolder(holder: MVH, position: Int) {



        val item = getItem(position)
        holder.binding.apply { title.text = if ( item?.name != null) item.name.toString() else item?.alternativeName.toString()

//                              if (item?.poster?.previewUrl != null)  {
//                                  Log.d("Ml","picasso")
//                                  Picasso.get().load(item.poster.previewUrl.toUri()).into(image)
//                              } else {image.setImageResource(R.drawable.no_image) }

                        image.load(item?.poster?.url) { crossfade(true); error(R.drawable.no_image) }

                              val genre_item= item?.genres?.getOrNull(0)?.name ?: "Пусто"
                              genre.text = genre_item?.substring(0,10.coerceAtMost(genre_item.length))
                              val country_item = item?.countries?.getOrNull(0)?.name ?: "пусто"
                              country.text = country_item.substring(0,10.coerceAtMost(country_item.length))
                              year.text = "Год: ${item?.year}"
                              rating.text = "Imdb: ${item?.rating?.imdb}"


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
    fun setOnItemClick (onItemClick: OnItemClick) { this.onItemClick = onItemClick}

}