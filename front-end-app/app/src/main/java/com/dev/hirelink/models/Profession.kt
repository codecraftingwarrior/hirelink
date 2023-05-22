package com.dev.hirelink.models

import com.dev.hirelink.components.Globals.PROFESSION_RESOURCE_PREFIX

data class Profession(
    val id: Int? = null,
    val name: String? = null
): IRIConvertible {
    override fun toIRI(): String {
        return PROFESSION_RESOURCE_PREFIX +id
    }
}