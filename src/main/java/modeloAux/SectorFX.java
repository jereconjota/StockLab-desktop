package modeloAux;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import modelo.Sector;

public class SectorFX extends Fx<Sector> {
	
	public final IntegerProperty idSector = new SimpleIntegerProperty();
	public final StringProperty nombreSector = new SimpleStringProperty();
	public final ObjectProperty<Date> fechaAlta = new SimpleObjectProperty<>();
	public final ObjectProperty<Date> fechaBaja = new SimpleObjectProperty<>();
	public final StringProperty estadoSector = new SimpleStringProperty();
	

	/**************************** CONSTRUCTOR *********************************/
	
	public SectorFX() {
		super();
	}
	
	
	public SectorFX(Sector s) {
		from(s);
	}
	
	
	/********************************** METODOS ***********************************/
	
	
	@Override
	public SectorFX from(Sector s) {
		idSector.set(s.getIdSector());
		nombreSector.set(s.getNombreSector());
		fechaAlta.set(s.getFechaAlta());
		fechaBaja.set(s.getFechaBaja());
		estadoSector.set(s.getEstadoSector());
		return this;
	}
	
	
	@Override
	public Sector to(Sector s) {
		s.setIdSector(idSector.get());
		s.setNombreSector(nombreSector.get());
		s.setFechaAlta(fechaAlta.get());
		s.setFechaBaja(fechaBaja.get());
		s.setEstadoSector(estadoSector.get());
		return s;
	}
	
}
