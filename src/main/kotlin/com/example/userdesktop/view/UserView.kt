package com.example.userdesktop.view

import com.example.userdesktop.controller.UserController
import com.example.userdesktop.model.User
import tornadofx.*

class UserView : View("User App") {
    private val controller: UserController by inject()

    override val root = vbox {
        tableview(controller.users) {
            readonlyColumn("#", User::id)
            column("Name", User::name)
            column("Email", User::email)
            columnResizePolicy = SmartResize.POLICY
            onLeftClick {
                if (controller.selectedUser == null || selectedItem != controller.selectedUser) {
                    controller.selectUser(selectedItem)
                } else {
                    controller.clearFields()
                }
            }
        }

        hbox {
            textfield(controller.nameProperty) {
                promptText = "Name"
                minWidth = 250.0
            }
        }
        hbox {
            textarea(controller.emailProperty) {
                promptText = "Email"
                minWidth = 250.0
            }
        }

        hbox {
            button("Add User") {
                action { controller.addUser() }
            }
            button("Update User") {
                action { controller.updateUser() }
            }
            button("Delete User") {
                action { controller.deleteUser() }
            }
        }
    }
}
