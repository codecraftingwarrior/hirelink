<?php

namespace App\EventSubscriber;

use Lexik\Bundle\JWTAuthenticationBundle\Event\AuthenticationFailureEvent;
use Lexik\Bundle\JWTAuthenticationBundle\Response\JWTAuthenticationFailureResponse;

class JWTAuthenticationFailureSubscriber
{
    public function onAuthenticationFailureResponse(AuthenticationFailureEvent $event): void
    {
        $data = 'incorrect email or password, please try again.';

        $response = new JWTAuthenticationFailureResponse($data);

        $event->setResponse($response);
    }
}