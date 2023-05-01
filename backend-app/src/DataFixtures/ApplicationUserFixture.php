<?php

namespace App\DataFixtures;

use App\Entity\ApplicationUser;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Common\DataFixtures\FixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class ApplicationUserFixture extends Fixture implements DependentFixtureInterface
{
    public const APPLE_MANAGER_REFERENCE = 'apple manager';

    public function __construct(private readonly UserPasswordHasherInterface $hasher)
    {
    }
    public function load(ObjectManager $manager): void
    {
        $ahmed = new ApplicationUser();
        $ahmed->setPhoneNumber('123-456-789')
            ->setEmail('ahmed@gmail.com')
            ->setRegistrationState('pending')
            ->setEnabled(true)
            ->setRole($this->getReference(RoleFixture::ROLE_EMPLOYER_REFERENCE))
        ;

        $hashPassword = $this->hasher->hashPassword($ahmed, '123456');
        $ahmed->setPassword($hashPassword);

        $manager->persist($ahmed);

        $appleManager = new ApplicationUser();
        $appleManager->setPhoneNumber('123-456-789')
            ->setEmail('apple-manager@apple.com')
            ->setPassword('apple')
            ->setRegistrationState('pending')
            ->setEnabled(true)
            ->setRole($this->getReference(RoleFixture::ROLE_EMPLOYER_REFERENCE))
            ->setCompany($this->getReference(CompanyFixture::APPLE_REFERENCE))
        ;
        $this->addReference(self::APPLE_MANAGER_REFERENCE,$appleManager);

        $manager->persist($appleManager);
        $manager->flush();
    }

    /**
     * @return list<class-string<FixtureInterface>>
     */
    public function getDependencies(): array
    {
        return [CompanyFixture::class,RoleFixture::class];
    }
}