package controlador;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import hibernate.util.CRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.IntegerStringConverter;
import main.AppMain;
import modelo.DetalleFactura;
import modelo.DetalleFacturaId;
import modelo.DetalleRemito;
import modelo.DetalleRemitoId;
import modelo.Factura;
import modelo.Insumo;
import modelo.Proveedor;
import modelo.Remito;
import modeloAux.DetalleFacturaFX;
import modeloAux.DetalleRemitoFX;
import modeloAux.InsumoFX;

public class ControladorIIngresarRemito {
	
	@FXML
    private JFXTextField txtFieldNroFactura;

	@FXML
    private JFXTextField txtFieldNroRemito;

    @FXML
    private DatePicker datePickerFechaRemito;

    @FXML
    private JFXComboBox<String> cBoxSeleccioneProvee;

    @FXML
    private Label lblFechaCargaRemito;
    
    @FXML
    private Label lblPertenecienteA;
    
    @FXML
    private Label lblFacturaNro;
    
    @FXML
    private JFXButton btnBuscarInsumos;
    
    @FXML
    private JFXButton btnGuardar;
  
    @FXML
    private JFXButton btnDeshacer;
    
    @FXML
    private JFXButton btnCancelar;
    
    @FXML
    private TableView<DetalleRemitoFX> tablaDetalleRemito;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_Insumo;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_Articulo;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_NroLote;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_UnidadMedida;

    @FXML
    private TableColumn<DetalleRemitoFX, Integer> tablaDetalleRemito_Cantidad;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_Categoria;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_Sector;
    
    @FXML
    private TableColumn<DetalleRemitoFX, Date> tablaDetalleRemito_Vencimiento;


//	@FXML
//    private JFXTextArea textAreaObservacion;
    @FXML
    private TextArea textAreaObservacion;
    
    @FXML
    private JFXCheckBox checkBoxVerSecCat;

	@FXML
    private JFXTextField textField_cuitProveedor;

	private BorderPane iPpalSecundaria;
    private ControladorIPpalSecundariaInsumos controllerPpalSecundariaInsumos;
    
    private BorderPane iSecundariaInsumosRemito;  //para tener referencia a la pantalla secundaria (la cual se usara a la hora de ir a pant alta insumo, y saber a donde volver)
    private ControladorISecundariaInsumosRemito controllerSecundariaInsumosRemito;
    
    public  ObservableList<DetalleRemitoFX> obListDetalleRemito = FXCollections.observableArrayList();
    private ObservableList<DetalleRemitoFX> obListDetalleRemitoAux = FXCollections.observableArrayList();

    //list observable para llenar la tabla de insumos de la pantalla secundaria, para evitar
    //utilizar el metodo cargar insumos de esa pantalla secundaria y q no se ponga lenta
    private ObservableList<InsumoFX> obListInsumosPantSecundaria = FXCollections.observableArrayList(); 
    
    private List<Proveedor> listaProveedores;
    
    private Proveedor proveedorBD;

    private Factura facturaBD;
    
    boolean datosYaGuardados = false;

	SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

	
	
	/**************************** CONSTRUCTOR *********************************/
	
	public ControladorIIngresarRemito() {
		
	}
	
	
	
	/**************************** GET - SET **********************************/
    
	public JFXTextField getTxtFieldNroFactura() {
		return txtFieldNroFactura;
	}


	public JFXTextField getTxtFieldNroRemito() {
		return txtFieldNroRemito;
	}


	public DatePicker getDatePickerFechaRemito() {
		return datePickerFechaRemito;
	}


	public JFXComboBox<String> getcBoxSeleccioneProvee() {
		return cBoxSeleccioneProvee;
	}


	public JFXButton getBtnGuardar() {
		return btnGuardar;
	}


	public TableView<DetalleRemitoFX> getTablaDetalleRemito() {
		return tablaDetalleRemito;
	}


//	public JFXTextArea getTextAreaObservacion() {
//		return textAreaObservacion;
//	}
	public TextArea getTextAreaObservacion() {
		return textAreaObservacion;
	}


	public JFXTextField getTextField_cuitProveedor() {
		return textField_cuitProveedor;
	}
    
    
    public BorderPane getiSecundariaInsumosRemito() {
		return iSecundariaInsumosRemito;
	}


	public BorderPane getiPpalSecundaria() {
		return iPpalSecundaria;
	}


	public ObservableList<InsumoFX> getObListInsumosPantSecundaria() {
		return obListInsumosPantSecundaria;
	}

	public void setObListInsumosPantSecundaria(ObservableList<InsumoFX> obListInsumosPantSecundaria) {
		this.obListInsumosPantSecundaria = obListInsumosPantSecundaria;
	}


	public ControladorIPpalSecundariaInsumos getControllerPpalSecundariaInsumos() {
		return controllerPpalSecundariaInsumos;
	}


	public ControladorISecundariaInsumosRemito getControllerSecundariaInsumosRemito() {
		return controllerSecundariaInsumosRemito;
	}


	public ObservableList<DetalleRemitoFX> getObListDetalleRemito() {
		return obListDetalleRemito;
	}

	public void setObListDetalleRemito(ObservableList<DetalleRemitoFX> obListDetalleRemito) {
		this.obListDetalleRemito = obListDetalleRemito;
	}


	public Proveedor getProveedorBD() {
		return proveedorBD;
	}

	public void setProveedorBD(Proveedor proveedorBD) {
		this.proveedorBD = proveedorBD;
	}
	

	public Label getLblFechaCargaRemito() {
		return lblFechaCargaRemito;
	}



	/********************************** METODOS ***********************************/
	
