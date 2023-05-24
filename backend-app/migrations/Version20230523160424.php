<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20230523160424 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE notifications (id INT AUTO_INCREMENT NOT NULL, user_id INT NOT NULL, job_application_id INT NOT NULL, title VARCHAR(255) NOT NULL, creation_date DATE NOT NULL, INDEX IDX_6000B0D3A76ED395 (user_id), INDEX IDX_6000B0D3AC7A5A08 (job_application_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE notifications ADD CONSTRAINT FK_6000B0D3A76ED395 FOREIGN KEY (user_id) REFERENCES application_user (id)');
        $this->addSql('ALTER TABLE notifications ADD CONSTRAINT FK_6000B0D3AC7A5A08 FOREIGN KEY (job_application_id) REFERENCES job_application (id)');
        $this->addSql('ALTER TABLE document CHANGE url file_name VARCHAR(255) DEFAULT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE notifications DROP FOREIGN KEY FK_6000B0D3A76ED395');
        $this->addSql('ALTER TABLE notifications DROP FOREIGN KEY FK_6000B0D3AC7A5A08');
        $this->addSql('DROP TABLE notifications');
        $this->addSql('ALTER TABLE document CHANGE file_name url VARCHAR(255) DEFAULT NULL');
    }
}
