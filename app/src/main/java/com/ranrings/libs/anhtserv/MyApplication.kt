package com.ranrings.libs.anhtserv

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.ranrings.libs.androidapptorest.AndroidRestServer
import com.ranrings.libs.androidapptorest.RequestHandler
import org.json.JSONObject



class MyApplication : Application() {

    data class Person(var name: String){

    }




    override fun onCreate() {
        super.onCreate()





        val androidRestServer = AndroidRestServer.Builder()
            .setApplication(this)
            .setPort(8080)
            .addRequestHandler(object : RequestHandler<Person,Any>(Person::class) {
                override fun getMethodName(): String {
                    return "getpackagename"
                }

                override fun onRequest(requestBody: Person): Any {
                    return JSONObject().apply {
                        put("packagename",this@MyApplication.packageName)
                    }
                }

            }).addRequestHandler(object : RequestHandler<Person,Any>(Person::class) {
                override fun getMethodName(): String {
                    return "getcurrentactivity"
                }

                override fun onRequest(requestBody: Person): Any {
                    val am =
                        getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    val cn = am.getRunningTasks(1)[0].topActivity
                    return JSONObject().apply {
                        put("packagename",cn.className)
                    }
                }

            })
            .
                build()
        androidRestServer.start()
    }
}
