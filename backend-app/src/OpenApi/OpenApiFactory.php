<?php

namespace App\OpenApi;

use ApiPlatform\OpenApi\Factory\OpenApiFactoryInterface;
use ApiPlatform\OpenApi\Model\Parameter;
use ApiPlatform\OpenApi\Model\PathItem;
use ApiPlatform\OpenApi\Model\RequestBody;
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

        $schemas = $openApi->getComponents()->getSchemas();

        $schemas['DigitVerificationInput'] = new \ArrayObject([
            'type' => 'object',
            'properties' => [
                'otpDigit' => [
                    'type' => 'string',
                    'example' => '1234'
                ],
                'userID' => [
                    'type' => 'int',
                    'example' => 34
                ],
                'userEmail' => [
                    'type' => 'string',
                    'example' => 'example@hirelink.fr'
                ]
            ]
        ]);

        $schemas['DigitVerificationOutput'] = new \ArrayObject([
            'type' => 'object',
            'properties' => [
                'status' => [
                    'type' => 'boolean',
                    'example' => true
                ]
            ]
        ]);

        /** @var PathItem $otpVerificationPathItem */
        $otpVerificationPathItem = $openApi->getPaths()->getPath('/api/auth/verify-digit');
        $operation = $otpVerificationPathItem
            ->getPost()
            ->withResponses([
                '200' => [
                    'description' => '4-Digits verification process successfully terminated.',
                    'content' => [
                        'application/json' => [
                            'schema' => [
                                '$ref' => '#/components/schemas/DigitVerificationOutput'
                            ]
                        ]
                    ]
                ],
                '404' => [
                    'description' => '4-Digits verification failed.',
                    'content' => []
                ]
            ])
            ->withRequestBody(new RequestBody(
                content: new \ArrayObject([
                    'application/json' => [
                        'schema' => [
                            '$ref' => '#/components/schemas/DigitVerificationInput'
                        ]
                    ]
                ])
            ));

        $otpVerificationPathItem = $otpVerificationPathItem->withPost($operation);

        $openApi->getPaths()->addPath('/api/auth/verify-digit', $otpVerificationPathItem);

        return $openApi;
    }
}