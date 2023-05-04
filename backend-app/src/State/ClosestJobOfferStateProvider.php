<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProviderInterface;
use App\Repository\JobOfferRepository;
use Doctrine\ORM\Query\QueryException;
use Symfony\Component\HttpFoundation\RequestStack;

class ClosestJobOfferStateProvider implements ProviderInterface
{

    public function __construct(
        private readonly RequestStack       $requestStack,
        private readonly JobOfferRepository $jobOfferRepository
    )
    {
    }

    /**
     * @throws QueryException
     */
    public function provide(Operation $operation, array $uriVariables = [], array $context = []): object|array|null
    {
        $request = $this->requestStack->getCurrentRequest();

        $criteria = $request->query->all();

        dump($request->query->all());

        return $this
            ->jobOfferRepository
            ->filter($request->query->get('page') ?? 1, $criteria);
    }
}
