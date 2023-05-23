<?php

namespace App\Enum;

enum JobApplicationState
{
    case PENDING;
    case IN_PROGRESS;
    case ACCEPTED;
    case REFUSED;
}