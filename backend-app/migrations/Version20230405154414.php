<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20230405154414 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE application_user (id INT AUTO_INCREMENT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL, first_name VARCHAR(80) DEFAULT NULL, last_name VARCHAR(80) DEFAULT NULL, nationality VARCHAR(80) DEFAULT NULL, birth_date DATE NOT NULL, phone_number VARCHAR(80) NOT NULL, address VARCHAR(255) DEFAULT NULL, pic_url VARCHAR(255) DEFAULT NULL, roles JSON NOT NULL, email VARCHAR(80) NOT NULL, password VARCHAR(255) NOT NULL, plain_password VARCHAR(255) NOT NULL, registration_state VARCHAR(80) NOT NULL, otp_code VARCHAR(5) DEFAULT NULL, otp_code_requested_at DATETIME DEFAULT NULL, enabled TINYINT(1) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE company (id INT AUTO_INCREMENT NOT NULL, name VARCHAR(80) NOT NULL, department_name VARCHAR(80) DEFAULT NULL, sub_department_name VARCHAR(80) DEFAULT NULL, national_unique_number VARCHAR(80) NOT NULL, contact_fullname VARCHAR(80) NOT NULL, contact_fullname2 VARCHAR(80) DEFAULT NULL, mail_address VARCHAR(80) NOT NULL, mail_address2 VARCHAR(80) DEFAULT NULL, phone_number VARCHAR(80) NOT NULL, phone_number2 VARCHAR(80) DEFAULT NULL, address VARCHAR(255) NOT NULL, city VARCHAR(80) DEFAULT NULL, country VARCHAR(80) DEFAULT NULL, website_link VARCHAR(80) DEFAULT NULL, linked_in_link VARCHAR(80) DEFAULT NULL, facebook_link VARCHAR(80) DEFAULT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE payment (id INT AUTO_INCREMENT NOT NULL, payment_information_id INT NOT NULL, amount DOUBLE PRECISION NOT NULL, payment_type VARCHAR(80) NOT NULL, INDEX IDX_6D28840DCBD88B05 (payment_information_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE payment_information (id INT AUTO_INCREMENT NOT NULL, credit_card_number VARCHAR(80) NOT NULL, cvv VARCHAR(3) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE plan (id INT AUTO_INCREMENT NOT NULL, name VARCHAR(80) NOT NULL, price DOUBLE PRECISION NOT NULL, conditions LONGTEXT NOT NULL, unsubscription_conditions LONGTEXT NOT NULL, description LONGTEXT NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE payment ADD CONSTRAINT FK_6D28840DCBD88B05 FOREIGN KEY (payment_information_id) REFERENCES payment_information (id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE payment DROP FOREIGN KEY FK_6D28840DCBD88B05');
        $this->addSql('DROP TABLE application_user');
        $this->addSql('DROP TABLE company');
        $this->addSql('DROP TABLE payment');
        $this->addSql('DROP TABLE payment_information');
        $this->addSql('DROP TABLE plan');
    }
}
