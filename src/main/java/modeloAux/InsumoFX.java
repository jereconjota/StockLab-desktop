package modeloAux;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import modelo.Insumo;

public class InsumoFX extends Fx<Insumo> {
	
	public final StringProperty pkNroLote = new SimpleStringProperty();
	public final StringProperty nombreCategoria = new SimpleStringProperty();
	public final StringProperty nombreInsumo = new SimpleStringProperty();
	public final StringProperty referencia = new SimpleStringProperty();
	public final StringProperty temperatura = new SimpleStringProperty();
	public final StringProperty unidadMedida = new SimpleStringProperty();
	public final StringProperty estadoInsumo = new SimpleStringProperty();

	public final ObjectProperty<Integer> idInsumo = new SimpleObjectProperty<>();
	public final ObjectProperty<Date> fechaVencimiento = new SimpleObjectProperty<>();
	public final ObjectProperty<Float> precioInsumo = new SimpleObjectProperty<>();
	public final ObjectProperty<Date> fechaUso = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> pdp = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> stockActual = new SimpleObjectProperty<>();
	public final ObjectProperty<Date> fechaIngreso = new SimpleObjectProperty<>();
	public final StringProperty articulo = new SimpleStringProperty();
	public final ObjectProperty<Date> fechaBaja = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> nroPedido = new SimpleObjectProperty<>();
	
	public final ObjectProperty<Integer> stockReal = new SimpleObjectProperty<>();
	public final ObjectProperty<Integer> stockGeneral = new SimpleObjectProperty<>();
	
	public Integer idCategoria;
	
	public final StringProperty ubicacion = new SimpleStringProperty("UBICACION");
	public final StringProperty nombreSector = new SimpleStringProperty();
	
	public final StringProperty proveedor = new SimpleStringProperty();			//Nombre proveedor
	
//	public String proveedor;
	
	public Integer idProveedor;
	
	public final StringProperty nombreSucursal = new SimpleStringProperty();
	
	/**************************** CONSTRUCTOR *********************************/
	
	public InsumoFX() {
		super();
	}
	
	
	public InsumoFX(Insumo i) {
		from(i);
	}
	
	/********************************** METODOS ***********************************/
	
	@Override
	public InsumoFX from(Insumo i) {
		pkNroLote.set(i.getNroLote());
		
		if (i.getNombreCategoria() == null) {
			nombreCategoria.set(i.getCategoria().getNombreCategoria());
		} else {											//x este sino viene en ciertas situaciones (x ej, desde ControladorITablaAbmInsumos / cargarInsumos2)
			nombreCategoria.set(i.getNombreCategoria());   //xq obtengo su nombre desde una consulta CRUD
		}
		
		idCategoria = i.getCategoria().getPkIdCategoria();
		
		idInsumo.set(i.getIdInsumo());
		nombreInsumo.set(i.getNombreInsumo());
		referencia.set(i.getReferencia());
		fechaVencimiento.set(i.getFechaVencimiento());
		temperatura.set(i.getTemperatura());
		precioInsumo.set(i.getPrecioInsumo());
		fechaUso.set(i.getFechaUso());
		unidadMedida.set(i.getUnidadMedida());
		estadoInsumo.set(i.getEstadoInsumo());
		pdp.set(i.getPdp());
		stockActual.set(i.getStockActual());
		stockReal.set(i.getStockReal()); 
		fechaIngreso.set(i.getFechaIngreso());
		articulo.set(i.getNroArticulo());
		fechaBaja.set(i.getFechaBaja());
		ubicacion.set("UBICACION");
		
		if (i.getProveedor() != null) {
			//con esto da null xq, i.getProveedor() solo tiene el id del provee, no tiene
			//acceso a todos sus atributos, x eso le agregamos aparte al objeto insumo
			//el atributo nombreProvee, para evitar este error
//			proveedor = i.getProveedor().getNombreProveedor();
			
//			proveedor = i.getNombreProveedor();
			proveedor.set(i.getNombreProveedor());
		}
		nroPedido.set(i.getNroPedido());
		
		return this;
	}
	
	
	@Override
	public Insumo to(Insumo i) {
		i.setNroLote(pkNroLote.get());
		i.setIdInsumo(idInsumo.get());
		i.setNombreInsumo(nombreInsumo.get());
		i.setReferencia(referencia.get());
		i.setFechaVencimiento(fechaVencimiento.get());
		i.setTemperatura(temperatura.get());
		i.setPrecioInsumo(precioInsumo.get());
		i.setFechaUso(fechaUso.get());
		i.setUnidadMedida(unidadMedida.get());
		i.setEstadoInsumo(estadoInsumo.get());
		i.setPdp(pdp.get());
		i.setStockActual(stockActual.get());
		i.setStockReal(stockReal.get());
		i.setFechaIngreso(fechaIngreso.get());	
		i.setNroArticulo(articulo.get());
		i.setFechaBaja(fechaBaja.get());
		i.setNroPedido(nroPedido.get());
		
		return i;
	}

}
