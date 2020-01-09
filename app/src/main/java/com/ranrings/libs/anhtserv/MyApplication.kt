package com.ranrings.libs.anhtserv

import android.app.Activity
import android.app.Application
import com.ranrings.libs.androidapptorest.AndroidRestServer
import com.ranrings.libs.androidapptorest.RequestHandler
import org.json.JSONObject

class MyApplication : Application() {

    data class Person(var name : String){

    }

    override fun onCreate() {
        super.onCreate()



        val androidRestServer = AndroidRestServer.Builder()
            .setApplication(this)
            .setPort(8080).addRequestHandler(object : RequestHandler<Person,Any>(Person::class) {
                override fun getMethodName(): String {
                   return "getname"
                }

                override fun onRequest(requestBody: Person): Any {
                    return JSONObject().apply {
                        put("message","Good Morning ${requestBody.name}")
                    }
                }

            }).
                build()
        androidRestServer.start()
    }
}
