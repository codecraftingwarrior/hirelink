<?php

namespace App\Enum;

enum RegistrationState
{
    case PENDING;
    case GRANTED;
    case REJECTED;
}
