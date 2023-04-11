<?php

namespace App\Entity;

use ApiPlatform\Metadata\ApiResource;
use ApiPlatform\Metadata\Get;
use ApiPlatform\Metadata\GetCollection;
use App\Entity\RootEntity\TrackableEntity;
use App\Repository\PlanRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: PlanRepository::class)]
#[ApiResource(
    operations: [
        new Get(),
        new GetCollection()
    ],
    normalizationContext: ['groups' => ['plan:read']],
    paginationEnabled: false
)]
class Plan extends TrackableEntity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    #[Groups(['plan:read'])]
    private ?int $id = null;

    #[ORM\Column(length: 80)]
    #[Groups(['plan:read'])]
    private ?string $name = null;

    #[ORM\Column]
    #[Groups(['plan:read'])]
    private ?float $price = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Groups(['plan:read'])]
    private ?string $conditions = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Groups(['plan:read'])]
    private ?string $unsubscriptionConditions = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Groups(['plan:read'])]
    private ?string $description = null;

    #[ORM\OneToMany(mappedBy: 'plan', targetEntity: ApplicationUser::class)]
    private Collection $applicationUsers;

    public function __construct()
    {
        $this->applicationUsers = new ArrayCollection();
    }

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

    public function getPrice(): ?float
    {
        return $this->price;
    }

    public function setPrice(float $price): self
    {
        $this->price = $price;

        return $this;
    }

    public function getConditions(): ?string
    {
        return $this->conditions;
    }

    public function setConditions(string $conditions): self
    {
        $this->conditions = $conditions;

        return $this;
    }

    public function getUnsubscriptionConditions(): ?string
    {
        return $this->unsubscriptionConditions;
    }

    public function setUnsubscriptionConditions(string $unsubscriptionConditions): self
    {
        $this->unsubscriptionConditions = $unsubscriptionConditions;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): self
    {
        $this->description = $description;

        return $this;
    }

    /**
     * @return Collection<int, ApplicationUser>
     */
    public function getApplicationUsers(): Collection
    {
        return $this->applicationUsers;
    }

    public function addApplicationUser(ApplicationUser $applicationUser): self
    {
        if (!$this->applicationUsers->contains($applicationUser)) {
            $this->applicationUsers->add($applicationUser);
            $applicationUser->setPlan($this);
        }

        return $this;
    }

    public function removeApplicationUser(ApplicationUser $applicationUser): self
    {
        if ($this->applicationUsers->removeElement($applicationUser)) {
            // set the owning side to null (unless already changed)
            if ($applicationUser->getPlan() === $this) {
                $applicationUser->setPlan(null);
            }
        }

        return $this;
    }
}
