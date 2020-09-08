package modelo;

import java.util.HashSet;
import java.util.Set;

public class Usuario implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pkDniUsuario;
	private String user;
	private String nombre;
	private String apellido;
	private String password;
	private String estadoUsuario;
	private String admin;
	private Set<Movimiento> movimientos = new HashSet<Movimiento>(0);
	private Set<UsuarioSector> usuarioSectors = new HashSet<UsuarioSector>(0);
	private Sucursal sucursal;
	
	private String mail;

	
	//////////////////////////////CONSTRUCTOR////////////////////////////


	public Usuario() {
		
	}
	
	
	/***** constructor necesario para la baja de un sector ******/
	public Usuario(String estadoUsuario) {
		this.estadoUsuario = estadoUsuario;
	}

	
	/******* contructor para la tabla Usuario(Gestion) ******/
	public Usuario(int pkDniUsuario, String nombre, String apellido, String user, String password, String admin, String estadoUsuario) {
		this.pkDniUsuario = pkDniUsuario;
		this.nombre = nombre;
		this.apellido = apellido;
		this.user = user;
		this.password = password;
		this.admin = admin;
		this.estadoUsuario = estadoUsuario;
	}
	

	public Usuario(int pkDniUsuario, String user, String nombre, String apellido, String password) {
		this.pkDniUsuario = pkDniUsuario;
		this.user = user;
		this.nombre = nombre;
		this.apellido = apellido;
		this.password = password;
	}

	public Usuario(int pkDniUsuario, String user, String nombre, String apellido, String password, String estadoUsuario,
			String admin, Set<Movimiento> movimientos, Set<UsuarioSector> usuarioSectors) {
//	public Usuario(int pkDniUsuario, String user, String nombre, String apellido, String password, String estadoUsuario,
//					String admin) {
		this.pkDniUsuario = pkDniUsuario;
		this.user = user;
		this.nombre = nombre;
		this.apellido = apellido;
		this.password = password;
		this.estadoUsuario = estadoUsuario;
		this.admin = admin;
		this.movimientos = movimientos;
		this.usuarioSectors = usuarioSectors;
	}

	/******* contructor con mail ******/
	public Usuario(int pkDniUsuario, String nombre, String apellido, String user, String password, String admin, String estadoUsuario, String m) {
		this.pkDniUsuario = pkDniUsuario;
		this.nombre = nombre;
		this.apellido = apellido;
		this.user = user;
		this.password = password;
		this.admin = admin;
		this.estadoUsuario = estadoUsuario;
		this.mail = m;
	}
	
	///////////////////// GET y SET ////////////////////////////////////////
	
	public int getPkDniUsuario() {
		return this.pkDniUsuario;
	}

	public void setPkDniUsuario(int pkDniUsuario) {
		this.pkDniUsuario = pkDniUsuario;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEstadoUsuario() {
		return this.estadoUsuario;
	}

	public void setEstadoUsuario(String estadoUsuario) {
		this.estadoUsuario = estadoUsuario;
	}

	public String getAdmin() {
		return this.admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public Set<Movimiento> getMovimientos() {
		return this.movimientos;
	}

	public void setMovimientos(Set<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public Set<UsuarioSector> getUsuarioSectors() {
		return this.usuarioSectors;
	}

	public void setUsuarioSectors(Set<UsuarioSector> usuarioSectors) {
		this.usuarioSectors = usuarioSectors;
	}
	
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	
	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}


	@Override
	public String toString() {
		return "Usuario [pkDniUsuario=" + pkDniUsuario + ", user=" + user + ", nombre=" + nombre + ", apellido="
				+ apellido + ", password=" + password + ", estadoUsuario=" + estadoUsuario + ", admin=" + admin
				+ ", movimientos=" + movimientos + ", usuarioSectors=" + usuarioSectors + ", sucursal=" + sucursal
				+ ", mail=" + mail + "]";
	}
	
	
}
