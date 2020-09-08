package modelo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Insumo implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idInsumo;
	private Categoria categoria;
	private Sucursal sucursal;
	private String nroLote;
	private String nombreInsumo;
	private String nroArticulo;
	private String referencia;
	private Date fechaVencimiento;
	private String ubicacion;
	private String temperatura;
	private Float precioInsumo;
	private Date fechaUso;
	private String unidadMedida;
	private String estadoInsumo;
	private Integer pdp;
	private Integer stockActual;
	private Integer stockReal;
	private Date fechaIngreso;
	private Date fechaBaja;
	private Set<DetalleFactura> detalleFacturas = new HashSet<DetalleFactura>(0);
	private Set<Movimiento> movimientos = new HashSet<Movimiento>(0);
	private Set<DetalleRemito> detalleRemitos = new HashSet<DetalleRemito>(0);
	private Proveedor proveedor;
	private Integer nroPedido;
	
	//////////// datos adicionales para insumoFX
	private Long stockGeneral; ///calculable
	
	private String nombreCategoria; ///no forma parte de la entidad insumo, pero teniendo el id categoria, 
									///puedo acceder a entidad categoria y obtener su nombre para setear este atributo
	private String nombreSector;
	
	private String nombreProveedor;
	
	private String nombreSucursal;

	//////////////////////////////CONSTRUCTOR////////////////////////////
	
	public Insumo() {
		
	}
	
	
	/**
	 * constructor mas completo
	 */
	public Insumo(Categoria categoria, Sucursal sucursal, Proveedor proveedor, String nroLote, String nombreInsumo, String nroArticulo, String referencia, 
					Date fechaVto, String ubicacion, String temperatura, Float precioInsumo, Date fechaUso, String unidadMedida, String estadoInsumo, Integer pdp,
					Integer stockActual, Date fechaIngreso, Date fechaBaja) {
		this.categoria = categoria;
		this.sucursal = sucursal;
		this.proveedor = proveedor;
		this.nroLote = nroLote;
		this.nombreInsumo = nombreInsumo;
		this.nroArticulo = nroArticulo;
		this.referencia = referencia;
		this.fechaVencimiento = fechaVto;
		this.ubicacion = ubicacion;
		this.temperatura = temperatura;
		this.precioInsumo = precioInsumo;
		this.fechaUso = fechaUso;
		this.unidadMedida = unidadMedida;
		this.estadoInsumo = estadoInsumo;
		this.pdp = pdp;
		this.stockActual = stockActual;
		this.fechaIngreso = fechaIngreso;
		this.fechaBaja = fechaBaja;
	}
	
	public Insumo(Integer idInsumo, Categoria categoria, String nroLote, String nombreInsumo, String nroArticulo, String referencia, 
			Date fechaVencimiento, String temperatura, Float precioInsumo, String unidadMedida, String estadoInsumo, Integer pdp, Integer stockActual, 
			Integer nroPedido, String nombreCategoria) {
		this.idInsumo = idInsumo;
		this.categoria = categoria;
		this.nroLote = nroLote;
		this.nombreInsumo = nombreInsumo;
		this.nroArticulo = nroArticulo;
		this.referencia = referencia;
		this.fechaVencimiento = fechaVencimiento;
		this.temperatura = temperatura;
		this.precioInsumo = precioInsumo;
		this.unidadMedida = unidadMedida;
		this.estadoInsumo = estadoInsumo;
		this.pdp = pdp;
		this.stockActual = stockActual;
		this.nroPedido = nroPedido;
		this.nombreCategoria = nombreCategoria;
	}
	
	
	/**constructor para tabla insumos activos gral x articulo, para pant secundaria de fac/remito **/
	public Insumo(Integer idInsumo, Categoria categoria, String nroLote, String nombreInsumo, String nroArticulo, String referencia, 
			Date fechaVencimiento, String temperatura, Float precioInsumo, String unidadMedida, 
			String estadoInsumo, Integer pdp, String nombreSector, String nombreCategoria) {
		this.idInsumo = idInsumo;
		this.categoria = categoria;
		this.nroLote = nroLote;
		this.nombreInsumo = nombreInsumo;
		this.nroArticulo = nroArticulo;
		this.referencia = referencia;
		this.fechaVencimiento = fechaVencimiento;
		this.temperatura = temperatura;
		this.precioInsumo = precioInsumo;
		this.unidadMedida = unidadMedida;
		this.estadoInsumo = estadoInsumo;
		this.pdp = pdp;
		this.nombreSector = nombreSector;
		this.nombreCategoria = nombreCategoria;
	}
	
	
