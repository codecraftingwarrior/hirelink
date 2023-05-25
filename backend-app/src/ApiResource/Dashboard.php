<?php

namespace App\ApiResource;

use ApiPlatform\Metadata\Get;
use App\State\DashboardProvider;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use ApiPlatform\Metadata\ApiResource;
use Symfony\Component\Serializer\Annotation\Groups;

#[ApiResource(
    operations : [
        new Get(
            uriTemplate: "/dashboard",
            normalizationContext: ['groups' => ['dashboard:read']],
            provider: DashboardProvider::class
        )
    ]
)]
class Dashboard
{
    #[Groups(['dashboard:read'])]
    private ?int $candidatureCount = null;
    #[Groups(['dashboard:read'])]
    private ?int $announceCount = null;
    #[Groups(['dashboard:read'])]
    private ArrayCollection $mostRequestedJobs;
    #[Groups(['dashboard:read'])]
    private ArrayCollection $mostProposedJobs;

    public function __construct()
    {
        $this->candidatureCount = 0;
        $this->announceCount = 0;
        $this->mostRequestedJobs = new ArrayCollection();
        $this->mostProposedJobs = new ArrayCollection();
    }

    /**
     * @return int|null
     */
    public function getCandidatureCount(): ?int
    {
        return $this->candidatureCount;
    }

    /**
     * @param int|null $candidatureCount
     */
    public function setCandidatureCount(?int $candidatureCount): void
    {
        $this->candidatureCount = $candidatureCount;
    }

    /**
     * @return int|null
     */
    public function getAnnounceCount(): ?int
    {
        return $this->announceCount;
    }

    /**
     * @param int|null $announceCount
     */
    public function setAnnounceCount(?int $announceCount): void
    {
        $this->announceCount = $announceCount;
    }

    /**
     * @return ArrayCollection
     */
    public function getMostRequestedJobs(): ArrayCollection
    {
        return $this->mostRequestedJobs;
    }

    /**
     * @param ArrayCollection $mostRequestedJobs
     */
    public function setMostRequestedJobs(ArrayCollection $mostRequestedJobs): void
    {
        $this->mostRequestedJobs = $mostRequestedJobs;
    }

    /**
     * @return ArrayCollection
     */
    public function getMostProposedJobs(): ArrayCollection
    {
        return $this->mostProposedJobs;
    }

    /**
     * @param ArrayCollection $mostProposedJobs
     */
    public function setMostProposedJobs(ArrayCollection $mostProposedJobs): void
    {
        $this->mostProposedJobs = $mostProposedJobs;
    }

}