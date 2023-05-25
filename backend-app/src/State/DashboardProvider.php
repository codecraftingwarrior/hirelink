<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProviderInterface;
use App\ApiResource\Dashboard;
use App\Repository\JobApplicationRepository;
use App\Repository\JobOfferRepository;
use Doctrine\Common\Collections\ArrayCollection;

class DashboardProvider implements ProviderInterface
{
    public function __construct(
        private readonly JobOfferRepository $jobOfferRepository,
        private readonly JobApplicationRepository $jobApplicationRepository
    )
    {
    }

    public function provide(Operation $operation, array $uriVariables = [], array $context = []): Dashboard
    {
        $dashboard = new Dashboard();

        $announceCount = $this->jobOfferRepository->count([]);
        $mostRequestedJobs = $this->jobApplicationRepository->findElementsWithHighestFrequency();

        $candidatureCount = $this->jobApplicationRepository->count([]);
        $mostProposedJobs = $this->jobOfferRepository->findElementsWithHighestFrequency();

        $dashboard->setAnnounceCount($announceCount);
        $dashboard->setCandidatureCount($candidatureCount);
        $dashboard->setMostProposedJobs(new ArrayCollection($mostProposedJobs));
        $dashboard->setMostRequestedJobs(new ArrayCollection($mostRequestedJobs));

        return $dashboard;
    }
}
