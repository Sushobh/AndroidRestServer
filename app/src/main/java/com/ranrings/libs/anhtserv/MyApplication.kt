package com.ranrings.libs.anhtserv

import android.app.Application
import com.ranrings.libs.androidapptorest.AndroidRestServer

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        val androidRestServer = AndroidRestServer.Builder()
            .setApplication(this)
            .setPort(8080).
                build()
        androidRestServer.start()
    }
}
