package com.ranrings.libs.anhtserv

import android.app.Activity
import android.app.Application
import com.ranrings.libs.androidapptorest.AndroidRestServer
import com.ranrings.libs.androidapptorest.RequestHandler

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        data class Person(var name : String,var age : Int,var alive : Boolean)

        val androidRestServer = AndroidRestServer.Builder()
            .setApplication(this)
            .setPort(8080).
                build()
        androidRestServer.start()
    }
}
