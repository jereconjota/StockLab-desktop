package controlador;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import hibernate.util.CRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.converter.DateStringConverter;
import main.AppMain;
import modelo.Categoria;
import modelo.Insumo;
import modeloAux.InsumoFX;

public class ControladorITablaDetalleInsumo {
	
	@FXML
    private AnchorPane anchorpaneinsumos;
	
	@FXML
    private TableView<InsumoFX> tablaInsumos;
	
	@FXML
    private TableColumn<InsumoFX, String> tablaInsumos_nombre;
	
	@FXML
    private TableColumn<InsumoFX, String> tablaInsumos_nroArticulo;
	
	@FXML
    private TableColumn<InsumoFX, String> tablaInsumos_referencia;
	
	@FXML
    private TableColumn<InsumoFX, String> tablaInsumos_nrolote;
	
	@FXML
    private TableColumn<InsumoFX, Integer> tablaInsumos_stockActual;
	
	@FXML
    private TableColumn<InsumoFX, Integer> tablaInsumos_stockGeneral;

    @FXML
    private TableColumn<InsumoFX, Integer> tablaInsumos_pdp;

    @FXML
    private TableColumn<InsumoFX, String> tablaInsumos_unidadDeMedida;

    @FXML
    private TableColumn<InsumoFX, Date> tablaInsumos_fechaDeUso;

    @FXML
    private TableColumn<InsumoFX, Date> tablaInsumos_fechaDeVencimiento;
    
    
    public  ObservableList<InsumoFX> obListInsumo = FXCollections.observableArrayList();
    
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorITablaDetalleInsumo() {
    	
	}
    
    
    /**************************** GET - SET **********************************/
    
    public TableView<InsumoFX> getTablaInsumos() {
		return tablaInsumos;
	}
	public void setTablaInsumos(TableView<InsumoFX> tablaInsumos) {
		this.tablaInsumos = tablaInsumos;
	}
	
	
	public TableColumn<InsumoFX, String> getTablaInsumos_nombre() {
		return tablaInsumos_nombre;
	}
	
    
    public ObservableList<InsumoFX> getObListInsumo() {
		return obListInsumo;
	}

	/********************************** METODOS ***********************************/
	
	public void initialize(){
    	definirTipoDatoColumnas();
       	//Metodo para dar funcionalidad al clickear una fila de la tabla de insumos
    	seleccionarFila();
    }
	
	
	private void definirTipoDatoColumnas() {
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
		tablaInsumos_nombre.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaInsumos_nrolote.setCellValueFactory(cellData -> cellData.getValue().pkNroLote);
		tablaInsumos_stockActual.setCellValueFactory(cellData ->cellData.getValue().stockActual);
		tablaInsumos_stockGeneral.setCellValueFactory(cellData ->cellData.getValue().stockGeneral);
		tablaInsumos_pdp.setCellValueFactory(cellData -> cellData.getValue().pdp);
		tablaInsumos_unidadDeMedida.setCellValueFactory(cellData -> cellData.getValue().unidadMedida);
		tablaInsumos_referencia.setCellValueFactory(cellData -> cellData.getValue().referencia);
		tablaInsumos_fechaDeUso.setCellValueFactory(cellData -> cellData.getValue().fechaUso);
		tablaInsumos_fechaDeUso.setCellFactory(TextFieldTableCell.<InsumoFX, Date>forTableColumn(convertirDaS));
		tablaInsumos_fechaDeVencimiento.setCellValueFactory(cellData -> cellData.getValue().fechaVencimiento);
		tablaInsumos_fechaDeVencimiento.setCellFactory(TextFieldTableCell.<InsumoFX, Date>forTableColumn(convertirDaS));
		tablaInsumos_nroArticulo.setCellValueFactory(cellData -> cellData.getValue().articulo);
		
//		tablaInsumos_ubicacion.setCellValueFactory(cellData -> cellData.getValue().ubicacion);
		alinearContenidoColumnas();
	}

	
	private void alinearContenidoColumnas() {
		tablaInsumos_nombre.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_nrolote.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_stockActual.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_pdp.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_unidadDeMedida.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_referencia.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_fechaDeUso.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_fechaDeVencimiento.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_nroArticulo.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_stockGeneral.setStyle("-fx-alignment: CENTER;");
	}
	
    
  //OBTENEMOS LOS INSUMOS DE LA BASE DE DATOS Y LOS METEMOS EN LA TABLA
	
	/**
	 * ControladorICsd_Principal.getDirUrl().substring(13, 27) = localhost:3306
	 * 
	 * ControladorICsd_Principal.getDirUrl().substring(13, 32) = 192.168.10.241:3306 (ejemplo, pero retorna dirIP)
	 */
	
