package br.com.fiap.globalsolution.globalsight_api.repository;

import br.com.fiap.globalsolution.globalsight_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Encontra um papel pelo seu nome.
     * @param name O nome do papel a ser buscado (ex: "ROLE_USER").
     * @return Um Optional contendo o papel se encontrado, ou vazio caso contrário.
     */
    Optional<Role> findByName(String name);
}