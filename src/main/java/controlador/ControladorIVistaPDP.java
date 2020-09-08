package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import main.AppMain;
import modelo.Categoria;
import modelo.Insumo;
import modelo.Proveedor;
import modelo.Sector;
import modeloAux.InsumoFX;

public class ControladorIVistaPDP {
	
	@FXML
    private BorderPane borderTabla;

    @FXML
    private JFXButton btnExportarExcel;

    @FXML
    private JFXButton btnExportarOC;
    
    @FXML
    private JFXTextField textFieldBuscar;

    @FXML
    private JFXButton btnLimpiarFiltros;

    @FXML
    private ImageView logo;

    @FXML
    private BorderPane borderPanePDP;

    @FXML
    private JFXComboBox<String> comboBoxSector;
    
    @FXML
    private JFXComboBox<String> comboBoxCategoria;
    
    @FXML
    private JFXComboBox<String> comboBoxProveedor;
    
//    private List<Categoria> listaCategorias;
    
//    private ObservableList<InsumoFX> obListPDPfiltrados = FXCollections.observableArrayList();
//
//	private ObservableList<InsumoFX> obListPDPfiltradosPorSector = FXCollections.observableArrayList();
    
    private ObservableList<InsumoFX> obListPDPGral = FXCollections.observableArrayList(); //para la tabla gral
    
    private ObservableList<InsumoFX> obListPDPAux = FXCollections.observableArrayList(); //para la tabla gral (se le aplica clear)
    
    private ObservableList<InsumoFX> obListPDPAuxParaProvee = FXCollections.observableArrayList(); //para la tabla gral (se le aplica clear)
    
    private ObservableList<InsumoFX> obListPDPfiltradosPorSector = FXCollections.observableArrayList(); //para aplicar filtro a insumos x sector
    
    private ObservableList<InsumoFX> obListPDPfiltradosPorCategoria = FXCollections.observableArrayList(); //para aplicar filtro a insumos x categoria
	
	FilteredList<InsumoFX> filtrarDatos;
	
	private Integer idSector;
    
    private List<Integer> listIdCategorias = new ArrayList<>();
    
    private ObservableList<InsumoFX> obListFiltroText = FXCollections.observableArrayList(); //para tener referencia a los insumos filtrados x el txtField
    
    private Integer idProveedorAux;

    @FXML
    private JFXButton btnAgregarAorden;
    
    @FXML
    private JFXButton btnLimpiarTablaDetalle;
//    
//    @FXML
//    private TableView<InsumoFX> detalleOrdenDeCompra;
//    
////    @FXML
////    private TableColumn<InsumoFX, String> detalleOrdenDeCompra_Articulo;
////    
////    @FXML
////    private TableColumn<InsumoFX, String> detalleOrdenDeCompra_Sector;
////    
////    @FXML
////    private TableColumn<InsumoFX, String> detalleOrdenDeCompra_Referencia;
////    
////    @FXML
////    private TableColumn<InsumoFX, String> detalleOrdenDeCompra_Categoria;
//    
//
//	@FXML
//    private TableColumn<InsumoFX, String> detalleOrdenDeCompra_Insumo;
//    
//    @FXML
//    private TableColumn<InsumoFX, Integer> tablaPDP_stockActual;
//
//    @FXML
//    private TableColumn<InsumoFX, Integer> tablaPDP_pdp;
//    
//    @FXML
//    private TableColumn<InsumoFX, String> tablaPDP_proveedor;
//    
    private ObservableList<InsumoFX> obListDetalleOC = FXCollections.observableArrayList(); //para la tabla detalle orden de compra
//
//    @FXML
//    private Label lblBorradorOrdenCompra;

	/************************************  CONSTRUCTOR  ************************************/
    
    public ControladorIVistaPDP() {
	
    }
    
    
    
    /**************************** GET - SET **********************************/
    
