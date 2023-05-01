<?php

namespace App\DataFixtures;

use App\Entity\Company;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;

class CompanyFixture extends Fixture
{
    public const APPLE_REFERENCE = 'apple';
    public function load(ObjectManager $manager): void
    {
        $apple = new Company();
        $apple->setName('Apple')
            ->setNationalUniqueNumber('1235456789')
            ->setMailAddress('contact@apple.com')
            ->setPhoneNumber('123-456-789')
            ->setAddress('767 Fifth Avenue New York, NY 10153')
        ;

        $this->addReference(self::APPLE_REFERENCE, $apple);
        $manager->persist($apple);

        $manager->flush();
    }
}