<?php

namespace App\Entity;

use App\Entity\RootEntity\Auditable;
use App\Repository\RoleRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: RoleRepository::class)]
class Role extends Auditable
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 80)]
    private ?string $code = null;

    #[ORM\Column(length: 80)]
    private ?string $name = null;

    #[ORM\OneToMany(mappedBy: 'role', targetEntity: ApplicationUser::class)]
    private Collection $applicationUsers;

    public function __construct()
    {
        $this->applicationUsers = new ArrayCollection();
    }

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
            $applicationUser->setRole($this);
        }

        return $this;
    }

    public function removeApplicationUser(ApplicationUser $applicationUser): self
    {
        if ($this->applicationUsers->removeElement($applicationUser)) {
            // set the owning side to null (unless already changed)
            if ($applicationUser->getRole() === $this) {
                $applicationUser->setRole(null);
            }
        }

        return $this;
    }
}
