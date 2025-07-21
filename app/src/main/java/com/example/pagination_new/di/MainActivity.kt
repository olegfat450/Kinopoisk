package com.example.pagination_new.di

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pagination_new.databinding.ActivityMainBinding
import com.example.pagination_new.di.adapter.PagingAdapter
import com.example.pagination_new.domain.classess.genre.Genre_list
import com.example.pagination_new.domain.useCases.GetGenresUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PagingAdapter.OnItemClick {

val adapter by lazy (LazyThreadSafetyMode.NONE) { PagingAdapter(this) }
    private val vm: MainViewModel by viewModels()
    private var withPoster_flag = false
    private var genre = "Все жанры"
    private lateinit var  binding: ActivityMainBinding
    var genre_list: List<Genre_list> = listOf()
   @Inject lateinit var  getGenresUseCase: GetGenresUseCase
   // val l = lifecycleScope
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter.addLoadStateListener {
            binding.progressUp.visibility =  if( it.prepend is LoadState.Loading || adapter.itemCount == 0 ) View.VISIBLE else View.GONE
            binding.progressDown.visibility = if( it.append is LoadState.Loading) View.VISIBLE else View.GONE
        }

             adapter.setOnItemClick(this)
        getGenres()
        getFilms()

        binding.apply {  listTv.layoutManager = GridLayoutManager(this@MainActivity,2)
                         listTv.adapter = adapter }

        binding.refreshButton.setOnClickListener {

              binding.listTv.smoothScrollToPosition(0) }

       binding.searchText.addTextChangedListener(object: TextWatcher {
           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

           }

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
              if (binding.searchText.text.isBlank()) { binding.withPosterCheckBox.isEnabled = true; getFilms() } else { binding.withPosterCheckBox.isEnabled = false }
           }

           override fun afterTextChanged(s: Editable?) {

           }
       } )

               binding.searchButton.setOnClickListener {
                     if (binding.searchText.text.isNotBlank()) {

                         binding.withPosterCheckBox.isEnabled = false
                         vm.searchFilmsByTitle(binding.searchText.text.toString()); launchLifecycle()
                     } else { vm.getAllFilms(); launchLifecycle()
                         binding.withPosterCheckBox.isEnabled = true; genre = "Все жанры" }

               }

        binding.filter.setOnClickListener { val genre = showfilterMenu(it) }

        binding.withPosterCheckBox.setOnClickListener {

            if (binding.withPosterCheckBox.isChecked) withPoster_flag = true else withPoster_flag = false
            getFilms()

        }

    }
    fun getGenres() {
        CoroutineScope(Dispatchers.IO).launch {  try {  genre_list = getGenresUseCase.execute() }  catch (e:Exception) {
            runOnUiThread {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("${e.message}")
                    .setNegativeButton("Повторить") { _,_ -> getGenres()}
                    .setPositiveButton("Выход") { _,_ -> finishAffinity()}
                    .create().show()
            }
        }}}
    private fun getFilms() {

        when(genre) {
            "Все жанры" -> { if (withPoster_flag) { vm.getFilmsWithPoster()} else vm.getAllFilms()}
            else -> { if(withPoster_flag) vm.getFilmsByGenreWithPoster(genre) else { vm.getFilmsByGenre(genre)}}
        }
        binding.withPosterCheckBox.isEnabled = true
        binding.searchText.text.clear()
               launchLifecycle()

    }

    private fun launchLifecycle() {
        binding.listTv.smoothScrollToPosition(0)
        lifecycleScope.launch { vm.data.collect { adapter.submitData(it) }; }


    }

    fun showfilterMenu (view: View) {

        val filterMenu = PopupMenu(this,view)
        filterMenu.menu.add("Все жанры")
        genre_list.forEach { filterMenu.menu.add(it.name) }

        filterMenu.show()
        filterMenu.setOnMenuItemClickListener {

                  genre = it.title.toString()
            getFilms()


            true
        }
    }

    override fun onItemClick(id: Int) {
        val intent = Intent(this,IdActivity::class.java)
        intent.putExtra("id",id)
        startActivity(intent)
    }


}