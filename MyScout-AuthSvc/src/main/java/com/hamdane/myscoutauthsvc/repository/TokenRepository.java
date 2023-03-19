package com.hamdane.myscoutauthsvc.repository;

import com.hamdane.myscoutauthsvc.model.Token;
import com.hamdane.myscoutauthsvc.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, Integer> {

  List<Token> findByUser(User user);

  Optional<Token> findByToken(String token);
}
