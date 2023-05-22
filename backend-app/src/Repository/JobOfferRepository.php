<?php

namespace App\Repository;

use App\Entity\JobOffer;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\ORM\NonUniqueResultException;
use Doctrine\Common\Collections\Criteria;
use Doctrine\ORM\Query\Expr\Join;
use Doctrine\ORM\Query\Expr\OrderBy;
use Doctrine\ORM\Query\QueryException;
use Doctrine\Persistence\ManagerRegistry;
use ApiPlatform\Doctrine\Orm\Paginator;
use Doctrine\ORM\Tools\Pagination\Paginator as DoctrinePaginator;


/**
 * @extends ServiceEntityRepository<JobOffer>
 *
 * @method JobOffer|null find($id, $lockMode = null, $lockVersion = null)
 * @method JobOffer|null findOneBy(array $criteria, array $orderBy = null)
 * @method JobOffer[]    findAll()
 * @method JobOffer[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class JobOfferRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, JobOffer::class);
    }

    public function save(JobOffer $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function remove(JobOffer $entity, bool $flush = false): void
    {
        $this->getEntityManager()->remove($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    /**
     * @throws QueryException
     */
    public function filter(int $page = 1, $criteria = []): Paginator
    {
        $firstResult = ($page - 1) * JOB_OFFER_ITEM_PER_PAGE;
        $qb = $this->createQueryBuilder('o');


        $qb->select('o');

        if (isset($criteria['userID'])) {
            $qb->where(
                $qb->expr()->notIn(
                    'o',
                    'SELECT jo
                    FROM App\Entity\JobApplication ja
                    JOIN ja.jobOffer jo
                    WHERE ja.applicant = :applicant'
                )
            )->setParameter('applicant', $criteria['userID']);
        }

        if (isset($criteria['jobTitle'])) {
            $qb->andWhere(
                $qb->expr()->like(
                    'o.title',
                    ':jobTitle'
                )
            )->setParameter('jobTitle', "%{$criteria['jobTitle']}%");
        }


        if (isset($criteria['latitude']) && isset($criteria['longitude']) && isset($criteria['maxDistance'])) {
            $qb->andWhere(
                $qb->expr()->lte(
                    'ROUND(DISTANCE(:latitude, :longitude, o.lat, o.lng)) * 1.609344',
                    ':maxDistance'
                )
            )->setParameter('longitude', $criteria['latitude'])
                ->setParameter('latitude', $criteria['longitude'])
                ->setParameter('maxDistance', $criteria['maxDistance']);
        }

        if (isset($criteria['companyIDs']) && is_array($criteria['companyIDs'])) {
            $qb
                ->join('o.owner', 'u')
                ->join('u.company', 'c', Join::WITH, $qb->expr()->in('c.id', ':companyIDs'))
                ->setParameter('companyIDs', $criteria['companyIDs']);
        }

        if (isset($criteria['professionIDs']) && is_array($criteria['professionIDs'])) {
            $qb->join('o.profession', 'p', Join::WITH, $qb->expr()->in('p.id', ':professionIDs'))
                ->setParameter('professionIDs', $criteria['professionIDs']);
        }

        if (isset($criteria['minSalary'])) {
            $qb
                ->andWhere('o.minSalary >= :minSalary')
                ->setParameter('minSalary', $criteria['minSalary']);
        }

        if (isset($criteria['maxSalary'])) {
            $qb
                ->andWhere(
                    $qb->expr()->lte('o.maxSalary', ':maxSalary')
                )->setParameter('maxSalary', $criteria['maxSalary']);
        }

        if (isset($criteria['fromDate']) && is_string($criteria['fromDate'])) {
            $qb
                ->andWhere(
                    $qb->expr()->lte('o.fromDate', ':fromDate')
                )->setParameter('fromDate', $criteria['fromDate']);
        }

        if (isset($criteria['toDate']) && is_string($criteria['toDate'])) {
            $qb
                ->andWhere(
                    $qb->expr()->gte('o.toDate', ':toDate')
                )->setParameter('toDate', $criteria['toDate']);
        }

        if (isset($criteria['city']) && is_array($criteria['city'])) {
            $qb
                ->andWhere(
                    $qb->expr()->in('o.city', ':cities')
                )
                ->setParameter('cities', $criteria['city']);
        }

        $qb->orderBy('o.createdAt', 'DESC');


        $criteriaPaging = Criteria::create()
            ->setFirstResult($firstResult)
            ->setMaxResults(JOB_OFFER_ITEM_PER_PAGE);
        $qb->addCriteria($criteriaPaging);


        $doctrinePaginator = new DoctrinePaginator($qb);

        return new Paginator($doctrinePaginator);
    }

    public function findByOwnerPaginated(int $page = 1, $criteria = []): Paginator {
        $firstResult = ($page - 1) * 5;

        $qb = $this
            ->createQueryBuilder('jo');

        $qb->where(
            $qb
                ->expr()
                ->eq('jo.owner', $criteria['ownerID'])
        )->orderBy('jo.createdAt', 'DESC');

        $criteriaPaging = Criteria::create()
            ->setFirstResult($firstResult)
            ->setMaxResults(JOB_OFFER_ITEM_PER_PAGE);
        $qb->addCriteria($criteriaPaging);


        $doctrinePaginator = new DoctrinePaginator($qb);

        return new Paginator($doctrinePaginator);
    }

//    /**
//     * @return JobOffer[] Returns an array of JobOffer objects
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

//    public function findOneBySomeField($value): ?JobOffer
//    {
//        return $this->createQueryBuilder('j')
//            ->andWhere('j.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
