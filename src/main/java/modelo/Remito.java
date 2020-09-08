package modelo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Remito implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer idRemito;
	private Factura factura;
	private String nroRemito;
	private Date fechaRemito;
	private Date fechaCarga;
	private String observacionRemito;
	private Set<DetalleRemito> detalleRemitos = new HashSet<DetalleRemito>(0);
	private Set<DetalleFactura> detalleFacturas = new HashSet<DetalleFactura>(0);
	
	//atributos adicionales para remitoFX
	private String nroFactura;
	private Integer idProveedor;
	private String nombreProveedor;
	private String nroCuit;

	
	//////////////////////////////CONSTRUCTOR////////////////////////////
	
	
	public Remito() {
	}
	
	/** constructor para remitoFX, el cual agrega atributos que no forman parte de la tabla remitp **/
	public Remito(Integer idRemito, Factura factura, String nroRemito, Date fechaRemito, Date fechaCarga, String observacionRemito, 
			String nroFactura, Integer idProveedor, String nombreProveedor, String nroCuit) {
		this.idRemito = idRemito;
		this.factura = factura;
		this.nroRemito = nroRemito;
		this.fechaRemito = fechaRemito;
		this.fechaCarga = fechaCarga;
		this.observacionRemito = observacionRemito;
		this.nroFactura = nroFactura;
		this.idProveedor = idProveedor;
		this.nombreProveedor = nombreProveedor;
		this.nroCuit = nroCuit;
	}
	
	
	/** constructor para query "obtenerRemitoPorId2(Integer idRemitoIN)" **/
	public Remito(Integer idRemito, Factura factura, String nroRemito, Date fechaRemito, Date fechaCarga, String observacionRemito,
					String nroFactura) {
		this.idRemito = idRemito;
		this.factura = factura;
		this.nroRemito = nroRemito;
		this.fechaRemito = fechaRemito;
		this.fechaCarga = fechaCarga;
		this.observacionRemito = observacionRemito;
		this.nroFactura = nroFactura;
	}
	

	public Remito(Factura factura, String nroRemito, Date fechaRemito, Date fechaCarga,
			Set<DetalleRemito> detalleRemitos, Set<DetalleFactura> detalleFacturas) {
		this.factura = factura;
		this.nroRemito = nroRemito;
		this.fechaRemito = fechaRemito;
		this.fechaCarga = fechaCarga;
		this.detalleRemitos = detalleRemitos;
		this.detalleFacturas = detalleFacturas;
	}
	
	//Constructor nuevo
	public Remito( String nroRemito, Factura factura, Date fechaRemito, Date fechaCarga) {
		this.factura = factura;
		this.nroRemito = nroRemito;
		this.fechaRemito = fechaRemito;
		this.fechaCarga = fechaCarga;
	}
	
	public Remito( String nroRemito, Factura factura, Date fechaRemito, Date fechaCarga, String observacion) {
		this.factura = factura;
		this.nroRemito = nroRemito;
		this.fechaRemito = fechaRemito;
		this.fechaCarga = fechaCarga;
		this.observacionRemito = observacion;
	}
	
	public Remito( String nroRemito, Date fechaRemito, Date fechaCarga) {
		this.nroRemito = nroRemito;
		this.fechaRemito = fechaRemito;
		this.fechaCarga = fechaCarga;
	}
	
	public Remito( String nroRemito, Date fechaRemito, Date fechaCarga, String observacion) {
		this.nroRemito = nroRemito;
		this.fechaRemito = fechaRemito;
		this.fechaCarga = fechaCarga;
		this.observacionRemito = observacion;
	}
	
	/////////////////////GET y SET////////////////////////////////////////

	public Integer getIdRemito() {
		return this.idRemito;
	}

	public void setIdRemito(Integer idRemito) {
		this.idRemito = idRemito;
	}

	public Factura getFactura() {
		return this.factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public String getNroRemito() {
		return this.nroRemito;
	}

	public void setNroRemito(String nroRemito) {
		this.nroRemito = nroRemito;
	}

	public Date getFechaRemito() {
		return this.fechaRemito;
	}

	public void setFechaRemito(Date fechaRemito) {
		this.fechaRemito = fechaRemito;
	}

	public Date getFechaCarga() {
		return this.fechaCarga;
	}

	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}

	public String getObservacionRemito() {
		return observacionRemito;
	}

	public void setObservacionRemito(String observacionRemito) {
		this.observacionRemito = observacionRemito;
	}

	public Set<DetalleRemito> getDetalleRemitos() {
		return this.detalleRemitos;
	}

	public void setDetalleRemitos(Set<DetalleRemito> detalleRemitos) {
		this.detalleRemitos = detalleRemitos;
	}

	public Set<DetalleFactura> getDetalleFacturas() {
		return this.detalleFacturas;
	}

	public void setDetalleFacturas(Set<DetalleFactura> detalleFacturas) {
		this.detalleFacturas = detalleFacturas;
	}

	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
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

	public String getNroCuit() {
		return nroCuit;
	}

	public void setNroCuit(String nroCuit) {
		this.nroCuit = nroCuit;
	}
	
	

}