  	public void cargarInsumos(String categIN) {
  		boolean vencidos = false;
  		String categoriaConVencidos = "";
  		Integer idCategoriaBD = null;
  		
  		Transaction tx = null;
  		try {
  			//antes de buscar mas insumos, se limpia la tabla
  			obListInsumo.clear();
  			
  			//Buscamos el ID de la categoria seleccionada
  			for (Categoria categoria : ControladorICsd_Principal.controllerVistaInsumos.getListaCategoria()) {
  				if (categoria.getNombreCategoria().equals(categIN) && 
  						categoria.getSector().getIdSector().equals(ControladorICsd_Principal.controllerVistaInsumos.getSectorElegido().getIdSector())) {
  					idCategoriaBD = categoria.getPkIdCategoria();
  					break;
  				}
  			}
  			
  			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query1 = appMain.getSession().createQuery("from Categoria where PK_Id_Categoria= :id");
			query1.setLong("id", idCategoriaBD);
			Categoria categoriaElegida = (Categoria) query1.uniqueResult();
			
  			//TRAE LOS INSUMOS ACTIVOS SEGUN LA CATEGORIA SELECCIONADA
			
//			Query query2 = appMain.getSession().createQuery("from Insumo where FK_Id_Categoria= :idCategoria and Estado_Insumo = \'Activo\'");
			Query query2 = appMain.getSession().createQuery("from Insumo where FK_Id_Categoria= :idCategoria and Estado_Insumo = \'Activo\' and FK_Id_Sucursal= :idSucursal");
			query2.setLong("idCategoria", categoriaElegida.getPkIdCategoria());
			
			//ahora necesitamos saber la dirURL de la sucursal en la q se esta ejecutando la app
//			if (ControladorICsd_Principal.getDirUrl().substring(13, 32).equals("192.168.10.241:3306")) { // DIAGNOS central
//				query2.setLong("idSucursal", 1);
//			} else {
//				if (ControladorICsd_Principal.getDirUrl().substring(13, 32).equals("201.190.238.88:3306")) { // KM 3
//					query2.setLong("idSucursal", 2);
//				} else {
//					if (ControladorICsd_Principal.getDirUrl().substring(13, 33).equals("168.228.143.124:3306")) { // Rada Tilly
//						query2.setLong("idSucursal", 4);
//					} else {  // localhost
//						query2.setLong("idSucursal", 1); //seteamos x defecto en diagnos
//					}
//				}
//			}
			
			query2.setLong("idSucursal", 1); //seteamos x defecto en diagnos
			
			List<Insumo> insumoPorCategoria = query2.list();
			
  			for (Insumo insumo : insumoPorCategoria) {				
      			//Si no esta activo, no lo pone en la tabla
  				if (insumo.getStockActual() == 0) {
  					continue;
  				}
  				final InsumoFX insumoFX = new InsumoFX(insumo);
  				insumoFX.stockGeneral.set(insumo.getStockActual());
  				//Buscamos el mismo grupo de insumos para darle el valor de stock general
  				for (Insumo insumo2 : insumoPorCategoria) {
  					//Si tiene el mismo nombre y distinto nro de lote, suma
  						if (insumo.getNombreInsumo().equals(insumo2.getNombreInsumo()) &&
  								insumo.getNroLote() != insumo2.getNroLote() && 
  								insumo.getCategoria().getPkIdCategoria() == insumo2.getCategoria().getPkIdCategoria()) {
  								insumoFX.stockGeneral.set(insumoFX.stockGeneral.get() + insumo2.getStockActual());
  						}
  					}
  				
  				insumoFX.idInsumo.set(insumo.getIdInsumo());
  				obListInsumo.add(insumoFX);

  				if (insumo.getFechaVencimiento() == null) {
  					vencidos = true;
  					categoriaConVencidos = categIN;
  					categoriaConVencidos = categoriaConVencidos.toUpperCase();
  				}

  			}
  			
  			appMain.getSession().close();
  			
  			//Mostramos aviso si hay insumos sin fecha de vencimiento
  			if (vencidos) {
  				ControladorICsd_Principal.controllerVistaInsumos.getLabelVencidos().setVisible(true);
  				ControladorICsd_Principal.controllerVistaInsumos.getLabelVencidos().setText("La categoria "+
  				categoriaConVencidos +" presenta insumos sin fecha de vencimiento\nPor favor modifique en sus valores 'Gestion' -> 'Configuracion'");
  				this.getTablaInsumos().setItems(obListInsumo);
  			}else{
  				ControladorICsd_Principal.controllerVistaInsumos.getLabelVencidos().setVisible(false);
  				//Ordena la tabla por nombre
  				this.getTablaInsumos().setItems(obListInsumo);
  			}
  			
  			this.getTablaInsumos().getSortOrder().clear();
  			this.getTablaInsumos_nombre().setSortType(TableColumn.SortType.ASCENDING);
  			this.getTablaInsumos().getSortOrder().add(tablaInsumos_nombre);

  			ControladorICsd_Principal.controllerVistaInsumos.getBtnExportar().setDisable(false);
  			ControladorICsd_Principal.controllerVistaInsumos.getTextField_Buscar().setDisable(false);
  			ControladorICsd_Principal.controllerVistaInsumos.getTextField_Buscar().setText(null);
  			
  			// PINTAR FILAS
  			pintarFilas();
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		//rolling back to save the test data
			tx.rollback();
  			e.getMessage();		
  		}
  	}
  	

