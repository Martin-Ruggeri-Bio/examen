package ar.com.plug.examen.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    private Double total;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User client;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User seller;

    public Sale(double total, LocalDateTime date, User client, User seller) {
        this.total = total;
        this.date = date;
        this.client = client;
        this.seller = seller;
    }
}
