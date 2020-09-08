package modeloAux;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import modelo.DetalleFactura;

public class DetalleFacturaFX extends Fx<DetalleFactura> {
	
	public final StringProperty factura = new SimpleStringProperty(); //NRO FACTURA

	public final StringProperty insumo = new SimpleStringProperty(); //NRO LOTE
	
	public final StringProperty proveedor = new SimpleStringProperty(); //CUIT
	
	public final ObjectProperty<Integer> cantidad = new SimpleObjectProperty<>();
	public final ObjectProperty<Float> precio = new SimpleObjectProperty<>();
	
	//estos no forman parte de los atributos de la clase "DetalleFactura", 
	//pero es necesario para mostrar datos en la tabla de la app
	public final StringProperty nombreInsumo = new SimpleStringProperty();
	public final ObjectProperty<Float> importe = new SimpleObjectProperty<>();
	public final StringProperty nroArticulo = new SimpleStringProperty();
	public final StringProperty unidadMedida = new SimpleStringProperty();
	
	public final ObjectProperty<Date> fechaVencimiento = new SimpleObjectProperty<>();
	
	public final StringProperty nroRemito = new SimpleStringProperty();
	
	public final StringProperty nombreCategoria = new SimpleStringProperty();
	public Integer idCategoria;
	
	public final StringProperty nombreSector = new SimpleStringProperty();
	
	public Integer numFila;
	
	public Integer idInsumo;
	
	public Integer idRemito;
	
	public Integer idProveedor;
	
	public String nroReferencia;
	
	
	/**************************** CONSTRUCTOR *********************************/
	
	public DetalleFacturaFX() {
		super();
	}
	
	public DetalleFacturaFX(DetalleFactura dF) {
		from(dF);
	}
	
	
	/********************************** METODOS ***********************************/
	
	@Override
	public DetalleFacturaFX from(DetalleFactura dF) {
		try {
			factura.set(dF.getNroFactura());
			insumo.set(dF.getNroLote());
			proveedor.set(dF.getNroCuit());
			cantidad.set(dF.getCantidad());
			precio.set(dF.getPrecio());
			nombreInsumo.set(dF.getNombreInsumo());
			importe.set(dF.getImporte());
			nroArticulo.set(dF.getNroArticulo());
			unidadMedida.set(dF.getUnidadMedida());

			if (dF.getFechaVtoInsumo() != null) {
				SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
				String fecha = formato.format(dF.getFechaVtoInsumo());
				Date fechaVto = formato.parse(fecha);
				fechaVencimiento.set(fechaVto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}		
		return this;
	}
	
	
	@Override
	public DetalleFactura to(DetalleFactura dF) {
		dF.setCantidad(cantidad.get());
		dF.setPrecio(precio.get());
		dF.setImporte(importe.get());
		return dF;
	}

}
