package modelo;

import java.util.Date;

public class Movimiento implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idMov;
	private Insumo insumo;
	private Usuario usuario;
	private String nombreUsuario;
	private String apellidoUsuario;
	private Date fechaMovimiento;
	private String descripcion;

	private String sucursal;
	
	private String nroLote;
	private String nombreInsumo;
	/**************************** CONSTRUCTOR *********************************/
	
	public Movimiento() {
		
	}
	
	
	/** constructor para query filtro por fecha, agrego nro lote y nombre insumo **/
	public Movimiento(Insumo insumo, Usuario usuario, String nombreUsuario, String apellidoUsuario,
						Date fechaMovimiento, String descripcion, String nroLote, String nombreInsumo, String sucursal) {
		this.insumo = insumo;
		this.usuario = usuario;
		this.nombreUsuario = nombreUsuario;
		this.apellidoUsuario = apellidoUsuario;
		this.fechaMovimiento = fechaMovimiento;
		this.descripcion = descripcion;
		this.nroLote = nroLote;
		this.nombreInsumo = nombreInsumo;
		this.sucursal = sucursal;
	}
	
	
	public Movimiento(Insumo insumo, Usuario usuario) {
		this.insumo = insumo;
		this.usuario = usuario;
	}

	
	public Movimiento(Insumo insumo, Usuario usuario, String nombreUsuario, String apellidoUsuario,
						Date fechaMovimiento, String descripcion, String suc) {
		this.insumo = insumo;
		this.usuario = usuario;
		this.nombreUsuario = nombreUsuario;
		this.apellidoUsuario = apellidoUsuario;
		this.fechaMovimiento = fechaMovimiento;
		this.descripcion = descripcion;
		this.sucursal = suc;
	}
	
	
	/**************************** GET - SET **********************************/
	

	public Integer getIdMov() {
		return this.idMov;
	}

	public void setIdMov(Integer idMov) {
		this.idMov = idMov;
	}

	public Insumo getInsumo() {
		return this.insumo;
	}

	public void setInsumo(Insumo insumo) {
		this.insumo = insumo;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNombreUsuario() {
		return this.nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getApellidoUsuario() {
		return this.apellidoUsuario;
	}

	public void setApellidoUsuario(String apellidoUsuario) {
		this.apellidoUsuario = apellidoUsuario;
	}

	public Date getFechaMovimiento() {
		return this.fechaMovimiento;
	}

	public void setFechaMovimiento(Date fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNroLote() {
		return nroLote;
	}

	public void setNroLote(String nroLote) {
		this.nroLote = nroLote;
	}

	public String getNombreInsumo() {
		return nombreInsumo;
	}

	public void setNombreInsumo(String nombreInsumo) {
		this.nombreInsumo = nombreInsumo;
	}
	
	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String suc) {
		this.sucursal = suc;
	}
	
}
