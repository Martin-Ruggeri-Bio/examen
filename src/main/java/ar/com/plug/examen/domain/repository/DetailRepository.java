package ar.com.plug.examen.domain.repository;

import ar.com.plug.examen.domain.model.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface DetailRepository extends JpaRepository<Detail, String> {
    List<Detail> findByVentaId(String saleId);
    List<Detail> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
