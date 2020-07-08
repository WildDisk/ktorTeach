package ru.wilddisk

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import ru.wilddisk.controller.hello
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HelloTest {
    fun `Find rows in FreeMarker template`(): Unit = withTestApplication {
        application.hello()
        handleRequest(HttpMethod.Get, "/") {
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue { response.content!!.contains("Hello admin!") }
            assertTrue { response.content!!.contains("Username: admin") }
            assertTrue { response.content!!.contains("Email: admin@example.com") }
        }
    }
}