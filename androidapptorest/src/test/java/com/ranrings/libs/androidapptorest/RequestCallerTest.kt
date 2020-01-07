package com.ranrings.libs.androidapptorest

import android.app.Activity
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.skyscreamer.jsonassert.JSONAssert
import java.lang.Exception


class RequestCallerTest {


    internal var requestCaller = Mockito.spy(RequestCaller())


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
        val someString = getPersonJSONString()
        val person : Person = requestCaller.parsePostBodyFromJSONString(someString,Person::class.java)
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
            "Hello1",String::class.java,String::class.java))
        assertThrows(Exception::class.java,{
           requestCaller.addRequestHandler(handler1)
       })
        handler1 = Mockito.spy(TestMethodHandler<String, String>("method1",
            "Hello1",String::class.java,String::class.java))
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

    open class TestMethodHandler<RQ, RB>(var nameOfMethod: String,
                                         var body: RB,val classReq : Class<RQ>, val classRes : Class<RB>) :
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


   }


    data class Person(var name : String,var age : Int,var alive : Boolean) {

    }


}