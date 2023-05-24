<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProviderInterface;
use App\Dto\UnreadNotificationOutput;
use App\Repository\NotificationsRepository;
use Lexik\Bundle\JWTAuthenticationBundle\Encoder\JWTEncoderInterface;
use Lexik\Bundle\JWTAuthenticationBundle\Exception\JWTDecodeFailureException;
use Symfony\Component\HttpFoundation\RequestStack;

class UnreadNotificationStateProvider implements ProviderInterface
{
    public function __construct(
        private readonly RequestStack $requestStack,
        private readonly NotificationsRepository $repository,
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

        $bearerHeader = explode("Bearer ", $request->headers->get('Authorization'));

        if(is_array($bearerHeader) && isset($bearerHeader[1])) {
            $token = $bearerHeader[1];
            $userID = $this->JWTEncoder->decode($token)['id'];

            $output = new UnreadNotificationOutput();
            $output->count = $this->repository->count([
                'user' => $userID,
                'seen' => false
            ]);

            return $output;
        }

        return [];
    }
}
