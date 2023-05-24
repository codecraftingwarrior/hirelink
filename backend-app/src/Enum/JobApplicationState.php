<?php

namespace App\Enum;

enum JobApplicationState
{
    case PENDING;
    case IN_PROGRESS;
    case ACCEPTED;
    case REFUSED;

    public static function fromCode(string $code): JobApplicationState
    {
        return match(true) {
            $code === ACCEPTED_CODE => JobApplicationState::ACCEPTED,
            $code === REFUSED_CODE => JobApplicationState::REFUSED,
            $code === IN_PROGRESS_CODE => JobApplicationState::IN_PROGRESS
        };
    }
}