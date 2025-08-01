package com.example.pagination_new.di

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
//import com.example.pagination_new.domain.useCases.GetIdByNameUseCse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PagingAdapter.OnItemClick {

val adapter by lazy (LazyThreadSafetyMode.NONE) { PagingAdapter() }
    private val vm: MainViewModel by viewModels()
    private var withPoster_flag = false
    private var genre = "Все жанры"
    private var search = "Поиск фильмов"
    private lateinit var  binding: ActivityMainBinding
    var genre_list: List<Genre_list> = listOf()
   @Inject lateinit var  getGenresUseCase: GetGenresUseCase
//   @Inject lateinit var getIdByName: GetIdByNameUseCse
   // val l = lifecycleScope
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.searchText.hint = search
        adapter.addLoadStateListener {
            binding.progressUp.visibility =  if( it.prepend is LoadState.Loading || adapter.itemCount == 0 ) View.VISIBLE else View.GONE
            binding.progressDown.visibility = if( it.append is LoadState.Loading) View.VISIBLE else View.GONE
        }

             adapter.setOnItemClick(this)
                   getGenres()
                   getFilms()

        binding.apply {  listTv.layoutManager = GridLayoutManager(this@MainActivity,2)
                         listTv.adapter = adapter }

        binding.refreshButton.setOnClickListener { binding.listTv.smoothScrollToPosition(0) }

       binding.searchText.addTextChangedListener(object: TextWatcher {
           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

               if ((binding.searchText.text.isBlank()) && (search == "Поиск фильмов"))
              { binding.searchText.hint = search; binding.withPosterCheckBox.isEnabled = true; } else { binding.withPosterCheckBox.isEnabled = false }
           }

           override fun afterTextChanged(s: Editable?) {}
       } )

               binding.searchButton.setOnClickListener {

                  //  if(binding.searchText.text.isBlank() && search != "Поиск фильмов") { return@setOnClickListener;  }

//                        if (binding.searchText.text.isBlank())  { vm.getAllFilms(); launchLifecycle()
//                          genre = "Все жанры"; return@setOnClickListener }

                         when(search) {
                             "Поиск фильмов" -> {
                                 binding.withPosterCheckBox.isEnabled = false
                                 vm.searchFilmsByTitle(binding.searchText.text.toString())
                                 launchLifecycle()
                             }


                             "Поиск актеров" -> {
                                 vm.searchPersons(binding.searchText.text.toString())
                                 launchLifecycle()

                             }


                         }

               }

        binding.filter.setOnClickListener { showfilterMenu(it) }
        binding.searchMenu.setOnClickListener { showSearchMenu(it) }

        binding.withPosterCheckBox.setOnClickListener {

            if (binding.withPosterCheckBox.isChecked) withPoster_flag = true else withPoster_flag = false
            getFilms()

        }

    }

//    private fun getFilmsByProfession(profession: String) {
//        binding.withPosterCheckBox.isEnabled = false
//        CoroutineScope(Dispatchers.IO).launch {
//            val id =
//                getIdByName.execute(binding.searchText.text.toString())
//            vm.getFilmsByProfession(profession = profession, id); launchLifecycle() }
//
//    }


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

        binding.withPosterCheckBox.isChecked = if (withPoster_flag) true else false

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
    private fun showSearchMenu(view: View) {
         val menu = PopupMenu(this,view)
        menu.menu.add("Поиск фильмов")
        menu.menu.add("Поиск актеров")
           menu.show()
            menu.setOnMenuItemClickListener {

                search = it.title.toString(); binding.searchText.hint = search;
                if (search == "Поиск актеров") binding.withPosterCheckBox.isEnabled = false else binding.withPosterCheckBox.isEnabled = true
                true }

    }
    fun showfilterMenu (view: View) {

        val menu = PopupMenu(this,view)
        menu.menu.add("Все жанры")
        genre_list.forEach { menu.menu.add(it.name) }

        menu.show()
        menu.setOnMenuItemClickListener {

                  genre = it.title.toString()
            getFilms()
            true } }

    override fun onItemClick(id: Int) {

        var intent: Intent? = null

        when(search) {
            "Поиск фильмов" -> {  intent = Intent(this,IdActivity::class.java) }
            "Поиск актеров" -> { intent = Intent(this,PersonActivity::class.java)}
        }


        intent?.putExtra("id",id)
        startActivity(intent)
    }


}