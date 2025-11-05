package com.beanbliss.subscription.controller;

import com.beanbliss.subscription.dto.SubscriptionDtos;
import com.beanbliss.subscription.entity.Subscription;
import com.beanbliss.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // For simplicity, we pass email via header X-User-Email since we didn't implement full JWT filter yet
    private String resolveEmail(@RequestHeader(value = "X-User-Email", required = false) String email,
                                @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (email != null && !email.isBlank()) return email;
        // In real case, parse JWT from Authorization header
        return "demo@beanbliss.com";
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> list(@RequestHeader(value = "X-User-Email", required = false) String email,
                                                   @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ResponseEntity.ok(subscriptionService.listByUser(resolveEmail(email, authorization)));
    }

    @PostMapping
    public ResponseEntity<Subscription> create(@RequestHeader(value = "X-User-Email", required = false) String email,
                                               @RequestHeader(value = "Authorization", required = false) String authorization,
                                               @Valid @RequestBody SubscriptionDtos.CreateRequest request) {
        return ResponseEntity.ok(subscriptionService.create(resolveEmail(email, authorization), request));
    }

    @PutMapping
    public ResponseEntity<Subscription> update(@RequestHeader(value = "X-User-Email", required = false) String email,
                                               @RequestHeader(value = "Authorization", required = false) String authorization,
                                               @Valid @RequestBody SubscriptionDtos.UpdateRequest request) {
        return ResponseEntity.ok(subscriptionService.update(resolveEmail(email, authorization), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@RequestHeader(value = "X-User-Email", required = false) String email,
                                       @RequestHeader(value = "Authorization", required = false) String authorization,
                                       @PathVariable Long id) {
        subscriptionService.cancel(resolveEmail(email, authorization), id);
        return ResponseEntity.noContent().build();
    }
}


