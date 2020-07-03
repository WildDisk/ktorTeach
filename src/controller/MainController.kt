package ru.wilddisk.controller

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import data.model.User
import ru.wilddisk.data.repository.UserRepository
import ru.wilddisk.util.users

/**
 * Function responsible for using the freemarker template and sending entrance data
 */
fun Application.hello() {
    routing {
        get("/") {
            call.respond(
                FreeMarkerContent(
                    "hello.ftl",
                    mapOf("user" to UserRepository().allUsers()[0], "users" to UserRepository().allUsers())
                )
            )
        }
        post {
            val postValue = call.receiveParameters()
            users.add(
                User(
                    postValue["username"].toString(),
                    postValue["password"].toString(),
                    postValue["firstName"],
                    postValue["lastName"],
                    postValue["email"].toString()
                )
            )
            call.respondRedirect("/")
        }
    }
}

