<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProcessorInterface;
use App\Dto\CreatePasswordInput;
use App\Entity\ApplicationUser;
use App\Enum\RoleType;
use App\Repository\ApplicationUserRepository;
use Lexik\Bundle\JWTAuthenticationBundle\Security\Http\Authentication\AuthenticationSuccessHandler;
use Lexik\Bundle\JWTAuthenticationBundle\Services\JWTTokenManagerInterface;
use Psr\Container\ContainerInterface;
use Symfony\Component\Finder\Exception\AccessDeniedException;
use Symfony\Component\HttpKernel\Exception\UnprocessableEntityHttpException;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class CreatePasswordStateProcessor implements ProcessorInterface
{

    public function __construct(
        private readonly UserPasswordHasherInterface $passwordHasher,
        private readonly ApplicationUserRepository   $userRepository,
        private readonly JWTTokenManagerInterface $JWTTokenManager,
        private readonly AuthenticationSuccessHandler $authenticationSuccessHandler,
    )
    {
    }

    public function process(mixed $data, Operation $operation, array $uriVariables = [], array $context = []): ApplicationUser
    {
        $user = $this->userRepository->find($uriVariables['id']);

        /** @var CreatePasswordInput $data */
        if (!in_array($user->getRole()->getCode(), [EMPLOYER_ROLE_CODE, INTERIM_AGENCY_ROLE_CODE]))
            throw new AccessDeniedException("You are not allowed to create a new password.");

        if ($user->getCompany()->getNationalUniqueNumber() !== $data->nationalUniqueNumber)
            throw new UnprocessableEntityHttpException("You provided the wrong national unique number.");

        if ($user->getEmail() !== $data->email)
            throw new UnprocessableEntityHttpException("You provided the wrong email address.");

        if (!$user->getOtpCodeVerifiedAt())
            throw new UnprocessableEntityHttpException("Your must verify your OTP code before creating a password.");

        if (!$data->plainPassword)
            throw new UnprocessableEntityHttpException("You must provide a password to proceed.");

        $hashedPassword = $this->passwordHasher->hashPassword(
            $user,
            $data->plainPassword
        );

        $user->setPassword($hashedPassword);

        $this->userRepository->save($user, true);

        //Generating JWT token and authenticating the user
        $jwtToken = $this->JWTTokenManager->create($user);

        $user->setToken($jwtToken);

        $this->authenticationSuccessHandler->handleAuthenticationSuccess($user, $jwtToken);

        return $user;
    }
}
