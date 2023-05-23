package com.dev.hirelink.models

import com.dev.hirelink.components.Globals

data class Document(
    var id: Int? = null,
    var title: String? = null,
    var filePath: String? = null,
    var fileName: String? = null,
    var content: String? = null,
    var owner: ApplicationUser? = null
) {
    fun getFullPath() = Globals.BACKEND_DOMAIN + filePath
}