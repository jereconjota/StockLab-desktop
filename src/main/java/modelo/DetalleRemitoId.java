package modelo;

public class DetalleRemitoId implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int pkNumDetalle;
	private int fkIdRemito;
	
	
	//////////////////////////////CONSTRUCTOR////////////////////////////

	public DetalleRemitoId() {
	}

	public DetalleRemitoId(int pkNumDetalle, int fkIdRemito) {
		this.pkNumDetalle = pkNumDetalle;
		this.fkIdRemito = fkIdRemito;
	}
	
	public DetalleRemitoId(int fkIdRemito) {
		this.fkIdRemito = fkIdRemito;
	}
	
	/////////////////////GET y SET////////////////////////////////////////

	public int getPkNumDetalle() {
		return this.pkNumDetalle;
	}

	public void setPkNumDetalle(int pkNumDetalle) {
		this.pkNumDetalle = pkNumDetalle;
	}

	public int getFkIdRemito() {
		return this.fkIdRemito;
	}

	public void setFkIdRemito(int fkIdRemito) {
		this.fkIdRemito = fkIdRemito;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DetalleRemitoId))
			return false;
		DetalleRemitoId castOther = (DetalleRemitoId) other;

		return (this.getPkNumDetalle() == castOther.getPkNumDetalle())
				&& (this.getFkIdRemito() == castOther.getFkIdRemito());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getPkNumDetalle();
		result = 37 * result + this.getFkIdRemito();
		return result;
	}

}
