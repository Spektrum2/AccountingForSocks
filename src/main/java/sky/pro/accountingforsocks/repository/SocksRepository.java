package sky.pro.accountingforsocks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sky.pro.accountingforsocks.model.Socks;

public interface SocksRepository extends JpaRepository<Socks, Long> {
    Socks findByColorAndCottonPart(String color, int cottonPart);

    @Query(value = "SELECT SUM(quantity) FROM socks WHERE color = :color AND cotton_part > :cottonPart", nativeQuery = true)
    Integer getCountSocksMore(@Param("color") String color, @Param("cottonPart") int cottonPart);

    @Query(value = "SELECT SUM(quantity) FROM socks WHERE color = :color AND cotton_part < :cottonPart", nativeQuery = true)
    Integer getCountSocksLess(@Param("color") String color, @Param("cottonPart") int cottonPart);

    @Query(value = "SELECT SUM(quantity) FROM socks WHERE color = :color AND cotton_part = :cottonPart", nativeQuery = true)
    Integer getCountSocksEqual(@Param("color") String color, @Param("cottonPart") int cottonPart);
}
