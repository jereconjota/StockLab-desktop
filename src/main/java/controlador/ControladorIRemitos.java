package controlador;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
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
import modelo.DetalleRemito;
import modelo.Remito;
import modeloAux.DetalleRemitoFX;
import modeloAux.RemitoFX;

public class ControladorIRemitos {
	
	@FXML
    private TableView<RemitoFX> tablaRemito;
	
	@FXML
    private TableColumn<RemitoFX, String> tablaRemito_NroRemito;
	
	@FXML
    private TableColumn<RemitoFX, String> tablaRemito_NroFactura;
	
	@FXML
    private TableColumn<RemitoFX, String> tablaRemito_Proveedor;
	
	@FXML
    private TableColumn<RemitoFX, String> tablaRemito_CUIT;
	
	@FXML
    private TableColumn<RemitoFX, Date> tablaRemito_FechaRemito;
	
	@FXML
    private TableColumn<RemitoFX, Date> tablaRemito_FechaCargaRemito;
	
	@FXML
    private TableView<DetalleRemitoFX> tablaDetalle;
	
	@FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalle_Insumo;
	
	@FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalle_NroArticulo;
	
	@FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalle_NroLote;
	
	@FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalle_UnidadMedida;
	
	@FXML
    private TableColumn<DetalleRemitoFX, Integer> tablaDetalle_Cantidad;
	
	@FXML
    private TableColumn<DetalleRemitoFX, Date> tablaDetalle_Vencimiento;

	@FXML
    private JFXTextField txtFieldNroRemito;
	
	@FXML
    private JFXButton btnModificar;
	
	@FXML
    private JFXTextArea textAreaObservacion;
	
    private ObservableList<DetalleRemitoFX> obListDetalleRemito = FXCollections.observableArrayList();
    
    private ObservableList<RemitoFX> obListRemitos = FXCollections.observableArrayList();
    
    private RemitoFX remitoSeleccionado; 
	
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorIRemitos() {
    	
	}
    
    
    /**************************** GET - SET **********************************/
    
    
    public JFXTextField getTxtFieldNroRemito() {
		return txtFieldNroRemito;
	}
    
    
    public JFXButton getBtnModificar() {
		return btnModificar;
	}

    
	public RemitoFX getRemitoSeleccionado() {
		return remitoSeleccionado;
	}

	public void setRemitoSeleccionado(RemitoFX remitoSeleccionado) {
		this.remitoSeleccionado = remitoSeleccionado;
	}


	/********************************** METODOS ***********************************/
    
    
    @FXML
    private void initialize() {
    	definirTipoDatoColumnasRemito();
    	definirTipoDatoColumnasDetalle();
//    	definirColumnasEditables();
    	seleccionarFila(); 
    	setearToolTip();
    }


