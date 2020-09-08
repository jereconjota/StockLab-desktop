package modeloAux;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
//import modelo.DetalleOrdenDeCompra;
import modelo.OrdenDeCompra;
//import modelo.Proveedor;
//import modelo.Usuario;

public class OrdenDeCompraFX extends Fx<OrdenDeCompra>{
	
	public final ObjectProperty<Integer> pkIdOrdenCompra = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> idUsuario = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> idProveedor = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> nroOrdenCompra = new SimpleObjectProperty<>();
	public final ObjectProperty<Date> fechaOrdenCompra = new SimpleObjectProperty<>();
	public final SimpleStringProperty observacion = new SimpleStringProperty();
	
	public final StringProperty nombreUsuario = new SimpleStringProperty();
	public final StringProperty nombreProveedor = new SimpleStringProperty();
	
/**************************** CONSTRUCTOR *********************************/
	
	public OrdenDeCompraFX() {
		super();
	}
	
	public OrdenDeCompraFX(OrdenDeCompra i) {
		from(i);
	}
	
	/********************************** METODOS ***********************************/
	
	@Override
	public OrdenDeCompraFX from(OrdenDeCompra i) {
		pkIdOrdenCompra.set(i.getPkIdOrdenCompra());
		idUsuario.set(i.getUsuario().getPkDniUsuario());
		idProveedor.set(i.getProveedor().getPkIdProveedor());
		nroOrdenCompra.set(i.getNroOrdenCompra());
		fechaOrdenCompra.set(i.getFechaOrdenCompra());
		observacion.set(i.getObservacionOrdenCompra());
		
		nombreUsuario.set(i.getNombreUsuario() + " " + i.getApellidoUsuario());
		nombreProveedor.set(i.getNombreProveedor());
		
		return this;
	}
	
	
	@Override
	public OrdenDeCompra to(OrdenDeCompra i) {
		i.setPkIdOrdenCompra(pkIdOrdenCompra.get());
//		i.setUsuario(idUsuario.get());
//		i.setProveedor(idProveedor.get());
		i.setNroOrdenCompra(nroOrdenCompra.get());
		i.setFechaOrdenCompra(fechaOrdenCompra.get());
		i.setObservacionOrdenCompra(observacion.get());
		
		i.setNombreUsuario(nombreUsuario.get());
		i.setNombreProveedor(nombreProveedor.get());
		return i;
	}

}
