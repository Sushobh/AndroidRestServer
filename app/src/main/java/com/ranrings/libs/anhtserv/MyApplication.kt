package com.ranrings.libs.anhtserv

import android.app.Application
import com.ranrings.libs.androidapptorest.AndroidRestServer
import com.ranrings.libs.androidapptorest.Base.GetRequestHandler
import com.ranrings.libs.androidapptorest.Base.PostRequestHandler
import com.ranrings.libs.androidapptorest.ReactWebApp
import java.util.*


class MyApplication : Application() {

    val port = 8080

    data class Person(var name: String , var age : Int, var city : String)

    companion object {
        lateinit var application: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        val androidRestServer = AndroidRestServer.Builder()
            .setApplication(this)
            .setPort(port)
            .addWebApp(
                ReactWebApp(
                    this,
                    assets.open("build.zip"),
                    "activitytracker",
                    UUID.randomUUID().toString()
                )
            )
            .addWebApp(
                ReactWebApp(
                    this,
                    assets.open("build.zip"),
                    "activitytrackerTwo",
                    UUID.randomUUID().toString()
                )
            ).addWebApp(
                ReactWebApp(
                    this,
                    assets.open("build.zip"),
                    "activitytrackerThree",
                    UUID.randomUUID().toString()
                )
            )
            .addRequestHandler(object : GetRequestHandler<Any>(){

                override fun onGetRequest(uri: String): Any {
                    return packageName
                }

                override fun getMethodName(): String {
                    return "getpackagename"
                }

            }).addRequestHandler(object : PostRequestHandler<Person, Any>(Person::class) {
                override fun getMethodName(): String {
                    return "personsummary"
                }

                override fun onRequest(requestBody: Person): Any {
                    return "${requestBody.name} is ${requestBody.age} years of age and lives in ${requestBody.city}."
                }

            }).startWebApp(true).build()

        androidRestServer.start()
    }


}
