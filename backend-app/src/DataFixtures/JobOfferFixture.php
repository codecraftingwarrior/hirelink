<?php

namespace App\DataFixtures;

use App\Entity\ApplicationUser;
use App\Entity\JobOffer;
use App\Entity\Role;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Bundle\FixturesBundle\FixtureGroupInterface;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Common\DataFixtures\FixtureInterface;
use Doctrine\Persistence\ManagerRegistry;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class JobOfferFixture extends Fixture implements DependentFixtureInterface, FixtureGroupInterface
{

    public function load(ObjectManager $manager): void
    {
        $faker = Factory::create();

        $contractTypeRefs = [
            ContractTypeFixture::INTERNSHIP_REFERENCE,
            ContractTypeFixture::ALT_REFERENCE,
            ContractTypeFixture::CDD_REFERENCE,
            ContractTypeFixture::CDI_REFERENCE
        ];

        for ($i = 0; $i < 30; $i++) {
            $minSalary = floor($faker->numberBetween(30000, 80000) / 10) * 10;
            $maxSalary = $minSalary + floor($faker->numberBetween(20000, 70000) / 10) * 10;

            $fromDate = new \DateTime('now + 1 week');
            $toDate = new \DateTime('now + 16 weeks');

            $offer = new JobOffer();
            $offer
                ->setTitle($faker->unique()->jobTitle())
                ->setDescription($faker->paragraph(5))
                ->setMaxSalary($maxSalary)
                ->setMinSalary($minSalary)
                ->setFromDate($fromDate)
                ->setToDate($toDate)
                ->setAddress($faker->address())
                ->setLat($faker->latitude())
                ->setLng($faker->longitude())
                ->setContractType($this->getReference($faker->randomElement($contractTypeRefs)))
                ->setOwner($this->getReference($faker->randomElement(ApplicationUserFixture::$refs)))
                ->setProfession($this->getReference($faker->randomElement(ProfessionFixture::$refs)))
                ->setCategory($this->getReference($faker->randomElement(JobOfferCategoryFixture::$refs)));

            $manager->persist($offer);
        }

        $manager->flush();
    }

    /**
     * @return list<class-string<FixtureInterface>>
     */
    public function getDependencies(): array
    {
        return [
            ContractTypeFixture::class,
            ProfessionFixture::class,
            JobOfferCategoryFixture::class
        ];
    }

    public static function getGroups(): array
    {
        return ['group1'];
    }
}
