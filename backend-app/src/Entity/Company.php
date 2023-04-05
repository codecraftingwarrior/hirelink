<?php

namespace App\Entity;

use App\Entity\RootEntity\Auditable;
use App\Repository\CompanyRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CompanyRepository::class)]
class Company extends Auditable
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 80)]
    private ?string $name = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $departmentName = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $subDepartmentName = null;

    #[ORM\Column(length: 80)]
    private ?string $nationalUniqueNumber = null;

    #[ORM\Column(length: 80)]
    private ?string $contactFullname = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $contactFullname2 = null;

    #[ORM\Column(length: 80)]
    private ?string $mailAddress = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $mailAddress2 = null;

    #[ORM\Column(length: 80)]
    private ?string $phoneNumber = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $phoneNumber2 = null;

    #[ORM\Column(length: 255)]
    private ?string $address = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $city = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $country = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $websiteLink = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $linkedInLink = null;

    #[ORM\Column(length: 80, nullable: true)]
    private ?string $facebookLink = null;

    #[ORM\OneToMany(mappedBy: 'company', targetEntity: ApplicationUser::class)]
    private Collection $applicationUsers;

    public function __construct()
    {
        $this->applicationUsers = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(string $name): self
    {
        $this->name = $name;

        return $this;
    }

    public function getDepartmentName(): ?string
    {
        return $this->departmentName;
    }

    public function setDepartmentName(?string $departmentName): self
    {
        $this->departmentName = $departmentName;

        return $this;
    }

    public function getSubDepartmentName(): ?string
    {
        return $this->subDepartmentName;
    }

    public function setSubDepartmentName(?string $subDepartmentName): self
    {
        $this->subDepartmentName = $subDepartmentName;

        return $this;
    }

    public function getNationalUniqueNumber(): ?string
    {
        return $this->nationalUniqueNumber;
    }

    public function setNationalUniqueNumber(string $nationalUniqueNumber): self
    {
        $this->nationalUniqueNumber = $nationalUniqueNumber;

        return $this;
    }

    public function getContactFullname(): ?string
    {
        return $this->contactFullname;
    }

    public function setContactFullname(string $contactFullname): self
    {
        $this->contactFullname = $contactFullname;

        return $this;
    }

    public function getContactFullname2(): ?string
    {
        return $this->contactFullname2;
    }

    public function setContactFullname2(string $contactFullname2): self
    {
        $this->contactFullname2 = $contactFullname2;

        return $this;
    }

    public function getMailAddress(): ?string
    {
        return $this->mailAddress;
    }

    public function setMailAddress(string $mailAddress): self
    {
        $this->mailAddress = $mailAddress;

        return $this;
    }

    public function getMailAddress2(): ?string
    {
        return $this->mailAddress2;
    }

    public function setMailAddress2(?string $mailAddress2): self
    {
        $this->mailAddress2 = $mailAddress2;

        return $this;
    }

    public function getPhoneNumber(): ?string
    {
        return $this->phoneNumber;
    }

    public function setPhoneNumber(string $phoneNumber): self
    {
        $this->phoneNumber = $phoneNumber;

        return $this;
    }

    public function getPhoneNumber2(): ?string
    {
        return $this->phoneNumber2;
    }

    public function setPhoneNumber2(?string $phoneNumber2): self
    {
        $this->phoneNumber2 = $phoneNumber2;

        return $this;
    }

    public function getAddress(): ?string
    {
        return $this->address;
    }

    public function setAddress(string $address): self
    {
        $this->address = $address;

        return $this;
    }

    public function getCity(): ?string
    {
        return $this->city;
    }

    public function setCity(?string $city): self
    {
        $this->city = $city;

        return $this;
    }

    public function getCountry(): ?string
    {
        return $this->country;
    }

    public function setCountry(?string $country): self
    {
        $this->country = $country;

        return $this;
    }

    public function getWebsiteLink(): ?string
    {
        return $this->websiteLink;
    }

    public function setWebsiteLink(?string $websiteLink): self
    {
        $this->websiteLink = $websiteLink;

        return $this;
    }

    public function getLinkedInLink(): ?string
    {
        return $this->linkedInLink;
    }

    public function setLinkedInLink(?string $linkedInLink): self
    {
        $this->linkedInLink = $linkedInLink;

        return $this;
    }

    public function getFacebookLink(): ?string
    {
        return $this->facebookLink;
    }

    public function setFacebookLink(?string $facebookLink): self
    {
        $this->facebookLink = $facebookLink;

        return $this;
    }

    /**
     * @return Collection<int, ApplicationUser>
     */
    public function getApplicationUsers(): Collection
    {
        return $this->applicationUsers;
    }

    public function addApplicationUser(ApplicationUser $applicationUser): self
    {
        if (!$this->applicationUsers->contains($applicationUser)) {
            $this->applicationUsers->add($applicationUser);
            $applicationUser->setCompany($this);
        }

        return $this;
    }

    public function removeApplicationUser(ApplicationUser $applicationUser): self
    {
        if ($this->applicationUsers->removeElement($applicationUser)) {
            // set the owning side to null (unless already changed)
            if ($applicationUser->getCompany() === $this) {
                $applicationUser->setCompany(null);
            }
        }

        return $this;
    }
}