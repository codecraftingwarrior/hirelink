<?php

namespace App\Repository;

use App\Entity\JobOffer;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Common\Collections\Criteria;
use Doctrine\ORM\Query\Expr\Join;
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

        $qb1 = $this->createQueryBuilder('o1');

        $qb->select('o');

        if (isset($criteria['latitude']) && isset($criteria['longitude']) && isset($criteria['maxDistance'])) {
            $qb->where(
                $qb->expr()->lte(
                    'ROUND(DISTANCE(:latitude, :longitude, o.lat, o.lng)) * 1.609344',
                    ':maxDistance'
                )
            )->setParameter('longitude', $criteria['latitude'])
                ->setParameter('latitude', $criteria['longitude'])
                ->setParameter('maxDistance', $criteria['maxDistance']);
        }

        if (isset($criteria['companyID']) && is_array($criteria['companyID'])) {
            $qb
                ->join('o.owner', 'u')
                ->join('u.company', 'c', Join::WITH, $qb->expr()->in('c.id', ':companyIDs'))
                ->setParameter('companyIDs', $criteria['companyID']);
        }

        if (isset($criteria['professionID']) && is_array($criteria['professionID'])) {
            $qb->join('o.profession', 'p', Join::WITH, $qb->expr()->in('p.id', ':professionIDs'))
                ->setParameter('professionIDs', $criteria['professionID']);
        }

        if (isset($criteria['minSalary']) && is_numeric($criteria['minSalary'])) {
            $qb
                ->andWhere('o.minSalary >= :minSalary')
                ->setParameter('minSalary', $criteria['minSalary']);
        }

        if (isset($criteria['maxSalary']) && is_numeric($criteria['maxSalary'])) {
            $qb
                ->andWhere(
                    $qb->expr()->gte('o.maxSalary', ':maxSalary')
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
