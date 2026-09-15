package br.dev.singular.overview.data.model

data class UserDataModel(
    val id: String,
    val name: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
)
