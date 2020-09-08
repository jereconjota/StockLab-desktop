package controlador;

import java.util.ArrayList;
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
import javafx.scene.input.MouseButton;
import main.AppMain;
import modelo.Categoria;
import modelo.Insumo;
import modeloAux.InsumoFX;

public class ControladorITablaInsumosGeneralStock {
	
	@FXML
    private TableView<InsumoFX> tablaInsumosGralStock;
	
	@FXML
    private TableColumn<InsumoFX, String> tablaInsumosGral_Insumo;

	@FXML
    private TableColumn<InsumoFX, String> tablaInsumosGral_UnidadMedida;
	
	@FXML
    private TableColumn<InsumoFX, Integer> tablaInsumosGral_StockGral;
	
	@FXML
    private TableColumn<InsumoFX, Integer> tablaInsumosGral_PDP;
	
	@FXML
    private TableColumn<InsumoFX, String> tablaInsumosGral_Estado;

    private ObservableList<InsumoFX> obListInsumosGralStock = FXCollections.observableArrayList(); //para la tabla gral
    
    private ObservableList<InsumoFX> obListInsumosAux = FXCollections.observableArrayList(); //para la tabla gral (se le aplica clear)
    
//    private ObservableList<InsumoFX> obListInsumosPorSector = FXCollections.observableArrayList(); //para aplicar filtro a insumos x sector
//    
    private ObservableList<InsumoFX> obListInsumosPorCategoria = FXCollections.observableArrayList(); //para aplicar filtro a insumos x categoria
//    
//    private FilteredList<InsumoFX> filtrarDatos;
//    
//    private Integer idSector;
    
    private List<Integer> listIdCategorias = new ArrayList<>();
    
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorITablaInsumosGeneralStock() {
    	
	}
    
    
    
    /**************************** GET - SET **********************************/
    
    
    public TableView<InsumoFX> getTablaInsumosGral() {
		return tablaInsumosGralStock;
	}


	public List<Integer> getListIdCategorias() {
		return listIdCategorias;
	}

	
	public TableColumn<InsumoFX, String> getTablaInsumosGral_Insumo() {
		return tablaInsumosGral_Insumo;
	}


	public ObservableList<InsumoFX> getObListInsumosGralStock() {
		return obListInsumosGralStock;
	}



	public ObservableList<InsumoFX> getObListInsumosAux() {
		return obListInsumosAux;
	}



	/********************************** METODOS ***********************************/

    
    @FXML
    public void initialize() {
    	definirTipoDatoColumnas(); 	
//    	escuchadorEventoTabla();
//    	llenarComboSector();        //lo llamo del controlador principal
    }


