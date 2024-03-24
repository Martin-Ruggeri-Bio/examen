package ar.com.plug.examen.domain.dtos;
import ar.com.plug.examen.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {
    private Product product;
    private int amount;
}
