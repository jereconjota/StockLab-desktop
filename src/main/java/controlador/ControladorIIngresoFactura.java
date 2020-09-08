package controlador;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
//import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
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
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import main.AppMain;
import modelo.Categoria;
import modelo.DetalleFactura;
import modelo.DetalleFacturaId;
import modelo.DetalleRemito;
import modelo.DetalleRemitoId;
import modelo.Factura;
import modelo.Insumo;
import modelo.Proveedor;
import modelo.Remito;
import modeloAux.DetalleFacturaFX;
import modeloAux.InsumoFX;

public class ControladorIIngresoFactura {
	
	@FXML
    private Label lblFechaCargaFactura;

    @FXML
    private DatePicker datePickerFechaFactura;

	@FXML
    private TableView<DetalleFacturaFX> tablaDetalleFactura;

    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalleFactura_NroLote;

    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalleFactura_Insumo;

    @FXML
    private TableColumn<DetalleFacturaFX, Float> tablaDetalleFactura_Precio;

    @FXML
    private TableColumn<DetalleFacturaFX, Integer> tablaDetalleFactura_Cantidad;
    
    @FXML
    private TableColumn<DetalleFacturaFX, Float> tablaDetalleFactura_Importe;
    
    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalleFactura_UnidadMedida;
    
    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalleFactura_Articulo;
    
    @FXML
    private TableColumn<DetalleFacturaFX, Date> tablaDetalleFactura_vencimiento;
    
    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalleFactura_NroRemito;
    
    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalleFactura_Sector;
    
    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaDetalleFactura_Categoria;
    
    @FXML
    private JFXCheckBox checkBoxVerSecCat;

    @FXML
    private JFXTextField txtFieldNroFactura;    

    @FXML
    private JFXTextField txtFieldNroCUIT;

	@FXML
    private Label lblMsjErrorNroFactura;

    @FXML
    private Label lblSubtotal;
    
    @FXML
    private Label lblMsjErrorFechaFactura;
    
    @FXML
    private JFXButton btnGuardarIngreso;

    @FXML
    private JFXButton btnDeshacer;
    
    @FXML
    private JFXComboBox<String> cBoxTipoFactura;
    
    @FXML
    private JFXComboBox<String> cBoxSeleccioneProvee;
    
    @FXML
    private JFXButton btnBuscar;
    
    @FXML
    private Label lblMsjErrorCUIT;
    
//    @FXML
//    private JFXTextArea txtArea;
    
    @FXML
    private TextArea textAreaObservacion;
    
    @FXML
    private JFXButton btnAgregarRemito;
    
    @FXML
    private JFXTextField txtFieldAgregarRemito;
    
    @FXML
    private JFXButton btnCancelar;
    
    @FXML
    private RadioButton radioConRemito;

	@FXML
    private RadioButton radioSinRemito;
    
    private ObservableList<DetalleFacturaFX> obListDetalleFactura = FXCollections.observableArrayList();
    private ObservableList<DetalleFacturaFX> obListDetalleFacturaAux = FXCollections.observableArrayList();
 
    //list observable para llenar la tabla de insumos de la pantalla secundaria, para evitar
    //utilizar el metodo cargar insumos de esa pantalla secundaria y q no se ponga lenta
    private ObservableList<InsumoFX> obListInsumosPantSecundaria = FXCollections.observableArrayList(); 
    
  //para ENTER en Nro Cuit
    private ActionEvent e;
    
    private Proveedor proveedorBD;
    
    private boolean reactivoProveedor; //me sirve para saber si el proveedor q ingrese el user, si esta "suspendido"
    									//si lo reactiva "true" o no "false"
    
    private boolean seMostroMsjReactivar = false; //indica si se abrio el msj de dialogo de reactivar proveedor
    
    private Insumo insumoBD;
    
    private Factura newFactura; //sirve para almacenar los datos de los campos factura, hasta q finalmente se guardara en la BD
    	
    private boolean tieneRemito;
    
    private BorderPane iSecundariaInsumos;  //para tener referencia a la pantalla secundaria (la cual se usara a la hora de ir a pant alta insumo, y saber a donde volver)
    
    private ControladorISecundariaInsumosFactura controllerSecundariaInsumos;
    
    private BorderPane iPpalSecundaria;
    private ControladorIPpalSecundariaInsumos controllerPpalSecundariaInsumos;
    
    private List<String> listaNroRemitos = new ArrayList<>();
    
    private Remito remitoBD;
    
    private List<String> listaNroRemitosAux = new ArrayList<>();  //este es para saber si desde el check "tiene remito", tengo q borrar insumos de un remito q se agrego desde el txtfield agregar
   
    private List<String> listaNewInsumos = new ArrayList<>();   //sirve solamente para consistir el editado del campo nro remito
    
    private ToggleGroup group = new ToggleGroup();

    private Factura facturaBorrador;
    
    
    /**************************** CONSTRUCTOR *********************************/
    
    
    public ControladorIIngresoFactura() {
    	
	}
    
    
    /**************************** GET - SET **********************************/
    
    public ToggleGroup getGroup() {
		return group;
	}
    
    
    public JFXButton getBtnAgregarRemito() {
		return btnAgregarRemito;
	}