//	(Integer idInsumo, String nombreInsumo, String nroLote, String nroArticulo, String referencia, String unidadMedida, 
//			Integer stockActual, Date fechaIngreso, Date fechaBaja, Date fechaVencimiento, Float precioInsumo, String temperatura,
//			Categoria idCategoria, String nombreCategoria) {
	
	/*constructor para tabla insumos PDP para pant Orden De Compra */
	public Insumo(Integer idInsumo, Categoria categoria, String nroLote, String nombreInsumo, String unidadMedida, 
			String nroArticulo, String referencia, Float precioInsumo, Integer nroPedido, 
			Long stockGeneral, Integer pdp, String estadoInsumo,String nombreSector, String nombreCategoria) {
		this.idInsumo = idInsumo;
		this.categoria = categoria;
		this.nroLote = nroLote;
		this.nombreInsumo = nombreInsumo;
		this.unidadMedida = unidadMedida;

		this.nroArticulo = nroArticulo;
		this.referencia = referencia;
		this.precioInsumo = precioInsumo;
		this.nroPedido = nroPedido;

		this.stockGeneral = stockGeneral;
		this.pdp = pdp;
		this.estadoInsumo = estadoInsumo;
		this.nombreSector = nombreSector;
		this.nombreCategoria = nombreCategoria;
	}
	
	/** constructor para guardar movimiento desde modif insumo, cuando se cambia el stock gral **/
	public Insumo(Long stockGral) {
		this.stockGeneral = stockGral;
	}
	
	
	/**** constructor para q deje de joder el nombre categoria en la clase InsumoFX, ya
	 * 		q este al no formar parte de la entidad insumo y solo tener su id
	 * 		provoca null 
	 */
	public Insumo(Integer idInsumo, String nombreInsumo, String nroLote, String nroArticulo, String referencia, String unidadMedida, 
					Integer stockActual, Date fechaIngreso, Date fechaBaja, Date fechaVencimiento, Float precioInsumo, String temperatura,
					Integer nroPedido, Categoria idCategoria, String nombreCategoria) {
		this.idInsumo = idInsumo;
		this.nombreInsumo = nombreInsumo;
		this.nroLote = nroLote;
		this.nroArticulo = nroArticulo;
		this.referencia = referencia;
		this.unidadMedida = unidadMedida;
		this.stockActual = stockActual;
		this.fechaIngreso = fechaIngreso;
		this.fechaBaja = fechaBaja;
		this.fechaVencimiento = fechaVencimiento;
		this.precioInsumo = precioInsumo;
		this.temperatura = temperatura;
		this.nroPedido = nroPedido;
		this.categoria = idCategoria;
		this.nombreCategoria = nombreCategoria;
	}
	
	/******* contructor para la tabla PDP, q necesitamos mostrar stock general, el cual es un calculable ******/
	public Insumo(String nombreInsumo, Long stockGeneral, Integer pdp) {
		this.nombreInsumo = nombreInsumo;
		this.stockGeneral = stockGeneral;
		this.pdp = pdp;
	}
	
	
	/******* contructor para la tabla PDP, q necesitamos mostrar stock general, el cual es un calculable, ademas muestra proveedor ******/
	public Insumo(String nombreInsumo, Long stockGeneral, Integer pdp, String nombreProveedor, String nomsec, String art, String ref, Categoria idCategoria, String nombreCat) {
		this.nombreInsumo = nombreInsumo;
		this.stockGeneral = stockGeneral;
		this.pdp = pdp;
		this.nombreProveedor = nombreProveedor;
		
		this.nombreSector = nomsec;
		this.nroArticulo = art;
		this.referencia = ref;
		this.categoria = idCategoria;
		this.nombreCategoria = nombreCat;
	}
	
	
	/******* contructor para la tabla PDP, q necesitamos mostrar stock general, cuando se aplica filtro x sector ******/
	public Insumo(String nombreInsumo, Long stockGeneral, Integer pdp, Categoria idCategoria, String nombreCategoria, String nomsec, String art, String ref, String nombreProveedor) {
		this.nombreInsumo = nombreInsumo;
		this.stockGeneral = stockGeneral;
		this.pdp = pdp;
		this.categoria = idCategoria;
		this.nombreCategoria = nombreCategoria;
		this.nombreSector = nomsec;
		this.nroArticulo = art;
		this.referencia = ref;
		this.nombreProveedor = nombreProveedor;
	}
	
	
	/******* contructor para la tabla Insumos Gral (sin aplicar filtro sector/categoria), q necesitamos mostrar stock general, el cual es un calculable ******/
	public Insumo(String nombreInsumo, String unidadMedida, Long stockGeneral, Integer pdp, String estadoInsumo) {
		this.nombreInsumo = nombreInsumo;
		this.unidadMedida = unidadMedida;
		this.stockGeneral = stockGeneral;
		this.pdp = pdp;
		this.estadoInsumo = estadoInsumo;
	}
	
	public Insumo(String nombreInsumo, String unidadMedida, Long stockGeneral, Integer pdp, String estadoInsumo, String nombreSucursal) {
		this.nombreInsumo = nombreInsumo;
		this.unidadMedida = unidadMedida;
		this.stockGeneral = stockGeneral;
		this.pdp = pdp;
		this.estadoInsumo = estadoInsumo;
		this.nombreSucursal = nombreSucursal;
	}
	

	public Insumo(Categoria categoria, String nombreInsumo) {
		this.categoria = categoria;
		this.nombreInsumo = nombreInsumo;
	}

