package modeloAux;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import modelo.DetalleRemito;

public class DetalleRemitoFX extends Fx<DetalleRemito> {
	
	public final StringProperty insumo = new SimpleStringProperty(); //NRO LOTE
	
	public final StringProperty proveedor = new SimpleStringProperty(); //CUIT
	
	public final StringProperty remito = new SimpleStringProperty(); //NRO REMITO
	
	public final ObjectProperty<Integer> cantidad = new SimpleObjectProperty<>();
	
	//estos no forman parte de los atributos de la clase "DetalleFactura", 
	//pero es necesario para mostrar datos en la tabla de la app
	public final StringProperty nombreInsumo = new SimpleStringProperty();
	public final StringProperty nroArticulo = new SimpleStringProperty();
	public final StringProperty unidadMedida = new SimpleStringProperty();
	
	
	public final StringProperty nombreCategoria = new SimpleStringProperty();
	public Integer idCategoria;
	
	public final StringProperty nombreSector = new SimpleStringProperty();
	
	public final ObjectProperty<Date> fechaVencimiento = new SimpleObjectProperty<>();
	
	public String nroReferencia;
	
	
	/**************************** CONSTRUCTOR *********************************/
	
	public DetalleRemitoFX() {
		super();
	}
	
	public DetalleRemitoFX(DetalleRemito dR) {
		from(dR);
	}
	
	
	/********************************** METODOS ***********************************/
	
	@Override
	public DetalleRemitoFX from(DetalleRemito dR) {
		try {
//			final Insumo ins = ControladorILogin.opCRUD.loadInsumo(dR.getInsumo().getIdInsumo());
//			insumo.set(ins.getNroLote());
			insumo.set(dR.getNroLote());

//			if (dR.getProveedor() != null) {
//				final Proveedor prov = ControladorILogin.opCRUD.loadProveedor(dR.getProveedor().getPkIdProveedor());		
//				proveedor.set(prov.getNroCuit());
//			}
			proveedor.set(dR.getNroCuit());
			
//			if (dR.getRemito().getIdRemito() != null) {
//				final Remito rem = ControladorILogin.opCRUD.loadRemito(dR.getRemito().getIdRemito());
//				remito.set(rem.getNroRemito());
//			}
			remito.set(dR.getNroRemito());
			
			cantidad.set(dR.getCantidad());

//			if (ins != null) {
//				nombreInsumo.set(ins.getNombreInsumo());
//				nroArticulo.set(ins.getNroArticulo());
//				unidadMedida.set(ins.getUnidadMedida());
//			}
			nombreInsumo.set(dR.getNombreInsumo());
			nroArticulo.set(dR.getNroArticulo());
			unidadMedida.set(dR.getUnidadMedida());
			
			if (dR.getFechaVtoInsumo() != null) {
				SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
				String fecha = formato.format(dR.getFechaVtoInsumo());
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
	public DetalleRemito to(DetalleRemito dR) {
		dR.setCantidad(cantidad.get());
		return dR;
	}

}
