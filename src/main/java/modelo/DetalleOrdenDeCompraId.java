package modelo;

public class DetalleOrdenDeCompraId implements java.io.Serializable {
	
private static final long serialVersionUID = 1L;
	
	private int pkNumDetalle;
	private int fkIdOrdenCompra;
	
	//////////////////////////////CONSTRUCTOR////////////////////////////

	public DetalleOrdenDeCompraId() {
	}

	public DetalleOrdenDeCompraId(int pkNumDetalle, int fkIdOrdenCompra) {
		this.pkNumDetalle = pkNumDetalle;
		this.fkIdOrdenCompra = fkIdOrdenCompra;
	}
	
	public DetalleOrdenDeCompraId(int fkIdOrdenCompra) {
		this.fkIdOrdenCompra = fkIdOrdenCompra;
	}
	
	/////////////////////GET y SET////////////////////////////////////////

	public int getPkNumDetalle() {
		return this.pkNumDetalle;
	}

	public void setPkNumDetalle(int pkNumDetalle) {
		this.pkNumDetalle = pkNumDetalle;
	}

	public int getFkIdOrdenCompra() {
		return this.fkIdOrdenCompra;
	}

	public void setFkIdOrdenCompra(int fkIdOrdenCompra) {
		this.fkIdOrdenCompra = fkIdOrdenCompra;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DetalleOrdenDeCompraId))
			return false;
		DetalleOrdenDeCompraId castOther = (DetalleOrdenDeCompraId) other;

		return (this.getPkNumDetalle() == castOther.getPkNumDetalle())
				&& (this.getFkIdOrdenCompra() == castOther.getFkIdOrdenCompra());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getPkNumDetalle();
		result = 37 * result + this.getFkIdOrdenCompra();
		return result;
	}

}
