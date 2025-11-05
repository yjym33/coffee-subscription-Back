package com.beanbliss.subscription.service;

import com.beanbliss.subscription.dto.SubscriptionDtos;
import com.beanbliss.subscription.entity.Product;
import com.beanbliss.subscription.entity.Subscription;
import com.beanbliss.subscription.entity.User;
import com.beanbliss.subscription.repository.ProductRepository;
import com.beanbliss.subscription.repository.SubscriptionRepository;
import com.beanbliss.subscription.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Subscription> listByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return subscriptionRepository.findByUser(user);
    }

    @Transactional
    public Subscription create(String email, SubscriptionDtos.CreateRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Product product = productRepository.findById(request.getProductId()).orElseThrow();
        Subscription sub = new Subscription();
        sub.setUser(user);
        sub.setProduct(product);
        sub.setFrequency(request.getFrequency());
        sub.setQuantity(request.getQuantity());
        sub.setStatus("active");
        sub.setNextDelivery(LocalDate.now().plusWeeks(2));
        sub.setLastDelivery(LocalDate.now());
        return subscriptionRepository.save(sub);
    }

    @Transactional
    public Subscription update(String email, SubscriptionDtos.UpdateRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Subscription sub = subscriptionRepository.findById(request.getId()).orElseThrow();
        if (!sub.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Forbidden");
        }
        if (request.getFrequency() != null) sub.setFrequency(request.getFrequency());
        if (request.getQuantity() != null) sub.setQuantity(request.getQuantity());
        if (request.getStatus() != null) sub.setStatus(request.getStatus());
        return sub;
    }

    @Transactional
    public void cancel(String email, Long id) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Subscription sub = subscriptionRepository.findById(id).orElseThrow();
        if (!sub.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Forbidden");
        }
        sub.setStatus("cancelled");
    }
}


