package exceptions;

public class InsuficientAttributesException extends Exception {
	private static final long serialVersionUID = -253026366023353807L;
	
	public InsuficientAttributesException() {
		super("O ficheiro n�o contem attributos suficientes.");
	}

}
