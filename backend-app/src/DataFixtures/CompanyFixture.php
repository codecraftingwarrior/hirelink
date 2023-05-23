<?php

namespace App\DataFixtures;

use App\Entity\Company;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Bundle\FixturesBundle\FixtureGroupInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class CompanyFixture extends Fixture implements FixtureGroupInterface
{
    public const APPLE_REFERENCE = 'apple';
    public static $refs = [];

    public function load(ObjectManager $manager): void
    {
        $ref = md5(microtime());

        $faker = Factory::create();

        $apple = new Company();
        $apple->setName('Apple')
            ->setNationalUniqueNumber('1235456789')
            ->setMailAddress('contact@apple.com')
            ->setPhoneNumber('123-456-789')
            ->setAddress('767 Fifth Avenue New York, NY 10153');

        self::$refs[] = $ref;

        $this->addReference($ref, $apple);
        $manager->persist($apple);

        $companies = [
            [
                'name' => 'Google',
                'national_unique_number' => '1234567890',
                'mail_address' => 'contact@google.com',
                'phone_number' => '0987654321',
                'address' => '1600 Amphitheatre Parkway, Mountain View, CA 94043',
            ],
            [
                'name' => 'Amazon',
                'national_unique_number' => '0987654321',
                'mail_address' => 'contact@amazon.com',
                'phone_number' => '1234567890',
                'address' => '410 Terry Ave N, Seattle, WA 98109',
            ]
        ];

        foreach ($companies as $data) {
            $ref = md5(microtime());
            $company = new Company();
            $company->setName($data['name'])
                ->setNationalUniqueNumber($data['national_unique_number'])
                ->setMailAddress($data['mail_address'])
                ->setPhoneNumber($data['phone_number'])
                ->setAddress($data['address']);

            self::$refs[] = $ref;
            $this->addReference($ref, $company);

            $manager->persist($company);
        }

        for ($i = 0; $i < 15; $i++) {
            $ref = md5(microtime());
            $company = new Company();
            $company->setName($faker->unique()->company())
                ->setNationalUniqueNumber($faker->ean13())
                ->setMailAddress($faker->companyEmail())
                ->setPhoneNumber($faker->phoneNumber())
                ->setLat($faker->latitude())
                ->setLng($faker->longitude())
                ->setAddress($faker->address());

            self::$refs[] = $ref;
            $this->addReference($ref, $company);

            $manager->persist($company);
        }

        $manager->flush();
    }

    public static function getGroups(): array
    {
        return ['group1'];
    }
}