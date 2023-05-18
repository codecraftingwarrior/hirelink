<?php

namespace App\Serializer;

use App\Entity\Document;
use Symfony\Component\Serializer\Exception\ExceptionInterface;
use Symfony\Component\Serializer\Normalizer\NormalizerAwareInterface;
use Symfony\Component\Serializer\Normalizer\NormalizerAwareTrait;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Vich\UploaderBundle\Storage\StorageInterface;

class DocumentNormalizer implements NormalizerInterface, NormalizerAwareInterface
{

    use NormalizerAwareTrait;

    public function __construct(
        private readonly StorageInterface $storage,
    )
    {
    }

    private const ALREADY_APPLY = 'AppDocumentNormalizerAlreadyCalled';


    public function supportsNormalization(mixed $data, string $format = null, array $context = []): bool
    {
        return !isset($context[self::ALREADY_APPLY]) && $data instanceof Document;
    }

    /**
     * @throws ExceptionInterface
     */
    public function normalize(mixed $object, string $format = null, array $context = [])
    {


        /** @var Document $object */
        $object->setFilePath($this->storage->resolveUri($object, 'file'));

        $context[self::ALREADY_APPLY] = true;

        return $this->normalizer->normalize($object, $format, $context);
    }
}