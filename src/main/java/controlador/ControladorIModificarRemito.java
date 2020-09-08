package controlador;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import hibernate.util.CRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;
import modelo.DetalleFactura;
import modelo.DetalleFacturaId;
import modelo.DetalleRemito;
import modelo.Factura;
import modelo.Insumo;
import modelo.Proveedor;
import modelo.Remito;
import modeloAux.DetalleRemitoFX;
import modeloAux.RemitoFX;

public class ControladorIModificarRemito {
	
	@FXML
    private JFXTextArea textAreaObservacion;

    @FXML
    private Label lblFechaCargaRemito;

    @FXML
    private JFXTextField textField_cuitProveedor;

    @FXML
    private JFXTextField textField_NroFactura;

    @FXML
    private JFXCheckBox checkBoxVerSecCat;

    @FXML
    private TableView<DetalleRemitoFX> tablaDetalleRemito;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_Insumo;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_Articulo;

    @FXML
    private TableColumn<DetalleRemitoFX, Integer> tablaDetalleRemito_Cantidad;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_unidadDeMedida;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_NroDeLote;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_Categoria;

    @FXML
    private TableColumn<DetalleRemitoFX, String> tablaDetalleRemito_Sector;

    @FXML
    private TableColumn<DetalleRemitoFX, Date> tablaDetalleRemito_Vencimiento;
    
    @FXML
    private JFXComboBox<String> cBoxSeleccioneProvee;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private DatePicker datePicker_fechaDeRemito;

    @FXML
    private JFXTextField textField_BuscarNroDeRemito;
	
	@FXML
    private JFXButton btnBuscarRemito;
	
    @FXML
    private JFXTextField textField_nroRemito;
	
    public  ObservableList<DetalleRemitoFX> obListDetalleRemito = FXCollections.observableArrayList();
    
    private List<Proveedor> listaProveedores;
    
    private Proveedor proveedorBD;
    
    private Remito remitoBD;
    
    private RemitoFX remitoAmodificar;
    
    boolean desdeVerRemitos;

	List<DetalleRemito> detalleDeRemito;
    
	SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

	private boolean hayCambios = false;
	
	private Factura facturaBD;
	
	private boolean correcto;
	
	
	/**************************** CONSTRUCTOR *********************************/
	
	public ControladorIModificarRemito() {
		
	}
	
	
	/**************************** GET - SET **********************************/
    
    
	public JFXTextArea getTextAreaObservacion() {
		return textAreaObservacion;
	}


	public Label getLblFechaCargaRemito() {
		return lblFechaCargaRemito;
	}


	public JFXTextField getTextField_cuitProveedor() {
		return textField_cuitProveedor;
	}


	public JFXTextField getTextField_NroFactura() {
		return textField_NroFactura;
	}


	public TableView<DetalleRemitoFX> getTablaDetalleRemito() {
		return tablaDetalleRemito;
	}


	public JFXComboBox<String> getcBoxSeleccioneProvee() {
		return cBoxSeleccioneProvee;
	}


	public DatePicker getDatePicker_fechaDeRemito() {
		return datePicker_fechaDeRemito;
	}


	public JFXTextField getTextField_BuscarNroDeRemito() {
		return textField_BuscarNroDeRemito;
	}


	public JFXTextField getTextField_nroRemito() {
		return textField_nroRemito;
	}
    
    
    public void setDesdeVerRemitos(boolean desdeVerRemitos) {
		this.desdeVerRemitos = desdeVerRemitos;
	}


	public void setProveedorBD(Proveedor proveedorBD) {
		this.proveedorBD = proveedorBD;
	}


	public JFXButton getBtnGuardar() {
		return btnGuardar;
	}

	
	public RemitoFX getRemitoAmodificar() {
		return remitoAmodificar;
	}


	/********************************** METODOS ***********************************/
	
