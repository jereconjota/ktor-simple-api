package jereconjota.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jereconjota.models.Channel

private val channels = mutableListOf<Channel>(
    Channel(1, "Ktor", "Ktor is a framework for building asynchronous servers and clients in connected systems using the powerful Kotlin programming language.", "https://ktor.io"),
    Channel(2, "Kotlin", "Kotlin is a cross-platform, statically typed, general-purpose programming language with type inference.", "https://kotlinlang.org"),
    Channel(3, "Android", "Android is an open-source operating system for mobile devices created by Google.", "https://developer.android.com"),
    Channel(4, "iOS", "iOS is a mobile operating system created and developed by Apple Inc.", "https://developer.apple.com/ios"),
    Channel(5, "Flutter", "Flutter is Google's UI toolkit for building natively compiled applications for mobile, web, and desktop from a single codebase.", "https://flutter.dev"),
    Channel(6, "React Native", "React Native is an open-source mobile application framework created by Facebook, Inc.", "https://reactnative.dev"),
    Channel(7, "Angular", "Angular is a platform and framework for building single-page client applications using HTML and TypeScript.", "https://angular.io"),
    Channel(8, "Vue.js", "Vue.js is an open-source model–view–viewmodel front end JavaScript framework for building user interfaces and single-page applications.", "https://vuejs.org"),
    Channel(9, "React", "React is an open-source, front end, JavaScript library for building user interfaces or UI components.", "https://reactjs.org"),
    Channel(10, "Node.js", "Node.js is an open-source, cross-platform, back-end JavaScript runtime environment that runs on the V8 engine and executes JavaScript code outside a web browser.", "https://nodejs.org"),
    Channel(11, "Express", "Express.js, or simply Express, is a back end web application framework for Node.js, released as free and open-source software under the MIT License.", "https://expressjs.com"),
    Channel(12, "Spring Boot", "Spring Boot makes it easy to create stand-alone, production-grade Spring-based Applications that you can just run.", "https://spring.io/projects/spring-boot"),
    Channel(13, "Micronaut", "Micronaut is a modern, JVM-based, full-stack framework for building modular, easily testable microservice and serverless applications.", "https://micronaut.io"),
)

fun Route.channelRouting() {
    route("/channels") {
        get {
            if (channels.isNotEmpty()) {
                call.respond(channels)
            } else {
                call.respondText("No channels found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"]?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val channel = channels.find { it.id == id.toInt() } ?: return@get call.respondText(
                "No channel with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(channel)
        }

        put("{id}") {
            val id = call.parameters["id"]?: return@put call.respond(HttpStatusCode.BadRequest)
            val channelIndex = channels.indexOfFirst { it.id == id.toInt() }
            if (channelIndex == -1) {
                return@put call.respond(HttpStatusCode.NotFound)
            }
            val channel = call.receive<Channel>()
            channels[channelIndex] = channel
            call.respondText("Channel updated correctly", status = HttpStatusCode.OK)
        }

        patch("{id}") {
            val id = call.parameters["id"]?: return@patch call.respond(HttpStatusCode.BadRequest)
            val channelIndex = channels.indexOfFirst { it.id == id.toInt() }
            if (channelIndex == -1) {
                return@patch call.respond(HttpStatusCode.NotFound)
            }
            val channel = call.receive<Channel>()
            val currentChannel = channels[channelIndex]
            val newChannel: Channel = currentChannel.copy(
                name = channel.name,
                description = channel.description,
                url = channel.url
            )
            channels[channelIndex] = newChannel
            call.respond(newChannel)
        }

        post {
            try {
                val channel = call.receive<Channel>()
                channels.add(channel)
                call.respondText("Channel stored correctly", status = HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respondText(e.toString(), status = HttpStatusCode.Conflict)
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (channels.removeIf { it.id == id.toInt() }) {
                call.respondText("Channel removed correctly", status = HttpStatusCode.OK)
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}