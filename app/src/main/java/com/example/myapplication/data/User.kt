package com.example.myapplication.data

class User (
    private val email: String?,
    private val token: String?,
    private val name: String?,
    private val image: Int,
    private val docId: String) {

    fun getEmail(): String? {
        return email
    }

    fun getToken(): String? {
        return token
    }

    fun getName() : String? {
        return name
    }

    fun getImage() : Int {
        return image
    }

    fun getDocId() : String {
        return docId
    }
}