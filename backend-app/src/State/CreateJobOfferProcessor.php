<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProcessorInterface;
use App\Repository\ApplicationUserRepository;
use App\Repository\DocumentRepository;
use App\Repository\JobOfferRepository;
use App\Repository\JobApplicationRepository;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;

class CreateJobOfferProcessor implements ProcessorInterface
{
    public function __construct(
        private readonly JobOfferRepository $jobOfferRepository,
        private readonly DocumentRepository $documentRepository,
        private readonly JobApplicationRepository $jobApplicationRepository,
        private readonly ApplicationUserRepository $applicationUserRepository
    )
    {
    }

    public function process(mixed $data, Operation $operation, array $uriVariables = [], array $context = []): void
    {
        $jobOffer = $this->jobOfferRepository->findOneBy([
            'id' => $uriVariables['jobOffer']
        ]);

        if (!$jobOffer)
            throw new NotFoundHttpException("There is no job offer with the given ID and Email.");

        $applicant = $this->applicationUserRepository->findOneBy([
            'id' => $uriVariables['applicant']
        ]);

        if (!$applicant)
            throw new NotFoundHttpException("There is no applicant with the given ID and Email.");

    }
}
