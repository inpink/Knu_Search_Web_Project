package knusearch.clear.repository;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class DataSourceTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext context;

    @Test
    public void findAllByEncryptedMenuSequenceAndEncryptedMenuBoardSequence() throws SQLException {
        String ddlAuto = context.getEnvironment().getProperty("spring.jpa.hibernate.ddl-auto");
        System.out.println("DataSourceTest ddl-auto: " + ddlAuto); // ApplicationContext는 내가 사용하는 application-test.properties의 값이 나옴!!
        System.out.println(context.getEnvironment().getProperty("spring.datasource.username"));

        System.out.println(dataSource.toString());
        System.out.println(dataSource.getConnection().getMetaData());
    }
}
