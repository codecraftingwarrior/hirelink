<?php

namespace App\Entity;

use ApiPlatform\Doctrine\Orm\Filter\SearchFilter;
use ApiPlatform\Metadata\ApiFilter;
use ApiPlatform\Metadata\ApiResource;
use ApiPlatform\Metadata\Get;
use ApiPlatform\Metadata\GetCollection;
use ApiPlatform\Metadata\Put;
use App\Dto\DigitVerificationOutput;
use App\Dto\UnreadNotificationOutput;
use App\Repository\NotificationsRepository;
use App\State\NotificationAllReadProcessor;
use App\State\UnreadNotificationStateProvider;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: NotificationsRepository::class)]
#[ApiResource(
    operations: [
        new GetCollection(
            paginationEnabled: true,
            paginationItemsPerPage: 5,
            paginationMaximumItemsPerPage: 5
        ),
        new Get(
            uriTemplate: 'notifications/unread',
            openapiContext: [
                'summary' => 'Retrieves the unread notifications for the logged in user.',
            ],
            normalizationContext: ['groups' => 'notification:unread'],
            output: UnreadNotificationOutput::class,
            provider: UnreadNotificationStateProvider::class
        ),
        new Put(
            uriTemplate: 'notifications/mark-all-read',
            openapiContext: [
                'summary' => 'Mark the unread notifications for the logged in user as read.',
            ],
            normalizationContext: ['groups' => 'digit:read'],
            output: DigitVerificationOutput::class,
            processor: NotificationAllReadProcessor::class
        )
    ],
    normalizationContext: ['groups' => ['notifications:read',
        'job-application:read:jobOffer',
        'job-offer:read:title',
        'job-offer:read:owner',
        'job-application:update-state',
        'company:read:name'
    ]]
),
    ApiFilter(
        SearchFilter::class,
        properties: [
            'user' => 'exact',
            'seen' => 'exact'
        ])
]
class Notifications
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    #[Groups(['notifications:read'])]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Groups(['notifications:read'])]
    private ?string $title = null;

    #[ORM\ManyToOne(inversedBy: 'notifications')]
    #[ORM\JoinColumn(nullable: false)]
    private ?ApplicationUser $user = null;

    #[ORM\ManyToOne(inversedBy: 'notifications')]
    #[ORM\JoinColumn(nullable: false)]
    #[Groups(['notifications:read'])]
    private ?JobApplication $jobApplication = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Groups(['notifications:read'])]
    private ?\DateTimeInterface $creationDate = null;

    #[ORM\Column(nullable: true)]
    #[Groups(['notifications:read'])]
    private ?bool $seen = false;

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

    public function getUser(): ?ApplicationUser
    {
        return $this->user;
    }

    public function setUser(?ApplicationUser $user): self
    {
        $this->user = $user;

        return $this;
    }

    public function getJobApplication(): ?JobApplication
    {
        return $this->jobApplication;
    }

    public function setJobApplication(?JobApplication $jobApplication): self
    {
        $this->jobApplication = $jobApplication;

        return $this;
    }

    public function getCreationDate(): ?\DateTimeInterface
    {
        return $this->creationDate;
    }

    public function setCreationDate(\DateTimeInterface $creationDate): self
    {
        $this->creationDate = $creationDate;

        return $this;
    }

    public function isSeen(): ?bool
    {
        return $this->seen ?? false;
    }

    public function setSeen(?bool $seen): self
    {
        $this->seen = $seen;

        return $this;
    }
}
