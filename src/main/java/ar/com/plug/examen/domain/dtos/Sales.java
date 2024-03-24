package ar.com.plug.examen.domain.dtos;

import ar.com.plug.examen.domain.model.Sale;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {
    private List<Sale> detail;
}
