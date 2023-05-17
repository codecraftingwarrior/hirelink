<?php

namespace App\DataFixtures;

use App\Entity\ContractType;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Bundle\FixturesBundle\FixtureGroupInterface;
use Doctrine\Persistence\ObjectManager;

class ContractTypeFixture extends Fixture implements FixtureGroupInterface
{
    public const CDI_REFERENCE = CDI_CONTRACT_CODE;
    public const CDD_REFERENCE = CDD_CONTRACT_CODE;
    public const ALT_REFERENCE = ALTERNATION_CONTRACT_CODE;
    public const INTERNSHIP_REFERENCE = INTERNSHIP_CONTRACT_CODE;

    public function load(ObjectManager $manager): void
    {
        $cdd = new ContractType();
        $cdd
            ->setCode(CDD_CONTRACT_CODE)
            ->setName('Fixed-term Contract');

        $this->addReference(self::CDD_REFERENCE, $cdd);

        $manager->persist($cdd);

        $cdi = new ContractType();
        $cdi->setCode(CDI_CONTRACT_CODE)
            ->setName('Permanent Contract');

        $this->addReference(self::CDI_REFERENCE, $cdi);


        $manager->persist($cdi);

        $alternation = new ContractType();
        $alternation
            ->setCode(ALTERNATION_CONTRACT_CODE)
            ->setName('Work-Study Contract');

        $this->addReference(self::ALT_REFERENCE, $alternation);


        $manager->persist($alternation);

        $internship = new contractType();
        $internship
            ->setCode(INTERNSHIP_CONTRACT_CODE)
            ->setName('Internship Contract');

        $this->addReference(self::INTERNSHIP_REFERENCE, $internship);

        $manager->persist($internship);

        $manager->flush();
    }

    public static function getGroups(): array
    {
        return ['group1'];
    }
}