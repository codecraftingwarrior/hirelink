<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProcessorInterface;
use App\Entity\ApplicationUser;
use App\Enum\RegistrationState;
use App\Enum\RoleType;
use App\Repository\ApplicationUserRepository;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Component\HttpKernel\Exception\UnprocessableEntityHttpException;
use Symfony\Component\Mailer\Exception\TransportExceptionInterface;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Address;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class RegistrationStateProcessor implements ProcessorInterface
{

    public function __construct(
        private readonly UserPasswordHasherInterface $passwordHasher,
        private readonly ApplicationUserRepository   $applicationUserRepository,
        private readonly MailerInterface             $mailer,
    )
    {
    }

    /**
     * @throws \Exception
     */
    public function process(mixed $data, Operation $operation, array $uriVariables = [], array $context = []): ApplicationUser
    {
        /** @var ApplicationUser $data */
        $hashedPassword = $this
            ->passwordHasher
            ->hashPassword($data, $data->getPlainPassword() ?? DEFAULT_PASSWORD);

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

    /**
     * @throws \Exception
     */
    public function handleEmployerRegistration(ApplicationUser $user): ApplicationUser
    {
        //generate OTP code
        $otp = strval(random_int(1000, 9999));
        $user->setOtpCode($otp);
        $user->setOtpCodeRequestedAt(new \DateTime());

        //sending email with the code
        $email = (new TemplatedEmail())
            ->from(new Address(APP_MAIL, 'HireLink'))
            ->to(new Address($user->getEmail(), $user->getFirstName() . " " . $user->getLastName()))
            ->subject(APP_NAME . " | 4-Digit Activation Code")
            ->htmlTemplate('emails/otp.html.twig')
            ->context([
                'user' => $user,
                'otpCode' => $otp,
            ]);

        /*try {
            $this->mailer->send($email);
        } catch (TransportExceptionInterface $e) {
            throw new UnprocessableEntityHttpException('An error occurred while sending the verification code by email.');
        }*/

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
