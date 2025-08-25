package com.example.pagination_new.di

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.pagination_new.R
import com.example.pagination_new.data.retrofit.MainDb
import com.example.pagination_new.databinding.ActivityFilmBinding

import com.example.pagination_new.di.adapter.PersonAdapter
import com.example.pagination_new.domain.classesss.FavoriteFilm
import com.example.pagination_new.domain.classesss.film.Description
import com.example.pagination_new.domain.classesss.film.Film
import com.example.pagination_new.domain.classesss.film.Person
import com.example.pagination_new.domain.useCases.DeleteFilmByIdUseCase
import com.example.pagination_new.domain.useCases.GetFavoriteFilmsIdUseCase

import com.example.pagination_new.domain.useCases.GetFilmByIdUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FilmActivity : AppCompatActivity(),PersonAdapter.OnItemClick {
    private lateinit var binding: ActivityFilmBinding
   @Inject lateinit var getFilmByIdUseCase: GetFilmByIdUseCase
    var favoriteFilmsId: List<Int>? = null
   @Inject lateinit var db: MainDb
   @Inject lateinit var getFavoriteFilmsIdUseCase: GetFavoriteFilmsIdUseCase
   private val vm: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

      //  binding.titleText.text = "sdffsdf"



//       CoroutineScope(Dispatchers.IO).launch {
//           favoriteFilmsId = getFavoriteFilmsIdUseCase.execute(db)
//           favoriteFilmsId?.forEach { Log.d("Ml","$it") } }

        binding.head.buttonBack.setOnClickListener { finish() }

        val id = intent.getIntExtra("id",0)

       CoroutineScope(Dispatchers.IO).launch { favoriteFilmsId = getFavoriteFilmsIdUseCase.execute(db);
           val description = getFilmByIdUseCase.execute(id);
           description?.body()?.let { runOnUiThread { show(it) } } }

    }



    fun show(description: Description) {

        binding.apply {

            favoriteFilmsId?.let {

                if (it.contains(description.id)) {
                    description.favorite = true
                    head.favoriteTv.setImageResource(R.drawable.favorite_on)} else
                { head.favoriteTv.setImageResource(R.drawable.favorite_off)}}



            head.favoriteTv.setOnClickListener {

                if(description.favorite) {
                    description.favorite = false; head.favoriteTv.setImageResource(R.drawable.favorite_off)
                    vm.deleteFilmById(description.id)

                } else {
                description.favorite = true; head.favoriteTv.setImageResource(R.drawable.favorite_on)

               //  CoroutineScope(Dispatchers.IO).launch {
                     vm.insertFilm(FavoriteFilm(description.id,description.countries.getOrNull(0)?.name?: "",
                      description.genres.getOrNull(0)?.name?: "", name = description.name ?: description.alternativeName,
                      rating = description.rating.imdb.toString(), year = description.year?: 0, poster = description.poster?.url?: ""))
                 //}

            } }


            var list: List<Person> = listOf()

           if (description.persons != null) description.persons.forEach { list += it } else {
               val params = binding.personTv.layoutParams
               params.height = 0
               binding.personTv.layoutParams = params }

            imageTv.load(description.poster?.url) { listener(
                onStart = {binding.progressBar.visibility = View.VISIBLE},
                onSuccess = {_,_ -> binding.progressBar.visibility = View.GONE},
                onError = {_,_ -> binding.progressBar.visibility = View.GONE}
            ); error(R.drawable.no_image) }

                binding.personTv.layoutManager = LinearLayoutManager(this@FilmActivity,LinearLayoutManager.HORIZONTAL,false)

            val adapter = PersonAdapter(list)
            binding.personTv.adapter = adapter

           adapter.setOnItemClick(this@FilmActivity)

            description.genres.forEach { binding.genresTv.append("  ${it.name}") }

            head.titleText.text = if(description.name != null) description.name else description.alternativeName

            descriptionTv.text = description.description

            countriesText.text = "Производство: "
            description.countries.forEach { countriesText.append(" ${it.name}") };

            val year = if(description.year!= null) description.year else "Н/Д"
            yearText.setText("Дата выхода: $year")

            ratingText.setText( " Imdb: ${description.rating.imdb}\n Kp: ${description.rating.kp}\n FilmCritics: ${description.rating.filmCritics}\n Russianfilmcritic: ${description.rating.russianFilmCritics}")

            description.ageRating?.let {  ageRatingTv.text = "${it.toString()}+" }


        }


    }

    override fun onItemClick(id: Int) {
    val intent = Intent(this,PersonActivity::class.java)
        intent.putExtra("id",id)
        startActivity(intent)
    }
}