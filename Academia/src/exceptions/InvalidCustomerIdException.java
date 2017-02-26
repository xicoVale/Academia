package exceptions;

public class InvalidCustomerIdException extends Exception {

	private static final long serialVersionUID = -6187964454920377553L;
	
	public InvalidCustomerIdException() {
		super ("O número de cliente é inválido.");
	}

}
