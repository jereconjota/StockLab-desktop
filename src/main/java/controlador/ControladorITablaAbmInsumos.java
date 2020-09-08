package controlador;

import java.util.Date;
import java.util.List;

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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.util.converter.DateStringConverter;
import modelo.Insumo;
import modeloAux.InsumoFX;

public class ControladorITablaAbmInsumos {
	
	@FXML
	private TableView<InsumoFX> tablaAbmInsumos;
	
	@FXML
	private TableColumn<InsumoFX, String> tablaAbmInsumos_Insumo;
	
	@FXML
	private TableColumn<InsumoFX, String> tablaAbmInsumos_NroLote;
	
	@FXML
	private TableColumn<InsumoFX, String> tablaAbmInsumos_NroArticulo;
	
	@FXML
	private TableColumn<InsumoFX, String> tablaAbmInsumos_Referencia;
	
	@FXML
	private TableColumn<InsumoFX, String> tablaAbmInsumos_UnidadMedida;

	@FXML
	private TableColumn<InsumoFX, Integer> tablaAbmInsumos_Cantidad;
	
	@FXML
	private TableColumn<InsumoFX, Date> tablaAbmInsumos_FechaIngreso;
	
	@FXML
	private TableColumn<InsumoFX, Date> tablaAbmInsumos_FechaBaja;
	
	@FXML
	private TableColumn<InsumoFX, Date> tablaAbmInsumos_FechaVto;

	@FXML
	private TableColumn<InsumoFX, Float> tablaAbmInsumos_Precio;
	
	@FXML
	private TableColumn<InsumoFX, String> tablaAbmInsumos_Temperatura;
	
	@FXML
	private TableColumn<InsumoFX, Integer> tablaAbmInsumos_NroPedido;

	@FXML
	private JFXComboBox<String> cBoxSeleccioneInsumo;

	@FXML
	private JFXTextField txtFieldBuscarInsumo;

	@FXML
	private Label lblNombreCategoria;
	 
	@FXML
	private Label lblNombreSector;
	
	@FXML
	private Label lblSucursal;
	 
	private ObservableList<InsumoFX> obListInsumos = FXCollections.observableArrayList();
	
	
	/************************************  CONSTRUCTOR  ************************************/
	
	public ControladorITablaAbmInsumos() {
	
	}
	
	
	
	/**************************** GET - SET **********************************/
    
	public Label getLblNombreCategoria() {
		return lblNombreCategoria;
	}


	public Label getLblNombreSector() {
		return lblNombreSector;
	}
	
	
	public Label getLblSucursal() {
		return lblSucursal;
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
		tablaAbmInsumos_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaAbmInsumos_Referencia.setCellValueFactory(cellData -> cellData.getValue().referencia);
		tablaAbmInsumos_NroLote.setCellValueFactory(cellData -> cellData.getValue().pkNroLote);
		tablaAbmInsumos_UnidadMedida.setCellValueFactory(cellData -> cellData.getValue().unidadMedida);
		tablaAbmInsumos_FechaVto.setCellValueFactory(cellData -> cellData.getValue().fechaVencimiento);
		tablaAbmInsumos_FechaVto.setCellFactory(TextFieldTableCell.<InsumoFX, Date>forTableColumn(convertirDaS));
		tablaAbmInsumos_Cantidad.setCellValueFactory(cellData -> cellData.getValue().stockActual);
		tablaAbmInsumos_FechaBaja.setCellValueFactory(cellData -> cellData.getValue().fechaBaja);
		tablaAbmInsumos_FechaBaja.setCellFactory(TextFieldTableCell.<InsumoFX, Date>forTableColumn(convertirDaS));
		tablaAbmInsumos_Temperatura.setCellValueFactory(cellData -> cellData.getValue().temperatura);
		tablaAbmInsumos_FechaIngreso.setCellValueFactory(cellData -> cellData.getValue().fechaIngreso);
		tablaAbmInsumos_FechaIngreso.setCellFactory(TextFieldTableCell.<InsumoFX, Date>forTableColumn(convertirDaS));
		tablaAbmInsumos_Precio.setCellValueFactory(cellData -> cellData.getValue().precioInsumo);
		tablaAbmInsumos_NroArticulo.setCellValueFactory(cellData -> cellData.getValue().articulo);
		tablaAbmInsumos_NroPedido.setCellValueFactory(cellData -> cellData.getValue().nroPedido);
		
		alinearContenidoColumnas();
	}


