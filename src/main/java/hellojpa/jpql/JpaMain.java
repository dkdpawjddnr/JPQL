package hellojpa.jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);

            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            //JPQL 서브쿼리의 한계 : FROM 절의 서브 쿼리는 불가능
            // -> 해결방법으로는 조인으로 풀 수 있으면 풀어서 해결하기.
            String query = "select m from (select m.age from Member m) as mm";
            List<Member> result = em.createQuery(query, Member.class)
                            .getResultList();

            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
