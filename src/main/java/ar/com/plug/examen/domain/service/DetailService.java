package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Detail;
import ar.com.plug.examen.domain.repository.DetailRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
public class DetailService {

    @Autowired
    private DetailRepository detailRepository;

    public List<Detail> getDetailsAll(){
        log.info("Fetching all details");
        return this.detailRepository.findAll();
    }

    public void createDetail(Detail detail){
        log.info("Creating new detail: {}", detail);
        this.detailRepository.save(detail);
    }

    public List<Detail> getDetailBySale(String saleId){
        log.info("Fetching details for sale: {}", saleId);
        return this.detailRepository.findByVentaId(saleId);
    }

    public List<Detail> findByDateBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Fetching details between dates: {} and {}", fechaInicio, fechaFin);
        return detailRepository.findByFechaBetween(fechaInicio, fechaFin);
    }
}
