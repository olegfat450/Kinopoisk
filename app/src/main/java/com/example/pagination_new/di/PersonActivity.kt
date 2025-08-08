package com.example.pagination_new.di

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.pagination_new.R
import com.example.pagination_new.databinding.ActivityPersonBinding
import com.example.pagination_new.di.adapter.FilmAdapter
import com.example.pagination_new.domain.classesss.person.PersonItem
import com.example.pagination_new.domain.useCases.GetActorByIdUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class PersonActivity : AppCompatActivity(){

    private lateinit var binding: ActivityPersonBinding
    @Inject lateinit var getActorById: GetActorByIdUseCase
    var filmographyOpen = false
    lateinit var filmsAdapter: FilmAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

         binding.head.buttonBack.setOnClickListener { finish() }

        val id = intent.getIntExtra("id",0)

        CoroutineScope(Dispatchers.IO).launch { val person = getActorById.execute(id)
       person?.let {  runOnUiThread { show(it) } }
        }


    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun show(personItem: PersonItem) {

        data class D(val id:Int,val name: String)
        var listFilms: List<Pair<Int,String?>> = emptyList()

        binding.filmography.setOnClickListener {

            if(filmographyOpen) { binding.filmographyImage.setImageResource(R.drawable.down); filmographyOpen = false; installSizeAdapter(0)
            } else { binding.progressBar.visibility = View.VISIBLE


                CoroutineScope(Dispatchers.IO).launch {
               personItem.movies.forEach { listFilms += Pair(it.id, second = it.name?: it.alternativeName)

                  runOnUiThread {
                      filmsAdapter = FilmAdapter(this@PersonActivity,listFilms)
                      binding.FilmsTv.adapter = filmsAdapter
                      showFilms(filmsAdapter,listFilms.size); }

               }

                }

            }
        }




        binding.FilmsTv.onItemClickListener = AdapterView.OnItemClickListener { _,_, position,_ ->

            val intent = Intent(this,FilmActivity::class.java)
            intent.putExtra("id",listFilms[position].first)
            startActivity(intent)
        }

        binding.head.titleText.text = personItem.name


        var im = personItem.photo
        if (im?.get(6) != '/') im = im?.drop(6)

        binding.apply {

            imageTv.load(im) { listener(

            onStart = { imageProgressBar.visibility = View.VISIBLE },
             onSuccess = { _,_ ->  imageProgressBar.visibility = View.GONE },
             onError = { _,_ -> imageProgressBar.visibility = View.GONE }

            ); error(R.drawable.no_image) }


                       personItem.name?.let { nameTv.setText(it) }
                       personItem.enName?.let { enNameTv.setText(it) }
                       personItem.birthday?.let {

                           birthDataTv.setText("Дата рождения: ${dateFormat(it)}") }
                       personItem.age?.let { birthDataTv.append("  Возраст: $it") }

                     if ((personItem.birthPlace) != null && personItem.birthPlace.isNotEmpty() ) { birthPlaceTv.setText("Место рождения: "); personItem.birthPlace.forEach { birthPlaceTv.append(" ${it.value} /") } }

            personItem.death?.let { deathTv.text = "Дата смерти: ${dateFormat(it)}" }

           if( personItem.facts?.isEmpty() == false) {  factsTv.setText("Интерестные факты\n")
                personItem.facts.forEach {

                factsTv.append("\n${Jsoup.parse(it.value).text()}\n") }
        }}
    }

    fun showFilms(filmsAdapter: FilmAdapter,count: Int) {

        val layoutItem = filmsAdapter.getView(0,null,binding.FilmsTv)
        layoutItem.measure(0,0)
        val heightItem = layoutItem.measuredHeight
           val totalHeight = (heightItem * count) + (binding.FilmsTv.dividerHeight * count)
            installSizeAdapter(totalHeight)
            binding.progressBar.visibility = View.GONE
        filmographyOpen = true; binding.filmographyImage.setImageResource(R.drawable.up)

    }
    private fun installSizeAdapter(size: Int){
        val params = binding.FilmsTv.layoutParams
        params.height = size
        binding.FilmsTv.requestLayout()
    }

}
@RequiresApi(Build.VERSION_CODES.O)
fun dateFormat(string: String): String {
    val inputFormater = DateTimeFormatter.ISO_DATE_TIME
    val outputFormater = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val date = LocalDateTime.parse(string,inputFormater)
    return outputFormater.format(date)
}