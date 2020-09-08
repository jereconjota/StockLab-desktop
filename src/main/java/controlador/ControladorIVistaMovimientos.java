package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import modeloAux.MovimientoFX;

public class ControladorIVistaMovimientos {
	
	@FXML
    private DatePicker fechaInicio;

    @FXML
    private BorderPane borderPane_paraTabla;

    @FXML
    private DatePicker fechaFinal;

    @FXML
    private JFXComboBox<String> comboboxFiltro;
    
    @FXML
    private JFXComboBox<String> comboboxSucursal;

	@FXML
    private JFXTextField textField_busqueda;
    
    @FXML
    private Label labelInicio;
    
    @FXML
    private Label labelFinal;
    
    @FXML
    private JFXButton btnFiltrar;

    @FXML
    private JFXButton btnLimpiarFiltros;
    
    @FXML
    private ImageView logo;
    
    @FXML
    private JFXButton btnExportar;
    
	private ObservableList<String> comboSucursales = FXCollections.observableArrayList("SUCURSAL","Diagnos","Cedig","Km3","Rada Tilly","Pediatrico");

	private ObservableList<String> comboFiltro = FXCollections.observableArrayList("INSUMO","USUARIO");

    private ObservableList<MovimientoFX> obListMovimientosSinFiltro = FXCollections.observableArrayList(); //aux
   
    private ObservableList<MovimientoFX> obListMovimientosFiltroPorNombreYusuario = FXCollections.observableArrayList(); //aux

    private ObservableList<MovimientoFX> obListMovimientosFiltroPorFecha = FXCollections.observableArrayList(); //aux
    
    private ObservableList<MovimientoFX> obListMovPorSucursal = FXCollections.observableArrayList(); //otro aux

    
    //////////////////////////////  CONSTRUCTOR  ////////////////////////////
    
    public ControladorIVistaMovimientos() {
    	
    }
    
    
    /////////////////////////// GET & SET ///////////////////////////////
    
    
    public BorderPane getBorderPane_paraTabla() {
 		return borderPane_paraTabla;
 	}
    
    
    public JFXComboBox<String> getComoboxFiltro() {
 		return comboboxFiltro;
 	}
    
    
    public JFXComboBox<String> getComboboxSucursal() {
		return comboboxSucursal;
	}
    
    
    public JFXTextField getTextField_busqueda() {
 		return textField_busqueda;
 	}
    
    
    public DatePicker getFechaInicio() {
 		return fechaInicio;
 	}
    
    
    public DatePicker getFechaFinal() {
 		return fechaFinal;
 	}
    
    
    public Label getLabelFinal() {
		return labelFinal;
	}
    
    
    public Label getLabelInicio() {
		return labelInicio;
	}
    
    
    public JFXButton getBtnFiltrar() {
		return btnFiltrar;
	}
    
    /////////////////////////// METODOS ///////////////////////////////
    
