<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProviderInterface;
use App\Repository\JobOfferRepository;
use Doctrine\ORM\Query\QueryException;
use Lexik\Bundle\JWTAuthenticationBundle\Encoder\JWTEncoderInterface;
use Lexik\Bundle\JWTAuthenticationBundle\Exception\JWTDecodeFailureException;
use Lexik\Bundle\JWTAuthenticationBundle\Services\JWTTokenManagerInterface;
use Symfony\Bundle\SecurityBundle\Security;
use Symfony\Component\HttpFoundation\RequestStack;

class JobOfferListStateProvider implements ProviderInterface
{

    public function __construct(
        private readonly RequestStack       $requestStack,
        private readonly JobOfferRepository $jobOfferRepository,
        private readonly Security $security,
        private readonly JWTEncoderInterface $JWTTokenManager
    )
    {
    }

    /**
     * @throws QueryException
     * @throws JWTDecodeFailureException
     */
    public function provide(Operation $operation, array $uriVariables = [], array $context = []): object|array|null
    {
        $request = $this->requestStack->getCurrentRequest();

        $criteria = $request->query->all();

        $bearerHeader = explode("Bearer ", $request->headers->get('Authorization'));

        if(is_array($bearerHeader) && isset($bearerHeader[1])) {
            $token = $bearerHeader[1];
            $criteria['userID'] = $this->JWTTokenManager->decode($token)['id'];
        }

        return $this
            ->jobOfferRepository
            ->filter($request->query->get('page') ?? 1, $criteria);
    }
}
