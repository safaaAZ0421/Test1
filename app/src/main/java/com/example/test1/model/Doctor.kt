package com.example.test1.model

import java.io.Serializable

data class Doctor(
    val id: String = "",
    val name: String = "",
    val specialty: String = "",
    val address: String = ""
): Serializable