//	public Insumo(Categoria categoria, String nroLote, String nombreInsumo, String nroArticulo, String referencia,
//			Date fechaVencimiento, String temperatura, Float precioInsumo, Date fechaUso, String unidadMedida,
//			String estadoInsumo, Integer pdp, Integer stockActual, Integer stockReal,
//			Date fechaIngreso, Date fechaBaja, Set<DetalleFactura> detalleFacturas, Set<Movimiento> movimientos,
//			Set<DetalleRemito> detalleRemitos) {
	public Insumo(Categoria categoria, String nroLote, String nombreInsumo, String nroArticulo, String referencia,
			Date fechaVencimiento, String temperatura, Float precioInsumo, Date fechaUso, String unidadMedida,
			String estadoInsumo, Integer pdp, Integer stockActual, Integer stockReal,
			Date fechaIngreso, Date fechaBaja) {
		this.categoria = categoria;
		this.nroLote = nroLote;
		this.nombreInsumo = nombreInsumo;
		this.nroArticulo = nroArticulo;
		this.referencia = referencia;
		this.fechaVencimiento = fechaVencimiento;
		this.temperatura = temperatura;
		this.precioInsumo = precioInsumo;
		this.fechaUso = fechaUso;
		this.unidadMedida = unidadMedida;
		this.estadoInsumo = estadoInsumo;
		this.pdp = pdp;
		this.stockActual = stockActual;
		this.stockReal = stockReal;
		this.fechaIngreso = fechaIngreso;
		this.fechaBaja = fechaBaja;
//		this.detalleFacturas = detalleFacturas;
//		this.movimientos = movimientos;
//		this.detalleRemitos = detalleRemitos;
	}

	
	public Insumo(Categoria categoria, String nombreInsumo, Integer pdp, Date fechaIngreso, String estado) {
		this.categoria = categoria;
		this.nombreInsumo = nombreInsumo;
		this.pdp = pdp;
		this.fechaIngreso = fechaIngreso;
		this.estadoInsumo = estado;
	}
	
	
	public Insumo(Categoria categoria, String nombreInsumo, String nroArticulo, String referencia, Date fechaVto,
						String temperatura, String unidadMedida, int pdp, Date fechaIngreso, String estado) {
		this.categoria = categoria;
		this.nombreInsumo = nombreInsumo;
		this.nroArticulo = nroArticulo;
		this.referencia = referencia;
		this.fechaVencimiento = fechaVto;
		this.temperatura = temperatura;
		this.unidadMedida = unidadMedida;
		this.pdp = pdp;
		this.fechaIngreso = fechaIngreso;
		this.estadoInsumo = estado;
	}
	
	public Insumo(Categoria categoria, String nombreInsumo, String nroArticulo, String referencia, Date fechaVto,
			String temperatura, String unidadMedida, int pdp, Date fechaIngreso, String estado, Sucursal suc) {
		this.categoria = categoria;
		this.nombreInsumo = nombreInsumo;
		this.nroArticulo = nroArticulo;
		this.referencia = referencia;
		this.fechaVencimiento = fechaVto;
		this.temperatura = temperatura;
		this.unidadMedida = unidadMedida;
		this.pdp = pdp;
		this.fechaIngreso = fechaIngreso;
		this.estadoInsumo = estado;
		this.sucursal = suc;
		}
	
	//Para obtener el proveedor PRUEBA
