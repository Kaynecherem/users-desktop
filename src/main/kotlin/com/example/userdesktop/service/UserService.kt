package com.example.userdesktop.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.example.userdesktop.model.User
import com.example.userdesktop.request.UserRequest
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class UserService {
    private val apiUrl = "http://localhost:8082/api/users"
    private val client = HttpClient.newHttpClient()
    private val objectMapper = jacksonObjectMapper()

    fun getAllUsers(): List<User> {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return objectMapper.readValue<List<User>>(response.body())
    }

    fun getUserById(id: Long): User? {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$apiUrl/$id"))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val apiResponse = objectMapper.readValue<User>(response.body())
        return apiResponse
    }

    fun createUser(todo: UserRequest): User? {
        val requestBody = objectMapper.writeValueAsString(todo)
        val request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .header("Content-Type", "application/json")
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() !in 200..299) {
            return null
        }
        val apiResponse = objectMapper.readValue<User>(response.body())
        return apiResponse
    }

    fun updateUser(id: Long, todo: UserRequest): User? {
        val requestBody = objectMapper.writeValueAsString(todo)
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$apiUrl/$id"))
            .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
            .header("Content-Type", "application/json")
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val apiResponse = objectMapper.readValue<User>(response.body())
        return apiResponse
    }

    fun deleteUser(id: Long): Boolean {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$apiUrl/$id"))
            .DELETE()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.statusCode() == 204
    }
}
