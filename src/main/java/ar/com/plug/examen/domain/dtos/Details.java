package ar.com.plug.examen.domain.dtos;

import java.util.List;

import ar.com.plug.examen.domain.model.Detail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Details {
    private List<Detail> detail;
}
