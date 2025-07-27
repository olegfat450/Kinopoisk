package com.example.pagination_new.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.pagination_new.R
import com.example.pagination_new.databinding.ActivityIdBinding
import com.example.pagination_new.di.adapter.PersonAdapter
import com.example.pagination_new.domain.classess.Description
import com.example.pagination_new.domain.classess.Person
import com.example.pagination_new.domain.useCases.GetFilmByIdUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdBinding
   @Inject lateinit var getFilmByIdUseCase: GetFilmByIdUseCase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

      //  binding.titleText.text = "sdffsdf"

        binding.buttonBack.setOnClickListener { finish() }

        val id = intent.getIntExtra("id",0)

       CoroutineScope(Dispatchers.IO).launch { val descroption = getFilmByIdUseCase.execute(id);
           descroption?.body()?.let { runOnUiThread { show(it) } } }

    }

    fun show(description: Description) {

        binding.apply {

            var list: List<Person> = listOf()

           if (description.persons != null) description.persons.forEach { list += it } else {
               val params = binding.personTv.layoutParams
               params.height = 0
               binding.personTv.layoutParams = params }

            imageTv.load(description.poster?.url) { crossfade(true); error(R.drawable.no_image) }

                binding.personTv.layoutManager = LinearLayoutManager(this@IdActivity,LinearLayoutManager.HORIZONTAL,false)

            val adapter = PersonAdapter(list)

            binding.personTv.adapter = adapter

            description.genres.forEach { binding.genresTv.append("  ${it.name}") }

        //  description.persons.forEach {  Log.d("Ml","${it.name}") }
             titleText.text = if(description.name != null) description.name else description.alternativeName

            descriptionTv.text = description.description

            countriesText.text = "Производство: "
            description.countries.forEach { countriesText.append(" ${it.name}") };
            yearText.setText("Дата выхода: ${description.year}")

            ratingText.setText( " Imdb: ${description.rating.imdb}\n Kp: ${description.rating.kp}\n FilmCritics: ${description.rating.filmCritics}\n Russianfilmcritic: ${description.rating.russianFilmCritics}")

            description.ageRating?.let {  ageRatingTv.text = "${it.toString()}+" }



        }


    }
}