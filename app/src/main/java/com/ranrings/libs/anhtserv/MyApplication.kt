package com.ranrings.libs.anhtserv

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.ranrings.libs.androidapptorest.AndroidRestServer
import com.ranrings.libs.androidapptorest.Base.GetRequestHandler
import com.ranrings.libs.androidapptorest.Base.PostRequestHandler
import com.ranrings.libs.androidapptorest.Base.RequestHandler
import org.json.JSONObject



class MyApplication : Application() {

    data class Person(var name: String){

    }




    override fun onCreate() {
        super.onCreate()

        val androidRestServer = AndroidRestServer.Builder()
            .setApplication(this)
            .setPort(8080)
            .addRequestHandler(object : PostRequestHandler<Person, Any>(Person::class) {
                override fun getMethodName(): String {
                    return "getpackagename"
                }

                override fun onRequest(requestBody: Person): Any {
                    return JSONObject().apply {
                        put("packagename",this@MyApplication.packageName)
                    }
                }

            }).addRequestHandler(object : PostRequestHandler<Person, Any>(Person::class) {
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
            .addRequestHandler(object : GetRequestHandler<Any>() {
                override fun onGetRequest(uri: String): Any {
                    return "At best"
                }

                override fun getMethodName(): String {
                    return "feel"
                }

            })
            .
                startWebApp(true).
                build()
        androidRestServer.start()
    }
}
