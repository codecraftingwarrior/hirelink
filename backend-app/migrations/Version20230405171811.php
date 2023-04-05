<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20230405171811 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE application_user_application_user (application_user_source INT NOT NULL, application_user_target INT NOT NULL, INDEX IDX_543980799313492C (application_user_source), INDEX IDX_543980798AF619A3 (application_user_target), PRIMARY KEY(application_user_source, application_user_target)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE contract_type (id INT AUTO_INCREMENT NOT NULL, code VARCHAR(80) NOT NULL, name VARCHAR(80) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE job_application (id INT AUTO_INCREMENT NOT NULL, job_offer_id INT NOT NULL, applicant_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL, state VARCHAR(80) NOT NULL, INDEX IDX_C737C6883481D195 (job_offer_id), INDEX IDX_C737C68897139001 (applicant_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE job_offer (id INT AUTO_INCREMENT NOT NULL, category_id INT DEFAULT NULL, profession_id INT NOT NULL, contract_type_id INT NOT NULL, owner_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL, title VARCHAR(255) NOT NULL, description LONGTEXT NOT NULL, salary DOUBLE PRECISION NOT NULL, from_date DATE NOT NULL, to_date DATE NOT NULL, address VARCHAR(255) DEFAULT NULL, lat DOUBLE PRECISION DEFAULT NULL, lng DOUBLE PRECISION DEFAULT NULL, INDEX IDX_288A3A4E12469DE2 (category_id), INDEX IDX_288A3A4EFDEF8996 (profession_id), INDEX IDX_288A3A4ECD1DF15B (contract_type_id), INDEX IDX_288A3A4E7E3C61F9 (owner_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE job_offer_tag (job_offer_id INT NOT NULL, tag_id INT NOT NULL, INDEX IDX_E80C390A3481D195 (job_offer_id), INDEX IDX_E80C390ABAD26311 (tag_id), PRIMARY KEY(job_offer_id, tag_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE job_offer_category (id INT AUTO_INCREMENT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL, name VARCHAR(80) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE profession (id INT AUTO_INCREMENT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL, name VARCHAR(80) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE report (id INT AUTO_INCREMENT NOT NULL, author_id INT NOT NULL, reported_user_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL, reason VARCHAR(255) NOT NULL, INDEX IDX_C42F7784F675F31B (author_id), INDEX IDX_C42F7784E7566E (reported_user_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE share_group (id INT AUTO_INCREMENT NOT NULL, owner_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL, name VARCHAR(255) NOT NULL, INDEX IDX_E4AEA8287E3C61F9 (owner_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE share_group_application_user (share_group_id INT NOT NULL, application_user_id INT NOT NULL, INDEX IDX_1EEF86577AF6A82 (share_group_id), INDEX IDX_1EEF86574CD0D6A6 (application_user_id), PRIMARY KEY(share_group_id, application_user_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE tag (id INT AUTO_INCREMENT NOT NULL, owner_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL, name VARCHAR(80) NOT NULL, INDEX IDX_389B7837E3C61F9 (owner_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE application_user_application_user ADD CONSTRAINT FK_543980799313492C FOREIGN KEY (application_user_source) REFERENCES application_user (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE application_user_application_user ADD CONSTRAINT FK_543980798AF619A3 FOREIGN KEY (application_user_target) REFERENCES application_user (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE job_application ADD CONSTRAINT FK_C737C6883481D195 FOREIGN KEY (job_offer_id) REFERENCES job_offer (id)');
        $this->addSql('ALTER TABLE job_application ADD CONSTRAINT FK_C737C68897139001 FOREIGN KEY (applicant_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE job_offer ADD CONSTRAINT FK_288A3A4E12469DE2 FOREIGN KEY (category_id) REFERENCES job_offer_category (id)');
        $this->addSql('ALTER TABLE job_offer ADD CONSTRAINT FK_288A3A4EFDEF8996 FOREIGN KEY (profession_id) REFERENCES profession (id)');
        $this->addSql('ALTER TABLE job_offer ADD CONSTRAINT FK_288A3A4ECD1DF15B FOREIGN KEY (contract_type_id) REFERENCES contract_type (id)');
        $this->addSql('ALTER TABLE job_offer ADD CONSTRAINT FK_288A3A4E7E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE job_offer_tag ADD CONSTRAINT FK_E80C390A3481D195 FOREIGN KEY (job_offer_id) REFERENCES job_offer (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE job_offer_tag ADD CONSTRAINT FK_E80C390ABAD26311 FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE report ADD CONSTRAINT FK_C42F7784F675F31B FOREIGN KEY (author_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE report ADD CONSTRAINT FK_C42F7784E7566E FOREIGN KEY (reported_user_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE share_group ADD CONSTRAINT FK_E4AEA8287E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE share_group_application_user ADD CONSTRAINT FK_1EEF86577AF6A82 FOREIGN KEY (share_group_id) REFERENCES share_group (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE share_group_application_user ADD CONSTRAINT FK_1EEF86574CD0D6A6 FOREIGN KEY (application_user_id) REFERENCES application_user (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE tag ADD CONSTRAINT FK_389B7837E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE company ADD created_at DATETIME NOT NULL, ADD updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE document ADD created_at DATETIME NOT NULL, ADD updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE payment ADD created_at DATETIME NOT NULL, ADD updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE payment_information ADD created_at DATETIME NOT NULL, ADD updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE plan ADD created_at DATETIME NOT NULL, ADD updated_at DATETIME NOT NULL');
        $this->addSql('ALTER TABLE role ADD created_at DATETIME NOT NULL, ADD updated_at DATETIME NOT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE application_user_application_user DROP FOREIGN KEY FK_543980799313492C');
        $this->addSql('ALTER TABLE application_user_application_user DROP FOREIGN KEY FK_543980798AF619A3');
        $this->addSql('ALTER TABLE job_application DROP FOREIGN KEY FK_C737C6883481D195');
        $this->addSql('ALTER TABLE job_application DROP FOREIGN KEY FK_C737C68897139001');
        $this->addSql('ALTER TABLE job_offer DROP FOREIGN KEY FK_288A3A4E12469DE2');
        $this->addSql('ALTER TABLE job_offer DROP FOREIGN KEY FK_288A3A4EFDEF8996');
        $this->addSql('ALTER TABLE job_offer DROP FOREIGN KEY FK_288A3A4ECD1DF15B');
        $this->addSql('ALTER TABLE job_offer DROP FOREIGN KEY FK_288A3A4E7E3C61F9');
        $this->addSql('ALTER TABLE job_offer_tag DROP FOREIGN KEY FK_E80C390A3481D195');
        $this->addSql('ALTER TABLE job_offer_tag DROP FOREIGN KEY FK_E80C390ABAD26311');
        $this->addSql('ALTER TABLE report DROP FOREIGN KEY FK_C42F7784F675F31B');
        $this->addSql('ALTER TABLE report DROP FOREIGN KEY FK_C42F7784E7566E');
        $this->addSql('ALTER TABLE share_group DROP FOREIGN KEY FK_E4AEA8287E3C61F9');
        $this->addSql('ALTER TABLE share_group_application_user DROP FOREIGN KEY FK_1EEF86577AF6A82');
        $this->addSql('ALTER TABLE share_group_application_user DROP FOREIGN KEY FK_1EEF86574CD0D6A6');
        $this->addSql('ALTER TABLE tag DROP FOREIGN KEY FK_389B7837E3C61F9');
        $this->addSql('DROP TABLE application_user_application_user');
        $this->addSql('DROP TABLE contract_type');
        $this->addSql('DROP TABLE job_application');
        $this->addSql('DROP TABLE job_offer');
        $this->addSql('DROP TABLE job_offer_tag');
        $this->addSql('DROP TABLE job_offer_category');
        $this->addSql('DROP TABLE profession');
        $this->addSql('DROP TABLE report');
        $this->addSql('DROP TABLE share_group');
        $this->addSql('DROP TABLE share_group_application_user');
        $this->addSql('DROP TABLE tag');
        $this->addSql('ALTER TABLE company DROP created_at, DROP updated_at');
        $this->addSql('ALTER TABLE document DROP created_at, DROP updated_at');
        $this->addSql('ALTER TABLE payment DROP created_at, DROP updated_at');
        $this->addSql('ALTER TABLE payment_information DROP created_at, DROP updated_at');
        $this->addSql('ALTER TABLE plan DROP created_at, DROP updated_at');
        $this->addSql('ALTER TABLE role DROP created_at, DROP updated_at');
    }
}
