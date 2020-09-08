package controlador;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import hibernate.util.CRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.util.converter.DateStringConverter;
import main.AppMain;
import modelo.Categoria;
import modelo.Sector;
import modeloAux.CategoriaFX;

public class ControladorITablaAbmCategorias {
	
	@FXML
    private TableView<CategoriaFX> tablaAbmCategorias;
	
	@FXML
    private TableColumn<CategoriaFX, String> tablaAbmCategorias_Categoria;
	
	@FXML
    private TableColumn<CategoriaFX, Date> tablaAbmCategorias_FechaAlta;
	
	@FXML
    private TableColumn<CategoriaFX, Date> tablaAbmCategorias_FechaBaja;
	
	@FXML
    private TableColumn<CategoriaFX, String> tablaAbmCategorias_Sector;
	
	@FXML
    private TableColumn<CategoriaFX, String> tablaAbmCategorias_Estado;
	
	@FXML
    private JFXComboBox<String> cBoxSeleccioneCategoria;

    @FXML
    private JFXComboBox<String> cBoxFiltroSectorCate;

    @FXML
    private JFXTextField txtFieldBuscarCategoria;

    
    private ObservableList<CategoriaFX> obListCategorias = FXCollections.observableArrayList(); //para mostrar las categorias en gral
    
    private ObservableList<CategoriaFX> obListItemsFiltro = FXCollections.observableArrayList(); //para tabla  gral, pero filtrada x sector
    
    private FilteredList<CategoriaFX> filtrarDatos;
    
    
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorITablaAbmCategorias() {
    	
	}
       
    
    /**************************** GET - SET **********************************/
    
    public TableView<CategoriaFX> getTablaAbmCategorias() {
		return tablaAbmCategorias;
	}
    
    
    /********************************** METODOS ***********************************/
    

    @FXML
    public void initialize() {
    	definirTipoDatoColumnas();
    	escuchadorEventoTabla();
    	llenarComboBoxSeleccione();
//    	llenarComboBoxSector();       //lo llamo del controlador principal
    	setearToolTipComboSector();
    	limpiarTxtFieldBuscar();
    }


	private void definirTipoDatoColumnas() {
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
		tablaAbmCategorias_Categoria.setCellValueFactory(cellData -> cellData.getValue().nombreCategoria);
		tablaAbmCategorias_FechaAlta.setCellValueFactory(cellData -> cellData.getValue().fechaAlta);
		tablaAbmCategorias_FechaAlta.setCellFactory(TextFieldTableCell.<CategoriaFX, Date>forTableColumn(convertirDaS));
		tablaAbmCategorias_FechaBaja.setCellValueFactory(cellData -> cellData.getValue().fechaBaja);
		tablaAbmCategorias_FechaBaja.setCellFactory(TextFieldTableCell.<CategoriaFX, Date>forTableColumn(convertirDaS));
		tablaAbmCategorias_Sector.setCellValueFactory(cellData -> cellData.getValue().nombreSector);
		tablaAbmCategorias_Estado.setCellValueFactory(cellData -> cellData.getValue().estadoCategoria);
		
		alinearContenidoColumnas();
	}


