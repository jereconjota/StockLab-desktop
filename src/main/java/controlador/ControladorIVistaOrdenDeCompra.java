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
import java.util.Optional;

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
import org.apache.poi.ss.util.CellRangeAddress;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import hibernate.util.CRUD;
import hibernate.util.HibernateUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import main.AppMain;
import modelo.DetalleFacturaId;
import modelo.DetalleOrdenDeCompra;
import modelo.DetalleOrdenDeCompraId;
import modelo.Insumo;
import modelo.OrdenDeCompra;
import modelo.Proveedor;
import modelo.Sucursal;
import modeloAux.DetalleFacturaFX;
import modeloAux.DetalleOrdenDeCompraFX;
import modeloAux.DetalleRemitoFX;
import modeloAux.InsumoFX;

public class ControladorIVistaOrdenDeCompra {

	
    @FXML
    private TableView<DetalleOrdenDeCompraFX> detalleOrdenDeCompra;
    
    @FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> detalleOrdenDeCompra_Sector;

    @FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> detalleOrdenDeCompra_Referencia;

    @FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> detalleOrdenDeCompra_Categoria;

    @FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> detalleOrdenDeCompra_Insumo;

    @FXML
    private TableColumn<DetalleOrdenDeCompraFX, String> detalleOrdenDeCompra_Articulo;

    @FXML
    private TableColumn<DetalleOrdenDeCompraFX, Integer> detalleOrdenDeCompra_Cantidad;
    
    @FXML
    private TableColumn<DetalleOrdenDeCompraFX, Integer> detalleOrdenDeCompra_Unidades;
    
	@FXML
    private JFXComboBox<String> cBoxSeleccioneProvee;
    
    @FXML
    private Label lblNombreUsuarioOrdenCompra;
    
    @FXML
    private JFXButton btnLimpiarFiltros;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXButton btnExportarOrden;

    @FXML
    private JFXButton btnGuardarOrden;

    @FXML
    private JFXTextField txtFieldNroCUIT;

    @FXML
    private JFXTextField txtFieldNroDeOrden;
    
    @FXML
    private Label lblFechaCargaOrdenCompra;

    @FXML
    private Label lblFechaCargaFactura7;

    @FXML
    private Label lblFechaCargaFactura6;

    @FXML
    private Label lblFechaCargaFactura5;

    @FXML
    private Label lblFechaCargaFactura4;

    @FXML
    private Label lblFechaCargaFactura3;

    @FXML
    private Label lblDetalleFactura;

    private ObservableList<DetalleOrdenDeCompraFX> obListDetalleOrdenDeCompra = FXCollections.observableArrayList(); //para la tabla detalle Orden de Compra

	private ObservableList<DetalleOrdenDeCompraFX> obListPDPimprimirOC = FXCollections.observableArrayList();

    private Insumo insumoBD;

    private boolean reactivoProveedor; //me sirve para saber si el proveedor q ingrese el user, si esta "suspendido"
										//si lo reactiva "true" o no "false"
    private Proveedor proveedorBD;
    
    @FXML
    private JFXButton btnBuscar;

    private ControladorISecundariaInsumosOrdenDeCompra controllerSecundariaInsumos;
    private BorderPane iSecundariaInsumos;  //para tener referencia a la pantalla secundaria (la cual se usara a la hora de ir a pant alta insumo, y saber a donde volver)

	private BorderPane iPpalSecundaria;
	private ControladorIPpalSecundariaInsumos controllerPpalSecundariaInsumos;

    //list observable para llenar la tabla de insumos de la pantalla secundaria, para evitar
    //utilizar el metodo cargar insumos de esa pantalla secundaria y q no se ponga lenta
    private ObservableList<InsumoFX> obListInsumosPantSecundaria = FXCollections.observableArrayList(); 

    private Date fechaDeOrden;
    
    @FXML
    private TextArea textAreaObservacion;
	
    @FXML
    private Label lblNroDeOrden;
  
    @FXML
    private JFXButton btnLimpiarTabla;

	private boolean desdePDP;
    /**************************** CONSTRUCTOR *********************************/
        public ControladorIVistaOrdenDeCompra() {
    	
	}

	/**************************** GET - SET **********************************/
    
	public Insumo getInsumoBD() {
		return insumoBD;
	}
	public void setInsumoBD(Insumo insumoBD) {
		this.insumoBD = insumoBD;
	}
	

    public JFXComboBox<String> getcBoxSeleccioneProvee() {
		return cBoxSeleccioneProvee;
	}
	
