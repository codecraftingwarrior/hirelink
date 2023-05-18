<?php

namespace App\Controller;

use ApiPlatform\Api\IriConverterInterface;
use App\Entity\Document;
use App\Entity\JobApplication;
use App\Entity\JobOffer;
use App\Repository\ApplicationUserRepository;
use PhpParser\Comment\Doc;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Bundle\SecurityBundle\Security;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpKernel\Attribute\AsController;
use Symfony\Component\HttpKernel\Exception\BadRequestHttpException;

#[AsController]
class CreateJobApplicationController extends AbstractController
{

    public function __construct(
        private readonly Security                  $security,
        private readonly IriConverterInterface     $iriConverter,
        private readonly ApplicationUserRepository $userRepository,
    )
    {
    }

    public function __invoke(Request $request)
    {
        $files = $request->files->all()['document'];
        $allDocuments = $request->request->all()['document'];
        $currentUser = $this->userRepository->findOneBy([
           'email' => $this->security->getUser()->getUserIdentifier()
        ]);

        $jobApplication = new JobApplication();

        if (!$files || count($files) == 0)
            throw new BadRequestHttpException("You must provide a cv and a cover letter");

        for ($i = 0; $i < count($files); $i++) {
            $document = new Document();
            $rawDocument = $allDocuments[$i];
            $document
                ->setFile($files[$i]['file'])
                ->setTitle($rawDocument['title'])
                ->setOwner($currentUser);

            $jobApplication->addDocument($document);
        }

        /** @var JobOffer $jobOffer */
        $jobOffer = $this
            ->iriConverter
            ->getResourceFromIri($request->request->get('jobOffer'));

        $jobApplication
            ->setJobOffer($jobOffer)
            ->setApplicant($currentUser);

        return $jobApplication;
    }

}