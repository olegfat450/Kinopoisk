package com.example.pagination_new.di

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pagination_new.domain.PageSource
import com.example.pagination_new.domain.Progress
import com.example.pagination_new.data.retrofit.MainDb
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
class MainActivity : AppCompatActivity(), PagingAdapter.OnItemClick, Progress {

val adapter by lazy (LazyThreadSafetyMode.NONE) { PagingAdapter() }


   companion object{ const val SEARCH_FILMS = "Поиск фильмов"
                      const val SEARCH_ACTORS = "Поиск актеров"}

    private val vm: MainViewModel by viewModels()
    private var withPoster_flag = false
    private var genre = "Все жанры"
    private var search = SEARCH_FILMS
    private var onFavorite = false
    private var searchOn = SEARCH_FILMS
    private var onMain = true


          private lateinit var  binding: ActivityMainBinding
    var genre_list: List<Genre_list> = listOf()
   @Inject lateinit var  getGenresUseCase: GetGenresUseCase
   @Inject lateinit var db: MainDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
    val resources = resources
    val resourcesId = resources.getIdentifier("status_bar_height","dimen","android")
    val h = resources.getDimensionPixelSize(resourcesId)
    binding.main.setPadding(0,h,0,0)
        setContentView(binding.root)

     binding.favoriteFilmsText.setOnClickListener { onFavorite = true

           onMain = false
            vm.getFavoriteFilms(); launchLifecycle()

     }

        vm.onDialog.observe(this,{ if(it) {
            AlertDialog.Builder(this)
                .setTitle("В избранном пусто")
                .setPositiveButton("OK") { _,_ ->
                    onMain = true
                  onFavorite = false
                   getFilms()

                }
                .create()
                .show()


        } })

       binding.searchText.hint = search
        adapter.addLoadStateListener {

            binding.progressUp.visibility =  if( it.prepend is LoadState.Loading ) View.VISIBLE else View.GONE
            binding.progressDown.visibility = if( it.append is LoadState.Loading) View.VISIBLE else View.GONE
        }

        PageSource().execute(this)
             adapter.setOnItemClick(this)
                   getGenres()
                   getFilms()

        binding.apply {  listTv.layoutManager = GridLayoutManager(this@MainActivity,2)
                         listTv.adapter = adapter }

        binding.refreshButton.setOnClickListener { binding.listTv.smoothScrollToPosition(0) }

       binding.searchText.addTextChangedListener(object: TextWatcher {
           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

             //  Log.d("Ml","${start} --  ${before} --  ${count}")


               if ((binding.searchText.text.isBlank()) && (search == SEARCH_FILMS))
              { binding.searchText.hint = search; binding.withPosterCheckBox.isEnabled = true; } else { binding.withPosterCheckBox.isEnabled = false }


           }

           override fun afterTextChanged(s: Editable) {
               Log.d("Ml","before: ${s.length}")
//
                 if (s.contains('\n')) {
                     when  {
                        (s.length == 1) -> { s.clear() }
                         (s.length > 1) -> { searchFilmsAndActors(); s.delete(s.length -1 ,s.length) }
                     }

                 }

               }

       } )

                binding.top250Text.setOnClickListener {
                    onFavorite = false
                    vm.getTop250Films(); launchLifecycle(); search = SEARCH_FILMS
                    binding.searchText.text.clear()
                    binding.searchText.hint = search }

               binding.searchButton.setOnClickListener {
                   searchFilmsAndActors()
               }

        binding.filter.setOnClickListener { showfilterMenu(it) }
        binding.searchMenu.setOnClickListener { showSearchMenu(it) }

        binding.withPosterCheckBox.setOnClickListener {
           onFavorite = false
            if (binding.withPosterCheckBox.isChecked) withPoster_flag = true else withPoster_flag = false
            getFilms() }

        class MyBackPressedCallBack: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

               if (onMain) { AlertDialog.Builder(this@MainActivity)
                   .setTitle("Выход")
                   .setMessage("Действительно выйти?")
                   .setPositiveButton("Да") { _,_ -> finishAffinity() }
                   .setNegativeButton("Нет") { _,_ -> return@setNegativeButton }
                   .create()
                   .show()

               } else {
                   onMain = true
                getFilms()
                onFavorite = false
                searchOn = SEARCH_FILMS
                               }
            }}

        val onBackPressedCallback = MyBackPressedCallBack()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

    }

    private fun searchFilmsAndActors() {
        onFavorite = false
        if (binding.searchText.text.isBlank()) {
            search = SEARCH_FILMS; search = SEARCH_FILMS; binding.searchText.hint =
                search; getFilms(); launchLifecycle(); return
        }



        when (search) {
            SEARCH_FILMS -> {

                searchOn = SEARCH_FILMS

                vm.searchFilmsByTitle(binding.searchText.text.toString())
            }

            SEARCH_ACTORS -> {
                searchOn = SEARCH_ACTORS
                vm.searchPersons(binding.searchText.text.toString())
            }
        }
        launchLifecycle()
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
        lifecycleScope.launch { vm.data.collect { adapter.submitData(it) } }


    }
    private fun showSearchMenu(view: View) {
         val menu = PopupMenu(this,view)
        menu.menu.add(SEARCH_FILMS)
        menu.menu.add(SEARCH_ACTORS)
           menu.show()
            menu.setOnMenuItemClickListener {
              onFavorite = false
                search = it.title.toString(); binding.searchText.hint = search
                if (search == SEARCH_FILMS && binding.searchText.text.isBlank()) { binding.withPosterCheckBox.isEnabled = true } else

                    binding.withPosterCheckBox.isEnabled = false
                true }

    }
    fun showfilterMenu (view: View) {

        val menu = PopupMenu(this,view)
        menu.menu.add("Все жанры")
        genre_list.forEach { menu.menu.add(it.name) }

        menu.show()
        menu.setOnMenuItemClickListener {
                onFavorite = false
                  genre = it.title.toString()
            getFilms()
            true } }

    override fun onItemClick(id: Int) {


        val intent: Intent?

        if (searchOn == SEARCH_FILMS) {intent = Intent(this,FilmActivity::class.java)} else {intent = Intent(this,PersonActivity::class.java)}
         onMain = false
        intent.putExtra("id",id)
        startActivity(intent)
    }



    override fun show() {
        binding.progressUp.visibility = View.VISIBLE
    }

    override fun noshow() {
       binding.progressUp.visibility = View.GONE
    }



    override fun onStart() {
        super.onStart()



        if (onFavorite) {
            onMain = false
             vm.getFavoriteFilms()
            launchLifecycle()


    } else { onMain = true }

}

     }


