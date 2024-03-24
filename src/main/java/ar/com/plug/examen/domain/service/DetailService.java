package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Detail;
import ar.com.plug.examen.domain.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

@Service
@Transactional
public class DetailService {

    @Autowired
    private DetailRepository detailRepository;

    public List<Detail> getDetailsAll(){
        return this.detailRepository.findAll();
    }

    public void createDetail(Detail detail){
        this.detailRepository.save(detail);
    }
    public List<Detail> getDetailBySale(String saleId){
        return this.detailRepository.findByVentaId(saleId);
    }

    public List<Detail> findByDateBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return detailRepository.findByFechaBetween(fechaInicio, fechaFin);
    }
}
