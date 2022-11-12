# AndroidRestServer [![](https://jitpack.io/v/Sushobh/AndroidRestServer.svg)](https://jitpack.io/#Sushobh/AndroidRestServer)

A web and REST interface to your Android application. Allows you to access Application specific state/data via REST api or a web interface.

## Building blocks.
   The library is built up of two components.
   - A Rest server that allows you write GET,POST apis.
   - An optional web app that allows you to access these apis. You can however choose to access the apis directly also.
   
## Screenshots
   Here are the screenshots of the web app.

   ![](https://raw.github.com/Sushobh/AndroidRestServer/master/Screenshot2.png)
   ![](https://raw.github.com/Sushobh/AndroidRestServer/master/Screenshot1.png)
   
## Use cases
  - Run SQLite queries using a REST Api.
  - Access SharedPreferences data.
  - Get the name of the current activity.
  - Creating a file browser for your device as a web app.
  
  Basically anything info that you need from your Android app.



## How to get it?

In your project level gradle file, add this

```kotlin
allprojects {
   repositories {
	  maven { url 'https://jitpack.io' }
	}
   }
```
And then add the dependency in your app level gradle file
```java
   dependencies {
		  implementation 'com.github.Sushobh:AndroidRestServer:Release2'
    }
```
  
## How to use it? 
   Once you have imported the library,  you will need to initialize the library in you application class.
   For example here is you how you create two requests , one GET and another one which is POST.
   ```java
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
                        .addRequestHandler(object : GetRequestHandler<Any>(){

                            override fun onGetRequest(uri: String): Any {
                                return packageName
                            }

                            override fun getMethodName(): String {
                                return "getpackagename"
                            }

                        }).
                            addRequestHandler(object : PostRequestHandler<Person,Any> (Person::class){
                                override fun getMethodName(): String {
                                    return "personsummary"
                                }

                                override fun onRequest(requestBody: Person): Any {
                                    return "${requestBody.name} is ${requestBody.age} years of age and lives in     ${requestBody.city}."
                                }

                            }).
                            startWebApp(true).build()

                    androidRestServer.start()

                }
            }
   
   ```
   
   ### Things to note.
   - The library will create the server on the port of your choosing in your device. You will then need to run an adb command 
   mentioned in the upcoming section.
   - In order to to create a GET request handler , you need to extend GetRequestHandler and in order to create a POST request 
   handler you will need to extend the post request handler class as can be seen in the above code snipet.
   - When you extend the POST request handler , you will need to create a data class which describes the post request body
      structure. AndroidRestServer then converts the post body into an instance of the data class.
   -  **Note that only data classes are supported for this purpose and and only _int,boolean,string,double_ fields inside data classes are supported**.   
   - ***Do not define your post body data classes inside functions, always create them in classes or seperate Kotlin files.If you define data classes inside functions , the library will be unable to parse the post body.***
   -  The library will throw an exception if you do not use a data class or use a field that is not supported inside of the data class.   
  -  In the web application, form fields are dynamiclly generated based on the fields of the data class. Refer to the screen shots to see how the person class is used to create three form fields for age,name,city.  
  
  
  ### After adding the code
   After you have done this , run you application and then run this command in order to forward requests from your device to
    your computer.
    
   **adb forward tcp:8080 tcp:8080**
    
   The second port is the port where you will access the api and the web application, you can choose a different port also.
   Once you have done this, visit the link.
   **http://localhost:8080/webapp** to access the web app. 
   
   If you chose not to start the webapp you can access the apis 
   ,directly at **http://localhost:8080/[your_method_name]**
   
   for example if your method name is **getcurrentactivity** you will access it like this
   **http://localhost:8080/getcurrentactivity**
   
      

    
    
# What next? 
  Here is a list of things that i want to do next.
- [x] Create the library and publish it.  
- [ ] Add support for creating request handlers which will serve files.
- [ ] Add more tests and allow support for Map request body in post requests.
- [ ] Find out a way to run the adb port forward command programatically.
   
   
   
