package dev.com.singular.overview.util.mock

import com.squareup.moshi.Json

data class MockPerson(
    val id: Int,
    @field:Json(name = "first_name")
    val firstName: String
) {
    override fun toString(): String {
        return "id:$id,first_name:$firstName"
    }

    override fun equals(other: Any?): Boolean {
        return if (other is MockPerson) {
            other.id == id && other.firstName == firstName
        } else {
            false
        }
    }
}
