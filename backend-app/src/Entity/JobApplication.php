<?php

namespace App\Entity;

use ApiPlatform\Metadata\ApiResource;
use ApiPlatform\Metadata\Get;
use ApiPlatform\Metadata\GetCollection;
use ApiPlatform\Metadata\Post;
use ApiPlatform\Metadata\Put;
use App\Entity\RootEntity\TrackableEntity;
use App\Enum\JobApplicationState;
use App\Repository\JobApplicationRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: JobApplicationRepository::class)]
#[ApiResource(
    operations:[
        new Get(
            normalizationContext: ['groups' => ['job-application:read', 'user:read', 'document:read']]
        ),
        new Post(
            denormalizationContext: ['groups' => ['job-application:writable',
                                                  'document:writable']]
        )
    ],
)]
class JobApplication extends TrackableEntity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 80)]
    private ?string $state = 'PENDING';

    #[ORM\ManyToOne(inversedBy: 'jobApplications')]
    #[ORM\JoinColumn(nullable: false)]
    #[Groups(['job-application:read', 'job-application:writable'])]
    private ?JobOffer $jobOffer = null;

    #[ORM\ManyToOne(inversedBy: 'jobApplications')]
    #[ORM\JoinColumn(nullable: false)]
    #[Groups(['job-application:read', 'job-application:writable', 'job-application:read:applicant'])]
    private ?ApplicationUser $applicant = null;

    #[ORM\ManyToMany(targetEntity: Document::class, inversedBy: 'jobApplications', cascade: ['persist'])]
    #[Groups(['job-application:read', 'job-application:writable'])]
    private Collection $documents;

    public function __construct()
    {
        $this->documents = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getState(): ?string
    {
        return $this->state;
    }

    public function setState(string $state): self
    {
        $this->state = $state;

        return $this;
    }

    public function getJobOffer(): ?JobOffer
    {
        return $this->jobOffer;
    }

    public function setJobOffer(?JobOffer $jobOffer): self
    {
        $this->jobOffer = $jobOffer;

        return $this;
    }

    public function getApplicant(): ?ApplicationUser
    {
        return $this->applicant;
    }

    public function setApplicant(?ApplicationUser $applicant): self
    {
        $this->applicant = $applicant;

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
        }

        return $this;
    }

    public function removeDocument(Document $document): self
    {
        $this->documents->removeElement($document);

        return $this;
    }
}
