package controlador;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.FloatStringConverter;
import modelo.DetalleFactura;
import modelo.DetalleRemito;
import modelo.DetalleRemitoId;
import modelo.Factura;
import modelo.Insumo;
import modelo.Proveedor;
import modelo.Remito;
import modeloAux.DetalleFacturaFX;

public class ControladorIModificarFactura {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox checkBoxTieneRemito;

	@FXML
    private Label lblFechaDeIngreso;

    @FXML
    private JFXComboBox<String> comboBoxTipoFactura;

    @FXML
    private JFXTextField textField_cuitProveedor;

	@FXML
    private JFXComboBox<String> comboBoxProveedor;

    @FXML
    private JFXTextField textField_BuscarNroFactura;

	@FXML
    private DatePicker dateFechaFactura;

	@FXML
    private TableView<DetalleFacturaFX> tablaInsumosEnFactura;

	@FXML
    private TableColumn<DetalleFacturaFX, Date> tablaInsumosEnFactura_vencimiento;

    @FXML
    private TableColumn<DetalleFacturaFX, Float> tablaInsumosEnFactura_precio;

    @FXML
    private TableColumn<DetalleFacturaFX, Integer> tablaInsumosEnFactura_cantidad;

    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaInsumosEnFactura_unidadMedida;

    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaInsumosEnFactura_insumo;

    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaInsumosEnFactura_nroRemito;

	@FXML
    private TableColumn<DetalleFacturaFX, String> tablaInsumosEnFactura_articulo;

    @FXML
    private TableColumn<DetalleFacturaFX, Float> tablaInsumosEnFactura_importe;

    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaInsumosEnFactura_nroLote;

    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaInsumosEnFactura_Categoria;

    @FXML
    private TableColumn<DetalleFacturaFX, String> tablaInsumosEnFactura_Sector;
    
    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnCancelar;
    
    @FXML
    private CheckBox checkBoxVerSecCat2;
    
    @FXML
    private JFXTextField textFieldnroDeFactura;

	@FXML
    private JFXButton btnBuscarFactura;
	
	@FXML
    private JFXCheckBox checkBoxVerSecCat;
    
    @FXML
    private Label lblMsjErrorNroFactura;
    
    @FXML
    private Label lblMsjErrorNroFacturaEnUso;
    
    @FXML
    private Label lblSubtotal;
    
//    @FXML
//    private JFXTextArea txtArea;
    
    @FXML
    private TextArea txtArea;
    
	ObservableList<String> itemsComboTipoFactura = FXCollections.observableArrayList("TIPO A", "TIPO B", "TIPO C");

    public  ObservableList<DetalleFacturaFX> obListDetalleFactura = FXCollections.observableArrayList();

    private ObservableList<DetalleFacturaFX> obListDetalleFacturaAux = FXCollections.observableArrayList();

    private String oldNroFactura;
    
    private List<String> listaNewNroRemitos = new ArrayList<>();   //sirve solamente para consistir el editado del campo nro remito
    
    private List<String> listaNroRemitos = new ArrayList<>();  //sirve para guardar referencia a los nuevos remitos q se crean en el guardar modif
    
    private boolean valido;
    
    private boolean cambio; 
    
    private Integer idFacturaAux;
    
    private Remito remitoBD;
    
    private Factura facturaBD;
    
    private Proveedor proveedorBD;
    
    
    /**************************** CONSTRUCTOR *********************************/
    
    public ControladorIModificarFactura() {
    	
	}
    
    
    /**************************** GET - SET **********************************/
    
    public JFXComboBox<String> getComboBoxTipoFactura() {
		return comboBoxTipoFactura;
	}
    
    
    public CheckBox getCheckBoxTieneRemito() {
		return checkBoxTieneRemito;
	}


	public Label getLblFechaDeIngreso() {
		return lblFechaDeIngreso;
	}


	public JFXTextField getTextField_cuitProveedor() {
		return textField_cuitProveedor;
	}


	public JFXComboBox<String> getComboBoxProveedor() {
		return comboBoxProveedor;
	}


	public JFXTextField getTextField_BuscarNroFactura() {
		return textField_BuscarNroFactura;
	}


	public DatePicker getDateFechaFactura() {
		return dateFechaFactura;
	}


	public TableView<DetalleFacturaFX> getTablaInsumosEnFactura() {
		return tablaInsumosEnFactura;
	}


	public JFXTextField getTextFieldnroDeFactura() {
		return textFieldnroDeFactura;
	}


	public Label getLblMsjErrorNroFactura() {
		return lblMsjErrorNroFactura;
	}


	public Label getLblMsjErrorNroFacturaEnUso() {
		return lblMsjErrorNroFacturaEnUso;
	}

	
	public Factura getFacturaBD() {
		return facturaBD;
	}

	public void setFacturaBD(Factura facturaBD) {
		this.facturaBD = facturaBD;
	}

	
	
	public Proveedor getProveedorBD() {
		return proveedorBD;
	}

	public void setProveedorBD(Proveedor proveedorBD) {
		this.proveedorBD = proveedorBD;
	}

	
	public Remito getRemitoBD() {
		return remitoBD;
	}

	public void setRemitoBD(Remito remitoBD) {
		this.remitoBD = remitoBD;
	}


	/********************************** METODOS ***********************************/
    
    
    @FXML
    void initialize() {
    	definirTipoDatoColumnas();
    	this.getComboBoxTipoFactura().setItems(itemsComboTipoFactura);
    	restringirFechas();
    	definirColumnasEditables();
    	seleccionarFila();
    }
    

