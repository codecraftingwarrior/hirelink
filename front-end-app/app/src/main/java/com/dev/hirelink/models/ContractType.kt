package com.dev.hirelink.models

import com.dev.hirelink.components.Globals.CONTRACT_TYPE_RESOURCE_PREFIX

data class ContractType(
    var id: Int? = null,
    var code: String? = null,
    var name: String? = null
): IRIConvertible {
    override fun toIRI(): String {
        return CONTRACT_TYPE_RESOURCE_PREFIX + id
    }
}