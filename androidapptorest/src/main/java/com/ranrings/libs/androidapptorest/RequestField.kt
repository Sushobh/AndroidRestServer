package com.ranrings.libs.androidapptorest


@Target(AnnotationTarget.FIELD)
annotation class RequestField(val description : String = "",
                              val required : Boolean = false)