package com.ranrings.libs.androidapptorest

import android.app.Activity
import android.app.Application
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.skyscreamer.jsonassert.JSONAssert
import java.lang.Exception
import kotlin.reflect.KClass


class RequestCallerTest {


    internal var requestCaller = Mockito.spy(RequestCaller(mock(Application::class.java)))


    @Test
    fun onRequestReceived() {

    }

    @Test
    fun parseUriToGetMethodName() {
        assertEquals(null, requestCaller.parseUriToGetMethodName("methodName"))
        assertEquals("methodName", requestCaller.parseUriToGetMethodName("/methodName"))
        assertEquals(null, requestCaller.parseUriToGetMethodName("/methodName?hello=ok"))
        assertEquals(null, requestCaller.parseUriToGetMethodName("/methodName/ok/right"))
    }



    @Test
    fun parsePostBodyFromJSONString() {
        val someString = getPersonJSONString2()
        val person : Person = requestCaller.parsePostBodyFromJSONString(someString,Person::class)
        assertNotNull(someString)
        assertEquals(person.age,120)
        assertEquals(person.name,"SushobhNadiger")
        assertFalse(person.alive)
    }

    @Test
    fun convertObjectToJSONString() {
        val someString = getPersonJSONString()
        val person : Person = Person(name = "SushobhNadiger",age = 120,alive = false)
        val convertedString = requestCaller.convertObjectToJSONString(person)

        JSONAssert.assertEquals(JSONObject(someString), JSONObject(convertedString),false)
    }


    @Test
    fun addRequestHandler() {
        var handler1 = Mockito.spy(TestMethodHandler<String, String>("method1/",
            "Hello1",String::class.java.kotlin,String::class))
        assertThrows(Exception::class.java,{
           requestCaller.addRequestHandler(handler1)
       })
        handler1 = Mockito.spy(TestMethodHandler<String, String>("method1",
            "Hello1",String::class.java.kotlin,String::class))
        assertThrows(RequestCaller.WrongRequestHandlerException::class.java,{
            requestCaller.addRequestHandler(handler1)
        })

    }


    @Test
    fun isValidRequestClass(){
        data class TestDataClass1(  var name : String ,
                                  var alive : Boolean,
                                  var cash : Double,
                                  var age  :Int)
        assertTrue(requestCaller.isValidRequestClass(TestDataClass1::class))
        assertThrows(RequestCaller.WrongRequestHandlerException::class.java,{
            requestCaller.isValidRequestClass(Activity::class)
        })
        data class TestDataClass2(var name : String , var person : Person)
        assertThrows(RequestCaller.WrongRequestHandlerException::class.java,{
            requestCaller.isValidRequestClass(TestDataClass2::class)
        })
    }

    open class TestMethodHandler<RQ : Any, RB : Any>(var nameOfMethod: String,
                                                     var body: RB, val classReq : KClass<RQ>, val classRes : KClass<RB>) :
        RequestHandler<RQ, RB>(classReq,classRes) {
        override fun getMethodName(): String {
            return nameOfMethod
        }


        override fun onRequest(requestBody: RQ): RB {
            return body
        }
    }

   companion object {
       fun getPersonJSONString() : String {
           return """
            {
            'name' : 'SushobhNadiger',
            'age' : 120,
            'alive' : false
            }
        """.trimIndent()
       }

       fun getPersonJSONString2() : String {
           return "{\"name\" : \"Sushobh\",\"alive\" : true,\"age\" : 2}"
       }


   }


    data class Person(var name : String,var age : Int,var alive : Boolean) {

    }


}