    public boolean getReactivoProveedor() {
		return reactivoProveedor;
	}
	public void setReactivoProveedor(boolean reactivoProveedor) {
		this.reactivoProveedor = reactivoProveedor;
	}
	
	public Proveedor getProveedorBD() {
		return proveedorBD;
	}
	public void setProveedorBD(Proveedor proveedorBD) {
		this.proveedorBD = proveedorBD;
	}

	public Label getLblNombreUsuarioOrdenCompra() {
		return lblNombreUsuarioOrdenCompra;
	}
	public void setLblNombreUsuarioOrdenCompra(Label lblNombreUsuarioOrdenCompra) {
		this.lblNombreUsuarioOrdenCompra = lblNombreUsuarioOrdenCompra;
	}

	public Label getLblFechaCargaOrdenCompra() {
		return lblFechaCargaOrdenCompra;
	}
	public void setLblFechaCargaOrdenCompra(Label lblFechaCargaOrdenCompra) {
		this.lblFechaCargaOrdenCompra = lblFechaCargaOrdenCompra;
	}

    public ControladorIPpalSecundariaInsumos getControllerPpalSecundariaInsumos() {
		return controllerPpalSecundariaInsumos;
	}
	public void setControllerPpalSecundariaInsumos(ControladorIPpalSecundariaInsumos controllerPpalSecundariaInsumos) {
		this.controllerPpalSecundariaInsumos = controllerPpalSecundariaInsumos;
	}
	
    public BorderPane getiPpalSecundaria() {
		return iPpalSecundaria;
	}
//	public void setiPpalSecundaria(BorderPane iPpalSecundaria) {
//		this.iPpalSecundaria = iPpalSecundaria;
//	}
	
    public BorderPane getiSecundariaInsumos() {
		return iSecundariaInsumos;
	}
    
    public ObservableList<InsumoFX> getObListInsumosPantSecundaria() {
		return obListInsumosPantSecundaria;
	}
	public void setObListInsumosPantSecundaria(ObservableList<InsumoFX> obListInsumosPantSecundaria) {
		this.obListInsumosPantSecundaria = obListInsumosPantSecundaria;
	}

	public ObservableList<DetalleOrdenDeCompraFX> getObListDetalleOrdenDeCompra() {
		return obListDetalleOrdenDeCompra;
	}

	public void setObListDetalleOrdenDeCompra(ObservableList<DetalleOrdenDeCompraFX> obListDetalleOrdenDeCompra) {
		this.obListDetalleOrdenDeCompra = obListDetalleOrdenDeCompra;
	}

	public TableView<DetalleOrdenDeCompraFX> getDetalleOrdenDeCompra() {
		return detalleOrdenDeCompra;
	}

	public JFXTextField getTxtFieldNroDeOrden() {
		return txtFieldNroDeOrden;
	}

	public void setTxtFieldNroDeOrden(JFXTextField txtFieldNroDeOrden) {
		this.txtFieldNroDeOrden = txtFieldNroDeOrden;
	}

	public Date getFechaDeOrden() {
		return fechaDeOrden;
	}
	
	public TextArea getTextAreaObservacion() {
		return textAreaObservacion;
	}
	public void setTextAreaObservacion(TextArea textAreaObservacion) {
		this.textAreaObservacion = textAreaObservacion;
	}

	//	public void setiSecundariaInsumos(BorderPane iSecundariaInsumos) {
//		this.iSecundariaInsumos = iSecundariaInsumos;
//	}
	/********************************** METODOS ***********************************/

    @FXML
    private void initialize() {
    	definirTipoDatosColumnas();
    	seleccionarFila();
    	definirColumnasEditables();

    }
    
	public void limpiarCampos() {
		obListDetalleOrdenDeCompra.clear();
		obListPDPimprimirOC.clear();
		cBoxSeleccioneProvee.getSelectionModel().select(-1);
		this.txtFieldNroCUIT.clear();
		this.btnExportarOrden.setDisable(true);
		this.getTxtFieldNroDeOrden().clear();
		this.btnLimpiarTabla.setDisable(true);
		this.getTextAreaObservacion().clear();
		}
	
	//flag para saber si venimos desde la panralla pdp o no
	public void desdePDP(boolean desdepdp) {
		desdePDP = desdepdp;
	}
	
