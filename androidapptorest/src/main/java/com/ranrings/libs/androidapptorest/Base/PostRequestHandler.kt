package com.ranrings.libs.androidapptorest.Base

import kotlin.reflect.KClass

abstract class PostRequestHandler<A : Any, B>(var requestClass : KClass<A>) : RequestHandler<A, B>(requestClass) {

    override fun getMethodType(): ASMethodType {
        return ASMethodType.POST
    }
}