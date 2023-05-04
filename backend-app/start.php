<?php
const APPLICANT_ROLE_CODE = 'APP';
const INTERIM_AGENCY_ROLE_CODE = 'AGC';
const EMPLOYER_ROLE_CODE = 'EMP';
const MANAGER_ROLE_CODE = 'MAN';
const DEFAULT_PASSWORD = 'welcome';

const CDI_CONTRACT_CODE = 'CDI';
const CDD_CONTRACT_CODE = 'CDD';
const ALTERNATION_CONTRACT_CODE = 'ALT';

const INTERNSHIP_CONTRACT_CODE = 'INTERN';

const APP_MAIL = 'noreply@hirelink.fr';
const APP_NAME = 'HireLink';

const JOB_OFFER_ITEM_PER_PAGE = 5;

function generateNumericOTP($n)
{
    $generator = "1357902468";

    $result = "";

    for ($i = 1; $i <= $n; $i++) {
        $result .= substr($generator, rand() % strlen($generator), 1);
    }

    return $result;
}
