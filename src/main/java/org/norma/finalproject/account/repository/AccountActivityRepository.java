package org.norma.finalproject.account.repository;

import org.norma.finalproject.account.entity.base.AccountActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountActivityRepository extends JpaRepository<AccountActivity, Long> {

    List<AccountActivity> findAllByAccount_Id(long accountID);
}
