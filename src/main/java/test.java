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
import com.myweb.utils.GeneralUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author huaquangdat
 */
public class test {

    public static void main(String[] args) {

        Dotenv.configure().load();
        private Dotenv dotenv=Dotenv.load();

    }
}
