<?php

namespace App\Entity;

use ApiPlatform\Doctrine\Orm\Filter\DateFilter;
use ApiPlatform\Doctrine\Orm\Filter\RangeFilter;
use ApiPlatform\Doctrine\Orm\Filter\SearchFilter;
use ApiPlatform\Metadata\ApiResource;
use ApiPlatform\Metadata\ApiFilter;
use ApiPlatform\Metadata\Get;
use ApiPlatform\Metadata\GetCollection;
use App\Entity\RootEntity\TrackableEntity;
use App\Repository\JobOfferRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: JobOfferRepository::class)]
#[ApiResource(
    operations:[
        new GetCollection(),
        new Get(
            normalizationContext: ['groups' => ['jobOffer:read']]
        )
    ]
),
ApiFilter(
    SearchFilter::class,
    properties: [
        'id' => 'exact',
        'address' => 'partial',
        'profession' => 'partial',
        'owner' => 'partial',
    ]
),
ApiFilter(RangeFilter::class, properties: ['salary']),
ApiFilter(DateFilter::class, properties: ['fromDate', 'toDate'])
]
class JobOffer extends TrackableEntity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    #[Groups(['job-offer:read'])]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Groups(['job-offer:read'])]
    private ?string $title = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Groups(['job-offer:read'])]
    private ?string $description = null;

    #[ORM\Column]
    #[Groups(['job-offer:read'])]
    private ?float $salary = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Groups(['job-offer:read'])]
    private ?\DateTimeInterface $fromDate = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Groups(['job-offer:read'])]
    private ?\DateTimeInterface $toDate = null;

    #[ORM\Column(length: 255, nullable: true)]
    #[Groups(['job-offer:read'])]
    private ?string $address = null;

    #[ORM\Column(nullable: true)]
    #[Groups(['job-offer:read'])]
    private ?float $lat = null;

    #[ORM\Column(nullable: true)]
    #[Groups(['job-offer:read'])]
    private ?float $lng = null;

    #[ORM\ManyToOne(inversedBy: 'jobOffers')]
    #[Groups(['job-offer:read'])]
    private ?JobOfferCategory $category = null;

    #[ORM\ManyToOne]
    #[ORM\JoinColumn(nullable: false)]
    #[Groups(['job-offer:read'])]
    private ?Profession $profession = null;

    #[ORM\ManyToOne]
    #[ORM\JoinColumn(nullable: false)]
    #[Groups(['job-offer:read'])]
    private ?ContractType $contractType = null;

    #[ORM\OneToMany(mappedBy: 'job-offer', targetEntity: JobApplication::class)]
    private Collection $jobApplications;

    #[ORM\ManyToMany(targetEntity: Tag::class, inversedBy: 'jobOffers')]
    #[Groups(['job-offer:read'])]
    private Collection $tags;

    #[ORM\ManyToOne(inversedBy: 'jobOffers')]
    #[ORM\JoinColumn(nullable: false)]
    #[Groups(['job-offer:read'])]
    private ?ApplicationUser $owner = null;

    public function __construct()
    {
        $this->jobApplications = new ArrayCollection();
        $this->tags = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getTitle(): ?string
    {
        return $this->title;
    }

    public function setTitle(string $title): self
    {
        $this->title = $title;

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

    public function getSalary(): ?float
    {
        return $this->salary;
    }

    public function setSalary(float $salary): self
    {
        $this->salary = $salary;

        return $this;
    }

    public function getFromDate(): ?\DateTimeInterface
    {
        return $this->fromDate;
    }

    public function setFromDate(\DateTimeInterface $fromDate): self
    {
        $this->fromDate = $fromDate;

        return $this;
    }

    public function getToDate(): ?\DateTimeInterface
    {
        return $this->toDate;
    }

    public function setToDate(\DateTimeInterface $toDate): self
    {
        $this->toDate = $toDate;

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

    public function getLat(): ?float
    {
        return $this->lat;
    }

    public function setLat(?float $lat): self
    {
        $this->lat = $lat;

        return $this;
    }

    public function getLng(): ?float
    {
        return $this->lng;
    }

    public function setLng(?float $lng): self
    {
        $this->lng = $lng;

        return $this;
    }

    public function getCategory(): ?JobOfferCategory
    {
        return $this->category;
    }

    public function setCategory(?JobOfferCategory $category): self
    {
        $this->category = $category;

        return $this;
    }

    public function getProfession(): ?Profession
    {
        return $this->profession;
    }

    public function setProfession(?Profession $profession): self
    {
        $this->profession = $profession;

        return $this;
    }

    public function getContractType(): ?ContractType
    {
        return $this->contractType;
    }

    public function setContractType(?ContractType $contractType): self
    {
        $this->contractType = $contractType;

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
            $jobApplication->setJobOffer($this);
        }

        return $this;
    }

    public function removeJobApplication(JobApplication $jobApplication): self
    {
        if ($this->jobApplications->removeElement($jobApplication)) {
            // set the owning side to null (unless already changed)
            if ($jobApplication->getJobOffer() === $this) {
                $jobApplication->setJobOffer(null);
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
        }

        return $this;
    }

    public function removeTag(Tag $tag): self
    {
        $this->tags->removeElement($tag);

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
