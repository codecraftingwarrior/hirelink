<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProviderInterface;
use App\Repository\JobOfferRepository;

class FindJobOffersByOwnerIdProvider implements ProviderInterface
{
    public function __construct(
        private readonly JobOfferRepository $jobOfferRepository,
    )
    {
    }
    public function provide(Operation $operation, array $uriVariables = [], array $context = []): object|array|null
    {
        $criteria = array('owner' => $uriVariables['id']);
        return $this
            ->jobOfferRepository
            ->findBy($criteria);
    }
}
