package modeloAux;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import modelo.Proveedor;

public class ProveedorFX extends Fx<Proveedor>{

	public final ObjectProperty<Integer> idProveedor = new SimpleObjectProperty<Integer>();
	public final StringProperty nroCuit = new SimpleStringProperty();
	public final StringProperty nombreProveedor = new SimpleStringProperty();
	public final StringProperty nroProveedor = new SimpleStringProperty();
	public final StringProperty direccionProveedor = new SimpleStringProperty();
	public final StringProperty estadoProveedor = new SimpleStringProperty();
	public final ObjectProperty<Date> fechaAlta = new SimpleObjectProperty<Date>();
	public final ObjectProperty<Date> fechaBaja = new SimpleObjectProperty<Date>();
	
	public final ObjectProperty<Integer> nroOrdenDeCompra = new SimpleObjectProperty<Integer>();
	
	/**************************** CONSTRUCTOR *********************************/
	
	public ProveedorFX() {
		// TODO Auto-generated constructor stub
	}
	
	
	public ProveedorFX(Proveedor p) {
		from(p);
	}
	
	
	/********************************** METODOS ***********************************/
	
	@Override
	public ProveedorFX from(Proveedor p) {
		idProveedor.set(p.getPkIdProveedor());
		nroCuit.set(p.getNroCuit());
		nombreProveedor.set(p.getNombreProveedor());
		nroProveedor.set(p.getNroProveedor());
		direccionProveedor.set(p.getDireccionProveedor());
		estadoProveedor.set(p.getEstadoProveedor());
		fechaAlta.set(p.getFechaAlta());
		fechaBaja.set(p.getFechaBaja());
		
		nroOrdenDeCompra.set(p.getNroDeOrdenDeCompra());
		return this;
	}
	
	
	
	@Override
	public Proveedor to(Proveedor p) {
		p.setPkIdProveedor(idProveedor.get());
		p.setNroCuit(nroCuit.get());
		p.setNombreProveedor(nombreProveedor.get());
		p.setNroProveedor(nroProveedor.get());
		p.setDireccionProveedor(direccionProveedor.get());
		p.setEstadoProveedor(estadoProveedor.get());
		p.setFechaAlta(fechaAlta.get());
		p.setFechaBaja(fechaBaja.get());
		return p;
	}

}
