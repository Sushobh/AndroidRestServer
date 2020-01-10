package com.ranrings.libs.androidapptorest.Base

import com.ranrings.libs.androidapptorest.Base.GetRequestHandler
import java.io.InputStream

abstract class GetInputStreamRequestHandler : GetRequestHandler<InputStream>() {

    abstract fun getMimeType(requestUri: String): String

}