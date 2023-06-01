<?php

namespace App\Entity\RootEntity;

use App\Entity\ApplicationUser;
use DateTimeInterface;
use Doctrine\DBAL\Types\Types;
use Lexik\Bundle\JWTAuthenticationBundle\Security\User\JWTUserInterface;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\UserInterface;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Validator\Constraints\Email;
use Symfony\Component\Validator\Constraints\NotBlank;


#[ORM\MappedSuperclass]
class BaseUser extends TrackableEntity implements UserInterface, PasswordAuthenticatedUserInterface, JWTUserInterface
{
    #[ORM\Column]
    //#[Groups('user:read')]
    protected array $roles = [];

    #[ORM\Column(length: 80)]
    #[Groups(['user:read', 'user:writable', 'user:writable:emp'])]
    #[NotBlank]
    #[Email]
    protected ?string $email = null;

    #[ORM\Column(length: 255)]
    protected ?string $password = null;

    #[ORM\Column(length: 255, nullable: true)]
    #[Groups(['user:writable', 'user:writable:emp'])]
    protected ?string $plainPassword = null;

    #[ORM\Column(length: 80)]
    #[Groups(['user:read:registration-state', 'user:write:registration-state'])]
    protected ?string $registrationState = 'PENDING';

    #[ORM\Column(length: 5, nullable: true)]
    #[Groups('user:read:otp')]
    protected ?string $otpCode = null;

    #[ORM\Column(type: Types::DATETIME_MUTABLE, nullable: true)]
    protected ?DateTimeInterface $otpCodeRequestedAt = null;

    #[ORM\Column(type: Types::DATETIME_MUTABLE, nullable: true)]
    protected ?DateTimeInterface $otpCodeVerifiedAt = null;

    #[ORM\Column]
    protected ?bool $enabled = null;


    public function getRoles(): array
    {
        $roles = $this->roles;

        $roles[] = 'ROLE_USER';

        return array_unique($roles);
    }

    public function eraseCredentials()
    {
        $this->otpCode = null;
        $this->otpCodeRequestedAt = null;
        $this->plainPassword = null;
    }

    public function getUserIdentifier(): string
    {
        return $this->email;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): self
    {
        $this->email = $email;

        return $this;
    }

    public function getPassword(): ?string
    {
        return $this->password;
    }

    public function setPassword(string $password): self
    {
        $this->password = $password;

        return $this;
    }

    /**
     * @return string|null
     */
    public function getPlainPassword(): ?string
    {
        return $this->plainPassword;
    }

    /**
     * @param string|null $plainPassword
     */
    public function setPlainPassword(?string $plainPassword): void
    {
        $this->plainPassword = $plainPassword;
    }

    public function getRegistrationState(): ?string
    {
        return $this->registrationState;
    }

    public function setRegistrationState(string $registrationState): self
    {
        $this->registrationState = $registrationState;

        return $this;
    }

    public function getOtpCode(): ?string
    {
        return $this->otpCode;
    }

    public function setOtpCode(?string $otpCode): self
    {
        $this->otpCode = $otpCode;

        return $this;
    }

    public function getOtpCodeRequestedAt(): ?DateTimeInterface
    {
        return $this->otpCodeRequestedAt;
    }

    public function setOtpCodeRequestedAt(?DateTimeInterface $otpCodeRequestedAt): self
    {
        $this->otpCodeRequestedAt = $otpCodeRequestedAt;

        return $this;
    }

    /**
     * @return DateTimeInterface|null
     */
    public function getOtpCodeVerifiedAt(): ?DateTimeInterface
    {
        return $this->otpCodeVerifiedAt;
    }

    public function setOtpCodeVerifiedAt(?DateTimeInterface $otpCodeVerifiedAt): self
    {
        $this->otpCodeVerifiedAt = $otpCodeVerifiedAt;

        return $this;
    }

    public function isEnabled(): ?bool
    {
        return $this->enabled;
    }

    public function setEnabled(bool $enabled): self
    {
        $this->enabled = $enabled;

        return $this;
    }

    public static function createFromPayload($identity, array $payload): ApplicationUser|JWTUserInterface
    {
        return (new ApplicationUser())
            ->setId($identity)
            ->setEmail($payload['username'])
            ->setRoles($payload['roles']);

    }


}