package modelo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Factura implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer idFactura;
	private String nroFactura;
	private Date fechaFactura;
	private String tipoFactura;
	private Date fechaCarga;
	private String observacionFactura;
	private String tieneRemito;
	private String estadoFactura;

	private Set<DetalleFactura> detalleFacturas = new HashSet<DetalleFactura>(0);
	private Set<Remito> remitos = new HashSet<Remito>(0);
	
	// atributos necesarios para FacturaFX
	private Double subTotal;			//un calculable
	private Integer idProveedor;
	private String nombreProveedor;
	private String nroCuitProveedor;
	
	
	//////////////////////////////CONSTRUCTOR////////////////////////////

	public Factura() {
	}
	
	/** constructor para FacturaFX, el cual agrega atrbutos adicionales de proveedor
	 * 		y calcula subtotal
	 */
	public Factura(Integer idFactura, String nroFactura, Date fechaFactura, String tipoFactura, Date fechaCarga, String observacion, 
					String tieneRemito, String estado, Integer idProveedor, String nombreProveedor, String nroCuitProveedor, Double subTotal) {
		this.idFactura = idFactura;
		this.nroFactura = nroFactura;
		this.fechaFactura = fechaFactura;
		this.tipoFactura = tipoFactura;
		this.fechaCarga = fechaCarga;
		this.observacionFactura = observacion;
		this.tieneRemito = tieneRemito;
		this.estadoFactura = estado;
		this.idProveedor = idProveedor;
		this.nombreProveedor = nombreProveedor;
		this.nroCuitProveedor = nroCuitProveedor;
		this.subTotal = subTotal;
	}
	

	public Factura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	public Factura(String nroFactura, Date fechaFactura, String tipoFactura, Date fechaCarga,
			Set<Remito> remitos, Set<DetalleFactura> detalleFacturas) {
		this.nroFactura = nroFactura;
		this.fechaFactura = fechaFactura;
		this.tipoFactura = tipoFactura;
		this.fechaCarga = fechaCarga;
		this.remitos = remitos;
		this.detalleFacturas = detalleFacturas;
	}

	public Factura(String nroFactura, Date fechaFactura, String tipoFactura, Date fechaCarga, String observacion) {
		this.nroFactura = nroFactura;
		this.fechaFactura = fechaFactura;
		this.tipoFactura = tipoFactura;
		this.fechaCarga = fechaCarga;
		this.observacionFactura = observacion;
	}
	
	public Factura(String nroFactura, Date fechaFactura, String tipoFactura, Date fechaCarga, String observacion, String tieneRemito) {
		this.nroFactura = nroFactura;
		this.fechaFactura = fechaFactura;
		this.tipoFactura = tipoFactura;
		this.fechaCarga = fechaCarga;
		this.observacionFactura = observacion;
		this.tieneRemito = tieneRemito;
	}
	
	/////////////////////GET y SET////////////////////////////////////////
	
	

	public Integer getIdFactura() {
		return this.idFactura;
	}

	public void setIdFactura(Integer idFactura) {
		this.idFactura = idFactura;
	}
	
	public String getNroFactura() {
		return this.nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	public Date getFechaFactura() {
		return this.fechaFactura;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public String getTipoFactura() {
		return this.tipoFactura;
	}

	public void setTipoFactura(String tipoFactura) {
		this.tipoFactura = tipoFactura;
	}

	public Date getFechaCarga() {
		return this.fechaCarga;
	}

	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}

	public String getObservacionFactura() {
		return observacionFactura;
	}

	public void setObservacionFactura(String observacionFactura) {
		this.observacionFactura = observacionFactura;
	}

	public String getTieneRemito() {
		return tieneRemito;
	}

	public void setTieneRemito(String tieneRemito) {
		this.tieneRemito = tieneRemito;
	}

	public Set<Remito> getRemitos() {
		return this.remitos;
	}

	public void setRemitos(Set<Remito> remitos) {
		this.remitos = remitos;
	}

	public Set<DetalleFactura> getDetalleFacturas() {
		return this.detalleFacturas;
	}

	public void setDetalleFacturas(Set<DetalleFactura> detalleFacturas) {
		this.detalleFacturas = detalleFacturas;
	}

	public String getEstadoFactura() {
		return estadoFactura;
	}
	
	public void setEstadoFactura(String estadoFactura) {
		this.estadoFactura = estadoFactura;
	}

	public Integer getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Integer idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public String getNroCuitProveedor() {
		return nroCuitProveedor;
	}

	public void setNroCuitProveedor(String nroCuitProveedor) {
		this.nroCuitProveedor = nroCuitProveedor;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	
	

}