    public BorderPane getBorderTabla() {
		return borderTabla;
	}
    
    
    public JFXTextField getTextFieldBuscar() {
		return textFieldBuscar;
	}
    
    
    public JFXComboBox<String> getComboBoxSector() {
		return comboBoxSector;
	}
    
    
    public JFXComboBox<String> getComboBoxCategoria() {
		return comboBoxCategoria;
	}
    
    
    public void setObListPDPGral(ObservableList<InsumoFX> obListPDPGral) {
		this.obListPDPGral = obListPDPGral;
	}
    
    
    public void setObListPDPAux(ObservableList<InsumoFX> obListPDPAux) {
		this.obListPDPAux = obListPDPAux;
	}


	public ObservableList<InsumoFX> getObListPDPfiltradosPorSector() {
		return obListPDPfiltradosPorSector;
	}

//    public TableView<InsumoFX> getDetalleOrdenDeCompra() {
//		return detalleOrdenDeCompra;
//	}
//	public void setDetalleOrdenDeCompra(TableView<InsumoFX> detalleOrdenDeCompra) {
//		this.detalleOrdenDeCompra = detalleOrdenDeCompra;
//	}
//	public TableColumn<InsumoFX, String> getDetalleOrdenDeCompra_Insumo() {
//		return detalleOrdenDeCompra_Insumo;
//	}
//	public void setDetalleOrdenDeCompra_Insumo(TableColumn<InsumoFX, String> detalleOrdenDeCompra_Insumo) {
//		this.detalleOrdenDeCompra_Insumo = detalleOrdenDeCompra_Insumo;
//	}

	public JFXButton getBtnExportarOC() {
		return btnExportarOC;
	}
	public void setBtnExportarOC(JFXButton btnExportarOC) {
		this.btnExportarOC = btnExportarOC;
	}

	public JFXButton getBtnAgregarAorden() {
		return btnAgregarAorden;
	}
	public void setBtnAgregarAorden(JFXButton btnAgregarAorden) {
		this.btnAgregarAorden = btnAgregarAorden;
	}

	/********************************** METODOS ***********************************/
    
    @FXML
    void initialize() {
		cargarLogo();
		borderTabla.requestFocus();
//		cargarSectoresYcategorias();
//		definirTipoColumnas();
//		seleccionarFila();
	}
//    
//	private void seleccionarFila(){
//	//se le asigna una accion al click de cada fila de la tabla insumos
//	detalleOrdenDeCompra.setRowFactory( tv -> {
//	    TableRow<InsumoFX> row = new TableRow<>();
//	    row.setOnMouseClicked(event -> {
//	        if (event.getButton() == MouseButton.PRIMARY){
//	        	
//	        	if (event.getClickCount() == 2) { // DOBLE CLICK
//	        		if (detalleOrdenDeCompra.getSelectionModel().getSelectedItem()!=null) {
//	        			obListDetalleOrdenDeCompra.remove(detalleOrdenDeCompra.getSelectionModel().getSelectedItem());
//		            	detalleOrdenDeCompra.setItems(obListDetalleOrdenDeCompra);
//		            	
//		            	//luego tengo q verificar q si no queda mas elementos en la tabla detalle 
//		            	//entonces deshabilito los botones guardar y deshacer
//			            	if (obListDetalleOrdenDeCompra.size() == 0) {
//			            		this.getBtnExportarOC().setDisable(true);
//			        		}
//					}else{
//						detalleOrdenDeCompra.getSelectionModel().clearSelection();
//					}
//	        		
//				}
//	        }
//	    });
//	    return row ;
//		});
//	}
	
//    private void definirTipoColumnas() {
//    	detalleOrdenDeCompra_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
//		tablaPDP_stockActual.setCellValueFactory(cellData -> cellData.getValue().stockGeneral);
//		tablaPDP_pdp.setCellValueFactory(cellData -> cellData.getValue().pdp);
//		tablaPDP_proveedor.setCellValueFactory(cellData -> cellData.getValue().proveedor);
//		alinearContenidoColumnas();
//	}
//	
//	private void alinearContenidoColumnas() {
//		detalleOrdenDeCompra_Insumo.setStyle("-fx-alignment: CENTER;");
//		tablaPDP_pdp.setStyle("-fx-alignment: CENTER;");
//		tablaPDP_stockActual.setStyle("-fx-alignment: CENTER;");
//		tablaPDP_proveedor.setStyle("-fx-alignment: CENTER;");
//	}
	
