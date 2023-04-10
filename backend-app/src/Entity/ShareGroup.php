<?php

namespace App\Entity;

use App\Entity\RootEntity\TrackableEntity;
use App\Repository\ShareGroupeRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ShareGroupeRepository::class)]
class ShareGroup extends TrackableEntity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    private ?string $name = null;

    #[ORM\ManyToOne(inversedBy: 'shareGroupes')]
    #[ORM\JoinColumn(nullable: false)]
    private ?ApplicationUser $owner = null;

    #[ORM\ManyToMany(targetEntity: ApplicationUser::class, inversedBy: 'belongedShareGroupes')]
    private Collection $members;

    public function __construct()
    {
        $this->members = new ArrayCollection();
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

    public function getOwner(): ?ApplicationUser
    {
        return $this->owner;
    }

    public function setOwner(?ApplicationUser $owner): self
    {
        $this->owner = $owner;

        return $this;
    }

    /**
     * @return Collection<int, ApplicationUser>
     */
    public function getMembers(): Collection
    {
        return $this->members;
    }

    public function addMember(ApplicationUser $member): self
    {
        if (!$this->members->contains($member)) {
            $this->members->add($member);
        }

        return $this;
    }

    public function removeMember(ApplicationUser $member): self
    {
        $this->members->removeElement($member);

        return $this;
    }
}
