<?php

namespace App\Entity;

use App\Entity\RootEntity\TrackableEntity;
use App\Repository\ReportRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ReportRepository::class)]
class Report extends TrackableEntity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\ManyToOne]
    #[ORM\JoinColumn(nullable: false)]
    private ?ApplicationUser $author = null;

    #[ORM\ManyToOne]
    #[ORM\JoinColumn(nullable: false)]
    private ?ApplicationUser $reportedUser = null;

    #[ORM\Column(length: 255)]
    private ?string $reason = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getAuthor(): ?ApplicationUser
    {
        return $this->author;
    }

    public function setAuthor(?ApplicationUser $author): self
    {
        $this->author = $author;

        return $this;
    }

    public function getReportedUser(): ?ApplicationUser
    {
        return $this->reportedUser;
    }

    public function setReportedUser(?ApplicationUser $reportedUser): self
    {
        $this->reportedUser = $reportedUser;

        return $this;
    }

    public function getReason(): ?string
    {
        return $this->reason;
    }

    public function setReason(string $reason): self
    {
        $this->reason = $reason;

        return $this;
    }
}
