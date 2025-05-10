package com.example.usersubmanager.repository;

import com.example.usersubmanager.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
 Set<Subscription> findAllByUsersId(Long userId);
 Optional<Subscription> findByName(String name);


 @Query(nativeQuery = true,
         value = "SELECT s.* FROM subscriptions s " +
                 "LEFT JOIN user_subscriptions us ON s.id = us.subscription_id " +
                 "GROUP BY s.id " +
                 "ORDER BY COUNT(us.user_id) DESC " +
                 "LIMIT 3"
 )
 Set<Subscription> findTop3PopularSubscriptions();

}
