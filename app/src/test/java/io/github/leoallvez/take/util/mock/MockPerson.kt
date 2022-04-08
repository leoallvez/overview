package io.github.leoallvez.take.util.mock

import com.squareup.moshi.Json

data class MockPerson(
    val id: Int,
    @field:Json(name = "first_name")
    val firstName: String
) {
    override fun toString(): String {
        return "id:$id,first_name:$firstName"
    }
}