	//maneja "ENTER" sobre el texteField nro factura a buscar..
  	@FXML
    void buscarFacturaEnter(KeyEvent event) {
      	if (event.getCode().equals(KeyCode.ENTER)){
      		buscarFactura();
      	}
    }
    
    
  	@FXML
    void guardar(ActionEvent event) {
  		valido = false;
    	cambio = false;
    	//1ero guardo las modificaciones (si se hicieron) sobre factura
    	guardarCambiosSobreFactura();
    	//proveedor y detalle van de la mano, xq la FK de proveedor, va sobre el detalleFactura y no sobre la factura
    	guardarCambiosSobreProveedorYdetalleFactura();
    	
    	if (valido) {
    		mostrarMsjDialogo("guardar");
    		limpiarCampos();
    		deshabilitarBotones();
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("AVISO");
			alert.setHeaderText("NO SE REALIZARON CAMBIOS");
			alert.showAndWait();
		}
    }
    
    
  	@FXML
    void cancelar(ActionEvent event) {
  		try {
  		//deshace todos los cambios q realizo sobre la fac y deja todos los campos y tabla como estaba
  	    	limpiarCampos();
  	    	//el valor "oldNroFactura", se setea anteriormente cuando se completo x 1era vez los campos de factura
  	    	Factura factura = CRUD.obtenerFacturaPorNroFactura(oldNroFactura);
  	    	completarCampos(factura);
  			completarDetalleFactura(factura);
  			mostrarMsjDialogo("cancelar");
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
  	@FXML
  	void buscarFactura() {
		try {
			if (!this.getTextField_BuscarNroFactura().getText().isEmpty()) {  //osea si apreto "enter" y el campo no esta vacio
				if (this.getTextField_BuscarNroFactura().getText().substring(0, 1).equals(" ")) {  //si tiene un blanco al inicio
					limpiarCampos();
					lblMsjErrorNroFactura.setVisible(true);
    			} else {
    				lblMsjErrorNroFactura.setVisible(false);
					final Factura factura = CRUD.obtenerFacturaPorNroFactura(this.getTextField_BuscarNroFactura().getText());
	    			if (factura == null) {
	    				limpiarCampos();
	    				deshabilitarBotones();
	    				deshabilitarCampos();
	    				mostrarMsjNoSeEncuentraFactura();
	    			} else {
	    				completarCampos(factura);
	    				
	    				completarDetalleFactura(factura);
	    				
	    				//habilito botones guardar y cancelar
	    				btnGuardar.setDisable(false);
	    				btnCancelar.setDisable(false);
	    				
	    				habilitarCampos();
	    			}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}  
    }
  	
  	
  	@FXML
    void manejaEventoCheckBoxVerSecCat(ActionEvent event) {    	
    	if (checkBoxVerSecCat2.isSelected()) {
			tablaInsumosEnFactura_Sector.setVisible(true);
			tablaInsumosEnFactura_Categoria.setVisible(true);
		} else {
			tablaInsumosEnFactura_Sector.setVisible(false);
			tablaInsumosEnFactura_Categoria.setVisible(false);
		}
    }
    
    
  	@FXML
    void eventoComboBoxProveedor(ActionEvent event) {
  		try {
    		if (this.getComboBoxProveedor().getSelectionModel().getSelectedItem() != null) {
    			String nombreProvee = this.getComboBoxProveedor().getSelectionModel().getSelectedItem();
    			Proveedor provee = CRUD.obtenerProveedorPorNombre(nombreProvee);
    			this.getTextField_cuitProveedor().setText(provee.getNroCuit());
    		}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
  	
    
	@FXML
    void eventoDatePickerFactura(ActionEvent event) {
		formatearFormatoFecha();
    }
    
    
	//manejador de evento, del txtField NroFactura
	//el q se utiliza para modificar si se quiere, dicho nro de la factura de la BD
	@FXML
    void nroFacturaEnUso(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)){
    		nroFacturaEnUso();
    	}
    }
    
    
	private void definirTipoDatoColumnas() {
    	tablaInsumosEnFactura_insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
    	tablaInsumosEnFactura_articulo.setCellValueFactory(cellData -> cellData.getValue().nroArticulo);
    	tablaInsumosEnFactura_nroLote.setCellValueFactory(cellData -> cellData.getValue().insumo);       //NRO LOTE
    	tablaInsumosEnFactura_unidadMedida.setCellValueFactory(cellData -> cellData.getValue().unidadMedida);
    	tablaInsumosEnFactura_cantidad.setCellValueFactory(cellData -> cellData.getValue().cantidad);
    	tablaInsumosEnFactura_precio.setCellValueFactory(cellData -> cellData.getValue().precio);
    	tablaInsumosEnFactura_importe.setCellValueFactory(cellData -> cellData.getValue().importe);
		tablaInsumosEnFactura_vencimiento.setCellValueFactory(cellData -> cellData.getValue().fechaVencimiento);
		tablaInsumosEnFactura_nroRemito.setCellValueFactory(cellData -> cellData.getValue().nroRemito);
		tablaInsumosEnFactura_Sector.setCellValueFactory(cellData -> cellData.getValue().nombreSector);
		tablaInsumosEnFactura_Categoria.setCellValueFactory(cellData -> cellData.getValue().nombreCategoria);
		alinearContenidoColumnas();
	}
    
	
	private void alinearContenidoColumnas() {
		tablaInsumosEnFactura_insumo.setStyle("-fx-alignment: CENTER;");
		tablaInsumosEnFactura_articulo.setStyle("-fx-alignment: CENTER;");
		tablaInsumosEnFactura_nroLote.setStyle("-fx-alignment: CENTER;");
		tablaInsumosEnFactura_unidadMedida.setStyle("-fx-alignment: CENTER;");
		tablaInsumosEnFactura_cantidad.setStyle("-fx-alignment: CENTER;");
		tablaInsumosEnFactura_precio.setStyle("-fx-alignment: CENTER-RIGHT;");
		tablaInsumosEnFactura_importe.setStyle("-fx-alignment: CENTER-RIGHT;");
		tablaInsumosEnFactura_vencimiento.setStyle("-fx-alignment: CENTER;");
		tablaInsumosEnFactura_nroRemito.setStyle("-fx-alignment: CENTER;");
		tablaInsumosEnFactura_Sector.setStyle("-fx-alignment: CENTER;");
		tablaInsumosEnFactura_Categoria.setStyle("-fx-alignment: CENTER;");
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
		        dateFechaFactura.setDayCellFactory(dayCellFactory);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}   
	}
    
    
	private void definirColumnasEditables() {
		tablaInsumosEnFactura_nroRemito.setCellFactory(TextFieldTableCell.forTableColumn());
		
		//atributo necesario para convertir el tipo de dato de "precio (float)" a string
		FloatStringConverter convertirFaS = new FloatStringConverter();
		tablaInsumosEnFactura_precio.setCellFactory(TextFieldTableCell.<DetalleFacturaFX, Float>forTableColumn(convertirFaS)); 
		
		tablaInsumosEnFactura_importe.setCellFactory(TextFieldTableCell.<DetalleFacturaFX, Float>forTableColumn(convertirFaS));
		
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
		tablaInsumosEnFactura_vencimiento.setCellFactory(TextFieldTableCell.<DetalleFacturaFX, Date>forTableColumn(convertirDaS));

		controlarEventosCampos();
	}
    
    
	private void seleccionarFila(){
	//se le asigna una accion al click de cada fila de la tabla detalle
		tablaInsumosEnFactura.setRowFactory( tv -> {
		    TableRow<DetalleFacturaFX> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		    	
		        if (event.getButton() == MouseButton.PRIMARY){
		        	
		        	if (tablaInsumosEnFactura.getSelectionModel().isSelected(row.getIndex(), tablaInsumosEnFactura_nroRemito)) {
		        		DetalleFacturaFX rowData = row.getItem();
		        		
		        		//////////////////////////////////////////////////// CAMPO NRO REMITO
		        		if (checkBoxTieneRemito.isSelected()) {
		      
		        			if (rowData.nroRemito.get() == null) {
			        			//si el nroRemito de la fila seleccionada esta vacia
			        			//habilito la col para su edicion
			        			tablaInsumosEnFactura_nroRemito.setEditable(true);
							} else {
								//verifico si el nro remito del campo es uno q ingreso recientemente, si es asi, x mas q este completo tiene q dejar editar
								//x si, se equivoco al completar ese nro remito
								if (nroRemitoEsNuevo(rowData.nroRemito.get())) {
									tablaInsumosEnFactura_nroRemito.setEditable(true);
								} else {
									//sino, era un campo q ya vino completo desde la bd, entonces cancelo la edicion de la col nroRemito
									tablaInsumosEnFactura_nroRemito.setEditable(false);
								}
							}
						}
		        		
					} else {
		        		tablaInsumosEnFactura.getSelectionModel().clearSelection();
					}
		        }
		    });
		    return row ;
			});
	}
    
    
	public void limpiarCampos() {
		this.getTextField_BuscarNroFactura().requestFocus();
		this.getTextField_BuscarNroFactura().clear();
		this.getTextFieldnroDeFactura().clear();
		this.getTextField_cuitProveedor().clear();
		this.getComboBoxProveedor().getSelectionModel().select(-1);
		this.getComboBoxTipoFactura().getSelectionModel().select(-1);
		this.getDateFechaFactura().setValue(null);
		obListDetalleFactura.clear();    	
		this.getTablaInsumosEnFactura().setItems(obListDetalleFactura);
		this.getDateFechaFactura().getEditor().clear();
		this.getLblFechaDeIngreso().setText("dd-mm-yyyy");
		this.getLblMsjErrorNroFactura().setVisible(false);
		this.getLblMsjErrorNroFacturaEnUso().setVisible(false);
		this.getCheckBoxTieneRemito().setSelected(false);
		checkBoxTieneRemito.setOpacity(1);
		tablaInsumosEnFactura_nroRemito.setVisible(false);
		//reseteo la lista nro remitos aux
		listaNewNroRemitos.clear();
		listaNroRemitos.clear();
		lblSubtotal.setText(null);
		txtArea.clear();
	}
	
	
	public void deshabilitarBotones() {
		//deshabilito botones guardar y cancelar
		btnGuardar.setDisable(true);
		btnCancelar.setDisable(true);
	}
	
	
	public void llenarComboProveedores() {
		try {
			this.getComboBoxProveedor().getItems().clear();
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			List<Proveedor> listaProveedores = CRUD.obtenerListaProveedoresActivos();
    		for (Proveedor proveedor : listaProveedores) {
    			itemsCombo.add(proveedor.getNombreProveedor());
			}
    		this.getComboBoxProveedor().setItems(itemsCombo);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void mostrarMsjNoSeEncuentraFactura() {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ATENCION");
		alert.setHeaderText("Factura no encontrada" );
		alert.setContentText("La factura con numero: "+ this.getTextField_BuscarNroFactura().getText() +" no se encuentra registrada");
		alert.showAndWait();
	}
	
	
	private void completarCampos(Factura facturaIN) {
		oldNroFactura = null;
		
		this.getTextFieldnroDeFactura().setText(facturaIN.getNroFactura());
		this.getTextField_BuscarNroFactura().clear();
		//luego dejo referemcia al nroFactura, x si luego decide modificarlo y luego decide q no
		//y escriba nuevamente el mismo nroFactura, no diga q se encuentra en uso
		oldNroFactura = this.getTextFieldnroDeFactura().getText();
		
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		this.getLblFechaDeIngreso().setText(formato.format(facturaIN.getFechaCarga()));
		
		if (!facturaIN.getTipoFactura().equals("")) {
			this.getComboBoxTipoFactura().getSelectionModel().select(facturaIN.getTipoFactura());
		}
		
		//esto es para q ademas de mostrar la fecha en el date, tambien setee la fecha..
		//xq si apreto sobre el date, y no selecciono nada lo q contenia el editor, se borraba xq
		//no habia fecha seteada.. (con esto se soluciona)
		CharSequence fecha = formato.format(facturaIN.getFechaFactura());
		LocalDate localDate = LocalDate.parse(fecha, formato2);
		this.getDateFechaFactura().setValue(localDate);
		txtArea.setText(facturaIN.getObservacionFactura());
	}
	
	
	private void completarDetalleFactura(Factura facturaIN) {
		try {
			Float subTotal = Float.valueOf("0");
//			SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
			//Buscamos el detalle de la factura y completamos los insumos y datos del proveedor.
			List<DetalleFactura> detalleDeFactura = CRUD.obtenerListaDetalleFacturaPorIdFactura2(facturaIN.getIdFactura());

			if (!detalleDeFactura.isEmpty()) {
				
				obListDetalleFactura.clear();
				
				for (DetalleFactura detalleFactura : detalleDeFactura) {
					
					DetalleFacturaFX detalleFX = new DetalleFacturaFX(detalleFactura);
					
//					if (detalleFactura.getRemito() != null) {
//						Remito remito = ControladorILogin.opCRUD.loadRemito(detalleFactura.getRemito().getIdRemito());
//						detalleFX.nroRemito.set(remito.getNroRemito());
					detalleFX.nroRemito.set(detalleFactura.getNroRemito());
//					}
					
//					Insumo ins = ControladorILogin.opCRUD.loadInsumo(detalleFactura.getInsumo().getIdInsumo());
//					Categoria cat = ControladorILogin.opCRUD.loadCategoria(ins.getCategoria().getPkIdCategoria());
//					Sector sec = ControladorILogin.opCRUD.loadSectorPorID(cat.getSector().getIdSector());
//					detalleFX.idCategoria = cat.getPkIdCategoria();
//					detalleFX.nombreCategoria.set(cat.getNombreCategoria());
//					detalleFX.nombreSector.set(sec.getNombreSector());
					detalleFX.idCategoria = detalleFactura.getIdCategoria();
					detalleFX.nombreCategoria.set(detalleFactura.getNombreCategoria());
					detalleFX.nombreSector.set(detalleFactura.getNombreSector());
					
//					if (ins.getFechaVencimiento() != null) {
//						String fecha = formato.format(ins.getFechaVencimiento());
//						Date fechaVto = formato.parse(fecha);
//						detalleFX.fechaVencimiento.set(fechaVto);
//					}
					
					//guardo referencia a la fila del detalle
//					detalleFX.numFila = detalleFactura.getId().getPkNumDetalle();
					detalleFX.numFila = detalleFactura.getId().getPkNumDetalle();
					
					detalleFX.nroReferencia = detalleFactura.getReferencia();
					
					//calcular el subtotal de la factura
					subTotal = subTotal + detalleFactura.getImporte();
					obListDetalleFactura.add(detalleFX);
				}
				
				lblSubtotal.setText(String.valueOf(subTotal) + " $");
				
				if (facturaIN.getTieneRemito().equals("Si")) {
					//seteo el check "tiene remito"
					checkBoxTieneRemito.setSelected(true);
					checkBoxTieneRemito.setOpacity(1);
					//muestro pa su edicion la col nro remito
					tablaInsumosEnFactura_nroRemito.setVisible(true);
				} else {
					checkBoxTieneRemito.setSelected(false);
					checkBoxTieneRemito.setOpacity(1);
					tablaInsumosEnFactura_nroRemito.setVisible(false);
				}
				
				//Como el detalle siempre va a tener el mismo proveedor para cada tupla, se toma la 1ra y se obtiene los datos de ahi
//				final Proveedor proveedor = ControladorILogin.opCRUD.loadProveedor(detalleDeFactura.get(0).getProveedor().getPkIdProveedor());
//				this.getTextField_cuitProveedor().setText(proveedor.getNroCuit());
//				this.getComboBoxProveedor().getSelectionModel().select(proveedor.getNombreProveedor());
				this.getTextField_cuitProveedor().setText(detalleDeFactura.get(0).getNroCuit());
				this.getComboBoxProveedor().getSelectionModel().select(detalleDeFactura.get(0).getNombreProveedor());
				this.getTablaInsumosEnFactura().setItems(obListDetalleFactura);
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}


	public void deshabilitarCampos() {
		textFieldnroDeFactura.setDisable(true);
		textField_cuitProveedor.setDisable(true);
		comboBoxProveedor.setDisable(true);
		comboBoxTipoFactura.setDisable(true);
		dateFechaFactura.setDisable(true);
	}
	
	
	public void habilitarCampos() {
		textFieldnroDeFactura.setDisable(false);
		textField_cuitProveedor.setDisable(false);
		comboBoxProveedor.setDisable(false);
		comboBoxTipoFactura.setDisable(false);
		dateFechaFactura.setDisable(false);
	}
	
	
	private void mostrarMsjDialogo(String tipo) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
		switch (tipo) {
		case "guardar":
			alert.setHeaderText("Cambios realizados");
			alert.setContentText("La factura Nro: "+ this.getTextFieldnroDeFactura().getText() +" ha sido modificada satisfactoriamente");
			break;

		case "cancelar":
			alert.setHeaderText("Cambios desechados");
			alert.setContentText("La factura Nro: "+ oldNroFactura +" ha sido restaurada satisfactoriamente");
			break;
		}
		alert.showAndWait();
	}
	
	
	private void formatearFormatoFecha() {
   	 dateFechaFactura.setConverter(new StringConverter<LocalDate>() {
  	     String pattern = "dd-MM-yyyy";
  	     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

  	     {
  	         dateFechaFactura.setPromptText(pattern.toLowerCase());
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
	
	
	//verifica si el nro de factura q ingreso, para cambiarle a la factura q trajo de la bd, esta siendo
	//usada x otra
	private void nroFacturaEnUso() {
		try {
			if (!this.getTextFieldnroDeFactura().getText().isEmpty()) {  //osea si apreto "enter" y el campo no esta vacio
				if (this.getTextFieldnroDeFactura().getText().substring(0, 1).equals(" ")) {  //si tiene un blanco al inicio
					lblMsjErrorNroFacturaEnUso.setText("No se admite blancos");
					lblMsjErrorNroFacturaEnUso.setVisible(true);
    			} else {
    				lblMsjErrorNroFacturaEnUso.setVisible(false);
    				final Factura factura = CRUD.obtenerFacturaPorNroFactura(this.getTextFieldnroDeFactura().getText());
    				if (factura != null) { //significa q esta siendo usada x otra factura
    					//pero tengo q verificar q si ingreso el nroFactura q tenia antes, deje ponerlo
    					if (!oldNroFactura.equals(factura.getNroFactura())) {
    						lblMsjErrorNroFacturaEnUso.setText("Ya se encuentra en uso");
        					lblMsjErrorNroFacturaEnUso.setVisible(true);
						}
    				} else {
    					lblMsjErrorNroFacturaEnUso.setVisible(false);
    				}
    			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void controlarEventosCampos() {
    	controlarEventosCampoPrecio();
    	
    	controlarEventosCampoImporte();
    	
    	controlarEventosCampoNroRemito();
	}
	
	
	private void controlarEventosCampoPrecio() {
		tablaInsumosEnFactura_precio.setOnEditCommit(data -> {
    		Float newPrecio = data.getNewValue();
    		Float oldPrecio = data.getOldValue();

    	    if (newPrecio != oldPrecio) {
				obListDetalleFactura.get(tablaInsumosEnFactura.getSelectionModel().getSelectedIndex()).precio.set(newPrecio);
        	    calcularSubtotal();
    	    } 
    	});
	}
	
	
	private void controlarEventosCampoImporte() {
		tablaInsumosEnFactura_importe.setOnEditCommit(data -> {
    		Float newImporte = data.getNewValue();
    		Float oldImporte = data.getOldValue();

    	    if (newImporte != oldImporte) {
				obListDetalleFactura.get(tablaInsumosEnFactura.getSelectionModel().getSelectedIndex()).importe.set(newImporte);
        	    calcularSubtotal();
    	    } 
    	});
	}
	
	
	private void controlarEventosCampoNroRemito() {
		tablaInsumosEnFactura_nroRemito.setOnEditCommit(data -> {
			String newRemito = data.getNewValue();
			//verifico q no este vacio y luego q no tenga blancos
	 		   if ((!(newRemito.isEmpty()))) {
	 			   
	 			   if (newRemito.substring(0, 1).equals(" ")) {
	     			   mostrarMsjDialogoBlancoEnRemito();
	     		   
	     		   } else {
	     			   //hay q verificar q el nro ingreso no este ya cargado en la BD
	     			   if (nroRemitoEnUso(newRemito)) {
						mostrarMsjNroRemitoEnUso(newRemito);
	     			   } else {
	     				  obListDetalleFactura.get(tablaInsumosEnFactura.getSelectionModel().getSelectedIndex()).nroRemito.set(newRemito);
	     				  //luego lo agrego a una lista aux, q guardara todos los nro remito recien ingresados
	     				  //xq luego del "enter", si se equivoco y quiere modificarlo no dejara xq el campo estara completo
	     				  //entendiendo el codigo como q ya vino ese valor de la bd (q no es correcto)
	     				  agregarNewNroRemitoAListaAux(newRemito);
	     			   }
	     		   }
	 		   } else { //osea el newRemito esta en blanco
	 			  obListDetalleFactura.get(tablaInsumosEnFactura.getSelectionModel().getSelectedIndex()).nroRemito.set(null);
	 		   }
		});
	}
	
	
	private void calcularSubtotal() {
		Float subTotal = Float.valueOf("0");
		for (int i = 0; i < tablaInsumosEnFactura.getItems().size(); i++) {
			if (tablaInsumosEnFactura.getItems().get(i).importe.get() != null) {
				subTotal = subTotal + tablaInsumosEnFactura.getItems().get(i).importe.get();
			}
		}
		lblSubtotal.setText(String.valueOf(subTotal) + " $");
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
			obListDetalleFactura.get(tablaInsumosEnFactura.getSelectionModel().getSelectedIndex()).nroRemito.set(null);
			actualizarTabla();
		}
	}
	
	
	private void actualizarTabla() {
		//reseteo oblist aux, q gurda momentaneamente lo q contiene la tabla
		obListDetalleFacturaAux.clear();
		//luego hago la actualizacion de la tabla
		for (DetalleFacturaFX detalleFacturaFX : obListDetalleFactura) {
			obListDetalleFacturaAux.add(detalleFacturaFX);
		}
		obListDetalleFactura.clear();
		tablaInsumosEnFactura.setItems(obListDetalleFactura);
		for (DetalleFacturaFX detalleFacturaFX : obListDetalleFacturaAux) {
			obListDetalleFactura.add(detalleFacturaFX);
		}
		tablaInsumosEnFactura.setItems(obListDetalleFactura);
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
			obListDetalleFactura.get(tablaInsumosEnFactura.getSelectionModel().getSelectedIndex()).nroRemito.set(null);
			actualizarTabla();
		}
	}
	
	
	private void agregarNewNroRemitoAListaAux(String newRemito) {
		boolean esta = false;
		if (listaNewNroRemitos.isEmpty()) {
			listaNewNroRemitos.add(newRemito);
		} else {
			//verifico el nroRemito nuevo, para q no se guarde nuevamente el mismo nro
			for (String nroRemitoAux : listaNewNroRemitos) {
				if (nroRemitoAux.equals(newRemito)) {
					esta = true;
					break;
				}
			}
			if (!esta) {
				listaNewNroRemitos.add(newRemito);
			}
		}
	}
	
	
	//verifica si el nroRemito q tiene el campo, es uno q ingreso recientemente
	//x lo tanto deberia estar en la lista aux de nro remitos..
	//sino esta, es xq era un campo q ya vino completo de la bd..
	//entonces no deja editar el campo
	private boolean nroRemitoEsNuevo(String nroRemitoIN) {
		boolean eraNuevo = false;
		for (String nroRemitoAux : listaNewNroRemitos) {
			if (nroRemitoAux.equals(nroRemitoIN)) {
				eraNuevo = true;
				break;
			}
		}
		return eraNuevo;
	}
	
	
	
	private void guardarCambiosSobreFactura() {
    	try {
        	//antes de setear debo verificar si el campo nroFactura tiene datos correctos
        	if (nroFacturaCorrecto()) {
        		//utilizo el valor original del nroFactura "oldNroFactura", antes de q sufriera alguna modificacion
        		Factura facBD = CRUD.obtenerFacturaPorNroFactura(oldNroFactura);
        		
        		if (!facBD.getNroFactura().equals(this.getTextFieldnroDeFactura().getText())) {
            		facBD.setNroFactura(this.getTextFieldnroDeFactura().getText());
            		valido = true;
				}
        		
        		if (!facBD.getTipoFactura().equals(this.getComboBoxTipoFactura().getSelectionModel().getSelectedItem())) {
                	facBD.setTipoFactura(this.getComboBoxTipoFactura().getSelectionModel().getSelectedItem());
            		valido = true;
				}
            	
            	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");  //formato q maneja el mysql
            	Date fechaFacturaPicker = java.sql.Date.valueOf(dateFechaFactura.getValue());  //conversion de LocalDate a Date
    			String fechaFacPicker = formato.format(fechaFacturaPicker);
    			Date fechaFactura = formato.parse(fechaFacPicker);
    			
    			if (!facBD.getFechaFactura().equals(fechaFactura)) {
        			facBD.setFechaFactura(fechaFactura);
        			valido = true;
				}
    			
    			if (!facBD.getObservacionFactura().equals(txtArea.getText())) {
        			facBD.setObservacionFactura(txtArea.getText());
        			valido = true;
				}
    			
    			if (valido) {
        			CRUD.actualizarObjeto(facBD);
				}
    			
    			//luego guardo referencia a la factura actualizada, para el guardar remito
    			this.setFacturaBD(facBD);
    			idFacturaAux = null;
    			idFacturaAux = facBD.getIdFactura();
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private boolean nroFacturaCorrecto() {
		boolean correcto = false;
		try {
			if (!this.getTextFieldnroDeFactura().getText().isEmpty()) {  //osea si apreto "enter" y el campo no esta vacio
				if (this.getTextFieldnroDeFactura().getText().substring(0, 1).equals(" ")) {  //si tiene un blanco al inicio
					lblMsjErrorNroFacturaEnUso.setText("No se admite blancos");
					lblMsjErrorNroFacturaEnUso.setVisible(true);
    			} else {
    				lblMsjErrorNroFacturaEnUso.setVisible(false);
    				final Factura factura = CRUD.obtenerFacturaPorNroFactura(this.getTextFieldnroDeFactura().getText());
    				if (factura != null) { //significa q esta siendo usada x otra factura
    					//pero tengo q verificar q si ingreso el nroFactura q tenia antes, deje ponerlo
    					if (!oldNroFactura.equals(factura.getNroFactura())) {
    						lblMsjErrorNroFacturaEnUso.setText("Ya se encuentra en uso");
        					lblMsjErrorNroFacturaEnUso.setVisible(true);
						} else {
							correcto = true;
						}
    				} else {
    					lblMsjErrorNroFacturaEnUso.setVisible(false);
    					correcto = true;
    				}
    			}
			} else {
				lblMsjErrorNroFacturaEnUso.setText("Campo obligatorio");
				lblMsjErrorNroFacturaEnUso.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return correcto;
	}
	
	
	
	private void guardarCambiosSobreProveedorYdetalleFactura() {
		Proveedor pro = null;
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");  //formato q maneja el mysql
			pro = CRUD.obtenerProveedorPorNroCuit(this.getTextField_cuitProveedor().getText());
			//guardo referencia al proveedor para el guardar remito
			this.setProveedorBD(pro);
			
			if (!listaNewNroRemitos.isEmpty()) {
				
				System.out.println(listaNewNroRemitos.size());

				for (String nroRemitoNuevo : listaNewNroRemitos) {
					for (DetalleFacturaFX dFacturaFX : obListDetalleFactura) {	//recorro la tabla 
						
						//entonces traigo de la bd, ese detalleFactura, para actualizarle sus campos
						DetalleFactura dFacturaBD = CRUD.obtenerDetalleFacturaPorNumFila(dFacturaFX.numFila);
						
						dFacturaBD.setProveedor(null); //?
						dFacturaBD.setProveedor(pro);
						
						if (dFacturaFX.nroRemito.get() == null) { //si entra es xq dejo en blanco el campo remito, pero quizas le modifico sus otros campos
							cambio = false;

							Insumo insBD = CRUD.obtenerInsumoPorNombreCategoriaLoteArticuloYReferencia(dFacturaFX.nombreInsumo.get(), 
																										dFacturaFX.idCategoria, 
																										dFacturaFX.insumo.get(),
																										dFacturaFX.nroArticulo.get(),
																										dFacturaFX.nroReferencia);

							if (!insBD.getPrecioInsumo().equals(dFacturaFX.precio.get())) {
								insBD.setPrecioInsumo(dFacturaFX.precio.get());
								CRUD.actualizarObjeto(insBD);
								cambio = true;
							}
							if (!dFacturaBD.getPrecio().equals(dFacturaFX.precio.get())) {
								dFacturaBD.setPrecio(dFacturaFX.precio.get());
								cambio = true;
							}
							if (!dFacturaBD.getImporte().equals(dFacturaFX.importe.get())) {
								dFacturaBD.setImporte(dFacturaFX.importe.get());
								cambio = true;
							}
							if (cambio) {
								CRUD.actualizarObjeto(dFacturaBD);
								valido = true;
							}
						
						} else {
							
							cambio = false;

							System.out.println("remito: "+dFacturaFX.nroRemito.get());
							
							//si entra es xq es el nro remito q completo
							if (nroRemitoNuevo.equals(dFacturaFX.nroRemito.get())) {
								
								System.out.println(1);
								
//								//entonces traigo de la bd, ese detalleFactura, para actualizarle sus campos
								Insumo insBD = CRUD.obtenerInsumoPorNombreCategoriaLoteArticuloYReferencia(dFacturaFX.nombreInsumo.get(), 
																											dFacturaFX.idCategoria, 
																											dFacturaFX.insumo.get(),
																											dFacturaFX.nroArticulo.get(),
																											dFacturaFX.nroReferencia);
								
								if (!insBD.getStockActual().equals(insBD.getStockActual() + dFacturaFX.cantidad.get())) {
									insBD.setStockActual(insBD.getStockActual() + dFacturaFX.cantidad.get());
									cambio = true;
								}
								
								if (!insBD.getPrecioInsumo().equals(dFacturaFX.precio.get())) {
									insBD.setPrecioInsumo(dFacturaFX.precio.get());
									cambio = true;
								}
								
								if (cambio) {
									CRUD.actualizarObjeto(insBD);
									crearRemito(dFacturaFX, insBD);
									valido = true;
								}
								
								if (!dFacturaBD.getPrecio().equals(dFacturaFX.precio.get())) {
									dFacturaBD.setPrecio(dFacturaFX.precio.get());
									cambio = true;
								}
								
								if (!dFacturaBD.getImporte().equals(dFacturaFX.importe.get())) {
									dFacturaBD.setImporte(dFacturaFX.importe.get());
									cambio = true;
								}
								
								dFacturaBD.setRemito(this.getRemitoBD());
								
								if (cambio) {
									CRUD.actualizarObjeto(dFacturaBD);
									valido = true;
								}
								//guardo movimiento (ya q al completar el campo, se actualiza el stock en la bd)
								//y como ahora ya no se guarda el mov desde ingreso fac, si el campo remito esta vacio
								//ahora se guardara aca (xq antes causaba confusion)
								ControladorICsd_Principal.controllerTablaMovimientos.guardarMovimientoDesdeRemito(insBD, dFacturaFX.cantidad.get(), "DIAGNOS", txtArea.getText());
								System.out.println(2);

								} else { //era una fila perteneciente a un remito q ya vino cargado de bd (su campo no es editable)
										//pero si se le debe actualizar los demas datos
									cambio = false;
									
									Remito remBD = CRUD.obtenerRemitoPorNroRemito(dFacturaFX.nroRemito.get());
					            	Date fechaFacturaPicker = java.sql.Date.valueOf(dateFechaFactura.getValue());  //conversion de LocalDate a Date
					    			String fechaFacPicker = formato.format(fechaFacturaPicker);
					    			Date fechaFactura = formato.parse(fechaFacPicker);
					    			
					    			if (!remBD.getFechaRemito().equals(fechaFactura)) {
						    			remBD.setFechaRemito(fechaFactura);
						    			cambio = true;
									}
	
					    			if (remBD.getObservacionRemito().equals(txtArea.getText())) {
						    			remBD.setObservacionRemito(txtArea.getText());
						    			cambio = true;
									}
					    			
					    			if (cambio) {
					    				CRUD.actualizarObjeto(remBD);
						    			valido = true;
									}
					    			
					    			DetalleRemito drBD = CRUD.obtenerDetalleRemitoPorIdRemito(remBD.getIdRemito());
					    			
					    			if (!drBD.getProveedor().equals(pro)) {
						    			drBD.setProveedor(pro);
						    			cambio = true;
									}
					    			
					    			if (cambio) {
					    				CRUD.actualizarObjeto(drBD);
						    			CRUD.actualizarObjeto(dFacturaBD);
						    			valido = true;
									}
								}
						}
					}
				}
			} else { //si viene x aca significa q cambio campos proveedor y/o precio-importe, sin agregar remitos
				for (DetalleFacturaFX dFacturaFX : obListDetalleFactura) {	
					cambio = false;
					
					Insumo insBD = CRUD.obtenerInsumoPorNombreCategoriaLoteArticuloYReferencia(dFacturaFX.nombreInsumo.get(), 
																								dFacturaFX.idCategoria, 
																								dFacturaFX.insumo.get(),
																								dFacturaFX.nroArticulo.get(),
																								dFacturaFX.nroReferencia);
					if (!insBD.getPrecioInsumo().equals(dFacturaFX.precio.get())) {
						insBD.setPrecioInsumo(dFacturaFX.precio.get());
						cambio = true;
					}
					
					if (cambio) {
						CRUD.actualizarObjeto(insBD);
						valido = true;
					}
					//entonces traigo de la bd, ese detalleFactura, para actualizarle sus campos
					DetalleFactura dFacturaBD = CRUD.obtenerDetalleFacturaPorNumFila(dFacturaFX.numFila);	
					
					if (!dFacturaBD.getProveedor().getPkIdProveedor().equals(pro.getPkIdProveedor())) {
						dFacturaBD.setProveedor(null);
						dFacturaBD.setProveedor(pro);
						cambio = true;
					}
					if (!dFacturaBD.getPrecio().equals(dFacturaFX.precio.get())) {
						dFacturaBD.setPrecio(dFacturaFX.precio.get());
						cambio = true;
					}
					if (!dFacturaBD.getImporte().equals(dFacturaFX.importe.get())) {
						dFacturaBD.setImporte(dFacturaFX.importe.get());
						cambio = true;
					}
					if (cambio) {
						CRUD.actualizarObjeto(dFacturaBD);
						valido = true;
					}
					
					if (dFacturaFX.nroRemito.get() != null) { //tambien tengo q ver, si habian remitos ya cargados, tambien modificarle sus datos
						cambio = false;
						
						Remito remBD = CRUD.obtenerRemitoPorNroRemito(dFacturaFX.nroRemito.get());
		            	Date fechaFacturaPicker = java.sql.Date.valueOf(dateFechaFactura.getValue());  //conversion de LocalDate a Date
		    			String fechaFacPicker = formato.format(fechaFacturaPicker);
		    			Date fechaFactura = formato.parse(fechaFacPicker);
		    			
		    			if (!remBD.getFechaRemito().equals(fechaFactura)) {
			    			remBD.setFechaRemito(fechaFactura);
			    			cambio = true;
						}
		    			if (!remBD.getObservacionRemito().equals(txtArea.getText())) {
			    			remBD.setObservacionRemito(txtArea.getText());
			    			cambio = true;
		    			}
		    			if (cambio) {
			    			CRUD.actualizarObjeto(remBD);
			    			valido = true;
						}
		    			
		    			DetalleRemito drBD = CRUD.obtenerDetalleRemitoPorIdRemito(remBD.getIdRemito());
		    			
		    			if (!drBD.getProveedor().equals(pro)) {
			    			drBD.setProveedor(pro);
			    			cambio = true;
						}
		    			if (cambio) {
			    			CRUD.actualizarObjeto(drBD);
			    			valido = true;
						}
					}
				}
			}
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
    		
    		if (listaNroRemitos.isEmpty()) { //si entra es xq es el 1er remito a guardar
    			
    			//1ero creo el objeto remito
    			if (txtArea.getText().isEmpty()) {
    				observacion = "Sin comentarios";
				} else {
					observacion = txtArea.getText();
				}
    			
    			nuevoRemito = new Remito(detalleFacturaFX.nroRemito.get(), this.getFacturaBD(), this.getFacturaBD().getFechaFactura(), this.getFacturaBD().getFechaCarga(), observacion);
    			CRUD.guardarObjeto(nuevoRemito);
    			
    			//guardo referencia al nuevo objeto remito, para luego usarlo en el detalle factura
    			this.setRemitoBD(nuevoRemito);
    			
    			//luego el detalle remito
    			DetalleRemitoId id = new DetalleRemitoId(nuevoRemito.getIdRemito());
    			DetalleRemito dR = new DetalleRemito(id, insumoBD, this.getProveedorBD(), nuevoRemito, detalleFacturaFX.cantidad.get());
    			CRUD.guardarObjeto(dR);
    			
    			//guardo en una lista, el nro remito, para luego cuando siga con la siguiente fila, si los nro remito son diferentes
    			//tengo q crear nuevo objeto remito y su detalle
    			//pero si tienen igual nro remito, solo tengo q asignarle un nuevo detalle
    			listaNroRemitos.add(this.getRemitoBD().getNroRemito());
    		
    		} else {	//significa q la lista ya tiene algun nro remito guardado
    			
    			for (String nroRemito : listaNroRemitos) { //recorro la lista en donde almaceno los nro remito para compararlos con los del detalle factura

    				System.out.println("remito array: " + nroRemito + " --- " + "remmito detalle fac: " + detalleFacturaFX.nroRemito.get());
    				
    				if (!detalleFacturaFX.nroRemito.get().equals(nroRemito)) {
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
	    			DetalleRemito dR = new DetalleRemito(id, insumoBD, this.getProveedorBD(), remBD, detalleFacturaFX.cantidad.get());
	    			CRUD.guardarObjeto(dR);
				
    			} else { //son diferentes entonces guardo nuevo remito y su detalle
					
    				if (txtArea.getText().isEmpty()) {
        				observacion = "Sin comentarios";
    				} else {
    					observacion = txtArea.getText();
    				}
    				
    				nuevoRemito = new Remito(detalleFacturaFX.nroRemito.get(), this.getFacturaBD(), this.getFacturaBD().getFechaFactura(), this.getFacturaBD().getFechaCarga(), observacion);
	    			CRUD.guardarObjeto(nuevoRemito);
	    			
	    			//guardo referencia al nuevo objeto remito, para luego usarlo en el detalle factura
	    			this.setRemitoBD(nuevoRemito);
	    			
	    			//luego el detalle
	    			DetalleRemitoId id = new DetalleRemitoId(nuevoRemito.getIdRemito());
	    			DetalleRemito dR = new DetalleRemito(id, insumoBD, this.getProveedorBD(), nuevoRemito, detalleFacturaFX.cantidad.get());
	    			CRUD.guardarObjeto(dR);
	    			
	    			listaNroRemitos.add(nuevoRemito.getNroRemito());
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	public void cargarFacturaYdetalle(Integer idFacturaIN) {
    	obListDetalleFactura.clear();
		try {
			lblMsjErrorNroFactura.setVisible(false);
			final Factura factura = CRUD.obtenerFacturaPorId(idFacturaIN);
			completarCampos(factura);
			completarDetalleFactura(factura);
			textFieldnroDeFactura.requestFocus();
			//habilito botones guardar y cancelar
			btnGuardar.setDisable(false);
			btnCancelar.setDisable(false);
			habilitarCampos();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}    
    }

}
