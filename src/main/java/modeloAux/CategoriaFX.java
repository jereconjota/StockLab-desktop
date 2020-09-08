package modeloAux;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import modelo.Categoria;

public class CategoriaFX extends Fx<Categoria> {
	
	public final StringProperty nombreCategoria = new SimpleStringProperty();
	public final StringProperty nombreSector = new SimpleStringProperty();
	public final ObjectProperty<Integer> idCategoria = new SimpleObjectProperty<>();
	public final ObjectProperty<Date> fechaAlta = new SimpleObjectProperty<>();
	public final ObjectProperty<Date> fechaBaja = new SimpleObjectProperty<>();
	public final StringProperty estadoCategoria = new SimpleStringProperty();
	
	
	/**************************** CONSTRUCTOR *********************************/
	
	public CategoriaFX() {
		super();
	}
	
	
	public CategoriaFX(Categoria c) {
		from(c);
	}
	
	/********************************** METODOS ***********************************/
	
	@Override
	public CategoriaFX from(Categoria c) {
		nombreCategoria.set(c.getNombreCategoria());
		
//		final Sector sector = ControladorILogin.opCRUD.loadSectorPorID(c.getSector().getIdSector());
//		nombreSector.set(sector.getNombreSector());

		nombreSector.set(c.getSector().getNombreSector());
		
		idCategoria.set(c.getPkIdCategoria());
		fechaAlta.set(c.getFechaAlta());
		fechaBaja.set(c.getFechaBaja());
		estadoCategoria.set(c.getEstadoCategoria());
		return this;
	}
	

	@Override
	public Categoria to(Categoria c) {
		c.setNombreCategoria(nombreCategoria.get());
		c.setPkIdCategoria(idCategoria.get());
		c.setFechaAlta(fechaAlta.get());
		c.setFechaBaja(fechaBaja.get());
		c.setEstadoCategoria(estadoCategoria.get());
		return c;
	}

}
