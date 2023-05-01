<?php

namespace App\DataFixtures;

use App\Entity\JobOfferCategory;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;

class JobOfferCategoryFixture extends Fixture
{
    public const COMPUTER_SCIENCE_REFERENCE = 'Computer Science';
    public function load(ObjectManager $manager): void
    {
        $computerScience = new JobOfferCategory();
        $computerScience->setName('Computer Science');
        $this->addReference(self::COMPUTER_SCIENCE_REFERENCE, $computerScience);
        $manager->persist($computerScience);

        $manager->flush();
    }
}