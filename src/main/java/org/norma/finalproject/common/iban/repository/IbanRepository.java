package org.norma.finalproject.common.iban.repository;

import org.norma.finalproject.common.iban.entity.Iban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IbanRepository extends JpaRepository<Iban,Long> {


}
