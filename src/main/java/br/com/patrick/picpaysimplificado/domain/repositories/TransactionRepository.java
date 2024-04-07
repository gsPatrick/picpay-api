package br.com.patrick.picpaysimplificado.domain.repositories;

import br.com.patrick.picpaysimplificado.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository  extends JpaRepository<Transaction, Long> {
}
