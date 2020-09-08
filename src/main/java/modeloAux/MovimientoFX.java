package modeloAux;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import modelo.Movimiento;

public class MovimientoFX extends Fx<Movimiento>{
	
	public final StringProperty nombre = new SimpleStringProperty();
	public final StringProperty descripcion = new SimpleStringProperty();
	public final StringProperty nombreInsumo = new SimpleStringProperty();
	public final StringProperty sucursal = new SimpleStringProperty();

	public final StringProperty insumo = new SimpleStringProperty();
	public final ObjectProperty<Integer> idMov = new SimpleObjectProperty<>();
	public final ObjectProperty<Date> fechaMovimiento = new SimpleObjectProperty<>();
	
	public final StringProperty datos = new SimpleStringProperty();

	/**************************** CONSTRUCTOR *********************************/	
	
	public MovimientoFX() {
		super();
	}
	
	
	public MovimientoFX(Movimiento i) {
		from(i);
	}
	
	
	/********************************** METODOS ***********************************/
	
	
	@Override
	public MovimientoFX from(Movimiento m) {
//		insumo.set(m.getInsumo().getNroLote());
		insumo.set(m.getNroLote());
//		nombreInsumo.set(m.getInsumo().getNombreInsumo());
		nombreInsumo.set(m.getNombreInsumo());
		nombre.set(m.getNombreUsuario() + " " + m.getApellidoUsuario());
		fechaMovimiento.set(m.getFechaMovimiento());
		descripcion.set(m.getDescripcion());
		idMov.set(m.getIdMov());
		sucursal.set(m.getSucursal());
		return this;
	}
	
	
	@Override
	public Movimiento to(Movimiento m) {
		m.setDescripcion(descripcion.get());
		m.setFechaMovimiento(fechaMovimiento.get());
		m.setNombreUsuario(nombre.get());
		m.setIdMov(idMov.get());
		return m;
	}
	
	@Override
	public String toString (){
	        String mensaje=nombre.get()+ "/"+insumo.get()+ "/"+descripcion.get()+ "/"+fechaMovimiento+"\n";
	        return mensaje;
	    }
}
