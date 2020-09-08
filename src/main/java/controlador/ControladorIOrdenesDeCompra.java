package controlador;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import hibernate.util.CRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.converter.DateStringConverter;
import modelo.DetalleFactura;
import modelo.DetalleOrdenDeCompra;
import modelo.Factura;
import modelo.Insumo;
import modelo.OrdenDeCompra;
import modelo.Proveedor;
import modeloAux.DetalleFacturaFX;
import modeloAux.DetalleOrdenDeCompraFX;
import modeloAux.FacturaFX;
import modeloAux.InsumoFX;
import modeloAux.OrdenDeCompraFX;

public class ControladorIOrdenesDeCompra {
	
	@FXML
    private TableView<OrdenDeCompraFX> tablaOrdenDeCompra;
    
    @FXML
    private TableColumn<OrdenDeCompraFX, Integer> tablaOrden_NroOrden;
    
    @FXML
    private TableColumn<OrdenDeCompraFX, String> tablaOrden_Usuario;
    
    @FXML
    private TableColumn<OrdenDeCompraFX, String> tablaOrden_Proveedor;
    
    @FXML
    private TableColumn<OrdenDeCompraFX, Date> tablaOrden_FechaOrden;
	
	@FXML
    private TableView<DetalleOrdenDeCompraFX> tablaDetalle;
	
	@FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> tablaDetalle_Insumo;
	
	@FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> tablaDetalle_Articulo;

    @FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> tablaDetalle_Referencia;
	
	@FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> tablaDetalle_Sector;
	
	@FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> tablaDetalle_Categoria;
	
	@FXML
    private TableColumn<DetalleOrdenDeCompraFX, Integer> tablaDetalle_Cantidad;
	
	@FXML
    private TableColumn<DetalleOrdenDeCompraFX, Integer> tablaDetalle_Unidades;
	
	@FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> tablaDetalle_Observacion;
	
	@FXML
    private JFXTextArea textAreaObservacion;    

    @FXML
    private JFXComboBox<String> comboProveedor;

    @FXML
    private JFXTextField textField_busqueda;
    
    @FXML
    private JFXButton btnExportar;
    
    @FXML
    private JFXButton btnLimpiarFiltros;
    
    
    private ObservableList<OrdenDeCompraFX> obListOrdenesDeCompra = FXCollections.observableArrayList();
    private ObservableList<OrdenDeCompraFX> obListOrdenesDeCompraAux = FXCollections.observableArrayList();	// (se le aplica clear)
    
    private ObservableList<OrdenDeCompraFX> obListAuxParaProvee = FXCollections.observableArrayList(); // (se le aplica clear)
    
    private ObservableList<DetalleOrdenDeCompraFX> obListDetalleOrdenes = FXCollections.observableArrayList();	// (se le aplica clear)
    
    private ObservableList<DetalleOrdenDeCompraFX> obListImprimirOC = FXCollections.observableArrayList();
    
    private List<Proveedor> listaProveedores;
    
    
    /**************************** CONSTRUCTOR *********************************/
   
    public ControladorIOrdenesDeCompra() {
	
    }

    /**************************** GET - SET **********************************/
    
    public TableColumn<DetalleOrdenDeCompraFX, Integer> getTablaDetalle_Unidades() {
		return tablaDetalle_Unidades;
	}

	public void setTablaDetalle_Unidades(TableColumn<DetalleOrdenDeCompraFX, Integer> tablaDetalle_Unidades) {
		this.tablaDetalle_Unidades = tablaDetalle_Unidades;
	}
    
    
    
    /********************************** METODOS ***********************************/

    @FXML
    private void initialize() {
    	definirTipoDatoColumnasOrdenes();
    	definirTipoDatoColumnasDetalle();
    	seleccionarFila();
    }
    

