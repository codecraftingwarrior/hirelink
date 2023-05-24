<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProviderInterface;
use App\Repository\JobApplicationRepository;
use Lexik\Bundle\JWTAuthenticationBundle\Encoder\JWTEncoderInterface;
use Lexik\Bundle\JWTAuthenticationBundle\Exception\JWTDecodeFailureException;
use Lexik\Bundle\JWTAuthenticationBundle\Services\JWTTokenManagerInterface;
use Symfony\Component\HttpFoundation\RequestStack;

class JobApplicationListStateProvider implements ProviderInterface
{

    public function __construct(
        private readonly RequestStack $requestStack,
        private readonly JobApplicationRepository $jobApplicationRepository,
        private readonly JWTEncoderInterface $JWTEncoder
    )
    {
    }

    /**
     * @throws JWTDecodeFailureException
     */
    public function provide(Operation $operation, array $uriVariables = [], array $context = []): object|array|null
    {
       $request = $this->requestStack->getCurrentRequest();

       $page = $request->query->get('page') ?? 1;

        $criteria = $request->query->all();

        $bearerHeader = explode("Bearer ", $request->headers->get('Authorization'));

        if(is_array($bearerHeader) && isset($bearerHeader[1])) {
            $token = $bearerHeader[1];
            $criteria['userID'] = $this->JWTEncoder->decode($token)['id'];
        }

        return $this
            ->jobApplicationRepository
            ->filter($page, $criteria);
    }
}