    @FXML
    void initialize() {
    	cargarLogo();
	    comboboxFiltro.setItems(comboFiltro);
	    comboboxSucursal.setItems(comboSucursales);
	    restringirFechas();
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
    
    
  //Evitamos que se asignen fechas mayores en lo calendarios
    private void restringirFechas() {
		try {
			final Callback<DatePicker, DateCell> dayCellFactory = 
		            new Callback<DatePicker, DateCell>() {
		                @Override
		                public DateCell call(final DatePicker datePicker) {
		                    return new DateCell() {
		                        @Override
		                        public void updateItem(LocalDate item, boolean empty) {
		                            super.updateItem(item, empty);
		                           
		                            if (item.isAfter(
		                            		LocalDate.now())
		                                ) {
		                                    setDisable(true);
		                                    setStyle("-fx-background-color: #ffc0cb;");
		                            }   
		                    }
		                };
		            }
		        };
		        
		        fechaInicio.setDayCellFactory(dayCellFactory);
		        fechaFinal.setDayCellFactory(dayCellFactory);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}  
	}
    

    @FXML
    void seleccionFiltro() {
  		try {
  			switch (comboboxFiltro.getSelectionModel().getSelectedIndex()) {
  			case 0: //INSUMO
  				this.getTextField_busqueda().setDisable(false);
  	    		this.getTextField_busqueda().setPromptText("Ingrese nombre de Insumo");
  	    		if (this.getComboboxSucursal().getSelectionModel().getSelectedIndex() != 0) {		//////////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  	    			limpiarSucursal();
  				}
  	    		this.getComboboxSucursal().setDisable(false);
  	    		this.getTextField_busqueda().clear();
  	    		break;
  			case 1: //USUARIO
  	    		this.getTextField_busqueda().setPromptText("Ingrese nombre de usuario");
  				this.getTextField_busqueda().setDisable(false);
  	    		if (this.getComboboxSucursal().getSelectionModel().getSelectedIndex() != 0) {		//////////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  	    			limpiarSucursal();
  				}
  	    		this.getComboboxSucursal().setDisable(false);
  	    		this.getTextField_busqueda().clear();
  	    		break;
  			default:
  				break;
  			}
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}		
    }

    
    @FXML
    void filtrarPorCoincidencias() {	
		obListMovimientosFiltroPorNombreYusuario = null;
    	obListMovimientosFiltroPorNombreYusuario = ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().getItems();
    	
    	FilteredList<MovimientoFX> filtrarDatos = new FilteredList<>(obListMovimientosFiltroPorNombreYusuario, e -> true);
		textField_busqueda.setOnKeyReleased(e ->{
			textField_busqueda.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filtrarDatos.setPredicate((java.util.function.Predicate<? super MovimientoFX>) MovimientoFX->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    	
                	//filtro para busqueda "INSUMO"
                    if (comboboxFiltro.getSelectionModel().getSelectedItem().equals("INSUMO")) {		//////////////////////////!!!!!!!!!!!!!!!!!!!!!
					
                    	if(MovimientoFX.nombreInsumo.get().contains(newValue)){
                            return true;
                        } else if (MovimientoFX.nombreInsumo.get().toLowerCase().contains(lowerCaseFilter)) {
                        	return true;
                        }
                    	
					} else { //filtra busqueda por "USUARIO" por apellido
					
						if(MovimientoFX.nombre.get().contains(newValue)){
	                        return true;
	                    } else if (MovimientoFX.nombre.get().toLowerCase().contains(lowerCaseFilter)) {
	                    	return true;
	                    }
					}
                    return false;
                });
            });
            SortedList<MovimientoFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().comparatorProperty());
            ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().setItems(sortedData);
            obListMovimientosFiltroPorNombreYusuario = sortedData;
			});
    }

    
    @FXML
    void verificaFechas() {
    	try {
    		//Si elegimos primero fecha fin y no elegimos fecha inicio, al dar click en "Filtrar" borra fecha fin
        	//obligando asi a siempre elegir fecha inicio primero
    		if (this.getFechaInicio().getValue() != null) {
        		getBtnFiltrar().setDisable(false);
    		}else{
        		this.getFechaFinal().setValue(null);
    		}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }

    
    @FXML
    void filtrarPorFecha() {
    	//Si la fecha fin es antes que la fecha inicio, es un error
    	if (!(this.getFechaFinal().getValue().isBefore(this.getFechaInicio().getValue()))){
    		labelInicio.setTextFill(Color.web("BLACK"));
    		labelFinal.setTextFill(Color.web("BLACK"));
    		Date inicio = java.sql.Date.valueOf(this.getFechaInicio().getValue());
    		Date fin = java.sql.Date.valueOf(this.getFechaFinal().getValue());
    	
    		ControladorICsd_Principal.controllerTablaMovimientos.botonBuscarMovimientos(inicio,fin);
    		obListMovimientosFiltroPorFecha.clear();

	    	for (MovimientoFX movFX : ControladorICsd_Principal.controllerTablaMovimientos.obListMovimiento) {
    			obListMovimientosFiltroPorFecha.add(movFX);
			}
	    	
	    	ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().setItems(obListMovimientosFiltroPorFecha);
	    	
	    	this.getTextField_busqueda().clear();
	    	this.getTextField_busqueda().setPromptText(null);
	    	this.getTextField_busqueda().setDisable(true);
				
//	    	this.getComboboxSucursal().getSelectionModel().select(0);
//	    	this.getComoboxFiltro().getSelectionModel().select(-1);
	    	this.getComboboxSucursal().getSelectionModel().clearAndSelect(0);
	    	this.getComoboxFiltro().getSelectionModel().clearAndSelect(-1);
			
			verificarDatosSobreTablaMovimiento();
	    	
    	
	    }else{//se borrar los datepicker y se colorean los label para indicar que se igreso erroneamente las fechas
	    	labelInicio.setTextFill(Color.web("RED"));
			labelFinal.setTextFill(Color.web("RED"));
			fechaInicio.setValue(null);
	        fechaFinal.setValue(null);
			getBtnFiltrar().setDisable(true);
			
	    	this.getComboboxSucursal().setDisable(true);
	    	this.getTextField_busqueda().setDisable(true);
	    	this.getComoboxFiltro().setDisable(true);
	    	
	    	this.getComboboxSucursal().getSelectionModel().select(0);
	    	this.getComoboxFiltro().getSelectionModel().select(-1);
	    	this.getTextField_busqueda().clear();
		}
    }

    
    @FXML
    void filtroSucursal() {
    	//Verificamos si esta filtrado por el combo (nombre, usuario o fecha), en el primer caso, no estaria filtrado
		//Trabaja con la tabla completa
		if (comboboxFiltro.getSelectionModel().getSelectedIndex() == -1) {
			if (comboboxSucursal.getSelectionModel().getSelectedIndex() == 0) { 
				ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().setItems(ControladorICsd_Principal.controllerTablaMovimientos.obListMovimiento);
			} else {
				String nombreSucursal = comboboxSucursal.getSelectionModel().getSelectedItem();
		    	obListMovimientosSinFiltro.clear();

		    	for (MovimientoFX movFX : ControladorICsd_Principal.controllerTablaMovimientos.obListMovimiento) {
					if (movFX.sucursal.get().equals(nombreSucursal)) {
						obListMovimientosSinFiltro.add(movFX);
					}
				}
		    	
		    	ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().setItems(obListMovimientosSinFiltro);
			}
			
		}else{	//sino, hay hecho un filtro y se debe filtrar la sucursal sobre la tabla ya filtrada
			
			if (comboboxSucursal.getSelectionModel().getSelectedIndex() == 0 && //Si no eligio sucursal y filtro por insumo o usuario
					comboboxFiltro.getSelectionModel().getSelectedIndex() != -1) {
				
				ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().setItems(obListMovimientosFiltroPorNombreYusuario);
				
				}else if (comboboxSucursal.getSelectionModel().getSelectedIndex() == 0 && //si no eligio sucursal y filtro por fecha
						comboboxFiltro.getSelectionModel().getSelectedIndex() == -1){
					
    				ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().setItems(obListMovimientosFiltroPorFecha);
    				
	    			}else{//filtro por usuario o insumo, y tambien por sucursal
	    				
	    				String nombreSucursal = comboboxSucursal.getSelectionModel().getSelectedItem();
	    				System.out.println(nombreSucursal);
	    		    	obListMovPorSucursal.clear();
	    		    	ObservableList<MovimientoFX> obListMovAux = FXCollections.observableArrayList();
	    		    	if (comboboxFiltro.getSelectionModel().getSelectedIndex() == -1){
	    		    		obListMovAux=obListMovimientosFiltroPorFecha;
	    		    	}else{
	    		    		obListMovAux=obListMovimientosFiltroPorNombreYusuario;
	    		    	}
	    		    	for (MovimientoFX movFX : obListMovAux) {
	    					if (movFX.sucursal.get().equals(nombreSucursal)) {
	    						obListMovPorSucursal.add(movFX);
	    					}
	    				}
	    		    	
	    		    	ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().setItems(obListMovPorSucursal);
	    			}
		}
    }

    
    @FXML
    void limpiarFiltros() {
    	textField_busqueda.clear();
    	textField_busqueda.setPromptText(null);
    	textField_busqueda.setDisable(true);
    	
    	fechaInicio.setValue(null);
        fechaFinal.setValue(null);
        
		this.getBtnFiltrar().setDisable(true);
		
		labelInicio.setTextFill(Color.web("BLACK"));
		labelFinal.setTextFill(Color.web("BLACK"));
		
//		comboboxSucursal.getSelectionModel().select(0);
//    	comboboxFiltro.getSelectionModel().select(-1);
		comboboxSucursal.getSelectionModel().clearAndSelect(0);
    	comboboxFiltro.getSelectionModel().clearAndSelect(-1);
		
    	limpiarObList();

    	LocalDate hoy = LocalDate.now();
		Date hoyDate = java.sql.Date.valueOf(hoy);
		LocalDate treintaDiasAntes = LocalDate.now().minusDays(30);
		Date treintaDiasAntesDate = java.sql.Date.valueOf(treintaDiasAntes);
		
		ControladorICsd_Principal.controllerTablaMovimientos.botonBuscarMovimientos(treintaDiasAntesDate, hoyDate);
		
		if (ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().getItems().isEmpty()) {
			this.getComboboxSucursal().setDisable(true);
			this.getComoboxFiltro().setDisable(true);
		} else {
			this.getComboboxSucursal().setDisable(false);
			this.getComoboxFiltro().setDisable(false);
		}
    
    	ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().requestFocus();
    }

    
    @FXML
    void Exportar(ActionEvent event) throws FileNotFoundException, IOException {
    	try {
    		ObservableList<MovimientoFX> obListMovimientoImprimir = ControladorICsd_Principal.controllerTablaMovimientos.obListMovimiento;
        	//Si no filtramos ni por usuario/inusmo/fecha ni por sucursal
        	if (comboboxFiltro.getSelectionModel().getSelectedIndex() == -1 && comboboxSucursal.getSelectionModel().getSelectedIndex() == 0) {
        		obListMovimientoImprimir = ControladorICsd_Principal.controllerTablaMovimientos.obListMovimiento;
        	//si filtramos usuario/inusmo/fecha y no por sucursal
    		}else if(comboboxFiltro.getSelectionModel().getSelectedIndex()!= -1 && comboboxSucursal.getSelectionModel().getSelectedIndex() == 0){
    			if (comboboxFiltro.getSelectionModel().getSelectedIndex() == 2){
    				obListMovimientoImprimir = obListMovimientosFiltroPorFecha;
    	    	}else{
    				obListMovimientoImprimir = obListMovimientosFiltroPorNombreYusuario;
    	    	}
    		//si filtramos por usuario/inusmo/fecha y tambien por sucursal
    		}else if(comboboxFiltro.getSelectionModel().getSelectedIndex() != -1 && comboboxSucursal.getSelectionModel().getSelectedIndex() != 0){
    			obListMovimientoImprimir = obListMovPorSucursal;
    		}else if (comboboxFiltro.getSelectionModel().getSelectedIndex() == -1 && comboboxSucursal.getSelectionModel().getSelectedIndex() != 0) {
    			obListMovimientoImprimir = obListMovimientosSinFiltro;
    		}
        	if (obListMovimientoImprimir.isEmpty()) {
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
    	        String [] encabezadoMovimientos = {"   USUARIO   ","  FECHA Y HORA  ","   NRO DE LOTE   ",
    	        		"         INSUMO        ", "UNIDADES", "          DESCRIPCION      ","SUCURSAL"};
    	        // Creamos la primer fila del excel, donde van los titulos
    	        Row filas = sheet.createRow((short)0);
    	        //integer auxiliar, lo usamos para contar las proximas filas
    	        int rowNum = 1;
    	        
    	        ////////////MOVIMIENTOS - Llenamos la tabla
    	        	for (int i = 0; i < encabezadoMovimientos.length; i++) {
    	        		//Creamos la celdas, asignamos ancho y centramos el contenido
    	    			Cell celda = filas.createCell(i);
    	    	        CellStyle cellStyle = wb.createCellStyle();
    	    	        cellStyle.setAlignment(HorizontalAlignment.CENTER);
    	    			celda.setCellStyle(cellStyle);
    	    			celda.setCellValue(encabezadoMovimientos[i]);
    	    	        sheet.autoSizeColumn((short)i);
    	            }
    	        	
    	        	/** agregamos estas lineas fuera del for de abajo, para q no haya limite con respecto a la cantidad de registros q pueda tener el excel **/
    	        	CellStyle cellStyle = wb.createCellStyle();
        	        cellStyle.setAlignment(HorizontalAlignment.CENTER);
    	        	
    	    		for (MovimientoFX movimiento : obListMovimientoImprimir) {
    	    			//Verificamos que es la tabla Movimientos y comenzamos a cargar datos en las celdas y filas
    	    			filas = sheet.createRow(rowNum++);
    	                for (int i = 0; i < encabezadoMovimientos.length; i++) {
    	                	Cell cell1 = filas.createCell(i);
    		                	switch (i) {
    		                	//Segun el titulo del encabezado, se llena la celda con lo que corresponda
    								case 0:
    				                    cell1.setCellValue(movimiento.nombre.get());
    				                    break;
    								case 1:
    									cell1.setCellValue(movimiento.fechaMovimiento.get().toString());
    									break;
    								case 2:
    									cell1.setCellValue(movimiento.insumo.get());
    									break;
    								case 3:
    									cell1.setCellValue(movimiento.nombreInsumo.get());
    									break;
    								case 4:
    									if (movimiento.datos.get().indexOf("decrementadas")!=-1) {
    										cell1.setCellValue(String.valueOf(movimiento.datos.get().substring(23, 25)));
										}else {
											cell1.setCellValue(String.valueOf(movimiento.datos.get().substring(20, 22)));
										}
    									break;
    								case 5:
    									if (movimiento.datos.get().indexOf("decrementadas")!=-1) {
    										cell1.setCellValue(movimiento.datos.get().substring(0, 22));
										}else {
											cell1.setCellValue(movimiento.datos.get().substring(0, 19));
										}
    		    	                	break;
    								case 6:
    									cell1.setCellValue(movimiento.sucursal.get());
    									break;
    								default:
    									break;
    							}
    		            		//centramos el contenido de las celdas
//    		        	        CellStyle cellStyle = wb.createCellStyle();				/// estas lineas si se dejan aca, la tabla excel va a tener un limite de 4000registros
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
    	        			} 
    	        	}
    	        	//posisionamos el foco en la tabla
    	            ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().requestFocus();    
    		}
        	
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }

    
    //Si filtro por sucursal, y despues quiere filtrar por nombre, primero elimina el filtro por sucursal
    private void limpiarSucursal(){
//		this.comboboxSucursal.getSelectionModel().select(0);
    	this.comboboxSucursal.getSelectionModel().clearAndSelect(0);
		ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().setItems(ControladorICsd_Principal.controllerTablaMovimientos.obListMovimiento);;
    }


    /** verifica si la tabla tiene datos
     * 		SI TIENE ---> habilita los filtros
     * 		SINO TIENE ---> no habilita los filtros
     */
	public void verificarDatosSobreTablaMovimiento() {
		try {
			
			if (ControladorICsd_Principal.controllerTablaMovimientos.getTablaMovimientos().getItems().isEmpty()) {
				this.getComboboxSucursal().setDisable(true);
				this.getComoboxFiltro().setDisable(true);
			} else {
				this.getComoboxFiltro().setDisable(false);
				this.getComboboxSucursal().setDisable(false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	
	public void limpiarObList() {
		ControladorICsd_Principal.controllerTablaMovimientos.obListMovimiento.clear();
    	this.obListMovimientosFiltroPorFecha.clear();
		this.obListMovimientosFiltroPorNombreYusuario.clear();				//////////////////////////!!!!!!!!!!!!!!!!!!!!!
    	this.obListMovimientosSinFiltro.clear();
    	this.obListMovPorSucursal.clear();

	}
	
	
}
