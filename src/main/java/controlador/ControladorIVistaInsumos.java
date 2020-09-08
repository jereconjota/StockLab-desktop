package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import main.AppMain;
import modelo.Categoria;
import modelo.Sector;
import modeloAux.InsumoFX;


public class ControladorIVistaInsumos {

	@FXML
    private BorderPane borderPaneTablaInsumos;

    @FXML
    private AnchorPane anchorpaneCategorias;

    @FXML
    private JFXListView<String> listviewCategorias;

    @FXML
    private BorderPane borderpaneInsumos;

    @FXML
    private JFXTextField textField_Buscar;

    @FXML
    private Label labelVencidos;

    @FXML
    private ImageView logo;

    @FXML
    private JFXButton btnExportar;

    @FXML
    private JFXComboBox<String> comboboxSector;

    @FXML
    private JFXButton btnActualizar;
    
  //Lista para cargar categorias
    List<Categoria> listaCategoria;
    
    private ObservableList<String> itemsList = FXCollections.observableArrayList();
    
    private  ObservableList<InsumoFX> obListFiltrado = FXCollections.observableArrayList();
    
    private ObservableList<InsumoFX> obListInsumosImprimir = FXCollections.observableArrayList();
    
    private Sector sectorElegido;
    
    public static Integer flag;

    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorIVistaInsumos() {
    	
	}
    

    
    /**************************** GET - SET **********************************/
    
    public BorderPane getBorderPaneTablaInsumos() {
		return borderPaneTablaInsumos;
	}
	public void setBorderPaneTablaInsumos(BorderPane borderPaneTablaInsumos) {
		this.borderPaneTablaInsumos = borderPaneTablaInsumos;
	}
	
	
	public List<Categoria> getListaCategoria() {
		return listaCategoria;
	}
	public void setListaCategoria(List<Categoria> listaCategoria) {
		this.listaCategoria = listaCategoria;
	}


	public JFXComboBox<String> getComboboxSector() {
		return comboboxSector;
	}
	
	
	public Sector getSectorElegido() {
		return sectorElegido;
	}
	public void setSectorElegido(Sector sectorElegido) {
		this.sectorElegido = sectorElegido;
	}
	
	
	public Label getLabelVencidos() {
		return labelVencidos;
	}
	public void setLabelVencidos(Label labelVencidos) {
		this.labelVencidos = labelVencidos;
	}
	
	
	public JFXButton getBtnExportar() {
		return btnExportar;
	}
	public void setBtnExportar(JFXButton btnExportar) {
		this.btnExportar = btnExportar;
	}
	
	
	public JFXTextField getTextField_Buscar() {
		return textField_Buscar;
	}
	public void setTextField_Buscar(JFXTextField textField_Buscar) {
		this.textField_Buscar = textField_Buscar;
	}
	
	
    public JFXListView<String> getListviewCategorias() {
		return listviewCategorias;
	}
	public void setListviewCategorias(JFXListView<String> listviewCategorias) {
		this.listviewCategorias = listviewCategorias;
	}


	public ObservableList<String> getItemsList() {
		return itemsList;
	}
	public void setItemsList(ObservableList<String> itemsList) {
		this.itemsList = itemsList;
	}


	public JFXButton getBtnActualizar() {
		return btnActualizar;
	}

	
	public ObservableList<InsumoFX> getObListFiltrado() {
		return obListFiltrado;
	}
	public void setObListFiltrado(ObservableList<InsumoFX> obListFiltrado) {
		this.obListFiltrado = obListFiltrado;
	}

	
	public ObservableList<InsumoFX> getObListInsumosImprimir() {
		return obListInsumosImprimir;
	}
	public void setObListInsumosImprimir(ObservableList<InsumoFX> obListInsumosImprimir) {
		this.obListInsumosImprimir = obListInsumosImprimir;
	}



	/********************************** METODOS ***********************************/
	
