package modeloAux;

public abstract class Fx <C>{

	public Fx() {
		// TODO Auto-generated constructor stub
	}

	public abstract Fx<C> from (C c);

	public abstract C to (C c);
	
}
