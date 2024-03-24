package ar.com.plug.examen.domain.exception;

public class SellerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3095543865324828748L;

	public SellerNotFoundException(Integer id) {
		super("Cannot find Seller " + id);
	}
}
