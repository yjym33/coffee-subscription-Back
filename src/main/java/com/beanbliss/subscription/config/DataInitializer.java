package com.beanbliss.subscription.config;

import com.beanbliss.subscription.entity.Product;
import com.beanbliss.subscription.entity.User;
import com.beanbliss.subscription.repository.ProductRepository;
import com.beanbliss.subscription.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            if (productRepository.count() == 0) {
                Product p1 = new Product();
                p1.setNameKey("ethiopianYirgacheffe");
                p1.setImage("/images/coffee/ethiopian-yirgacheffe.jpg");
                p1.setPrice(new BigDecimal("16.99"));
                productRepository.save(p1);

                Product p2 = new Product();
                p2.setNameKey("decafSumatra");
                p2.setImage("/images/coffee/decaf-sumatra.jpg");
                p2.setPrice(new BigDecimal("15.99"));
                productRepository.save(p2);

                Product p3 = new Product();
                p3.setNameKey("kenyanAa");
                p3.setImage("/images/coffee/kenyan-aa.jpg");
                p3.setPrice(new BigDecimal("17.99"));
                productRepository.save(p3);
            }
            userRepository.findByEmail("demo@beanbliss.com").orElseGet(() -> {
                User u = new User();
                u.setEmail("demo@beanbliss.com");
                u.setPasswordHash(passwordEncoder.encode("password123"));
                u.setName("Demo User");
                u.setAdmin(false);
                return userRepository.save(u);
            });
        };
    }
}


