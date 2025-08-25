package com.example.pagination_new

import android.app.Application
import android.util.Log
import com.example.pagination_new.data.retrofit.MainDb
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class App: Application () {

//    @Inject lateinit var database: MainDb
//
//    fun log() { Log.d("Ml","AppDb: ${database}")}

    // val database by lazy { MainDb.getDataBase(this) }

}