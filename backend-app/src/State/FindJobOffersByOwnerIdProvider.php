<?php

namespace App\State;

use ApiPlatform\Doctrine\Orm\Paginator;
use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProviderInterface;
use App\Repository\JobOfferRepository;
use Doctrine\Common\Collections\Criteria;
use Doctrine\ORM\Query\QueryException;
use Doctrine\ORM\Tools\Pagination\Paginator as DoctrinePaginator;
use Symfony\Component\HttpFoundation\RequestStack;

class FindJobOffersByOwnerIdProvider implements ProviderInterface
{
    public function __construct(
        private readonly JobOfferRepository $jobOfferRepository,
        private readonly RequestStack       $requestStack
    )
    {
    }

    /**
     * @throws QueryException
     */
    public function provide(Operation $operation, array $uriVariables = [], array $context = []): object|array|null
    {
        $request = $this->requestStack->getCurrentRequest();
        $page = $request->query->get('page');

        $criteria = [
            'ownerID' => $uriVariables['id'],
            'searchQuery' => $request->query->get('searchQuery')
        ];

        return $this->jobOfferRepository->findByOwnerPaginated($page, $criteria);
    }
}
