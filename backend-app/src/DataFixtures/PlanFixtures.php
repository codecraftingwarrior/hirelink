<?php

namespace App\DataFixtures;

use App\Entity\Plan;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Bundle\FixturesBundle\FixtureGroupInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class PlanFixtures extends Fixture implements FixtureGroupInterface
{
    public static $refs = [];

    public function load(ObjectManager $manager): void
    {
        $faker = Factory::create();

        $plans = [
            [
                'name' => 'Start',
                'price' => 10,
                'conditions' => $faker->paragraph(5),
                'unsubscription_conditions' => $faker->paragraph(5),
                'description' => $faker->paragraph(3),
            ],
            [
                'name' => 'Basic',
                'price' => 200,
                'conditions' => $faker->paragraph(5),
                'unsubscription_conditions' => $faker->paragraph(5),
                'description' => $faker->paragraph(),
            ],
            [
                'name' => 'Standard',
                'price' => 400,
                'conditions' => $faker->paragraph(5),
                'unsubscription_conditions' => $faker->paragraph(5),
                'description' => $faker->paragraph(),
            ],
            [
                'name' => 'Advanced',
                'price' => 600,
                'conditions' => $faker->paragraph(5),
                'unsubscription_conditions' => $faker->paragraph(5),
                'description' => $faker->paragraph(),
            ],
            [
                'name' => 'Premium',
                'price' => 1000,
                'conditions' => $faker->paragraph(5),
                'unsubscription_conditions' => $faker->paragraph(5),
                'description' => $faker->paragraph(),
            ],
            [
                'name' => 'Ultimate',
                'price' => 1500,
                'conditions' => $faker->paragraph(5),
                'unsubscription_conditions' => $faker->paragraph(5),
                'description' => $faker->paragraph(),
            ],
        ];

        foreach ($plans as $plan) {
            $ref = md5(microtime());

            $offerPlan = new Plan();

            $offerPlan
                ->setName($plan['name'])
                ->setPrice($plan['price'])
                ->setConditions($plan['conditions'])
                ->setUnsubscriptionConditions($plan['unsubscription_conditions'])
                ->setDescription($plan['description']);

            self::$refs[] = $ref;
            $this->addReference($ref, $offerPlan);

            $manager->persist($offerPlan);
        }

        $manager->flush();
    }

    public static function getGroups(): array
    {
        return ['group1'];
    }
}
