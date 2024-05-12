package br.com.patrick.picpaysimplificado.domain.dtos;

import br.com.patrick.picpaysimplificado.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO (String firstName, String lastName, String doocument, BigDecimal balance, String email, String passoword, UserType userType){
}
