package com.dev.hirelink.enums

enum class RegistrationStep(val number: Int) {
    STEP_1(1),
    STEP_2(2),
    STEP_3(3),
    STEP_4(4),
    STEP_5(5),
    STEP_6_DIRECT_DEBIT(6),
    STEP_6_CREDIT_CARD(6),
    STEP_END_CANDIDATE(7),
}