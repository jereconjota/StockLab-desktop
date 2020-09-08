package controlador;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.util.converter.DateStringConverter;
import main.AppMain;
import modelo.Sector;
import modeloAux.SectorFX;

public class ControladorITablaAbmSectores {
	
	@FXML
    private TableView<SectorFX> tablaAbmSectores;
	
	@FXML
    private TableColumn<SectorFX, String> tablaAbmSectores_Sector;
	
	@FXML
    private TableColumn<SectorFX, Date> tablaAbmSectores_FechaAlta;

    @FXML
    private TableColumn<SectorFX, Date> tablaAbmSectores_FechaBaja;
    
    @FXML
    private TableColumn<SectorFX, String> tablaAbmSectores_Estado;
    
	@FXML
    private JFXTextField txtFieldBuscarSector;

    @FXML
    private JFXComboBox<String> cBoxSeleccioneSector;
    
    private ObservableList<SectorFX> obListSectores = FXCollections.observableArrayList();

    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorITablaAbmSectores() {
	
    }
    
    
    /**************************** GET - SET **********************************/
    
    
    public TableView<SectorFX> getTablaAbmSectores() {
		return tablaAbmSectores;
	}
    
    
    /********************************** METODOS ***********************************/
    
    
    @FXML
    public void initialize() {
    	definirTipoDatoColumnas();
    	escuchadorEventoTabla();
    	llenarComboBoxSeleccione();
    	limpiarTxtFieldBuscar();
    }
    


	private void definirTipoDatoColumnas() {
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
		tablaAbmSectores_Sector.setCellValueFactory(cellData -> cellData.getValue().nombreSector);
		tablaAbmSectores_FechaAlta.setCellValueFactory(cellData -> cellData.getValue().fechaAlta);
		tablaAbmSectores_FechaAlta.setCellFactory(TextFieldTableCell.<SectorFX, Date>forTableColumn(convertirDaS));
		tablaAbmSectores_FechaBaja.setCellValueFactory(cellData -> cellData.getValue().fechaBaja);
		tablaAbmSectores_FechaBaja.setCellFactory(TextFieldTableCell.<SectorFX, Date>forTableColumn(convertirDaS));
		tablaAbmSectores_Estado.setCellValueFactory(cellData -> cellData.getValue().estadoSector);
		
		alinearContenidoColumnas();
	}

    
	private void alinearContenidoColumnas() {
		tablaAbmSectores_Sector.setStyle("-fx-alignment: CENTER;");
		tablaAbmSectores_FechaAlta.setStyle("-fx-alignment: CENTER;");
		tablaAbmSectores_FechaBaja.setStyle("-fx-alignment: CENTER;");
		tablaAbmSectores_Estado.setStyle("-fx-alignment: CENTER;");
	}
	
	
	private void escuchadorEventoTabla() {
		tablaAbmSectores.setRowFactory( tv -> {
			TableRow<SectorFX> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					ControladorICsd_Principal.controllerPantAdmin.mostrarBtnBajaModifSector();
				}
			});
			return row;
		});
	}
	
	
	private void llenarComboBoxSeleccione() {
		ObservableList<String> itemsCombo = FXCollections.observableArrayList();
		itemsCombo.add("NOMBRE");
		cBoxSeleccioneSector.setItems(itemsCombo);
		
		cBoxSeleccioneSector.getSelectionModel().select(0);
	}
	
	
	public void limpiarTxtFieldBuscar() {
		txtFieldBuscarSector.clear();
	}
	
	
	public void removerDuplicadosEnTabla() {
		obListSectores.clear();
	}
	
	
	public void cargarSectores() {
		Transaction tx = null;
		try {			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sector");
			List<Sector> listaSectores = query.list();
			
    		for (Sector sector : listaSectores) {	
    			final SectorFX sectorFX = new SectorFX(sector);
    			obListSectores.add(sectorFX);
    		}
    		
    		appMain.getSession().close();
    		
			tablaAbmSectores.setItems(obListSectores);	
			definirOrdenDatos();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
	
	
	private void definirOrdenDatos() {
		tablaAbmSectores_Sector.setSortType(TableColumn.SortType.ASCENDING);
		tablaAbmSectores.getSortOrder().add(tablaAbmSectores_Sector);
	}
	
	
	public void limpiarSeleccion() {
		tablaAbmSectores.getSelectionModel().clearSelection();
	}
	

	//menejador del txtField buscar sector
	//el cual filtrara la tabla de acuerdo lo q se vaya ingresando en el textfield
    @FXML
    void filtrarSectores() {
    	FilteredList<SectorFX> filtrarDatos = new FilteredList<>(obListSectores, e -> true);
        txtFieldBuscarSector.setOnKeyReleased(e ->{
            txtFieldBuscarSector.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filtrarDatos.setPredicate((java.util.function.Predicate<? super SectorFX>) sectorFX->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    	
                	//filtro para busqueda "NRO ARTICULO"
                    if (cBoxSeleccioneSector.getSelectionModel().getSelectedItem().equals("NOMBRE")) {
					
                    	if(sectorFX.nombreSector.get().contains(newValue)){
                            return true;
                        } else if (sectorFX.nombreSector.get().toLowerCase().contains(lowerCaseFilter)) {
                        	return true;
                        }
                    	
					} 

                    return false;
                });
            });
            SortedList<SectorFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(tablaAbmSectores.comparatorProperty());
            tablaAbmSectores.setItems(sortedData);
            
        });
    }

    
    public String nombreSectorSeleccionado() {
		String nombreSector = tablaAbmSectores.getSelectionModel().getSelectedItem().nombreSector.get();
		return nombreSector;
	}

}
