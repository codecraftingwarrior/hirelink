<?php

namespace App\DataFixtures;

use App\Entity\ApplicationUser;
use App\Entity\ContractType;
use App\Entity\JobOffer;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Common\DataFixtures\FixtureInterface;
use Doctrine\Persistence\ObjectManager;

class JobOfferFixture extends Fixture implements DependentFixtureInterface
{
    public function load(ObjectManager $manager): void
    {
        // $product = new Product();
        // $manager->persist($product);
        $offreApple = new JobOffer();

        $offreApple->setTitle('Software Engineer')
            ->setDescription(' At Apple, new ideas have a way of becoming phenomenal products, services, and customer experiences very quickly. You bring passion, and dedication to your job and there is no telling what you could accomplish! The Creativity Apps team is looking for a senior engineer to help architect and build collaborative features for apps including Keynote, Numbers, and Pages. You’ll be working with the world-class teams behind iWork, Final Cut Pro, Logic Pro, iMovie, and GarageBand to create next-generation tools for creators.
Key Qualifications

    5+ years of proven experience shipping high quality, tested code with experience on Apple platforms preferred
    Experience as a technical lead; demonstrating strong communication, collaboration, and project management skills
    Proven experience architecting applications with cloud backend storage
    Solid grasp of computer science fundamentals and object-oriented design
    Proficiency in Swift, Objective-C, C++, or C
    Ability to write performant, scalable, maintainable, and correct multi-threaded code
    Ability to work with ambiguity and take ownership of features/product
    Willingness to ask for help, learn from others, and mentor others in turn
    Committed to encouraging an open and inclusive work environment

Description
This is a great opportunity to be one of the early members of a new engineering team! You will be responsible for setting the direction and architecture of new and innovative collaborative features for the creative space. You will work with human interface designers, quality assurance teams, and cross-functional teams to go beyond code and will influence everything from user interface to project planning. The ideal candidate should have experience in iOS development, cloud technologies, care about long-term sustainable software development, and can drive features from concept all the way to delivery. This position also requires a self-motivated individual with excellent interpersonal skills to effectively collaborate with all levels of the organization.
Education & Experience
Bachelor’s Degree in Computer Science or equivalent experience
Additional Requirements
')
            ->setSalary(180000)
            ->setFromDate(new \DateTime('01-05-2023'))
            ->setToDate(new \DateTime('01-05-2026'))
            ->setAddress('New York, NY')
            ->setContractType($this->getReference(ContractTypeFixture::CDD_REFERENCE))
            ->setOwner($this->getReference(ApplicationUserFixture::APPLE_MANAGER_REFERENCE))
            ->setProfession($this->getReference(ProfessionFixture::SOFTWARE_DEVELOPER_REFERENCE))
            ->setCategory($this->getReference(JobOfferCategoryFixture::COMPUTER_SCIENCE_REFERENCE))
            ;
        $manager->persist($offreApple);


        $manager->flush();
    }

    /**
     * @return list<class-string<FixtureInterface>>
     */
    public function getDependencies(): array
    {
        return [ContractTypeFixture::class,CompanyFixture::class,ProfessionFixture::class,JobOfferCategoryFixture::class];
    }
}
