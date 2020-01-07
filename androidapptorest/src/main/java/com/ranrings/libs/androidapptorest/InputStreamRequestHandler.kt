package com.ranrings.libs.androidapptorest

import java.io.InputStream

abstract class InputStreamRequestHandler<X>(val requestClass : Class<X>) : RequestHandler<X,InputStream>(requestClass) {

    abstract fun getMimeType() : String

}