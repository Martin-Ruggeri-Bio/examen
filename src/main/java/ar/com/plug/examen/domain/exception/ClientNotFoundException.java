package ar.com.plug.examen.domain.exception;

public class ClientNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3095543865324828748L;

	public ClientNotFoundException(Integer id) {
		super("Cannot find Client " + id);
	}
}