	@FXML 
    void initialize() {
    	cargarLogo();
    	listviewCategoriasItemClick();
    }
	
	
	private void cargarLogo() {
		FileInputStream input;
		try {
			input = new FileInputStream("img/diagnosdahinten.png");
			Image image = new Image(input);
	    	logo.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	//METODO QUE DA ACCION AL CLICK DE CADA ITEM DEL ItemListCategorias
	@FXML
	public void listviewCategoriasItemClick() {
		listviewCategorias.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	
		    	/**este if else, es para evitar el bucle infinito q genera el manejo de la accion sobre un item
		    	 * de la lista de categorias...ya q "NEW VALUE" en cierto momento queda en null
		    	 */
		    	if (newValue != null) {
		    		ControladorICsd_Principal.controllerTablaDetalleInsumo.cargarInsumos(listviewCategorias.getSelectionModel().getSelectedItem());
				} else {
					
					ControladorICsd_Principal.controllerTablaDetalleInsumo.getObListInsumo().clear();
				}
		    	
		    }
		});
	}


	@FXML
    void filtrarPorCoincidencias() {
		this.setObListFiltrado(ControladorICsd_Principal.controllerTablaDetalleInsumo.getObListInsumo()); 
    	FilteredList<InsumoFX> filtrarDatos = new FilteredList<>(this.getObListFiltrado(), e -> true);
    	this.getTextField_Buscar().setOnKeyReleased(e ->{
    		this.getTextField_Buscar().textProperty().addListener((observableValue, oldValue, newValue) ->{
                filtrarDatos.setPredicate((java.util.function.Predicate<? super InsumoFX>) insumoFX->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    	
                    	if(insumoFX.nombreInsumo.get().contains(newValue)){
                            return true;
                        } else if (insumoFX.nombreInsumo.get().toLowerCase().contains(lowerCaseFilter)) {
                        	return true;
                        }
                    return false;
                });
            });
            SortedList<InsumoFX> sortedData = new SortedList<>(filtrarDatos);
            sortedData.comparatorProperty().bind(ControladorICsd_Principal.controllerTablaDetalleInsumo.getTablaInsumos().comparatorProperty());
            this.setObListFiltrado(sortedData);
            ControladorICsd_Principal.controllerTablaDetalleInsumo.getTablaInsumos().setItems(sortedData);
        });
    }

	
    @FXML
    void Exportar(ActionEvent event) throws FileNotFoundException, IOException {
    	try {
    		if (this.getTextField_Buscar().getText()!=null) {
	    		this.setObListInsumosImprimir(getObListFiltrado()); 
			} else {
		    	this.setObListInsumosImprimir(ControladorICsd_Principal.controllerTablaDetalleInsumo.getObListInsumo());
			}
	    	
	    	if (this.getObListInsumosImprimir().isEmpty()) {
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
		        String [] encabezadoInsumos = {"    NOMBRE INSUMO    ","NRO DE ARTICULO","  REFERNECIA  ","  NRO DE LOTE  ","STOCK ACTUAL",
		        		"STOCK GENERAL","PDP", "UNIDAD DE MEDIDA", "ULTIMA FECHA DE USO", "FECHA DE VENCIMIENTO"};
		        // Creamos la primer fila del excel, donde van los titulos
		        Row filas = sheet.createRow((short)0);
		        //integer auxiliar, lo usamos para contar las proximas filas
		        int rowNum = 1;
		        
		        ////////////MOVIMIENTOS - Llenamos la tabla
		        	for (int i = 0; i < encabezadoInsumos.length; i++) {
		        		//Creamos la celdas, asignamos ancho y centramos el contenido
		    			Cell celda = filas.createCell(i);
		    	        CellStyle cellStyle = wb.createCellStyle();
		    	        cellStyle.setAlignment(HorizontalAlignment.CENTER);
		    			celda.setCellStyle(cellStyle);
		    			celda.setCellValue(encabezadoInsumos[i]);
		    	        sheet.autoSizeColumn((short)i);
		            }
		        	
		        	//centramos el contenido de las celdas
        	        CellStyle cellStyle = wb.createCellStyle();
        	        cellStyle.setAlignment(HorizontalAlignment.CENTER);
		        	
		    		for (InsumoFX insumo : this.getObListInsumosImprimir()) {
		    			//Verificamos que es la tabla Movimientos y comenzamos a cargar datos en las celdas y filas
		    			filas = sheet.createRow(rowNum++);
		                for (int i = 0; i < encabezadoInsumos.length; i++) {
		                	Cell cell1 = filas.createCell(i);
			                	switch (i) {
			                	//Segun el titulo del encabezado, se llena la celda con lo que corresponda
									case 0:
					                    cell1.setCellValue(insumo.nombreInsumo.get());
										break;
									case 1:
					                    cell1.setCellValue(insumo.articulo.get());
					                    break;
									case 2:
					                    cell1.setCellValue(insumo.referencia.get());
					                    break;
									case 3:
										cell1.setCellValue(insumo.pkNroLote.get());
										break;
									case 4:
										cell1.setCellValue(insumo.stockActual.get());
										break;
									case 5:
										cell1.setCellValue(insumo.stockGeneral.get());
										break;
									case 6:
										cell1.setCellValue(insumo.pdp.get());
										break;
									case 7: 
										if (insumo.unidadMedida.get()!=null) {
											cell1.setCellValue(insumo.unidadMedida.get());
										}
										break;
									case 8: 
										if (insumo.fechaUso.get()!=null) {
											cell1.setCellValue(insumo.fechaUso.get().toString());
										}
										break;
									case 9:
										if (insumo.fechaVencimiento.get()!=null) {
											cell1.setCellValue(insumo.fechaVencimiento.get().toString());
										}
										break;
									default:
										break;
								}
//			            		//centramos el contenido de las celdas
//			        	        CellStyle cellStyle = wb.createCellStyle();
//			        	        cellStyle.setAlignment(HorizontalAlignment.CENTER);
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
		        	        
		        	        wb.close();			//////////////////////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!
		        	        
		        			} catch (IOException e) {
		        				e.printStackTrace();
		        				e.getMessage();
		        			} 
		        	}
		        	//posisionamos el foco en la tabla
		            ControladorICsd_Principal.controllerTablaDetalleInsumo.getTablaInsumos().requestFocus();    
			}
	    	
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }

    
  //Actualizamos los valores de insumos en la tabla
    @FXML
    void refreshInsumos(ActionEvent event) {
    	if (!ControladorICsd_Principal.controllerTablaDetalleInsumo.getTablaInsumos().getItems().isEmpty()) {
    		ControladorICsd_Principal.controllerTablaDetalleInsumo.cargarInsumos(getListviewCategorias().getSelectionModel().getSelectedItem());
    		ControladorICsd_Principal.controllerTablaDetalleInsumo.getTablaInsumos().requestFocus();
		}
    }
    
    
    public void setearVistaInsumos(String sectorDeUsuario) {
    	Transaction tx = null;
		try {
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			//obtener la tabla de sectores activos que estan en la BD
			Query query = appMain.getSession().createQuery("from Sector where Estado_Sector= \'Activo\'");
			List<Sector> listaSectores = query.list();
			
			appMain.getSession().close();
			
			for (Sector sect : listaSectores) {
				//Se cargan los sectores que esten activos
				if ( !(sect.getNombreSector().equals("Administracion"))) {
					itemsCombo.add(sect.getNombreSector());
				}
			}		
			
		    comboboxSector.setItems(itemsCombo);
		    
		    if (sectorDeUsuario.equals("") || sectorDeUsuario.equals("Administracion")) {
		    	
		    	flag = 1;
		    	
		    	comboboxSector.getSelectionModel().select(-1);
		    	listviewCategorias.getSelectionModel().clearSelection();
		    	itemsList.clear();
		    	listviewCategorias.setItems(itemsList);

		    }else{
		    	
		    	flag = 2;
		    	
		    	comboboxSector.setValue(sectorDeUsuario);
		    	setearListItemCategorias();
		    	listviewCategorias.getSelectionModel().clearSelection();
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
    
    
  //Recorremos las categorias, las que coincidan con el sector que esta en el combo, se setean en el list
    @FXML
	public void setearListItemCategorias(){
		Transaction tx = null;
		try {
			
			if (((flag == 1) || (flag == 2)) && (comboboxSector.getSelectionModel().getSelectedIndex() != -1)) {

				itemsList.clear();
				
				AppMain appMain = AppMain.getSingletonSession();
				tx = appMain.getSession().beginTransaction();
				
				Query query1 = appMain.getSession().createQuery("from Sector where Nombre_Sector = :nombreSec");
				query1.setParameter("nombreSec", this.getComboboxSector().getSelectionModel().getSelectedItem());
				
				Sector sector = (Sector) query1.uniqueResult();
				
				this.setSectorElegido(sector);
				
				Query query2 = appMain.getSession().createQuery("from Categoria where FK_Id_Sector= :id");
				query2.setLong("id", sector.getIdSector());
				listaCategoria = query2.list();
				
				appMain.getSession().close();
				
				for (Categoria categoria : listaCategoria) {
					//se carga a la lista las categorias correspondientes al sector seleccionado en el combo
					//y que esten activas  
					itemsList.add(categoria.getNombreCategoria());
				}
				
				listviewCategorias.setItems(itemsList);
				this.getLabelVencidos().setVisible(false);
			} 
			
		} catch (Exception e) {	
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}

	
	
}
