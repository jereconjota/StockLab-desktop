package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import main.AppMain;
import modelo.Categoria;
import modelo.Insumo;
import modelo.Sector;
import modeloAux.InsumoFX;

public class ControladorIVistaVencidos {
	
	@FXML
    private BorderPane borderTabla;

    @FXML
    private JFXButton btnExportarExcel;

    @FXML
    private JFXTextField textFieldBuscar;

    @FXML
    private BorderPane borderPaneVencidos;

    @FXML
    private JFXButton btnLimpiarFiltros;

    @FXML
    private ImageView logo;

    @FXML
    private JFXComboBox<String> comboBoxSector;
    
    private ObservableList<InsumoFX> obListVencidosfiltrados = FXCollections.observableArrayList();
	private ObservableList<InsumoFX> obListVencidosFiltradosPorSector = FXCollections.observableArrayList();
	private List<Categoria> listaCategorias;

	FilteredList<InsumoFX> filtrarDatos;
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorIVistaVencidos() {
    	
	}
    
    
    /**************************** GET - SET **********************************/
    
    
    public BorderPane getBorderTabla() {
		return borderTabla;
	}
	
	    
    public JFXComboBox<String> getComboBoxSector() {
		return comboBoxSector;
	}


	public JFXTextField getTextFieldBuscar() {
		return textFieldBuscar;
	}



	/********************************** METODOS ***********************************/
    
	
	@FXML
    void initialize() {
		cargarLogo();
		borderTabla.requestFocus();
    } 
	
	
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
	

