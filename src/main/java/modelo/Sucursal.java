package modelo;

import java.util.HashSet;
import java.util.Set;

public class Sucursal implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer pkIdSucursal;
	private String nombreSucursal;
	private String direccionSucursal;
	private String telefonoSucursal;
	private Set<Insumo> insumo = new HashSet<Insumo>(0);
	private Set<Usuario> usuario = new HashSet<Usuario>(0);

	
	//////////////////////////////CONSTRUCTOR////////////////////////////

	public Sucursal() {
		
	}
	
	
	public Sucursal(Integer pkIdSuc) {
		this.pkIdSucursal = pkIdSuc;
	}
	
	
	public Sucursal(String nombreSuc) {
		this.nombreSucursal = nombreSuc;
	}
	
	
	public Sucursal(String nombreSuc, String direccionS, String telefonoSuc) {
		this.nombreSucursal = nombreSuc;
		this.direccionSucursal = direccionS;
		this.telefonoSucursal = telefonoSuc;
	}
	
	
	public Sucursal(Integer pkIdSuc, String nombreSuc, String direccionS, String telefonoSuc) {
		
	}
	
	
	/////////////////////GET y SET////////////////////////////////////////

	public Integer getPkIdSucursal() {
		return pkIdSucursal;
	}

	public void setPkIdSucursal(Integer pkIdSucursal) {
		this.pkIdSucursal = pkIdSucursal;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public String getDireccionSucursal() {
		return direccionSucursal;
	}

	public void setDireccionSucursal(String direccionSucursal) {
		this.direccionSucursal = direccionSucursal;
	}

	public String getTelefonoSucursal() {
		return telefonoSucursal;
	}

	public void setTelefonoSucursal(String telefonoSucursal) {
		this.telefonoSucursal = telefonoSucursal;
	}

	public Set<Insumo> getInsumo() {
		return insumo;
	}

	public void setInsumo(Set<Insumo> insumo) {
		this.insumo = insumo;
	}

	public Set<Usuario> getUsuario() {
		return usuario;
	}

	public void setUsuario(Set<Usuario> usuario) {
		this.usuario = usuario;
	}
}
