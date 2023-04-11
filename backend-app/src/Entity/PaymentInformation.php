<?php

namespace App\Entity;

use ApiPlatform\Metadata\ApiResource;
use ApiPlatform\Metadata\Get;
use ApiPlatform\Metadata\Post;
use App\Entity\RootEntity\TrackableEntity;
use App\Repository\PaymentInformationRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\NotBlank;

#[ORM\Entity(repositoryClass: PaymentInformationRepository::class)]
#[ApiResource(
    operations: [
        new Get(),
        new Post()
    ],
    normalizationContext: ['groups' => ['payment-information:read']],
    denormalizationContext: ['groups' => ['payment-information:writable']]
)]
#[UniqueEntity(fields: ['creditCardNumber'], message: 'There is already a credit card with this number.')]
class PaymentInformation extends TrackableEntity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    #[Groups(['payment-information:read'])]
    private ?int $id = null;

    #[ORM\Column(length: 80)]
    #[Groups(['payment-information:read', 'payment-information:writable'])]
    #[NotBlank]
    private ?string $creditCardNumber = null;

    #[ORM\Column(length: 3)]
    #[Groups(['payment-information:read', 'payment-information:writable'])]
    #[Length(min: 3, max: 3)]
    private ?string $cvv = null;

    #[ORM\OneToMany(mappedBy: 'paymentInformation', targetEntity: Payment::class)]
    private Collection $payments;

    #[ORM\ManyToOne(inversedBy: 'paymentInformation')]
    #[ORM\JoinColumn(nullable: false)]
    #[Groups(['payment-information:writable'])]
    #[NotBlank]
    private ?ApplicationUser $owner = null;

    public function __construct()
    {
        $this->payments = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getCreditCardNumber(): ?string
    {
        return $this->creditCardNumber;
    }

    public function setCreditCardNumber(string $creditCardNumber): self
    {
        $this->creditCardNumber = $creditCardNumber;

        return $this;
    }

    public function getCvv(): ?string
    {
        return $this->cvv;
    }

    public function setCvv(string $cvv): self
    {
        $this->cvv = $cvv;

        return $this;
    }

    /**
     * @return Collection<int, Payment>
     */
    public function getPayments(): Collection
    {
        return $this->payments;
    }

    public function addPayment(Payment $payment): self
    {
        if (!$this->payments->contains($payment)) {
            $this->payments->add($payment);
            $payment->setPaymentInformation($this);
        }

        return $this;
    }

    public function removePayment(Payment $payment): self
    {
        if ($this->payments->removeElement($payment)) {
            // set the owning side to null (unless already changed)
            if ($payment->getPaymentInformation() === $this) {
                $payment->setPaymentInformation(null);
            }
        }

        return $this;
    }

    public function getOwner(): ?ApplicationUser
    {
        return $this->owner;
    }

    public function setOwner(?ApplicationUser $owner): self
    {
        $this->owner = $owner;

        return $this;
    }
}
