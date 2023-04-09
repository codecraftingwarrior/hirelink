<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProcessorInterface;
use App\Entity\ApplicationUser;
use App\Enum\RegistrationState;
use App\Enum\RoleType;
use App\Repository\ApplicationUserRepository;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class RegistrationStateProcessor implements ProcessorInterface
{

    public function __construct(
        private readonly UserPasswordHasherInterface $passwordHasher,
        private readonly ApplicationUserRepository $applicationUserRepository,
    )
    {
    }

    public function process(mixed $data, Operation $operation, array $uriVariables = [], array $context = []): ApplicationUser
    {
        /** @var ApplicationUser $data */
        $hashedPassword = $this
            ->passwordHasher
            ->hashPassword($data, $data->getPlainPassword());

        $data->setPassword($hashedPassword);
        $data->setRegistrationState(RegistrationState::PENDING->name);
        $data->setEnabled(false);

        $appRole = RoleType::fromCode($data->getRole()->getCode());

        return match ($appRole) {
            RoleType::APPLICANT => $this->handleApplicantRegistration($data),
            RoleType::EMPLOYER => $this->handleEmployerRegistration($data),
            RoleType::INTERIM_AGENCY => $this->handleInterimAgencyRegistration($data),
            RoleType::MANAGER => $this->handleManagerRegistration($data)
        };

    }


    private function handleApplicantRegistration(ApplicationUser $user): ApplicationUser
    {
        //TODO: send welcome email

        $user->eraseCredentials();

        $this->applicationUserRepository->save($user, true);

        return $user;
    }

    public function handleEmployerRegistration(ApplicationUser $user): ApplicationUser
    {
        $this->applicationUserRepository->save($user, true);

        return $user;
    }

    public function handleInterimAgencyRegistration(ApplicationUser $user): ApplicationUser
    {
        $this->applicationUserRepository->save($user, true);

       return $user;
    }

    public function handleManagerRegistration(ApplicationUser $user): ApplicationUser
    {
        $this->applicationUserRepository->save($user, true);

        return $user;
    }
}
