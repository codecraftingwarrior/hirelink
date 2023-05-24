<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProcessorInterface;
use App\Dto\DigitVerificationOutput;
use App\Dto\UnreadNotificationOutput;
use App\Repository\NotificationsRepository;
use Lexik\Bundle\JWTAuthenticationBundle\Encoder\JWTEncoderInterface;
use Lexik\Bundle\JWTAuthenticationBundle\Exception\JWTDecodeFailureException;
use Symfony\Component\HttpFoundation\RequestStack;

class NotificationAllReadProcessor implements ProcessorInterface
{

    public function __construct(
        private readonly RequestStack            $requestStack,
        private readonly NotificationsRepository $repository,
        private readonly JWTEncoderInterface     $JWTEncoder
    )
    {
    }

    /**
     * @throws JWTDecodeFailureException
     */
    public function process(mixed $data, Operation $operation, array $uriVariables = [], array $context = []): DigitVerificationOutput
    {

        $request = $this->requestStack->getCurrentRequest();
        $output = new DigitVerificationOutput();
        $output->status = false;

        $bearerHeader = explode("Bearer ", $request->headers->get('Authorization'));

        if (is_array($bearerHeader) && isset($bearerHeader[1])) {
            $token = $bearerHeader[1];
            $userID = $this->JWTEncoder->decode($token)['id'];

            $this->repository->markAllAsRead($userID);

            $output->status = true;

            return $output;
        }

        return $output;
    }
}
