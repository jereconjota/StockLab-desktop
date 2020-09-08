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
import modelo.DetalleFactura;
import modelo.Factura;
import modeloAux.DetalleFacturaFX;
import modeloAux.FacturaFX;

public class ControladorIFacturas {
	
	@FXML
    private TableView<DetalleFacturaFX> tablaDetalle;
	
	@FXML
    private TableColumn<DetalleFacturaFX, Float> tablaDetalle_Importe;
	
	@FXML
    private TableColumn<DetalleFacturaFX, Integer> tablaDetalle_Cantidad;
	
	@FXML
    private TableColumn<DetalleFacturaFX, Float> tablaDetalle_Precio;
	
	@FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalle_NroLote;
	
	@FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalle_NroArticulo;
	
	@FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalle_Insumo;

    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalle_UnidadMedida;
    
    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalle_NroRemito;
    
    @FXML
    private TableColumn<DetalleFacturaFX, Date> tablaDetalle_Vencimiento;

    @FXML
    private TableView<FacturaFX> tablaFactura;
    
    @FXML
    private TableColumn<FacturaFX, String> tablaFactura_NroFactura;

    @FXML
    private TableColumn<FacturaFX, String> tablaFactura_TipoFactura;
    
    @FXML
    private TableColumn<FacturaFX, Date> tablaFactura_FechaFactura;

    @FXML
    private TableColumn<FacturaFX, Date> tablaFactura_FechaCargaFactura;

    @FXML
    private TableColumn<FacturaFX, Float> tablaFactura_Subtotal;

    @FXML
    private TableColumn<FacturaFX, String> tablaFactura_Proveedor;

    @FXML
    private TableColumn<FacturaFX, String> tablaFactura_CUIT;

    @FXML
    private JFXTextField txtFieldNroFactura;
    
    @FXML
    private JFXButton btnModificar;

	@FXML
    private JFXTextArea txtAreaObservacion;
    
    private ObservableList<DetalleFacturaFX> obListDetalleFactura = FXCollections.observableArrayList();
    
    private ObservableList<FacturaFX> obListFacturas = FXCollections.observableArrayList();
	
	private FacturaFX fact;
    
    
	/************************************  CONSTRUCTOR  ************************************/
	
	public ControladorIFacturas() {
		
	}
	
	
	
	/**************************** GET - SET **********************************/
	
	public TableView<FacturaFX> getTablaFactura() {
		return tablaFactura;
	}


	public JFXButton getBtnModificar() {
		return btnModificar;
	}
	
	
	public JFXTextField getTxtFieldNroFactura() {
		return txtFieldNroFactura;
	}



	/********************************** METODOS ***********************************/
	
	@FXML
    private void initialize() {
    	definirTipoDatoColumnasFactura();
    	definirTipoDatoColumnasDetalle();
    	//cargarFacturas();
    	seleccionarFila();
    	setearToolTip();
    }


