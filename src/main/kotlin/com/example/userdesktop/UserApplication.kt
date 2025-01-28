package com.example.userdesktop

import com.example.userdesktop.view.UserView
import javafx.application.Application
import tornadofx.*

class TodoApplication : App(UserView::class)

fun main() = Application.launch(TodoApplication::class.java)