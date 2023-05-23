package com.dev.hirelink.dto

import java.io.File


data class DocumentDTO (
    var id: Int? = null,
    var title: String? = null,
    var filePath: String? = null,
    var content: String? = null,
    var file: File? = null,
    var owner: String? = null
)