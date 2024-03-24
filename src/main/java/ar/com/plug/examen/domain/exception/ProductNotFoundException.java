package ar.com.plug.examen.domain.exception;

public class ProductNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3095543865324828748L;

	public ProductNotFoundException(Integer id) {
		super("Cannot find Product " + id);
	}
}
