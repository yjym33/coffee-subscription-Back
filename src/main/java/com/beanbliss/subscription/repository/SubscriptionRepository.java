package com.beanbliss.subscription.repository;

import com.beanbliss.subscription.entity.Subscription;
import com.beanbliss.subscription.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser(User user);
}


