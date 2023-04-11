<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20230411094400 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE bank_information ADD owner_id INT NOT NULL, ADD bank_name VARCHAR(80) DEFAULT NULL');
        $this->addSql('ALTER TABLE bank_information ADD CONSTRAINT FK_BF2AEFC7E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('CREATE INDEX IDX_BF2AEFC7E3C61F9 ON bank_information (owner_id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE bank_information DROP FOREIGN KEY FK_BF2AEFC7E3C61F9');
        $this->addSql('DROP INDEX IDX_BF2AEFC7E3C61F9 ON bank_information');
        $this->addSql('ALTER TABLE bank_information DROP owner_id, DROP bank_name');
    }
}
