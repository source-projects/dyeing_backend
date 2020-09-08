package Dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import model.Fabric;


@EnableJpaRepositories
public interface FabricsDao extends JpaRepository<Fabric, Long> {

}
