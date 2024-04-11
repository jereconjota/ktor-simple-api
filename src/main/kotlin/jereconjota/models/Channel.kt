package jereconjota.models

import kotlinx.serialization.Serializable

@Serializable
class Channel(
    val id: Int,
    val name: String,
    val description: String,
    val url: String) {

    fun copy(name: String, description: String, url: String): Channel {
        return Channel(id, name, description, url)
    }



}