    @FXML
    void filtroSector() {
    	obListVencidosFiltradosPorSector.clear();
    	Integer idCategoria=null;
    	try {
    		
    		if (this.getComboBoxSector().getSelectionModel().getSelectedIndex() != -1) {
    			
    			if (this.getComboBoxSector().getSelectionModel().getSelectedIndex() == 0) {
        			ControladorICsd_Principal.controllerTablaVencidos.getTablaVencimiento().setItems(ControladorICsd_Principal.controllerTablaVencidos.obListInsumo);
        		} else {
        			
        			Sector sectorElegido = CRUD.obtenerSectorPorNombre(this.getComboBoxSector().getSelectionModel().getSelectedItem());
        			
        			for (Categoria categoria : listaCategorias) {

        				if (categoria.getSector().getIdSector().equals(sectorElegido.getIdSector())) {
        					
        					idCategoria = categoria.getPkIdCategoria();
        					
        					List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorIdCategoria2(idCategoria);
        					
        					for (Insumo ins : listaInsumos){
        						if (ins.getFechaVencimiento() != null && ins.getStockActual() > 0 && ins.getCategoria().getPkIdCategoria() == idCategoria ) {
        							Date date = java.sql.Date.valueOf(LocalDate.now());
        							if (ins.getFechaVencimiento().before(date) || ins.getFechaVencimiento().equals(date)) {
        								final InsumoFX insumoFX = new InsumoFX(ins);
        								obListVencidosFiltradosPorSector.add(insumoFX);
        							}
        						}
        					}
        				}
        			}
        			ControladorICsd_Principal.controllerTablaVencidos.getTablaVencimiento().setItems(obListVencidosFiltradosPorSector);
        		}
        			
        		this.getTextFieldBuscar().clear();
    			
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
                    	
                	//filtro para busqueda "NOMBRE"					
                    	if(insumoFX.nombreInsumo.get().contains(newValue)){
                            return true;
                        } else if (insumoFX.nombreInsumo.get().toLowerCase().contains(lowerCaseFilter)) {
                        	return true;
                        }

                    return false;
                });
            });
            SortedList<InsumoFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(ControladorICsd_Principal.controllerTablaVencidos.getTablaVencimiento().comparatorProperty());
            obListVencidosfiltrados = sortedData;
            ControladorICsd_Principal.controllerTablaVencidos.getTablaVencimiento().setItems(sortedData);
            
        });
    }

	
    @FXML
    void manejadorObList() {
    	if (this.getComboBoxSector().getSelectionModel().getSelectedIndex() != 0) {
    		filtrarDatos = new FilteredList<>(obListVencidosFiltradosPorSector, e -> true);
		} else {
			filtrarDatos = new FilteredList<>(ControladorICsd_Principal.controllerTablaVencidos.obListInsumo, e -> true);
		}
    }

    
    @FXML
    void limpiarFiltros() {
    	comboBoxSector.getSelectionModel().select(0);
		this.textFieldBuscar.clear();
		ControladorICsd_Principal.controllerTablaVencidos.getTablaVencimiento().setItems(ControladorICsd_Principal.controllerTablaVencidos.obListInsumo);
		ControladorICsd_Principal.controllerTablaVencidos.getTablaVencimiento().requestFocus();
    }

    
    @FXML
    void exportarExcel(ActionEvent event) throws FileNotFoundException, IOException {
    	try {
    		
    		ObservableList<InsumoFX> obListVencidosimprimir;
        	if(this.getComboBoxSector().getSelectionModel().getSelectedIndex() == 0 && textFieldBuscar.getText().equals("")){
        		obListVencidosimprimir = ControladorICsd_Principal.controllerTablaVencidos.obListInsumo;
        		}else if (this.getComboBoxSector().getSelectionModel().getSelectedIndex() != 0 && textFieldBuscar.getText().equals("")) {
        			obListVencidosimprimir = obListVencidosFiltradosPorSector;
    	    			}else{
    	    	    		obListVencidosimprimir = obListVencidosfiltrados;
    					}
        	if(obListVencidosimprimir.isEmpty()){
        		Alert alert = new Alert(AlertType.WARNING);
    			alert.setTitle("ATENCION");
    			alert.setHeaderText("Tabla sin datos" );
    			alert.setContentText("No se puede exportar porque no hay datos en la tabla");
    			alert.showAndWait();
        	}else{
    	    	//Creamos libro de excel
    	    	Workbook wb = new HSSFWorkbook();  // or new XSSFWorkbook();
    	        Sheet sheet = wb.createSheet("Nueva Planilla");
    	        //Creamos los archivos
    	        FileOutputStream fileOut = null;
    	        File file = null;
    	        //creamos los string que van en la primer fila, los titulos de las columnas
    	        String [] encabezadoVencidos = {"				INSUMO				", "NRO DE LOTE", "FECHA DE VENCIMIENTO"};
    	        // Creamos la primer fila del excel, donde van los titulos
    	        Row filas = sheet.createRow((short)0);
    	        //integer auxiliar, lo usamos para contar las proximas filas
    	        int rowNum = 1;  		
    	    		
    	    	////////////VENCIDOS- Llenamos la tabla
    	        for (int i = 0; i < encabezadoVencidos.length; i++) {
    	        		//Creamos la celdas, asignamos ancho y centramos el contenido
    	    			Cell celda = filas.createCell(i);
    	    			CellStyle cellStyle = wb.createCellStyle();
//    	    	        cellStyle.setAlignment(cellStyle.ALIGN_CENTER);// !!! for poi 3.16
    	    			cellStyle.setAlignment(HorizontalAlignment.CENTER);	// for poi 3.17
    	    			celda.setCellStyle(cellStyle);
    	    			celda.setCellValue(encabezadoVencidos[i]);
    	    	        sheet.autoSizeColumn((short)i);
    	            }
    	        
	    	      //centramos el contenido de las celdas
	           	 CellStyle cellStyle = wb.createCellStyle();
	           	cellStyle.setAlignment(HorizontalAlignment.CENTER);
           	 
    	        	for (InsumoFX insumoVencido : obListVencidosimprimir) {
    	    			//Verificamos que es la tabla Vencidos y comenzamos a cargar datos en las celdas y filas
    	    			filas = sheet.createRow(rowNum++);
    	                for (int i = 0; i < encabezadoVencidos.length; i++) {
    	                	Cell cell1 = filas.createCell(i);
    		                	switch (i) {
    		                	//Segun el titulo del encabezado, se llena la celda con lo que corresponda
    								case 0:
    				                    cell1.setCellValue(insumoVencido.nombreInsumo.get());
    									break;
    								case 1:
    				                    cell1.setCellValue(insumoVencido.pkNroLote.get());
    				                    break; 
    								case 2:
    				                    cell1.setCellValue(insumoVencido.fechaVencimiento.get().toString());
    				                    break;
    								default:
    									break;
    							}
//    		             //centramos el contenido de las celdas
//    	            	 CellStyle cellStyle = wb.createCellStyle();
//    	        	     cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // !!!	for poi 3.16
//    	            	 cellStyle.setAlignment(HorizontalAlignment.CENTER);	// for poi 3.17
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
    			ControladorICsd_Principal.controllerTablaVencidos.getTablaVencimiento().requestFocus();
        	}
    		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    public void cargarSectoresYcategorias(){
		Transaction tx = null;
		try {
			ObservableList<String> itemsCombo = FXCollections.observableArrayList("SECTOR");
			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query1 = appMain.getSession().createQuery("from Sector where Estado_Sector= \'Activo\'");
			List<Sector> listaSectores = query1.list();
			
			for (Sector sect : listaSectores) {
    			if ( !(sect.getNombreSector().equals("Administracion"))) {
    				itemsCombo.add(sect.getNombreSector());
    			}
    		}		   
    		comboBoxSector.setItems(itemsCombo);
    		
    		Query query2 = appMain.getSession().createQuery("from Categoria");
			listaCategorias = query2.list();
    		
    		appMain.getSession().close();
    		
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}

}
