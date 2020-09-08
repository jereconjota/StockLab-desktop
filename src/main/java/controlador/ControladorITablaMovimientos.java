package controlador;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import hibernate.util.CRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import main.AppMain;
import modelo.Insumo;
import modelo.Movimiento;
import modelo.Sucursal;
import modeloAux.MovimientoFX;

public class ControladorITablaMovimientos {

	@FXML
    private AnchorPane anchorPaneMovimientos;

	@FXML
    private TableView<MovimientoFX> tablaMovimientos;
	
    @FXML
    private TableColumn<MovimientoFX, String> tablaMovimientos_nombre;
    
    @FXML
    private TableColumn<MovimientoFX, Date> tablaMovimientos_fecha;
    
    @FXML
    private TableColumn<MovimientoFX, String> tablaMovimientos_insumo;
    
    @FXML
    private TableColumn<MovimientoFX, String> tablaMovimientos_descripcion;
    
    @FXML
    private TableColumn<MovimientoFX, String> tablaMovimientos_nombreInsumo;
    
    @FXML
    private TableColumn<MovimientoFX, String> tablaMovimientos_sucursal;

    @FXML
    private TableColumn<MovimientoFX, String> tablaMovimientos_datos;

    public  ObservableList<MovimientoFX> obListMovimiento = FXCollections.observableArrayList();

    Insumo insumoBD;
	
	
	//////////////////////////////  CONSTRUCTOR  ////////////////////////////
    
	public ControladorITablaMovimientos() {
		
	}
	
	
	/////////////////////GET y SET////////////////////////////////////////
	
	public TableView<MovimientoFX> getTablaMovimientos() {
		return tablaMovimientos;
	}
	