	private void alinearContenidoColumnas() {
		tablaAbmInsumos_Insumo.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_Referencia.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_NroLote.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_UnidadMedida.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_FechaVto.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_Cantidad.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_FechaBaja.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_Temperatura.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_FechaIngreso.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_Precio.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_NroArticulo.setStyle("-fx-alignment: CENTER;");
		tablaAbmInsumos_NroPedido.setStyle("-fx-alignment: CENTER;");
	}
	
	
	private void escuchadorEventoTabla() {
		tablaAbmInsumos.setRowFactory( tv -> {
			TableRow<InsumoFX> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					
					if (tablaAbmInsumos.getSelectionModel().isSelected(row.getIndex(), tablaAbmInsumos_Insumo)) {
						ControladorICsd_Principal.controllerPantAdmin.mostrarBtnBajaModifInsumo();
					} else {
						tablaAbmInsumos.getSelectionModel().clearSelection();
						ControladorICsd_Principal.controllerPantAdmin.deshabilitarBotones2();
					}
				}
			});
			return row;
		});		
	}
	
	
	private void llenarComboBoxSeleccione() {
		ObservableList<String> itemsCombo = FXCollections.observableArrayList();
		itemsCombo.add("NRO ARTICULO");
		cBoxSeleccioneInsumo.setItems(itemsCombo);
		
		itemsCombo.add("NRO LOTE");
		cBoxSeleccioneInsumo.setItems(itemsCombo);
		
		cBoxSeleccioneInsumo.getSelectionModel().select(0);
	}
	
	
	@FXML
    void filtrarInsumos() {
		FilteredList<InsumoFX> filtrarDatos = new FilteredList<>(obListInsumos, e -> true);
        txtFieldBuscarInsumo.setOnKeyReleased(e ->{
            txtFieldBuscarInsumo.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filtrarDatos.setPredicate((java.util.function.Predicate<? super InsumoFX>) insumoFX->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    	
                	//filtro para busqueda "NRO ARTICULO"
                    if (cBoxSeleccioneInsumo.getSelectionModel().getSelectedItem().equals("NRO ARTICULO")) {
					
                    	if(insumoFX.articulo.get().contains(newValue)){
                            return true;
                        } else if (insumoFX.articulo.get().toLowerCase().contains(lowerCaseFilter)) {
                        	return true;
                        }
                    	
					} else { // NRO LOTE
						
						if(insumoFX.pkNroLote.get().contains(newValue)){
                            return true;
                        } else if (insumoFX.pkNroLote.get().toLowerCase().contains(lowerCaseFilter)) {
                        	return true;
                        }
						
					}

                    return false;
                });
            });
            SortedList<InsumoFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(tablaAbmInsumos.comparatorProperty());
            tablaAbmInsumos.setItems(sortedData);
            
        });
	}
	
	
	public void removerDuplicadosEnTabla() {
		obListInsumos.clear();
	}

	
	public void limpiarSeleccion() {
		tablaAbmInsumos.getSelectionModel().clearSelection();
	}
	
	
	public void cargarInsumos2(String nombreInsumoIN, Integer idSectorIN, List<Integer> listIdCategoriasIN, Integer idCategoriaIN, String nombreSucursalIN) {
		boolean entrar = true;
		List<Insumo> listaInsumosPorSectorCategoria = null;
		List<Insumo> listaInsumosIgualNombre = null;
		Integer flag = 0;  //es para saber si usar la lista de insumos --> 0 (filtro sector/categoria) , 1 (sin filtro, igual nombre)
		try {	
			if ((idSectorIN != null) && (idCategoriaIN == null)) {  //va a mostrar insumos x sector
				
				for (Integer idCategoriaList : listIdCategoriasIN) {
					List<Insumo> listaInsumosPorSector = CRUD.obtenerListaInsumosPorNombreYPorIdCategoria2(nombreInsumoIN, idCategoriaList, nombreSucursalIN);
					
					for (Insumo ins : listaInsumosPorSector) {
						final InsumoFX insFX = new InsumoFX(ins);
						obListInsumos.add(insFX);
					}	
				}
				ControladorICsd_Principal.controllerAltaInsumo.setIdSector(idSectorIN);
				ControladorICsd_Principal.controllerAltaInsumo.setIdCategoria(null);
				entrar = false;
				
			} else {
				
				if ((idSectorIN != null) && (idCategoriaIN != null)) {  //va a mostrar insumos x sector/categoria
					
					listaInsumosPorSectorCategoria = CRUD.obtenerListaInsumosPorNombreYPorIdCategoria2(nombreInsumoIN, idCategoriaIN, nombreSucursalIN);
					
					//tengo q guardar referencia a la categoria en el controlador alta insumo
					//xq si luego ingresa a modificar (ya sea cancela, o guarda una modif), debe mostrar solo los insumos de igual
					//nombre pero correspondiente a la categoria la cual eligio en el filtro
					ControladorICsd_Principal.controllerAltaInsumo.setIdCategoria(idCategoriaIN);
					ControladorICsd_Principal.controllerAltaInsumo.setIdSector(idSectorIN);
					
					flag = 0;
					
				} else {  //va a mostrar insumos de igual nombre (es decir, no se aplico ningun tipo de filtro en tabla insumo gral)
					
					listaInsumosIgualNombre = CRUD.obtenerListaInsumosPorNombre3(nombreInsumoIN, nombreSucursalIN);
					
					//dejo con valor x defecto a los lbl nombre sector y categoria
					this.getLblNombreSector().setText("En General");
					this.getLblNombreCategoria().setText("En General");
					
					//reseteo el valor del idCategoria, para q no quede la tabla en blanco
					//luego de modificar insumo (sin filtro-cancelar) y
					//modificar insumo (con filtro-cancelar) y finalmente
					//volver a modificar insumo (sin filtro-cancelar) y quede en blanco la tabla
					ControladorICsd_Principal.controllerAltaInsumo.setIdCategoria(null);
					ControladorICsd_Principal.controllerAltaInsumo.setIdSector(null);
					
					flag = 1;
				}
			}
					
			if (entrar) {
				if (flag == 0) {				
					for (Insumo insumo : listaInsumosPorSectorCategoria) {
		    			final InsumoFX insumoFX = new InsumoFX(insumo);
		    			obListInsumos.add(insumoFX);
		    		}
				} else {
					for (Insumo insumo : listaInsumosIgualNombre) {
		    			final InsumoFX insumoFX = new InsumoFX(insumo);
		    			obListInsumos.add(insumoFX);
		    		}	
				}	
			}
    		tablaAbmInsumos.setItems(obListInsumos);	
    		definirOrdenDatos();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void definirOrdenDatos() {
		tablaAbmInsumos_FechaIngreso.setSortType(TableColumn.SortType.ASCENDING);
		tablaAbmInsumos.getSortOrder().add(tablaAbmInsumos_FechaIngreso);
	}
	
	
	public void limpiarTxtFieldBuscar() {
		txtFieldBuscarInsumo.clear();
	}
	
	
	public Integer idInsumoSeleccionado() {
		Integer idInsumo = tablaAbmInsumos.getSelectionModel().getSelectedItem().idInsumo.get();
		return idInsumo;
	}


	public void setearLblSucursal(String nombreSucursalSeleccionado) {
		lblSucursal.setText(nombreSucursalSeleccionado);
	}
	
}
