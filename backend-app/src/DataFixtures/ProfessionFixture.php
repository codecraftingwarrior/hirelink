<?php

namespace App\DataFixtures;

use App\Entity\Profession;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;

class ProfessionFixture extends Fixture
{
    public const SOFTWARE_DEVELOPER_REFERENCE = 'Software Developer';
    public function load(ObjectManager $manager): void
    {
        $softwareDeveloper = new Profession();
        $softwareDeveloper->setName('Software Developer');
        $this->addReference(self::SOFTWARE_DEVELOPER_REFERENCE, $softwareDeveloper);
        $manager->persist($softwareDeveloper);

        $manager->flush();
    }
}