	/////////////////////////// METODOS ///////////////////////////////
	
	
	@FXML
    public void initialize() {
    	definirTipoDatoColumnas();
    }
	
	
	private void definirTipoDatoColumnas() {
		tablaMovimientos_nombre.setCellValueFactory(cellData -> cellData.getValue().nombre);
		tablaMovimientos_fecha.setCellValueFactory(cellData -> cellData.getValue().fechaMovimiento);
		tablaMovimientos_insumo.setCellValueFactory(cellData -> cellData.getValue().insumo);
		tablaMovimientos_descripcion.setCellValueFactory(cellData -> cellData.getValue().descripcion);
		tablaMovimientos_sucursal.setCellValueFactory(cellData -> cellData.getValue().sucursal);
		tablaMovimientos_nombreInsumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		
		tablaMovimientos_datos.setCellValueFactory(cellData -> cellData.getValue().datos);
		alinearContenidoColumnas();
	}

	
	private void alinearContenidoColumnas() {
		tablaMovimientos_nombre.setStyle("-fx-alignment: CENTER;");
		tablaMovimientos_fecha.setStyle("-fx-alignment: CENTER;");
		tablaMovimientos_insumo.setStyle("-fx-alignment: CENTER;");
		tablaMovimientos_descripcion.setStyle("-fx-alignment: CENTER-LEFT;");
		tablaMovimientos_nombreInsumo.setStyle("-fx-alignment: CENTER;");
		tablaMovimientos_sucursal.setStyle("-fx-alignment: CENTER;");
		tablaMovimientos_datos.setStyle("-fx-alignment: CENTER;");
	}
	
	
	public void botonBuscarMovimientos(Date inicio, Date fin) {
		try {
			obListMovimiento.clear();  
			
			List<Movimiento> listaMovimientos = CRUD.obtenerListaMovimientosPorFecha(inicio, fin);
			
			//Si la lista no esta vacia, llena la tabla con su contenido
			if(!listaMovimientos.isEmpty()){
				for (Movimiento movimiento : listaMovimientos) {
					final MovimientoFX movimientoFX = new MovimientoFX(movimiento);
					
//					//Divido la descripcion en 2, asi meto lo de la sucursal en una columna y lo demas en la otra
//					int punto = movimiento.getDescripcion().indexOf("{");
	
					int inicioDescr = movimiento.getDescripcion().indexOf("OBSERVAC");
					
					String desc = movimiento.getDescripcion().substring(0, inicioDescr);
					desc = desc.replace(" Tipo de ", "\nTipo de ");
					desc = desc.replace(" Stock ", "\nStock ");
					
					movimientoFX.datos.set(desc);
					String descripcion = movimiento.getDescripcion();
					descripcion = descripcion.replaceAll("OBSERVACION:", "");
	
					movimientoFX.descripcion.set(descripcion);
//					movimientoFX.sucursal.set(movimiento.getSucursal());
					obListMovimiento.add(movimientoFX);
				}
			}
			//Se muestra la tabla ordenada por fecha, el ultimo movimiento hecho se ve primero
			tablaMovimientos_fecha.setSortType(TableColumn.SortType.DESCENDING);
			tablaMovimientos.setItems(obListMovimiento);
			tablaMovimientos.getSortOrder().add(tablaMovimientos_fecha);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	public void guardarMovimiento(Insumo insumoIN, String sucursal, String observacion) {
		try{
			//Obtenemos fecha y hr pasa asignarla al movimiento de actualizacion de stock
			Date date = new Date();
			Timestamp time = new Timestamp(date.getTime());
			
			String datos = "Unidades decrementadas: " + 
					ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_cantidadUsada().getText() + "\nTipo de unidad: " +
					"\nStock restante: "+ (ControladorICsd_Principal.controllerActualizarStockInsumo.getStockGeneral() - Integer.valueOf(ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_cantidadUsada().getText()));
			
			String descripcionMovimiento = datos +" OBSERVACION: "+ observacion;
			
	
	
			Movimiento mov = new Movimiento(insumoIN, (ControladorICsd_Principal.getUser()),
					(ControladorICsd_Principal.getUser().getNombre()), (ControladorICsd_Principal.getUser().getApellido()), 
					time, descripcionMovimiento, sucursal );
			
			MovimientoFX movimFX = new MovimientoFX(mov);
			movimFX.descripcion.set(descripcionMovimiento);
			movimFX.sucursal.set(sucursal);
			
			//Traigo el insumo de la BD al que voy a actualizar su stock, asi obtengo el nombre que es lo que tambien 
			//voy a mostrar en la tablaMovimientos
			insumoBD = CRUD.obtenerInsumoPorId1(mov.getInsumo().getIdInsumo());
			movimFX.nombreInsumo.set(insumoBD.getNombreInsumo());
			obListMovimiento.add(movimFX);
			CRUD.guardarObjeto(mov);
			
		}catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		
	}
	
	
	public void guardarMovimientoEnviar(Insumo insumoIN, String sucursal, String observacion) {
		try{
			//Obtenemos fecha y hr pasa asignarla al movimiento de actualizacion de stock
			Date date = new Date();
			Timestamp time = new Timestamp(date.getTime());
			
			String datos = "Unidades enviadas: " + 
					ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_cantidadUsada().getText() + "\nTipo de unidad: " + insumoIN.getUnidadMedida() +
					"\nStock restante en central: "+ (ControladorICsd_Principal.controllerActualizarStockInsumo.getStockGeneral() - Integer.valueOf(ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_cantidadUsada().getText()));
			
			String descripcionMovimiento = datos +" OBSERVACION: "+ observacion;
			
	
			Movimiento mov = new Movimiento(insumoIN, (ControladorICsd_Principal.getUser()),
					(ControladorICsd_Principal.getUser().getNombre()), (ControladorICsd_Principal.getUser().getApellido()), 
					time, descripcionMovimiento,sucursal );
			
			MovimientoFX movimFX = new MovimientoFX(mov);
			movimFX.descripcion.set(descripcionMovimiento);
			movimFX.sucursal.set(sucursal);
			
			//Traigo el insumo de la BD al que voy a actualizar su stock, asi obtengo el nombre que es lo que tambien 
			//voy a mostrar en la tablaMovimientos
			insumoBD = CRUD.obtenerInsumoPorId1(mov.getInsumo().getIdInsumo());
			movimFX.nombreInsumo.set(insumoBD.getNombreInsumo());
			obListMovimiento.add(movimFX);
			CRUD.guardarObjeto(mov);
			
		}catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		
	}
	
	
	public void guardarMovimientoDesdeRemito(Insumo insumoIN, Integer cantidad, String nombreSucursalIN, String observacion) {
		try{
		//Obtenemos fecha y hr pasa asignarla al movimiento de actualizacion de stock
		Date date = new Date();
		Timestamp time = new Timestamp(date.getTime());
		
//		Insumo insumo = CRUD.obtenerInsumoStockGralPorIdCategoria(insumoIN.getCategoria().getPkIdCategoria(), insumoIN.getNombreInsumo());
		
		Sucursal sucursal = CRUD.obtenerSucursalPorNombre(nombreSucursalIN);
		Insumo insumo = CRUD.obtenerInsumoStockGralPorIdCategoriaYSucursal(insumoIN.getCategoria().getPkIdCategoria(), insumoIN.getNombreInsumo(), sucursal.getPkIdSucursal());
		Integer stockGeneral = (int) (long) insumo.getStockGeneral();
		
		String datos =  "Unidades ingresadas: " + cantidad + 
				"\nTipo de unidad: " + insumoIN.getUnidadMedida() +
				"\nStock total: "+ stockGeneral;
		
		String descripcionMovimiento = datos + "\n OBSERVACION: "+ observacion + " {"+ nombreSucursalIN;
		
		
		//Divido la descripcion en 2, asi meto lo de la sucursal en una columna y lo demas en la otra
		int punto = descripcionMovimiento.indexOf(" {");
		
		Movimiento mov = new Movimiento(insumoIN, (ControladorICsd_Principal.getUser()),
				(ControladorICsd_Principal.getUser().getNombre()), (ControladorICsd_Principal.getUser().getApellido()), 
				time, descripcionMovimiento,sucursal.getNombreSucursal() );
		
		MovimientoFX movimFX = new MovimientoFX(mov);
		movimFX.descripcion.set(descripcionMovimiento.substring(0, punto));
		movimFX.sucursal.set(descripcionMovimiento.substring(punto+1, descripcionMovimiento.length()));

		movimFX.nombreInsumo.set(insumoIN.getNombreInsumo());
		obListMovimiento.add(movimFX);
		
		CRUD.guardarObjeto(mov);
		
		}catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		
	}
	
	
}
