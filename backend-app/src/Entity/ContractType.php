<?php

namespace App\Entity;

use ApiPlatform\Metadata\ApiResource;
use ApiPlatform\Metadata\Get;
use ApiPlatform\Metadata\GetCollection;
use App\Repository\ContractTypeRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: ContractTypeRepository::class)]
#[ApiResource(
    operations:[
        new Get(),
        new GetCollection(
            paginationEnabled: false
        )
    ],
    normalizationContext: ['groups' => ['contract-type:read']]
)]

class ContractType
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    #[Groups(['contract-type:read'])]
    private ?int $id = null;

    #[ORM\Column(length: 80)]
    #[Groups(['contract-type:read'])]
    private ?string $code = null;

    #[ORM\Column(length: 80)]
    #[Groups(['contract-type:read'])]
    private ?string $name = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getCode(): ?string
    {
        return $this->code;
    }

    public function setCode(string $code): self
    {
        $this->code = $code;

        return $this;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(string $name): self
    {
        $this->name = $name;

        return $this;
    }
}