	@FXML
    void initialize() {
//		llenarComboProveedores();		////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!! LLAMARLO DESDE AFUERA!!!!!!!!!!!!!!!!
		definirTipoDatoColumnas();
		definirColumnasEditables();
		restringirFechas();
		setearToolTipTxtFields();		////////////////////////////////!!!!!!!!!!!  XQ LO COMENTASTE?????????????????
		seleccionarFila();
    }


	@FXML
    void manejaEventoCheckBoxVerSecCat(ActionEvent event) {
		if (checkBoxVerSecCat.isSelected()) {
			tablaDetalleRemito_Sector.setVisible(true);
			tablaDetalleRemito_Categoria.setVisible(true);
		} else {
			tablaDetalleRemito_Sector.setVisible(false);
			tablaDetalleRemito_Categoria.setVisible(false);
		}
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
        	iSecundariaInsumosRemito = null;
        	controllerSecundariaInsumosRemito = null;
    		
        	FXMLLoader loader2 = new FXMLLoader();
        	loader2.setLocation(AppMain.class.getResource("/vista/ISecundariaInsumosRemito.fxml"));
        	//////////////////////////////
    		
    		iPpalSecundaria = loader.load();
    		controllerPpalSecundariaInsumos = loader.getController();

    		controllerPpalSecundariaInsumos.setOtroStage(otroStage);
    		Scene escena = new Scene(iPpalSecundaria);
    		otroStage.setScene(escena);
    		
    		otroStage.setResizable(false);
    		
    		//luego de instanciar-crear-mostrar la pantalla ppal secundaria (esta solo es contendora)
    		//ahi mismo le seteo en el centro la pantalla q verdaderamente contiene toda la info
    		iSecundariaInsumosRemito = loader2.load();
    		controllerSecundariaInsumosRemito = loader2.getController();
    		
    		iPpalSecundaria.setCenter(iSecundariaInsumosRemito);
    		
    		controllerSecundariaInsumosRemito.getLblDetalleRemito().requestFocus();
    		
    		controllerSecundariaInsumosRemito.llenarComboSector();
    		
    		controllerSecundariaInsumosRemito.llenarTablaInsumos();
    		
    		controllerSecundariaInsumosRemito.setearOrdenDatosEnTabla();
    		
    		otroStage.setOnCloseRequest(new EventHandler<WindowEvent>(){ //controla el evento q se genera cuando se cierra la ventana
	            @Override public void handle(WindowEvent event) {
	            	controllerSecundariaInsumosRemito.cancelar();
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
	
	
	@FXML
    void eventoCBoxSeleccioneProvee(ActionEvent event) {
    	if (this.getcBoxSeleccioneProvee().getSelectionModel().getSelectedIndex() != -1) {
    		for (Proveedor proveedor : listaProveedores) {
        		if (this.getcBoxSeleccioneProvee().getSelectionModel().getSelectedItem().equals(proveedor.getNombreProveedor())) {
        			this.getTextField_cuitProveedor().setText(proveedor.getNroCuit());
        			this.setProveedorBD(proveedor);
        			this.getTextField_cuitProveedor().setUnFocusColor(Color.web("#4d4d4d"));
        			break;
        		}
    		}    
    	}	
    }
	
	
	@FXML
    void guardarRemito(ActionEvent event) {
        Remito remitoNuevo = null;
        String observacion = null;
    	try {
    		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");  //formato q maneja el mysql
    		SimpleDateFormat formato2 = new SimpleDateFormat("dd-MM-yyyy");
    			
    		this.getTxtFieldNroRemito().setUnFocusColor(Color.web("#4d4d4d"));
    		
    		if(verificarIngresoDeDatos() && datosEnTablaValidos()) {
   	
    			ControladorILogin.controllerPpal.mostrarProgressIndicator();
    			
    			Date fechaCarga1 = null;
    			Date feCarga1 = null;
		
    			feCarga1 = formato2.parse(lblFechaCargaRemito.getText());
    			String feCar1= formato.format(feCarga1);
    			fechaCarga1 = formato.parse(feCar1);
				
	    		Date fechaRemito = java.sql.Date.valueOf(this.getDatePickerFechaRemito().getValue());
	    	
	    		Date fechaDeCargaRemito = formato.parse(this.getLblFechaCargaRemito().getText());

			//Si el campo de numero de factura esta vacio, se crea un remito sin factura asociada.
	    		if (this.getTxtFieldNroFactura().getText().isEmpty()) {
	   
	    			Proveedor proveedor = CRUD.obtenerProveedorPorNroCuit(this.getTextField_cuitProveedor().getText());
	    		
	    			if (this.getTextAreaObservacion().getText().isEmpty()) {
						observacion = "Sin comentarios";
					} else {
						observacion = this.getTextAreaObservacion().getText();
					}
	    			
	    			remitoNuevo = new Remito(this.getTxtFieldNroRemito().getText(), fechaRemito, fechaCarga1, observacion);
					CRUD.guardarObjeto(remitoNuevo);
					
					DetalleRemitoId detalleRemitoid = new DetalleRemitoId(remitoNuevo.getIdRemito());
					
					for (DetalleRemitoFX drFX : obListDetalleRemito) {
						Insumo insumo = verificarInsumo(drFX);
    					DetalleRemito detalleRemito = new DetalleRemito(detalleRemitoid,insumo,proveedor,remitoNuevo,drFX.cantidad.get());

    					ControladorICsd_Principal.controllerTablaMovimientos.guardarMovimientoDesdeRemito(insumo, drFX.cantidad.get(), "Diagnos", observacion);

    					CRUD.guardarObjeto(detalleRemito);
					}
					
			//Si el campo de numero de factura esta completo, revisamos si la factura existe o hay que crearla.
	    		} else { 
	    			
	    			if (!datosYaGuardados) {
	    				facturaBD = CRUD.obtenerFacturaPorNroFactura(this.getTxtFieldNroFactura().getText());
	    			}
		    					    		
		    		//Si no existe factura con el nro ingresado, crea factura nueva
		    		if (facturaBD == null) {
		    		
		    			facturaBD = new Factura(this.getTxtFieldNroFactura().getText());
						Date fechaCarga = null;
						Date feCarga = null;
					
						feCarga = formato2.parse(lblFechaCargaRemito.getText());
						String feCar = formato.format(feCarga);
						fechaCarga = formato.parse(feCar);
						facturaBD.setFechaCarga(fechaCarga);
						
						Date fechaFacturaPicker = java.sql.Date.valueOf(datePickerFechaRemito.getValue());  //conversion de LocalDate a Date
						String fechaFacPicker = formato.format(fechaFacturaPicker);
						Date fechaFactura = formato.parse(fechaFacPicker);	//basicamente, le aplique el formato "yyyy-MM-dd" al datepicker para q lo reconosca el mysql
						facturaBD.setFechaFactura(fechaFactura);
						facturaBD.setTipoFactura("");  //esto es para evitar error al llenar campos desde el modificar factura
						facturaBD.setObservacionFactura("Sin comentarios");
						facturaBD.setTieneRemito("Si");
						CRUD.guardarObjeto(facturaBD);
		    			
		    			Proveedor proveedor = CRUD.obtenerProveedorPorNroCuit(this.getTextField_cuitProveedor().getText());

		    			if (this.getTextAreaObservacion().getText().isEmpty()) {
							observacion = "Sin comentarios";
						} else {
							observacion = this.getTextAreaObservacion().getText();
						}
		    			
		    			remitoNuevo = new Remito(this.getTxtFieldNroRemito().getText(), facturaBD, fechaRemito, facturaBD.getFechaCarga(), observacion);
						CRUD.guardarObjeto(remitoNuevo);
					    
	    				DetalleFacturaId detalleFacturaId = new DetalleFacturaId(facturaBD.getIdFactura());
	    				DetalleRemitoId detalleRemitoid = new DetalleRemitoId(remitoNuevo.getIdRemito());
	    				
	    				for (DetalleRemitoFX drFX : obListDetalleRemito) {
	    					
	    					Insumo insumo = verificarInsumo(drFX);
	    					
	    					DetalleFactura detalleFactura = new DetalleFactura(detalleFacturaId, facturaBD, insumo, proveedor, remitoNuevo, drFX.cantidad.get(), insumo.getPrecioInsumo(), Float.valueOf("0"));
	    					CRUD.guardarObjeto(detalleFactura);
	    					
	    					DetalleRemito detalleRemito = new DetalleRemito(detalleRemitoid,insumo,proveedor,remitoNuevo,drFX.cantidad.get());
	    					CRUD.guardarObjeto(detalleRemito);

	    					ControladorICsd_Principal.controllerTablaMovimientos.guardarMovimientoDesdeRemito(insumo, drFX.cantidad.get(), "Diagnos", observacion);
						}
				//Si existe, asocia el remito nuevo a la factura existente
		    		} else {	
    					DetalleFactura df = CRUD.obtenerDetalleFacturaPorIdFactura(facturaBD.getIdFactura());

    					Proveedor proveedor = CRUD.obtenerProveedorPorId(df.getProveedor().getPkIdProveedor());
    					this.getcBoxSeleccioneProvee().setValue(proveedor.getNombreProveedor());
	    				this.getTextField_cuitProveedor().setText(proveedor.getNroCuit());
		    				
	    				if (this.getTextAreaObservacion().getText().isEmpty()) {
							observacion = "Sin comentarios";
						} else {
							observacion = this.getTextAreaObservacion().getText();
						}	
	    				
	    				remitoNuevo = new Remito(this.getTxtFieldNroRemito().getText(), facturaBD, fechaRemito, fechaDeCargaRemito, observacion);
						CRUD.guardarObjeto(remitoNuevo);
					   
						remitoNuevo.setObservacionRemito(observacion);
	    				DetalleFacturaId detalleFacturaId = new DetalleFacturaId(facturaBD.getIdFactura());
	    				DetalleRemitoId detalleRemitoid = new DetalleRemitoId(remitoNuevo.getIdRemito());
	    				
	    				for (DetalleRemitoFX drFX : obListDetalleRemito) {
	    					Insumo insumo = verificarInsumo(drFX);
	    					
	    					DetalleFactura detalleFactura = new DetalleFactura(detalleFacturaId, facturaBD, insumo, proveedor, remitoNuevo, drFX.cantidad.get(), insumo.getPrecioInsumo(), Float.valueOf("0"));
	    					CRUD.guardarObjeto(detalleFactura);
	    					
	    					DetalleRemito detalleRemito = new DetalleRemito(detalleRemitoid,insumo,proveedor,remitoNuevo,drFX.cantidad.get());
	    					CRUD.guardarObjeto(detalleRemito);
	    					
			    			ControladorICsd_Principal.controllerTablaMovimientos.guardarMovimientoDesdeRemito(insumo, drFX.cantidad.get(), "Diagnos", observacion);
						}
	    			}	
	    		}
	    		
	    		this.limpiarCampos();
	    		Alert alert = new Alert(AlertType.INFORMATION);
	    		alert.setTitle("AVISO");
	    		alert.setHeaderText("REMITO GUARDADO");
	    		alert.setContentText("El remito Nro: "+remitoNuevo.getNroRemito()+" se guardo correctamente");

	    		alert.showAndWait();
	    		
	    		ControladorILogin.controllerPpal.cerrarStageProgress();
	    		
    		}
	    } catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
	
	
	@FXML
    void cancelar() {
		this.limpiarCampos();
		this.habilitarBotones();
    }
	
	
	@FXML
    void eventoDatePickerRemito(ActionEvent event) {
		formatearFormatoFecha();
		this.getDatePickerFechaRemito().setStyle("-fx-border-color: none;");
    }
	
	
	@FXML
    void deshacer(ActionEvent event) {
		if (tablaDetalleRemito.getSelectionModel().getSelectedIndex() != -1) {
			
			obListDetalleRemito.remove(tablaDetalleRemito.getSelectionModel().getSelectedIndex());
	    	tablaDetalleRemito.setItems(obListDetalleRemito);
	    	
	    	tablaDetalleRemito.getSelectionModel().clearSelection();
	    	
	    	btnDeshacer.setDisable(true);
	    	
	    	//luego tengo q verificar q si no queda mas elementos en la tabla detalle 
	    	//entonces deshabilito los botones guardar y deshacer
	    	if (obListDetalleRemito.size() == 0) {
				btnGuardar.setDisable(true);
			}
		} else {
			btnDeshacer.setDisable(true);
		}
    }
	
	
	@FXML
    void buscarFactura(KeyEvent event) {
		try {
			if (event.getCode().equals(KeyCode.ENTER)){
	    		if (hayBlanco(this.getTxtFieldNroFactura().getText())) {
	    			Alert alert = new Alert(AlertType.ERROR);
	    			alert.setTitle("DATOS ERRONEOS");
	    			alert.setHeaderText("El campo Nro de Factura contiene datos erroneos");
	    			alert.setContentText("Si desea asociar una factura ingrese un numero valido y presione ENTER");
	    			alert.showAndWait();
	    			
	    			this.getTextField_cuitProveedor().clear();
		  			this.getTextField_cuitProveedor().setDisable(false);
		  			this.getcBoxSeleccioneProvee().getSelectionModel().select(-1);
		  			this.getcBoxSeleccioneProvee().setDisable(false);
			} else {
				facturaBD = CRUD.obtenerFacturaPorNroFactura(this.getTxtFieldNroFactura().getText());	
				if (facturaBD != null) {
					if (facturaBD.getTieneRemito().equals("Si")) {
							DetalleFactura df = CRUD.obtenerDetalleFacturaPorIdFactura(facturaBD.getIdFactura());
							proveedorBD = CRUD.obtenerProveedorPorId(df.getProveedor().getPkIdProveedor());
							this.getcBoxSeleccioneProvee().setValue(proveedorBD.getNombreProveedor());
							this.getTextField_cuitProveedor().setText(proveedorBD.getNroCuit());
							this.getcBoxSeleccioneProvee().setDisable(true);
							this.getTextField_cuitProveedor().setDisable(true);
						} else {
							Alert alert = new Alert(AlertType.ERROR);
				  			alert.setTitle("ATENCION");
				  			alert.setHeaderText("La factura Nro: "+ this.getTxtFieldNroFactura().getText() +" esta definida sin remitos");
				  			alert.setContentText("Si desea asociar una factura ingrese un numero valido y presione ENTER");
				  			alert.showAndWait();
				  			
				  			this.getTextField_cuitProveedor().clear();
				  			this.getTextField_cuitProveedor().setDisable(false);
				  			this.getcBoxSeleccioneProvee().getSelectionModel().select(-1);
				  			this.getcBoxSeleccioneProvee().setDisable(false);
						}
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
			  			alert.setTitle("ATENCION");
			  			alert.setHeaderText("EL numero de factura indicado no esta registrado en el sistema");
			  			alert.setContentText("Se creara la factura nueva y se asociara el presente remito");
			  			alert.showAndWait();
			  			
			  			//a su vez, si ya habia ingresado un nro correcto y seteo combobox proveedor y deshabilito, tengo q volver a
			  			//habilitarlo desde aca
			  			this.getTextField_cuitProveedor().clear();
			  			this.getTextField_cuitProveedor().setDisable(false);
			  			this.getcBoxSeleccioneProvee().getSelectionModel().select(-1);
			  			this.getcBoxSeleccioneProvee().setDisable(false);
					}
				}	
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
	
	
	@FXML
    void enterTxtFieldCuitProveedor(KeyEvent event) {
    	try {
    		if (event.getCode().equals(KeyCode.ENTER)){
    	    	
        		if ((!(this.getTextField_cuitProveedor().getText().isEmpty())) && (!hayBlanco(this.getTextField_cuitProveedor().getText()))) {
        			Proveedor pro = CRUD.obtenerProveedorPorNroCuit(this.getTextField_cuitProveedor().getText());
        			if (pro != null) {
        				this.getcBoxSeleccioneProvee().getSelectionModel().select(pro.getNombreProveedor());
					} else {
						this.getcBoxSeleccioneProvee().getSelectionModel().select(-1);
					}
        			
    			} else {
    				this.getcBoxSeleccioneProvee().getSelectionModel().select(-1);
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
	
	
	public void llenarComboProveedores() {
		try {
			this.getcBoxSeleccioneProvee().getItems().clear();
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			listaProveedores = CRUD.obtenerListaProveedoresActivos();
	    		for (Proveedor proveedor : listaProveedores) {
	    			itemsCombo.add(proveedor.getNombreProveedor());
				}
	    	this.getcBoxSeleccioneProvee().setItems(itemsCombo);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void definirTipoDatoColumnas() {
		tablaDetalleRemito_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaDetalleRemito_Articulo.setCellValueFactory(cellData -> cellData.getValue().nroArticulo);
		tablaDetalleRemito_NroLote.setCellValueFactory(cellData -> cellData.getValue().insumo);       //NRO LOTE
		tablaDetalleRemito_UnidadMedida.setCellValueFactory(cellData -> cellData.getValue().unidadMedida);
		tablaDetalleRemito_Cantidad.setCellValueFactory(cellData -> cellData.getValue().cantidad);
		tablaDetalleRemito_Sector.setCellValueFactory(cellData -> cellData.getValue().nombreSector);
		tablaDetalleRemito_Categoria.setCellValueFactory(cellData -> cellData.getValue().nombreCategoria);
		tablaDetalleRemito_Vencimiento.setCellValueFactory(cellData -> cellData.getValue().fechaVencimiento);
		alinearContenidoColumnas();
	}

	
	private void alinearContenidoColumnas() {
    	tablaDetalleRemito_Insumo.setStyle("-fx-alignment: CENTER;");
    	tablaDetalleRemito_Articulo.setStyle("-fx-alignment: CENTER;");
    	tablaDetalleRemito_NroLote.setStyle("-fx-alignment: CENTER;");
    	tablaDetalleRemito_UnidadMedida.setStyle("-fx-alignment: CENTER;");
    	tablaDetalleRemito_Cantidad.setStyle("-fx-alignment: CENTER;");
    	tablaDetalleRemito_Sector.setStyle("-fx-alignment: CENTER;");
    	tablaDetalleRemito_Categoria.setStyle("-fx-alignment: CENTER;");
    	tablaDetalleRemito_Categoria.setStyle("-fx-alignment: CENTER;");
    	tablaDetalleRemito_Vencimiento.setStyle("-fx-alignment: CENTER;");
	}
	
	
	private void definirColumnasEditables() {
		tablaDetalleRemito_NroLote.setCellFactory(TextFieldTableCell.forTableColumn());
			
		//atributo necesario para convertir el tipo de dato de "cantidad (integer)" a string
		IntegerStringConverter convertirIaS = new IntegerStringConverter();
		tablaDetalleRemito_Cantidad.setCellFactory(TextFieldTableCell.<DetalleRemitoFX, Integer>forTableColumn(convertirIaS));
		
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
		tablaDetalleRemito_Vencimiento.setCellFactory(TextFieldTableCell.<DetalleRemitoFX, Date>forTableColumn(convertirDaS));
		
		controlarEventosCampos();
	}
	
	
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
		        datePickerFechaRemito.setDayCellFactory(dayCellFactory);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void setearToolTipTxtFields() {
		textField_cuitProveedor.setTooltip(new Tooltip("Luego de ingresar el CUIT" + "\n" 
				+ "Presione ENTER, para continuar"));
		
		txtFieldNroFactura.setTooltip(new Tooltip("Luego de ingresar el Nro Factura" + "\n" 
				+ "Presione ENTER, para continuar"));
		
		btnCancelar.setTooltip(new Tooltip("Limpia por completo la pantalla"));
	}
	
	
	public void limpiarCampos() {
		this.getcBoxSeleccioneProvee().getSelectionModel().select(-1);
		this.getcBoxSeleccioneProvee().setDisable(false);
		
		this.setearFechaCargaRemito();
		
		this.getTextAreaObservacion().clear();
		
		this.getTextField_cuitProveedor().clear();
		this.getTextField_cuitProveedor().setDisable(false);
		this.getTextField_cuitProveedor().setUnFocusColor(Color.web("#4d4d4d"));
		
		this.getTxtFieldNroRemito().clear();
		this.getTxtFieldNroRemito().setUnFocusColor(Color.web("#4d4d4d"));
		
		this.getTxtFieldNroFactura().clear();
		this.getTxtFieldNroFactura().setUnFocusColor(Color.web("#4d4d4d"));
		
		this.getDatePickerFechaRemito().setValue(null);
		this.getDatePickerFechaRemito().getEditor().clear();
		this.getDatePickerFechaRemito().setStyle("-fx-border-color: none;");
		
		obListDetalleRemito.clear();
		this.getTablaDetalleRemito().setItems(obListDetalleRemito);
		
		this.getBtnGuardar().setDisable(true);
		btnDeshacer.setDisable(true);
	}
	
	
	public void setearFechaCargaRemito() {
		Date fecha = new Date();
		String fechaCargaFactura = formato.format(fecha);
		lblFechaCargaRemito.setText(fechaCargaFactura);
	}
	
	
	//metodo q es llamado desde pant secundaria-insumos
   	public void habilitarBotones() {
   		btnGuardar.setDisable(false);
		tablaDetalleRemito.getSelectionModel().clearSelection();
		btnDeshacer.setDisable(true);
   	}
   	
   	
   	private void formatearFormatoFecha() {
   	  this.getDatePickerFechaRemito().setConverter(new StringConverter<LocalDate>() {
  	      String pattern = "dd-MM-yyyy";
  	      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

  	      {
  	          datePickerFechaRemito.setPromptText(pattern.toLowerCase());
  	      }

  	      @Override public String toString(LocalDate date) {
  	          if (date != null) {
  	              return dateFormatter.format(date);
  	          } else {
  	              return "";
  	          }
  	      }

  	      @Override public LocalDate fromString(String string) {
  	          if (string != null && !string.isEmpty()) {
  	              return LocalDate.parse(string, dateFormatter);
  	          } else {
  	              return null;
  	          }
  	      }
  	  });
    }
   	
   	
   	private void seleccionarFila(){
	//se le asigna una accion al click de cada fila de la tabla detalle
   		tablaDetalleRemito.setRowFactory( tv -> {
   			TableRow<DetalleRemitoFX> row = new TableRow<>();
   			row.setOnMouseClicked(event -> {
	    	
	        if (event.getButton() == MouseButton.PRIMARY){
	        	btnDeshacer.setDisable(false);
	        }
	    });
	    return row ;
		});
	}
	
   	
   	private boolean hayBlanco(String campo) {
		boolean hay = false;
		if (!campo.isEmpty() && campo.substring(0, 1).equals(" ")) {
			hay = true;
		}
		return hay;
	}
   	
   	
   	private void controlarEventosCampos() {
    	controlarEventosCampoNroLote();
    	controlarEventosCampoCantidad();
    	controlarEventosCampoVto();
	}
   	
   	
   	private void controlarEventosCampoNroLote() {
		tablaDetalleRemito_NroLote.setOnEditCommit(data -> {
    		String newNroLote = data.getNewValue();
    		String oldNroLote = data.getOldValue();
    	    
    		//verifico q no este vacio y luego q no tenga blancos
 		   if ((!(newNroLote.isEmpty()))) {
 			   
 			   if (newNroLote.substring(0, 1).equals(" ")) {
     			   mostrarMsjDialogoBlancoEnNroLote(oldNroLote);
     			  tablaDetalleRemito.getSelectionModel().clearSelection();
     		   
     		   } else {
 			   	//tengo q verificar q no ingrese un nro de lote q ya este cargado en el detalle, es decir, en las filas anteriores
 	    	    //y si mete un nro de lote q no esta en el detalle, pero si esta en la Bd, basicamente es actualizar el stock
 	    	    //q tiene ese insumo y precio, en caso de q lo cambie
	 	    	    
	 	    	   if (nroLoteYaAgregado(newNroLote)) {
	 	    		   mostrarMsjDialogoNroLoteYaAgregado2(newNroLote, oldNroLote);
	 	    		  tablaDetalleRemito.getSelectionModel().clearSelection();
	 	    		 
	 	    	   } else { //significa q el nuevo nroLote no estaba siendo usado en otra fila
	 	    		   
	 	    		//ya no es necesario verificar si el lote pertenece a otro tipo de insumo en la bd
	 	    		//xq existe tal posibilidad   
	 	    		  if (tablaDetalleRemito.getSelectionModel().getSelectedIndex() != -1) {
	 	    			 obListDetalleRemito.get(tablaDetalleRemito.getSelectionModel().getSelectedIndex()).insumo.set(newNroLote);
	 	    		  }
	 	    	   }	   
     		   }
    	    
		   } else { //osea el newNroLote esta en blanco
			   obListDetalleRemito.get(tablaDetalleRemito.getSelectionModel().getSelectedIndex()).insumo.set("");
			   tablaDetalleRemito.getSelectionModel().clearSelection();
			   btnDeshacer.setDisable(true);
		   }
    	    
    	});
	}
   	
   	
   	private void controlarEventosCampoCantidad() {

		tablaDetalleRemito_Cantidad.setOnEditCommit(data -> {
			
			Integer newCantidad = data.getNewValue();
    		Integer oldCantidad = data.getOldValue();
    	   
    	    if (newCantidad != oldCantidad) { //significa q las cambio, entonces seteo este nuevo valor en la listaObservable
    	    								//xq, luego cuando vuelva a meter un insumo nuevo, los campos q modifico al isumo anterior
    	    								//van aparecer con los valores q tenia x 1era vez
    	    	obListDetalleRemito.get(tablaDetalleRemito.getSelectionModel().getSelectedIndex()).cantidad.set(newCantidad);

    	    } 
    	 
    	});
	
	}
   	
   	
   	private void controlarEventosCampoVto() {

		tablaDetalleRemito_Vencimiento.setOnEditCommit(data -> {
			
			DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			
			String newFecha = null;
			
			if (data.getNewValue() != null) { //sino es blanco
				newFecha = sdf.format(data.getNewValue());
				//no necesito verificar si ingresa blancos, xq al decirle q la col vto va a aplicar el formato (dd-MM-yyyy)
				//x mas blancos q le ingrese solamente va a tomar en el enter los numeros q apliquen el formato
				
				//si todo correcto, verificar si la fecha ingresada completa no es
				//menor o igual a la actual
				Date fechaActual = new Date();
				if (data.getNewValue().compareTo(fechaActual) > 0) {  //osea la fecha ingresada es mayor a la actual
					obListDetalleRemito.get(tablaDetalleRemito.getSelectionModel().getSelectedIndex()).fechaVencimiento.set(data.getNewValue());
				} else {
					String fA = sdf.format(fechaActual);
					mostrarMsjDialogoErrorFechaVto("Completa", newFecha, null, fA);
					tablaDetalleRemito.getSelectionModel().clearSelection();
					btnDeshacer.setDisable(true);
				}
			}
		});
	}
   	
   	
   	private void mostrarMsjDialogoBlancoEnNroLote(String oldNroLote) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
		alert.setHeaderText("El Nro de Lote ingresado tiene errores");
		alert.setContentText("Asegurese de no utilizar espacios en el comienzo del Nro de Lote!");

		//alert.showAndWait();
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//le dejo el campo nroLote blanco
			obListDetalleRemito.get(tablaDetalleRemito.getSelectionModel().getSelectedIndex()).insumo.set("");
			actualizarTabla();
		}
	}
   	
   	
  //se encarga de actualizar la tabla, para cuando ingrese datos erroneos, x mas q esten borrados
	//sigue siendo visible lo ultimo escrito en la tupla.
	//de esta forma se evita tal error
	private void actualizarTabla() {
		//reseteo oblist aux, q gurda momentaneamente lo q contiene la tabla
		obListDetalleRemitoAux.clear();
		//luego hago la actualizacion de la tabla
		for (DetalleRemitoFX detalleRemitoFX : obListDetalleRemito) {
			obListDetalleRemitoAux.add(detalleRemitoFX);
		}
		obListDetalleRemito.clear();
		tablaDetalleRemito.setItems(obListDetalleRemito);
		for (DetalleRemitoFX detalleRemitoFX : obListDetalleRemitoAux) {
			obListDetalleRemito.add(detalleRemitoFX);
		}
		tablaDetalleRemito.setItems(obListDetalleRemito);
	}
	
	
	private boolean nroLoteYaAgregado(String newNroLote) {
		boolean agregado = false;
		Integer i = 0;
		try {
			while ((i < tablaDetalleRemito.getItems().size()) && (!(agregado))) {
				
				if (tablaDetalleRemito.getItems().get(i).insumo.get().equals(newNroLote) 
						&& (obListDetalleRemito.get(i).nombreInsumo.get().equals(tablaDetalleRemito.getSelectionModel().getSelectedItem().nombreInsumo.get()))
						&& (obListDetalleRemito.get(i).nroArticulo.get().equals(tablaDetalleRemito.getSelectionModel().getSelectedItem().nroArticulo.get()))) { //osea si la tabla tiene algun nroLote igual al nuevo q ingreso
					agregado = true;
				} else {
					i = i + 1;
				}
					
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return agregado;
	}
	
	
	private void mostrarMsjDialogoNroLoteYaAgregado2(String newNroLote, String oldNroLote) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
		alert.setHeaderText("El Nro de Lote ingresado ya se encuentra en uso");
		alert.setContentText("Asegurese de utilizar un Nro de Lote distinto!");

		//alert.showAndWait();
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
			obListDetalleRemito.get(tablaDetalleRemito.getSelectionModel().getSelectedIndex()).insumo.set("");
			actualizarTabla();
		}
	}
	
	
	private void mostrarMsjDialogoErrorFechaVto(String tipo, String valor, Integer anioActual, String fechaActual) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
		switch (tipo) {
		case "Dia":
			alert.setHeaderText("El Dia: " + valor);
			alert.setContentText("No puede ser mayor a 31!");
			break;

		case "Mes":
			alert.setHeaderText("El Mes: " + valor);
			alert.setContentText("No puede ser mayor a 12!");
			break;
			
		case "Anio":
			alert.setHeaderText("El Anio: " + valor);
			alert.setContentText("No puede ser menor al Anio actual: " + anioActual + " !");
			break;
		
		case "Completa":
			alert.setHeaderText("La fecha ingresada: " + valor);
			alert.setContentText("No puede ser menor o igual a la Fecha actual: " + fechaActual + " !");
			break;
			
		}

		//alert.showAndWait();
		Optional<ButtonType> result = alert.showAndWait();
		try {
			if (result.get() == ButtonType.OK){
				//le dejo el campo vto blanco
				obListDetalleRemito.get(tablaDetalleRemito.getSelectionModel().getSelectedIndex()).fechaVencimiento.set(null);
				actualizarTabla();
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}	
	}
	
	
	private boolean verificarIngresoDeDatos() {
		boolean noGuardar=true;
		Factura facturaBD = null;
		try {
			if (hayBlanco(this.getTxtFieldNroFactura().getText())){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("DATOS ERRONEOS");
				alert.setHeaderText("El campo Nro de Factura contiene datos erroneos");
				alert.setContentText("Si desea asociar una factura ingrese un numero valido y presione ENTER");
				alert.showAndWait();
				
				this.getTxtFieldNroFactura().setUnFocusColor(Color.RED);
				noGuardar=false;
			} else { //se verifica si ese nro factura (existe) y ademas, si permite o no la vinculacion con remitos
				facturaBD = CRUD.obtenerFacturaPorNroFactura(this.getTxtFieldNroFactura().getText());	
				if (facturaBD != null) {
					if (facturaBD.getTieneRemito().equals("No")) {
						Alert alert = new Alert(AlertType.ERROR);
			  			alert.setTitle("ATENCION");
			  			alert.setHeaderText("La factura Nro: "+ this.getTxtFieldNroFactura().getText() +" esta definida sin remitos");
			  			alert.setContentText("Si desea asociar una factura ingrese un numero valido y presione ENTER");
			  			alert.showAndWait();
			  			
			  			this.getTxtFieldNroFactura().setUnFocusColor(Color.RED);
						noGuardar=false;
					}
				}
			}
			if (this.getTxtFieldNroRemito().getText().isEmpty() || (hayBlanco(this.getTxtFieldNroRemito().getText()))) {
				this.getTxtFieldNroRemito().setUnFocusColor(Color.RED);
				noGuardar=false;
			} else {
				Remito remito = CRUD.obtenerRemitoPorNroRemito(this.getTxtFieldNroRemito().getText());
				if (remito != null) {
					Alert alert = new Alert(AlertType.INFORMATION);
		    		alert.setTitle("AVISO");
		    		alert.setHeaderText("Nro remito no disponible");
		    		alert.setContentText("El remito Nro: "+ remito.getNroRemito()+" ya se encuentra en uso");

		    		alert.showAndWait();
					this.getTxtFieldNroRemito().setUnFocusColor(Color.RED);
					noGuardar = false;
				}
			}
			if (this.getDatePickerFechaRemito().getValue() == null) {
				this.getDatePickerFechaRemito().setStyle("-fx-border-color: RED;");
				noGuardar=false;
			}
			if (this.getTextField_cuitProveedor().getText().isEmpty() || (hayBlanco(this.getTextField_cuitProveedor().getText()))) {
				this.getTextField_cuitProveedor().setUnFocusColor(Color.RED);
				noGuardar=false;
			} else { //verificar si el proveedor existe en la bd
				Proveedor pro = CRUD.obtenerProveedorPorNroCuit(this.getTextField_cuitProveedor().getText());
				if (pro == null) { //sino existe, setea el combo en (-1) seleccione
					this.getcBoxSeleccioneProvee().getSelectionModel().select(-1);
					this.getTextField_cuitProveedor().clear();
					this.getTextField_cuitProveedor().setUnFocusColor(Color.RED);
					noGuardar=false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return noGuardar;
	}
	
	
	private boolean datosEnTablaValidos() {
  		boolean validos = true;
  		Integer i = 0;
  		
  		while ((i < obListDetalleRemito.size()) && (validos)) {

  			if ((obListDetalleRemito.get(i).cantidad.get() == null)
  					|| (obListDetalleRemito.get(i).cantidad.get() == 0)) {
  				
  				validos = false;
  			} else {
  				i = i + 1;
  			}	
  		}
  		if (!(validos)) { 
  	    	Alert alert = new Alert(AlertType.INFORMATION);
  			alert.setTitle("AVISO");
  			alert.setHeaderText("Hay campos en el detalle del remito sin completar");
  			alert.setContentText("Asegurese de llenar todos los campos!");

  			alert.showAndWait();
  		}
  		return validos;
  	}
	
	
	private Insumo verificarInsumo(DetalleRemitoFX detalleRemitoFxIN) {
		boolean encontro = false;
		Insumo insumoAretornar = null;
		try {
			if (detalleRemitoFxIN.insumo.get().equals("")) {
				detalleRemitoFxIN.insumo.set("NA");
			}
			
//			List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorNroLote(detalleRemitofx.insumo.get());
			List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorLoteArticuloYReferencia(detalleRemitoFxIN.insumo.get(), 
																							detalleRemitoFxIN.nroArticulo.get(), 
																							detalleRemitoFxIN.nroReferencia);
		    
		    if (listaInsumos.isEmpty()) {
		    		//No se encontro insumo con ese lote, o sea que es nuevo en la base         
//		    		insumoAretornar = CRUD.loadInsumoPorNombreYCategoria(detalleRemitofx.nombreInsumo.get(), detalleRemitofx.idCategoria);
		    	insumoAretornar = CRUD.obtenerInsumoPorNombreIdCategoriaArticuloYReferencia(detalleRemitoFxIN.nombreInsumo.get(), detalleRemitoFxIN.idCategoria, 
																						detalleRemitoFxIN.nroArticulo.get(), detalleRemitoFxIN.nroReferencia);
	    		insumoAretornar.setNroLote(detalleRemitoFxIN.insumo.get());
	    		insumoAretornar.setStockActual(detalleRemitoFxIN.cantidad.get());
	    		insumoAretornar.setFechaVencimiento(detalleRemitoFxIN.fechaVencimiento.get());
	    		
	    	/***************************************************************************************/
				
				/*** asigno x defecto a atributo sucursal de insumo, la sucursal "Diagnos"  ***/
	    		insumoAretornar.setSucursal(CRUD.obtenerSucursal());
				
				/*** asigno a insumo el proveedor involucrado ***/
	    		insumoAretornar.setProveedor(this.getProveedorBD());
				
			/***************************************************************************************/
	    
	    		CRUD.guardarObjeto(insumoAretornar);	
	    		
			} else {
				//Si el lote existe, hay que verificar en que sector y categoria esta y actualizarlo
			    	for (Insumo insumo : listaInsumos) {
			    		
						if ((insumo.getNombreInsumo().equals(detalleRemitoFxIN.nombreInsumo.get()) 
								&& (insumo.getCategoria().getPkIdCategoria() == detalleRemitoFxIN.idCategoria))
								&& (insumo.getNroArticulo().equals(detalleRemitoFxIN.nroArticulo.get()))
            					&& (insumo.getReferencia().equals(detalleRemitoFxIN.nroReferencia))) {
							
						/////esto no es necesario ya, xq el query donde obtengo listaInsumos, retorna x cada insumos, todos sus atributos
                		////lo mismo q hace el query obtenerInsumoPorId1 (solo q este retorna un solo insumo)
//							insumoAretornar = CRUD.obtenerInsumoPorId1(insumo.getIdInsumo());
							insumoAretornar = insumo;
							insumoAretornar.setStockActual(insumoAretornar.getStockActual() + detalleRemitoFxIN.cantidad.get());
							insumoAretornar.setFechaVencimiento(detalleRemitoFxIN.fechaVencimiento.get());
							CRUD.actualizarObjeto(insumoAretornar);
							encontro = true;
							break;
						}
					}
			    	
			    	if (!encontro) {
			    		//si viene x aca significa q el lote si esta en la bd, pero lo deben estar usando otro/otros tipos de insumos
						//x lo q hay q darlo de alta para el nuevo tipo de insumo q ingreso
			    		insumoAretornar = CRUD.obtenerInsumoPorNombreIdCategoriaArticuloYReferencia(detalleRemitoFxIN.nombreInsumo.get(), detalleRemitoFxIN.idCategoria, 
																									detalleRemitoFxIN.nroArticulo.get(), detalleRemitoFxIN.nroReferencia);
						insumoAretornar.setStockActual(detalleRemitoFxIN.cantidad.get());
		    			insumoAretornar.setNroLote(detalleRemitoFxIN.insumo.get());
		    			insumoAretornar.setFechaVencimiento(detalleRemitoFxIN.fechaVencimiento.get());
		    			CRUD.guardarObjeto(insumoAretornar);	
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	    return insumoAretornar;
	}
	
	
}
