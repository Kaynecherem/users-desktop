package com.example.userdesktop.controller

import com.example.userdesktop.model.User
import com.example.userdesktop.request.UserRequest
import com.example.userdesktop.service.UserService
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class UserController : Controller() {
    private val userService = UserService()

    val users = mutableListOf<User>().asObservable()

    val nameProperty = SimpleStringProperty()
    private var name: String? by nameProperty

    val emailProperty = SimpleStringProperty()
    private var email: String? by emailProperty

    var selectedUser: User? = null

    init {
        loadUsers()
    }

    private fun loadUsers() {
        runAsync {
            userService.getAllUsers()
        } ui { users.setAll(it) }
    }

    fun selectUser(selectedItem: User?) {
        selectedUser = selectedItem
        if (selectedItem != null) {
            name = selectedItem.name
            email = selectedItem.email
        }
    }

    fun addUser() {
        if (name.isNullOrBlank()) {
            return
        }

        val newUser = UserRequest(
            name = name!!,
            email = email ?: ""
        )
        runAsync {
            userService.createUser(newUser)
        } ui { todo ->
            if (todo != null) {
                users.add(todo)
            }
            clearFields()
        }
    }

    fun updateUser() {
        selectedUser?.let {
            val updatedUser = UserRequest(
                name = if (name.isNullOrBlank()) it.name else name!!,
                email = if (email.isNullOrBlank()) it.email else email!!
            )
            runAsync {
                userService.updateUser(it.id, updatedUser)
            } ui { todo ->
                if (todo != null) {
                    users[users.indexOf(selectedUser)] = todo
                }
                clearFields()
            }
        }
    }

    fun deleteUser() {
        selectedUser?.apply {
            runAsync {
                userService.deleteUser(id)
            } ui { success ->
                if (success) {
                    users.removeIf {
                        it.id == id
                    }
                }
                clearFields()
            }
        }
    }

    fun clearFields() {
        name = ""
        email = ""
        selectedUser = null
    }
}
