<?php

namespace App\Dto;

use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Validator\Constraints\NotBlank;

final class DigitVerificationInput
{
    #[NotBlank]
    #[Groups(['digit:write'])]
    public string $otpDigit;

    #[NotBlank]
    #[Groups(['digit:write'])]
    public int $userID;

    #[NotBlank]
    #[Groups(['digit:write'])]
    public string $userEmail;

}