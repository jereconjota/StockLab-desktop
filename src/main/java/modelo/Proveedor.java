package modelo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Proveedor implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer pkIdProveedor;
	private String nroCuit;
	private String nombreProveedor;
	private String nroProveedor;
	private String direccionProveedor;
	private String estadoProveedor;
	private Date fechaAlta;
	private Date fechaBaja;
	private String trabajaConRemito;
	private Set<DetalleFactura> detalleFacturas = new HashSet<DetalleFactura>(0);
	private Set<DetalleRemito> detalleRemitos = new HashSet<DetalleRemito>(0);
	private Set<DetalleRemito> insumo = new HashSet<DetalleRemito>(0);
	
	private Integer nroDeOrdenDeCompra;
	
	//////////////////////////////CONSTRUCTOR////////////////////////////

	public Proveedor() {
		
	}

	public Proveedor(String nroCuit, String nombreProveedor) {
		this.nroCuit = nroCuit;
		this.nombreProveedor = nombreProveedor;
	}

	public Proveedor(String nroCuit, String nombreProveedor, String nroProveedor, String direccionProveedor,
			String estadoProveedor, Date fechaAlta, Date fechaBaja) {
		this.nroCuit = nroCuit;
		this.nombreProveedor = nombreProveedor;
		this.nroProveedor = nroProveedor;
		this.direccionProveedor = direccionProveedor;
		this.estadoProveedor = estadoProveedor;
		this.fechaAlta = fechaAlta;
		this.fechaBaja = fechaBaja;
	}
	
	public Proveedor(String nroCuit, String nombreProveedor, String nroProveedor, String direccionProveedor,
			String estadoProveedor, Date fechaAlta, Date fechaBaja, Set<DetalleFactura> detalleFacturas,
			Set<DetalleRemito> detalleRemitos) {
		this.nroCuit = nroCuit;
		this.nombreProveedor = nombreProveedor;
		this.nroProveedor = nroProveedor;
		this.direccionProveedor = direccionProveedor;
		this.estadoProveedor = estadoProveedor;
		this.fechaAlta = fechaAlta;
		this.fechaBaja = fechaBaja;
		this.detalleFacturas = detalleFacturas;
		this.detalleRemitos = detalleRemitos;
	}

	
	/////////////////////GET y SET////////////////////////////////////////

	public Integer getPkIdProveedor() {
		return pkIdProveedor;
	}

	public void setPkIdProveedor(Integer pkIdProveedor) {
		this.pkIdProveedor = pkIdProveedor;
	}

	public String getNroCuit() {
		return nroCuit;
	}

	public void setNroCuit(String nroCuit) {
		this.nroCuit = nroCuit;
	}

	public String getNombreProveedor() {
		return this.nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public String getNroProveedor() {
		return this.nroProveedor;
	}

	public void setNroProveedor(String nroProveedor) {
		this.nroProveedor = nroProveedor;
	}

	public String getDireccionProveedor() {
		return this.direccionProveedor;
	}

	public void setDireccionProveedor(String direccionProveedor) {
		this.direccionProveedor = direccionProveedor;
	}

	public String getEstadoProveedor() {
		return this.estadoProveedor;
	}

	public void setEstadoProveedor(String estadoProveedor) {
		this.estadoProveedor = estadoProveedor;
	}

	public Date getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Date getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getTrabajaConRemito() {
		return trabajaConRemito;
	}

	public void setTrabajaConRemito(String trabajaConRemito) {
		this.trabajaConRemito = trabajaConRemito;
	}
	
	public Set<DetalleFactura> getDetalleFacturas() {
		return this.detalleFacturas;
	}

	public void setDetalleFacturas(Set<DetalleFactura> detalleFacturas) {
		this.detalleFacturas = detalleFacturas;
	}
	
	public Set<DetalleRemito> getDetalleRemitos() {
		return this.detalleRemitos;
	}

	public void setDetalleRemitos(Set<DetalleRemito> detalleRemitos) {
		this.detalleRemitos = detalleRemitos;
	}

	public Set<DetalleRemito> getInsumo() {
		return insumo;
	}

	public void setInsumo(Set<DetalleRemito> insumo) {
		this.insumo = insumo;
	}
	
	public Integer getNroDeOrdenDeCompra() {
		return nroDeOrdenDeCompra;
	}
	
	public void setNroDeOrdenDeCompra(Integer nroDeOrderDeCompra) {
		this.nroDeOrdenDeCompra = nroDeOrderDeCompra;
	}
}