	private void alinearContenidoColumnas() {
		tablaAbmCategorias_Categoria.setStyle("-fx-alignment: CENTER;");
		tablaAbmCategorias_FechaAlta.setStyle("-fx-alignment: CENTER;");
		tablaAbmCategorias_FechaBaja.setStyle("-fx-alignment: CENTER;");
		tablaAbmCategorias_Sector.setStyle("-fx-alignment: CENTER;");
		tablaAbmCategorias_Estado.setStyle("-fx-alignment: CENTER;");
	}
	
	
	private void escuchadorEventoTabla() {
		tablaAbmCategorias.setRowFactory( tv -> {
			TableRow<CategoriaFX> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					ControladorICsd_Principal.controllerPantAdmin.mostrarBtnBajaModifCategoria();
				}
			});
			return row;
		});		
	}
	
	
	private void llenarComboBoxSeleccione() {
		ObservableList<String> itemsCombo = FXCollections.observableArrayList();
		itemsCombo.add("NOMBRE");
		cBoxSeleccioneCategoria.setItems(itemsCombo);
		
		cBoxSeleccioneCategoria.getSelectionModel().select(0);
	}
	
	
	public void llenarComboBoxSector() {
		Transaction tx = null;
		ObservableList<String> itemsCombo = FXCollections.observableArrayList();
		itemsCombo.add("SECTOR");
		try {
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
			
			cBoxFiltroSectorCate.setItems(itemsCombo);
			cBoxFiltroSectorCate.getSelectionModel().select(0); //q aparezca x defecto en "SECTOR"
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
	
	
	private void setearToolTipComboSector() {
		cBoxFiltroSectorCate.setTooltip(new Tooltip("Filtra Categorias por Sector"));
	}
	
	
	public void limpiarTxtFieldBuscar() {
		txtFieldBuscarCategoria.clear();
	}
	
	
	public void removerDuplicadosEnTabla() {
		obListCategorias.clear();
	}
	
	
	public void cargarCategorias() {
		Transaction tx = null;
		try {			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Categoria");
			List<Categoria> listaCategorias = query.list();
			
    		for (Categoria categoria : listaCategorias) {	
    			final CategoriaFX categoriaFX = new CategoriaFX(categoria);
    			obListCategorias.add(categoriaFX);
    		}
			
    		appMain.getSession().close();
    		
    		tablaAbmCategorias.setItems(obListCategorias);
    		definirOrdenDatos();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
	
	
	private void definirOrdenDatos() {
		tablaAbmCategorias_Sector.setSortType(TableColumn.SortType.ASCENDING);
		tablaAbmCategorias.getSortOrder().add(tablaAbmCategorias_Sector);
	}
	
	
	public void limpiarSeleccion() {
		tablaAbmCategorias.getSelectionModel().clearSelection();
	}
	
    
	//manejador de cBoxSector
	//el cual filtrara la tabla gral de categorias, de acuerdo al sector q se elija
    @FXML
    void filtrarCategoriasPorSector() {
    	try {
    		if (!cBoxFiltroSectorCate.getSelectionModel().isEmpty()) {
    			
    			if (cBoxFiltroSectorCate.getSelectionModel().getSelectedIndex() == 0) { // SECTOR
    				//muestro tabla sin filtro
    				tablaAbmCategorias.setItems(obListCategorias);
    			} else {
    				String nombreSector = cBoxFiltroSectorCate.getSelectionModel().getSelectedItem();
    				//antes limpio la lista observable
    				obListItemsFiltro.clear();
    				filtrarTabla(nombreSector);
    			}
    			
    		}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }

    
  //manejador del txtField buscar categoria.. el cual filtrara la tabla, segun vaya escribiendo el user
    @FXML
    void filtrarCategorias() {
        txtFieldBuscarCategoria.setOnKeyReleased(e ->{
            txtFieldBuscarCategoria.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filtrarDatos.setPredicate((java.util.function.Predicate<? super CategoriaFX>) categoriaFX->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    	
                  //filtra busqueda por "CATEGORIA"
                    if (cBoxSeleccioneCategoria.getSelectionModel().getSelectedItem().equals("NOMBRE")) {
					
                    	if(categoriaFX.nombreCategoria.get().contains(newValue)){
	                        return true;
	                    } else if (categoriaFX.nombreCategoria.get().toLowerCase().contains(lowerCaseFilter)) {
	                    	return true;
	                    }
                    	
					} 

                    return false;
                });
            });
            SortedList<CategoriaFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(tablaAbmCategorias.comparatorProperty());
            tablaAbmCategorias.setItems(sortedData); 
        });
    }

    
    //manejador de click sobre el txtField buscar categoria, el cual se encarga de
	//setear al "filtrarDatos", la correspondiente lista observable
	//ya q si el "cBoxSector", no esta en "SECTOR"
	//debe usar la lista observable "obListItemsFiltro", la cual filtrara a los categorias
	//por dicho sector
	//  y si esta en "SECTOR", entonces le setea la lista observable "obListInsumosGral"
	// el "filtrarDatos", es el valor q se utiliza en el metodo "filtrarInsumos"
	// necesario para cuando vaya escribiendo sobre el txtField, empieze a filtrar la tabla
    @FXML
    void manejadorObList() {
    	if (cBoxFiltroSectorCate.getSelectionModel().getSelectedIndex() == 0) { // SECTOR
			filtrarDatos = new FilteredList<>(obListCategorias, e -> true);
		} else {
			filtrarDatos = new FilteredList<>(obListItemsFiltro, e -> true);
		}
    }

    
    public String nombreCategoriaSeleccionado() {
		String nombreCategoria = tablaAbmCategorias.getSelectionModel().getSelectedItem().nombreCategoria.get();
		return nombreCategoria;
	}
    
    
    public String sectorCategoriaSeleccionado() {
		String sectorCategoria = tablaAbmCategorias.getSelectionModel().getSelectedItem().nombreSector.get();
		return sectorCategoria;
	}
    
    
    private void filtrarTabla(String nombreSectorIN) {
		Sector sec = null;
		Transaction tx = null;
		try {
			sec = CRUD.obtenerSectorPorNombre(nombreSectorIN);
			
			//la linea de abajo no me sirve, xq al cerrar sesion depue de obtener la lista..
			//cuando creo el objeto fx, necesito el nombre del sector y no lo tengo
			//x eso es mejor hacer el query aca mismo y depue de la creacion del fx, cerrar la sesion
			//para poder acceder a todos los datos
//			List<Categoria> listaCategorias = CRUD.obtenerListaCategoriasPorIdSector(sec.getIdSector()); 
			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Categoria "
					+ "where sector= :id");
			query.setInteger("id", sec.getIdSector());
			List<Categoria> listaCategorias = query.list();
			
			for (Categoria categoria : listaCategorias) {
				CategoriaFX categoriaFX = new CategoriaFX(categoria);
				obListItemsFiltro.add(categoriaFX);
			}
			
			appMain.getSession().close();
			
			tablaAbmCategorias.setItems(obListItemsFiltro);
			
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
	}
    
    
    public void resetearComboBoxSector() {
    	cBoxFiltroSectorCate.getSelectionModel().clearAndSelect(0);
    }
}
