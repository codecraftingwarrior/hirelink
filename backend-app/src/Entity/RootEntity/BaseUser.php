<?php

namespace App\Entity\RootEntity;

use DateTimeInterface;
use Doctrine\DBAL\Types\Types;
use Symfony\Component\Security\Core\User\UserInterface;
use Doctrine\ORM\Mapping as ORM;


#[ORM\MappedSuperclass]
class BaseUser extends Auditable implements UserInterface
{
    #[ORM\Column]
    protected array $roles = [];

    #[ORM\Column(length: 80)]
    protected ?string $email = null;

    #[ORM\Column(length: 255)]
    protected ?string $password = null;

    #[ORM\Column(length: 255)]
    protected ?string $plainPassword = null;

    #[ORM\Column(length: 80)]
    protected ?string $registrationState = null;

    #[ORM\Column(length: 5, nullable: true)]
    protected ?string $otpCode = null;

    #[ORM\Column(type: Types::DATETIME_MUTABLE, nullable: true)]
    protected ?DateTimeInterface $otpCodeRequestedAt = null;

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

    public function isEnabled(): ?bool
    {
        return $this->enabled;
    }

    public function setEnabled(bool $enabled): self
    {
        $this->enabled = $enabled;

        return $this;
    }
}