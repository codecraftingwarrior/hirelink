<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20230503151952 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE job_application_document (job_application_id INT NOT NULL, document_id INT NOT NULL, INDEX IDX_F6C49621AC7A5A08 (job_application_id), INDEX IDX_F6C49621C33F7837 (document_id), PRIMARY KEY(job_application_id, document_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE job_application_document ADD CONSTRAINT FK_F6C49621AC7A5A08 FOREIGN KEY (job_application_id) REFERENCES job_application (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE job_application_document ADD CONSTRAINT FK_F6C49621C33F7837 FOREIGN KEY (document_id) REFERENCES document (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE job_offer CHANGE category_id category_id INT NOT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE job_application_document DROP FOREIGN KEY FK_F6C49621AC7A5A08');
        $this->addSql('ALTER TABLE job_application_document DROP FOREIGN KEY FK_F6C49621C33F7837');
        $this->addSql('DROP TABLE job_application_document');
        $this->addSql('ALTER TABLE job_offer CHANGE category_id category_id INT DEFAULT NULL');
    }
}
