package com.ranrings.libs.anhtserv

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.ranrings.libs.androidapptorest.AndroidRestServer
<<<<<<< HEAD
import com.ranrings.libs.androidapptorest.GetRequestHandler
=======
import com.ranrings.libs.androidapptorest.Base.GetRequestHandler
import com.ranrings.libs.androidapptorest.Base.PostRequestHandler
import com.ranrings.libs.androidapptorest.Base.RequestHandler
>>>>>>> acc42f007044333c839496a4ae409fe766cbb9b9
import org.json.JSONObject



class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val androidRestServer = AndroidRestServer.Builder()
            .setApplication(this)
            .setPort(8080)
<<<<<<< HEAD
            .addRequestHandler(object : GetRequestHandler() {
                override fun onGetRequest(uri: String): Any {
=======
            .addRequestHandler(object : PostRequestHandler<Person, Any>(Person::class) {
                override fun getMethodName(): String {
                    return "getpackagename"
                }

                override fun onRequest(requestBody: Person): Any {
>>>>>>> acc42f007044333c839496a4ae409fe766cbb9b9
                    return JSONObject().apply {
                        put("packagename",this@MyApplication.packageName)
                    }
                }
<<<<<<< HEAD
                override fun getMethodName(): String {
                    return "getpackagename"
=======

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
>>>>>>> acc42f007044333c839496a4ae409fe766cbb9b9
                }
            })
            .
                startWebApp(true).
                build()
        androidRestServer.start()
    }
}
