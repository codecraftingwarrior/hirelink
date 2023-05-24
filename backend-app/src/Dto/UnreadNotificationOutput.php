<?php

namespace App\Dto;

use Symfony\Component\Serializer\Annotation\Groups;

class UnreadNotificationOutput
{
    #[Groups(['notification:unread'])]
    public int $count;
}