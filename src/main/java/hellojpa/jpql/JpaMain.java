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
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // 엔티티타입 프로젝션
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            // 임베디드타입 프로젝션
            List<Address> resultList = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();

            // 스칼라타입 프로젝션
            List<Object[]> objects = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();

            // 스칼라타입 프로젝션
            List<MemberDTO> memberDtos = em.createQuery("select new hellojpa.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            Object[] results = objects.get(0);
            System.out.println("username = " + results[0]);
            System.out.println("age = " + results[1]);

            MemberDTO memberDTO = memberDtos.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());

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
