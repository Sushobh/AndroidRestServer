package com.ranrings.libs.androidapptorest

import android.app.Application
import com.google.gson.Gson
import com.ranrings.libs.androidapptorest.RequestCallerTest.Companion.getPersonJSONString
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.lang.Exception

class ApiTests {

    @Test
    fun test2() {

        val port = 4200
        var androidRestServer = AndroidRestServer.Builder()
            .setApplication(mock(Application::class.java))
            .addRequestHandler(object : RequestHandler<RequestCallerTest.Person, RequestCallerTest.Person>
                (RequestCallerTest.Person::class.java,RequestCallerTest.Person::class.java)
            {
                override fun getMethodName(): String {
                    return "method"
                }

                override fun onRequest(requestBody: RequestCallerTest.Person): RequestCallerTest.Person {
                    return  RequestCallerTest.Person("Sushobh Nadiger",120,false)
                }

            }).setPort(port).build()
        androidRestServer.start()
        var service = ApiRequests.getVeryOwnRetrofit(port)
            .create(ApiRequests.POSTService::class.java)
        val map : Map<String,Any> =  mapOf("name" to "Sushobh Nadiger", "age" to 120, "alive" to false)
        val result = service.makeRequest("/method",map).blockingFirst()
        assertTrue( result.asJsonObject.keySet().size == 3)
        assertNotNull(result)
        androidRestServer.stop()
        Thread.sleep(2000)
    }



}