	@FXML
    void initialize() {
		definirTipoDatoColumnas();
//		llenarComboProveedores();		////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!! lo llamo desde controlador ppal
		restringirFechas();
    }
	

	@FXML
    void cancelar(ActionEvent event) {
		try {
			if (desdeVerRemitos) {
				this.getcBoxSeleccioneProvee().getSelectionModel().select(-1);
				SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
				
				this.getLblFechaCargaRemito().setText(formato.format(remitoAmodificar.fechaCarga.get()));
				
				this.getTextField_cuitProveedor().setText(remitoAmodificar.nroCuit.get());
				
//				Proveedor proveedorAux = CRUD.obtenerProveedorPorNroCuit(remitoAmodificar.nroCuit.get());
//				this.getcBoxSeleccioneProvee().getSelectionModel().select(proveedorAux.getNombreProveedor());
				this.getcBoxSeleccioneProvee().getSelectionModel().select(remitoAmodificar.nombreProveedor.get());
				this.getTextField_BuscarNroDeRemito().clear();
				
				this.getTextField_NroFactura().setText(remitoAmodificar.factura.get());;
				this.getDatePicker_fechaDeRemito().getEditor().setText(formato.format(remitoAmodificar.fechaRemito.get()));

				this.getTablaDetalleRemito().setItems(obListDetalleRemito);
				this.getTextField_nroRemito().setText(remitoAmodificar.nroRemito.get());
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("AVISO");
				alert.setHeaderText("NO SE REALIZARON CAMBIOS" );
				alert.setContentText("El remito Nro: "+ remitoAmodificar.nroRemito.get() +" no sufrio cambios.");
				alert.showAndWait();
				
			} else {	
				limpiarCampos();
				deshabilitarCampos();
				deshabilitarBotones();
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
	
	
	@FXML
    void eventoCBoxSeleccioneProvee(ActionEvent event) {
    	if (this.getcBoxSeleccioneProvee().getSelectionModel().getSelectedIndex() != -1) {
    		for (Proveedor proveedor : listaProveedores) {
        		if (this.getcBoxSeleccioneProvee().getSelectionModel().getSelectedItem().equals(proveedor.getNombreProveedor())) {
        			this.getTextField_cuitProveedor().setText(proveedor.getNroCuit());
        			this.setProveedorBD(proveedor);;
        			break;
        		}
    		}    
    	}
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
    void buscarRemito() {
		obListDetalleRemito.clear();
		this.setDesdeVerRemitos(false); //reseteo de nuevo este flag, xq si vino desde el ver remitos, y depue busca un nuevo remito desde el buscar y cancela (evita error)
		try {
			//volvemos a habilitar campo nro factura
			this.getTextField_NroFactura().setDisable(false);
			this.getTextField_NroFactura().clear();
			
			if (this.getTextField_BuscarNroDeRemito().getText().isEmpty() || this.getTextField_BuscarNroDeRemito().getText().substring(0,1).equals(" ")) {
    			this.getTextField_BuscarNroDeRemito().setUnFocusColor(Color.RED);
    			this.getTextField_BuscarNroDeRemito().clear();
				} else {
					this.getTextField_BuscarNroDeRemito().setUnFocusColor(Color.web("#4d4d4d"));
    				remitoAmodificar = null;
    				final Remito remito = CRUD.obtenerRemitoPorNroRemito(this.getTextField_BuscarNroDeRemito().getText());							
    				if (remito == null) {
    					Alert alert = new Alert(AlertType.INFORMATION);
    					alert.setTitle("ATENCION!");
    					alert.setHeaderText("REMITO NO ENCONTRADO" );
    					alert.setContentText("El remito con numero: "+ this.getTextField_BuscarNroDeRemito().getText() +" no se encuentra registrado");
    					alert.showAndWait();
    					this.getTextField_BuscarNroDeRemito().clear();
    					this.limpiarCampos();
    					this.deshabilitarCampos();
    					this.deshabilitarBotones();
    				} else {
						remitoAmodificar = new RemitoFX(remito);
						this.getLblFechaCargaRemito().setText(formato.format(remitoAmodificar.fechaCarga.get()));
		
						DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
						
						//esto es para q ademas de mostrar la fecha en el date, tambien setee la fecha..
						//xq si apreto sobre el date, y no selecciono nada lo q contenia el editor, se borraba xq
						//no habia fecha seteada.. (con esto se soluciona)
						CharSequence fecha = formato.format(remito.getFechaRemito());
						LocalDate localDate = LocalDate.parse(fecha, formato2);
						
						this.getDatePicker_fechaDeRemito().setValue(localDate);
						this.getDatePicker_fechaDeRemito().getEditor().setText(formato.format(remito.getFechaRemito()));
					
						//Buscamos el detalle del remito y completamos los insumos y datos del proveedor.
						List<DetalleRemito> detalleDeRemito = CRUD.obtenerListaDetalleRemitoPorIdRemito3(remito.getIdRemito());
						if (!detalleDeRemito.isEmpty()) {
							for (DetalleRemito detalleRemito : detalleDeRemito) {
								DetalleRemitoFX detalle = new DetalleRemitoFX(detalleRemito);
								obListDetalleRemito.add(detalle);
							}
							//ya no uso lo q esta comentado, xq "CRUD.obtenerListaDetalleRemitoPorIdRemito3", me retorna los datos necesarios de proveedor
//							final Proveedor proveedor = CRUD.obtenerProveedorPorId(detalleDeRemito.get(0).getProveedor().getPkIdProveedor());
//							this.getTextField_cuitProveedor().setText(proveedor.getNroCuit());
//							this.getcBoxSeleccioneProvee().getSelectionModel().select(proveedor.getNombreProveedor());
//							this.getTablaDetalleRemito().setItems(obListDetalleRemito);
							this.getTextField_cuitProveedor().setText(detalleDeRemito.get(0).getNroCuit());
							this.getcBoxSeleccioneProvee().getSelectionModel().select(detalleDeRemito.get(0).getNombreProveedor());
							this.getTablaDetalleRemito().setItems(obListDetalleRemito);
							
							remitoAmodificar.nombreProveedor.set(detalleDeRemito.get(0).getNombreProveedor());
						}
						if (!(remito.getFactura().getIdFactura() == null)) {
							final Factura factura = CRUD.obtenerFacturaPorId(remito.getFactura().getIdFactura());
							
							this.getTextField_NroFactura().setText(factura.getNroFactura());
							this.getTextField_cuitProveedor().setDisable(true);
							this.getcBoxSeleccioneProvee().setDisable(true);
							
							//deshabilitamos el campo nro factura, para no permitir editar la vinculacion con dicha fac
							this.getTextField_NroFactura().setDisable(true);
							
						} else {
							this.getTextField_cuitProveedor().setDisable(false);
							this.getcBoxSeleccioneProvee().setDisable(false);
						}
									
						this.getTextField_BuscarNroDeRemito().setText(remito.getNroRemito());
						this.getTextField_nroRemito().setText(this.getTextField_BuscarNroDeRemito().getText());
						this.getTextField_BuscarNroDeRemito().clear();
						
						this.getTextAreaObservacion().setText(remito.getObservacionRemito());
						this.getBtnGuardar().setDisable(false);
						btnCancelar.setDisable(false);
						
						datePicker_fechaDeRemito.setDisable(false);
						textField_nroRemito.setDisable(false);
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
	
	
	@FXML
    void guardar(ActionEvent event) {
		hayCambios = false;
		guardarCambiosEnRemito();
		
		guardarCambiosSobreProveedor();
		
		if (hayCambios || guardarCambiosSobreProveedor()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("AVISO");
			alert.setHeaderText("CAMBIOS REALIZADOS");
			alert.setContentText("El remito Nro: "+ this.getRemitoAmodificar().nroRemito.get()+
					" ha sido modificada satisfactoriamente");
			alert.showAndWait();
			
			this.limpiarCampos();
			this.deshabilitarCampos();
			this.deshabilitarBotones();
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("AVISO");
			alert.setHeaderText("NO SE REALIZARON CAMBIOS");
			alert.showAndWait();
		}
    }
	
	
    @FXML
    void buscarRemitoEnter(KeyEvent event) {
    	if (event.getCode().equals(KeyCode.ENTER)){
    		this.setDesdeVerRemitos(false); //reseteo de nuevo este flag, xq si vino desde el ver remitos, y depue busca un nuevo remito desde el buscar y cancela (evita error)
    		buscarRemito();
    	}
    }

	
	@FXML
    void eventoDatePickerRemito(ActionEvent event) {
		formatearFormatoFecha();
    }
	
	
	@FXML
    void enterTxtFieldNroFactura(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)){
			nroFacturaCorrecto();
		}
    }
	
	
	private void definirTipoDatoColumnas() {
		tablaDetalleRemito_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaDetalleRemito_Articulo.setCellValueFactory(cellData -> cellData.getValue().nroArticulo);
		tablaDetalleRemito_NroDeLote.setCellValueFactory(cellData -> cellData.getValue().insumo);       //NRO LOTE
		tablaDetalleRemito_unidadDeMedida.setCellValueFactory(cellData -> cellData.getValue().unidadMedida);
		tablaDetalleRemito_Cantidad.setCellValueFactory(cellData -> cellData.getValue().cantidad);
		tablaDetalleRemito_Sector.setCellValueFactory(cellData -> cellData.getValue().nombreSector);
		tablaDetalleRemito_Categoria.setCellValueFactory(cellData -> cellData.getValue().nombreCategoria);
		tablaDetalleRemito_Vencimiento.setCellValueFactory(cellData -> cellData.getValue().fechaVencimiento);

		alinearContenidoColumnas();
	}
	
	
	private void alinearContenidoColumnas() {
		tablaDetalleRemito_Insumo.setStyle("-fx-alignment: CENTER;");
		tablaDetalleRemito_Articulo.setStyle("-fx-alignment: CENTER;");
		tablaDetalleRemito_NroDeLote.setStyle("-fx-alignment: CENTER;");
		tablaDetalleRemito_unidadDeMedida.setStyle("-fx-alignment: CENTER;");
		tablaDetalleRemito_Cantidad.setStyle("-fx-alignment: CENTER;");
		tablaDetalleRemito_Sector.setStyle("-fx-alignment: CENTER;");
		tablaDetalleRemito_Categoria.setStyle("-fx-alignment: CENTER;");
		tablaDetalleRemito_Vencimiento.setStyle("-fx-alignment: CENTER;");
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
	
	
	public void setearFechaCargaRemito() {		
		Date fecha = new Date();
		String fechaCargaRemito = formato.format(fecha);
		lblFechaCargaRemito.setText(fechaCargaRemito);
	}
	
	
	public void limpiarCampos() {
		this.getcBoxSeleccioneProvee().getSelectionModel().select(-1);
		this.getcBoxSeleccioneProvee().setDisable(false);
		this.getTextField_cuitProveedor().setDisable(false);
		this.getTextField_cuitProveedor().clear();
		this.getTextAreaObservacion().clear();
		this.getTextField_BuscarNroDeRemito().clear();
		this.getTextField_BuscarNroDeRemito().setUnFocusColor(Color.web("#4d4d4d"));
		this.getTextField_NroFactura().clear();
		this.getTextField_NroFactura().setDisable(false);
		this.getLblFechaCargaRemito().setText("");
		this.setearFechaCargaRemito();
		this.getDatePicker_fechaDeRemito().setValue(null);
		this.getDatePicker_fechaDeRemito().getEditor().clear();
		obListDetalleRemito.clear();
		this.getTablaDetalleRemito().setItems(obListDetalleRemito);
		this.getTextField_nroRemito().clear();
		this.getTextField_nroRemito().setUnFocusColor(Color.web("#4d4d4d"));
	}
	
	
	public void deshabilitarBotones() {
		//deshabilito botones guardar y cancelar
		btnGuardar.setDisable(true);
		btnCancelar.setDisable(true);
	}
	
	
	public void deshabilitarCampos() {
		textField_cuitProveedor.setDisable(true);
		cBoxSeleccioneProvee.setDisable(true);
		textField_NroFactura.setDisable(true);
		textField_nroRemito.setDisable(true);
		datePicker_fechaDeRemito.setDisable(true);
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
		        datePicker_fechaDeRemito.setDayCellFactory(dayCellFactory);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}   
	}
	
	
	private void formatearFormatoFecha() {
	   	 datePicker_fechaDeRemito.setConverter(new StringConverter<LocalDate>() {
	  	     String pattern = "dd-MM-yyyy";
	  	     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

	  	     {
	  	         datePicker_fechaDeRemito.setPromptText(pattern.toLowerCase());
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
	
	
	private void nroFacturaCorrecto() {
		correcto = false;
		try {
			if (hayBlanco(this.getTextField_NroFactura().getText())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("DATOS ERRONEOS");
				alert.setHeaderText("El campo Nro de Factura contiene datos erroneos");
				alert.setContentText("Si desea asociar una factura ingrese un numero valido y presione ENTER");
				alert.showAndWait();
				correcto = false;
			} else {
				facturaBD = CRUD.obtenerFacturaPorNroFactura(this.getTextField_NroFactura().getText());	
				if (facturaBD != null) {
					if (facturaBD.getTieneRemito().equals("Si")) {
						
							DetalleFactura df = CRUD.obtenerDetalleFacturaPorIdFactura2(facturaBD.getIdFactura());
							
							//el query me retorna datos del proveedor, x lo q no es necesario hacer un query mas para obtener al proveedor
//							proveedorBD = ControladorILogin.opCRUD.loadProveedor(df.getProveedor().getPkIdProveedor());
//							this.getcBoxSeleccioneProvee().setValue(proveedorBD.getNombreProveedor());
//							this.getTextField_cuitProveedor().setText(proveedorBD.getNroCuit());
							this.getcBoxSeleccioneProvee().setValue(df.getNombreProveedor());
							this.getTextField_cuitProveedor().setText(df.getNroCuit());
							
							this.getcBoxSeleccioneProvee().setDisable(true);
							this.getTextField_cuitProveedor().setDisable(true);
							correcto = true;
						}else{
							Alert alert = new Alert(AlertType.ERROR);
				  			alert.setTitle("ATENCION");
				  			alert.setHeaderText("La factura Nro: "+ this.getTextField_NroFactura().getText() +" esta definida sin remitos");
				  			alert.setContentText("Si desea asociar una factura ingrese un numero valido y presione ENTER");
				  			alert.showAndWait();
				  			correcto = false;
						}
					} else {
						correcto = false;
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private boolean hayBlanco(String campo) {
		boolean hay = false;
		if (!campo.isEmpty() && campo.substring(0, 1).equals(" ")) {
			hay = true;
		}
		return hay;
	}
	
	
	private void guardarCambiosEnRemito() {
    	try {
    		remitoBD = null;
    		facturaBD = null;
    		
    		//lo seteo aca, por si no entra a nroFacturaCorrecto();
    		correcto = false;
    		
    		if (nroRemitoCorrecto()) {
    			remitoBD = CRUD.obtenerRemitoPorId2(remitoAmodificar.idRemito);
    			
    			if (!remitoBD.getNroRemito().equals(this.getTextField_nroRemito().getText())) {
        			remitoBD.setNroRemito(this.getTextField_nroRemito().getText());
        			hayCambios = true;
				}
    			
    			SimpleDateFormat formatomysql = new SimpleDateFormat("yyyy-MM-dd");  //formato q maneja el mysql

    			Date fechaRemitoPicker = java.sql.Date.valueOf(this.getDatePicker_fechaDeRemito().getValue());
    			String fechaRem = formatomysql.format(fechaRemitoPicker);
    			Date fechaRemito = formatomysql.parse(fechaRem);
    			
    			if (!remitoBD.getFechaRemito().equals(fechaRemito)) {
        			remitoBD.setFechaRemito(fechaRemito);
        			hayCambios = true;
				}
    			
    			if (!this.getTextField_NroFactura().getText().equals("")) {
        			nroFacturaCorrecto();
				}
    			if (correcto) {
//    				if(!remitoBD.getFactura().equals(facturaBD)) {
//        				remitoBD.setFactura(facturaBD);
//        			} else {
//        				remitoBD.setFactura(null);
//        			}
    				if (remitoBD.getFactura() == null) { //no estaba asociado a ninguna factura, entonces le asigno directamente la fac
    					remitoBD.setFactura(facturaBD);
					}
//    				if(!remitoBD.getFactura().getIdFactura().equals(facturaBD.getIdFactura())) {
//        				remitoBD.setFactura(facturaBD);
//        			} else {
//        				remitoBD.setFactura(null);
//        			}
        			hayCambios = true;
				}
    			if (!remitoBD.getObservacionRemito().equals(textAreaObservacion.getText())) {
        			remitoBD.setObservacionRemito(textAreaObservacion.getText());
        			hayCambios = true;
				}
    			if (hayCambios) {
        			CRUD.actualizarObjeto(remitoBD);
				}

    		} else {
    			hayCambios = false;
    			Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("AVISO");
    			alert.setHeaderText("ERROR EN DATOS MODIFICADOS DE REMITO" );
    			alert.setContentText("Complete correctamente los campos");
    			alert.showAndWait();
    		}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private boolean nroRemitoCorrecto() {
		boolean correcto = false;
			try {
				if (this.getTextField_nroRemito().getText().isEmpty() || this.getTextField_nroRemito().getText().substring(0,1).equals(" ")) {
					this.getTextField_nroRemito().setUnFocusColor(Color.RED);
				} else {
					correcto = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				e.getMessage();
			}
		return correcto;
	}
	
	
	private boolean guardarCambiosSobreProveedor() {
		Proveedor proveedor = null;
		boolean hayCambiosEnProveedor = false;

		//Tengo q setearlo?
		//DetalleRemitoId drid = new DetalleRemitoId(remitoAmodificar.idRemito);
		try {
			proveedor = CRUD.obtenerProveedorPorNroCuit(this.getTextField_cuitProveedor().getText());

			if (proveedor != null) {
				if (!remitoAmodificar.nombreProveedor.get().equals(proveedor.getNombreProveedor())) {
				hayCambiosEnProveedor = true;
				List<DetalleRemito> detalleDeRemito = CRUD.obtenerListaDetalleRemitoPorIdRemito(remitoAmodificar.idRemito);
					if (!detalleDeRemito.isEmpty()) {
						for (DetalleRemito detalleRemito : detalleDeRemito) {
							detalleRemito.setProveedor(proveedor);
							CRUD.actualizarObjeto(detalleRemito);
							
							//significa q se esta vinculando el remito a una factura (q lo permite), x lo tanto se le 
							//tiene q agregar a su detalle fac, nueva fila de insumo
							if (correcto) {
								Insumo insBD = CRUD.obtenerInsumoPorId1(detalleRemito.getInsumo().getIdInsumo());
								guardarFilaDetalleFactura(insBD, detalleRemito.getCantidad(), Float.valueOf("0"), remitoBD);
							}
						}
					}
				} else {
					hayCambiosEnProveedor = false;
				}
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("AVISO");
				alert.setHeaderText("ERROR EN DATOS DE PROVEEDOR" );
				alert.setContentText("Complete correctamente los campos");
				alert.showAndWait();
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return hayCambiosEnProveedor;
	}
	
	
	private void guardarFilaDetalleFactura(Insumo insumoIN, Integer cantidad, Float importe, Remito remitoIN) {
		try {
			DetalleFacturaId id = new DetalleFacturaId(facturaBD.getIdFactura());
	    	
	    	Float precio = insumoIN.getPrecioInsumo();
	    	
	    	DetalleFactura dF = new DetalleFactura(id, facturaBD, insumoIN, proveedorBD, remitoIN, cantidad, precio, importe);
		
	    	CRUD.guardarObjeto(dF);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
	
	
	//metodo q se llama desde el btn modificar de pantalla ver remitos
    //"remitoFX" contiene la info del remito seleccionado de la fila, de la tabla remitos
	public void completarCampos(RemitoFX remitoFX) {
		try {
			remitoAmodificar = null;
			
			this.getTextField_NroFactura().setDisable(false);
			
			Remito remito = CRUD.obtenerRemitoPorId(remitoFX.idRemito);
			
			textAreaObservacion.setText(remito.getObservacionRemito());
			
			remitoAmodificar = new RemitoFX(remito);
			
			this.getTextField_cuitProveedor().setText(remitoFX.nroCuit.get());
			this.getcBoxSeleccioneProvee().getSelectionModel().select(remitoFX.nombreProveedor.get());
			
			if (!(remitoFX.factura.get() == null)) {
				this.getTextField_NroFactura().setText(remitoFX.factura.get());
				this.getTextField_cuitProveedor().setDisable(true);
				this.getcBoxSeleccioneProvee().setDisable(true);
				this.getTextField_NroFactura().setDisable(true);
				
				remitoAmodificar.factura.set(remitoFX.factura.get());
					
			} else {
				this.getTextField_cuitProveedor().setDisable(false);
				this.getcBoxSeleccioneProvee().setDisable(false);
			}
			
			DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			
			//esto es para q ademas de mostrar la fecha en el date, tambien setee la fecha..
			//xq si apreto sobre el date, y no selecciono nada lo q contenia el editor, se borraba xq
			//no habia fecha seteada.. (con esto se soluciona)
			CharSequence fecha = formato.format(remitoFX.fechaRemito.get());
			LocalDate localDate = LocalDate.parse(fecha, formato2);
			
			this.getDatePicker_fechaDeRemito().setValue(localDate);
			this.getDatePicker_fechaDeRemito().getEditor().setText(formato.format(remitoFX.fechaRemito.get()));
			
			this.getLblFechaCargaRemito().setText(formato.format(remitoFX.fechaCarga.get()));
			
			List<DetalleRemito> detalleDeRemito = CRUD.obtenerListaDetalleRemitoPorIdRemito3(remitoFX.idRemito);
			obListDetalleRemito.clear();
			if (!detalleDeRemito.isEmpty()) {
				for (DetalleRemito detalleRemito : detalleDeRemito) {
					DetalleRemitoFX detalle = new DetalleRemitoFX(detalleRemito);
					obListDetalleRemito.add(detalle);
				}
				remitoAmodificar.nombreProveedor.set(detalleDeRemito.get(0).getNombreProveedor());
			}
			this.getTablaDetalleRemito().setItems(obListDetalleRemito);
			this.getTextField_nroRemito().setText(remitoFX.nroRemito.get());
			this.getTextField_BuscarNroDeRemito().clear();
			this.getTextField_nroRemito().requestFocus();
			
			this.getBtnGuardar().setDisable(false);
			btnCancelar.setDisable(false);
			
			datePicker_fechaDeRemito.setDisable(false);
			textField_nroRemito.setDisable(false);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	

}
