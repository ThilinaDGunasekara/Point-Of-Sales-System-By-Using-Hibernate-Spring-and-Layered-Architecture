package lk.ijse.dep.pos.db;

import lk.ijse.dep.crypto.DEPCrypt;
import lk.ijse.dep.pos.entity.Customer;
import lk.ijse.dep.pos.entity.Item;
import lk.ijse.dep.pos.entity.Order;
import lk.ijse.dep.pos.entity.OrderDetail;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {

    private static SessionFactory sessionFactory=buildSessionFactory();
    private static String username;
    private static String password;
    private static String db;
    private static String port;
    private static String host;

    private static SessionFactory buildSessionFactory(){

        File file = new File("layered-pos-hibernate/resources/application.properties");
        Properties properties = new Properties();

        try(FileInputStream fis = new FileInputStream(file);){
            properties.load(fis);

        }catch (Exception e){
            Logger.getLogger("lk.ijse.dep.pos.db").log(Level.SEVERE,null,e);

        }

        username= DEPCrypt.decode(properties.getProperty("hibernate.connection.username"),"dep4");
        password=DEPCrypt.decode(properties.getProperty("hibernate.connection.password"),"dep4");
        db=properties.getProperty("ijse.dep.db");
        port=properties.getProperty("ijse.dep.port");
        host=properties.getProperty("ijse.dep.ip");


        // 1
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .loadProperties(file)
                .applySetting("hibernate.connection.username", getUsername())
                .applySetting("hibernate.connection.password", getPassword())
                .build();

        //2
        Metadata metadata = new MetadataSources( standardRegistry )
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(OrderDetail.class)
                .getMetadataBuilder()
                .applyImplicitNamingStrategy( ImplicitNamingStrategyJpaCompliantImpl.INSTANCE )
                .build();

        //3
        return metadata.getSessionFactoryBuilder()
                .build();

    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getDb() {
        return db;
    }

    public static String getPort() {
        return port;
    }

    public static String getHost() {
        return host;
    }
}
