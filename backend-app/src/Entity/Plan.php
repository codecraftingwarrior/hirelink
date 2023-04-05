<?php

namespace App\Entity;

use App\Entity\RootEntity\Auditable;
use App\Repository\PlanRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: PlanRepository::class)]
class Plan extends Auditable
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 80)]
    private ?string $name = null;

    #[ORM\Column]
    private ?float $price = null;

    #[ORM\Column(type: Types::TEXT)]
    private ?string $conditions = null;

    #[ORM\Column(type: Types::TEXT)]
    private ?string $unsubscriptionConditions = null;

    #[ORM\Column(type: Types::TEXT)]
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
