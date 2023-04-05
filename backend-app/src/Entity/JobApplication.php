<?php

namespace App\Entity;

use App\Entity\RootEntity\Auditable;
use App\Repository\JobApplicationRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: JobApplicationRepository::class)]
class JobApplication extends Auditable
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 80)]
    private ?string $state = null;

    #[ORM\ManyToOne(inversedBy: 'jobApplications')]
    #[ORM\JoinColumn(nullable: false)]
    private ?JobOffer $jobOffer = null;

    #[ORM\ManyToOne(inversedBy: 'jobApplications')]
    #[ORM\JoinColumn(nullable: false)]
    private ?ApplicationUser $applicant = null;

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
}
