package com.main.glory.Dao.admin;

import com.main.glory.model.admin.InvoiceSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface InvoiceSequenceDao extends JpaRepository<InvoiceSequence,Long> {


    @Query(value = "select * from invoice_sequence as x LIMIT 1",nativeQuery = true)
    InvoiceSequence getSequence();

    @Query("select x from InvoiceSequence x where x.id=:id")
    InvoiceSequence getSequenceById(Long id);

    @Modifying
    @Transactional
    @Query("update InvoiceSequence x set x.sequence=:l where x.id=:id")
    void updateSequenceByOne(Long id, long l);
}