	@FXML
    void mostrarModificarRemito(ActionEvent event) {
		try {
			ControladorILogin.controllerPpal.modificarRemito();
			ControladorICsd_Principal.controllerModificarRemito.setDesdeVerRemitos(true);
			ControladorICsd_Principal.controllerModificarRemito.completarCampos(this.getRemitoSeleccionado());
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    

    //se encarga de filtrar la tabla remito..
    //de acuerdo a los nroRemito..q vaya poniendo el user sobre el txtField NroRemito
	@FXML
    void filtrarRemitosPorNroRemito() {
		FilteredList<RemitoFX> filtrarDatos = new FilteredList<>(obListRemitos, e -> true);
        txtFieldNroRemito.setOnKeyReleased(e ->{
            txtFieldNroRemito.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filtrarDatos.setPredicate((java.util.function.Predicate<? super RemitoFX>) remitoFX->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
         
	                	if(remitoFX.nroRemito.get().contains(newValue)){
	                        return true;
	                    } else if (remitoFX.nroRemito.get().toLowerCase().contains(lowerCaseFilter)) {
	                    	return true;
	                    }
	            
	                    return false;
	                });
            });
            SortedList<RemitoFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(tablaRemito.comparatorProperty());
            tablaRemito.setItems(sortedData);
            this.getBtnModificar().setDisable(true);
            this.tablaRemito.getSelectionModel().clearSelection();
        });
    }
    
    
	private void definirTipoDatoColumnasRemito() {
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");

		tablaRemito_NroRemito.setCellValueFactory(cellData -> cellData.getValue().nroRemito);
		tablaRemito_NroFactura.setCellValueFactory(cellData -> cellData.getValue().factura);
		tablaRemito_Proveedor.setCellValueFactory(cellData -> cellData.getValue().nombreProveedor);
		tablaRemito_CUIT.setCellValueFactory(cellData -> cellData.getValue().nroCuit);
		
		tablaRemito_FechaRemito.setCellValueFactory(cellData -> cellData.getValue().fechaRemito);
		tablaRemito_FechaRemito.setCellFactory(TextFieldTableCell.<RemitoFX, Date>forTableColumn(convertirDaS));
		tablaRemito_FechaCargaRemito.setCellValueFactory(cellData -> cellData.getValue().fechaCarga);
		tablaRemito_FechaCargaRemito.setCellFactory(TextFieldTableCell.<RemitoFX, Date>forTableColumn(convertirDaS));
		
		alinearContenidoColumnasRemito();
	}

	
	private void alinearContenidoColumnasRemito() {
		tablaRemito_NroRemito.setStyle("-fx-alignment: CENTER;");
		tablaRemito_NroFactura.setStyle("-fx-alignment: CENTER;");
		tablaRemito_Proveedor.setStyle("-fx-alignment: CENTER;");
		tablaRemito_CUIT.setStyle("-fx-alignment: CENTER;");
		tablaRemito_FechaRemito.setStyle("-fx-alignment: CENTER;");
		tablaRemito_FechaCargaRemito.setStyle("-fx-alignment: CENTER;");
	}
	
	
	private void definirTipoDatoColumnasDetalle() {
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");

		tablaDetalle_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaDetalle_NroArticulo.setCellValueFactory(cellData -> cellData.getValue().nroArticulo);
		tablaDetalle_NroLote.setCellValueFactory(cellData -> cellData.getValue().insumo);
		tablaDetalle_UnidadMedida.setCellValueFactory(cellData -> cellData.getValue().unidadMedida);
		tablaDetalle_Cantidad.setCellValueFactory(cellData -> cellData.getValue().cantidad);
		
		tablaDetalle_Vencimiento.setCellValueFactory(cellData -> cellData.getValue().fechaVencimiento);
		tablaDetalle_Vencimiento.setCellFactory(TextFieldTableCell.<DetalleRemitoFX, Date>forTableColumn(convertirDaS));

		alinearContenidoColumnasDetalle();
	}
	
	
	private void alinearContenidoColumnasDetalle() {
		tablaDetalle_Insumo.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_NroArticulo.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_NroLote.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_UnidadMedida.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Cantidad.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Vencimiento.setStyle("-fx-alignment: CENTER;");

	}
    
    
	private void seleccionarFila(){
		//se le asigna una accion al click de cada fila de la tabla remito
		tablaRemito.setRowFactory( tv -> {
		    TableRow<RemitoFX> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		    	
		        if (event.getButton() == MouseButton.PRIMARY){
		        	
		        	if (tablaRemito.getSelectionModel().isSelected(row.getIndex(), tablaRemito_NroRemito)) {
		        		RemitoFX rowData = row.getItem();
		        		textAreaObservacion.clear();
		        		textAreaObservacion.setText(rowData.observacion.get());
		        		llenarDetalleRemito(rowData);
						
		        		//si el nroFactura de la fila seleccionada esta vacia
		        		//habilito btn modificar (el cual llevara a pantalla modificar)
	        			btnModificar.setDisable(false);
	        			this.setRemitoSeleccionado(rowData);
		        		
					}else{
		        		tablaRemito.getSelectionModel().clearSelection();
		        		btnModificar.setDisable(true);
		        		this.setRemitoSeleccionado(null);
					}
		        }
		    });
		    return row ;
			});
	}
    
    
	private void setearToolTip() {
		btnModificar.setTooltip(new Tooltip("Abre pantalla Modificar Remito"));
	}
	
	
	public void limpiarSeleccionEnTablaRemito() {
		tablaRemito.getSelectionModel().clearSelection();
	}
	
	
	public void limpiarTablaDetalle() {
		tablaDetalle.getSelectionModel().clearSelection();
		obListDetalleRemito.clear();
	}
	
	
	public void removerDuplicadosTablaRemito() {
		obListRemitos.clear();
	}
	
	
	public void resetearBtnModificar() {
		btnModificar.setDisable(true);
	}
	
	
	public void limpiarTxtAreaObservacion() {
		textAreaObservacion.clear();
	}
    
	
	public void cargarRemitos() {
		try {			
			SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
			
			List<Remito> listaRemitos = CRUD.obtenerListaRemitos();

    		for (Remito remito : listaRemitos) {
    			
    			final RemitoFX remitoFX = new RemitoFX(remito);

				String fecha1 = formato.format(remito.getFechaRemito());
				Date fechaRemito = formato.parse(fecha1);
				remitoFX.fechaRemito.set(fechaRemito);
				
				String fecha2 = formato.format(remito.getFechaCarga());
				Date fechaCarga = formato.parse(fecha2);
				remitoFX.fechaCarga.set(fechaCarga);
			
    			obListRemitos.add(remitoFX);
    		}
    		
			tablaRemito.setItems(obListRemitos);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	//una vez q se seleeciona un remito de la tabla, se llena automaticamente
	//la tabla detalle con los datos correspondientes
	private void llenarDetalleRemito(RemitoFX remitoFX) {
		try {
			//antes q nada limpio la tabla detalle
			limpiarTablaDetalle();
			
			List<DetalleRemito> listaDetalles = CRUD.obtenerListaDetalleRemitoPorIdRemito1(remitoFX.idRemito);
			
			for (DetalleRemito detalleRemito : listaDetalles) {
				
				final DetalleRemitoFX detalleRemitoFX = new DetalleRemitoFX(detalleRemito);
					
				obListDetalleRemito.add(detalleRemitoFX);
			}
			
			tablaDetalle.setItems(obListDetalleRemito);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	
	public void setearOrdenDatos() {
		tablaRemito_FechaCargaRemito.setSortType(TableColumn.SortType.DESCENDING);
		tablaRemito.getSortOrder().add(tablaRemito_FechaCargaRemito);
	}
	
}
