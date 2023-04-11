<?php

namespace App\Enum;

enum RoleType
{
    case APPLICANT;
    case EMPLOYER;
    case INTERIM_AGENCY;
    case MANAGER;

    public static function fromCode(string $code): RoleType
    {
        return match(true) {
            $code === APPLICANT_ROLE_CODE => RoleType::APPLICANT,
            $code === INTERIM_AGENCY_ROLE_CODE => RoleType::INTERIM_AGENCY,
            $code === EMPLOYER_ROLE_CODE => RoleType::EMPLOYER,
            $code === MANAGER_ROLE_CODE => RoleType::MANAGER
        };
    }
}
