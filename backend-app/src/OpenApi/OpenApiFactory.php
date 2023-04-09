<?php

namespace App\OpenApi;

use ApiPlatform\OpenApi\Factory\OpenApiFactoryInterface;
use ApiPlatform\OpenApi\Model\Parameter;
use ApiPlatform\OpenApi\Model\PathItem;
use ApiPlatform\OpenApi\OpenApi;

class OpenApiFactory implements OpenApiFactoryInterface
{
    public function __construct(private OpenApiFactoryInterface $decorated)
    {
    }

    public function __invoke(array $context = []): OpenApi
    {
        $openApi = $this->decorated->__invoke($context);
        /** @var PathItem $path */
        foreach ($openApi->getPaths()->getPaths() as $key => $path) {
            if ($path?->getGet()?->getSummary() === 'hidden')
                $openApi->getPaths()->addPath($key, $path->withGet(null));
        }

        return $openApi;
    }
}