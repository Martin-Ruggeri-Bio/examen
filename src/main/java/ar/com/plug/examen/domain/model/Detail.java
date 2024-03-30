package ar.com.plug.examen.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Detail {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'z'")
    private LocalDateTime fecha;
    private String ventaId;
    private Long product;
    private Float precio;
}
