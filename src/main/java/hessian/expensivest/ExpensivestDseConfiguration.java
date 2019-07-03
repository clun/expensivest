package hessian.expensivest;

import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.dse.driver.api.core.DseSessionBuilder;
import hessian.expensivest.mapper.ExpenseDao;
import hessian.expensivest.mapper.ExpenseMapper;
import hessian.expensivest.mapper.ExpenseMapperBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;


@Configuration
public class ExpensivestDseConfiguration {
    @Value("${dse.contactPoints}")
    public String contactPoints;

    @Value("${dse.port}")
    private int port = 9042;

    @Value("${dse.keyspace}")
    private String keyspace = "expensivest";

    @Value("dc1")
    private String localDatacenter;

    public String getContactPoints() {
        return contactPoints;
    }

    public String getKeyspaceName() {
        return keyspace;
    }

    public int getPort() {
        return port;
    }


    @Bean
    public DseSession dseSession() {
        DseSessionBuilder dseSessionBuilder = DseSession.builder().withLocalDatacenter(localDatacenter);
        for (String s : contactPoints.split(","))
                dseSessionBuilder.addContactPoint(InetSocketAddress.createUnresolved(s, port));
        return dseSessionBuilder.build();
    }

    @Bean
    public ExpenseMapper expenseMapper(DseSession dseSession) {
        return new ExpenseMapperBuilder(dseSession).build();
    }

    @Bean
    public ExpenseDao expenseDao(ExpenseMapper expenseMapper) {
        return expenseMapper.expenseDao(keyspace, "expenses");
    }

}