	private void seleccionarFila(){
//		//se le asigna una accion al click de cada fila de la tabla detalle
//		detalleOrdenDeCompra.setRowFactory( tv -> {
//		    TableRow<InsumoFX> row = new TableRow<>();
//		    row.setOnMouseClicked(event -> {
//		    	
//		        if (event.getButton() == MouseButton.PRIMARY){
//		        	//habilito btn Deshacer
//	        		btnDeshacer.setDisable(false);
//		        } 
//		        });
//		    return row ;
//		    });
		
		//se le asigna una accion al click de cada fila de la tabla insumos
//		detalleOrdenDeCompra.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//					if (newSelection != null) {
////						InsumoFX insumofx = detalleOrdenDeCompra.getSelectionModel().getSelectedItem();
//						
//						//habilito btn Deshacer
//		        		btnDeshacer.setDisable(false);
//						
//					}else{
//						detalleOrdenDeCompra.getSelectionModel().clearSelection();
//					}
//					
//				});
		detalleOrdenDeCompra.setRowFactory( tv -> {
		    TableRow<DetalleOrdenDeCompraFX> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getButton() == MouseButton.PRIMARY){
		        	
		        	if (event.getClickCount() == 3) { // DOBLE CLICK
		        		if (detalleOrdenDeCompra.getSelectionModel().isSelected(row.getIndex(), detalleOrdenDeCompra_Insumo)) {
		        			obListDetalleOrdenDeCompra.remove(detalleOrdenDeCompra.getSelectionModel().getSelectedIndex());
		        	    	detalleOrdenDeCompra.setItems(obListDetalleOrdenDeCompra);
		        	    	
						}else{
							detalleOrdenDeCompra.getSelectionModel().clearSelection();
						}
					}
		        }
		    });
		    return row ;
			});
	}
	
	private void definirTipoDatosColumnas() {
		detalleOrdenDeCompra_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		detalleOrdenDeCompra_Articulo.setCellValueFactory(cellData -> cellData.getValue().articulo);
		detalleOrdenDeCompra_Referencia.setCellValueFactory(cellData -> cellData.getValue().referencia);
		detalleOrdenDeCompra_Sector.setCellValueFactory(cellData -> cellData.getValue().nombreSector);
		detalleOrdenDeCompra_Categoria.setCellValueFactory(cellData -> cellData.getValue().nombreCategoria);
		detalleOrdenDeCompra_Cantidad.setCellValueFactory(cellData -> cellData.getValue().cantidad);
		detalleOrdenDeCompra_Unidades.setCellValueFactory(cellData -> cellData.getValue().unidades);
		alinearContenidoColumnas();
	}
	private void alinearContenidoColumnas() {
		detalleOrdenDeCompra_Insumo.setStyle("-fx-alignment: CENTER;");
		detalleOrdenDeCompra_Articulo.setStyle("-fx-alignment: CENTER;");
		detalleOrdenDeCompra_Referencia.setStyle("-fx-alignment: CENTER;");
		detalleOrdenDeCompra_Sector.setStyle("-fx-alignment: CENTER;");
		detalleOrdenDeCompra_Categoria.setStyle("-fx-alignment: CENTER;");	
		detalleOrdenDeCompra_Cantidad.setStyle("-fx-alignment: CENTER;");	
		detalleOrdenDeCompra_Unidades.setStyle("-fx-alignment: CENTER;");	
	}
	private void definirColumnasEditables() {
	
		//atributo necesario para convertir el tipo de dato de "cantidad (integer)" a string
		IntegerStringConverter convertirIaS = new IntegerStringConverter();
		detalleOrdenDeCompra_Cantidad.setCellFactory(TextFieldTableCell.<DetalleOrdenDeCompraFX, Integer>forTableColumn(convertirIaS));
		detalleOrdenDeCompra_Unidades.setCellFactory(TextFieldTableCell.<DetalleOrdenDeCompraFX, Integer>forTableColumn(convertirIaS));
		
		controlarEventosCampoCantidad();
		controlarEventosCampoUnidades();
	}

	private void controlarEventosCampoCantidad() {
		detalleOrdenDeCompra_Cantidad.setOnEditCommit(data -> {
			
			Integer newCantidad = data.getNewValue();
    		Integer oldCantidad = data.getOldValue();
    	   
    	    if (newCantidad != oldCantidad) { //significa q las cambio, entonces seteo este nuevo valor en la listaObservable
    	    								//xq, luego cuando vuelva a meter un insumo nuevo, los campos q modifico al isumo anterior
    	    								//van aparecer con los valores q tenia x 1era vez
    	    	obListDetalleOrdenDeCompra.get(detalleOrdenDeCompra.getSelectionModel().getSelectedIndex()).cantidad.set(newCantidad);
				//se lo seteo a esta listaObservable y no al DetalleFactura, xq todavia no se si guardara el ingreso
    	    	//entonces no toco los valores originales de la BD	
    	    	
    	    	//luego verifico si el valor de cantidad es cero, en dicho caso preguntar si desea setear el campo nro pedido del insumo
    	    	//con el nuevo valor q ingreso en cantidad (este valor afectara a todos insumos de igual nombre
    	    	if (oldCantidad == 0) {
    	    		Alert alert = new Alert(AlertType.CONFIRMATION);
			    	alert.setTitle("OPCIONAL");
	        		alert.setHeaderText(" Desea asignar cantidad (" + newCantidad +")" + "\n" + " como Nro Pedido del insumo" + "\n \n \n" + "¡Aviso! Afectara a todos los insumos de igual nombre");
	
		    		Optional<ButtonType> result = alert.showAndWait();
		    		if (result.get() == ButtonType.OK){
		    			agregarCantidadANroPedidoDelInsumo(newCantidad, obListDetalleOrdenDeCompra.get(detalleOrdenDeCompra.getSelectionModel().getSelectedIndex()).nombreInsumo.get());
		    		}
    	    	}
    	    } 
    	 
    	});
	}
	
	
	private void agregarCantidadANroPedidoDelInsumo(Integer newCantidadIN, String nombreInsumoIN) {
//		List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorNombre2(nombreInsumoIN);
		List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorNombreSucursalDiagnos(nombreInsumoIN);
		for (Insumo insumo : listaInsumos) {
			insumo.setNroPedido(newCantidadIN);
			CRUD.actualizarObjeto(insumo);
		}
	}
	

	private void controlarEventosCampoUnidades() {
		detalleOrdenDeCompra_Unidades.setOnEditCommit(data -> {
			
			Integer newCantidad = data.getNewValue();
    		Integer oldCantidad = data.getOldValue();
    	   
    	    if (newCantidad != oldCantidad) { //significa q las cambio, entonces seteo este nuevo valor en la listaObservable
    	    								//xq, luego cuando vuelva a meter un insumo nuevo, los campos q modifico al isumo anterior
    	    								//van aparecer con los valores q tenia x 1era vez
    	    	obListDetalleOrdenDeCompra.get(detalleOrdenDeCompra.getSelectionModel().getSelectedIndex()).unidades.set(newCantidad);
				//se lo seteo a esta listaObservable y no al DetalleFactura, xq todavia no se si guardara el ingreso
    	    	//entonces no toco los valores originales de la BD	
    	    } 
    	 
    	});
	}
	
	@FXML
	void exportarOrdenDeCompra() {
		 try{
			 txtFieldNroDeOrden.setUnFocusColor(Color.web("#4d4d4d"));

			 //Verificamos que esta todo ok en la orden para guardarla
			 if (verficarOrden()) {
				 	/////////////////////
			    	//BASICAMENTE TOMA UN EXCEL CON EL FORMATO DE ORDEN DE COMPRA, LO EDITA Y LO GUARDA EN UN NUEVO EXCEL
				 	/////////////////////
				 
				FileInputStream file;
		    		if (this.getcBoxSeleccioneProvee().getSelectionModel().getSelectedItem().equals("SISTEMAS ANALITICOS")) {
						file = new FileInputStream(new File("ordenesDeCompra/Orden de Compra Sistemas Analiticos.xls"));
					}else {
						file = new FileInputStream(new File("ordenesDeCompra/Orden de Compra General.xls"));
					}
				
				//HSSFWorkbook crea un libro .xls, XSSFWorkbook crea .xlsx
				HSSFWorkbook workbook = new HSSFWorkbook(file);
				 	
				//Al parecer de esta forma tambien
//				File file = new File("./OCsistemasanaliticos.xlsx");
//				Workbook workbook = WorkbookFactory.create(file);
				
				// Getting the Sheet at index zero
		        Sheet sheet = workbook.getSheetAt(0);
    	        
    	        //Creamos el encabezado de cada orden y la celda del nombre de proveedor
		        ArrayList<String> encabezadoInsumosOrdenDeCompra = new ArrayList<String>();
		        Cell nombreProveedor;
		        Cell nombreUsuario;
		        Cell fechaCargaOrdenDeCompra;
		        Cell nroOrdenDeCompra;
		        Cell mail;
			        if (this.getcBoxSeleccioneProvee().getSelectionModel().getSelectedItem().equals("SISTEMAS ANALITICOS")) {
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
						}else {
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
    			nombreProveedor.setCellValue(this.getcBoxSeleccioneProvee().getSelectionModel().getSelectedItem());
    			//Seteamos nombre del usuario que esta creando la orden
    			nombreUsuario.setCellValue(ControladorICsd_Principal.getUser().getNombre()+" "+ControladorICsd_Principal.getUser().getApellido());
    			//Seteamos fecha de creacion de la orden de compra
    			fechaCargaOrdenDeCompra.setCellValue(this.getLblFechaCargaOrdenCompra().getText());
    			//Seteamos numero de la orden de compra
    			nroOrdenDeCompra.setCellValue(this.getTxtFieldNroDeOrden().getText());
    			//Seteamos mail
		        mail.setCellValue(ControladorICsd_Principal.getUser().getMail());
		        
		        //Se comienza a cargar insumos a partir de la fila 11
		        Integer insumosApartirDeFila = 11;
		        
		        obListPDPimprimirOC = obListDetalleOrdenDeCompra;
		        
		        Row filas;
		        //integer auxiliar, lo usamos para contar las proximas filas
		        int rowNum = insumosApartirDeFila++;
		        
		        //Creamos los objetos de archivos a guardar
		        FileOutputStream fileOut = null;
		        File newfile = null;
		        
		        //Creamos tanta cantidad de filas como insumos haya en la lista de Orden de Compra
		        int totalRows = sheet.getLastRowNum();
				sheet.shiftRows(rowNum, totalRows, obListPDPimprimirOC.size());
				
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
    	        
		        for (DetalleOrdenDeCompraFX docFX : obListPDPimprimirOC) {
		        	Cell insumosEnOrden = null;
		        	filas = sheet.createRow(rowNum++);
	    			
	    			//Si es con Sistemas Analiticos, se utiliza la orden de compra personalizada
	    			if (this.getcBoxSeleccioneProvee().getSelectionModel().getSelectedItem().equals("SISTEMAS ANALITICOS")) {
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
		                }else {
		                	for (int i = 0; i < encabezadoInsumosOrdenDeCompra.size(); i++) {
			                	insumosEnOrden = filas.createCell(i);
				                	switch (i) {
				                	//Segun el titulo del encabezado, se llena la celda con lo que corresponda
										case 0:
						                    insumosEnOrden.setCellValue(docFX.referencia.get());
						                    insumosEnOrden.setCellStyle(style);
											break;
										case 1:
						                    insumosEnOrden.setCellValue(docFX.cantidad.get());
						                    insumosEnOrden.setCellStyle(style);
						                    break;
										case 2:
						                    insumosEnOrden.setCellValue(docFX.unidades.get());
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
//											insumosEnOrden.setCellStyle(style);		
				        			
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
					 	guardarOrdenDeCompra();
					 	// Confirmamos y posisionamos el foco en la tabla
				    	mostrarMsjGuardadoExitoso();
				    	// Closing the workbook
				        workbook.close();
				    	limpiarCampos();
				    	// Abrimos archivo en excel o programa predeterminado
				    	abrirOrdenCreada(newfile);
		    		} catch (IOException e) {
	    				e.printStackTrace();
	    				} 
		    	}
			    	ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().requestFocus(); 
			 }else {
				 txtFieldNroDeOrden.setUnFocusColor(Color.RED);
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
	
    private boolean verficarOrden() {
    	boolean verificacion = false;
    	
    	//si esta vacio el campo retorna falso
    	//Ya no verificamos si el nro de orden existe ya que paso a ser un atributo del proveedor, cada proveedor llevar el numero de orden que le corresponde
    	if (!this.getTxtFieldNroDeOrden().getText().equals("")) {
				verificacion = true;
		}
    	for (DetalleOrdenDeCompraFX detalleOrdenDeCompraFX : obListDetalleOrdenDeCompra) {
    		if(detalleOrdenDeCompraFX.cantidad.get()==0 || detalleOrdenDeCompraFX.cantidad.get()==null) {
//    			verificacion = false;
    		}
		}
    	return verificacion;
	}
   
	@FXML
    void guardarOrdenDeCompra() {
    	//Metodo deshacer (cambio a 'guardarOrdenDeCompra')
    	/**obListDetalleOrdenDeCompra.remove(detalleOrdenDeCompra.getSelectionModel().getSelectedIndex());
    	detalleOrdenDeCompra.setItems(obListDetalleOrdenDeCompra);
    	
    	detalleOrdenDeCompra.getSelectionModel().clearSelection();
    	
    	btnGuardarOrden.setDisable(true);
    	this.btnExportarOrden.setDisable(true);**/
    	
    	
    	OrdenDeCompra ordenDeCompra = new OrdenDeCompra(ControladorICsd_Principal.getUser(),proveedorBD, 
    			Integer.valueOf(this.getTxtFieldNroDeOrden().getText()), this.getFechaDeOrden(), observacion());
    	proveedorBD.setNroDeOrdenDeCompra(Integer.valueOf(this.getTxtFieldNroDeOrden().getText()));
    	CRUD.actualizarObjeto(proveedorBD);
    	CRUD.guardarObjeto(ordenDeCompra);
    	
    	for (DetalleOrdenDeCompraFX detalleODCFX : obListDetalleOrdenDeCompra) {
    		
    		DetalleOrdenDeCompraId id = new DetalleOrdenDeCompraId(ordenDeCompra.getPkIdOrdenCompra());
    		
			DetalleOrdenDeCompra detalleOrdenDeCompra = new DetalleOrdenDeCompra(id, verificarInsumo(detalleODCFX), ordenDeCompra,detalleODCFX.cantidad.get(),
					String.valueOf(verificarInsumo(detalleODCFX).getPrecioInsumo()),"", detalleODCFX.unidades.get());
			CRUD.guardarObjeto(detalleOrdenDeCompra);

		}
    }

    private String  observacion() {
		if (this.getTextAreaObservacion().getText().length()<256) {
			return this.getTextAreaObservacion().getText();
		}else {
			return this.getTextAreaObservacion().getText().substring(0, 255);
		}
		
	}

	private Insumo verificarInsumo(DetalleOrdenDeCompraFX detalleOdcFX) {
		Insumo insumoAretornar = null;
		insumoAretornar = CRUD.obtenerInsumoPorNroArticulo(detalleOdcFX.articulo.get());
		return insumoAretornar;
    }
    
    @FXML
    void cancelar() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("CANCELAR ORDEN DE COMPRA");
		alert.setHeaderText("Desea cancelar la emision de ésta orden de compra?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			this.limpiarCampos();
			
        } else if (result.get() == ButtonType.CANCEL){
	    	ControladorICsd_Principal.controllerTablaPDP.getTablaPDP().requestFocus(); 
        }
    }

	public void llenarComboProveedores() {
		try {
			cBoxSeleccioneProvee.getItems().clear();
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
			List<Proveedor> listaProveedores = CRUD.obtenerListaProveedoresActivos();
			
    		for (Proveedor proveedor : listaProveedores) {
    			itemsCombo.add(proveedor.getNombreProveedor());
			}
    		cBoxSeleccioneProvee.setItems(itemsCombo);
    		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	 //se encarga de setear el campo txtField CUIT, de acuerdo a lo seleccionado en el
    //comboBox donde estan todos los provee del sistema (solo "activos")
	@FXML
    void eventoCBoxSeleccioneProvee() {
		try {
			//El evento del combo se activa cuando volvemos abrir la ventana ingresarFactura
			//por eso tenemos q checkear con este if para que no de error
			if (cBoxSeleccioneProvee.getSelectionModel().getSelectedIndex() != -1) {
				//Obtenemos datos del proveedor
				String nombreProvee = cBoxSeleccioneProvee.getSelectionModel().getSelectedItem();
				
				if (nombreProvee.equals("SISTEMAS ANALITICOS")) {
					this.detalleOrdenDeCompra_Unidades.setVisible(false);
				}else {
					this.detalleOrdenDeCompra_Unidades.setVisible(true);
				}
				
				Proveedor provee = CRUD.obtenerProveedorPorNombre(nombreProvee);
				txtFieldNroCUIT.clear();
				txtFieldNroCUIT.setText(provee.getNroCuit());
//				lblMsjErrorCUIT.setVisible(false);
				txtFieldNroCUIT.setUnFocusColor(Color.web("#4d4d4d"));
				setearComboBoxProvee(provee.getNombreProveedor());
				
				//Precargamos los insumos asociados al proveedor, si los tuviera
				List<Insumo> listaInsumos = CRUD.obtenerListaInsumosActivosAgrupadosPorProveedorConStockGeneral(provee.getPkIdProveedor());
				
				if (listaInsumos.isEmpty() && !desdePDP) {
					System.out.println("proveedor sin insumos asociados");
					 limpiarTabla();
				}else {
					//Si viene desde 
					if (!desdePDP) {
						Alert alert = new Alert(AlertType.CONFIRMATION);
				    	alert.setTitle("INSUMOS DEL PROVEEDOR "+provee.getNombreProveedor());
		        		alert.setHeaderText("Desea cargar a la orden los insumos asociados a "+provee.getNombreProveedor());
		
			    		Optional<ButtonType> result = alert.showAndWait();
			    		if (result.get() == ButtonType.OK){
			    			cargarInsumosAorden(listaInsumos);
			    		} else if (result.get() == ButtonType.CANCEL){
			            	this.btnBuscar.requestFocus();
			            }
					}
	    		}
				//Agregamos automaticamente el nro de orden de compra que corresponda
//				this.getTxtFieldNroDeOrden().setText(Integer.toString(CRUD.obtenerUltimoNroDeOrdenDeCompra()+1));
				provee.setNroDeOrdenDeCompra(provee.getNroDeOrdenDeCompra()+1);
				this.getTxtFieldNroDeOrden().setText(Integer.toString(provee.getNroDeOrdenDeCompra()));
				this.setProveedorBD(provee); //para tener una referencia al proveedor, y luego completar los campos con sus datos

			}else{
				this.btnExportarOrden.setDisable(true);
				btnGuardarOrden.setDisable(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	private void cargarInsumosAorden(List<Insumo> listaIns) {
		obListDetalleOrdenDeCompra.clear();
		
		for (Insumo insumo : listaIns) {
			final DetalleOrdenDeCompraFX detalleOrdenCompraFX = new DetalleOrdenDeCompraFX();
			
			detalleOrdenCompraFX.nombreInsumo.set(insumo.getNombreInsumo());
			detalleOrdenCompraFX.articulo.set(insumo.getNroArticulo());
			detalleOrdenCompraFX.referencia.set(insumo.getReferencia());
			detalleOrdenCompraFX.nombreCategoria.set(insumo.getNombreCategoria());
			detalleOrdenCompraFX.nombreSector.set(insumo.getNombreSector());
			if (insumo.getNroPedido() != null) {
				detalleOrdenCompraFX.cantidad.set(insumo.getNroPedido());
			} else {
				detalleOrdenCompraFX.cantidad.set(0);
			}
			detalleOrdenCompraFX.unidades.set(0);
			
			obListDetalleOrdenDeCompra.add(detalleOrdenCompraFX);
		}
	
		this.getDetalleOrdenDeCompra().setItems(obListDetalleOrdenDeCompra);
		habilitarBotones();
	}
	
	private void setearComboBoxProvee(String nombreProveedorIN) {
		boolean salir = false;
		Integer i = 0;
		try {
			//1ero verifico si reactivo el provee, de ser asi.. tengo q agregarlo al comboBox provee
			if (this.getReactivoProveedor()) {
				cBoxSeleccioneProvee.getItems().add(nombreProveedorIN);
				this.setReactivoProveedor(false);						//reseteo el boolean, para q no agregue repetidos, una vez reactiva el provee
			}
			while ((i < cBoxSeleccioneProvee.getItems().size()) && (!(salir))) {
				if (cBoxSeleccioneProvee.getItems().get(i).equals(nombreProveedorIN)) {
					cBoxSeleccioneProvee.getSelectionModel().select(i);
					salir = true;
				} else {
					i = i + 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	public void setearFechaCarga() {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		this.fechaDeOrden = new Date();
		String fechaCargaFactura = formato.format(this.getFechaDeOrden());
		lblFechaCargaOrdenCompra.setText(fechaCargaFactura);
	}
	
	@FXML
	void mostrarPantallaSecundariaInsumos(ActionEvent event) {
    	try {
    		Stage otroStage = new Stage();

        	/////////////////////////////// INSTANCIO PANTALLA PPAL SECUNDARIA (contendra las pantallas q tiene los datos-componentes, y/o el alta insumo)
        	iPpalSecundaria = null;
        	controllerPpalSecundariaInsumos = null;
        	
        	FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(AppMain.class.getResource("/vista/IPpalSecundariaInsumos.fxml"));
        	
        	/////////////////////////////// INSTANCIO PANTALLA Q CONTIENE LOS DATOS Y COMPONENTES
        	iSecundariaInsumos = null;
    		controllerSecundariaInsumos = null;
    		
        	FXMLLoader loader2 = new FXMLLoader();
        	loader2.setLocation(AppMain.class.getResource("/vista/ISecundariaInsumosOrdenDeCompra.fxml"));
        	//////////////////////////////
    		
    		iPpalSecundaria = loader.load();
    		controllerPpalSecundariaInsumos = loader.getController();

    		controllerPpalSecundariaInsumos.setOtroStage(otroStage);
    		Scene escena = new Scene(iPpalSecundaria);
    		otroStage.setScene(escena);
    		
    		otroStage.setResizable(false);
    		
    		//luego de instanciar-crear-mostrar la pantalla ppal secundaria (esta solo es contendora)
    		//ahi mismo le seteo en el centro la pantalla q verdaderamente contiene toda la info
    		iSecundariaInsumos = loader2.load();
    		controllerSecundariaInsumos = loader2.getController();
    		
    		iPpalSecundaria.setCenter(iSecundariaInsumos);
    		
    		controllerSecundariaInsumos.getLblDetalleFactura().requestFocus();
    		
    		controllerSecundariaInsumos.llenarComboSector();
    		
    		controllerSecundariaInsumos.llenarTablaInsumos();
    		
    		controllerSecundariaInsumos.setearOrdenDatosEnTabla();
    		
    		otroStage.setOnCloseRequest(new EventHandler<WindowEvent>(){ //controla el evento q se genera cuando se cierra la ventana
	            @Override public void handle(WindowEvent event) {
	            	controllerSecundariaInsumos.cancelar();
	            }  
	        });
    		
    	/////////////////////////////////////////////////////////////////////////////////
    	///// estas 3 lineas es para evitar q una vez abierta la ventana 	/////////////
    	/////  secundaria, si se clickea en la ventana principal, no se   ///////////
    	////  cambie el foco, y permanezca en la secundaria, hasta q se cierre   ////////
    	////////////////////////////////////////////////////////////////////////////////	
    		otroStage.initOwner(ControladorILogin.controllerPpal.getPrimaryStage());
    		otroStage.initModality(Modality.APPLICATION_MODAL); 
    		otroStage.showAndWait();

		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
    }

	//metodo q es llamado desde pant secundaria-insumos
	public void habilitarBotones() {
		this.btnExportarOrden.setDisable(false);
		this.getDetalleOrdenDeCompra().getSelectionModel().clearSelection();
		btnGuardarOrden.setDisable(false);
		this.btnLimpiarTabla.setDisable(false);
	}

    private void mostrarMsjGuardadoExitoso() {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
    	alert.setHeaderText("Orden de compra creada");
		alert.setContentText("La orden de compra Nro: "+ this.getTxtFieldNroDeOrden().getText() +" ha sido guardada satisfactoriamente");
		alert.showAndWait();
	}

    
    @FXML
    void limpiarTabla() {
    	this.obListDetalleOrdenDeCompra.clear();
    	this.btnLimpiarTabla.setDisable(true);
    	this.btnExportarOrden.setDisable(true);
    }

	
    public void cargarInsumosDesdePDPaOrden(String nombreProv,ObservableList<InsumoFX> listaInsumos) {
    	//Agregamos automaticamente el nro de orden de compra que corresponda
		this.getTxtFieldNroDeOrden().setText(Integer.toString(CRUD.obtenerUltimoNroDeOrdenDeCompra()+1));
		
		Proveedor provee = CRUD.obtenerProveedorPorNombre(nombreProv);
		this.setProveedorBD(provee);
		txtFieldNroCUIT.clear();
		txtFieldNroCUIT.setText(provee.getNroCuit());
//		lblMsjErrorCUIT.setVisible(false);
		txtFieldNroCUIT.setUnFocusColor(Color.web("#4d4d4d"));
		setearComboBoxProvee(provee.getNombreProveedor());
		
		obListDetalleOrdenDeCompra.clear();
		
		for (InsumoFX insumoFX : listaInsumos) {
			final DetalleOrdenDeCompraFX detalleOrdenCompraFX = new DetalleOrdenDeCompraFX();
			
			detalleOrdenCompraFX.nombreInsumo.set(insumoFX.nombreInsumo.get());
			detalleOrdenCompraFX.articulo.set(insumoFX.articulo.get());
			detalleOrdenCompraFX.referencia.set(insumoFX.referencia.get());
			detalleOrdenCompraFX.nombreCategoria.set(insumoFX.nombreCategoria.get());
			detalleOrdenCompraFX.nombreSector.set(insumoFX.nombreSector.get());
			detalleOrdenCompraFX.cantidad.set(0);
			detalleOrdenCompraFX.unidades.set(0);

			obListDetalleOrdenDeCompra.add(detalleOrdenCompraFX);
		}
	this.getDetalleOrdenDeCompra().setItems(obListDetalleOrdenDeCompra);
	habilitarBotones();
	}
    
}