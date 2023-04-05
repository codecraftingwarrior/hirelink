<?php

namespace App\Entity;

use App\Entity\RootEntity\BaseUser;
use App\Repository\ApplicationUserRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;


#[ORM\Entity(repositoryClass: ApplicationUserRepository::class)]
class ApplicationUser extends BaseUser
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $firstName = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $lastName = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $nationality = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    private ?\DateTimeInterface $birthDate = null;

    #[ORM\Column(length: 80)]
    private ?string $phoneNumber = null;


    #[ORM\Column(length: 255, nullable: true)]
    private ?string $address = null;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $picUrl = null;

    #[ORM\OneToMany(mappedBy: 'owner', targetEntity: Document::class)]
    private Collection $documents;

    #[ORM\ManyToOne(inversedBy: 'applicationUsers')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Role $role = null;

    #[ORM\ManyToOne(inversedBy: 'applicationUsers')]
    private ?Plan $plan = null;

    #[ORM\ManyToOne(inversedBy: 'applicationUsers')]
    private ?Company $company = null;

    #[ORM\ManyToMany(targetEntity: self::class, inversedBy: 'applicationUsers')]
    private Collection $blockedUsers;

    #[ORM\OneToMany(mappedBy: 'owner', targetEntity: ShareGroup::class)]
    private Collection $createdShareGroups;

    #[ORM\ManyToMany(targetEntity: ShareGroup::class, mappedBy: 'members')]
    private Collection $belongedShareGroups;

    #[ORM\OneToMany(mappedBy: 'applicant', targetEntity: JobApplication::class)]
    private Collection $jobApplications;

    #[ORM\OneToMany(mappedBy: 'owner', targetEntity: JobOffer::class)]
    private Collection $jobOffers;

    #[ORM\OneToMany(mappedBy: 'owner', targetEntity: Tag::class)]
    private Collection $tags;

    public function __construct()
    {
        $this->documents = new ArrayCollection();
        $this->blockedUsers = new ArrayCollection();
        $this->createdShareGroups = new ArrayCollection();
        $this->belongedShareGroups = new ArrayCollection();
        $this->jobApplications = new ArrayCollection();
        $this->jobOffers = new ArrayCollection();
        $this->tags = new ArrayCollection();
    }


    public function getId(): ?int
    {
        return $this->id;
    }

    public function getFirstName(): ?string
    {
        return $this->firstName;
    }

    public function setFirstName(?string $firstName): self
    {
        $this->firstName = $firstName;

        return $this;
    }

    public function getLastName(): ?string
    {
        return $this->lastName;
    }

    public function setLastName(?string $lastName): self
    {
        $this->lastName = $lastName;

        return $this;
    }

    public function getNationality(): ?string
    {
        return $this->nationality;
    }

    public function setNationality(?string $nationality): self
    {
        $this->nationality = $nationality;

        return $this;
    }

    public function getBirthDate(): ?\DateTimeInterface
    {
        return $this->birthDate;
    }

    public function setBirthDate(\DateTimeInterface $birthDate): self
    {
        $this->birthDate = $birthDate;

        return $this;
    }

    public function getPhoneNumber(): ?string
    {
        return $this->phoneNumber;
    }

    public function setPhoneNumber(string $phoneNumber): self
    {
        $this->phoneNumber = $phoneNumber;

        return $this;
    }

    public function getAddress(): ?string
    {
        return $this->address;
    }

    public function setAddress(?string $address): self
    {
        $this->address = $address;

        return $this;
    }

    public function getPicUrl(): ?string
    {
        return $this->picUrl;
    }

    public function setPicUrl(?string $picUrl): self
    {
        $this->picUrl = $picUrl;

        return $this;
    }

    public function getRoles(): array
    {
        return $this->roles;
    }

    public function setRoles(array $roles): self
    {
        $this->roles = $roles;

        return $this;
    }

    /**
     * @return Collection<int, Document>
     */
    public function getDocuments(): Collection
    {
        return $this->documents;
    }

    public function addDocument(Document $document): self
    {
        if (!$this->documents->contains($document)) {
            $this->documents->add($document);
            $document->setOwner($this);
        }

        return $this;
    }

    public function removeDocument(Document $document): self
    {
        if ($this->documents->removeElement($document)) {
            // set the owning side to null (unless already changed)
            if ($document->getOwner() === $this) {
                $document->setOwner(null);
            }
        }

        return $this;
    }

    public function getRole(): ?Role
    {
        return $this->role;
    }

    public function setRole(?Role $role): self
    {
        $this->role = $role;

        return $this;
    }

    public function getPlan(): ?Plan
    {
        return $this->plan;
    }

    public function setPlan(?Plan $plan): self
    {
        $this->plan = $plan;

        return $this;
    }

    public function getCompany(): ?Company
    {
        return $this->company;
    }

    public function setCompany(?Company $company): self
    {
        $this->company = $company;

        return $this;
    }

    /**
     * @return Collection<int, self>
     */
    public function getBlockedUsers(): Collection
    {
        return $this->blockedUsers;
    }

    public function addBlockedUser(self $blockedUser): self
    {
        if (!$this->blockedUsers->contains($blockedUser)) {
            $this->blockedUsers->add($blockedUser);
        }

        return $this;
    }

    public function removeBlockedUser(self $blockedUser): self
    {
        $this->blockedUsers->removeElement($blockedUser);

        return $this;
    }

    /**
     * @return Collection<int, ShareGroup>
     */
    public function getCreatedShareGroups(): Collection
    {
        return $this->createdShareGroups;
    }

    public function addShareGroupe(ShareGroup $shareGroupe): self
    {
        if (!$this->createdShareGroups->contains($shareGroupe)) {
            $this->createdShareGroups->add($shareGroupe);
            $shareGroupe->setOwner($this);
        }

        return $this;
    }

    public function removeShareGroupe(ShareGroup $shareGroupe): self
    {
        if ($this->createdShareGroups->removeElement($shareGroupe)) {
            // set the owning side to null (unless already changed)
            if ($shareGroupe->getOwner() === $this) {
                $shareGroupe->setOwner(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, ShareGroup>
     */
    public function getBelongedShareGroups(): Collection
    {
        return $this->belongedShareGroups;
    }

    public function addBelongedShareGroupe(ShareGroup $belongedShareGroupe): self
    {
        if (!$this->belongedShareGroups->contains($belongedShareGroupe)) {
            $this->belongedShareGroups->add($belongedShareGroupe);
            $belongedShareGroupe->addMember($this);
        }

        return $this;
    }

    public function removeBelongedShareGroupe(ShareGroup $belongedShareGroupe): self
    {
        if ($this->belongedShareGroups->removeElement($belongedShareGroupe)) {
            $belongedShareGroupe->removeMember($this);
        }

        return $this;
    }

    /**
     * @return Collection<int, JobApplication>
     */
    public function getJobApplications(): Collection
    {
        return $this->jobApplications;
    }

    public function addJobApplication(JobApplication $jobApplication): self
    {
        if (!$this->jobApplications->contains($jobApplication)) {
            $this->jobApplications->add($jobApplication);
            $jobApplication->setApplicant($this);
        }

        return $this;
    }

    public function removeJobApplication(JobApplication $jobApplication): self
    {
        if ($this->jobApplications->removeElement($jobApplication)) {
            // set the owning side to null (unless already changed)
            if ($jobApplication->getApplicant() === $this) {
                $jobApplication->setApplicant(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, JobOffer>
     */
    public function getJobOffers(): Collection
    {
        return $this->jobOffers;
    }

    public function addJobOffer(JobOffer $jobOffer): self
    {
        if (!$this->jobOffers->contains($jobOffer)) {
            $this->jobOffers->add($jobOffer);
            $jobOffer->setOwner($this);
        }

        return $this;
    }

    public function removeJobOffer(JobOffer $jobOffer): self
    {
        if ($this->jobOffers->removeElement($jobOffer)) {
            // set the owning side to null (unless already changed)
            if ($jobOffer->getOwner() === $this) {
                $jobOffer->setOwner(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, Tag>
     */
    public function getTags(): Collection
    {
        return $this->tags;
    }

    public function addTag(Tag $tag): self
    {
        if (!$this->tags->contains($tag)) {
            $this->tags->add($tag);
            $tag->setOwner($this);
        }

        return $this;
    }

    public function removeTag(Tag $tag): self
    {
        if ($this->tags->removeElement($tag)) {
            // set the owning side to null (unless already changed)
            if ($tag->getOwner() === $this) {
                $tag->setOwner(null);
            }
        }

        return $this;
    }
}
