package modelo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class OrdenDeCompra implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Integer pkIdOrdenCompra;
	private Usuario usuario;
	private Proveedor proveedor;
	private Integer nroOrdenCompra;
	private Date fechaOrdenCompra;
	private String observacionOrdenCompra;
	
	public String nombreUsuario;
	public String apellidoUsuario;
	public String nombreProveedor;
	
	//////////////////////////////CONSTRUCTOR////////////////////////////
	
	public OrdenDeCompra() {
	
	}
	
	
	public OrdenDeCompra(Usuario usr, Proveedor prov, Integer nroOrden, Date fechaOrden) {
		this.usuario = usr;
		this.proveedor = prov;
		this.nroOrdenCompra = nroOrden;
		this.fechaOrdenCompra = fechaOrden;
	}
	
	public OrdenDeCompra(Usuario usr, Proveedor prov, Integer nroOrden, Date fechaOrden, String obs) {
		this.usuario = usr;
		this.proveedor = prov;
		this.nroOrdenCompra = nroOrden;
		this.fechaOrdenCompra = fechaOrden;
		this.observacionOrdenCompra = obs;
	}
	
	public OrdenDeCompra(Integer nroOrden) {
		this.nroOrdenCompra = nroOrden;
	}
	
	
	public OrdenDeCompra(Integer idOrdenCompra, Usuario usr, Proveedor prov, Integer nroOrden, Date fechaOrden, String observacion,
							String nombreUser, String apellidoUser, String nombreProvee) {
		this.pkIdOrdenCompra = idOrdenCompra;
		this.usuario = usr;
		this.proveedor = prov;
		this.nroOrdenCompra = nroOrden;
		this.fechaOrdenCompra = fechaOrden;
		this.observacionOrdenCompra = observacion;
		this.nombreUsuario = nombreUser;
		this.apellidoUsuario = apellidoUser;
		this.nombreProveedor = nombreProvee;
	}
	
	
	/////////////////////GET y SET////////////////////////////////////////

	public Integer getPkIdOrdenCompra() {
		return pkIdOrdenCompra;
	}

	public void setPkIdOrdenCompra(Integer pkIdOrdenCompra) {
		this.pkIdOrdenCompra = pkIdOrdenCompra;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Integer getNroOrdenCompra() {
		return nroOrdenCompra;
	}

	public void setNroOrdenCompra(Integer nroOrdenCompra) {
		this.nroOrdenCompra = nroOrdenCompra;
	}

	public Date getFechaOrdenCompra() {
		return fechaOrdenCompra;
	}

	public void setFechaOrdenCompra(Date fechaOrdenCompra) {
		this.fechaOrdenCompra = fechaOrdenCompra;
	}

	public Set<DetalleOrdenDeCompra> getDetalleOrdenDeCompra() {
		return detalleOrdenDeCompra;
	}

	public void setDetalleOrdenDeCompra(Set<DetalleOrdenDeCompra> detalleOrdenDeCompra) {
		this.detalleOrdenDeCompra = detalleOrdenDeCompra;
	}

	private Set<DetalleOrdenDeCompra> detalleOrdenDeCompra = new HashSet<DetalleOrdenDeCompra>(0);

	
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public String getApellidoUsuario() {
		return apellidoUsuario;
	}

	public void setApellidoUsuario(String apellidoUsuario) {
		this.apellidoUsuario = apellidoUsuario;
	}

	public String getObservacionOrdenCompra() {
		return observacionOrdenCompra;
	}

	public void setObservacionOrdenCompra(String observacionOrdenCompra) {
		this.observacionOrdenCompra = observacionOrdenCompra;
	}

}
