<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProviderInterface;
use App\Repository\ApplicationUserRepository;
use Symfony\Bundle\SecurityBundle\Security;
use Symfony\Component\Security\Core\User\UserInterface;

class CurrentUserProvider implements ProviderInterface
{

    public function __construct(
        private readonly Security                  $security,
        private readonly ApplicationUserRepository $userRepository,
    )
    {
    }

    public function provide(Operation $operation, array $uriVariables = [], array $context = []): ?UserInterface
    {
        return $this
            ->userRepository
            ->findOneByEmail($this
                ->security
                ->getUser()
                ->getUserIdentifier()
            );
    }
}
