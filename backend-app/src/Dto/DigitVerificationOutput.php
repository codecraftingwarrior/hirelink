<?php

namespace App\Dto;

use Symfony\Component\Serializer\Annotation\Groups;

final class DigitVerificationOutput
{
    #[Groups(['digit:read'])]
    public bool $status;
}