  	private void pintarFilas() {
		try {
		tablaInsumos.setRowFactory(tv -> new TableRow<InsumoFX>() {
		    @Override
		    public void updateItem(InsumoFX item, boolean empty) {
		        super.updateItem(item, empty);
		 
		        if (item == null) {
		            setStyle("");
		        } else if (item.stockGeneral.get() <= item.pdp.get()) {
		        	setStyle("-fx-background-color: #ffbac0;");
		        } else {
		            setStyle("");
		        }
		    }
		    
		});
		
		}catch (Exception e) {
			e.getMessage();
		}
	}

  	
  	private void seleccionarFila(){
		//se le asigna una accion al click de cada fila de la tabla insumos
		tablaInsumos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				InsumoFX insumofx = tablaInsumos.getSelectionModel().getSelectedItem();
				
				ControladorICsd_Principal.controllerVistaInsumos.getBorderPaneTablaInsumos().setCenter(ControladorICsd_Principal.iActualizarStockInsumo);
	            //mandamos los datos de la fila seleccionada a la vistaActualizarInsumo
//	            llenarCamposInsumo(insumofx);
	            llenarCamposInsumo(newSelection);
	            //Limpiamos el campo de cantidad
	    		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_cantidadUsada().clear();
	    		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextArea_observacion().clear();
	    		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_cantidadUsada().requestFocus();
	    		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_cantidadUsada().setUnFocusColor(Color.web("#4d4d4d"));
	    		//Desactivamos los campos de sector y categoria mientras actualiza stock
	    		ControladorICsd_Principal.controllerVistaInsumos.getComboboxSector().setDisable(true);
	    		ControladorICsd_Principal.controllerVistaInsumos.getListviewCategorias().setDisable(true);
	    		ControladorICsd_Principal.controllerVistaInsumos.getBtnActualizar().setDisable(true);
	    		ControladorICsd_Principal.controllerVistaInsumos.getTextField_Buscar().setDisable(true);
	    		//tambien como consistencia desactivamos los campos del menu principal
	    		ControladorILogin.controllerPpal.getMenuBarPrincipal().setDisable(true);
	    		
	    		if (ControladorICsd_Principal.controllerVistaInsumos.getLabelVencidos().isVisible()) {
	    			ControladorICsd_Principal.controllerVistaInsumos.getLabelVencidos().setVisible(false);
				}
	    		
	    		//ocultamos btn enviar insumo
	    		ControladorICsd_Principal.controllerActualizarStockInsumo.getBtnEnviar().setVisible(false);
	    		
			}else{
        		tablaInsumos.getSelectionModel().clearSelection();
			}
			
		});
	}
  	
  	
  //Llenamos los campos de la vistaActualizarInsumo//
  	private void llenarCamposInsumo(InsumoFX insumoFxIN){
  		try {
  			SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
  			Insumo insumoBD = CRUD.obtenerInsumoPorId1(insumoFxIN.idInsumo.get());
  			
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.setStockGeneral(insumoFxIN.stockGeneral.get());
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getLabel_NombreInsumo().setText(insumoFxIN.nombreInsumo.get());
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_nroLote().setText(insumoFxIN.pkNroLote.get());
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_nroLote().setEditable(false);
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextFied_referencia().setText(insumoFxIN.referencia.get());
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextFied_referencia().setEditable(false);
  	  		
  	  		if (!(insumoFxIN.fechaVencimiento.get() == null)) {
  	  			ControladorICsd_Principal.controllerActualizarStockInsumo.getTextFied_fechaVencimiento().setText(formato.format(insumoFxIN.fechaVencimiento.get()));
  	  		}
  	  		
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextFied_fechaVencimiento().setEditable(false);
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_pdp().setText(insumoFxIN.pdp.get().toString());
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_pdp().setEditable(false);
  	  		Date ahora = new Date();
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextFied_fechaDeUso().setText(formato.format(ahora));
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextFied_fechaDeUso().setEditable(false);
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_stockActual().setText(String.valueOf(insumoBD.getStockActual()));
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextField_stockActual().setEditable(false);
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getComboBox_sucursal().getSelectionModel().select(0);
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextFied_unidadMedida().setText(insumoFxIN.unidadMedida.get());
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getTextFied_unidadMedida().setEditable(false);;
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.getErrorLabel().setVisible(false);
  	  		ControladorICsd_Principal.controllerVistaInsumos.getBtnExportar().setDisable(true);
  	  		ControladorICsd_Principal.controllerActualizarStockInsumo.setId(insumoFxIN.idInsumo.get());
		
  		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
  	}
  	
  	
}
