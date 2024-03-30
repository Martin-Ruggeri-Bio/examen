package ar.com.plug.examen.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Martin
 *
 */
@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String nombre;
	private String descripcion;
	private Float precio;
	private String urlImagen;
	private Boolean activo;
	private String creado;
	private String actualizado;
}
