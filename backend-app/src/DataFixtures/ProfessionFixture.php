<?php

namespace App\DataFixtures;

use App\Entity\Profession;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Bundle\FixturesBundle\FixtureGroupInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class ProfessionFixture extends Fixture implements FixtureGroupInterface
{
    public static $refs = [];

    public function load(ObjectManager $manager): void
    {
        $faker = Factory::create('fr_FR');

        for ($i = 0; $i < 20; $i++) {
            $ref = md5(microtime());
            $profession = new Profession();
            $profession->setName($faker->unique()->jobTitle());
            self::$refs[] = $ref;
            $this->addReference($ref, $profession);
            $manager->persist($profession);
        }


        $manager->flush();
    }

    public static function getGroups(): array
    {
        return ['group1'];
    }
}