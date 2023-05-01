<?php

namespace App\Entity;

use ApiPlatform\Metadata\ApiResource;
use ApiPlatform\Metadata\Get;
use ApiPlatform\Metadata\Post;
use App\Repository\BankInformationRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: BankInformationRepository::class)]
#[ApiResource(
    operations: [
        new Get(),
        new Post()
    ],
    normalizationContext: ['groups' => ['bank-information:read']],
    denormalizationContext: ['groups' => ['bank-information:writable']]
)]
class BankInformation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    #[Groups(['bank-information:read'])]
    private ?int $id = null;

    #[ORM\Column(length: 80)]
    #[Groups(['bank-information:read', 'bank-information:writable'])]
    private ?string $iban = null;

    #[ORM\Column(length: 80)]
    #[Groups(['bank-information:read', 'bank-information:writable'])]
    private ?string $bic = null;

    #[ORM\Column(length: 80, nullable: true)]
    #[Groups(['bank-information:read', 'bank-information:writable'])]
    private ?string $bankName = null;

    #[ORM\ManyToOne(inversedBy: 'bankInformations')]
    #[ORM\JoinColumn(nullable: false)]
    #[Groups(['bank-information:writable'])]
    private ?ApplicationUser $owner = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getIban(): ?string
    {
        return $this->iban;
    }

    public function setIban(string $iban): self
    {
        $this->iban = $iban;

        return $this;
    }

    public function getBic(): ?string
    {
        return $this->bic;
    }

    public function setBic(string $bic): self
    {
        $this->bic = $bic;

        return $this;
    }

    public function getBankName(): ?string
    {
        return $this->bankName;
    }

    public function setBankName(?string $bankName): self
    {
        $this->bankName = $bankName;

        return $this;
    }

    public function getOwner(): ?ApplicationUser
    {
        return $this->owner;
    }

    public function setOwner(?ApplicationUser $owner): self
    {
        $this->owner = $owner;

        return $this;
    }
}
