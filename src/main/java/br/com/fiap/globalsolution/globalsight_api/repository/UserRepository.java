package br.com.fiap.globalsolution.globalsight_api.repository;

import br.com.fiap.globalsolution.globalsight_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Encontra um usuário pelo seu nome de usuário.
     * @param username O nome de usuário a ser buscado.
     * @return Um Optional contendo o usuário se encontrado, ou vazio caso contrário.
     */
    Optional<User> findByUsername(String username);
}