	@FXML
    void exportarOrdenes() {
		try {
			SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
			
			//BASICAMENTE TOMA UN EXCEL CON EL FORMATO DE ORDEN DE COMPRA, LO EDITA Y LO GUARDA EN UN NUEVO EXCEL
		 
			FileInputStream file;
    		if (tablaOrdenDeCompra.getSelectionModel().getSelectedItem().nombreProveedor.get().equals("SISTEMAS ANALITICOS")) {
				file = new FileInputStream(new File("ordenesDeCompra/Orden de Compra Sistemas Analiticos.xls"));
			} else {
				file = new FileInputStream(new File("ordenesDeCompra/Orden de Compra General.xls"));
			}
    		
    		//HSSFWorkbook crea un libro .xls, XSSFWorkbook crea .xlsx
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			
			// Getting the Sheet at index zero
	        Sheet sheet = workbook.getSheetAt(0);
	        
	        //Creamos el encabezado de cada orden y la celda del nombre de proveedor
	        ArrayList<String> encabezadoInsumosOrdenDeCompra = new ArrayList<String>();
	        Cell nombreProveedor;
	        Cell nombreUsuario;
	        Cell fechaCargaOrdenDeCompra;
	        Cell nroOrdenDeCompra;
	        Cell mail;
	        
	        if (tablaOrdenDeCompra.getSelectionModel().getSelectedItem().nombreProveedor.get().equals("SISTEMAS ANALITICOS")) {
	        	encabezadoInsumosOrdenDeCompra.add("   Cod   ");
	        	encabezadoInsumosOrdenDeCompra.add("              DESCRIPCION              ");
	        	encabezadoInsumosOrdenDeCompra.add("   REF ART   ");
	        	encabezadoInsumosOrdenDeCompra.add("  PRECIO  ");
	        	encabezadoInsumosOrdenDeCompra.add("CANTIDAD");
	        	encabezadoInsumosOrdenDeCompra.add("    OBSERVACION   ");
	        	nombreProveedor = sheet.getRow(7).getCell(1);
	        	nombreUsuario = sheet.getRow(4).getCell(4);
	        	fechaCargaOrdenDeCompra = sheet.getRow(1).getCell(5);
	        	
	        	nroOrdenDeCompra = sheet.getRow(5).getCell(5);
	        	mail =  sheet.getRow(4).getCell(1);
			} else {
				encabezadoInsumosOrdenDeCompra.add("   Cod   ");
	        	encabezadoInsumosOrdenDeCompra.add("CANTIDAD");
	        	encabezadoInsumosOrdenDeCompra.add("   Unid   ");
	        	encabezadoInsumosOrdenDeCompra.add("              DESCRIPCION              ");
	        	encabezadoInsumosOrdenDeCompra.add("    Control Int   ");
	        	nombreProveedor = sheet.getRow(6).getCell(1);
	        	nombreUsuario = sheet.getRow(4).getCell(4);
	        	fechaCargaOrdenDeCompra = sheet.getRow(1).getCell(5);
	        	
	        	nroOrdenDeCompra = sheet.getRow(5).getCell(5);
	        	mail =  sheet.getRow(4).getCell(1);
			}
	        
	        //Seteamos nombre de proveedor en la orden de compra
   			nombreProveedor.setCellValue(tablaOrdenDeCompra.getSelectionModel().getSelectedItem().nombreProveedor.get());
   			//Seteamos nombre del usuario que esta creando la orden
   			nombreUsuario.setCellValue(ControladorICsd_Principal.getUser().getNombre()+" "+ControladorICsd_Principal.getUser().getApellido());
   			//Seteamos fecha de creacion de la orden de compra
   			String fecha = formato.format(tablaOrdenDeCompra.getSelectionModel().getSelectedItem().fechaOrdenCompra.get());
   			fechaCargaOrdenDeCompra.setCellValue(fecha);
   			//Seteamos numero de la orden de compra
   			nroOrdenDeCompra.setCellValue(tablaOrdenDeCompra.getSelectionModel().getSelectedItem().nroOrdenCompra.get());
   			//Seteamos mail
		    mail.setCellValue(ControladorICsd_Principal.getUser().getMail());
		    
		    //Se comienza a cargar insumos a partir de la fila 11
	        Integer insumosApartirDeFila = 11;
	        
	        obListImprimirOC = obListDetalleOrdenes;
	        
	        Row filas;
	        //integer auxiliar, lo usamos para contar las proximas filas
	        int rowNum = insumosApartirDeFila++;
	        
	        //Creamos los objetos de archivos a guardar
	        FileOutputStream fileOut = null;
	        File newfile = null;
	        
	        //Creamos tanta cantidad de filas como insumos haya en la lista de Orden de Compra
	        int totalRows = sheet.getLastRowNum();
			sheet.shiftRows(rowNum, totalRows, obListImprimirOC.size());
			
			//Creamos el estilo para darle formato a las celdas
   	        CellStyle style = workbook.createCellStyle();
   	        CellStyle style2 = workbook.createCellStyle();
   	        CellStyle style3 = workbook.createCellStyle();
   	        //centramos el contenido de las celdas
   	        style.setAlignment(HorizontalAlignment.CENTER);
   	        style2.setAlignment(HorizontalAlignment.CENTER);
   	        style3.setAlignment(HorizontalAlignment.CENTER);
		    //pintamos bordes
   	        style = allBorderSet(style);
   	        
	   	     for (DetalleOrdenDeCompraFX docFX : obListImprimirOC) {
	   	    	
	   	    	Cell insumosEnOrden = null;
	        	filas = sheet.createRow(rowNum++);
    			
    			//Si es con Sistemas Analiticos, se utiliza la orden de compra personalizada
    			if (tablaOrdenDeCompra.getSelectionModel().getSelectedItem().nombreProveedor.get().equals("SISTEMAS ANALITICOS")) {
    			
    				for (int i = 0; i < encabezadoInsumosOrdenDeCompra.size(); i++) {
    					insumosEnOrden = filas.createCell(i);
	                	switch (i) {
	                	//Segun el titulo del encabezado, se llena la celda con lo que corresponda
							case 0:
			                    insumosEnOrden.setCellValue(docFX.articulo.get());
								break;
							case 1:
			                    insumosEnOrden.setCellValue(docFX.nombreInsumo.get());
			                    break;
							case 2:
			                    insumosEnOrden.setCellValue(docFX.referencia.get());
			                    break;
							case 3:
								insumosEnOrden.setCellValue("");
								break;
							case 4:
								insumosEnOrden.setCellValue(docFX.cantidad.get());
								break;
							case 5:
								insumosEnOrden.setCellValue("");
								break;
							default:
								break;
							}
	                	insumosEnOrden.setCellStyle(style);
    				}
    				//Si con cualquier otro proveedor, se utiliza la orden de compra general
                	//La orden de compra general tuvimos que setearle el estilo a cada celda especifica porque no habia forma
                	//que no se rompa todo apache poi y excel lcdsm
    			} else {
    				
    				for (int i = 0; i < encabezadoInsumosOrdenDeCompra.size(); i++) {
    					insumosEnOrden = filas.createCell(i);
	                	switch (i) {
	                	//Segun el titulo del encabezado, se llena la celda con lo que corresponda
							case 0:
			                    insumosEnOrden.setCellValue(docFX.articulo.get());
			                    insumosEnOrden.setCellStyle(style);
								break;
							case 1:
			                    insumosEnOrden.setCellValue(docFX.cantidad.get());
			                    insumosEnOrden.setCellStyle(style);
			                    break;
							case 2:
								if (docFX.unidades.get() != null) {
									insumosEnOrden.setCellValue(docFX.unidades.get());
								} else {
									insumosEnOrden.setCellValue(0);
								}
			                    insumosEnOrden.setCellStyle(style);
			                    break;
							case 3:
			                    insumosEnOrden.setCellValue(docFX.nombreInsumo.get());
			                    insumosEnOrden.setCellStyle(style);
								break;
							case 4:
								insumosEnOrden = filas.createCell(4);
								style3 = bottomBorderSet(style3);
								insumosEnOrden.setCellStyle(style3);
								
								insumosEnOrden = filas.createCell(5);
								insumosEnOrden.setCellStyle(style3);
								
								insumosEnOrden = filas.createCell(6);
								style2 = rigthBorderSet(style2);
								insumosEnOrden.setCellStyle(style2);
								insumosEnOrden.setCellValue("");
								break;
							default:
								break;
							}
    				}
    			}
	   	     }
	   	     
	   	    //damos a elegir donde guardar el archivo exportado
	    	FileChooser fileChooser = new FileChooser();
	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel files (*.xls)", "*.xls");
	        fileChooser.getExtensionFilters().add(extFilter);
	        
	        newfile = new File("");
	        newfile = fileChooser.showSaveDialog(ControladorILogin.controllerPpal.getPrimaryStage());
	        
	        if(newfile!=null){
	    		try {
	    			fileOut = new FileOutputStream(newfile);
	    			workbook.write(fileOut);
	    			fileOut.flush();
	    	        fileOut.close();
	    	        
	    	        // Guardamos la orden y el detalle de la orden en la base de datos
//				 	guardarOrdenDeCompra();
				 	// Confirmamos y posisionamos el foco en la tabla
			    	mostrarMsjCreadoExitoso();
			    	// Closing the workbook
			        workbook.close();
			        limpiarFiltros();
			    	// Abrimos archivo en excel o programa predeterminado
			    	abrirOrdenCreada(newfile);
	    		} catch (IOException e) {
    				e.printStackTrace();
    			} 
	    	}
			
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }
	
	
	private CellStyle allBorderSet(CellStyle stl) {
	 	stl.setBorderBottom(BorderStyle.MEDIUM);
        stl.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        stl.setBorderLeft(BorderStyle.MEDIUM);
        stl.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        stl.setBorderRight(BorderStyle.MEDIUM);
        stl.setRightBorderColor(IndexedColors.BLACK.getIndex());
        stl.setBorderTop(BorderStyle.MEDIUM);
        stl.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return stl;
	}
	
	
	private CellStyle rigthBorderSet(CellStyle stl) {
	 	stl.setBorderBottom(BorderStyle.MEDIUM);
        stl.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        stl.setBorderLeft(BorderStyle.NONE);
//        stl.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        stl.setBorderRight(BorderStyle.MEDIUM);
        stl.setRightBorderColor(IndexedColors.BLACK.getIndex());
        stl.setBorderTop(BorderStyle.NONE);
//        stl.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return stl;
	}
	
	
	private CellStyle bottomBorderSet(CellStyle stl) {
	 	stl.setBorderBottom(BorderStyle.MEDIUM);
        stl.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        stl.setBorderLeft(BorderStyle.NONE);
//        stl.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        stl.setBorderRight(BorderStyle.NONE);
//        stl.setRightBorderColor(IndexedColors.BLACK.getIndex());
        stl.setBorderTop(BorderStyle.NONE);
//        stl.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return stl;
	}
	
	
	private void mostrarMsjCreadoExitoso() {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
    	alert.setHeaderText("Orden de compra exportada");
		alert.setContentText("La orden de compra Nro: "+ tablaOrdenDeCompra.getSelectionModel().getSelectedItem().nroOrdenCompra.get() +" ha sido exportada satisfactoriamente");
		alert.showAndWait();
	}
	
	
	private void abrirOrdenCreada(File archivo) {	 
		//ruta del archivo en el pc
		String file = new String(archivo.getAbsolutePath()); 
		try{ 
		   File path = new File (file);
		   Desktop.getDesktop().open(path);
		  
		  }catch(IOException e){
		     e.printStackTrace();
		  }catch(IllegalArgumentException e){
		     JOptionPane.showMessageDialog(null, "No se pudo encontrar el archivo","Error",JOptionPane.ERROR_MESSAGE);
		     e.printStackTrace();
		 }  
	}
    
    
    @FXML
    void filtroProveedor() {
    	try {
			if (!comboProveedor.getSelectionModel().isEmpty()) {
				
				Proveedor proveBD = CRUD.obtenerProveedorPorNombre(comboProveedor.getSelectionModel().getSelectedItem());
				
				filtrarTablaPorProveedor(proveBD.getPkIdProveedor());
				
//				idProveedorAux = proveBD.getPkIdProveedor(); //para usarlo luego en filtro sector y/o categoria	
				
				if (!tablaDetalle.getItems().isEmpty()) {
					limpiarTablaDetalle();
					limpiarTxtAreaObservacion();
				}
				
				btnExportar.setDisable(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    @FXML
    void limpiarFiltros() {
    	limpiarTxtAreaObservacion();
    	
    	obListAuxParaProvee.clear();
    	
		comboProveedor.getSelectionModel().clearAndSelect(-1);
		comboProveedor.setDisable(false);
		
		limpiarTablaDetalle();
		
		obListOrdenesDeCompraAux.clear();
		
		for (OrdenDeCompraFX ordenCompraFX : obListOrdenesDeCompra) {
			obListOrdenesDeCompraAux.add(ordenCompraFX);
		}
		
		tablaOrdenDeCompra.setItems(obListOrdenesDeCompraAux);
		setearOrdenDatosTablaOrdenes();
		
		btnExportar.setDisable(true);
		
		obListImprimirOC.clear();
    }

    
    private void filtrarTablaPorProveedor(Integer idProveedorIN) {
		try {
			
			obListAuxParaProvee.clear();
			
			SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
  			
  			List<OrdenDeCompra> listaOrdenesCompra = CRUD.obtenerListaOrdenDeCompraPorIdProveedor(idProveedorIN);
  			
  			for (OrdenDeCompra ordenCompra : listaOrdenesCompra) {
	    			final OrdenDeCompraFX ordenCompraFX = new OrdenDeCompraFX(ordenCompra);
	    			
	    			String fecha = formato.format(ordenCompra.getFechaOrdenCompra());
					Date fechaOrden = formato.parse(fecha);
					ordenCompraFX.fechaOrdenCompra.set(fechaOrden);
	    			
	    			obListAuxParaProvee.add(ordenCompraFX);
				}
			
			tablaOrdenDeCompra.setItems(obListAuxParaProvee);
			
			setearOrdenDatosTablaOrdenes();
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    private void definirTipoDatoColumnasOrdenes() {
    	DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
    	tablaOrden_NroOrden.setCellValueFactory(cellData -> cellData.getValue().nroOrdenCompra);
    	tablaOrden_Usuario.setCellValueFactory(cellData -> cellData.getValue().nombreUsuario);
    	tablaOrden_Proveedor.setCellValueFactory(cellData -> cellData.getValue().nombreProveedor);
    	tablaOrden_FechaOrden.setCellValueFactory(cellData -> cellData.getValue().fechaOrdenCompra);
    	tablaOrden_FechaOrden.setCellFactory(TextFieldTableCell.<OrdenDeCompraFX, Date>forTableColumn(convertirDaS));
		alinearContenidoColumnasOrdenes();
	}
    
    
	private void alinearContenidoColumnasOrdenes() {
		tablaOrden_NroOrden.setStyle("-fx-alignment: CENTER;");
		tablaOrden_Usuario.setStyle("-fx-alignment: CENTER;");
		tablaOrden_Proveedor.setStyle("-fx-alignment: CENTER;");
		tablaOrden_FechaOrden.setStyle("-fx-alignment: CENTER;");
	}

	
	private void definirTipoDatoColumnasDetalle() {
		tablaDetalle_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaDetalle_Articulo.setCellValueFactory(cellData -> cellData.getValue().articulo);
		tablaDetalle_Referencia.setCellValueFactory(cellData -> cellData.getValue().referencia);
		tablaDetalle_Sector.setCellValueFactory(cellData -> cellData.getValue().nombreSector);
		tablaDetalle_Categoria.setCellValueFactory(cellData -> cellData.getValue().nombreCategoria);
		tablaDetalle_Cantidad.setCellValueFactory(cellData -> cellData.getValue().cantidad);
		tablaDetalle_Unidades.setCellValueFactory(cellData -> cellData.getValue().unidades);
		tablaDetalle_Observacion.setCellValueFactory(cellData -> cellData.getValue().observacion);
		alinearContenidoColumnasDetalle();
	}

	
	private void alinearContenidoColumnasDetalle() {
		tablaDetalle_Insumo.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Articulo.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Referencia.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Sector.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Categoria.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Cantidad.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Unidades.setStyle("-fx-alignment: CENTER;");
		tablaDetalle_Observacion.setStyle("-fx-alignment: CENTER;");
	}

	
	private void seleccionarFila() {
		//se le asigna una accion al click de cada fila de la tabla factura
		tablaOrdenDeCompra.setRowFactory( tv -> {
		    TableRow<OrdenDeCompraFX> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getButton() == MouseButton.PRIMARY){
		        	if (tablaOrdenDeCompra.getSelectionModel().isSelected(row.getIndex(), tablaOrden_NroOrden)) {
		        		OrdenDeCompraFX rowData = row.getItem();
		        		
		        		llenarDetalleOrdenCompra(rowData);
		        		completarObservacionGral(rowData);
		        		habilitarColUnidades(rowData);
		        		btnExportar.setDisable(false);

//		        		this.getBtnModificar().setDisable(false);
					}else{
		        		tablaOrdenDeCompra.getSelectionModel().clearSelection();
//		        		this.getBtnModificar().setDisable(true);
					}
		        }
		    });
		    return row ;
		});
	}
	

	private void llenarDetalleOrdenCompra(OrdenDeCompraFX rowData) {
		try {
			//antes q nada limpio la tabla detalle
			limpiarTablaDetalle();
			
			List<DetalleOrdenDeCompra> listaDetalles = CRUD.obtenerDetalleOrdenDeCompraPorIdOrden(tablaOrdenDeCompra.getSelectionModel().getSelectedItem().pkIdOrdenCompra.get());
			
			for (DetalleOrdenDeCompra detalleOrdenCompra : listaDetalles) {
		
				final DetalleOrdenDeCompraFX detalleFX = new DetalleOrdenDeCompraFX(detalleOrdenCompra);
				
				obListDetalleOrdenes.add(detalleFX);
			}
			
			tablaDetalle.setItems(obListDetalleOrdenes);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void habilitarColUnidades(OrdenDeCompraFX ordenCompraFxIN) {
		if (ordenCompraFxIN.nombreProveedor.get().equals("SISTEMAS ANALITICOS")) {
			tablaDetalle_Unidades.setVisible(false);
		} else {
			tablaDetalle_Unidades.setVisible(true);
		}
	}
	
	
	private void completarObservacionGral(OrdenDeCompraFX ordenCompraFxIN) {
		textAreaObservacion.clear();
		textAreaObservacion.setText(ordenCompraFxIN.observacion.get());
	}
	

	public void limpiarSeleccionEnTablaOrdenes() {
		tablaOrdenDeCompra.getSelectionModel().clearSelection();
	}

	
	public void limpiarTablaDetalle() {
		tablaDetalle.getSelectionModel().clearSelection();
		obListDetalleOrdenes.clear();
	}

	
	public void limpiarTablaOrdenes() {
		obListOrdenesDeCompra.clear();
	}

	
	public void ocultarColDetalleUnidades() {
		tablaDetalle_Unidades.setVisible(false);
	}

	
	public void desactivarBtnExportar() {
		btnExportar.setDisable(true);
	}
	
	
	public void limpiarTxtAreaObservacion() {
		textAreaObservacion.clear();
	}

	
	public void limpiarComboYtxtField() {
		comboProveedor.getSelectionModel().select(-1);
		textField_busqueda.clear();
	}

	
	public void cargarOrdenesDeCompra() {
		try {			
			
			obListOrdenesDeCompraAux.clear();
			
			SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
			
			List<OrdenDeCompra> listaOrdenes = CRUD.obtenerListaOrdenesDeCompra2();
					
    		for (OrdenDeCompra ordenCompra : listaOrdenes) {
    			
    			final OrdenDeCompraFX ordenCompraFX = new OrdenDeCompraFX(ordenCompra);
    			
				String fecha = formato.format(ordenCompra.getFechaOrdenCompra());
				Date fechaOrden = formato.parse(fecha);
				ordenCompraFX.fechaOrdenCompra.set(fechaOrden);
				
    			obListOrdenesDeCompra.add(ordenCompraFX);
    			
    			obListOrdenesDeCompraAux.add(ordenCompraFX);
    		}
    		
			tablaOrdenDeCompra.setItems(obListOrdenesDeCompraAux);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	
	public void setearOrdenDatosTablaOrdenes() {
		tablaOrden_FechaOrden.setSortType(TableColumn.SortType.DESCENDING);
		tablaOrdenDeCompra.getSortOrder().add(tablaOrden_FechaOrden);
	}

	
	public void llenarComboProveedor() {
		try {
			comboProveedor.getItems().clear();
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			listaProveedores = CRUD.obtenerListaProveedoresActivos();
	    		for (Proveedor proveedor : listaProveedores) {
	    			itemsCombo.add(proveedor.getNombreProveedor());
				}
	    	comboProveedor.setItems(itemsCombo);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	
    
    

}
