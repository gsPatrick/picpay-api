package br.com.patrick.picpaysimplificado.domain.services;

import br.com.patrick.picpaysimplificado.domain.dtos.TransactionDTO;
import br.com.patrick.picpaysimplificado.domain.repositories.TransactionRepository;
import br.com.patrick.picpaysimplificado.domain.transaction.Transaction;
import br.com.patrick.picpaysimplificado.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {

        User sender = this.userService.findUserByid(transaction.senderId());
        User receiver = this.userService.findUserByid(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());


        boolean isAuthorized = this.authorizedTransaction(sender, transaction.value());
        if(!isAuthorized){
            throw new Exception("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        this.userService.save(sender);
        this.userService.save(receiver);

            this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
            this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");
        return newTransaction;

    }



    public boolean authorizedTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);
            if (authorizationResponse.getStatusCode() == HttpStatus.OK){
                String message = (String) authorizationResponse.getBody().get("message");
                return "autorizado".equalsIgnoreCase(message);
            } else return false;
        }
    }

