package modeloAux;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import modelo.Usuario;

public class UsuarioFX extends Fx<Usuario> {
	
	public final IntegerProperty dni = new SimpleIntegerProperty();
	public final StringProperty user = new SimpleStringProperty();
	public final StringProperty nombre = new SimpleStringProperty();
	public final StringProperty apellido = new SimpleStringProperty();
	public final StringProperty password = new SimpleStringProperty();
	public final StringProperty estadoUsuario = new SimpleStringProperty();
	public final StringProperty admin = new SimpleStringProperty();
	
	
	/**************************** CONSTRUCTOR *********************************/
	
	public UsuarioFX() {
		super();
	}
	
	
	public UsuarioFX(Usuario u) {
		from(u);
	}
	
	
	/********************************** METODOS ***********************************/
	
	
	@Override
	public UsuarioFX from(Usuario u) {
		dni.set(u.getPkDniUsuario());
		user.set(u.getUser());
		nombre.set(u.getNombre());
		apellido.set(u.getApellido());
		password.set(u.getPassword());
		estadoUsuario.set(u.getEstadoUsuario());
		admin.set(u.getAdmin());
		
		return this;
	}

	@Override
	public Usuario to(Usuario u) {
		u.setPkDniUsuario(dni.get());
		u.setUser(user.get());
		u.setNombre(nombre.get());
		u.setApellido(apellido.get());
		u.setPassword(password.get());
		u.setEstadoUsuario(estadoUsuario.get());
		u.setAdmin(admin.get());
		
		return u;
	}

}
