package com.example.hackeruapp.managers



object LoginManager {

    var databaseUsername = "ofek"
    var databasePassword = "123"

    fun login(username:String, password:String): Boolean {
        if (username == databaseUsername && password == databasePassword) {
            return true
        }
        return false
    }

    fun signup(username: String,password: String) {
        databaseUsername = username
        databasePassword = password
    }
}