    private void cargarLogo() {
		FileInputStream input;
		try {
			input = new FileInputStream("img/diagnosdahinten.png");
			Image image = new Image(input);
	    	logo.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
		}
	} 

    public void llenarComboSector() {
		Transaction tx = null;
		try {
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sector where Estado_Sector= \'Activo\'");
			List<Sector> listaSectores = query.list();
			
			for (Sector sector : listaSectores) {
				if (!(sector.getNombreSector().equals("Administracion"))) {
					itemsCombo.add(sector.getNombreSector());
				}
			}
			
			appMain.getSession().close();
			
			comboBoxSector.setItems(itemsCombo);
			comboBoxSector.getSelectionModel().select(-1);
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
    
    /** si el user inicia con este filtro, se desactiva el combo Proveedor **/
    @FXML
    void filtroSector() {	//manejador evento comboBox Sector
    	try {
			
			if (!comboBoxSector.getSelectionModel().isEmpty()) {
				
				idSector = null;
				listIdCategorias.clear();
				
				obListPDPAux.clear();  //aca limpiamos el contenido de la ob list aux
				
				if (comboBoxCategoria.getSelectionModel().getSelectedIndex() != -1) {
					comboBoxCategoria.getSelectionModel().clearSelection();
					comboBoxCategoria.getSelectionModel().select(-1); //y vuelvo a dejarlo en la posicion x defecto
					comboBoxCategoria.getItems().clear();
					obListPDPfiltradosPorCategoria.clear(); //tambien limpiamos el oblist de categoria (x si aplico un filtro x categoria)
				}
				
				Sector secBD = CRUD.obtenerSectorPorNombre(comboBoxSector.getSelectionModel().getSelectedItem());

				idSector = secBD.getIdSector();
				llenarComboCategoria(idSector);
				
				if (comboBoxProveedor.getSelectionModel().isEmpty()) {	//funcionamiento normal
					
					comboBoxProveedor.setDisable(true);
					
					//el filtrar filtrarTablaPorSector(idSector) resuelve el problema de mostrar insumos q no estaban en su PDP,
					//x eso la linea de abajo esta comentada!
//					filtrarTablaPorSector(listIdCategorias); 
					
					filtrarTablaPorSector(idSector); 
					
				} else {	//significa q aplico antes filtro provee a la tabla
					
					filtrarTablaPorSector2(idSector, idProveedorAux);
				}
				  
				for (InsumoFX insumoFX : obListPDPGral) {	//y le vuelvo poner a la obListAux el contenido q tenia, para usarlo mas adelante
					
					obListPDPAux.add(insumoFX);
				}
				
				this.btnAgregarAorden.setDisable(false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }

	@FXML
    void filtroCategoria() {	//manejador evento comboBox Categoria
    	try {
			
			if (!comboBoxCategoria.getSelectionModel().isEmpty()) {
				
				obListPDPfiltradosPorSector.clear();
				
				//el valor de "idSector", se setea cuando se selecciona un sector en el comboSector
				Categoria catBD = CRUD.obtenerCategoriaPorNombreCategoriaYPorIdSector1(comboBoxCategoria.getSelectionModel().getSelectedItem(), idSector);
				Integer idCategoria = catBD.getPkIdCategoria();
				filtrarTablaPorCategoria(idCategoria);
				
				this.btnAgregarAorden.setDisable(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    @FXML
    void filtrarPorCoincidencias() {
    	textFieldBuscar.setOnKeyReleased(e ->{
            textFieldBuscar.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filtrarDatos.setPredicate((java.util.function.Predicate<? super InsumoFX>) insumoFX->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    	
                  //filtra busqueda por "NOMBRE"
                    if(insumoFX.nombreInsumo.get().contains(newValue)){
                        return true;
                    } else if (insumoFX.nombreInsumo.get().toLowerCase().contains(lowerCaseFilter)) {
                    	return true;
                    } 

                    return false;
                });
            });
            SortedList<InsumoFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().comparatorProperty());
            obListFiltroText = sortedData;
            ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().setItems(sortedData);       
        });
    	
    	this.btnAgregarAorden.setDisable(false);
    }

    @FXML
    void manejadorObList() {
		// si combo sector y categoria (ambos) estan sin seleccionar y combo proveedor, entonces se aplicara filtro sobre los insumos en gral
		if ((comboBoxSector.getSelectionModel().getSelectedIndex() == -1) && 
				(comboBoxCategoria.getSelectionModel().getSelectedIndex() == -1) &&
				(comboBoxProveedor.getSelectionModel().getSelectedIndex() == -1)) { 
			
			filtrarDatos = new FilteredList<>(obListPDPAux, e -> true);
			
		} else {
			//si combo proveedor activado y combo sector desactivado, filtra sobre insumos del proveedor
			if ((comboBoxProveedor.getSelectionModel().getSelectedIndex() >= 0) &&
					(comboBoxSector.getSelectionModel().getSelectedIndex() == -1)) {
				
				filtrarDatos = new FilteredList<>(obListPDPAuxParaProvee, e -> true);
			
			} else {
				
				//si se selecciono algun sector, pero no se selecciono ninguna categoria, el filtro se aplicara sobre los insumos de dicho sector
				if ((comboBoxSector.getSelectionModel().getSelectedIndex() >= 0) &&
						(comboBoxCategoria.getSelectionModel().getSelectedIndex() == -1)) {
					
					filtrarDatos = new FilteredList<>(obListPDPfiltradosPorSector, e -> true);
				
				} else {
					//significa q selecciono alguna categoria, entonces se aplica filtro a insumos x categoria
					filtrarDatos = new FilteredList<>(obListPDPfiltradosPorCategoria, e -> true);
				
				}
				
			}
		}
    }

    @FXML
    public void limpiarFiltros() {
    	obListPDPfiltradosPorCategoria.clear();
    	obListPDPfiltradosPorSector.clear();
    	
    	obListPDPAuxParaProvee.clear();
    	
    	comboBoxCategoria.getSelectionModel().select(-1);
		comboBoxCategoria.getItems().clear();
		
		comboBoxSector.getSelectionModel().clearAndSelect(-1);
		
		comboBoxProveedor.getSelectionModel().clearAndSelect(-1);
		comboBoxProveedor.setDisable(false);
		
		textFieldBuscar.clear();
		
		obListPDPAux.clear();
		
		for (InsumoFX insumoFX : obListPDPGral) {
			obListPDPAux.add(insumoFX);
			}
		
		this.getBtnAgregarAorden().setDisable(false);

		ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().setItems(obListPDPAux);
    	ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().requestFocus();
    }

    @FXML
    void exportarExcel(ActionEvent event) throws FileNotFoundException, IOException {
    	try {
    		ObservableList<InsumoFX> obListPDPimprimir;
//        	if(this.getComboBoxSector().getSelectionModel().getSelectedIndex() == -1 && textFieldBuscar.getText().equals("")){
//        		obListPDPimprimir = ControladorICsd_Principal.controllerTablaPDP.obListInsumo;
//        		}else if (this.getComboBoxSector().getSelectionModel().getSelectedIndex()!= -1 && textFieldBuscar.getText().equals("")) {
//        			obListPDPimprimir = obListPDPfiltradosPorSector;
//    	    			}else{
//    	    	    		obListPDPimprimir = obListPDPGral;
//    					}
    		
    		if ((comboBoxSector.getSelectionModel().getSelectedIndex() == -1) 				//imprime sin filtros, texto en blanco
    				&& (comboBoxCategoria.getSelectionModel().getSelectedIndex() == -1)
    				&& (comboBoxProveedor.getSelectionModel().getSelectedIndex() == -1)
    				&& (textFieldBuscar.getText().equals(""))) {
    			obListPDPimprimir = obListPDPAux;
			} else {
				if ((comboBoxProveedor.getSelectionModel().getSelectedIndex() >= 0)			//imprime filtro proveedor, texto en blanco
						&& (comboBoxSector.getSelectionModel().getSelectedIndex() == -1)
						&& (textFieldBuscar.getText().equals(""))) {
					obListPDPimprimir = obListPDPAuxParaProvee;
				} else {
					if ((comboBoxSector.getSelectionModel().getSelectedIndex() >= 0) 			//imprime filtros sector, texto en blanco
		    				&& (comboBoxCategoria.getSelectionModel().getSelectedIndex() == -1)
		    				&& (textFieldBuscar.getText().equals(""))) {
							obListPDPimprimir = obListPDPfiltradosPorSector;
						} else {
							if ((comboBoxSector.getSelectionModel().getSelectedIndex() >= 0) 		//imprime filtros categoria, texto en blanco
		    				&& (comboBoxCategoria.getSelectionModel().getSelectedIndex() >= 0)
		    				&& (textFieldBuscar.getText().equals(""))) {
								obListPDPimprimir = obListPDPfiltradosPorCategoria;
							} else {
								obListPDPimprimir = obListFiltroText;						//imprime sin/con filtros sector/categoria, con filtro de texto
							}
						}
				}
			}
    		
        	if (obListPDPimprimir.isEmpty()) {
        		Alert alert = new Alert(AlertType.WARNING);
    			alert.setTitle("ATENCION");
    			alert.setHeaderText("Tabla sin datos" );
    			alert.setContentText("No se puede exportar porque no hay datos en la tabla");
    			alert.showAndWait();
    		} else {
    	    	//Creamos libro de excel
    	    	Workbook wb = new HSSFWorkbook();  // or new XSSFWorkbook();
    	        Sheet sheet = wb.createSheet("Nueva Planilla");
    	        //Creamos los archivos
    	        FileOutputStream fileOut = null;
    	        File file = null;
    	        //creamos los string que van en la primer fila, los titulos de las columnas
    	        String [] encabezadoPDP = {"              INSUMO              ","STOCK GENERAL","PUNTO DE PEDIDO","PROVEEDOR"};
    	        // Creamos la primer fila del excel, donde van los titulos
    	        Row filas = sheet.createRow((short)0);
    	        //integer auxiliar, lo usamos para contar las proximas filas
    	        int rowNum = 1;
    	        
    	        ////////////PDP - Llenamos la tabla
    	        	for (int i = 0; i < encabezadoPDP.length; i++) {
    	        		//Creamos la celdas, asignamos ancho y centramos el contenido
    	    			Cell celda = filas.createCell(i);
    	    	        CellStyle cellStyle = wb.createCellStyle();
    	    	        cellStyle.setAlignment(HorizontalAlignment.CENTER);
    	    			celda.setCellStyle(cellStyle);
    	    			celda.setCellValue(encabezadoPDP[i]);
    	    	        sheet.autoSizeColumn((short)i);
    	            }
    	        	
    	        	//centramos el contenido de las celdas
        	        CellStyle cellStyle = wb.createCellStyle();
        	        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        	        
    	    		for (InsumoFX insumoPDP : obListPDPimprimir) {
    	    			//Verificamos que es la tabla PDP y comenzamos a cargar datos en las celdas y filas
    	    			filas = sheet.createRow(rowNum++);
    	                for (int i = 0; i < encabezadoPDP.length; i++) {
    	                	Cell cell1 = filas.createCell(i);
    		                	switch (i) {
    		                	//Segun el titulo del encabezado, se llena la celda con lo que corresponda
    								case 0:
    				                    cell1.setCellValue(insumoPDP.nombreInsumo.get());
    									break;
    								case 1:
    				                    cell1.setCellValue(insumoPDP.stockGeneral.get());
    				                    break;
    								case 2:
    									cell1.setCellValue(insumoPDP.pdp.get());
    									break;
    								case 3:
    									cell1.setCellValue(insumoPDP.proveedor.get());
    								default:
    									break;
    							}
//    		            		//centramos el contenido de las celdas
//    		        	        CellStyle cellStyle = wb.createCellStyle();
//    		        	        cellStyle.setAlignment(HorizontalAlignment.CENTER);
    		        			cell1.setCellStyle(cellStyle);
    					}	
    				}    	
    	    
    	        //damos a elegir donde guardar el archivo exportado
    	    	FileChooser fileChooser = new FileChooser();
    	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel files (*.xls)", "*.xls");
    	        fileChooser.getExtensionFilters().add(extFilter);
    	    	file = new File("");
    	        file = fileChooser.showSaveDialog(ControladorILogin.controllerPpal.getPrimaryStage());
    	    	if(file!=null){
    	    		try {
    	    			fileOut = new FileOutputStream(file);
    	    			wb.write(fileOut);
    	    			fileOut.flush();
    	    	        fileOut.close();
    	    	        
    	    	        wb.close();        /////////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!
    	
    	    			} catch (IOException e) {
    	    				e.printStackTrace();
    	    				e.getMessage();
    	    			} 
    	    	}
    	    	//posisionamos el foco en la tabla
    	        ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().requestFocus();        
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
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
			
			comboBoxCategoria.setItems(itemsCombo);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    private void filtrarTablaPorSector(Integer idSectorIN) {
  		try {
  			obListPDPfiltradosPorSector.clear();
  				
			List<Insumo> listaInsumos = CRUD.obtenerListaInsumosStrockGralPdpPorIdSector(idSectorIN);
			
			for (Insumo insumo : listaInsumos) {
    			final InsumoFX insumoFX = new InsumoFX();
    			
    			insumoFX.nombreInsumo.set(insumo.getNombreInsumo());
    			insumoFX.unidadMedida.set(insumo.getUnidadMedida());
    			Integer stockGral = (int) (long) insumo.getStockGeneral();
    			insumoFX.stockGeneral.set(stockGral);
    			insumoFX.pdp.set(insumo.getPdp());
    			insumoFX.estadoInsumo.set(insumo.getEstadoInsumo());
    			insumoFX.proveedor.set(insumo.getNombreProveedor());
    			
    			//Lo necesitamos a la hr de exportar a orden de compra
    			insumoFX.articulo.set(insumo.getNroArticulo());
    			insumoFX.referencia.set(insumo.getReferencia());
    			insumoFX.nombreCategoria.set(insumo.getNombreCategoria());
    			insumoFX.nombreSector.set(insumo.getNombreSector());
    			
    			obListPDPfiltradosPorSector.add(insumoFX);
			}
  				
  			ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().setItems(obListPDPfiltradosPorSector);
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    /** filtra la tabla de insumos q a su vez ya se encuentra filtrada por proveedor 
     * 		idProveedorAuxIN se setea cuando se elije el provee en el comboProvee **/
    private void filtrarTablaPorSector2(Integer idSectorIN, Integer idProveedorAuxIN) {
    	try {
  			obListPDPfiltradosPorSector.clear();
  			
			List<Insumo> listaInsumos = CRUD.obtenerListaInsumosStrockGralPdpPorIdSectorAndIdProveedor(idSectorIN, idProveedorAuxIN);
			
			for (Insumo insumo : listaInsumos) {
    			final InsumoFX insumoFX = new InsumoFX();
    			
    			insumoFX.nombreInsumo.set(insumo.getNombreInsumo());
    			insumoFX.unidadMedida.set(insumo.getUnidadMedida());
    			Integer stockGral = (int) (long) insumo.getStockGeneral();
    			insumoFX.stockGeneral.set(stockGral);
    			insumoFX.pdp.set(insumo.getPdp());
    			insumoFX.estadoInsumo.set(insumo.getEstadoInsumo());
    			insumoFX.proveedor.set(insumo.getNombreProveedor());
    			
    			//Lo necesitamos a la hr de exportar a orden de compra
    			insumoFX.articulo.set(insumo.getNroArticulo());
    			insumoFX.referencia.set(insumo.getReferencia());
    			insumoFX.nombreCategoria.set(insumo.getNombreCategoria());
    			insumoFX.nombreSector.set(insumo.getNombreSector());
    			
    			obListPDPfiltradosPorSector.add(insumoFX);
			}
  			
//  			tablaInsumosGral.setItems(obListInsumosPorSector);
  			ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().setItems(obListPDPfiltradosPorSector);
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    private void filtrarTablaPorCategoria(Integer idCategoriaIN) {
  		try {
  			obListPDPfiltradosPorCategoria.clear();
  			
  			List<Insumo> listaInsumos = CRUD.obtenerListaInsumosStrockGralPdpPorIdCategoria(idCategoriaIN);
  			
  			for (Insumo insumo : listaInsumos) {
	    			final InsumoFX insumoFX = new InsumoFX();
	    			
	    			insumoFX.nombreInsumo.set(insumo.getNombreInsumo());
	    			insumoFX.unidadMedida.set(insumo.getUnidadMedida());
	    			Integer stockGral = (int) (long) insumo.getStockGeneral();
	    			insumoFX.stockGeneral.set(stockGral);
	    			insumoFX.pdp.set(insumo.getPdp());
	    			insumoFX.estadoInsumo.set(insumo.getEstadoInsumo());
	    			insumoFX.proveedor.set(insumo.getNombreProveedor());
	    			
	    			//Lo necesitamos a la hr de exportar a orden de compra
	    			insumoFX.articulo.set(insumo.getNroArticulo());
	    			insumoFX.referencia.set(insumo.getReferencia());
	    			insumoFX.nombreCategoria.set(insumo.getNombreCategoria());
	    			insumoFX.nombreSector.set(insumo.getNombreSector());
	    			
	    			obListPDPfiltradosPorCategoria.add(insumoFX);
				}
  			
//  			tablaInsumosGral.setItems(obListInsumosPorCategoria);
  			ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().setItems(obListPDPfiltradosPorCategoria);
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    /// para llamarlo desde el controlador ICsd_Principal
    public void limpiarFiltros2() {
    	obListPDPfiltradosPorCategoria.clear();
    	obListPDPfiltradosPorSector.clear();
    	
    	comboBoxCategoria.getSelectionModel().select(-1);
		comboBoxCategoria.getItems().clear();
		
		comboBoxSector.getSelectionModel().clearAndSelect(-1);
		
		comboBoxProveedor.getSelectionModel().clearAndSelect(-1);
		comboBoxProveedor.setDisable(false);
		
		textFieldBuscar.clear();
		
		this.getBtnAgregarAorden().setDisable(false);
		
    	ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().requestFocus();
    }



	public void addOblistAPDPGralYAux(ObservableList<InsumoFX> obListInsumoIN) {
		obListPDPGral.clear();
		obListPDPAux.clear();
		for (InsumoFX insumoFX : obListInsumoIN) {
			obListPDPGral.add(insumoFX);
			obListPDPAux.add(insumoFX);
		}
	}


	public void llenarComboProveedor() {
		try {
			comboBoxProveedor.getItems().clear();
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
			List<Proveedor> listaProveedores = CRUD.obtenerListaProveedoresActivos();
			
    		for (Proveedor proveedor : listaProveedores) {
    			itemsCombo.add(proveedor.getNombreProveedor());
			}
    		comboBoxProveedor.setItems(itemsCombo);
    		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	@FXML
    void filtroProveedor() {
		try {
			if (!comboBoxProveedor.getSelectionModel().isEmpty()) {
				
				Proveedor proveBD = CRUD.obtenerProveedorPorNombre(comboBoxProveedor.getSelectionModel().getSelectedItem());
				
				filtrarTablaPorProveedor(proveBD.getPkIdProveedor());
				
				idProveedorAux = proveBD.getPkIdProveedor(); //para usarlo luego en filtro sector y/o categoria
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }


	private void filtrarTablaPorProveedor(Integer idProveedorIN) {
		try {
			
			obListPDPAuxParaProvee.clear();
  			
  			List<Insumo> listaInsumos = CRUD.obtenerListaInsumosStrockGralPdpPorIdProveedor(idProveedorIN);
  			
  			for (Insumo insumo : listaInsumos) {
	    			final InsumoFX insumoFX = new InsumoFX();
	    			
	    			insumoFX.nombreInsumo.set(insumo.getNombreInsumo());
	    			insumoFX.unidadMedida.set(insumo.getUnidadMedida());
	    			Integer stockGral = (int) (long) insumo.getStockGeneral();
	    			insumoFX.stockGeneral.set(stockGral);
	    			insumoFX.pdp.set(insumo.getPdp());
	    			insumoFX.estadoInsumo.set(insumo.getEstadoInsumo());
	    			
	    			insumoFX.proveedor.set(insumo.getNombreProveedor());
	    			
	    			//Lo necesitamos a la hr de exportar a orden de compra
	    			insumoFX.articulo.set(insumo.getNroArticulo());
	    			insumoFX.referencia.set(insumo.getReferencia());
	    			insumoFX.nombreCategoria.set(insumo.getNombreCategoria());
	    			insumoFX.nombreSector.set(insumo.getNombreSector());
	    			
	    			obListPDPAuxParaProvee.add(insumoFX);
				}
			
			ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().setItems(obListPDPAuxParaProvee);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    @FXML
    void exportarAordenDeCompra() {
    	ControladorILogin.controllerPpal.mostrarOrdenDeCompra();
    	ControladorICsd_Principal.controllerIVistaOrdenDeCompra.desdePDP(true);
    	ControladorICsd_Principal.controllerIVistaOrdenDeCompra.cargarInsumosDesdePDPaOrden(manejadorObListAexportar().get(0).proveedor.get(),manejadorObListAexportar());

    }
    
    
    private ObservableList<InsumoFX> manejadorObListAexportar() {
		// si combo sector y categoria (ambos) estan sin seleccionar y combo proveedor, entonces se aplicara filtro sobre los insumos en gral
//		if ((comboBoxSector.getSelectionModel().getSelectedIndex() == -1) && 
//				(comboBoxCategoria.getSelectionModel().getSelectedIndex() == -1) &&
//				(comboBoxProveedor.getSelectionModel().getSelectedIndex() == -1)) { 
//			
//			return obListPDPAux;
//			
//		} else {
//			//si combo proveedor activado y combo sector desactivado, filtra sobre insumos del proveedor
//			if ((comboBoxProveedor.getSelectionModel().getSelectedIndex() >= 0) &&
//					(comboBoxSector.getSelectionModel().getSelectedIndex() == -1)) {
//				
//				return obListPDPAuxParaProvee;
//			
//			} else {
//				
//				//si se selecciono algun sector, pero no se selecciono ninguna categoria, el filtro se aplicara sobre los insumos de dicho sector
//				if ((comboBoxSector.getSelectionModel().getSelectedIndex() >= 0) &&
//						(comboBoxCategoria.getSelectionModel().getSelectedIndex() == -1)) {
//					
//					return obListPDPfiltradosPorSector;
//				
//				} else {
//					//significa q selecciono alguna categoria, entonces se aplica filtro a insumos x categoria
//					return obListPDPfiltradosPorCategoria;
//				
//				}
//				
//			}
//		}
    	
    	obListDetalleOC = ControladorICsd_Principal.controllerTablaPDPborrador.getObListDetalleOrdenDeCompra();
    	return obListDetalleOC;
    }

	
    @FXML
    void agregarAordenDeCompra() {
    	try {
    		final ObservableList<InsumoFX> insumosAagregarATablaSecundaria = ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().getItems();
        	if (!insumosAagregarATablaSecundaria.isEmpty()) {
        		for (int i = 0; i < insumosAagregarATablaSecundaria.size(); i++) {
            		
            		InsumoFX rowData = insumosAagregarATablaSecundaria.get(i);
            		ControladorICsd_Principal.controllerTablaPDPborrador.getObListDetalleOrdenDeCompra().add(rowData);
              		
        		}
        		ControladorICsd_Principal.controllerTablaPDPborrador.getDetalleOrdenDeCompra().setItems(ControladorICsd_Principal.controllerTablaPDPborrador.getObListDetalleOrdenDeCompra());
            	this.getBtnExportarOC().setDisable(false);
            	this.btnAgregarAorden.setDisable(true);
			}else {
				ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().requestFocus();
			}
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

    @FXML
    void limpiarTablaDetalle() {
    	ControladorICsd_Principal.controllerTablaPDPborrador.getObListDetalleOrdenDeCompra().clear();
    	this.getBtnExportarOC().setDisable(true);
    	btnAgregarAorden.setDisable(false);
    }
}
