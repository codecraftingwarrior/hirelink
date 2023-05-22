<?php

namespace App\Entity;

use ApiPlatform\Doctrine\Orm\Filter\SearchFilter;
use ApiPlatform\Metadata\ApiFilter;
use ApiPlatform\Metadata\ApiResource;
use ApiPlatform\Metadata\Get;
use ApiPlatform\Metadata\GetCollection;
use App\Entity\RootEntity\TrackableEntity;
use App\Repository\ProfessionRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: ProfessionRepository::class)]
#[ApiResource(
    operations:[
        new Get(requirements: ['id' => '\d+']),
        new GetCollection(
            paginationEnabled: true,
            paginationItemsPerPage: 10,
            paginationMaximumItemsPerPage: 10
        ),
        new GetCollection(
            uriTemplate: "/professions/all",
            paginationEnabled: false
        )
    ],
    normalizationContext: ['groups' => ['profession:read']]
)]
#[ApiFilter(
    SearchFilter::class,
    properties: [
        'name' => 'partial'
    ]
)]
class Profession extends TrackableEntity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    #[Groups(['profession:read'])]
    private ?int $id = null;

    #[ORM\Column(length: 80)]
    #[Groups(['profession:read'])]
    private ?string $name = null;

    public function getId(): ?int
    {
        return $this->id;
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
