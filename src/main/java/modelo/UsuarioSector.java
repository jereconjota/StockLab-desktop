package modelo;

public class UsuarioSector implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsuarioSectorId id;
	private Sector sector;
	private Usuario usuario;
	private String nombreUsuario;
	private String apellidoUsuario;

	public UsuarioSector() {
	}

	public UsuarioSector(UsuarioSectorId id, Sector sector, Usuario usuario) {
		this.id = id;
		this.sector = sector;
		this.usuario = usuario;
	}

	public UsuarioSector(UsuarioSectorId id, Sector sector, Usuario usuario, String nombreUsuario,
			String apellidoUsuario) {
		this.id = id;
		this.sector = sector;
		this.usuario = usuario;
		this.nombreUsuario = nombreUsuario;
		this.apellidoUsuario = apellidoUsuario;
	}

	public UsuarioSectorId getId() {
		return this.id;
	}

	public void setId(UsuarioSectorId id) {
		this.id = id;
	}

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
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

}
