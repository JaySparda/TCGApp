package com.jay.tcgapp

import android.app.Application
import androidx.room.Room
import com.google.android.material.color.DynamicColors
import com.jay.tcgapp.data.db.MyDatabase
import com.jay.tcgapp.data.repo.CardsRepo

class MyApp: Application() {

    lateinit var repo: CardsRepo

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        val db = Room.databaseBuilder(
            this,
            MyDatabase::class.java,
            MyDatabase.NAME
        )
            .build()
        repo = CardsRepo(db.getCardDao())
    }
}