<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Bundle\SecurityBundle\Security;
use Symfony\Component\HttpKernel\Attribute\AsController;
use Symfony\Component\Security\Core\User\UserInterface;

#[AsController]
class CurrentUserController extends AbstractController
{
    public function __construct(private readonly Security $security)
    {
    }

    public function __invoke(): ?UserInterface
    {
    return $this->security->getUser();
    }
}