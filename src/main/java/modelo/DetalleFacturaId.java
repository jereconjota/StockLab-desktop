package modelo;

public class DetalleFacturaId implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int pkNumDetalle;
	private int fkIdFactura;
	
	//////////////////////////////CONSTRUCTOR////////////////////////////

	public DetalleFacturaId() {
	}

	public DetalleFacturaId(int pkNumDetalle, int fkIdFactura) {
		this.pkNumDetalle = pkNumDetalle;
		this.fkIdFactura = fkIdFactura;
	}
	
	public DetalleFacturaId(int fkIdFactura) {
		this.fkIdFactura = fkIdFactura;
	}
	
	/////////////////////GET y SET////////////////////////////////////////

	public int getPkNumDetalle() {
		return this.pkNumDetalle;
	}

	public void setPkNumDetalle(int pkNumDetalle) {
		this.pkNumDetalle = pkNumDetalle;
	}

	public int getFkIdFactura() {
		return this.fkIdFactura;
	}

	public void setFkIdFactura(int fkIdFactura) {
		this.fkIdFactura = fkIdFactura;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DetalleFacturaId))
			return false;
		DetalleFacturaId castOther = (DetalleFacturaId) other;

		return (this.getPkNumDetalle() == castOther.getPkNumDetalle())
				&& (this.getFkIdFactura() == castOther.getFkIdFactura());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getPkNumDetalle();
		result = 37 * result + this.getFkIdFactura();
		return result;
	}

}
