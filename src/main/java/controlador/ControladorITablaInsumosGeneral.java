package controlador;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import hibernate.util.CRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import main.AppMain;
import modelo.Categoria;
import modelo.Insumo;
import modelo.Sector;
import modeloAux.InsumoFX;

public class ControladorITablaInsumosGeneral {
	
	@FXML
    private TableView<InsumoFX> tablaInsumosGral;
	
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
	
	@FXML
    private TableColumn<InsumoFX, String> tablaInsumosGral_Sucursal;

    @FXML
    private Label lblBuscar;

    @FXML
    private JFXButton btnLimpiarFiltros;

    @FXML
    private JFXComboBox<String> cBoxSeleccioneInsumo;

    @FXML
    private JFXComboBox<String> cBoxSector;

    @FXML
    private JFXTextField txtFieldBuscarInsumo;

    @FXML
    private JFXComboBox<String> cBoxCategoria;
    
    private ObservableList<InsumoFX> obListInsumosGral = FXCollections.observableArrayList(); //para la tabla gral
    
    private ObservableList<InsumoFX> obListInsumosAux = FXCollections.observableArrayList(); //para la tabla gral (se le aplica clear)
    
    private ObservableList<InsumoFX> obListInsumosPorSector = FXCollections.observableArrayList(); //para aplicar filtro a insumos x sector
    
    private ObservableList<InsumoFX> obListInsumosPorCategoria = FXCollections.observableArrayList(); //para aplicar filtro a insumos x categoria
    
    private FilteredList<InsumoFX> filtrarDatos;
    
    private Integer idSector;
    
    private List<Integer> listIdCategorias = new ArrayList<>();
    
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorITablaInsumosGeneral() {
    	
	}
    
    
    
    /**************************** GET - SET **********************************/
    
    public JFXComboBox<String> getcBoxSector() {
		return cBoxSector;
	}
    
    
    public TableView<InsumoFX> getTablaInsumosGral() {
		return tablaInsumosGral;
	}


	public List<Integer> getListIdCategorias() {
		return listIdCategorias;
	}



	/********************************** METODOS ***********************************/

    
    @FXML
    public void initialize() {
    	definirTipoDatoColumnas(); 	
    	escuchadorEventoTabla();
    	llenarComboBoxSeleccione();
//    	llenarComboSector();        //lo llamo del controlador principal
    	limpiarTxtFieldBuscar();
    }


