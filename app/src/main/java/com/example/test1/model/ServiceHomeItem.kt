package com.example.test1.models
import java.io.Serializable

data class ServiceHomeItem(
    val serviceName: String,
    val serviceImage: Int,
    val activityClass: Class<*>
): Serializable
