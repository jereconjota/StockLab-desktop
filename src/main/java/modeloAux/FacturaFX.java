package modeloAux;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import modelo.Factura;

public class FacturaFX extends Fx<Factura> {
	
	public final SimpleStringProperty nroFactura = new SimpleStringProperty();
	public final SimpleObjectProperty<Date> fechaFactura = new SimpleObjectProperty<>();
	public final SimpleStringProperty tipoFactura = new SimpleStringProperty();
	public final SimpleObjectProperty<Date> fechaCarga = new SimpleObjectProperty<>();
	public final SimpleStringProperty observacion = new SimpleStringProperty();
	
	//Estos 3 no son atributos q formen, parte de tabla factura
	public final ObjectProperty<Integer> idProveedor = new SimpleObjectProperty<>();
    public final SimpleStringProperty nombreProveedor = new SimpleStringProperty();
    public final SimpleStringProperty nroCuit = new SimpleStringProperty();
    public final SimpleObjectProperty<Float> subtotal = new SimpleObjectProperty<>();
    public final SimpleStringProperty tieneRemito = new SimpleStringProperty();
	
    public Integer IdFactura;
    
    
	/**************************** CONSTRUCTOR *********************************/
	
	public FacturaFX() {
		super();
	}
	
	
	public FacturaFX(Factura f) {
		from(f);
	}
	
	
	/********************************** METODOS ***********************************/

	
	@Override
	public FacturaFX from(Factura f) {
		IdFactura = f.getIdFactura();
		nroFactura.set(f.getNroFactura());
		fechaFactura.set(f.getFechaFactura());
		tipoFactura.set(f.getTipoFactura());
		fechaCarga.set(f.getFechaCarga());
		observacion.set(f.getObservacionFactura());
		idProveedor.set(f.getIdProveedor());
		nombreProveedor.set(f.getNombreProveedor());
		nroCuit.set(f.getNroCuitProveedor());
		subtotal.set(f.getSubTotal().floatValue());	//tengo q convertir el double q me regresa el query en float
		tieneRemito.set(f.getTieneRemito());
		return this;
	}


	@Override
	public Factura to(Factura f) {
		f.setNroFactura(nroFactura.get());
		f.setFechaFactura(fechaFactura.get());
		f.setTipoFactura(tipoFactura.get());
		f.setFechaCarga(fechaCarga.get());
		f.setObservacionFactura(observacion.get());
		return f;
	}
	

}
