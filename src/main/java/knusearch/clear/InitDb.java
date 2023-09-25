package knusearch.clear;

import knusearch.clear.domain.Search;
import knusearch.clear.domain.SearchSite;
import knusearch.clear.domain.content.Content;
import knusearch.clear.domain.content.ContentMain;
import knusearch.clear.service.CrawlService;
import knusearch.clear.service.content.ContentMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;

import java.util.Date;


@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final CrawlService crawlService;


        public void dbInit1() {
            System.out.println("Init1" + this.getClass());

            SearchSite searchSite1=SearchSite.createSearchSite("searchSite1");
            SearchSite searchSite2=SearchSite.createSearchSite("searchSite2");
            Search search = Search.createSearch(searchSite1,searchSite2);
            em.persist(search); //★여기는 초기 예시라 그렇고, 실제는 클라이언트에 의해 동적으로 되므로,
            //DB는 repository를 이용해 조작한다.

            ContentMain contentMain= ContentMain.createContentMain(
                    false,"ea2c9f2a785ae43e520c322896013dfe","1e093662ef168ebe2afb304b031a0e43",
                    "title임","본문임 ","imageLink~~",new Date());
            em.persist(contentMain);

            Content content= Content.createContent("contentTitle","content본문",".",new Date());
            em.persist(content);


            /*Member member = createMember("userA", "서울", "1", "1111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);*/
        }


        public void dbInit2() {
            System.out.println("Init2" + this.getClass());

            SearchSite searchSite3=SearchSite.createSearchSite("searchSite3");
            SearchSite searchSite4=SearchSite.createSearchSite("searchSite4");
            Search search = Search.createSearch(searchSite3,searchSite4);
            em.persist(search);

            /*Member member = createMember("userA", "서울", "1", "1111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);*/
        }


    }
}

