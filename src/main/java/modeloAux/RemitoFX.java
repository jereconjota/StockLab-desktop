package modeloAux;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import modelo.Factura;
import modelo.Remito;

public class RemitoFX extends Fx<Remito> {
	
	public final SimpleStringProperty nroRemito = new SimpleStringProperty();
	public final SimpleStringProperty factura = new SimpleStringProperty();			//NRO FACTURA
	public final SimpleObjectProperty<Date> fechaRemito = new SimpleObjectProperty<>();
	public final SimpleObjectProperty<Date> fechaCarga = new SimpleObjectProperty<>();
	public final SimpleStringProperty observacion = new SimpleStringProperty();
	
	//Estos 2 no son atributos q formen, parte de tabla remito
	public final ObjectProperty<Integer> idProveedor = new SimpleObjectProperty<>();
    public final SimpleStringProperty nombreProveedor = new SimpleStringProperty();
    public final SimpleStringProperty nroCuit = new SimpleStringProperty();
	
    public Integer idRemito;
    
	/**************************** CONSTRUCTOR *********************************/
	
	public RemitoFX() {
		super();
	}
	
	
	public RemitoFX(Remito r) {
		from(r);
	}
	
	
	/********************************** METODOS ***********************************/
	
	
	@Override
	public RemitoFX from(Remito r) {
		idRemito = r.getIdRemito();
		nroRemito.set(r.getNroRemito());
		
		//esto es xq cuando se da alta remito, a veces no tendra nro factura
		//x lo q el objeto factura dentro de la clase remito estaria en null
		//lo q afectaria a la hs de mostrar los datos en la tabla de fx
		//x lo tanto le asigno al remito (SOLO) para mostrar un objeto factura vacio
		if (r.getFactura() == null) {        
			r.setFactura(new Factura());
		}
		
//		if (r.getFactura().getIdFactura() != null) {
//			final Factura fact = ControladorILogin.opCRUD.loadFactura(r.getFactura().getIdFactura());
//			factura.set(fact.getNroFactura());
//		}
		factura.set(r.getNroFactura());
		
		fechaRemito.set(r.getFechaRemito());
		fechaCarga.set(r.getFechaCarga());
		
		idProveedor.set(r.getIdProveedor());
		nombreProveedor.set(r.getNombreProveedor());
		nroCuit.set(r.getNroCuit());
		
		observacion.set(r.getObservacionRemito());
		return this;
	}


	@Override
	public Remito to(Remito r) {
		r.setNroRemito(nroRemito.get());
//		r.setFactura(ControladorILogin.opCRUD.loadFactura(factura.get()));
		r.setFechaRemito(fechaRemito.get());
		r.setFechaCarga(fechaCarga.get());
		r.setObservacionRemito(observacion.get());
		return r;
	}

}
