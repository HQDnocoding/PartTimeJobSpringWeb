/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import com.myweb.pojo.Application;
import com.myweb.pojo.Candidate;
import com.myweb.pojo.CandidateReview;
import com.myweb.pojo.Company;
import com.myweb.pojo.CompanyReview;
import com.myweb.pojo.Day;
import com.myweb.pojo.DayJob;
import com.myweb.pojo.Follow;
import com.myweb.pojo.ImageWorkplace;
import com.myweb.pojo.Job;
import com.myweb.pojo.Major;
import com.myweb.pojo.MarjorJob;
import com.myweb.pojo.User;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author huaquangdat
 */
public class test {

    public static void main(String[] args) {

        Configuration conf = new Configuration();
        Properties props = new Properties();
        props.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        props.put(Environment.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver");
        props.put(Environment.JAKARTA_JDBC_URL, "jdbc:mysql://localhost:3306/jobdb?zeroDateTimeBehavior=CONVERT_TO_NULL");
        props.put(Environment.JAKARTA_JDBC_USER, "root");
        props.put(Environment.JAKARTA_JDBC_PASSWORD, "Admin@123");
        props.put(Environment.SHOW_SQL, "true");
        
        
        conf.setProperties(props);
        
        conf.addAnnotatedClass(Candidate.class);
        conf.addAnnotatedClass(Company.class);
        conf.addAnnotatedClass(User.class);
        conf.addAnnotatedClass(Application.class);
        conf.addAnnotatedClass(Day.class);
        conf.addAnnotatedClass(DayJob.class);
        conf.addAnnotatedClass(Job.class);
        conf.addAnnotatedClass(Follow.class);
        conf.addAnnotatedClass(Major.class);
        conf.addAnnotatedClass(MarjorJob.class);
        conf.addAnnotatedClass(CompanyReview.class);
        conf.addAnnotatedClass(CandidateReview.class);
        conf.addAnnotatedClass(ImageWorkplace.class);
        
        Company c=new Company();
        
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
        
        SessionFactory  FACTORY = conf.buildSessionFactory(serviceRegistry);
        
        Session session =FACTORY.openSession();
        
        Transaction tran=session.beginTransaction();
        
        
        c.setUsername("Congty8");
        c.setPassword("123456");
        c.setAvatar("");
        c.setName("ABC");
        c.setCity("HCM");
        c.setDistrict("Q1");
        c.setEmail("10yBd@gmail.com");
        c.setFullAddress("HCM,Q1");
        c.setTaxCode("123d1A45");
        c.setRegisterDate(LocalDateTime.now());
        c.setRole("ROLE_COMPANY");
        c.setStatus("pending");
        
        
        session.persist(c);
        tran.commit();
        session.close();
        FACTORY.close();
    }
}
