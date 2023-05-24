<?php

namespace App\State;

use ApiPlatform\Metadata\Operation;
use ApiPlatform\State\ProcessorInterface;
use App\Entity\JobApplication;
use App\Entity\Notifications;
use App\Enum\JobApplicationState;
use App\Enum\RoleType;
use App\Repository\ApplicationUserRepository;
use App\Repository\DocumentRepository;
use App\Repository\JobApplicationRepository;
use App\Repository\JobOfferRepository;
use App\Repository\NotificationsRepository;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;

class UpdateJobApplicationStateProcessor implements ProcessorInterface
{
    public function __construct(
        private readonly NotificationsRepository $notificationsRepository,
        private readonly JobApplicationRepository $jobApplicationRepository,
        private readonly ApplicationUserRepository $applicationUserRepository
    )
    {
    }

    public function process(mixed $data, Operation $operation, array $uriVariables = [], array $context = []): JobApplication
    {
        $jobApplication = $this->jobApplicationRepository->findOneBy([
            'id' => $uriVariables['id']
        ]);

        if (!$jobApplication){
            throw new NotFoundHttpException("There is no job application found with the given ID and Email.");
        }

        $applicant = $jobApplication->getApplicant();

        $notification = new Notifications();

        $jobApplicationState = JobApplicationState::fromCode($data->getState());

        match ($jobApplicationState) {
            JobApplicationState::ACCEPTED => $this->handleJobApplicationAccepted($notification, $jobApplication),
            JobApplicationState::REFUSED=> $this->handleJobApplicationRefused($notification, $jobApplication),
            JobApplicationState::IN_PROGRESS => $this->handleJobApplicationInProgress($notification, $jobApplication),
            JobApplicationState::PENDING => null
        };

        $notification->setJobApplication($jobApplication);
        $notification->setUser($applicant);
        $notification->setCreationDate(new \DateTime());

        $jobApplication->addNotification($notification);
        $applicant->addNotification($notification);

        $this->notificationsRepository->save($notification, true);
        $this->jobApplicationRepository->save($jobApplication, true);
        $this->applicationUserRepository->save($applicant, true);

        return $jobApplication;
    }

    private function handleJobApplicationAccepted(Notifications $notification, JobApplication $jobApplication): void
    {
        $notification->setTitle("Your candidacy has been accepted");
        $jobApplication->setState("ACCEPTED");
    }

    private function handleJobApplicationRefused(Notifications $notification, JobApplication $jobApplication): void
    {
        $notification->setTitle("Your candidacy has been rejected");
        $jobApplication->setState("REFUSED");
    }

    private function handleJobApplicationInProgress(Notifications $notification, JobApplication $jobApplication): void
    {
        $notification->setTitle("Your candidacy has been seen");
        $jobApplication->setState("IN_PROGRESS");
    }
}
