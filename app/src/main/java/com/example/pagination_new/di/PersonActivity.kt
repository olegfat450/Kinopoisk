package com.example.pagination_new.di

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.pagination_new.R
import com.example.pagination_new.databinding.ActivityPersonBinding
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
    private fun show(personItem: PersonItem){

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

}
@RequiresApi(Build.VERSION_CODES.O)
fun dateFormat(string: String): String {
    val inputFormater = DateTimeFormatter.ISO_DATE_TIME
    val outputFormater = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val date = LocalDateTime.parse(string,inputFormater)
    return outputFormater.format(date)
}