	//se encarga de filtrar la tabla factura..
    //de acuerdo a los nrofactura..q vaya poniendo el user sobre el txtField NroFactura
    @FXML
    void filtrarFacturasPorNroFactura() {
    	FilteredList<FacturaFX> filtrarDatos = new FilteredList<>(obListFacturas, e -> true);
        txtFieldNroFactura.setOnKeyReleased(e ->{
            txtFieldNroFactura.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filtrarDatos.setPredicate((java.util.function.Predicate<? super FacturaFX>) facturaFX->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
         
                	if(facturaFX.nroFactura.get().contains(newValue)){
                        return true;
                    } else if (facturaFX.nroFactura.get().toLowerCase().contains(lowerCaseFilter)) {
                    	return true;
                    }
            
                    return false;
                });
            });
            SortedList<FacturaFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(tablaFactura.comparatorProperty());
            tablaFactura.setItems(sortedData);
            tablaFactura.getSelectionModel().clearSelection();
    		this.getBtnModificar().setDisable(true);
        });
    }
	
	
    @FXML
    void modificarFactura(ActionEvent event) {
    	ControladorILogin.controllerPpal.modificarFactura();
    	ControladorICsd_Principal.controllerModificarFactura.cargarFacturaYdetalle(tablaFactura.getSelectionModel().getSelectedItem().IdFactura);
    }
	
	
    public void limpiarSeleccionEnTablaFactura() {
		tablaFactura.getSelectionModel().clearSelection();
	}


	public void limpiarTablaDetalle() {
		tablaDetalle.getSelectionModel().clearSelection();
		obListDetalleFactura.clear();
	}


	public void removerDuplicadosTablaFactura() {
		obListFacturas.clear();
	}
	
	
	private void definirTipoDatoColumnasFactura() {
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
		tablaFactura_NroFactura.setCellValueFactory(cellData -> cellData.getValue().nroFactura);
		tablaFactura_TipoFactura.setCellValueFactory(cellData -> cellData.getValue().tipoFactura);
		tablaFactura_Proveedor.setCellValueFactory(cellData -> cellData.getValue().nombreProveedor);
		tablaFactura_CUIT.setCellValueFactory(cellData -> cellData.getValue().nroCuit);
		tablaFactura_FechaFactura.setCellValueFactory(cellData -> cellData.getValue().fechaFactura);
		tablaFactura_FechaFactura.setCellFactory(TextFieldTableCell.<FacturaFX, Date>forTableColumn(convertirDaS));
		tablaFactura_FechaCargaFactura.setCellValueFactory(cellData -> cellData.getValue().fechaCarga);
		tablaFactura_FechaCargaFactura.setCellFactory(TextFieldTableCell.<FacturaFX, Date>forTableColumn(convertirDaS));
		tablaFactura_Subtotal.setCellValueFactory(cellData -> cellData.getValue().subtotal);
		
		alinearContenidoColumnasFactura();
	}

	
	private void alinearContenidoColumnasFactura() {
		tablaFactura_NroFactura.setStyle("-fx-alignment: CENTER;");
		tablaFactura_TipoFactura.setStyle("-fx-alignment: CENTER;");
		tablaFactura_Proveedor.setStyle("-fx-alignment: CENTER;");
		tablaFactura_CUIT.setStyle("-fx-alignment: CENTER;");
		tablaFactura_FechaFactura.setStyle("-fx-alignment: CENTER;");
		tablaFactura_FechaCargaFactura.setStyle("-fx-alignment: CENTER;");
		tablaFactura_Subtotal.setStyle("-fx-alignment: CENTER-RIGHT;");
	}


	private void definirTipoDatoColumnasDetalle() {
		tablaDetalle_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaDetalle_NroArticulo.setCellValueFactory(cellData -> cellData.getValue().nroArticulo);
		tablaDetalle_NroLote.setCellValueFactory(cellData -> cellData.getValue().insumo);
		tablaDetalle_UnidadMedida.setCellValueFactory(cellData -> cellData.getValue().unidadMedida);
		tablaDetalle_Cantidad.setCellValueFactory(cellData -> cellData.getValue().cantidad);
		tablaDetalle_Precio.setCellValueFactory(cellData -> cellData.getValue().precio);
		tablaDetalle_Importe.setCellValueFactory(cellData -> cellData.getValue().importe);
		tablaDetalle_NroRemito.setCellValueFactory(cellData -> cellData.getValue().nroRemito);
		tablaDetalle_Vencimiento.setCellValueFactory(cellData -> cellData.getValue().fechaVencimiento);
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
		tablaDetalle_Vencimiento.setCellFactory(TextFieldTableCell.<DetalleFacturaFX, Date>forTableColumn(convertirDaS));
		
		alinearContenidoColumnasDetalle();
	}
	
	
	private void alinearContenidoColumnasDetalle() {
		tablaDetalle_Insumo.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_NroArticulo.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_NroLote.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_UnidadMedida.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Cantidad.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Precio.setStyle("-fx-alignment: CENTER-RIGHT;");
		tablaDetalle_Importe.setStyle("-fx-alignment: CENTER-RIGHT;");
		tablaDetalle_NroRemito.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Vencimiento.setStyle("-fx-alignment: CENTER;");
	}
	
	
	private void seleccionarFila(){
	//se le asigna una accion al click de cada fila de la tabla factura
		tablaFactura.setRowFactory( tv -> {
		    TableRow<FacturaFX> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getButton() == MouseButton.PRIMARY){
		        	if (tablaFactura.getSelectionModel().isSelected(row.getIndex(), tablaFactura_NroFactura)) {
		        		FacturaFX rowData = row.getItem();
		        		txtAreaObservacion.clear();
		        		txtAreaObservacion.setText(rowData.observacion.get());
		        		habilitarColNroRemito(rowData);
		        		llenarDetalleFactura(rowData);
		        		fact = rowData;
		        		this.getBtnModificar().setDisable(false);
					}else{
		        		tablaFactura.getSelectionModel().clearSelection();
		        		this.getBtnModificar().setDisable(true);
					}
		        }
		    });
		    return row ;
			});
	}
	
	
	private void habilitarColNroRemito(FacturaFX facFX) {
		try {
			if (facFX.tieneRemito.get().equals("Si")) {
				tablaDetalle_NroRemito.setVisible(true);
			} else {
				tablaDetalle_NroRemito.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	//una vez q se seleeciona una factura de la tabla, se llena automaticamente
	//la tabla detalle con los datos correspondientes
	private void llenarDetalleFactura(FacturaFX facturaFX) {
		try {
			//antes q nada limpio la tabla detalle
			limpiarTablaDetalle();
			
			List<DetalleFactura> listaDetalles = CRUD.obtenerListaDetalleFacturaPorIdFactura2(tablaFactura.getSelectionModel().getSelectedItem().IdFactura);
			
			for (DetalleFactura detalleFactura : listaDetalles) {
		
				final DetalleFacturaFX detalleFX = new DetalleFacturaFX(detalleFactura);
				
				if (detalleFactura.getRemito() != null) {
					detalleFX.nroRemito.set(detalleFactura.getNroRemito());
				}
				
				obListDetalleFactura.add(detalleFX);
			}
			
			tablaDetalle.setItems(obListDetalleFactura);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
		
	
	public void deshabilitarColNroRemito() {
    	tablaDetalle_NroRemito.setVisible(false);
    }


	public void limpiarTxtAreaObservacion() {
		txtAreaObservacion.clear();
	}
	
	
	public void cargarFacturas() {
		try {			
			SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
			
			List<Factura> listaFacturas = CRUD.obtenerListaFacturas();
					
    		for (Factura factura : listaFacturas) {
    			
    			final FacturaFX facturaFX = new FacturaFX(factura);
    			
				String fecha1 = formato.format(factura.getFechaFactura());
				Date fechaFactura = formato.parse(fecha1);
				facturaFX.fechaFactura.set(fechaFactura);
				
				String fecha2 = formato.format(factura.getFechaCarga());
				Date fechaCarga = formato.parse(fecha2);
				facturaFX.fechaCarga.set(fechaCarga);
				
    			obListFacturas.add(facturaFX);
    		}
    		
			tablaFactura.setItems(obListFacturas);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void setearToolTip() {
		btnModificar.setTooltip(new Tooltip("Abre pantalla Modificar Factura"));
	}



	public void setearOrdenDatos() {
		tablaFactura_FechaCargaFactura.setSortType(TableColumn.SortType.DESCENDING);
		tablaFactura.getSortOrder().add(tablaFactura_FechaCargaFactura);
	}

}
