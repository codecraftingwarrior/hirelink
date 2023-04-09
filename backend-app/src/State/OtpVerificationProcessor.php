<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProcessorInterface;
use ApiPlatform\State\ProviderInterface;
use ApiPlatform\Symfony\Messenger\Processor;
use App\Dto\DigitVerificationInput;
use App\Dto\DigitVerificationOutput;
use App\Entity\ApplicationUser;
use App\Repository\ApplicationUserRepository;
use DateInterval;
use DateTime;
use Symfony\Component\HttpKernel\Exception\BadRequestHttpException;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\HttpKernel\Exception\UnprocessableEntityHttpException;

class OtpVerificationProcessor implements ProcessorInterface
{
    public function __construct(
        private readonly ApplicationUserRepository $userRepository
    )
    {
    }


    public function process(mixed $data, Operation $operation, array $uriVariables = [], array $context = []): DigitVerificationOutput
    {
        /** @var DigitVerificationInput $data */
        /** @var ApplicationUser $user */

        $user = $this->userRepository->findOneBy([
            'email' => $data->userEmail,
            'id' => $data->userID
        ]);

        if (!$user)
            throw new NotFoundHttpException("There is no user with the given ID and Email.");

        if (!$user->getOtpCode())
            throw new BadRequestHttpException("There is no 4-Digit code to verify.");

        if ($user->getOtpCode() !== $data->otpDigit)
            throw new BadRequestHttpException("You have provided the wrong 4-digit code");

        if (new DateTime() < $user->getOtpCodeRequestedAt()->add(new DateInterval('P2D'))) {
            $user->setEnabled(true);
            $user->eraseCredentials();
            $this->userRepository->save($user, true);
            $output = new DigitVerificationOutput();
            $output->status = true;
            return $output;
        }
        $user->eraseCredentials();
        $this->userRepository->save($user, true);
        throw new UnprocessableEntityHttpException("This 4-digit code has expired. Please start the registration process again.");

    }
}
