<?php

namespace App\DataFixtures;

use App\Entity\JobOfferCategory;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Bundle\FixturesBundle\FixtureGroupInterface;
use Doctrine\Persistence\ObjectManager;

class JobOfferCategoryFixture extends Fixture implements FixtureGroupInterface
{
    public static array $refs = [];

    public function load(ObjectManager $manager): void
    {
        $names = ['Marketing', 'Finance', 'Sales', 'Human Resources', 'Engineering', 'Design', 'Operations', 'Customer Service', 'Legal', 'Research', 'Computer Science'];

        foreach ($names as $name) {
            $ref = md5(microtime());
            self::$refs[] = $ref;
            $jobOfferCategory = new JobOfferCategory();
            $jobOfferCategory->setName($name);
            $this->addReference($ref, $jobOfferCategory);
            $manager->persist($jobOfferCategory);
        }

        $manager->flush();
    }

    public static function getGroups(): array
    {
        return ['group1'];
    }
}