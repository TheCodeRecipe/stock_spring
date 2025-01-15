package com.stockproject.stock_analysis.repository;

import com.stockproject.stock_analysis.entity.Stock;
import com.stockproject.stock_analysis.entity.User;
import com.stockproject.stock_analysis.entity.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    @Query("SELECT new map(s.stockCode as stockCode, s.stockName as stockName, uf.id as id) " +
            "FROM UserFavorite uf " +
            "JOIN uf.stock s " +
            "JOIN uf.user u " +
            "WHERE u.username = :username")
     List<Map<String, Object>> findFavoritesWithStockDetails(@Param("username") String username);
    
    
    @Query("SELECT uf FROM UserFavorite uf WHERE uf.user.username = :username")
    List<UserFavorite> findByUserUsername(@Param("username") String username);
    
    boolean existsByUserAndStock(User user, Stock stock);
    
    Optional<UserFavorite> findByUserIdAndStockId(Long userId, Long stockId);
    
    Optional<UserFavorite> findByUserUsernameAndStockId(String username, Long stockId);
    
    Optional<UserFavorite> findByUserUsernameAndStockStockCode(String username, String stockCode);
}