	private void definirTipoDatoColumnas() {
		tablaInsumosGral_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaInsumosGral_UnidadMedida.setCellValueFactory(cellData -> cellData.getValue().unidadMedida);
		tablaInsumosGral_StockGral.setCellValueFactory(cellData -> cellData.getValue().stockGeneral);
		tablaInsumosGral_PDP.setCellValueFactory(cellData -> cellData.getValue().pdp);
		tablaInsumosGral_Estado.setCellValueFactory(cellData -> cellData.getValue().estadoInsumo);
		
		alinearContenidoColumnas();
	}

	
	private void alinearContenidoColumnas() {
		tablaInsumosGral_Insumo.setStyle("-fx-alignment: CENTER;");
		tablaInsumosGral_UnidadMedida.setStyle("-fx-alignment: CENTER;");
		tablaInsumosGral_StockGral.setStyle("-fx-alignment: CENTER;");
		tablaInsumosGral_PDP.setStyle("-fx-alignment: CENTER;");
		tablaInsumosGral_Estado.setStyle("-fx-alignment: CENTER;");
	}
	
	
	private void escuchadorEventoTabla() {
		tablaInsumosGralStock.setRowFactory( tv -> {
			TableRow<InsumoFX> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					
					if (tablaInsumosGralStock.getSelectionModel().isSelected(row.getIndex(), tablaInsumosGral_Insumo)) {
						ControladorICsd_Principal.controllerPantAdmin.mostrarBtnBajaVer();
					} else {
						tablaInsumosGralStock.getSelectionModel().clearSelection();
						ControladorICsd_Principal.controllerPantAdmin.deshabilitarBotones1();
					}
					
				}
			});
			return row;
		});
	}
	
	
	public void removerDuplicadosEnTabla() {
		obListInsumosGralStock.clear();
		obListInsumosAux.clear();
	}
	
	
	public void cargarInsumosGral() {
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query1 = appMain.getSession().createQuery("select new Insumo(ins.nombreInsumo, ins.unidadMedida, sum(ins.stockActual), ins.pdp, ins.estadoInsumo) "
					+ "from Insumo ins "
					+ "group by ins.nombreInsumo");
    		
    		List<Insumo> listaInsumos = query1.list();
    		
    		for (Insumo insumo : listaInsumos) {
    			
    			final InsumoFX insumoFX = new InsumoFX();
    			
    			insumoFX.nombreInsumo.set(insumo.getNombreInsumo());
    			insumoFX.unidadMedida.set(insumo.getUnidadMedida());
    			Integer stockGral = (int) (long) insumo.getStockGeneral();
    			insumoFX.stockGeneral.set(stockGral);
    			insumoFX.pdp.set(insumo.getPdp());
    			insumoFX.estadoInsumo.set(insumo.getEstadoInsumo());
    			
    			obListInsumosGralStock.add(insumoFX);    
    			
    			obListInsumosAux.add(insumoFX);
			}
			
    		appMain.getSession().close();
    		
    		tablaInsumosGralStock.setItems(obListInsumosAux);
    		
    		//luego defino el orden de los datos sobre la tabla
    		definirOrdenDatos();
    		
		} catch (Exception e) {
			e.printStackTrace();
			
			//rolling back to save the test data
			tx.rollback();
			
			e.getMessage();
		}
	}
	
  	public void filtrarTablaPorCategoria(String categIN) {
  			obListInsumosPorCategoria.clear();
	  		Integer idCategoriaBD = null;
	  		
  		try {
  			//Buscamos el ID de la categoria seleccionada
  			for (Categoria categoria : ControladorICsd_Principal.controllerVistaInsumosStock.getListaCategoria()) {
  				if (categoria.getNombreCategoria().equals(categIN) && 
  						categoria.getSector().getIdSector().equals(ControladorICsd_Principal.controllerVistaInsumosStock.getSectorElegido().getIdSector())) {
  					idCategoriaBD = categoria.getPkIdCategoria();
  					break;
  				}
  			}

			
  			List<Insumo> listaInsumos = CRUD.obtenerListaInsumosStrockGralPorIdCategoria(idCategoriaBD);
  			
  			for (Insumo insumo : listaInsumos) {
	    			final InsumoFX insumoFX = new InsumoFX();
	    			
	    			insumoFX.nombreInsumo.set(insumo.getNombreInsumo());
	    			insumoFX.unidadMedida.set(insumo.getUnidadMedida());
	    			Integer stockGral = (int) (long) insumo.getStockGeneral();
	    			insumoFX.stockGeneral.set(stockGral);
	    			insumoFX.pdp.set(insumo.getPdp());
	    			insumoFX.estadoInsumo.set(insumo.getEstadoInsumo());
	    			
	    			obListInsumosPorCategoria.add(insumoFX);
				}
  			
  			tablaInsumosGralStock.setItems(obListInsumosPorCategoria);
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();

		}
	}
  	
	public void definirOrdenDatos() {
		tablaInsumosGral_Insumo.setSortType(TableColumn.SortType.ASCENDING);
		tablaInsumosGralStock.getSortOrder().add(tablaInsumosGral_Insumo);
	}	
	
	
	public void limpiarSeleccion() {
		tablaInsumosGralStock.getSelectionModel().clearSelection();
	}
	
	
	//metodo invocado en controlador iPantallaAdmin "INSUMOS"
	//verfico si el stock del insumo seleccionado en la tabla esta en CERO
	//en ese caso... se habilita el btn baja insumo.
	public boolean stockGralCero() {
		boolean esCero = false;
		if (tablaInsumosGralStock.getSelectionModel().getSelectedItem().stockGeneral.get() == 0) {
			esCero = true;
		}
		return esCero;
	}
	

  	
  	
  	public String nombreSeleccionado() {
		String nombre = tablaInsumosGralStock.getSelectionModel().getSelectedItem().nombreInsumo.get();
		return nombre;
	}
  	

  	
}