	public JFXTextField getTxtFieldAgregarRemito() {
		return txtFieldAgregarRemito;
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


	public BorderPane getiPpalSecundaria() {
		return iPpalSecundaria;
	}


	public BorderPane getiSecundariaInsumos() {
		return iSecundariaInsumos;
	}
	
	
	public ControladorISecundariaInsumosFactura getControllerSecundariaInsumos() {
		return controllerSecundariaInsumos;
	}
	

	public ObservableList<DetalleFacturaFX> getObListDetalleFactura() {
		return obListDetalleFactura;
	}

	public void setObListDetalleFactura(ObservableList<DetalleFacturaFX> obListDetalleFactura) {
		this.obListDetalleFactura = obListDetalleFactura;
	}


	public TableView<DetalleFacturaFX> getTablaDetalleFactura() {
		return tablaDetalleFactura;
	}

	
	public RadioButton getRadioConRemito() {
		return radioConRemito;
	}


	public RadioButton getRadioSinRemito() {
		return radioSinRemito;
	}


	private boolean getReactivoProveedor() {
		return reactivoProveedor;
	}

	private void setReactivoProveedor(boolean reactivoProveedor) {
		this.reactivoProveedor = reactivoProveedor;
	}

	
	public Proveedor getProveedorBD() {
		return proveedorBD;
	}

	public void setProveedorBD(Proveedor proveedorBD) {
		this.proveedorBD = proveedorBD;
	}


	public JFXComboBox<String> getcBoxSeleccioneProvee() {
		return cBoxSeleccioneProvee;
	}


	public JFXTextField getTxtFieldNroCUIT() {
		return txtFieldNroCUIT;
	}


	public JFXTextField getTxtFieldNroFactura() {
		return txtFieldNroFactura;
	}


	public Factura getNewFactura() {
		return newFactura;
	}

	public void setNewFactura(Factura newFactura) {
		this.newFactura = newFactura;
	}


	public Insumo getInsumoBD() {
		return insumoBD;
	}

	public void setInsumoBD(Insumo insumoBD) {
		this.insumoBD = insumoBD;
	}
	
	
	public Remito getRemitoBD() {
		return remitoBD;
	}

	public void setRemitoBD(Remito remitoBD) {
		this.remitoBD = remitoBD;
	}


	/********************************** METODOS ***********************************/
    
    @FXML
    private void initialize() {
    	llenarCombosBox();
    	restringirFechas();
    	definirTipoDatoColumnas();
    	definirColumnasEditables();
    	setearToolTipTxtFields();
    	seleccionarFila();
    }
    
    
	@FXML
    void conRemito() {
    	tieneRemito = true;
		tablaDetalleFactura_NroRemito.setVisible(true);
		//muestro agregar remito
		btnAgregarRemito.setVisible(true);
		txtFieldAgregarRemito.clear();
		txtFieldAgregarRemito.setVisible(true);
    }
	
	
    @FXML
    void sinRemito() {
    	tieneRemito = false;
		tablaDetalleFactura_NroRemito.setVisible(false);
		//oculto agregar remito
		btnAgregarRemito.setVisible(false);
		txtFieldAgregarRemito.setVisible(false);
		//y saco (si puso anteriormente) los insumos del remito, del detalle factura
		sacarRemitosDeDetalleFactura();
    }
    
    
  //se encarga de setear el campo txtField CUIT, de acuerdo a lo seleccionado en el
    //comboBox donde estan todos los provee del sistema (solo "activos")
	@FXML
    void eventoCBoxSeleccioneProvee() {
		try {
			//El evento del combo se activa cuando volvemos abrir la ventana ingresarFactura
			//por eso tenemos q checkear con este if para que no de error
			if (cBoxSeleccioneProvee.getSelectionModel().getSelectedIndex() != -1) {
				
				String nombreProvee = cBoxSeleccioneProvee.getSelectionModel().getSelectedItem();
	 
				Proveedor provee = CRUD.obtenerProveedorPorNombre(nombreProvee);
		
				txtFieldNroCUIT.clear();
				txtFieldNroCUIT.setText(provee.getNroCuit());
				lblMsjErrorCUIT.setVisible(false);
				txtFieldNroCUIT.setUnFocusColor(Color.web("#4d4d4d"));
				setearComboBoxProvee(provee.getNombreProveedor());
				this.setProveedorBD(provee); //para tener una referencia al proveedor, y luego completar los campos con sus datos
				
				if (this.getTxtFieldAgregarRemito().getText().equals("")) {
					
					if (provee.getTrabajaConRemito().equals("Si")) {
						this.getGroup().selectToggle(getRadioConRemito());
						conRemito();
					} else {
						this.getGroup().selectToggle(getRadioSinRemito());
						sinRemito();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
	@FXML
    void enterTxtFieldNroProveedor(KeyEvent event) {
    	if (event.getCode().equals(KeyCode.ENTER)){
    	//no es necesario verificar si mete "BLANCOS", xq tiene un txtfield custom, q solo toma numeros
    		//pero si es necesario q aprete "enter", con el campo vacio
    		if ((!(txtFieldNroCUIT.getText().isEmpty())) && (!hayBlanco(txtFieldNroCUIT.getText()))) {
			
    			if (estaProveedor(txtFieldNroCUIT.getText(), e)) {
    				
        			if (seMostroMsjReactivar) { //si se abrio el msj dialogo de reactivar
    					if (this.getReactivoProveedor()) { //verificar si decidio reactivar proveedor
    						setearComboBoxProvee(this.getProveedorBD().getNombreProveedor());  //si reactivo muestra campos
    					} else {
    						//reseteo boolean 
    						seMostroMsjReactivar = false;
    					}
    				} else { //si viene x aca, significa q no ingreso un proveedor suspendido, seria el camino de exito
    					setearComboBoxProvee(this.getProveedorBD().getNombreProveedor());
    				}
        			
    			} else {
    				//avisar con un cartel q no esta, y si desea darlo de alta
    				mostrarMensajeDialogoAlta("Proveedor");
    			}
			} else {
				this.getcBoxSeleccioneProvee().getSelectionModel().select(-1);
			}
		}
    }
    
    
	@FXML
    void enterTxtFieldNroFactura(KeyEvent event) {
    	if (event.getCode().equals(KeyCode.ENTER)) {
			//a este txtField si es necesario verificar q no meta blancos y algun nro de factura q este en uso
    		//y tambien verificar q aprete "enter" con el campo vacio
    		if (!(txtFieldNroFactura.getText().isEmpty())) {
			
    			if (nroFacturaValido(txtFieldNroFactura.getText())) {
        			
    				//limpio color y msj error de factura
    				txtFieldNroFactura.setUnFocusColor(Color.web("#4d4d4d"));
    				lblMsjErrorNroFactura.setVisible(false);
    			} 
			}
		}
    }
	
	
	// manejador de evento del datePicker fecha factura 
    @FXML
    void ocultarLblMsjError(ActionEvent event) {
    	formatearFormatoFecha();
    	lblMsjErrorFechaFactura.setVisible(false);
    }
	
	
    @FXML
    void deshacerUltimaAccion(ActionEvent event) {
    	//ya no va a sacar el ultimo de la tabla, ahora sacara de la tabla, aquel insumo q alla seleccionado
    	//y en caso de ser un insumo de un remito (q se agrego desde el Agregar") no dejara sacarlo
    	//a no ser q use el btn "cancelar", q limpiara toda la pantalla o usando el 
    	//tiene remito nuevamente para q saque solo los insumos de los remitos agregados
    	boolean sacar = true;
    	if (tieneRemito) {
			if (tablaDetalleFactura.getSelectionModel().getSelectedItem().nroRemito.get() != null) {
				for (String nroRemitos : listaNroRemitosAux) {
					if (tablaDetalleFactura.getSelectionModel().getSelectedItem().nroRemito.get().equals(nroRemitos)) {
						mostrarMsjSacarInsumoRemito(nroRemitos);
						tablaDetalleFactura.getSelectionModel().clearSelection();
						
			        	btnDeshacer.setDisable(true);
						sacar = false;
						break;
					}
				}
			}
		}
    	if (sacar) {
    		obListDetalleFactura.remove(tablaDetalleFactura.getSelectionModel().getSelectedIndex());
        	tablaDetalleFactura.setItems(obListDetalleFactura);
        	
        	tablaDetalleFactura.getSelectionModel().clearSelection();
        	
        	//y vuelvo a calcular el subtotal de los insumos q quedaron
        	calcularSubtotal();
        	
        	btnDeshacer.setDisable(true);
        	
        	//luego tengo q verificar q si no queda mas elementos en la tabla detalle 
        	//entonces deshabilito los botones guardar y deshacer
        	if (obListDetalleFactura.size() == 0) {
    			btnGuardarIngreso.setDisable(true);
    			lblSubtotal.setText(null);
    		}
		}
    }
	
	
  //se encarga de guardar por un lado "factura" y la tabla reclacion "detalle factura"
    //y tambien se tendra en cuanta si estara actualizando el stock de lote ya existente (osea ya estaba cargado en la BD)
    //si modifica el lote de un insumo (q ya tenia un lote definido)
    //o si esta creando nuevos lotes para un insumo (osea, el insumo existe en la Bd, pero no tiene asignado ningun lote)
    //		x lo q debo guardarlo en la tabla "insumo"
    //y el tema de "PRECIOS", sigo con duda!!!!!!!!!!!!!!!!!!!!!!
    @FXML
    void guardarFactura() {
    	
    	boolean encontrado = false;
    	
    	try {
    		/////DIALOGO DE CONFIRMACION//////
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		
    		if (this.getGroup().getSelectedToggle().equals(radioConRemito)) {
    			alert.setTitle("GUARDAR FACTURA");
        		alert.setHeaderText("FACTURA "+ this.getTxtFieldNroFactura().getText());
        		alert.setContentText("Factura CON REMITO\n"
        				+ "Solo se actualizara el Stock de los insumos involucrados en el/los remitos.");
			} else {
				alert.setTitle("GUARDAR FACTURA");
        		alert.setHeaderText("FACTURA "+ this.getTxtFieldNroFactura().getText());
        		alert.setContentText("Factura SIN REMITO\n"
        				+ "Se actualizara el Stock de TODOS los insumos involucrados en la factura.");
			}

    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.get() == ButtonType.OK){

	    		//1ero verificar q esten completos los datos de proveedor y factura
	    		
	    		if (camposProveeYfacCompletos()) {
	    			
	    			//luego verificar q no haya ninguna tupla vacia (nroLote-cantidad-precio-importe)
	    			//de esta forma evito q no actualize ni guarde algun objeto con valores en cero o blanco
	    			
	        		if (datosValidos()) {
	        			
	        			ControladorILogin.controllerPpal.mostrarProgressIndicator();           ///////////////////////////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	        			
	        			//como se q todos los campos del detalle factura son validos, tengo q guardar el
	        			//objeto "factura" en la Bd, para q de esa forma no alla error a la hs de 
	        			//guardar cada linea del detalle factura en la bd
	        			//ya q es una tabla relacion, y sino la clave de factura no esta ya almacenada en la Bd, daria error
	        			
	        			CRUD.guardarObjeto(this.getNewFactura());  //el newFactura, se setea en el metodo "camposProveeYfacCompletos()"
	        			
	        			if (!listaNroRemitosAux.isEmpty()) { //si entra, es xq se agregaron insumos desde un remito existente, ya sea del btn o txtField agregar remito
	        				
	        				for (String nroRemitoDeAgregar : listaNroRemitosAux) { 
	        					
	        					for (DetalleFacturaFX detalleFacturaFX : obListDetalleFactura) { //recorrer su listaObservable (es lo mismo q recorrer la tabla detalle)
	                    			
	        							//1ero me fijo si dicha fila, corresponde a un insumo de un remito q se agrego desde el txtField o btn agregar
	                    				//ya este ya se encuentra en la bd, el remito existe y su detalle tambien,
	                    				//solo tendria q guardar sobre el insumo el editado del campo precio e importe
	                    				//ya q estos campos no se pueden completar desde el ingreso remito
	                    				//en la lista "listaNroRemitosAux", contengo los nro remito q se agregaron desde el "agregar"
	                    				     						
	    						    if ((detalleFacturaFX.nroRemito.get() != null) && (detalleFacturaFX.nroRemito.get().equals(nroRemitoDeAgregar))) {
	        							//si entra entonces solo actualizo los valores de precio de dicho insumo
	            						//y guardo la fila en el detalle factura
	    						    	
	//    						    	CRUD.actualizarPrecioInsumoPorIdInsumo(detalleFacturaFX.idInsumo, detalleFacturaFX.precio.get());
	//    						    	
	//    						    	CRUD.actualizarFacturaEnRemitoPorIdRemito(detalleFacturaFX.idRemito, this.getNewFactura());
	    						    	
	    						    	Insumo insBD = CRUD.obtenerInsumoPorId1(detalleFacturaFX.idInsumo);
	            						insBD.setPrecioInsumo(detalleFacturaFX.precio.get());
	            						CRUD.actualizarObjeto(insBD);
	            						
	            						Remito remBD = CRUD.obtenerRemitoPorId(detalleFacturaFX.idRemito);
	            						remBD.setFactura(this.getNewFactura());
	            						CRUD.actualizarObjeto(remBD);
	    								
	            						guardarFilaDetalleFactura(insBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), remBD, "noGuardar");
	        						
	        						} else { //era un insumo q se agrego desde pantalla secundaria
	        							
	        							//antes q nada verificar si no completo el campo lote, x defecto se lo seteo a 
	                    				// "NA = no aplica"
	                    				//de esta forma evito llamar a la op crud con un dato en null o vacio
		
	        							if (detalleFacturaFX.insumo.get().equals("")) {
	            							detalleFacturaFX.insumo.set("NA");
	            						}
	                    				
	                    				
	                    				//1ero veo si el nroLote de una fila esta en la BD
	                    				//entonces traigo todos los insumos q tengan el lote q ingreso en el detalle
	                    				//si no llega a estar, deberia ir x el camino del catch
//	        							List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorNroLote(detalleFacturaFX.insumo.get());
	        							List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorLoteArticuloYReferencia(detalleFacturaFX.insumo.get(), 
																														detalleFacturaFX.nroArticulo.get(), 
																														detalleFacturaFX.nroReferencia);
	                    				
	                    				//me fijo si la lista esta null, si es asi significa q el lote no estaba en la bd
	                    				//entonces debo guardarlo
	                    				if (listaInsumos.isEmpty()) {
	                    					
	                    					//el metodo guardarNuevoInsumo, verifica si el check esta o no activado
	                    					//entonces:
	                    					//si no tiene remito (no activado) --> guarda insumo con el stock q puso en cantidad
	                    					//(activado) y col remito completo --> guarda insumo con el stock q puso en cantidad
	                    					//(activado) y col remito no completo --> guarda insumo con el stock en cero
	                    					//			(pero en el detalle figura la cantidad, pa cuando le completen el campo remito, se le asigne al stock actual)
	                    					
	                    					guardarNuevoInsumo(detalleFacturaFX);
	                    					
	                    					//luego si el check "tiene remito" esta seteado, guardar objeto remito y su detalle
	                        	    		if (tieneRemito) {
	                        	    			if (detalleFacturaFX.nroRemito.get() != null) { //osea, si en la fila el campo nro remito esta completo
	                        	    				crearRemito(detalleFacturaFX, insumoBD);
	                        	    				//ahora debo guardar cada linea del detalle en la BD
	                                	    		guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), this.getRemitoBD(), "guardarMov");
	            								} else {
	            									guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "noGuardar");
	            								}
	            							} else {
	            								guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "guardarMov");
	            							}
	                    					
	            						} else {
	            							
	            							for (Insumo insumo : listaInsumos) {
	            	                			//esta en la bd.. entonces actualizo su stock
	            	                			
//	            	                			if ((insumo.getNombreInsumo().equals(detalleFacturaFX.nombreInsumo.get()))
//	            	                					&& (insumo.getCategoria().getPkIdCategoria() == detalleFacturaFX.idCategoria)) {
	            								if ((insumo.getNombreInsumo().equals(detalleFacturaFX.nombreInsumo.get()))
	    	    	                					&& (insumo.getCategoria().getPkIdCategoria() == detalleFacturaFX.idCategoria)
	    	    	                					&& (insumo.getNroArticulo().equals(detalleFacturaFX.nroArticulo.get()))
	    	    	                					&& (insumo.getReferencia().equals(detalleFacturaFX.nroReferencia))
	    	    	                					&& (insumo.getSucursal().getPkIdSucursal() == 1)) {                  // '1' = sucursal Diagnos
	            	                				
//	            	                				this.setInsumoBD(CRUD.obtenerInsumoPorId1(insumo.getIdInsumo())); 	
	            									this.setInsumoBD(insumo);
	         	                				
	            	                				if (!(tieneRemito)) {
	            	                					//si el check "tiene remito" no esta seteado (no tiene remito)
	            	                					//entonces seteo el stock del insumo
	            	                					insumoBD.setStockActual(insumoBD.getStockActual() + detalleFacturaFX.cantidad.get());
	            	                				
	            	                				} else {	//si el check "tiene remito" esta seteado (tiene remito)
	            	                					
	            	                					//verifico si en la fila, el campo remito esta o no completo
	            	                					if (detalleFacturaFX.nroRemito.get() == null) {
	            	                						
	            	                						//si el campo remito esta vacio, entonces al insumo no le actualizo el stock
	            	                						insumoBD.setStockActual(insumoBD.getStockActual() + 0);
	            	                					} else {
	            	                						//pero si esta completo entonces, si le actualizo el stock
	            	                						insumoBD.setStockActual(insumoBD.getStockActual() + detalleFacturaFX.cantidad.get());
	            	                					}
	            	                				}
	            	                				insumoBD.setFechaVencimiento(detalleFacturaFX.fechaVencimiento.get());
	            	                	    		insumoBD.setPrecioInsumo(detalleFacturaFX.precio.get());
	            	                	    		
	            	                	    		insumoBD.setEstadoInsumo("Activo");
	            	                	    		
	            	                	    	/***************************************************************************************/
	    	    	                				
	    	    	                				/*** asigno x defecto a atributo sucursal de insumo, la sucursal "Diagnos"  ***/
//	    	    	                				insumoBD.setSucursal(CRUD.obtenerSucursal());
	    	    	                				
	    	    	                				/*** asigno a insumo el proveedor involucrado ***/
	    	    	                				insumoBD.setProveedor(this.getProveedorBD());
	    	    	                				
	    	    	                			/***************************************************************************************/
	            	                	    		
	            	                	    		CRUD.actualizarObjeto(insumoBD);
	            	                	    		
	            	                	    		if (tieneRemito) {
	            	                	    			if (detalleFacturaFX.nroRemito.get() != null) { //osea, si en la fila el campo nro remito esta completo
	            	                	    				crearRemito(detalleFacturaFX, insumoBD);
	            	                	    				//ahora debo guardar cada linea del detalle en la BD
	            	                        	    		guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), this.getRemitoBD(), "guardarMov");
	            	    								} else { //sino, el campo nro remito estaba vacio
	            	    									guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "noGuardar");
	            	    								}
	            	    							} else { //osea si no esta seteado el check "tiene remito"
	            	    								guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "guardarMov");
	            	    							}
	            	                	    		
	            	                	    		encontrado = true;
	            	                	    		
	            	        					} 
	            							}
	            	                		
	            	                		if (!(encontrado)) {
	            	                			//si viene x aca significa q el lote si esta en la bd, pero lo deben estar usando otro/otros tipos de insumos
	            	    						//x lo q hay q darlo de alta para el nuevo tipo de insumo q ingreso
	            	    						
	            	                			guardarNuevoInsumo(detalleFacturaFX);
	            	                			
	            	                			if (tieneRemito) {
	                            	    			if (detalleFacturaFX.nroRemito.get() != null) { //osea, si en la fila el campo nro remito esta completo
	                            	    				crearRemito(detalleFacturaFX, insumoBD);
	                            	    				//ahora debo guardar cada linea del detalle en la BD
	                                    	    		guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), this.getRemitoBD(), "guardarMov");
	                								} else { //sino, el campo nro remito estaba vacio
	                									guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "noGuardar");
	                								}
	                							} else {
	                								guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "guardarMov");
	                							}
	            	    					}
	            						}
	                    				
	                            		encontrado = false; //vuelvo a resetear el boolean para salir de la busqueda
	                            		
	        						}
	        					
	                    		}
	        					
							}
	        				
						} else { //si viene x aca, es xq no se agrego ningun remito existente desde el agregar remito (seria el camino principal)
							
							for (DetalleFacturaFX detalleFacturaFX : obListDetalleFactura) { //recorrer su listaObservable (es lo mismo q recorrer la tabla detalle)
	                				
								//antes q nada verificar si no completo el campo lote, x defecto se lo seteo a 
	            				// "NA = no aplica"
	            				//de esta forma evito llamar a la op crud con un dato en null o vacio
					
								if (detalleFacturaFX.insumo.get().equals("")) {
	    							detalleFacturaFX.insumo.set("NA");
	    						}
	            				
	            				//1ero veo si el nroLote de una fila esta en la BD
	            				//entonces traigo todos los insumos q tengan el lote q ingreso en el detalle
	            				//si no llega a estar, deberia ir x el camino del catch
								
//								List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorNroLote(detalleFacturaFX.insumo.get());
								
								List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorLoteArticuloYReferencia(detalleFacturaFX.insumo.get(), 
																												detalleFacturaFX.nroArticulo.get(), 
																												detalleFacturaFX.nroReferencia);
	            				
	            				//me fijo si la lista esta null, si es asi significa q el lote no estaba en la bd
	            				//entonces debo guardarlo
	            				if (listaInsumos.isEmpty()) {
	            					
	            					//el metodo guardarNuevoInsumo, verifica si el check esta o no activado
	            					//entonces:
	            					//si no tiene remito (no activado) --> guarda insumo con el stock q puso en cantidad
	            					//(activado) y col remito completo --> guarda insumo con el stock q puso en cantidad
	            					//(activado) y col remito no completo --> guarda insumo con el stock en cero
	            					//			(pero en el detalle figura la cantidad, pa cuando le completen el campo remito, se le asigne al stock actual)
	            					
	            					guardarNuevoInsumo(detalleFacturaFX);
	            					
	            					//luego si el check "tiene remito" esta seteado, guardar objeto remito y su detalle
	                	    		if (tieneRemito) {
	                	    			if (detalleFacturaFX.nroRemito.get() != null) { //osea, si en la fila el campo nro remito esta completo
	                	    				crearRemito(detalleFacturaFX, insumoBD);
	                	    				//ahora debo guardar cada linea del detalle en la BD
	                	    				guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), this.getRemitoBD(), "guardarMov");
	    								} else {
	    									guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "noGuardar");
	    								}
	    							} else {
	    								guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "guardarMov");
	    							}
	            					
	    						} else {
	    							
	    							for (Insumo insumo : listaInsumos) {
	    	                			
	    	                			//esta en la bd.. entonces actualizo su stock
	    	                			if ((insumo.getNombreInsumo().equals(detalleFacturaFX.nombreInsumo.get()))
	    	                					&& (insumo.getCategoria().getPkIdCategoria() == detalleFacturaFX.idCategoria)
	    	                					&& (insumo.getNroArticulo().equals(detalleFacturaFX.nroArticulo.get()))
	    	                					&& (insumo.getReferencia().equals(detalleFacturaFX.nroReferencia))
	    	                					&& (insumo.getSucursal().getPkIdSucursal() == 1)) {					// '1' = sucursal Diagnos
	    	                				
	    	                			/////esto no es necesario ya, xq el query donde obtengo listaInsumos, retorna x cada insumos, todos sus atributos
	    	                			////lo mismo q hace el query obtenerInsumoPorId1 (solo q este retorna un solo insumo)	
//	    	                				this.setInsumoBD(CRUD.obtenerInsumoPorId1(insumo.getIdInsumo()));  
	    	                				this.setInsumoBD(insumo);
	    	                				
	    	                				if (!(tieneRemito)) {
	    	                					//si el check "tiene remito" no esta seteado (no tiene remito)
	    	                					//entonces seteo el stock del insumo
	    	                					insumoBD.setStockActual(insumoBD.getStockActual() + detalleFacturaFX.cantidad.get());
	    	                				
	    	                				} else {	//si el check "tiene remito" esta seteado (tiene remito)
	    	                					//verifico si en la fila, el campo remito esta o no completo
	    	                					if (detalleFacturaFX.nroRemito.get() == null) {
	    	                						//si el campo remito esta vacio, entonces al insumo no le actualizo el stock
	    	                						insumoBD.setStockActual(insumoBD.getStockActual() + 0);
	    	                					} else {
	    	                						//pero si esta completo entonces, si le actualizo el stock
	    	                						insumoBD.setStockActual(insumoBD.getStockActual() + detalleFacturaFX.cantidad.get());
	    	                					}
	    	                				}
	    	                				insumoBD.setFechaVencimiento(detalleFacturaFX.fechaVencimiento.get());
	    	                	    		insumoBD.setPrecioInsumo(detalleFacturaFX.precio.get());
	    	                	    		
	    	                	    		insumoBD.setEstadoInsumo("Activo");
	    	                	    		
	    	                	    	/***************************************************************************************/
	    	                				
	    	                				/*** asigno x defecto a atributo sucursal de insumo, la sucursal "Diagnos"  ***/
//	    	                				insumoBD.setSucursal(CRUD.obtenerSucursal());
	    	                				
	    	                				/*** asigno a insumo el proveedor involucrado ***/
	    	                				insumoBD.setProveedor(this.getProveedorBD());
	    	                				
	    	                			/***************************************************************************************/
	    	                	    		
	    	                	    		CRUD.actualizarObjeto(insumoBD);
	    	                	    		
	    	                	    		if (tieneRemito) {
	    	                	    			if (detalleFacturaFX.nroRemito.get() != null) { //osea, si en la fila el campo nro remito esta completo
	    	                	    				crearRemito(detalleFacturaFX, insumoBD);
	    	                	    				//ahora debo guardar cada linea del detalle en la BD
	    	                        	    		guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), this.getRemitoBD(), "guardarMov");
	    	    								} else { //sino, el campo nro remito estaba vacio
	    	    									guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "noGuardar");
	    	    								}
	    	    							} else { //osea si no esta seteado el check "tiene remito"
	    	    								guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "guardarMov");
	    	    							}
	    	                	    
	    	                	    		encontrado = true;
	    	                	    	
	    	        					} 
	    							}
	    	                		
	    	                		if (!(encontrado)) {
	    	                			//si viene x aca significa q el lote si esta en la bd, pero lo deben estar usando otro/otros tipos de insumos
	    	    						//x lo q hay q darlo de alta para el nuevo tipo de insumo q ingreso
	    	                			guardarNuevoInsumo(detalleFacturaFX);
	    	                			
	    	                			if (tieneRemito) {
	                    	    			if (detalleFacturaFX.nroRemito.get() != null) { //osea, si en la fila el campo nro remito esta completo
	                    	    				crearRemito(detalleFacturaFX, insumoBD);
	                    	    				//ahora debo guardar cada linea del detalle en la BD
	                            	    		guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), this.getRemitoBD(), "guardarMov");
	        								} else { //sino, el campo nro remito estaba vacio
	        									guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "noGuardar");
	        								}
	        							} else {
	        								guardarFilaDetalleFactura(insumoBD, detalleFacturaFX.cantidad.get(), detalleFacturaFX.importe.get(), null, "guardarMov");
	        							}
	    	    					}
	    						}
	            				
	                    		encontrado = false; //vuelvo a resetear el boolean para salir de la busqueda
	                       	
	                		}
							
						}
	        			//luego de haber recorrido (si se recorrio) la lista de insumos q se agregaraon de un remito existente
	        			//y todas las filas del detalle factura y guardar cada una..
	        			//reseteo pantalla
	        			mostrarMsjGuardadoExitoso();
	        			
	        			limpiarCampos();
	        			
	        			ControladorILogin.controllerPpal.cerrarStageProgress(); 		//////////////////////////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	        		}
				}
    		
    		} else {
    		    // ... user chose CANCEL or closed the dialog
    		}
    		
    	} catch (Exception e) {		
			e.printStackTrace();
			e.getMessage();
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
        	iSecundariaInsumos = null;
    		controllerSecundariaInsumos = null;
    		
        	FXMLLoader loader2 = new FXMLLoader();
        	loader2.setLocation(AppMain.class.getResource("/vista/ISecundariaInsumos.fxml"));
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
    
    
  //se encarga de mostrar las columnas Sector y Categoria, en el detalle factura
  	//para un mayor control sobre los insumos q esta ingresando
  	@FXML
    void manejaEventoCheckBoxVerSecCat(ActionEvent event) {
  		if (checkBoxVerSecCat.isSelected()) {
  			tablaDetalleFactura_Sector.setVisible(true);
  			tablaDetalleFactura_Categoria.setVisible(true);
  		} else {
  			tablaDetalleFactura_Sector.setVisible(false);
  			tablaDetalleFactura_Categoria.setVisible(false);
  		}
    }
    
    
  	@FXML
    void enterAgregarRemito(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			buscarRemito();
		}
    }
	
  	
  //evento del btn agregar remito
  	@FXML
    void agregarRemito(ActionEvent event) {
  		buscarRemito();
    }
  	
  	
  	@FXML
    void eventoBtnCancelar(ActionEvent event) {
		limpiarCampos();
    }
  	
  	
    private void llenarCombosBox() {
		llenarComboTipoFactura();
		
		radioConRemito.setToggleGroup(group);
		radioSinRemito.setToggleGroup(group);
	}
	

	private void llenarComboTipoFactura() {
		ObservableList<String> itemsCombo = FXCollections.observableArrayList();
		itemsCombo.add("TIPO A");
		itemsCombo.add("TIPO B");
		itemsCombo.add("TIPO C");
		cBoxTipoFactura.setItems(itemsCombo);
	}
  	
  	
	//se encarga de restringar las fechas a seleccionar del datePicker fecha factura
	//ya q este solo puede tomar fechas q sean igual o menor a la fecha de ingreso remito	
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
		        datePickerFechaFactura.setDayCellFactory(dayCellFactory);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}        
	}
  	
	
	private void definirTipoDatoColumnas() {
		tablaDetalleFactura_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaDetalleFactura_Articulo.setCellValueFactory(cellData -> cellData.getValue().nroArticulo);
		tablaDetalleFactura_NroLote.setCellValueFactory(cellData -> cellData.getValue().insumo);       //NRO LOTE
		tablaDetalleFactura_UnidadMedida.setCellValueFactory(cellData -> cellData.getValue().unidadMedida);
		tablaDetalleFactura_Cantidad.setCellValueFactory(cellData -> cellData.getValue().cantidad);
		tablaDetalleFactura_Precio.setCellValueFactory(cellData -> cellData.getValue().precio);
		tablaDetalleFactura_Importe.setCellValueFactory(cellData -> cellData.getValue().importe);
		tablaDetalleFactura_vencimiento.setCellValueFactory(cellData -> cellData.getValue().fechaVencimiento);
		tablaDetalleFactura_NroRemito.setCellValueFactory(cellData -> cellData.getValue().nroRemito);
		tablaDetalleFactura_Sector.setCellValueFactory(cellData -> cellData.getValue().nombreSector);
		tablaDetalleFactura_Categoria.setCellValueFactory(cellData -> cellData.getValue().nombreCategoria);
		alinearContenidoColumnas();
	}


	private void alinearContenidoColumnas() {
		tablaDetalleFactura_Insumo.setStyle("-fx-alignment: CENTER;");
		tablaDetalleFactura_Articulo.setStyle("-fx-alignment: CENTER;");
		tablaDetalleFactura_NroLote.setStyle("-fx-alignment: CENTER;");
		tablaDetalleFactura_UnidadMedida.setStyle("-fx-alignment: CENTER;");
		tablaDetalleFactura_Cantidad.setStyle("-fx-alignment: CENTER;");
		tablaDetalleFactura_Precio.setStyle("-fx-alignment: CENTER-RIGHT;");
		tablaDetalleFactura_Importe.setStyle("-fx-alignment: CENTER-RIGHT;");
		tablaDetalleFactura_vencimiento.setStyle("-fx-alignment: CENTER;");
		tablaDetalleFactura_NroRemito.setStyle("-fx-alignment: CENTER;");
		tablaDetalleFactura_Sector.setStyle("-fx-alignment: CENTER;");
		tablaDetalleFactura_Categoria.setStyle("-fx-alignment: CENTER;");
	}
  	
	
	private void definirColumnasEditables() {
		
		tablaDetalleFactura_NroRemito.setCellFactory(TextFieldTableCell.forTableColumn());

		tablaDetalleFactura_NroLote.setCellFactory(TextFieldTableCell.forTableColumn());
		
		//atributo necesario para convertir el tipo de dato de "cantidad (integer)" a string
		IntegerStringConverter convertirIaS = new IntegerStringConverter();
		tablaDetalleFactura_Cantidad.setCellFactory(TextFieldTableCell.<DetalleFacturaFX, Integer>forTableColumn(convertirIaS));
		
		//atributo necesario para convertir el tipo de dato de "precio (float)" a string
		FloatStringConverter convertirFaS = new FloatStringConverter();
		tablaDetalleFactura_Precio.setCellFactory(TextFieldTableCell.<DetalleFacturaFX, Float>forTableColumn(convertirFaS)); 
		
		tablaDetalleFactura_Importe.setCellFactory(TextFieldTableCell.<DetalleFacturaFX, Float>forTableColumn(convertirFaS));
		
		//columna Vencimiento
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
		tablaDetalleFactura_vencimiento.setCellFactory(TextFieldTableCell.<DetalleFacturaFX, Date>forTableColumn(convertirDaS));

		controlarEventosCampos();
	}
	
	
	//se encarga de controlador el editado de los campos de cada fila q ingrese el user
    private void controlarEventosCampos() {
    	controlarEventosCampoNroLote();
    	
    	controlarEventosCampoVto();
    	
    	controlarEventosCampoCantidad();
    	
    	controlarEventosCampoPrecio();
    	
    	controlarEventosCampoImporte();
    	
    	controlarEventosCampoNroRemito();
	}
    
    
    private void controlarEventosCampoNroLote() {
    	try {
    		tablaDetalleFactura_NroLote.setOnEditCommit(data -> {
        		String newNroLote = data.getNewValue();
        		String oldNroLote = data.getOldValue();
        	    
        		//verifico q no este vacio y luego q no tenga blancos
     		   if ((!(newNroLote.isEmpty()))) {
     			   
     			   if (newNroLote.substring(0, 1).equals(" ")) {
         			   mostrarMsjDialogoBlancoEnNroLote(oldNroLote);
         			   tablaDetalleFactura.getSelectionModel().clearSelection();
         			  btnDeshacer.setDisable(true);
         		   } else {
         			
     			   	//tengo q verificar q no ingrese un nro de lote q ya este cargado en el detalle, es decir, en las filas anteriores
     	    	    //y si mete un nro de lote q no esta en el detalle, pero si esta en la Bd, basicamente es actualizar el stock
     	    	    //q tiene ese insumo y precio, en caso de q lo cambie
    	 	    	    
    	 	    	   if (nroLoteYaAgregado(newNroLote)) {
    	 	    		   mostrarMsjDialogoNroLoteYaAgregado2(newNroLote, oldNroLote);
    	 	    		  tablaDetalleFactura.getSelectionModel().clearSelection();
    	 	    		 btnDeshacer.setDisable(true);
    	 	    		 
    	 	    	   } else { //significa q el nuevo nroLote no estaba siendo usado en otra fila 
    	 	    		   
    	 	    		//ya no es necesario verificar si el lote pertenece a otro tipo de insumo en la bd
    	 	    		//xq existe tal posibilidad   
    	 	    		   if (tablaDetalleFactura.getSelectionModel().getSelectedIndex() != -1) {
    	 	    			   
    	 	    			  obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).insumo.set(newNroLote);
    	 	    		   
    	 	    		   }
//    	 	    		  obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).insumo.set(newNroLote);
    	 	    		   
    	 	    	   }	   
         		   }
        	    
    		   } else { //osea el newNroLote esta en blanco
    			   obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).insumo.set("");
    			   tablaDetalleFactura.getSelectionModel().clearSelection();
    			   btnDeshacer.setDisable(true);
    		   }
        	    
        	});	
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
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
			obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).insumo.set("");
			actualizarTabla();
		}
	}
    
    
  //se encarga de actualizar la tabla, para cuando ingrese datos erroneos, x mas q esten borrados
  	//sigue siendo visible lo ultimo escrito en la tupla.
  	//de esta forma se evita tal error
  	private void actualizarTabla() {
  		//reseteo oblist aux, q gurda momentaneamente lo q contiene la tabla
  		obListDetalleFacturaAux.clear();
  		//luego hago la actualizacion de la tabla
  		for (DetalleFacturaFX detalleFacturaFX : obListDetalleFactura) {
  			obListDetalleFacturaAux.add(detalleFacturaFX);
  		}
  		obListDetalleFactura.clear();
  		tablaDetalleFactura.setItems(obListDetalleFactura);
  		for (DetalleFacturaFX detalleFacturaFX : obListDetalleFacturaAux) {
  			obListDetalleFactura.add(detalleFacturaFX);
  		}
  		tablaDetalleFactura.setItems(obListDetalleFactura);
  	}
  	
  	
  	private boolean nroLoteYaAgregado(String newNroLote) {
		boolean agregado = false;
		Integer i = 0;
		try {
			while ((i < tablaDetalleFactura.getItems().size()) && (!(agregado))) {
				
//				if (tablaDetalleFactura.getItems().get(i).insumo.get().equals(newNroLote) && 
//			(obListDetalleFactura.get(i).nombreInsumo.get().equals(tablaDetalleFactura.getSelectionModel().getSelectedItem().nombreInsumo.get()))) { //osea si la tabla tiene algun nroLote igual al nuevo q ingreso
				if (tablaDetalleFactura.getItems().get(i).insumo.get().equals(newNroLote) 
						&& (obListDetalleFactura.get(i).nombreInsumo.get().equals(tablaDetalleFactura.getSelectionModel().getSelectedItem().nombreInsumo.get()))
						&& (obListDetalleFactura.get(i).nroArticulo.get().equals(tablaDetalleFactura.getSelectionModel().getSelectedItem().nroArticulo.get()))) { //osea si la tabla tiene algun nroLote igual al nuevo q ingreso
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
  	
  	
  //este me sirve para mostrar msj, cuando cambio el nro lote en la tabla, x lo necesito el limpiar campo	
  	private void mostrarMsjDialogoNroLoteYaAgregado2(String newNroLote, String oldNroLote) {
  		Alert alert = new Alert(AlertType.INFORMATION);
  		alert.setTitle("AVISO");
  		alert.setHeaderText("El Nro de Lote ingresado ya se encuentra en uso");
  		alert.setContentText("Asegurese de utilizar un Nro de Lote distinto!");

  		//alert.showAndWait();
  		Optional<ButtonType> result = alert.showAndWait();
  		
  		if (result.get() == ButtonType.OK){
  			obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).insumo.set("");
  			actualizarTabla();
  		}
  	}
  	
  	
  	private void controlarEventosCampoVto() {
		tablaDetalleFactura_vencimiento.setOnEditCommit(data -> {
			
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
					obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).fechaVencimiento.set(data.getNewValue());
				} else {
					String fA = sdf.format(fechaActual);
					mostrarMsjDialogoErrorFechaVto("Completa", newFecha, null, fA);
					tablaDetalleFactura.getSelectionModel().clearSelection();
					btnDeshacer.setDisable(true);
				}
			}
			
		});
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
				obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).fechaVencimiento.set(null);
				actualizarTabla();
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		
	}
  	
  	
  	private void controlarEventosCampoCantidad() {
		tablaDetalleFactura_Cantidad.setOnEditCommit(data -> {
			
			Integer newCantidad = data.getNewValue();
    		Integer oldCantidad = data.getOldValue();
    	   
    	    if (newCantidad != oldCantidad) { //significa q las cambio, entonces seteo este nuevo valor en la listaObservable
    	    								//xq, luego cuando vuelva a meter un insumo nuevo, los campos q modifico al isumo anterior
    	    								//van aparecer con los valores q tenia x 1era vez
    	    	obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).cantidad.set(newCantidad);
				//se lo seteo a esta listaObservable y no al DetalleFactura, xq todavia no se si guardara el ingreso
    	    	//entonces no toco los valores originales de la BD
    	    	
    	    	//luego setear importe con la nueva cantidad, no es necesario hacer el calculo con la cantidad 
    	    	// old, xq x defecto esta siempre es CERO
    	    	// "calculoCP = calculo Cantidad Precio"
    	    	//EL CALCULO YA NO ES NECESARIO!!! xq ahora ellos ingresan el importe
    	    	
    	    	Float calculoCP = (newCantidad) * (tablaDetalleFactura.getSelectionModel().getSelectedItem().precio.get());
        	    tablaDetalleFactura.getSelectionModel().getSelectedItem().importe.set(calculoCP);
        	    
        	    //aunq x mas q luego agregue nuevos insumos, este campo importe sigue con el valor del calculo,
        	    //x las dudas voy a setearle a la listaObservable el nuevo valor
        	    obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).importe.set(calculoCP);
        	    
        	    //luego hacer la sumatoria de los importes y mostrarla en el label SUBTOTAL
        	    calcularSubtotal();

    	    } 
    	 
    	});
	}
  	
  	
  //se encarga de sumar todos importe de todos los insumos, y setearlo en el label SUBTOTAL	
  	private void calcularSubtotal() {
  		Float subTotal = Float.valueOf("0");
  		for (int i = 0; i < tablaDetalleFactura.getItems().size(); i++) {
  			if (tablaDetalleFactura.getItems().get(i).importe.get() != null) {
  				subTotal = subTotal + tablaDetalleFactura.getItems().get(i).importe.get();
  			}
  		}
  		lblSubtotal.setText(String.valueOf(subTotal) + " $");
  	}
  	
  	
  	private void controlarEventosCampoPrecio() {
		tablaDetalleFactura_Precio.setOnEditCommit(data -> {
    		Float newPrecio = data.getNewValue();
    		Float oldPrecio = data.getOldValue();

    	    if (newPrecio != oldPrecio) {
				obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).precio.set(newPrecio);
        	    
				Float calculoCP = (newPrecio) * (tablaDetalleFactura.getSelectionModel().getSelectedItem().cantidad.get());
        	    tablaDetalleFactura.getSelectionModel().getSelectedItem().importe.set(calculoCP);
        	    
        	    obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).importe.set(calculoCP);
				
        	    calcularSubtotal();
		
    	    } //aca un sino no es necesario, osea para el caso de hacer el calculo para el "oldPrecio", ya q es el valor q
    	    	//viene x defecto del insumo, x lo tanto supongamos q no hace cambio en este campo y apreta "enter"
    	    	//pero el campo cantidad estaba en CERO, dejaria el importe en CERO
    	    	//entonces tendria q modificar el campo cantidad, y eso se resuelve arriba
    	});
	}
  	
  	
  	private void controlarEventosCampoImporte() {
		tablaDetalleFactura_Importe.setOnEditCommit(data -> {
    		Float newImporte = data.getNewValue();
    		Float oldImporte = data.getOldValue();

    	    if (newImporte != oldImporte) {
    	    	//antes de dejarlo cargar importe verifico q cant y precio esten completos
				obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).importe.set(newImporte);
        	    
        	    calcularSubtotal();
    	    } 
    	});
	}
  	
  	
  	private void controlarEventosCampoNroRemito() {
		tablaDetalleFactura_NroRemito.setOnEditCommit(data -> {
			String newRemito = data.getNewValue();
			//verifico q no este vacio y luego q no tenga blancos
	 		   if ((!(newRemito.isEmpty()))) {
	 			   
	 			   if (newRemito.substring(0, 1).equals(" ")) {
	     			   mostrarMsjDialogoBlancoEnRemito();
	     			  tablaDetalleFactura.getSelectionModel().clearSelection();
	     			  btnDeshacer.setDisable(true);
	     		   
	     		   } else {
	     			//hay q verificar q el nro ingreso no este ya cargado en la BD
	     			   if (nroRemitoEnUso(newRemito)) {
	     				  mostrarMsjNroRemitoEnUso(newRemito);
	     				 tablaDetalleFactura.getSelectionModel().clearSelection();
	     				btnDeshacer.setDisable(true);
	     			   } else {
	     				  obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).nroRemito.set(newRemito);
	     				 agregarNewNroRemitoAListaAux(newRemito);
	     			   }
	     		   }
	 		   } else { //osea el newRemito esta en blanco
	 			  obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).nroRemito.set(null);
	 			 tablaDetalleFactura.getSelectionModel().clearSelection();
	 			btnDeshacer.setDisable(true);
	 		   }
		});
	}
  	
  	
  	private void mostrarMsjDialogoBlancoEnRemito() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
		alert.setHeaderText("El Nro de Remito ingresado tiene errores");
		alert.setContentText("Asegurese de no utilizar espacios en el comienzo del Nro de Remito!");

		//alert.showAndWait();
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//le dejo el campo nroRemito blanco
			obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).nroRemito.set(null);
			actualizarTabla();
		}
	}
  	
  	
  	private boolean nroRemitoEnUso(String newRemito) {
		boolean enUso = true;
		try {
			Remito remBD = CRUD.obtenerRemitoPorNroRemito(newRemito);
			if (remBD == null) { //significa q no trajo nada de la consulta, xq no se encuentra dicho nroRemito en la BD
				enUso = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return enUso;
	}
  	
  	
  	private void mostrarMsjNroRemitoEnUso(String newRemito) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ATENCION");
		alert.setHeaderText("Numero remito no disponible" );
		alert.setContentText("El numero remito: "+ newRemito +" ya se encuentra en uso");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//le dejo el campo nroRemito blanco
			obListDetalleFactura.get(tablaDetalleFactura.getSelectionModel().getSelectedIndex()).nroRemito.set(null);
			actualizarTabla();
		}
	}
  	
  	
  	private void agregarNewNroRemitoAListaAux(String newRemito) {
		boolean esta = false;
		
		if (listaNewInsumos.isEmpty()) {
			
			listaNewInsumos.add(newRemito);
		} else {
			//verifico el nroRemito nuevo, para q no se guarde nuevamente el mismo nro
			for (String nroRemitoAux : listaNewInsumos) {
				if (nroRemitoAux.equals(newRemito)) {
					
					esta = true;
					break;
				}
			}
			if (!esta) {
				
				listaNewInsumos.add(newRemito);
			}
		}
	}
	
	
	private void setearToolTipTxtFields() {
		txtFieldNroCUIT.setTooltip(new Tooltip("Luego de ingresar el CUIT" + "\n" 
				+ "Presione ENTER, para continuar"));
		
		txtFieldNroFactura.setTooltip(new Tooltip("Luego de ingresar el Nro Factura" + "\n" 
				+ "Presione ENTER, para continuar"));
		
		btnCancelar.setTooltip(new Tooltip("Limpia por completo la pantalla"));
	}
	
	
	private void seleccionarFila(){
		//se le asigna una accion al click de cada fila de la tabla detalle
		tablaDetalleFactura.setRowFactory( tv -> {
		    TableRow<DetalleFacturaFX> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		    	
		        if (event.getButton() == MouseButton.PRIMARY){
		        	
		        	if (tablaDetalleFactura.getSelectionModel().isSelected(row.getIndex(), tablaDetalleFactura_NroRemito)) {
		        		DetalleFacturaFX rowData = row.getItem();
		        		
		        		//habilito btn Deshacer
		        		btnDeshacer.setDisable(false);
		        		
		        		//////////////////////////////////////////////////// CAMPO NRO REMITO
		        		if (this.getGroup().getSelectedToggle() == this.getRadioConRemito()) {

		        			if (rowData.nroRemito.get() == null) {
			        			//si el nroRemito de la fila seleccionada esta vacia
			        			//habilito la col para su edicion
			        			tablaDetalleFactura_NroRemito.setEditable(true);
			        			tablaDetalleFactura_NroLote.setEditable(true);
			        			tablaDetalleFactura_vencimiento.setEditable(true);
			        			tablaDetalleFactura_Cantidad.setEditable(true);
			        			tablaDetalleFactura_Precio.setEditable(true);
			        			tablaDetalleFactura_Importe.setEditable(true);
							} else {
								//verifico si el nro remito del campo es uno q ingreso recientemente, si es asi, x mas q este completo tiene q dejar editar
								//x si, se equivoco al completar ese nro remito
								if (nroRemitoEsNuevo(rowData.nroRemito.get())) {
									tablaDetalleFactura_NroRemito.setEditable(true);
									tablaDetalleFactura_NroLote.setEditable(true);
				        			tablaDetalleFactura_vencimiento.setEditable(true);
				        			tablaDetalleFactura_Cantidad.setEditable(true);
				        			tablaDetalleFactura_Precio.setEditable(true);
				        			tablaDetalleFactura_Importe.setEditable(true);
								} else {
									//sino, era un campo q ya vino completo desde la bd, entonces cancelo la edicion de la col nroRemito
									tablaDetalleFactura_NroRemito.setEditable(false);
									tablaDetalleFactura_NroLote.setEditable(false);
				        			tablaDetalleFactura_vencimiento.setEditable(false);
				        			tablaDetalleFactura_Cantidad.setEditable(false);
				        			//estos dos si los habilito pa editar, xq como vienen de un remito, estos valores no se setean en pantalla ingreso remito, xq no maneja precio e importe
				        			tablaDetalleFactura_Precio.setEditable(true);
				        			tablaDetalleFactura_Importe.setEditable(true);
								}
							}
						}		
					} else {
		        		tablaDetalleFactura.getSelectionModel().clearSelection();
					}
		        }
		    });
		    return row ;
			});
	}
	
	
	private boolean nroRemitoEsNuevo(String nroRemitoIN) {
		boolean eraNuevo = false;
		for (String nroRemitoAux : listaNewInsumos) {
			if (nroRemitoAux.equals(nroRemitoIN)) {
				eraNuevo = true;
				break;
			}
		}
		return eraNuevo;
	}
	
	
	public void limpiarCampos() {
		txtFieldNroCUIT.clear();
		txtFieldNroCUIT.setUnFocusColor(Color.web("#4d4d4d"));
		lblMsjErrorCUIT.setVisible(false);
		
		cBoxSeleccioneProvee.getSelectionModel().select(-1);
		cBoxSeleccioneProvee.setUnFocusColor(Color.web("#4d4d4d"));
		
		txtFieldNroFactura.clear();
		txtFieldNroFactura.setUnFocusColor(Color.web("#4d4d4d"));
		lblMsjErrorNroFactura.setVisible(false);
		
		limpiarCheckBoxs();
		
		limpiarRadioButtons();
		
		datePickerFechaFactura.getEditor().clear();
		datePickerFechaFactura.setValue(null);
		lblMsjErrorFechaFactura.setVisible(false);
		
		cBoxTipoFactura.setUnFocusColor(Color.web("#4d4d4d"));
		cBoxTipoFactura.getSelectionModel().select(-1);
		
		btnGuardarIngreso.setDisable(true);
		btnDeshacer.setDisable(true);
		
		obListDetalleFactura.clear();
		
		lblSubtotal.setText(null);
		
		//oculto col nro remito
		tablaDetalleFactura_NroRemito.setVisible(false);
		
		//oculto columnas sector y categoria
		tablaDetalleFactura_Sector.setVisible(false);
		tablaDetalleFactura_Categoria.setVisible(false);
		
		//oculto btn y txtField agregar remito
		ocultarAgregarRemito();
		
		//reseteo lista aux de insumos nuevos q se agregan al detalle
		listaNewInsumos.clear();
		
		//reseteo lista de nros remitos, q se agregan desde el btn o txtField "agregar"
		listaNroRemitosAux.clear();
		
		listaNroRemitos.clear();
		
		txtFieldAgregarRemito.setUnFocusColor(Color.web("#4d4d4d"));
		
		//limpio textArea
		textAreaObservacion.clear();
	}
	
	
	public void limpiarCheckBoxs() {
		tieneRemito = false;
		checkBoxVerSecCat.setSelected(false);
	}
	
	
	private void limpiarRadioButtons() {
		this.getGroup().selectToggle(null);
		tieneRemito = false;
	}
	
	
	public void ocultarAgregarRemito() {
		this.getBtnAgregarRemito().setVisible(false);
		this.getTxtFieldAgregarRemito().clear();
		this.getTxtFieldAgregarRemito().setVisible(false);
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
	
	
	public void setearFechaCarga() {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		Date fecha = new Date();
		String fechaCargaFactura = formato.format(fecha);
		lblFechaCargaFactura.setText(fechaCargaFactura);
	}
	
	
	//metodo q es llamado desde pant secundaria-insumos
	public void habilitarBotones() {
		btnGuardarIngreso.setDisable(false);
		tablaDetalleFactura.getSelectionModel().clearSelection();
		btnDeshacer.setDisable(true);
	}
	
	
	private void sacarRemitosDeDetalleFactura() {
		Integer i = 0;
		
		if (!listaNroRemitosAux.isEmpty()) {
			
			for (String nroRemito : listaNroRemitosAux) { //recorro la lista aux de nros de remitos q se agregaron al detalle
				
				while (i < obListDetalleFactura.size()) { //recorro la tabla detalle factura
					
					if (obListDetalleFactura.get(i).nroRemito.get() != null) { //esto es x si la fila tiene un insumo con el campo remito vacio
						
						if (obListDetalleFactura.get(i).nroRemito.get().equals(nroRemito)) {
							
							obListDetalleFactura.remove(obListDetalleFactura.get(i));
							i = 0;
							
						} else {
							i = i + 1;
						}
						
					} else {
						i = i + 1;
					}
					
				}
				i = 0;
			}
			
			tablaDetalleFactura.setItems(obListDetalleFactura);
			listaNroRemitosAux.clear();
		}
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
	
	
	private boolean hayBlanco(String campo) {
		boolean hay = false;

		if (campo.substring(0, 1).equals(" ")) {
			hay = true;
		}
		return hay;
	}
	
  	
	private boolean estaProveedor(String nroCuitIN, ActionEvent event) {
		boolean encontrado = false;
		try {
			
			Proveedor proveedor = CRUD.obtenerProveedorPorNroCuit(nroCuitIN);
			
			if (proveedor != null) { //entra si el proveedor existe en la bd
				
				if (proveedor.getEstadoProveedor().equals("Activo")) {
					this.setProveedorBD(proveedor); //para tener una referencia al proveedor, y luego completar los campos con sus datos
					encontrado = true;
				} else {
					//sino, significa q el proveedor esta "suspendido", pregunto si desea reactivar
					mostrarMsjDialogoReactivar(nroCuitIN, proveedor.getPkIdProveedor());
					seMostroMsjReactivar = true; //me sirve luego, para saber si mostrar o no los datos del proveedor suspendido (solo, si reactivo)
					encontrado = true;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return encontrado;
	}
	
	
	private void mostrarMsjDialogoReactivar(String nroCuitIN, Integer idProveedorIN) {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Error");
			alert.setHeaderText("El Proveedor solicitado se encuentra Suspendido");
			alert.setContentText("Desea Reactivarlo?");
	
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
			    // ... user chose OK
				
				Proveedor proveedorReactivar = CRUD.obtenerProveedorPorId(idProveedorIN); 
					
				proveedorReactivar.setEstadoProveedor("Activo");
				proveedorReactivar.setFechaBaja(null);
				CRUD.actualizarObjeto(proveedorReactivar);
				this.setProveedorBD(proveedorReactivar); //es la referencia al proveedor, para luego mostrar sus datos
				this.setReactivoProveedor(true); //boolean q sirve para saber si reactivo al proveedor, dicho caso luego mostrar sus datos en pantalla
				
			} else {
			    // ... user chose CANCEL or closed the dialog
				alert.close();
				this.setReactivoProveedor(false);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void mostrarMensajeDialogoAlta(String tipo) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Error");
		alert.setHeaderText("El " + tipo + " solicitado no se encuentra cargado");
		alert.setContentText("Desea darlo de Alta?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    // ... user chose OK
			
			switch (tipo) {
			case "Insumo":  /////////mostrar alta insumo
				
				mostrarAltaInsumo();
				
				break;

			case "Proveedor":  /////////mostrar alta proveedor
				
				mostrarAltaProveedor();
				
				break;
			}
			
		} else {
		    // ... user chose CANCEL or closed the dialog
			alert.close();
		}
	}
	
	
	private void mostrarAltaInsumo() {
		ControladorICsd_Principal.controllerAltaInsumo.setIngresoFactura(true); //es para avisar q voy al alta, desde "ingreso-remito" 
																								//y no de "admin"
		ControladorICsd_Principal.controllerAltaInsumo.getLblAltaInsumo().setVisible(true);
		ControladorICsd_Principal.controllerAltaInsumo.getLblModifInsumo().setVisible(false);
		ControladorICsd_Principal.controllerAltaInsumo.getBtnGuardarInsumo().setVisible(true);
		ControladorICsd_Principal.controllerAltaInsumo.getBtnGuardarModifInsumo().setVisible(false);
		ControladorICsd_Principal.controllerAltaInsumo.llenarComboSector();
		
		ControladorILogin.controllerPpal.getBorderPanePpal().setCenter(ControladorICsd_Principal.iAltaInsumo);
	}
	
	
	private void mostrarAltaProveedor() {
		ControladorICsd_Principal.controllerAltaProveedor.setIngresoFactura(true); //es para avisar q voy al alta, desde "ingreso-factura" 
		//y no de "admin"
		ControladorICsd_Principal.controllerAltaProveedor.getLblAltaProveedor().setVisible(true);
		ControladorICsd_Principal.controllerAltaProveedor.getLblModifProveedor().setVisible(false);
		ControladorICsd_Principal.controllerAltaProveedor.getBtnGuardarAltaProvee().setVisible(true);
		ControladorICsd_Principal.controllerAltaProveedor.getBtnGuardarModifProvee().setVisible(false);
		
		ControladorILogin.controllerPpal.getBorderPanePpal().setCenter(ControladorICsd_Principal.iAltaProveedor);
	}
	
	
	//este metodo se usa, cuando se da de alta un proveedor, desde la pantalla ingreso-remito
	public void llenarCamposProveedor(Proveedor nuevoProveedor) {
		ControladorILogin.controllerPpal.mostrarPantIngresoFactura();
		txtFieldNroCUIT.setText(nuevoProveedor.getNroCuit());
		setearComboBoxProvee(nuevoProveedor.getNombreProveedor());
	}
	
	
	private boolean nroFacturaValido(String nroFacturaIN) {
		boolean valido = false;
		
		if (!(hayBlanco(nroFacturaIN))) {
			
			if (!(nroFacturaEnUso(nroFacturaIN))) {
				valido = true;
			} else {
				txtFieldNroFactura.setUnFocusColor(Color.RED);
				lblMsjErrorNroFactura.setText("Ya se encuentra en uso");
				lblMsjErrorNroFactura.setVisible(true);
			}
			
		} else { 
			txtFieldNroFactura.setUnFocusColor(Color.RED);
			lblMsjErrorNroFactura.setText("No se admite blancos antes del Nro de Factura");
			lblMsjErrorNroFactura.setVisible(true);
		}
		
		return valido;
	}
	
	
	private boolean nroFacturaEnUso(String nroFacturaIN) {
		boolean enUso = false;
		try {
			Factura factura = CRUD.obtenerFacturaPorNroFactura(nroFacturaIN);
			
			if (factura != null) {
				enUso = true;
			}
				
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return enUso;
	}
	
	
	//consiste en aplicar al datePicker, el formato de fecha "dia-mes-anio"
    private void formatearFormatoFecha() {
    	datePickerFechaFactura.setConverter(new StringConverter<LocalDate>() {
   	     String pattern = "dd-MM-yyyy";
   	     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

   	     {
   	         datePickerFechaFactura.setPromptText(pattern.toLowerCase());
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
    
    
    private void mostrarMsjSacarInsumoRemito(String nroRemitoIN) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("CONFIRMACION");
		alert.setHeaderText("El insumo seleccionado pertenece a un Remito existente");
		alert.setContentText("No se puede sacar a menos que quiera retirar todos" + "\n" + "Desea sacar todos los insumos asociado al Remito " + nroRemitoIN + "?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			sacarInsumosRemitoDeDetalleFactura(nroRemitoIN);
			
			calcularSubtotal();
			
			if (obListDetalleFactura.size() == 0) {
    			btnGuardarIngreso.setDisable(true);
    			lblSubtotal.setText(null);
    		}
		}
	}
    
    
    private void sacarInsumosRemitoDeDetalleFactura(String nroRemitoIN) {
		Integer i = 0;
			
		while (i < obListDetalleFactura.size()) { //recorro la tabla detalle factura
			
			if (obListDetalleFactura.get(i).nroRemito.get() != null) { //esto es x si la fila tiene un insumo con el campo remito vacio
				
				if (obListDetalleFactura.get(i).nroRemito.get().equals(nroRemitoIN)) {
					
					obListDetalleFactura.remove(obListDetalleFactura.get(i));
					i = 0;
					
				} else {
					i = i + 1;
				}
				
			} else {
				i = i + 1;
			}
			
		}
		
		tablaDetalleFactura.setItems(obListDetalleFactura);
		//luego saco el nro remito aux, de la lista aux de nros remitos
		listaNroRemitosAux.remove(nroRemitoIN);
	}
    
    
  //busco remito:
  	//1-si el fk factura en remito es null, entonces si lleno la tabla detalle fac
  	//2-si esta pero tiene completa el fk factura, no deja agregar
  	//3-si no existe aviso
  	private void buscarRemito() {
  		boolean seguir = true;		
  		try {
  			this.getTxtFieldAgregarRemito().setUnFocusColor(Color.web("#4d4d4d"));
  			//verificar q no este vacio, blanco
  			if (!txtFieldAgregarRemito.getText().isEmpty()) {
  				
  				if (!hayBlanco(txtFieldAgregarRemito.getText())) {
  					
  					//luego verifico q no quiera ingresar nuevamente el mismo remito al detalle
  					if (!listaNroRemitosAux.isEmpty()) {
  						for (String nroRemitoAux : listaNroRemitosAux) {
  							if (nroRemitoAux.equals(txtFieldAgregarRemito.getText())) { //si entra es xq quiere agregar de nuevo el mismo remito
  								seguir = false;
  								break;
  							}
  						}
  					}
  					
  					if ((listaNroRemitosAux.isEmpty()) || (seguir)) {
  						
  						Remito remBD = CRUD.obtenerRemitoPorNroRemito(txtFieldAgregarRemito.getText());
  						
  						if (remBD != null) {
  							
  							if (remBD.getFactura() == null) {
  								
  								//del query de abajo solo obtengo del proveedor su cuit y nombre, no necesito los demas datos (x lo q van a estar en null)
  								Proveedor proveedorRemito = CRUD.obtenerProveedorDeUnDetalleRemitoPorIdRemito(remBD.getIdRemito());
  								
  								//Si ya eligio proveedor, y luego agrega un remito, el proveedor del remito tiene que coincidir con el elegido
  								//sino, alguno esta mal
  								if (!(this.getcBoxSeleccioneProvee().getSelectionModel().getSelectedIndex()==-1)){
  										if (this.getTxtFieldNroCUIT().getText().equals(proveedorRemito.getNroCuit())) {
  											//guardo su nro en una lista auxiliar, para luego si vuelve a setear 
  											//el check "tiene remito", tengo q sacar los insumos de dicho remito q agrego
  											guardarReferenciaNroRemito(remBD.getNroRemito());
  											
  											//tengo q traer su detalle, y agregarlo al detalle factura
  											buscarYagreagarDetalleRemitoAdetalleFactura(remBD);
  											
  											//luego habilito botones guardar y deshacer (si ya estan habilitados no pasa nada)
  											habilitarBotones();
  										}else {
  											mostrarMsjErrorAgregarRemito("proveedor", txtFieldAgregarRemito.getText());
  										}
  								}else {
  									//guardo su nro en una lista auxiliar, para luego si vuelve a setear 
  									//el check "tiene remito", tengo q sacar los insumos de dicho remito q agrego
  									guardarReferenciaNroRemito(remBD.getNroRemito());
  									
  									//tengo q traer su detalle, y agregarlo al detalle factura
  									buscarYagreagarDetalleRemitoAdetalleFactura(remBD);
  									
  									//luego habilito botones guardar y deshacer (si ya estan habilitados no pasa nada)
  									habilitarBotones();
  								}
  								
  								
  							} else {
  								mostrarMsjErrorAgregarRemito("en uso", txtFieldAgregarRemito.getText());
  							}
  							
  						} else {
  							mostrarMsjErrorAgregarRemito("no esta", txtFieldAgregarRemito.getText());
  						}
  						
  					} else {
  						mostrarMsjErrorAgregarRemito("agregado", txtFieldAgregarRemito.getText());
  					}
  					
  				} else {
  					mostrarMsjErrorAgregarRemito("blanco", null);
  				}
  				
  			}else {
  				this.getTxtFieldAgregarRemito().setUnFocusColor(Color.web("RED"));
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  			e.getMessage();
  		}
  	}
  	
  	
  	private void guardarReferenciaNroRemito(String nroRemito) {
		boolean esta = false;
		if (listaNroRemitosAux.isEmpty()) {
			listaNroRemitosAux.add(nroRemito);
		} else {
			//verifico el nroRemito nuevo, para q no se guarde nuevamente el mismo nro
			for (String nroRemitoAux : listaNroRemitosAux) {
				if (nroRemitoAux.equals(nroRemito)) {
					esta = true;
					break;
				}
			}
			if (!esta) {
				listaNroRemitosAux.add(nroRemito);
			}
		}
	}
  	
  	
  	private void buscarYagreagarDetalleRemitoAdetalleFactura(Remito remBD) {
		try {
			
			List<DetalleRemito> listaDetallesRemito = CRUD.obtenerListaDetalleRemitoPorIdRemito2(remBD.getIdRemito());
			
			for (DetalleRemito detalleRemito : listaDetallesRemito) {
				
				DetalleFacturaFX dFacturaFX = new DetalleFacturaFX();
				
				dFacturaFX.nroRemito.set(remBD.getNroRemito());
				dFacturaFX.nombreInsumo.set(detalleRemito.getNombreInsumo());
				dFacturaFX.nroArticulo.set(detalleRemito.getNroArticulo());
				dFacturaFX.insumo.set(detalleRemito.getNroLote());
				dFacturaFX.fechaVencimiento.set(detalleRemito.getFechaVtoInsumo());
				dFacturaFX.unidadMedida.set(detalleRemito.getUnidadMedida());
				dFacturaFX.nombreSector.set(detalleRemito.getNombreSector());
				dFacturaFX.nombreCategoria.set(detalleRemito.getNombreCategoria());
				dFacturaFX.idCategoria = detalleRemito.getIdCategoria();
				dFacturaFX.cantidad.set(detalleRemito.getCantidad());
				dFacturaFX.precio.set(Float.valueOf("0"));
				dFacturaFX.importe.set(Float.valueOf("0"));
				dFacturaFX.idInsumo = detalleRemito.getInsumo().getIdInsumo();
				dFacturaFX.idRemito = detalleRemito.getRemito().getIdRemito();
				obListDetalleFactura.add(dFacturaFX);
			}
			tablaDetalleFactura.setItems(obListDetalleFactura);
			//luego completo los campos de proveedor
			completarCampoProveedor(listaDetallesRemito.get(0).getNroCuit(), listaDetallesRemito.get(0).getNombreProveedor());
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
  	
  	private void completarCampoProveedor(String nroCuit, String nombre) {
		txtFieldNroCUIT.setText(nroCuit);
		cBoxSeleccioneProvee.getSelectionModel().select(nombre);
	}
  	
  	
  	private void mostrarMsjErrorAgregarRemito(String tipo, String nroRemitoIN) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ATENCION");
		switch (tipo) {
		case "blanco":
			alert.setHeaderText("Numero remito con errores" );
			alert.setContentText("No se admite blancos antes del Numero de Remito");
			break;

		case "en uso":
			alert.setHeaderText("Remito en uso" );
			alert.setContentText("El remito: " + nroRemitoIN + " ya se encuentra vinculado a otra factura");
			break;
		
		case "no esta":
			alert.setHeaderText("Remito inexistente");
			alert.setContentText("El remito: " + nroRemitoIN + " no se encuentra cargado en el sistema");
			break;
			
		case "agregado":
			alert.setHeaderText("El remito no se puede agregar");
			alert.setContentText("El remito: " + nroRemitoIN + " ya se encuentra agregado al Detalle Factura");
			break;
		case "proveedor":
			alert.setHeaderText("El proveedor del remito no coincide con el de factura");
			alert.setContentText("El remito: " + nroRemitoIN + " esta asociado a un proveedor distinto al de\nla factura que quiere relacionar, corrija donde corresponda");
			break;
		}
		alert.showAndWait();
		this.getTxtFieldAgregarRemito().setUnFocusColor(Color.web("RED"));
		this.getTablaDetalleFactura().requestFocus();
	}
  	
  	
  //verifico si los campos del proveedor y factura estan completos
    private boolean camposProveeYfacCompletos() {
		boolean completos = false;
		
		if (proveeCompleto()) {
			
			if (facCompleta()) {
				
				//creo objeto factura, y guardo referencia
				//pero antes, debo verificar si ese nro fac, esta siendo usado
				//dicho caso, aviso q no es posible
				//esto es para controlar, en caso de q no haya apretado "enter" cuando ingreso el nro fac
				if (!(nroFacturaEnUso(txtFieldNroFactura.getText()))) {
					crearFactura();
					completos = true;
				} else {
					txtFieldNroFactura.setUnFocusColor(Color.RED);
					lblMsjErrorNroFactura.setText("Ya se encuentra en uso");
					lblMsjErrorNroFactura.setVisible(true);
				}
			}
		}
		return completos;
	}
  	
  	
    private boolean proveeCompleto() {
		boolean completo = false;
		if (txtFieldNroCUIT.getText().isEmpty()) {          //ESTA VACIO?
			lblMsjErrorCUIT.setText("Campo obligatorio");
			lblMsjErrorCUIT.setVisible(true);
			txtFieldNroCUIT.setUnFocusColor(Color.RED);
		} else {
			if (hayBlanco(txtFieldNroCUIT.getText())) {    //TIENE BLANCOS?
				lblMsjErrorCUIT.setText("No se admite blancos antes del Nro de CUIT");
				lblMsjErrorCUIT.setVisible(true);
				txtFieldNroCUIT.setUnFocusColor(Color.RED);
			} else {
				//vuelvo a limpiar msj error y color del txtField(x si antes hubo un error)
				txtFieldNroCUIT.setUnFocusColor(Color.web("#4d4d4d"));
				lblMsjErrorCUIT.setVisible(false);
				
				if (cBoxSeleccioneProvee.getSelectionModel().getSelectedIndex() == - 1) {  //COMBO SIN SELECCION?
					cBoxSeleccioneProvee.setUnFocusColor(Color.RED);
				} else {
					//vuelvo a limpiar color del comboProvee (x si antes hubo un error)
					cBoxSeleccioneProvee.setUnFocusColor(Color.web("#4d4d4d"));
					
					completo = true;
				}
			}
		}
		return completo;
	}
  	
  	
    private boolean facCompleta() {
		boolean completo = false;
		if (txtFieldNroFactura.getText().isEmpty()) {         //ESTA VACIO?
			lblMsjErrorNroFactura.setText("Campo obligatorio");
			lblMsjErrorNroFactura.setVisible(true);
			txtFieldNroFactura.setUnFocusColor(Color.RED);
		} else {
			if (hayBlanco(txtFieldNroFactura.getText())) {         //TIENE BLANCOS?
				lblMsjErrorNroFactura.setText("No se admite blancos antes del Nro de Factura");
				lblMsjErrorNroFactura.setVisible(true);
				txtFieldNroFactura.setUnFocusColor(Color.RED);
			} else {
				//vuelvo a limpiar msj error y color del txtField(x si antes hubo un error)
				txtFieldNroFactura.setUnFocusColor(Color.web("#4d4d4d"));
				lblMsjErrorNroFactura.setVisible(false);
				
				if (cBoxTipoFactura.getSelectionModel().getSelectedIndex() == -1) {           //COMBO SIN SELECCION?
					cBoxTipoFactura.setUnFocusColor(Color.RED);
				} else {
					//vuelvo a limpiar color del comboTipoFac (x si antes hubo un error)
					cBoxTipoFactura.setUnFocusColor(Color.web("#4d4d4d"));
					
					if (datePickerFechaFactura.getEditor().getText().isEmpty()) {			//FECHA FAC SIN SELECCIONAR?
						lblMsjErrorFechaFactura.setVisible(true);
					} else {
						//vuelvo a limpiar msj error fecha fac (x si antes hubo un error)
						lblMsjErrorFechaFactura.setVisible(false);
						
						completo = true;
					}
				}
			}
		}
		return completo;
	}
    
    
    private void crearFactura() {
		Date fechaCarga = null;
		Date feCarga = null;
		Factura factura = null;
		String tieneRem = null;
		try { //conversion de String a Date
			
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");  //formato q maneja el mysql
			SimpleDateFormat formato2 = new SimpleDateFormat("dd-MM-yyyy");
			feCarga = formato2.parse(lblFechaCargaFactura.getText());
			String feCar = formato.format(feCarga);
			fechaCarga = formato.parse(feCar);
			Date fechaFacturaPicker = java.sql.Date.valueOf(datePickerFechaFactura.getValue());  //conversion de LocalDate a Date
			String fechaFacPicker = formato.format(fechaFacturaPicker);
			Date fechaFactura = formato.parse(fechaFacPicker);	//basicamente, le aplique el formato "yyyy-MM-dd" al datepicker para q lo reconosca el mysql
			String tipoFactura = cBoxTipoFactura.getSelectionModel().getSelectedItem();
			String observacion = "Sin comentarios";
	    	
	    	if (!textAreaObservacion.getText().isEmpty()) { //osea q si escribieron algo en observaciones
				observacion = textAreaObservacion.getText();
			}
	    	
	    	if (tieneRemito) {
	    		tieneRem = "Si";
			} else {
				tieneRem = "No";
			}

	    	factura = new Factura(txtFieldNroFactura.getText(), fechaFactura, tipoFactura, fechaCarga, observacion, tieneRem);
			
			//reseteo el newFactura
			this.setNewFactura(null);
			this.setNewFactura(factura); //necesito dejar una referencia a esta factura, para luego usarla a la hora de hacer el guardar detalle factura final
		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
  //verifico si algun campo de todas las filas del detalle factura, esta vacio o con Ceros
  	private boolean datosValidos() {
  		boolean validos = true;
  		Integer i = 0;
  		
  		while ((i < obListDetalleFactura.size()) && (validos)) {

  			if ((obListDetalleFactura.get(i).cantidad.get() == null)
  					|| (obListDetalleFactura.get(i).cantidad.get() == 0)
  					|| (obListDetalleFactura.get(i).precio.get() == null)
  					|| (obListDetalleFactura.get(i).precio.get() == Float.valueOf("0"))
  					|| (obListDetalleFactura.get(i).importe.get() == null)
  					|| (obListDetalleFactura.get(i).importe.get() == Float.valueOf("0"))) {
  				
  				validos = false;
  			} else {
  				i = i + 1;
  			}
  			
  		}
  		
  		if (!(validos)) { 
  	    	Alert alert = new Alert(AlertType.INFORMATION);
  			alert.setTitle("AVISO");
  			alert.setHeaderText("Hay campos en el Detalle Factura sin completar");
  			alert.setContentText("Asegurese de llenar todos los campos!");

  			alert.showAndWait();
  		}
  		
  		return validos;
  	}
  	
  	
  //se encarga de guardar cada linea del detalle factura en la bd
    //el argumento "insumoIN", es q se actualizo previamente (ya sea su cantidad, o precio)
    //el argumento "numDetalle", es para identificar a q linea se refiere del detalle
    //el argumento "cantidad", contiene lo q se incremento en el detalle..(no es la cantidad total del insumo)
    //el argumento "importe", antes era calculable, ahora como es ingresado, debo guardarlo en el objeto detalle
    //el argumento "tipo", sirve para saber si tengo q guardar o no el movimiento
    //	ya q si el check "tiene remito":
    //		-no seteado: entonces guarda movimiento (xq se actualiza o guarda nuevo stock)
    //		-seteado: --campo remito:
    //						|-(completo): guarda movimiento (xq se actualiza o guarda nuevo stock)
    //						|-(no completo): no guarda movimiento (xq x mas q ponga una cantidad no se sumara al stock q tenia, y si no tenia
    //																su stock figurara en cero..x lo tanto el movimiento no se guarda para no 
    //																causar confusion)
    private void guardarFilaDetalleFactura(Insumo insumoIN, Integer cantidad, Float importe, Remito remitoIN, String tipo) { 
    	try {
    		DetalleFacturaId id = new DetalleFacturaId(this.getNewFactura().getIdFactura());
        	
        	Float precio = insumoIN.getPrecioInsumo();
        	
        	DetalleFactura dF = new DetalleFactura(id, this.getNewFactura(), insumoIN, this.getProveedorBD(), remitoIN, cantidad, precio, importe);
    	
        	CRUD.guardarObjeto(dF);
        	
        	String observacion = "Sin comentarios";
        	
        	if (!textAreaObservacion.getText().isEmpty()) { //osea q si escribieron algo en observaciones
    			observacion = textAreaObservacion.getText();
    		}
        	
        	//luego tengo q guardar cada linea detalle, en la tabla movimientos.
        	//el argumento "Diagnos", hace referencia a la "SUCURSAL"
        	
        	if (tipo.equals("guardarMov")) {
        		ControladorICsd_Principal.controllerTablaMovimientos.guardarMovimientoDesdeRemito(insumoIN, cantidad, "Diagnos", observacion);
    		}

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
  //se encarga de guardar nuevo insumo, q se agrego con anterioridad en alguna fila del detalle factura
    //el argumento "detalleFacturaFX", basicamente serian los datos q contiene una fila
    private void guardarNuevoInsumo(DetalleFacturaFX detalleFacturaFxIN) {
    	try {
//			EN VEZ DE TRAER TODOS LOS INSUMOS DE LA BD, Y RECORRER HASTA ENCONTRAR AL INSUMO X SU NOBRE
//			Q YA NO ALCANZA SOLO CON COINCIDIR ESE DATO.. TRAIGO LOS DATOS DE UN SOLO INSUMO
//			DONDE COINCIDA NOMBRE INSUMO-CATEGORIA

//    	Insumo insBD = CRUD.obtenerInsumoPorNombreEIdCategoria(detalleFacturaFxIN.nombreInsumo.get(), detalleFacturaFxIN.idCategoria);
    		
    		Insumo insBD = CRUD.obtenerInsumoPorNombreIdCategoriaArticuloYReferencia(detalleFacturaFxIN.nombreInsumo.get(), detalleFacturaFxIN.idCategoria, 
    																				detalleFacturaFxIN.nroArticulo.get(), 
    																				detalleFacturaFxIN.nroReferencia);
    		
	    	//copio los datos de ese insumo.. y luego le seteo los valores
			//q cambian..q son los datos q tengo en la fila detalle
	
			Categoria categoria = insBD.getCategoria();
			String nombre = insBD.getNombreInsumo();
			String nroArticulo = insBD.getNroArticulo();
			String referencia = insBD.getReferencia();
			String temperatura = insBD.getTemperatura();
			String unidadMedida = insBD.getUnidadMedida();
			String estado = insBD.getEstadoInsumo();
	//		String ubicacion = insBD.getUbicacion();
			int pdp = insBD.getPdp();
			Date fechaIngreso = new Date();
			Date fechaVto = detalleFacturaFxIN.fechaVencimiento.get();
							
			Insumo newInsumo = new Insumo(categoria, nombre, nroArticulo, referencia, fechaVto, temperatura, unidadMedida, pdp, fechaIngreso, estado);
	//		Insumo newInsumo = new Insumo(categoria, nombre, nroArticulo, referencia, fechaVto, temperatura, unidadMedida, pdp, fechaIngreso, estado, ubicacion);
	
			newInsumo.setNroLote(detalleFacturaFxIN.insumo.get());
	    	
			if (!(tieneRemito)) {
				//si el check "tiene remito" no esta seteado (no tiene remito)
				//entonces seteo el stock del insumo
				newInsumo.setStockActual(detalleFacturaFxIN.cantidad.get());
			} 
			//si el check "tiene remito" esta seteado (tiene remito)
			else {
				//verifico si en la fila, el campo remito esta o no completo
				if (detalleFacturaFxIN.nroRemito.get() == null) {
					//si el campo remito esta vacio, entonces al insumo le seteo stock en cero
					newInsumo.setStockActual(0);
				} else {
					//pero si esta completo entonces, si le asigno al insumo la cantidad q puso en dicha fila
					newInsumo.setStockActual(detalleFacturaFxIN.cantidad.get());
				}
			}
			
			newInsumo.setPrecioInsumo(detalleFacturaFxIN.precio.get());
			
		/***************************************************************************************/
			
			/*** asigno x defecto a atributo sucursal de insumo, la sucursal "Diagnos"  ***/
			newInsumo.setSucursal(CRUD.obtenerSucursal());
			
			/*** asigno a insumo el proveedor involucrado ***/
			newInsumo.setProveedor(this.getProveedorBD());
			
		/***************************************************************************************/	
			CRUD.guardarObjeto(newInsumo);
			
			//luego guardo referencia del nuevo insumo, para usarlo en el guardarDetalle
			this.setInsumoBD(newInsumo);
		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}	
	}
    
    
    private void crearRemito(DetalleFacturaFX detalleFacturaFX, Insumo insumoBD) {
    	Remito nuevoRemito = null;
    	boolean iguales = true;
    	String observacion = null;
    	try {
    		//reseteo el objeto remitoBD, q utilizo para guardar referencias a los nuevos objetos creados
    		this.setRemitoBD(null);
    		
    		if (listaNroRemitos.isEmpty()) { //si entra es xq es el 1er remito a guardar (1er fila del detalle factura)
    			
    			//1ero creo el objeto remito
    			if (textAreaObservacion.getText().isEmpty()) {
    				observacion = "Sin comentarios";
				} else {
					observacion = textAreaObservacion.getText();
				}
    			
    			nuevoRemito = new Remito(detalleFacturaFX.nroRemito.get(), this.getNewFactura(), this.getNewFactura().getFechaFactura(), 
    										this.getNewFactura().getFechaCarga(), observacion);
    			CRUD.guardarObjeto(nuevoRemito);
    			
    			//guardo referencia al nuevo objeto remito, para luego usarlo en el detalle factura
    			this.setRemitoBD(nuevoRemito);
    			
    			//luego el detalle
    			DetalleRemitoId id = new DetalleRemitoId(nuevoRemito.getIdRemito());
    			DetalleRemito dR = new DetalleRemito(id, this.getInsumoBD(), this.getProveedorBD(), nuevoRemito, detalleFacturaFX.cantidad.get());
    			CRUD.guardarObjeto(dR);
    			
    			//guardo en una lista, el nro remito, para luego cuando siga con la siguiente fila, si los nro remito son diferentes
    			//tengo q crear nuevo objeto remito y su detalle
    			//pero si tienen igual nro remito, solo tengo q asignarle un nuevo detalle
    			listaNroRemitos.add(nuevoRemito.getNroRemito());
    		
    		} else {	//significa q la lista ya tiene algun nro remito guardado
    			
    			for (String nroRemito : listaNroRemitos) { //recorro la lista en donde almaceno los nro remito para compararlos con los del detalle factura
    				if (detalleFacturaFX.nroRemito.get().equals(nroRemito)) {
						break;
					} else {
						iguales = false;
						break;
					}
    			}
    			
    			if (iguales) { //iguales nro remito, entonces solo le asigno un nuevo detalle al remito q ya se encuentra guardado en la bd
    				
    				Remito remBD = CRUD.obtenerRemitoPorNroRemito(detalleFacturaFX.nroRemito.get());
    				
    				//guardo referencia al nuevo objeto remito, para luego usarlo en el detalle factura
        			this.setRemitoBD(remBD);
					
					//luego el detalle
        			DetalleRemitoId id = new DetalleRemitoId(remBD.getIdRemito());
	    			DetalleRemito dR = new DetalleRemito(id, this.getInsumoBD(), this.getProveedorBD(), remBD, detalleFacturaFX.cantidad.get());
	    			CRUD.guardarObjeto(dR);
				
    			} else { //son diferentes entonces guardo nuevo remito y su detalle
    				
    				if (textAreaObservacion.getText().isEmpty()) {
        				observacion = "Sin comentarios";
    				} else {
    					observacion = textAreaObservacion.getText();
    				}
    				
    				nuevoRemito = new Remito(detalleFacturaFX.nroRemito.get(), this.getNewFactura(), this.getNewFactura().getFechaFactura(), 
    											this.getNewFactura().getFechaCarga(), observacion);
	    			CRUD.guardarObjeto(nuevoRemito);
	    			
	    			//guardo referencia al nuevo objeto remito, para luego usarlo en el detalle factura
	    			this.setRemitoBD(nuevoRemito);
	    			
	    			//luego el detalle
	    			DetalleRemitoId id = new DetalleRemitoId(nuevoRemito.getIdRemito());
	    			DetalleRemito dR = new DetalleRemito(id, this.getInsumoBD(), this.getProveedorBD(), nuevoRemito, detalleFacturaFX.cantidad.get());
	    			CRUD.guardarObjeto(dR);
	    			
	    			listaNroRemitos.add(nuevoRemito.getNroRemito());
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
  	
  	
    private void mostrarMsjGuardadoExitoso() {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
    	alert.setHeaderText("Factura creada");
		alert.setContentText("La factura Nro: "+ txtFieldNroFactura.getText() +" ha sido guardada satisfactoriamente");
		alert.showAndWait();
	}

}
