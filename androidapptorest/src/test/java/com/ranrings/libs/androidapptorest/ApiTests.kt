package com.ranrings.libs.androidapptorest

import android.app.Application
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class ApiTests {

    @Test
    fun test2() {

        val port = 4300
        var androidRestServer = AndroidRestServer.Builder()
            .setApplication(mock(Application::class.java))
            .addRequestHandler(object : RequestHandler<RequestCallerTest.Person, RequestCallerTest.Person>
                (RequestCallerTest.Person::class,RequestCallerTest.Person::class)
            {
                override fun getMethodName(): String {
                    return "method"
                }

                override fun onRequest(requestBody: RequestCallerTest.Person): RequestCallerTest.Person {
                    return  RequestCallerTest.Person("Sushobh Nadiger",120,false)
                }

            }).
        addRequestHandler(object : RequestHandler<RequestCallerTest.Person, RequestCallerTest.Person>
            (RequestCallerTest.Person::class,RequestCallerTest.Person::class)
        {
            override fun getMethodName(): String {
                return "methodtwo"
            }

            override fun onRequest(requestBody: RequestCallerTest.Person): RequestCallerTest.Person {
                return  RequestCallerTest.Person("Donald Trump",120,false)
            }

        })
            .setPort(port).buildForTest()
        androidRestServer.start()
        var service = ApiRequests.getVeryOwnRetrofit(port)
            .create(ApiRequests.POSTService::class.java)
        val map : Map<String,Any> =  mapOf("name" to "Sushobh Nadiger", "age" to 120, "alive" to false)
        val result = service.makeRequest("/method",map).blockingFirst()
        assertTrue( result.asJsonObject.keySet().size == 3)
        assertNotNull(result)
        Thread.sleep(2000)
        androidRestServer.stop()

    }

    @Test
    fun test3(){
        val port = 4300
        var androidRestServer = AndroidRestServer.Builder()
            .setApplication(mock(Application::class.java)).setPort(port).buildForTest()
        androidRestServer.start()
        var service = ApiRequests.getVeryOwnRetrofit(port)
            .create(ApiRequests.POSTService::class.java)
        val map : Map<String,Any> =  mapOf("methodName" to "getmethodinfo")
        val result = service.makeRequest("/getmethodinfo",map).blockingFirst()
        assertTrue( result.asJsonObject.keySet().size == 3)
        assertNotNull(result)
        androidRestServer.stop()
        Thread.sleep(2000)
    }

    @Test
    fun test4(){
        val port = 4300
        var androidRestServer = AndroidRestServer.Builder()
            .setApplication(mock(Application::class.java)).setPort(port).buildForTest()
        androidRestServer.start()
        var service = ApiRequests.getVeryOwnRetrofit(port)
            .create(ApiRequests.GETService::class.java)
        val result = service.makeRequest("/getmethodinfo").blockingFirst()
        assertNotNull(result)
        androidRestServer.stop()
        Thread.sleep(2000)
    }

    @Test
    fun test5(){
        var service = ApiRequests.getVeryOwnRetrofit(8080)
            .create(ApiRequests.POSTService::class.java)
        val map : Map<String,Any> =  mapOf("name" to "Sushobh Nadiger", "age" to 120, "alive" to false)
        val result = service.makeRequest("/getpersonsummary",map).blockingFirst()
        assertTrue( result.asJsonObject.keySet().size == 3)
        assertNotNull(result)
        Thread.sleep(2000)
    }


//{"name":"Sushobh Nadiger","age":120,"alive":false}
}