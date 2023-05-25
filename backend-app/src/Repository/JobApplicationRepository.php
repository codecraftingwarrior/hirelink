<?php

namespace App\Repository;

use ApiPlatform\Doctrine\Orm\Paginator;
use App\Entity\JobApplication;
use App\Enum\JobApplicationState;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Common\Collections\Criteria;
use Doctrine\ORM\Tools\Pagination\Paginator as DoctrinePaginator;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<JobApplication>
 *
 * @method JobApplication|null find($id, $lockMode = null, $lockVersion = null)
 * @method JobApplication|null findOneBy(array $criteria, array $orderBy = null)
 * @method JobApplication[]    findAll()
 * @method JobApplication[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class JobApplicationRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, JobApplication::class);
    }

    public function save(JobApplication $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function remove(JobApplication $entity, bool $flush = false): void
    {
        $this->getEntityManager()->remove($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function filter(int $page = 1, $criteria = [])
    {
        $firstResult = ($page - 1) * JOB_APPLICATION_ITEM_PER_PAGE;

        $qb = $this->createQueryBuilder('ja');

        $qb->where('ja.id IS NOT NULL');

        if (isset($criteria['userID'])) {
            $qb->andWhere(
                $qb->expr()->eq(
                    'ja.applicant',
                    ':applicant'
                )
            )->setParameter('applicant', $criteria['userID']);
        }

        if (isset($criteria['state'])) {
            $qb->andWhere(
                $qb->expr()->eq(
                    'ja.state',
                    ':state'
                )
            )->setParameter('state', $criteria['state']);
        }

        if (isset($criteria['searchQuery'])) {
            $qb
                ->join('ja.jobOffer', 'jo')
                ->join('jo.owner', 'u')
                ->join('u.company', 'c');


            $qb->andWhere(
                $qb->expr()->orX(
                    $qb->expr()->like('jo.title', ':searchQuery'),
                    $qb->expr()->like('c.name', ':searchQuery'),
                    $qb->expr()->like('jo.address', ':searchQuery')
                )
            )->setParameter('searchQuery', '%' . $criteria['searchQuery'] . '%');
        }

        $qb->orderBy('ja.createdAt', 'DESC');

        $criteriaPaging = Criteria::create()
            ->setFirstResult($firstResult)
            ->setMaxResults(JOB_APPLICATION_ITEM_PER_PAGE);
        $qb->addCriteria($criteriaPaging);

        $doctrinePaginator = new DoctrinePaginator($qb);

        return new Paginator($doctrinePaginator);
    }

    public function findElementsWithHighestFrequency(int $limit = 3)
    {
        return $this->createQueryBuilder('ja')
            ->select('p.name, COUNT(ja.jobOffer) AS frequency')
            ->leftJoin('ja.jobOffer', 'jo')
            ->leftJoin('jo.profession', 'p')
            ->groupBy('p.name')
            ->orderBy('frequency', 'DESC')
            ->setMaxResults($limit)
            ->getQuery()
            ->getResult();
    }

//    /**
//     * @return JobApplication[] Returns an array of JobApplication objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('j')
//            ->andWhere('j.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('j.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?JobApplication
//    {
//        return $this->createQueryBuilder('j')
//            ->andWhere('j.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
