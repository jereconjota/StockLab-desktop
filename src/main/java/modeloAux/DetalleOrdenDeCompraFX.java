package modeloAux;


import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import modelo.DetalleOrdenDeCompra;

public class DetalleOrdenDeCompraFX extends Fx<DetalleOrdenDeCompra>{

	


//	public final ObjectProperty<Integer> pkNumDetalle = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> nroOrdenCompra = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> cantidad = new SimpleObjectProperty<>();
	public final ObjectProperty<Date> fechaOrdenCompra = new SimpleObjectProperty<>();

	public final StringProperty nombreInsumo = new SimpleStringProperty();
	public final StringProperty articulo = new SimpleStringProperty();
	public final StringProperty referencia = new SimpleStringProperty();
	public final StringProperty nombreCategoria = new SimpleStringProperty();
	public final StringProperty nombreSector = new SimpleStringProperty();
//	public final StringProperty unidadDeMedida = new SimpleStringProperty();
	
	public final ObjectProperty<Integer> idSector = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> idCategoria = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> idProveedor = new SimpleObjectProperty<>();
	
	public final ObjectProperty<Integer> unidades = new SimpleObjectProperty<>();
	
	public final StringProperty observacion = new SimpleStringProperty();
	
	public Integer numFila;

	/**************************** CONSTRUCTOR *********************************/
	
	public DetalleOrdenDeCompraFX() {
		super();
	}
	
	public DetalleOrdenDeCompraFX(DetalleOrdenDeCompra i) {
		from(i);
	}
	
	/********************************** METODOS ***********************************/
	
	@Override
	public DetalleOrdenDeCompraFX from(DetalleOrdenDeCompra i) {
		
//		pkNumDetalle.set(i.getPkNumDetalle());
		
		nroOrdenCompra.set(i.getNroOrdenDeCompra());
		cantidad.set(i.getCantidad());
		fechaOrdenCompra.set(i.getFechaOrden());
		
		nombreInsumo.set(i.getNombreInsumo());
		articulo.set(i.getArticulo());
		referencia.set(i.getReferencia());
		
		if (i.getNombreCategoria() == null) {
			nombreCategoria.set(i.getNombreCategoria());
		} else {											//x este sino viene en ciertas situaciones (x ej, desde ControladorITablaAbmInsumos / cargarInsumos2)
			nombreCategoria.set(i.getNombreCategoria());   //xq obtengo su nombre desde una consulta CRUD
		}
		
		nombreSector.set(i.getNombreSector());
		
		idSector.set(i.idSector);
		idCategoria.set(i.idCategoria);
		idProveedor.set(i.idProveedor);
		
//		unidadDeMedida.set(i.getUnidadDeMedida());
		unidades.set(i.getUnidades());
		
		observacion.set(i.getObservacion());
		
		return this;
	}
	
	
	@Override
	public DetalleOrdenDeCompra to(DetalleOrdenDeCompra i) {
		i.setArticulo(articulo.get());
		i.setCantidad(cantidad.get());
		i.setFechaOrden(fechaOrdenCompra.get());
		i.setNroOrdenDeCompra(nroOrdenCompra.get());
		i.setObservacion(observacion.get());
		i.setUnidades(unidades.get());
		
		return i;
	}


}
