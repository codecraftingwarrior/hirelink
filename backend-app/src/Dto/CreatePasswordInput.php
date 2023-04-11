<?php

namespace App\Dto;

use Symfony\Component\Serializer\Annotation\Groups;

class CreatePasswordInput
{
    #[Groups(['create-pwd:writable'])]
    public string $nationalUniqueNumber;

    #[Groups(['create-pwd:writable'])]
    public string $email;

    #[Groups(['create-pwd:writable'])]
    public string $plainPassword;
}