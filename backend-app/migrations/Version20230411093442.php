<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20230411093442 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE payment_information ADD owner_id INT NOT NULL');
        $this->addSql('ALTER TABLE payment_information ADD CONSTRAINT FK_DFF5C8597E3C61F9 FOREIGN KEY (owner_id) REFERENCES application_user (id)');
        $this->addSql('CREATE INDEX IDX_DFF5C8597E3C61F9 ON payment_information (owner_id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE payment_information DROP FOREIGN KEY FK_DFF5C8597E3C61F9');
        $this->addSql('DROP INDEX IDX_DFF5C8597E3C61F9 ON payment_information');
        $this->addSql('ALTER TABLE payment_information DROP owner_id');
    }
}
