package ar.com.plug.examen.domain.exception;

public class SaleNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3095543865324828748L;

	public SaleNotFoundException(Integer id) {
		super("Cannot find Sale " + id);
	}
}
