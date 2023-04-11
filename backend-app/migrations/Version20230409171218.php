<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20230409171218 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE application_user CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE company CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE document CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE job_application CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE job_offer CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE job_offer_category CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE payment CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE payment_information CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE plan CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE profession CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE report CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE role CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE share_group CHANGE updated_at updated_at DATETIME DEFAULT NULL');
        $this->addSql('ALTER TABLE tag CHANGE updated_at updated_at DATETIME DEFAULT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE application_user CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE company CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE document CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE job_application CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE job_offer CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE job_offer_category CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE payment CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE payment_information CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE plan CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE profession CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE report CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE role CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE share_group CHANGE updated_at updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE tag CHANGE updated_at updated_at DATETIME NOT NULL');
    }
}
