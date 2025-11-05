package com.beanbliss.subscription.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String frequency; // weekly | biweekly | monthly

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String status; // active | paused | cancelled

    private LocalDate nextDelivery;
    private LocalDate lastDelivery;

    public Subscription() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getNextDelivery() { return nextDelivery; }
    public void setNextDelivery(LocalDate nextDelivery) { this.nextDelivery = nextDelivery; }
    public LocalDate getLastDelivery() { return lastDelivery; }
    public void setLastDelivery(LocalDate lastDelivery) { this.lastDelivery = lastDelivery; }
}


