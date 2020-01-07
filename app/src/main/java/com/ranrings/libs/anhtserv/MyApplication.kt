package com.ranrings.libs.anhtserv

import android.app.Application
import com.ranrings.libs.androidapptorest.AndroidRestServer
import com.ranrings.libs.androidapptorest.RequestHandler

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        data class Person(var name : String,var age : Int,var alive : Boolean)

        val androidRestServer = AndroidRestServer.Builder()
            .setApplication(this)
            .addRequestHandler(object : RequestHandler<Person,Any>(Person::class){
                override fun getMethodName(): String {
                    return "getpersons"
                }

                override fun onRequest(requestBody: Person): Any {
                   return Person("Sushobh",12,false)
                }

            })
            .setPort(8080).
                build()
        androidRestServer.start()
    }
}
