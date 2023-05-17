<?php

namespace App\DataFixtures;

use App\Entity\ApplicationUser;
use App\Enum\RegistrationState;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Bundle\FixturesBundle\FixtureGroupInterface;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Common\DataFixtures\FixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class ApplicationUserFixture extends Fixture implements DependentFixtureInterface, FixtureGroupInterface
{
    public const APPLE_MANAGER_REFERENCE = 'apple manager';

    public static $refs = [];

    public function __construct(private readonly UserPasswordHasherInterface $hasher)
    {
    }

    public function load(ObjectManager $manager): void
    {
        $faker = Factory::create('fr_FR');

        for ($i = 0; $i < 15; $i++) {
            $ref = md5(microtime());
            self::$refs[] = $ref;

            $user = new ApplicationUser();

            $user
                ->setPhoneNumber($faker->phoneNumber())
                ->setEmail($faker->email())
                ->setFirstName($faker->firstName())
                ->setLastName($faker->lastName())
                ->setRegistrationState(RegistrationState::PENDING->name)
                ->setEnabled(true)
                ->setRole($this->getReference(RoleFixture::ROLE_EMPLOYER_REFERENCE))
                ->setPlan($this->getReference($faker->randomElement(PlanFixtures::$refs)))
                ->setCompany($this->getReference(CompanyFixture::$refs[$i]));

            $hashPassword = $this->hasher->hashPassword($user, DEFAULT_PASSWORD);
            $user->setPassword($hashPassword);

            $this->addReference($ref, $user);

            $manager->persist($user);
        }
        $manager->flush();
    }

    /**
     * @return list<class-string<FixtureInterface>>
     */
    public function getDependencies(): array
    {
        return [
            CompanyFixture::class,
            RoleFixture::class
        ];
    }

    public static function getGroups(): array
    {
        return ['group1'];
    }
}