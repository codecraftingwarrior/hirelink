<?php

namespace App\DataFixtures;

use App\Entity\Role;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Bundle\FixturesBundle\FixtureGroupInterface;
use Doctrine\Persistence\ObjectManager;

class RoleFixture extends Fixture implements FixtureGroupInterface
{
    public const ROLE_EMPLOYER_REFERENCE = EMPLOYER_ROLE_CODE;

    public function load(ObjectManager $manager)
    {
        $applicantRole = new Role();
        $applicantRole->setCode('APP')
            ->setName('APPLICANT');

        $manager->persist($applicantRole);

        $employerRole = new Role();
        $employerRole->setCode('EMP')
            ->setName('EMPLOYER');
        $manager->persist($employerRole);

        $this->addReference(self::ROLE_EMPLOYER_REFERENCE, $employerRole);

        $agencyRole = new Role();
        $agencyRole->setCode('AGC')
            ->setName('AGENCY');

        $manager->persist($agencyRole);

        $managerRole = new Role();
        $managerRole->setCode('MAN')
            ->setName('MANAGER');

        $manager->persist($managerRole);

        $manager->flush();
    }

    public static function getGroups(): array
    {
        return ['group1'];
    }
}