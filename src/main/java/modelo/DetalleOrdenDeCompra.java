package modelo;

import java.util.Date;

public class DetalleOrdenDeCompra implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
//	private Integer pkNumDetalle;
	private DetalleOrdenDeCompraId id;
	private Insumo insumo;
	private OrdenDeCompra ordenDeCompra;
	private Integer cantidad;
	private String precio;
	private String observacion;
	private Integer unidades;

	//Atributos adicionales necesarios
	private String nombreInsumo;
	private String articulo;
	private String referencia;
	private String nombreSector;
	private String nombreCategoria;
	
	public Integer idSector;
	
	public Integer idCategoria;
	
	public Integer idProveedor;
	
	public Integer nroOrdenDeCompra;

	public Date fechaOrden;
	
//	private String unidadDeMedida;

	//////////////////////////////CONSTRUCTOR////////////////////////////
	
	public DetalleOrdenDeCompra() {
	
	}
	
	public DetalleOrdenDeCompra(DetalleOrdenDeCompraId id, Insumo ins, OrdenDeCompra oc, Integer cant, String precio, String observ) {
		this.id = id;
		this.insumo = ins;
		this.ordenDeCompra = oc;
		this.cantidad = cant;
		this.precio = precio;
		this.observacion = observ;
	}
	
	public DetalleOrdenDeCompra(DetalleOrdenDeCompraId id, Insumo ins, OrdenDeCompra oc, Integer cant, String precio, String observ, Integer u) {
		this.id = id;
		this.insumo = ins;
		this.ordenDeCompra = oc;
		this.cantidad = cant;
		this.precio = precio;
		this.observacion = observ;
		this.unidades = u;
	}
	
	public DetalleOrdenDeCompra(Insumo ins, OrdenDeCompra odc, Integer cant, String nomInsumo,
			String art, String ref, String nomSector, String nomCategoria, Integer idSec, Integer idCat, Integer idProv,
			Integer nroOrdenCompra, Date fechaOrd /**, String udm **/, Integer un) {
		
		insumo = ins;
		ordenDeCompra = odc;
		cantidad = cant;
		nombreInsumo = nomInsumo;
		articulo = art;
		referencia = ref;
		nombreSector = nomSector;
		nombreCategoria = nomCategoria;
		idSector = idSec;
		idCategoria = idCat;
		idProveedor = idProv;
		nroOrdenDeCompra = nroOrdenCompra;
		fechaOrden = fechaOrd;
//		unidadDeMedida = udm;
		unidades = un;
	}

	
	public DetalleOrdenDeCompra(DetalleOrdenDeCompraId id, Insumo insumo, OrdenDeCompra ordenDeCompra, Integer cantidad, String observacion, 
								String precio, Integer unidades, String nombreInsumo, String articulo, String referencia, 
								String nombreSector, String nombreCategoria, Integer idSector, Integer idCategoria) {
		this.id = id;
		this.insumo = insumo;
		this.ordenDeCompra = ordenDeCompra;
		this.cantidad = cantidad;
		this.observacion = observacion;
		this.precio = precio;
		this.unidades = unidades;
		this.nombreInsumo = nombreInsumo;
		this.articulo = articulo;
		this.referencia = referencia;
		this.nombreSector = nombreSector;
		this.nombreCategoria = nombreCategoria;
		this.idSector = idSector;
		this.idCategoria = idCategoria;
}
	
	
	/////////////////////GET y SET////////////////////////////////////////

//	public Integer getPkNumDetalle() {
//		return pkNumDetalle;
//	}
//	public void setPkNumDetalle(Integer pkNumDetalle) {
//		this.pkNumDetalle = pkNumDetalle;
//	}
	
	public DetalleOrdenDeCompraId getId() {
		return this.id;
	}

	public void setId(DetalleOrdenDeCompraId id) {
		this.id = id;
	}
	
	public Insumo getInsumo() {
		return insumo;
	}
	
	public void setInsumo(Insumo insumo) {
		this.insumo = insumo;
	}
	
	public OrdenDeCompra getOrdenDeCompra() {
		return ordenDeCompra;
	}
	
	public void setOrdenDeCompra(OrdenDeCompra ordenDeCompra) {
		this.ordenDeCompra = ordenDeCompra;
	}
	
	public Integer getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public String getPrecio() {
		return precio;
	}
	
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	
	public String getObservacion() {
		return observacion;
	}
	
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getNombreInsumo() {
		return nombreInsumo;
	}

	public void setNombreInsumo(String nombreInsumo) {
		this.nombreInsumo = nombreInsumo;
	}

	public String getArticulo() {
		return articulo;
	}

	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getNombreSector() {
		return nombreSector;
	}

	public void setNombreSector(String nombreSector) {
		this.nombreSector = nombreSector;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	public Integer getIdSector() {
		return idSector;
	}

	public void setIdSector(Integer idSector) {
		this.idSector = idSector;
	}

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public Integer getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Integer idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Integer getNroOrdenDeCompra() {
		return nroOrdenDeCompra;
	}

	public void setNroOrdenDeCompra(Integer nroOrdenDeCompra) {
		this.nroOrdenDeCompra = nroOrdenDeCompra;
	}

	public Date getFechaOrden() {
		return fechaOrden;
	}

	public void setFechaOrden(Date fechaOrden) {
		this.fechaOrden = fechaOrden;
	}
	
	
//	public String getUnidadDeMedida() {
//		return unidadDeMedida;
//	}
//
//
//	public void setUnidadDeMedida(String udm) {
//		this.unidadDeMedida = udm;
//	}
	
	public Integer getUnidades() {
		return unidades;
	}

	public void setUnidades(Integer un) {
		this.unidades = un;
	}
}