	private void definirTipoDatoColumnas() {
		tablaInsumosGral_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaInsumosGral_UnidadMedida.setCellValueFactory(cellData -> cellData.getValue().unidadMedida);
		tablaInsumosGral_StockGral.setCellValueFactory(cellData -> cellData.getValue().stockGeneral);
		tablaInsumosGral_PDP.setCellValueFactory(cellData -> cellData.getValue().pdp);
		tablaInsumosGral_Estado.setCellValueFactory(cellData -> cellData.getValue().estadoInsumo);
		tablaInsumosGral_Sucursal.setCellValueFactory(cellData -> cellData.getValue().nombreSucursal);
		
		alinearContenidoColumnas();
	}

	
	private void alinearContenidoColumnas() {
		tablaInsumosGral_Insumo.setStyle("-fx-alignment: CENTER;");
		tablaInsumosGral_UnidadMedida.setStyle("-fx-alignment: CENTER;");
		tablaInsumosGral_StockGral.setStyle("-fx-alignment: CENTER;");
		tablaInsumosGral_PDP.setStyle("-fx-alignment: CENTER;");
		tablaInsumosGral_Estado.setStyle("-fx-alignment: CENTER;");
		tablaInsumosGral_Sucursal.setStyle("-fx-alignment: CENTER;");
	}
	
	
	private void escuchadorEventoTabla() {
		tablaInsumosGral.setRowFactory( tv -> {
			TableRow<InsumoFX> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					
					if (tablaInsumosGral.getSelectionModel().isSelected(row.getIndex(), tablaInsumosGral_Insumo)) {
						ControladorICsd_Principal.controllerPantAdmin.mostrarBtnBajaVer();
					} else {
						tablaInsumosGral.getSelectionModel().clearSelection();
						ControladorICsd_Principal.controllerPantAdmin.deshabilitarBotones1();
					}
					
				}
			});
			return row;
		});
	}
	
	
	private void llenarComboBoxSeleccione() {
		ObservableList<String> itemsCombo = FXCollections.observableArrayList();
		itemsCombo.add("NOMBRE");
		cBoxSeleccioneInsumo.setItems(itemsCombo);
		
		cBoxSeleccioneInsumo.getSelectionModel().select(0);
	}
	
	
	public void llenarComboSector() {
		Transaction tx = null;
		try {
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sector");
			List<Sector> listaSectores = query.list();
			
			for (Sector sector : listaSectores) {
				if (!(sector.getNombreSector().equals("Administracion"))) {
					itemsCombo.add(sector.getNombreSector());
				}
			}
			
			appMain.getSession().close();
			
			cBoxSector.setItems(itemsCombo);
			cBoxSector.getSelectionModel().select(-1);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			//rolling back to save the test data
			tx.rollback();
			
			e.getMessage();
		}
	}
	
	
	public void limpiarTxtFieldBuscar() {
		txtFieldBuscarInsumo.clear();
	}
	
	
	public void removerDuplicadosEnTabla() {
		obListInsumosGral.clear();
		obListInsumosAux.clear();
	}
	
	
	public void cargarInsumosGral() {
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query1 = appMain.getSession().createQuery("select new Insumo(ins.nombreInsumo, ins.unidadMedida, sum(ins.stockActual), ins.pdp, "
					+ "ins.estadoInsumo, suc.nombreSucursal) "
					+ "from Insumo ins "
					+ "inner join ins.sucursal suc "
					+ "where ins.estadoInsumo = 'Activo' "
					+ "group by ins.nombreInsumo, suc.nombreSucursal");
    		
    		List<Insumo> listaInsumos = query1.list();
    		
    		for (Insumo insumo : listaInsumos) {
    			
    			final InsumoFX insumoFX = new InsumoFX();
    			
    			insumoFX.nombreInsumo.set(insumo.getNombreInsumo());
    			insumoFX.unidadMedida.set(insumo.getUnidadMedida());
    			Integer stockGral = (int) (long) insumo.getStockGeneral();
    			insumoFX.stockGeneral.set(stockGral);
    			insumoFX.pdp.set(insumo.getPdp());
    			insumoFX.estadoInsumo.set(insumo.getEstadoInsumo());
    			insumoFX.nombreSucursal.set(insumo.getNombreSucursal());
    			
    			obListInsumosGral.add(insumoFX);    
    			
    			obListInsumosAux.add(insumoFX);
    			
			}
			
    		appMain.getSession().close();
    		
    		tablaInsumosGral.setItems(obListInsumosAux);
    		
    		//luego defino el orden de los datos sobre la tabla
    		definirOrdenDatos();
    		
		} catch (Exception e) {
			e.printStackTrace();
			
			//rolling back to save the test data
			tx.rollback();
			
			e.getMessage();
		}
	}
	
	
	private void definirOrdenDatos() {
		tablaInsumosGral_Insumo.setSortType(TableColumn.SortType.ASCENDING);
		tablaInsumosGral.getSortOrder().add(tablaInsumosGral_Insumo);
	}	
	
	
	public void limpiarSeleccion() {
		tablaInsumosGral.getSelectionModel().clearSelection();
	}
	
	
	//metodo invocado en controlador iPantallaAdmin "INSUMOS"
	//verfico si el stock del insumo seleccionado en la tabla esta en CERO
	//en ese caso... se habilita el btn baja insumo.
	public boolean stockGralCero() {
		boolean esCero = false;
		if (tablaInsumosGral.getSelectionModel().getSelectedItem().stockGeneral.get() == 0) {
			esCero = true;
		}
		return esCero;
	}
	
	
	//manejador de evento del cBoxCategoria
	//el cual filtrara la tabla gral de insumos, de acuerdo a la categoria q se elija
    @FXML
    void eventoComboCategoria() {
		try {
			
			if (!cBoxCategoria.getSelectionModel().isEmpty()) {
				
				obListInsumosPorSector.clear();
				
				//el valor de "idSector", se setea cuando se selecciona un sector en el comboSector
				Categoria catBD = CRUD.obtenerCategoriaPorNombreCategoriaYPorIdSector1(cBoxCategoria.getSelectionModel().getSelectedItem(), idSector);
				Integer idCategoria = catBD.getPkIdCategoria();
				filtrarTablaPorCategoria(idCategoria);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }

    
  //manejador de evento del txtField "buscar insumo"...el cual filtrara la tabla gral.. de acuerdo a lo seleccionado
  	//en el comboBox "seleccione"
  	//(en este caso, solo esta disponible por "nombre")
    @FXML
    void filtrarInsumos() {
    	txtFieldBuscarInsumo.setOnKeyReleased(e ->{
            txtFieldBuscarInsumo.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filtrarDatos.setPredicate((java.util.function.Predicate<? super InsumoFX>) insumoFX->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    	
                  //filtra busqueda por "NOMBRE"
                    if (cBoxSeleccioneInsumo.getSelectionModel().getSelectedItem().equals("NOMBRE")) {
					
                    	if(insumoFX.nombreInsumo.get().contains(newValue)){
	                        return true;
	                    } else if (insumoFX.nombreInsumo.get().toLowerCase().contains(lowerCaseFilter)) {
	                    	return true;
	                    }
                    	
					} 

                    return false;
                });
            });
            SortedList<InsumoFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(tablaInsumosGral.comparatorProperty());
            tablaInsumosGral.setItems(sortedData);
            
        });
    }

    
  //manejador de click sobre el txtField buscar insumo, el cual se encarga de
  	//setear al "filtrarDatos", la correspondiente lista observable
  	//ya q si el "cBoxCategoria", no esta en "SIN FILTRO"
  	//debe usar la lista observable "obListItemsFiltro", la cual filtrara a los insumos
  	//q estan a su vez filtrados por una cierta categoria
  	//  y si esta en "SIN FILTRO", entonces le setea la lista observable "obListInsumosGral"
  	// el "filtrarDatos", es el valor q se utiliza en el metodo "filtrarInsumos"
  	// necesario para cuando vaya escribiendo sobre el txtField, empieze a filtrar la tabla
    @FXML
    void manejadorObList() {
		// si combo sector y categoria (ambos) estan sin seleccionar, entonces se aplicara filtro sobre los insumos en gral
		if ((cBoxSector.getSelectionModel().getSelectedIndex() == -1) && 
				(cBoxCategoria.getSelectionModel().getSelectedIndex() == -1)) { 
			
			filtrarDatos = new FilteredList<>(obListInsumosAux, e -> true);
			
		} else {
			//si se selecciono algun sector, pero no se selecciono ninguna categoria, el filtro se aplicara sobre los insumos de dicho sector
			if ((cBoxSector.getSelectionModel().getSelectedIndex() >= 0) &&
					(cBoxCategoria.getSelectionModel().getSelectedIndex() == -1)) {
				
				filtrarDatos = new FilteredList<>(obListInsumosPorSector, e -> true);
			
			} else {
				//significa q selecciono alguna categoria, entonces se aplica filtro a insumos x categoria
				filtrarDatos = new FilteredList<>(obListInsumosPorCategoria, e -> true);
			
			}
		}
    }

    
  //manejador del comboSector.. el cual ademas llena el comboCategoria.
    @FXML
    void eventoComboSector() {
		try {
			
			if (!cBoxSector.getSelectionModel().isEmpty()) {
				
				idSector = null;
				listIdCategorias.clear();
				
				obListInsumosAux.clear();  //aca limpiamos el contenido de la ob list aux
				
				if (cBoxCategoria.getSelectionModel().getSelectedIndex() != -1) {
					cBoxCategoria.getSelectionModel().clearSelection();
					cBoxCategoria.getSelectionModel().select(-1); //y vuelvo a dejarlo en la posicion x defecto
					cBoxCategoria.getItems().clear();
					obListInsumosPorCategoria.clear(); //tambien limpiamos el oblist de categoria (x si aplico un filtro x categoria)
				}
				
				Sector secBD = CRUD.obtenerSectorPorNombre(cBoxSector.getSelectionModel().getSelectedItem());

				idSector = secBD.getIdSector();
				llenarComboCategoria(idSector);
				filtrarTablaPorSector(listIdCategorias);
				  
				for (InsumoFX insumoFX : obListInsumosGral) {	//y le vuelvo poner a la obListAux el contenido q tenia, para usarlo mas adelante
					obListInsumosAux.add(insumoFX);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }

    
    @FXML
    public void limpiarFiltros() {
    	obListInsumosPorCategoria.clear();
    	obListInsumosPorSector.clear();
    	
    	cBoxCategoria.getSelectionModel().select(-1);
		cBoxCategoria.getItems().clear();
		
		cBoxSector.getSelectionModel().clearAndSelect(-1);
		
		cBoxSeleccioneInsumo.getSelectionModel().select(0);
		
		txtFieldBuscarInsumo.clear();
		
		obListInsumosAux.clear();
		for (InsumoFX insumoFX : obListInsumosGral) {
			obListInsumosAux.add(insumoFX);
		}
		
		tablaInsumosGral.setItems(obListInsumosAux);

		lblBuscar.requestFocus();
    }
    
    
  //verifica si el combo esta habilitado y si esta seleccionado alguna categoria,
  	//en caso de q asi fuera, setearlo a la pos 0 (SIN FILTRO)
  	//para q la tabla insumo no quede vacia
  	public void verficarComboSectorCategoria() {
  		if ((!(cBoxCategoria.isDisable())) && (cBoxCategoria.getSelectionModel().getSelectedIndex() > 0)) {
  			cBoxCategoria.getSelectionModel().select(0);
  		}
  	}
  	
  	
  	private void llenarComboCategoria(Integer idSectorIN) {
		try {
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
			List<Categoria> listaCategorias = CRUD.obtenerListaCategoriasPorIdSector(idSectorIN);

			for (Categoria categoria : listaCategorias) {
				itemsCombo.add(categoria.getNombreCategoria());
				listIdCategorias.add(categoria.getPkIdCategoria()); //guardo en un list aux, los id de las categorias q perteneces a dicho sector
			}
			
			cBoxCategoria.setItems(itemsCombo);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

  	
  	private void filtrarTablaPorSector(List<Integer> listIdCategorias) {  // el argumento "listIdCategorias", ya posee todas las id categorias de un sector
  		try {
  			obListInsumosPorSector.clear();
  			for (Integer idCategoriaList : listIdCategorias) {
  				
  				List<Insumo> listaInsumos = CRUD.obtenerListaInsumosStrockGralPorIdCategoria2(idCategoriaList);
  				
  				for (Insumo insumo : listaInsumos) {
  	    			final InsumoFX insumoFX = new InsumoFX();
  	    			
  	    			insumoFX.nombreInsumo.set(insumo.getNombreInsumo());
  	    			insumoFX.unidadMedida.set(insumo.getUnidadMedida());
  	    			Integer stockGral = (int) (long) insumo.getStockGeneral();
  	    			insumoFX.stockGeneral.set(stockGral);
  	    			insumoFX.pdp.set(insumo.getPdp());
  	    			insumoFX.estadoInsumo.set(insumo.getEstadoInsumo());
  	    			insumoFX.nombreSucursal.set(insumo.getNombreSucursal());
  	    			
  	    			obListInsumosPorSector.add(insumoFX);
  				}
  				
  			}
  			
  			tablaInsumosGral.setItems(obListInsumosPorSector);
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
  	
  	
  	private void filtrarTablaPorCategoria(Integer idCategoriaIN) {
  		try {
  			obListInsumosPorCategoria.clear();
  			
  			List<Insumo> listaInsumos = CRUD.obtenerListaInsumosStrockGralPorIdCategoria2(idCategoriaIN);
  			
  			for (Insumo insumo : listaInsumos) {
	    			final InsumoFX insumoFX = new InsumoFX();
	    			
	    			insumoFX.nombreInsumo.set(insumo.getNombreInsumo());
	    			insumoFX.unidadMedida.set(insumo.getUnidadMedida());
	    			Integer stockGral = (int) (long) insumo.getStockGeneral();
	    			insumoFX.stockGeneral.set(stockGral);
	    			insumoFX.pdp.set(insumo.getPdp());
	    			insumoFX.estadoInsumo.set(insumo.getEstadoInsumo());
  	    			insumoFX.nombreSucursal.set(insumo.getNombreSucursal());
  	    			
	    			obListInsumosPorCategoria.add(insumoFX);
				}
  			
  			tablaInsumosGral.setItems(obListInsumosPorCategoria);
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
  	
  	
  	public String nombreSeleccionado() {
		String nombre = tablaInsumosGral.getSelectionModel().getSelectedItem().nombreInsumo.get();
		return nombre;
	}
  	
  	
  	public Integer sectorSeleccionado() {
		Sector sec = null;
		Integer idSec = null;
		try {
			if (!(cBoxSector.getSelectionModel().isEmpty())) {
				sec = CRUD.obtenerSectorPorNombre(cBoxSector.getSelectionModel().getSelectedItem());
				idSec = sec.getIdSector();
				if (cBoxCategoria.getSelectionModel().isEmpty()) {
					ControladorICsd_Principal.controllerTablaInsumo.getLblNombreSector().setText(sec.getNombreSector());
					ControladorICsd_Principal.controllerTablaInsumo.getLblNombreCategoria().setText("En General");
				}
			} else {
				idSec = null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return idSec;
	}
  	
  	
  //metodo q se llama desde el controlador pantalla admin, del evento "ver insumos"
  	public Integer categoriaSeleccionada(Integer idSectorSeleecionadoIN) {  		
  		Integer idCat = null;
  		Categoria cat = null;
  		try {
  			Sector sec = CRUD.obtenerSectorPorId(idSectorSeleecionadoIN);
  			
  			if (!(cBoxCategoria.getSelectionModel().isEmpty())) {
  				
  				cat = CRUD.obtenerCategoriaPorNombreCategoriaYPorIdSector2(cBoxCategoria.getSelectionModel().getSelectedItem(), sec.getIdSector());
  				
  				idCat = cat.getPkIdCategoria();
  				
  				//si entra hasta aca, tonce luego si o si tengo q setear en la tabla abm insumos
  				//los lbl de nombre sector, y nombre categoria
  				ControladorICsd_Principal.controllerTablaInsumo.getLblNombreSector().setText(sec.getNombreSector());
  				ControladorICsd_Principal.controllerTablaInsumo.getLblNombreCategoria().setText(cat.getNombreCategoria());
  			} else {
  				
  				idCat = null;
  			}
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  			e.getMessage();
  		}
  		return idCat;
  	}
  	
  	
  	public void resetearComboBoxCategoria() {
		obListInsumosPorCategoria.clear();
		cBoxCategoria.getSelectionModel().select(-1);
		cBoxCategoria.getItems().clear();
	}



	public String sucursalSeleccionado() {
		String nombre = tablaInsumosGral.getSelectionModel().getSelectedItem().nombreSucursal.get();
		return nombre;
	}
  	
  	
}
