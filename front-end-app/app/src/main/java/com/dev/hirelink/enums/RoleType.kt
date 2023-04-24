package com.dev.hirelink.enums

enum class RoleType(val code: String, val roleName: String) {
    APPLICANT("APP", "APPLICANT"),
    EMPLOYER("EMP", "EMPLOYER"),
    INTERIM_AGENCY("AGC", "AGENCY"),
    MANAGER("MAN", "MANAGER")
}