//	public Insumo(Integer idInsumo, String nombreInsumo, Proveedor prov, String nombreProv ) {
//		this.proveedor = prov;
//		this.nombreInsumo = nombreInsumo;
//		this.idInsumo = idInsumo;
//		this.nombreProveedor = nombreProv;
//	}
	
	
	public Insumo(Integer idInsumo, String nombreInsumo, String nroArt, String ref, Integer nroPedido, Proveedor prov,  String nombreProv, Categoria cat, 
			String nombresector, String nombrecategoria) {
			this.proveedor = prov;
			this.nombreInsumo = nombreInsumo;
			this.idInsumo = idInsumo;
			this.nroArticulo = nroArt;
			this.proveedor = prov;
			this.nombreProveedor = nombreProv;
			this.categoria = cat;
			this.nombreSector = nombresector;
			this.nombreCategoria = nombrecategoria;
			this.referencia = ref;
			this.nroPedido = nroPedido;
		}
	/////////////////////GET y SET////////////////////////////////////////
	

	public Integer getIdInsumo() {
		return this.idInsumo;
	}

	public void setIdInsumo(Integer idInsumo) {
		this.idInsumo = idInsumo;
	}

	public Categoria getCategoria() {
		return this.categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getNroLote() {
		return this.nroLote;
	}

	public void setNroLote(String nroLote) {
		this.nroLote = nroLote;
	}

	public String getNombreInsumo() {
		return this.nombreInsumo;
	}

	public void setNombreInsumo(String nombreInsumo) {
		this.nombreInsumo = nombreInsumo;
	}

	public String getNroArticulo() {
		return this.nroArticulo;
	}

	public void setNroArticulo(String nroArticulo) {
		this.nroArticulo = nroArticulo;
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Date getFechaVencimiento() {
		return this.fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getTemperatura() {
		return this.temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public Float getPrecioInsumo() {
		return this.precioInsumo;
	}

	public void setPrecioInsumo(Float precioInsumo) {
		this.precioInsumo = precioInsumo;
	}

	public Date getFechaUso() {
		return this.fechaUso;
	}

	public void setFechaUso(Date fechaUso) {
		this.fechaUso = fechaUso;
	}

	public String getUnidadMedida() {
		return this.unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public String getEstadoInsumo() {
		return this.estadoInsumo;
	}

	public void setEstadoInsumo(String estadoInsumo) {
		this.estadoInsumo = estadoInsumo;
	}

	public Integer getPdp() {
		return this.pdp;
	}

	public void setPdp(Integer pdp) {
		this.pdp = pdp;
	}

	public Integer getStockActual() {
		return this.stockActual;
	}

	public void setStockActual(Integer stockActual) {
		this.stockActual = stockActual;
	}

	public Integer getStockReal() {
		return this.stockReal;
	}

	public void setStockReal(Integer stockReal) {
		this.stockReal = stockReal;
	}

	public Date getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Date getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
	public String getUbicacion() {
		return this.ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Set<DetalleFactura> getDetalleFacturas() {
		return this.detalleFacturas;
	}

	public void setDetalleFacturas(Set<DetalleFactura> detalleFacturas) {
		this.detalleFacturas = detalleFacturas;
	}

	
	public Set<Movimiento> getMovimientos() {
		return this.movimientos;
	}

	public void setMovimientos(Set<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public Set<DetalleRemito> getDetalleRemitos() {
		return this.detalleRemitos;
	}

	public void setDetalleRemitos(Set<DetalleRemito> detalleRemitos) {
		this.detalleRemitos = detalleRemitos;
	}	
	
	public Long getStockGeneral() {
		return stockGeneral;
	}

	public void setStockGeneral(Long stockGeneral) {
		this.stockGeneral = stockGeneral;
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
	
	
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}


	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}


	public Integer getNroPedido() {
		return nroPedido;
	}

	public void setNroPedido(Integer nroPedido) {
		this.nroPedido = nroPedido;
	}
	
	
}
