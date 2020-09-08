package modelo;

import java.util.Date;

public class DetalleFactura implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private DetalleFacturaId id;
	private Factura factura;
	private Insumo insumo;
	private Proveedor proveedor;
	private Remito remito;
	private Integer cantidad;
	private Float precio;
	private Float importe;
	
	// datos adicionales para DetalleFacturaFX
	private String nroFactura; 
	private String nroRemito;
	private String nombreInsumo;
	private String nroLote;
	private String nroArticulo;
	private String referencia;
	private String unidadMedida;
	private Date fechaVtoInsumo;
	private String nroCuit;
	private String nombreProveedor;
	private Integer idCategoria;
	private String nombreCategoria;
	private String nombreSector;
	
	//////////////////////////////CONSTRUCTOR////////////////////////////

	public DetalleFactura() {
	}
	
	
	/** constructor para DetalleFacturaFX, el cual agrega atributos adicionales q no forman parte de la tabla 
	 * (NO AGREGO DetalleFacturaId, que tiene el nroDetalle)**/
	public DetalleFactura(Factura factura, Insumo insumo, Proveedor proveedor, Remito remito, Integer cantidad, Float precio, Float importe, 
			String nroFactura, String nroRemito, String nombreInsumo, String nroLote, String nroArticulo, String unidadMedida, 
			Date fechaVto, String nroCuit) {
		this.factura = factura;
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
		this.cantidad = cantidad;
		this.precio = precio;
		this.importe = importe;
		// datos adicionales para DetalleFacturaFX
		this.nroFactura = nroFactura; 
		this.nroRemito = nroRemito;
		this.nombreInsumo = nombreInsumo;
		this.nroLote = nroLote;
		this.nroArticulo = nroArticulo;
		this.unidadMedida = unidadMedida;
		this.fechaVtoInsumo = fechaVto;
		this.nroCuit = nroCuit;
	}
	
	
	/** constructor para DetalleFacturaFX, el cual agrega atributos adicionales q no forman parte de la tabla 
	 *		ademas atributos de categoria y sector
	 ***/
	public DetalleFactura(DetalleFacturaId id, Factura factura, Insumo insumo, Proveedor proveedor, Remito remito, Integer cantidad, Float precio, Float importe, 
			String nroFactura, String nroRemito, String nombreInsumo, String nroLote, String nroArticulo, String referencia, String unidadMedida, 
			Date fechaVto, String nroCuit, String nombreProveedor, Integer idCategoria, String nombreCategoria, String nombreSector) {
		this.id = id;
		this.factura = factura;
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
		this.cantidad = cantidad;
		this.precio = precio;
		this.importe = importe;
		// datos adicionales para DetalleFacturaFX
		this.nroFactura = nroFactura; 
		this.nroRemito = nroRemito;
		this.nombreInsumo = nombreInsumo;
		this.nroLote = nroLote;
		this.nroArticulo = nroArticulo;
		this.referencia = referencia;
		this.unidadMedida = unidadMedida;
		this.fechaVtoInsumo = fechaVto;
		this.nroCuit = nroCuit;
		this.nombreProveedor = nombreProveedor;
		this.idCategoria = idCategoria;
		this.nombreCategoria = nombreCategoria;
		this.nombreSector = nombreSector;
	}
	
	
	/** constructor para DetalleFacturaFX, el cual agrega atributos adicionales q no forman parte de la tabla 
	 * (NO AGREGO DetalleFacturaId, que tiene el nroDetalle) 
	 * 	y agrego datos adicionales de proveedor
	 * 	lo llamo desde query "obtenerDetalleFacturaPorIdFactura2(Integer idFacturaIN)" **/
	public DetalleFactura(Factura factura, Insumo insumo, Proveedor proveedor, Remito remito, Integer cantidad, Float precio, Float importe, 
			String nroFactura, String nroRemito, String nombreInsumo, String nroLote, String nroArticulo, String unidadMedida, 
			Date fechaVto, String nroCuit, String nombreProveedor) {
		this.factura = factura;
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
		this.cantidad = cantidad;
		this.precio = precio;
		this.importe = importe;
		// datos adicionales para DetalleFacturaFX
		this.nroFactura = nroFactura; 
		this.nroRemito = nroRemito;
		this.nombreInsumo = nombreInsumo;
		this.nroLote = nroLote;
		this.nroArticulo = nroArticulo;
		this.unidadMedida = unidadMedida;
		this.fechaVtoInsumo = fechaVto;
		this.nroCuit = nroCuit;
		this.nombreProveedor = nombreProveedor;
	}
	

	public DetalleFactura(DetalleFacturaId id, Factura factura, Insumo insumo, Proveedor proveedor, Remito remito, Integer cantidad) {
		this.id = id;
		this.factura = factura;
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
		this.cantidad = cantidad;
	}

	public DetalleFactura(DetalleFacturaId id, Factura factura, Insumo insumo, Proveedor proveedor, Remito remito,
			Integer cantidad, Float precio, Float importe) {
		this.id = id;
		this.factura = factura;
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
		this.cantidad = cantidad;
		this.precio = precio;
		this.importe = importe;
	}

	//Constructor sin ID
	public DetalleFactura(Factura factura, Insumo insumo, Proveedor proveedor, Integer cantidad,
			Float precio) {
		this.factura = factura;
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.cantidad = cantidad;
		this.precio = precio;
	}
	
	
	//constructor para el guardar detalle
	public DetalleFactura(DetalleFacturaId id, Factura factura, Insumo insumo, Proveedor proveedor,
								Integer cantidad, Float precio, Float importe) {
		this.id = id;
		this.factura = factura;
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.cantidad = cantidad;
		this.precio = precio;
		this.importe = importe;
	}
	
	
	/////////////////////GET y SET////////////////////////////////////////
	
	

	public DetalleFacturaId getId() {
		return this.id;
	}

	public void setId(DetalleFacturaId id) {
		this.id = id;
	}

	public Factura getFactura() {
		return this.factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public Insumo getInsumo() {
		return this.insumo;
	}

	public void setInsumo(Insumo insumo) {
		this.insumo = insumo;
	}

	public Proveedor getProveedor() {
		return this.proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Float getPrecio() {
		return this.precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}
	
	public Remito getRemito() {
		return this.remito;
	}

	public void setRemito(Remito remito) {
		this.remito = remito;
	}
	
	public Float getImporte() {
		return this.importe;
	}

	public void setImporte(Float importe) {
		this.importe = importe;
	}

	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	public String getNroLote() {
		return nroLote;
	}

	public void setNroLote(String nroLote) {
		this.nroLote = nroLote;
	}

	public String getNroCuit() {
		return nroCuit;
	}

	public void setNroCuit(String nroCuit) {
		this.nroCuit = nroCuit;
	}

	public String getNombreInsumo() {
		return nombreInsumo;
	}

	public void setNombreInsumo(String nombreInsumo) {
		this.nombreInsumo = nombreInsumo;
	}

	public String getNroArticulo() {
		return nroArticulo;
	}

	public void setNroArticulo(String nroArticulo) {
		this.nroArticulo = nroArticulo;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public Date getFechaVtoInsumo() {
		return fechaVtoInsumo;
	}

	public void setFechaVtoInsumo(Date fechaVto) {
		this.fechaVtoInsumo = fechaVto;
	}

	public String getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(String nroRemito) {
		this.nroRemito = nroRemito;
	}


	public Integer getIdCategoria() {
		return idCategoria;
	}


	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}


	public String getNombreCategoria() {
		return nombreCategoria;
	}


	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}


	public String getNombreSector() {
		return nombreSector;
	}


	public void setNombreSector(String nombreSector) {
		this.nombreSector = nombreSector;
	}


	public String getNombreProveedor() {
		return nombreProveedor;
	}


	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}


	public String getReferencia() {
		return referencia;
	}


	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
}
