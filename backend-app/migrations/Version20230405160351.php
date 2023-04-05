<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20230405160351 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE document (id INT AUTO_INCREMENT NOT NULL, owner_id INT NOT NULL, title VARCHAR(80) NOT NULL, url VARCHAR(255) DEFAULT NULL, content VARCHAR(255) DEFAULT NULL, INDEX IDX_D8698A767E3C61F9 (owner_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE document ADD CONSTRAINT FK_D8698A767E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE application_user ADD role_id INT NOT NULL, ADD plan_id INT DEFAULT NULL, ADD company_id INT DEFAULT NULL');
        $this->addSql('ALTER TABLE application_user ADD CONSTRAINT FK_7A7FBEC1D60322AC FOREIGN KEY (role_id) REFERENCES role (id)');
        $this->addSql('ALTER TABLE application_user ADD CONSTRAINT FK_7A7FBEC1E899029B FOREIGN KEY (plan_id) REFERENCES plan (id)');
        $this->addSql('ALTER TABLE application_user ADD CONSTRAINT FK_7A7FBEC1979B1AD6 FOREIGN KEY (company_id) REFERENCES company (id)');
        $this->addSql('CREATE INDEX IDX_7A7FBEC1D60322AC ON application_user (role_id)');
        $this->addSql('CREATE INDEX IDX_7A7FBEC1E899029B ON application_user (plan_id)');
        $this->addSql('CREATE INDEX IDX_7A7FBEC1979B1AD6 ON application_user (company_id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE document DROP FOREIGN KEY FK_D8698A767E3C61F9');
        $this->addSql('DROP TABLE document');
        $this->addSql('ALTER TABLE application_user DROP FOREIGN KEY FK_7A7FBEC1D60322AC');
        $this->addSql('ALTER TABLE application_user DROP FOREIGN KEY FK_7A7FBEC1E899029B');
        $this->addSql('ALTER TABLE application_user DROP FOREIGN KEY FK_7A7FBEC1979B1AD6');
        $this->addSql('DROP INDEX IDX_7A7FBEC1D60322AC ON application_user');
        $this->addSql('DROP INDEX IDX_7A7FBEC1E899029B ON application_user');
        $this->addSql('DROP INDEX IDX_7A7FBEC1979B1AD6 ON application_user');
        $this->addSql('ALTER TABLE application_user DROP role_id, DROP plan_id, DROP company_id');
    }
}
