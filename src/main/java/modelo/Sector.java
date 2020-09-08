package modelo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Sector implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idSector;
	private String nombreSector;
	private Date fechaAlta;
	private Date fechaBaja;
	private String estadoSector;
	private Set<UsuarioSector> usuarioSectors = new HashSet<UsuarioSector>(0);
	private Set<Categoria> categorias = new HashSet<Categoria>(0);

	
	//////////////////////////////CONSTRUCTOR////////////////////////////
	
	
	public Sector() {
	}

	public Sector(String nombreSector) {
		this.nombreSector = nombreSector;
	}

	public Sector(String nombreSector, Date fechaAlta, Date fechaBaja, String estadoSector,
			Set<UsuarioSector> usuarioSectors, Set<Categoria> categorias) {
//	public Sector(String nombreSector, Date fechaAlta, Date fechaBaja, String estadoSector) {
		this.nombreSector = nombreSector;
		this.fechaAlta = fechaAlta;
		this.fechaBaja = fechaBaja;
		this.estadoSector = estadoSector;
		this.usuarioSectors = usuarioSectors;
		this.categorias = categorias;
	}

	public Sector(String nombreSector, Date fechaActual, String estado) {
		this.nombreSector = nombreSector;
		this.fechaAlta = fechaActual;
		this.estadoSector = estado;
	}

	
	
	/////////////////////GET y SET////////////////////////////////////////
	
	public Integer getIdSector() {
		return this.idSector;
	}

	public void setIdSector(Integer idSector) {
		this.idSector = idSector;
	}

	public String getNombreSector() {
		return this.nombreSector;
	}

	public void setNombreSector(String nombreSector) {
		this.nombreSector = nombreSector;
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

	public String getEstadoSector() {
		return this.estadoSector;
	}

	public void setEstadoSector(String estadoSector) {
		this.estadoSector = estadoSector;
	}

	public Set<UsuarioSector> getUsuarioSectors() {
		return this.usuarioSectors;
	}

	public void setUsuarioSectors(Set<UsuarioSector> usuarioSectors) {
		this.usuarioSectors = usuarioSectors;
	}

	public Set<Categoria> getCategorias() {
		return this.categorias;
	}

	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}
	
}
