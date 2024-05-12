package br.com.patrick.picpaysimplificado.domain.services;

import br.com.patrick.picpaysimplificado.domain.dtos.UserDTO;
import br.com.patrick.picpaysimplificado.domain.repositories.UserRepository;
import br.com.patrick.picpaysimplificado.domain.user.User;
import br.com.patrick.picpaysimplificado.domain.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {

        if(sender.getUserType() == UserType.MERCHANT) {

            throw new Exception("Usuário do tipo Lojista não está autorizado a realizar transação");

        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo o suficente");
        }
  }

    public User findUserByid(Long id) throws Exception {
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuario não encontrado"));
    }

    public User createUser(UserDTO data) {
        User newUser = new User(data);
        this.save(newUser);
        return newUser;
    }

    public List<User> getAllUsers(){
      return this.repository.findAll();
    }

    public void save(User user) {
        this.repository.save(user);
    }

}
