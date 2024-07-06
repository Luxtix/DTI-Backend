package com.luxtix.eventManagementWebsite.Transactions.repository;

import com.luxtix.eventManagementWebsite.Transactions.entity.Transactions;
import com.luxtix.eventManagementWebsite.Transactions.dao.getAllTransactionResponseDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long> {
    List<Transactions> findByUsersId(long userId);


    public static final String allUserTransactionQuery ="select\n" +
            "\ttl.tickets.id as id,\n" +
            "    e.name as eventName,\n" +
            "    e.venueName  as venue,\n" +
            "    ti.name AS ticketName,\n" +
            "    tl.qty AS ticketQty,\n" +
            "    e.eventDate as eventDate,\n" +
            "    e.cities.name as cityName,\n" +
            "    e.startTime  AS startTime,\n" +
            "    e.endTime  as endTime,\n" +
            "    e.isOnline AS isOnline\n" +
            "FROM \n" +
            "    Users u\n" +
            "JOIN \n" +
            "    Transactions tr ON u.id = tr.users.id\n" +
            "JOIN \n" +
            "    TransactionList tl ON tr.id = tl.transactions.id\n" +
            "JOIN \n" +
            "    Tickets ti ON tl.tickets.id = ti.id\n" +
            "JOIN \n" +
            "    Events e ON ti.events.id = e.id\n" +
            "WHERE \n" +
            "    u.id = :userId";

    @Query(value = allUserTransactionQuery)
    Optional<List<getAllTransactionResponseDao>> getAllUserTransactions(@Param("userId") long userId);
}
