package hibernate.util;

/**
 *  clase creada para la obtencion del nodo (hibernate.connection.url) del archivo de conexion hibernate
 *  de esta forma, saber en q dir IP se hace la conexion. Por consiguiente saber la sucursal
 */

public class Conexion {
	
	private String url;
	
	public Conexion() {
		// TODO Auto-generated constructor stub
	}

	
	public String getUrl() {
		return this.url;
	}
	
	
	public void setUrl(String nodeValue) {
		this.url = nodeValue;
	}
	

}
