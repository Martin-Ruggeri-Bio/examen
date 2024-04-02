package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Detail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DetailServiceTest {

    @Autowired
    private DetailService detailService;

    @Test
    public void testGetDetailsAll() {
        // Act
        List<Detail> details = detailService.getDetailsAll();

        // Assert
        assertNotNull(details);
    }

    @Test
    public void testCreateDetail() {
        // Arrange
        Detail detail = new Detail();
        detail.setFecha(LocalDateTime.now());
        detail.setPrecio(100.0f);
        detail.setProduct(1L);
        detail.setVentaId("1");

        // Act
        detailService.createDetail(detail);

        // Assert
        // Check if the detail was created successfully
        List<Detail> details = detailService.getDetailBySale(detail.getVentaId());
        assertFalse(details.isEmpty());
    }

    @Test
    public void testGetDetailBySale() {
        // Arrange
        Detail detail = new Detail();
        detail.setFecha(LocalDateTime.now());
        detail.setPrecio(100.0f);
        detail.setProduct(1L);
        detail.setVentaId("1");
        detailService.createDetail(detail);

        // Act
        List<Detail> details = detailService.getDetailBySale(detail.getVentaId());

        // Assert
        assertNotNull(details);
    }

    @Test
    public void testFindByDateBetween() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        // Act
        List<Detail> details = detailService.findByDateBetween(startDate, endDate);

        // Assert
        assertNotNull(details);
    }
}
