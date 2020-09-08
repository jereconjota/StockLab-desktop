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
import modelo.Proveedor;
import modeloAux.ProveedorFX;

public class ControladorITablaAbmProveedor {
	
	@FXML
    private TableView<ProveedorFX> tablaAbmProvee;
	
	@FXML
    private TableColumn<ProveedorFX, String> tablaAbmProvee_Nombre;
	
	@FXML
    private TableColumn<ProveedorFX, String> tablaAbmProvee_NroCUIT;
	
	@FXML
    private TableColumn<ProveedorFX, String> tablaAbmProvee_NroProvee;
	
	@FXML
    private TableColumn<ProveedorFX, String> tablaAbmProvee_Direc;
	
	@FXML
    private TableColumn<ProveedorFX, Date> tablaAbmProvee_FechaAlta;
	
	@FXML
    private TableColumn<ProveedorFX, Date> tablaAbmProvee_FechaBaja;
	
	@FXML
    private TableColumn<ProveedorFX, String> tablaAbmProvee_Estado;

    @FXML
    private JFXTextField txtFieldBuscarProvee;

    @FXML
    private JFXComboBox<String> cBoxSeleccioneProvee;

    
    private ObservableList<ProveedorFX> obListProveedores = FXCollections.observableArrayList();
    

    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorITablaAbmProveedor() {
    	
	}
    
    
    
    /**************************** GET - SET **********************************/
    
    
    public TableView<ProveedorFX> getTablaAbmProvee() {
		return tablaAbmProvee;
	}
    
    
    /********************************** METODOS ***********************************/
    
    
    @FXML
    public void initialize() {
    	definirTipoDatoColumnas();
    	escuchadorEventoTabla();
    	llenarComboBoxSeleccione();
    }


	private void definirTipoDatoColumnas() {
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
		tablaAbmProvee_Nombre.setCellValueFactory(cellData -> cellData.getValue().nombreProveedor);
		tablaAbmProvee_NroCUIT.setCellValueFactory(cellData -> cellData.getValue().nroCuit);
		tablaAbmProvee_NroProvee.setCellValueFactory(cellData -> cellData.getValue().nroProveedor);
		tablaAbmProvee_Direc.setCellValueFactory(cellData -> cellData.getValue().direccionProveedor);
		tablaAbmProvee_Estado.setCellValueFactory(cellData -> cellData.getValue().estadoProveedor);
		tablaAbmProvee_FechaAlta.setCellValueFactory(cellData -> cellData.getValue().fechaAlta);
		tablaAbmProvee_FechaAlta.setCellFactory(TextFieldTableCell.<ProveedorFX, Date>forTableColumn(convertirDaS));
		tablaAbmProvee_FechaBaja.setCellValueFactory(cellData -> cellData.getValue().fechaBaja);
		tablaAbmProvee_FechaBaja.setCellFactory(TextFieldTableCell.<ProveedorFX, Date>forTableColumn(convertirDaS));
		
		alinearContenidoColumnas();
	}


	private void alinearContenidoColumnas() {
		tablaAbmProvee_Nombre.setStyle("-fx-alignment: CENTER;");
		tablaAbmProvee_NroCUIT.setStyle("-fx-alignment: CENTER;");
		tablaAbmProvee_NroProvee.setStyle("-fx-alignment: CENTER;");
		tablaAbmProvee_Direc.setStyle("-fx-alignment: CENTER;");
		tablaAbmProvee_Estado.setStyle("-fx-alignment: CENTER;");
		tablaAbmProvee_FechaAlta.setStyle("-fx-alignment: CENTER;");
		tablaAbmProvee_FechaBaja.setStyle("-fx-alignment: CENTER;");
	}
	
	
	private void escuchadorEventoTabla() {
		tablaAbmProvee.setRowFactory( tv -> {
			TableRow<ProveedorFX> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					ControladorICsd_Principal.controllerPantAdmin.mostrarBtnBajaModifProveedor();
				}
			});
			return row;
		});		
	}
	
	
	private void llenarComboBoxSeleccione() {
		ObservableList<String> itemsCombo = FXCollections.observableArrayList();
		itemsCombo.add("NOMBRE");
		cBoxSeleccioneProvee.setItems(itemsCombo);
		itemsCombo.add("CUIT");
		cBoxSeleccioneProvee.setItems(itemsCombo);
		itemsCombo.add("NRO PROVEEDOR");
		cBoxSeleccioneProvee.setItems(itemsCombo);
		
		cBoxSeleccioneProvee.getSelectionModel().select(0);
	}
	
	
	public void removerDuplicadosEnTabla() {
		obListProveedores.clear();
	}
	
	
	public void cargarProveedores() {
		Transaction tx = null;
		try {			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Proveedor");
			List<Proveedor> listaProveedores = query.list();
			
    		for (Proveedor proveedor : listaProveedores) {	
    			final ProveedorFX proveedorFX = new ProveedorFX(proveedor);
    			obListProveedores.add(proveedorFX);
    		}
    		
    		appMain.getSession().close();
    		
    		tablaAbmProvee.setItems(obListProveedores);	
    		definirOrdenDatos();
    		
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
	
	
	private void definirOrdenDatos() {
		tablaAbmProvee_Nombre.setSortType(TableColumn.SortType.ASCENDING);
		tablaAbmProvee.getSortOrder().add(tablaAbmProvee_Nombre);
	}
	
	
	public void limpiarSeleccion() {
		tablaAbmProvee.getSelectionModel().clearSelection();
	}
	
	
	public void limpiarTxtFieldBuscar() {
		txtFieldBuscarProvee.clear();
	}
	

    @FXML
    void filtrarProveedores() {
    	FilteredList<ProveedorFX> filtrarDatos = new FilteredList<>(obListProveedores, e -> true);
        txtFieldBuscarProvee.setOnKeyReleased(e ->{
            txtFieldBuscarProvee.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filtrarDatos.setPredicate((java.util.function.Predicate<? super ProveedorFX>) proveedorFX->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    
                    switch (cBoxSeleccioneProvee.getSelectionModel().getSelectedItem()) {
					case "NOMBRE":
						if(proveedorFX.nombreProveedor.get().contains(newValue)){
                            return true;
                        } else if (proveedorFX.nombreProveedor.get().toLowerCase().contains(lowerCaseFilter)) {
                        	return true;
                        }
						break;

					case "CUIT":
						if(String.valueOf(proveedorFX.nroCuit.get()).contains(newValue)){
                            return true;
                        } else if (String.valueOf(proveedorFX.nroCuit.get()).toLowerCase().contains(lowerCaseFilter)) {
                        	return true;
                        }
						break;
						
					case "NRO PROVEEDOR":
						if(String.valueOf(proveedorFX.nroProveedor.get()).contains(newValue)){
                            return true;
                        } else if (String.valueOf(proveedorFX.nroProveedor.get()).toLowerCase().contains(lowerCaseFilter)) {
                        	return true;
                        }
						break;
					}

                    return false;
                });
            });
            SortedList<ProveedorFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(tablaAbmProvee.comparatorProperty());
            tablaAbmProvee.setItems(sortedData);
            
        });
    }

    
  //retorna el id del proveedor seleccionado
  	public Integer idSeleccionado() {
  		Integer idProvee = null;
  		idProvee = tablaAbmProvee.getSelectionModel().getSelectedItem().idProveedor.get();
  		return idProvee;
  	}
  	
  	
}
