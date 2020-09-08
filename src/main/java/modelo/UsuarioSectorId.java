package modelo;

public class UsuarioSectorId implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int fkDniUsuario;
	private int fkIdSector;

	public UsuarioSectorId() {
	}

	public UsuarioSectorId(int fkDniUsuario, int fkIdSector) {
		this.fkDniUsuario = fkDniUsuario;
		this.fkIdSector = fkIdSector;
	}

	public int getFkDniUsuario() {
		return this.fkDniUsuario;
	}

	public void setFkDniUsuario(int fkDniUsuario) {
		this.fkDniUsuario = fkDniUsuario;
	}

	public int getFkIdSector() {
		return this.fkIdSector;
	}

	public void setFkIdSector(int fkIdSector) {
		this.fkIdSector = fkIdSector;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UsuarioSectorId))
			return false;
		UsuarioSectorId castOther = (UsuarioSectorId) other;

		return (this.getFkDniUsuario() == castOther.getFkDniUsuario())
				&& (this.getFkIdSector() == castOther.getFkIdSector());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getFkDniUsuario();
		result = 37 * result + this.getFkIdSector();
		return result;
	}

}
