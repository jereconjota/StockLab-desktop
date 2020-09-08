package modelo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Categoria implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer pkIdCategoria;
	private Sector sector;
	private String nombreCategoria;
	private Date fechaAlta;
	private Date fechaBaja;
	private String estadoCategoria;
	private Set<Insumo> insumos = new HashSet<Insumo>(0);
	
	
	//////////////////////////////CONSTRUCTOR////////////////////////////

	public Categoria() {
		
	}
	
	
	public Categoria(Integer pkIdCategoria) {
		this.pkIdCategoria = pkIdCategoria;
	}
	
	
	public Categoria(Integer pkIdCategoria, String nombreCategoria) {
		this.pkIdCategoria = pkIdCategoria;
		this.nombreCategoria = nombreCategoria;
	}


	public Categoria(Sector sector, String nombreCategoria) {
		this.sector = sector;
		this.nombreCategoria = nombreCategoria;
	}

	
	public Categoria(Sector sector, String nombreCategoria, Date fechaAlta, Date fechaBaja, String estadoCategoria,
			Set<Insumo> insumos) {
//	public Categoria(Sector sector, String nombreCategoria, Date fechaAlta, Date fechaBaja, String estadoCategoria) {
		this.sector = sector;
		this.nombreCategoria = nombreCategoria;
		this.fechaAlta = fechaAlta;
		this.fechaBaja = fechaBaja;
		this.estadoCategoria = estadoCategoria;
		this.insumos = insumos;
	}

	
	public Categoria(String nombreCat, Sector sector, Date fechaAlta, String estado) {
		this.nombreCategoria = nombreCat;
		this.sector = sector;
		this.fechaAlta = fechaAlta;
		this.estadoCategoria = estado;
	}

	
	/////////////////////GET y SET////////////////////////////////////////
	
	
	public Integer getPkIdCategoria() {
		return this.pkIdCategoria;
	}

	public void setPkIdCategoria(Integer pkIdCategoria) {
		this.pkIdCategoria = pkIdCategoria;
	}

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public String getNombreCategoria() {
		return this.nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
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

	public String getEstadoCategoria() {
		return this.estadoCategoria;
	}

	public void setEstadoCategoria(String estadoCategoria) {
		this.estadoCategoria = estadoCategoria;
	}

	public Set<Insumo> getInsumos() {
		return this.insumos;
	}

	public void setInsumos(Set<Insumo> insumos) {
		this.insumos = insumos;
	}
	
}
