package com.example.ExpenseTracker.repository;

import com.example.ExpenseTracker.model.Expense;
import com.example.ExpenseTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);

    @Query("""
            select e from Expense e
            where e.user = :user
            and (
                lower(e.title) like lower(concat('%', :keyword, '%'))
                or lower(e.category) like lower(concat('%', :keyword, '%'))
            )
            order by e.date desc, e.id desc
            """)
    List<Expense> searchByUserAndKeyword(@Param("user") User user, @Param("keyword") String keyword);

    @Query("""
            select e from Expense e
            where e.user = :user
            and (:keyword is null or :keyword = '' or lower(e.title) like lower(concat('%', :keyword, '%')) or lower(e.category) like lower(concat('%', :keyword, '%')))
            and (:category is null or :category = '' or lower(e.category) = lower(:category))
            order by e.date desc, e.id desc
            """)
    List<Expense> filterByUserAndCriteria(@Param("user") User user,
                                          @Param("keyword") String keyword,
                                          @Param("category") String category);
}

