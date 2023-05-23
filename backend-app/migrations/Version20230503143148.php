<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20230503143148 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE application_user (id INT AUTO_INCREMENT NOT NULL, role_id INT NOT NULL, plan_id INT DEFAULT NULL, company_id INT DEFAULT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, first_name VARCHAR(80) DEFAULT NULL, last_name VARCHAR(80) DEFAULT NULL, nationality VARCHAR(80) DEFAULT NULL, birth_date DATE DEFAULT NULL, phone_number VARCHAR(80) NOT NULL, address VARCHAR(255) DEFAULT NULL, pic_url VARCHAR(255) DEFAULT NULL, roles LONGTEXT NOT NULL COMMENT \'(DC2Type:json)\', email VARCHAR(80) NOT NULL, password VARCHAR(255) NOT NULL, plain_password VARCHAR(255) DEFAULT NULL, registration_state VARCHAR(80) NOT NULL, otp_code VARCHAR(5) DEFAULT NULL, otp_code_requested_at DATETIME DEFAULT NULL, otp_code_verified_at DATETIME DEFAULT NULL, enabled TINYINT(1) NOT NULL, INDEX IDX_7A7FBEC1D60322AC (role_id), INDEX IDX_7A7FBEC1E899029B (plan_id), INDEX IDX_7A7FBEC1979B1AD6 (company_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE application_user_application_user (application_user_source INT NOT NULL, application_user_target INT NOT NULL, INDEX IDX_543980799313492C (application_user_source), INDEX IDX_543980798AF619A3 (application_user_target), PRIMARY KEY(application_user_source, application_user_target)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE bank_information (id INT AUTO_INCREMENT NOT NULL, owner_id INT NOT NULL, iban VARCHAR(80) NOT NULL, bic VARCHAR(80) NOT NULL, bank_name VARCHAR(80) DEFAULT NULL, INDEX IDX_BF2AEFC7E3C61F9 (owner_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE company (id INT AUTO_INCREMENT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, name VARCHAR(80) NOT NULL, department_name VARCHAR(80) DEFAULT NULL, sub_department_name VARCHAR(80) DEFAULT NULL, national_unique_number VARCHAR(80) NOT NULL, contact_fullname VARCHAR(80) DEFAULT NULL, contact_fullname2 VARCHAR(80) DEFAULT NULL, mail_address VARCHAR(80) NOT NULL, mail_address2 VARCHAR(80) DEFAULT NULL, phone_number VARCHAR(80) DEFAULT NULL, phone_number2 VARCHAR(80) DEFAULT NULL, address VARCHAR(255) NOT NULL, city VARCHAR(80) DEFAULT NULL, country VARCHAR(80) DEFAULT NULL, website_link VARCHAR(80) DEFAULT NULL, linked_in_link VARCHAR(80) DEFAULT NULL, facebook_link VARCHAR(80) DEFAULT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE contract_type (id INT AUTO_INCREMENT NOT NULL, code VARCHAR(80) NOT NULL, name VARCHAR(80) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE document (id INT AUTO_INCREMENT NOT NULL, owner_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, title VARCHAR(80) NOT NULL, url VARCHAR(255) DEFAULT NULL, content VARCHAR(255) DEFAULT NULL, INDEX IDX_D8698A767E3C61F9 (owner_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE job_application (id INT AUTO_INCREMENT NOT NULL, job_offer_id INT NOT NULL, applicant_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, state VARCHAR(80) NOT NULL, INDEX IDX_C737C6883481D195 (job_offer_id), INDEX IDX_C737C68897139001 (applicant_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE job_application_document (job_application_id INT NOT NULL, document_id INT NOT NULL, INDEX IDX_F6C49621AC7A5A08 (job_application_id), INDEX IDX_F6C49621C33F7837 (document_id), PRIMARY KEY(job_application_id, document_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE job_offer (id INT AUTO_INCREMENT NOT NULL, category_id INT NOT NULL, profession_id INT NOT NULL, contract_type_id INT NOT NULL, owner_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, title VARCHAR(255) NOT NULL, description LONGTEXT NOT NULL, salary DOUBLE PRECISION NOT NULL, from_date DATE NOT NULL, to_date DATE NOT NULL, address VARCHAR(255) DEFAULT NULL, lat DOUBLE PRECISION DEFAULT NULL, lng DOUBLE PRECISION DEFAULT NULL, INDEX IDX_288A3A4E12469DE2 (category_id), INDEX IDX_288A3A4EFDEF8996 (profession_id), INDEX IDX_288A3A4ECD1DF15B (contract_type_id), INDEX IDX_288A3A4E7E3C61F9 (owner_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE job_offer_tag (job_offer_id INT NOT NULL, tag_id INT NOT NULL, INDEX IDX_E80C390A3481D195 (job_offer_id), INDEX IDX_E80C390ABAD26311 (tag_id), PRIMARY KEY(job_offer_id, tag_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE job_offer_category (id INT AUTO_INCREMENT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, name VARCHAR(80) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE payment (id INT AUTO_INCREMENT NOT NULL, payment_information_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, amount DOUBLE PRECISION NOT NULL, payment_type VARCHAR(80) NOT NULL, INDEX IDX_6D28840DCBD88B05 (payment_information_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE payment_information (id INT AUTO_INCREMENT NOT NULL, owner_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, credit_card_number VARCHAR(80) NOT NULL, cvv VARCHAR(3) NOT NULL, INDEX IDX_DFF5C8597E3C61F9 (owner_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE plan (id INT AUTO_INCREMENT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, name VARCHAR(80) NOT NULL, price DOUBLE PRECISION NOT NULL, conditions LONGTEXT NOT NULL, unsubscription_conditions LONGTEXT NOT NULL, description LONGTEXT NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE profession (id INT AUTO_INCREMENT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, name VARCHAR(80) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE report (id INT AUTO_INCREMENT NOT NULL, author_id INT NOT NULL, reported_user_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, reason VARCHAR(255) NOT NULL, INDEX IDX_C42F7784F675F31B (author_id), INDEX IDX_C42F7784E7566E (reported_user_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE role (id INT AUTO_INCREMENT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, code VARCHAR(80) NOT NULL, name VARCHAR(80) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE share_group (id INT AUTO_INCREMENT NOT NULL, owner_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, name VARCHAR(255) NOT NULL, INDEX IDX_E4AEA8287E3C61F9 (owner_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE share_group_application_user (share_group_id INT NOT NULL, application_user_id INT NOT NULL, INDEX IDX_1EEF86577AF6A82 (share_group_id), INDEX IDX_1EEF86574CD0D6A6 (application_user_id), PRIMARY KEY(share_group_id, application_user_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE tag (id INT AUTO_INCREMENT NOT NULL, owner_id INT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME DEFAULT NULL, name VARCHAR(80) NOT NULL, INDEX IDX_389B7837E3C61F9 (owner_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE application_user ADD CONSTRAINT FK_7A7FBEC1D60322AC FOREIGN KEY (role_id) REFERENCES role (id)');
        $this->addSql('ALTER TABLE application_user ADD CONSTRAINT FK_7A7FBEC1E899029B FOREIGN KEY (plan_id) REFERENCES plan (id)');
        $this->addSql('ALTER TABLE application_user ADD CONSTRAINT FK_7A7FBEC1979B1AD6 FOREIGN KEY (company_id) REFERENCES company (id)');
        $this->addSql('ALTER TABLE application_user_application_user ADD CONSTRAINT FK_543980799313492C FOREIGN KEY (application_user_source) REFERENCES application_user (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE application_user_application_user ADD CONSTRAINT FK_543980798AF619A3 FOREIGN KEY (application_user_target) REFERENCES application_user (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE bank_information ADD CONSTRAINT FK_BF2AEFC7E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE document ADD CONSTRAINT FK_D8698A767E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE job_application ADD CONSTRAINT FK_C737C6883481D195 FOREIGN KEY (job_offer_id) REFERENCES job_offer (id)');
        $this->addSql('ALTER TABLE job_application ADD CONSTRAINT FK_C737C68897139001 FOREIGN KEY (applicant_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE job_application_document ADD CONSTRAINT FK_F6C49621AC7A5A08 FOREIGN KEY (job_application_id) REFERENCES job_application (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE job_application_document ADD CONSTRAINT FK_F6C49621C33F7837 FOREIGN KEY (document_id) REFERENCES document (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE job_offer ADD CONSTRAINT FK_288A3A4E12469DE2 FOREIGN KEY (category_id) REFERENCES job_offer_category (id)');
        $this->addSql('ALTER TABLE job_offer ADD CONSTRAINT FK_288A3A4EFDEF8996 FOREIGN KEY (profession_id) REFERENCES profession (id)');
        $this->addSql('ALTER TABLE job_offer ADD CONSTRAINT FK_288A3A4ECD1DF15B FOREIGN KEY (contract_type_id) REFERENCES contract_type (id)');
        $this->addSql('ALTER TABLE job_offer ADD CONSTRAINT FK_288A3A4E7E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE job_offer_tag ADD CONSTRAINT FK_E80C390A3481D195 FOREIGN KEY (job_offer_id) REFERENCES job_offer (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE job_offer_tag ADD CONSTRAINT FK_E80C390ABAD26311 FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE payment ADD CONSTRAINT FK_6D28840DCBD88B05 FOREIGN KEY (payment_information_id) REFERENCES payment_information (id)');
        $this->addSql('ALTER TABLE payment_information ADD CONSTRAINT FK_DFF5C8597E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE report ADD CONSTRAINT FK_C42F7784F675F31B FOREIGN KEY (author_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE report ADD CONSTRAINT FK_C42F7784E7566E FOREIGN KEY (reported_user_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE share_group ADD CONSTRAINT FK_E4AEA8287E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE share_group_application_user ADD CONSTRAINT FK_1EEF86577AF6A82 FOREIGN KEY (share_group_id) REFERENCES share_group (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE share_group_application_user ADD CONSTRAINT FK_1EEF86574CD0D6A6 FOREIGN KEY (application_user_id) REFERENCES application_user (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE tag ADD CONSTRAINT FK_389B7837E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE application_user DROP FOREIGN KEY FK_7A7FBEC1D60322AC');
        $this->addSql('ALTER TABLE application_user DROP FOREIGN KEY FK_7A7FBEC1E899029B');
        $this->addSql('ALTER TABLE application_user DROP FOREIGN KEY FK_7A7FBEC1979B1AD6');
        $this->addSql('ALTER TABLE application_user_application_user DROP FOREIGN KEY FK_543980799313492C');
        $this->addSql('ALTER TABLE application_user_application_user DROP FOREIGN KEY FK_543980798AF619A3');
        $this->addSql('ALTER TABLE bank_information DROP FOREIGN KEY FK_BF2AEFC7E3C61F9');
        $this->addSql('ALTER TABLE document DROP FOREIGN KEY FK_D8698A767E3C61F9');
        $this->addSql('ALTER TABLE job_application DROP FOREIGN KEY FK_C737C6883481D195');
        $this->addSql('ALTER TABLE job_application DROP FOREIGN KEY FK_C737C68897139001');
        $this->addSql('ALTER TABLE job_application_document DROP FOREIGN KEY FK_F6C49621AC7A5A08');
        $this->addSql('ALTER TABLE job_application_document DROP FOREIGN KEY FK_F6C49621C33F7837');
        $this->addSql('ALTER TABLE job_offer DROP FOREIGN KEY FK_288A3A4E12469DE2');
        $this->addSql('ALTER TABLE job_offer DROP FOREIGN KEY FK_288A3A4EFDEF8996');
        $this->addSql('ALTER TABLE job_offer DROP FOREIGN KEY FK_288A3A4ECD1DF15B');
        $this->addSql('ALTER TABLE job_offer DROP FOREIGN KEY FK_288A3A4E7E3C61F9');
        $this->addSql('ALTER TABLE job_offer_tag DROP FOREIGN KEY FK_E80C390A3481D195');
        $this->addSql('ALTER TABLE job_offer_tag DROP FOREIGN KEY FK_E80C390ABAD26311');
        $this->addSql('ALTER TABLE payment DROP FOREIGN KEY FK_6D28840DCBD88B05');
        $this->addSql('ALTER TABLE payment_information DROP FOREIGN KEY FK_DFF5C8597E3C61F9');
        $this->addSql('ALTER TABLE report DROP FOREIGN KEY FK_C42F7784F675F31B');
        $this->addSql('ALTER TABLE report DROP FOREIGN KEY FK_C42F7784E7566E');
        $this->addSql('ALTER TABLE share_group DROP FOREIGN KEY FK_E4AEA8287E3C61F9');
        $this->addSql('ALTER TABLE share_group_application_user DROP FOREIGN KEY FK_1EEF86577AF6A82');
        $this->addSql('ALTER TABLE share_group_application_user DROP FOREIGN KEY FK_1EEF86574CD0D6A6');
        $this->addSql('ALTER TABLE tag DROP FOREIGN KEY FK_389B7837E3C61F9');
        $this->addSql('DROP TABLE application_user');
        $this->addSql('DROP TABLE application_user_application_user');
        $this->addSql('DROP TABLE bank_information');
        $this->addSql('DROP TABLE company');
        $this->addSql('DROP TABLE contract_type');
        $this->addSql('DROP TABLE document');
        $this->addSql('DROP TABLE job_application');
        $this->addSql('DROP TABLE job_application_document');
        $this->addSql('DROP TABLE job_offer');
        $this->addSql('DROP TABLE job_offer_tag');
        $this->addSql('DROP TABLE job_offer_category');
        $this->addSql('DROP TABLE payment');
        $this->addSql('DROP TABLE payment_information');
        $this->addSql('DROP TABLE plan');
        $this->addSql('DROP TABLE profession');
        $this->addSql('DROP TABLE report');
        $this->addSql('DROP TABLE role');
        $this->addSql('DROP TABLE share_group');
        $this->addSql('DROP TABLE share_group_application_user');
        $this->addSql('DROP TABLE tag');
    }
}
