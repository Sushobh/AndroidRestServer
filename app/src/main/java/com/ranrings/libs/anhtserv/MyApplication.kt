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
                addRequestHandler(object : RequestHandler<Person,Any>(Person::class) {
                    override fun getMethodName(): String {
                        return "getpersonsummary"
                    }

                    override fun onRequest(requestBody: Person): Any {
                        return """
                            { 'personSummary' : '${requestBody.name}' 
                            is ${requestBody.age} years old 
                            and is ${if(requestBody.alive) {
                                   "Alive,fortunately."
                             }
                           else {
                            "Dead,unfortunately."
                           }
                            }
                        """.trimIndent()
                    }

                }).
                build()
        androidRestServer.start()
    }
}
