package com.dev.hirelink.models

data class Document(
    var id: Int? = null,
    var title: String? = null,
    var filePath: String? = null,
    var content: String? = null,
    var owner: ApplicationUser? = null
)