<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProviderInterface;
use Symfony\Bundle\SecurityBundle\Security;
use Symfony\Component\Security\Core\User\UserInterface;

class CurrentUserProvider implements ProviderInterface
{

    public function __construct(private readonly Security $security)
    {
    }

    public function provide(Operation $operation, array $uriVariables = [], array $context = []): ?UserInterface
    {
        return $this->security->getUser();
    }
}
