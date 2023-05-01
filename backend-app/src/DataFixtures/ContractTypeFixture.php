<?php

namespace App\DataFixtures;

use App\Entity\ContractType;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;

class ContractTypeFixture extends Fixture
{
    public const CDI_REFERENCE = 'CDI';
    public const CDD_REFERENCE = 'CDD';
    public function load(ObjectManager $manager): void
    {
        $cdd = new ContractType();
        $cdd->setCode('CDD')->setName('contract of limited duration');
        $this->addReference(self::CDD_REFERENCE, $cdd);
        $manager->persist($cdd);

        $cdi = new ContractType();
        $cdi->setCode('CDI')->setName('contract of indefinite duration');
        $this->addReference(self::CDI_REFERENCE, $cdi);
        $manager->persist($cdi);

        $manager->flush();
    }
}