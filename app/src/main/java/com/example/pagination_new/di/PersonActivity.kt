package com.example.pagination_new.di

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
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

        personItem.movies.forEach { listFilms += Pair(it.id, second = it.name?: it.alternativeName) }



        val filmsAdapter = FilmAdapter(this,listFilms)
        binding.FilmsTv.adapter = filmsAdapter

        binding.FilmsTv.layoutParams.height = 0

        binding.filmography.setOnClickListener {
            if(filmographyOpen) { binding.filmographyImage.setImageResource(R.drawable.down); filmographyOpen = false; installSizeAdapter(0)
            } else { showFilms(filmsAdapter,listFilms.size); filmographyOpen = true; binding.filmographyImage.setImageResource(R.drawable.up) }
        }

       // var tH = 0

      //  val adapterSize = binding.FilmsTv.size


//        for (i in 0..  adapterSize-1) {
       //     val lI = filmsAdapter.getView(0,null,binding.FilmsTv)

//            tH += lI.measuredHeight
//        //   Log.d("Ml",lI.measuredHeight.toString())
//        }
//
//        Log.d("Ml","$tH")


      //  Log.d("Ml","${listFilms.size}  --  ${binding.FilmsTv.size}")

      //  lI.measure(0,0)
      //  val total_heigth = (lI.measuredHeight * listFilms.size) + (binding.FilmsTv.dividerHeight * listFilms.size)
       // val total_heigth = item_heigth
     //   val params = binding.FilmsTv.layoutParams
     //  params.height = total_heigth
       // binding.FilmsTv.requestLayout()
      //  Log.d("Ml","${lI.measuredHeight} --  ${binding.FilmsTv.dividerHeight}  ")


        binding.FilmsTv.onItemClickListener = AdapterView.OnItemClickListener { _,_, position,_ ->

            val intent = Intent(this,IdActivity::class.java)
            intent.putExtra("id",listFilms[position].first)
            startActivity(intent)
        }


       // Log.d("Ml","LP: ${tH+(binding.FilmsTv.dividerHeight * (listFilms.size -1))}")
//        binding.FilmsTv.layoutParams = params
//        binding.FilmsTv.requestLayout()

        binding.head.titleText.text = personItem.name
      //  binding.imageTv.setImageURI(personItem.photo?.toUri())

        var im = personItem.photo
        if (im?.get(6) != '/') im = im?.drop(6)

       // Log.d("Ml","${im}")
        binding.apply { imageTv.load(im) { error(R.drawable.no_image)}
                       personItem.name?.let { nameTv.setText(it) }
                       personItem.enName?.let { enNameTv.setText(it) }
                       personItem.birthday?.let {

                           birthTv.setText("Дата рождения: ${dateFormat(it)}") }
                       personItem.age?.let { birthTv.append("  Возраст: $it") }
            personItem.death?.let { deathTv.text = "Дата смерти: ${dateFormat(it)}" }

           if( personItem.facts?.isEmpty() == false) {  factsTv.setText("Интерестные факты\n")
                personItem.facts.forEach {

                factsTv.append("\n${Jsoup.parse(it.value).text()}\n") }
        }}
    }

    private fun showFilms(filmsAdapter: FilmAdapter,count: Int) {

        val layoutItem = filmsAdapter.getView(0,null,binding.FilmsTv)
        layoutItem.measure(0,0)
        val heightItem = layoutItem.measuredHeight
        val totalHeight = (heightItem * count) + (binding.FilmsTv.dividerHeight * count)
           installSizeAdapter(totalHeight)



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