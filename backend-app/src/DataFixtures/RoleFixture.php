<?php

namespace App\DataFixtures;

use App\Entity\Role;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;

class RoleFixture extends Fixture
{
    public const ROLE_EMPLOYER_REFERENCE = 'employer';
    public function load(ObjectManager $manager): void
    {
        $employer = new Role();
        $employer->setName('EMPLOYER')
            ->setCode('EMP')
        ;

        $this->setReference(self::ROLE_EMPLOYER_REFERENCE,$employer);

        $manager->persist($employer);
        $manager->flush();
    }
}