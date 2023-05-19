<?php

namespace App\Controller;

use ApiPlatform\Api\IriConverterInterface;
use App\Entity\ApplicationUser;
use App\Entity\Document;
use JetBrains\PhpStorm\NoReturn;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpKernel\Attribute\AsController;

#[AsController]
class CreateDocumentController extends AbstractController
{

    public function __construct(
        private readonly IriConverterInterface $iriConverter
    )
    {
    }

    public function __invoke(Request $request): Document
    {
        $document = new Document();

        $file = $request->files->get('file');

        /** @var ApplicationUser $owner */
        $owner = $this
            ->iriConverter
            ->getResourceFromIri($request->request->get('owner'));
            $request->request->get('owner');

        $document
            ->setFile($file)
            ->setTitle($request->request->get('title'))
            ->setOwner($owner);

        return $document;

    }
}