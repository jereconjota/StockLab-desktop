package modelo;

import java.util.Date;

public class DetalleRemito implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private DetalleRemitoId id;
	private Insumo insumo;
	private Proveedor proveedor;
	private Remito remito;
	private Integer cantidad;
	
	//datos adicionales para detalleremitoFX
	private String nroLote;
	private String nroCuit;
	private	String nroRemito;
	private	String nombreInsumo;
	private	String nroArticulo;
	private String unidadMedida;
	private Date fechaVtoInsumo;
	
	//datos extras
	private Integer idCategoria;
	private String nombreCategoria;
	private String nombreSector;
	private String cuitProveedor;
	private String nombreProveedor;
	
	//////////////////////////////CONSTRUCTOR////////////////////////////

	public DetalleRemito() {
	}
	
	
	/** constructor para detalleRemitoFX que agrega atributos adicionales 
	 * 		lo llamo desde el query "obtenerListaDetalleRemitoPorIdRemito1(Integer idRemitoIN)" **/
	public DetalleRemito(Insumo insumo, Proveedor proveedor, Remito remito, Integer cantidad, String nroLote, 
			String nroCuit, String nroRemito, String nombreInsumo, String nroArticulo, String unidadMedida, Date fechaVtoInsumo) {
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
		this.cantidad = cantidad;
		this.nroLote = nroLote;
		this.nroCuit = nroCuit;
		this.nroRemito = nroRemito;
		this.nombreInsumo = nombreInsumo;
		this.nroArticulo = nroArticulo;
		this.unidadMedida = unidadMedida;
		this.fechaVtoInsumo = fechaVtoInsumo;
	}
	
	
	/** constructor compatible con detalleremitoFX y ademas agrega atrubutos de categoria, sector, y proveedor 
	 * 		lo llamo desde el query "obtenerListaDetalleRemitoPorIdRemito2(Integer idRemitoIN)" **/
	public DetalleRemito(Insumo insumo, Proveedor proveedor, Remito remito, Integer cantidad, String nroLote, 
			String nroCuit, String nroRemito, String nombreInsumo, String nroArticulo, String unidadMedida, Date fechaVtoInsumo,
			Integer idCategoria, String nombreCategoria, String nombreSector, String cuitProveedor, String nombreProveedor) {
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
		this.cantidad = cantidad;
		this.nroLote = nroLote;
		this.nroCuit = nroCuit;
		this.nroRemito = nroRemito;
		this.nombreInsumo = nombreInsumo;
		this.nroArticulo = nroArticulo;
		this.unidadMedida = unidadMedida;
		this.fechaVtoInsumo = fechaVtoInsumo;
		this.idCategoria = idCategoria;
		this.nombreCategoria = nombreCategoria;
		this.nombreSector = nombreSector;
		this.cuitProveedor = cuitProveedor;
		this.nombreProveedor = nombreProveedor;
	}
	
	
	/** constructor para detalleRemitoFX que agrega atributos adicionales 
	 * 		lo llamo desde el query "obtenerListaDetalleRemitoPorIdRemito3(Integer idRemitoIN)" **/
	public DetalleRemito(Insumo insumo, Proveedor proveedor, Remito remito, Integer cantidad, String nroLote, 
			String nroCuit, String nombreProveedor, String nroRemito, String nombreInsumo, String nroArticulo, String unidadMedida, Date fechaVtoInsumo) {
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
		this.cantidad = cantidad;
		this.nroLote = nroLote;
		this.nroCuit = nroCuit;
		this.nombreProveedor = nombreProveedor;
		this.nroRemito = nroRemito;
		this.nombreInsumo = nombreInsumo;
		this.nroArticulo = nroArticulo;
		this.unidadMedida = unidadMedida;
		this.fechaVtoInsumo = fechaVtoInsumo;
	}
	

	public DetalleRemito(DetalleRemitoId id, Insumo insumo, Proveedor proveedor, Remito remito) {
		this.id = id;
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
	}

	public DetalleRemito(DetalleRemitoId id, Insumo insumo, Proveedor proveedor, Remito remito, Integer cantidad) {
		this.id = id;
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
		this.cantidad = cantidad;
	}
	
	//Constructor sin ID
	public DetalleRemito(Insumo insumo, Proveedor proveedor, Remito remito, Integer cantidad) {
		this.insumo = insumo;
		this.proveedor = proveedor;
		this.remito = remito;
		this.cantidad = cantidad;
	}
	
	
	/////////////////////GET y SET////////////////////////////////////////

	public DetalleRemitoId getId() {
		return this.id;
	}

	public void setId(DetalleRemitoId id) {
		this.id = id;
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

	public Remito getRemito() {
		return this.remito;
	}

	public void setRemito(Remito remito) {
		this.remito = remito;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
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

	public String getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(String nroRemito) {
		this.nroRemito = nroRemito;
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


	public void setFechaVtoInsumo(Date fechaVtoInsumo) {
		this.fechaVtoInsumo = fechaVtoInsumo;
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


	public String getCuitProveedor() {
		return cuitProveedor;
	}


	public void setCuitProveedor(String cuitProveedor) {
		this.cuitProveedor = cuitProveedor;
	}


	public String getNombreProveedor() {
		return nombreProveedor;
	}


	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}
	
	

}
