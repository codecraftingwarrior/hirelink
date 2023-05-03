package com.dev.hirelink.models

import com.google.gson.annotations.SerializedName

data class PaginatedResourceWrapper<T>(
    @SerializedName("@context") var context: String? = null,
    @SerializedName("@id") val id: String? = null,
    @SerializedName("@type") val type: String? = null,
    @SerializedName("hydra:totalItems") val totalItems: Int? = null,
    @SerializedName("hydra:member") val items: MutableList<T?>? = null,
    @SerializedName("hydra:view") val paginationView: PaginationView? = null
)

data class PaginationView(
    @SerializedName("@id") val id: String? = null,
    @SerializedName("@type") val type: String? = null,
    @SerializedName("hydra:first") val firstItemLink: String? = null,
    @SerializedName("hydra:last") val lastItemLink: String? = null,
    @SerializedName("hydra:next") val nextItemLink: String? = null,
    @SerializedName("hydra:previous") val previousItemLink: String? = null
)