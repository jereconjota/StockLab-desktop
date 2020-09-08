package controlador;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import modelo.Categoria;
import modelo.Insumo;
import modelo.Proveedor;
import modelo.Sector;
import modelo.Sucursal;
import modeloAux.InsumoFX;

public class ControladorIAltaInsumo {
	
	@FXML
    private JFXTextField txtFieldNombreInsumo;

    @FXML
    private Label lblMsjErrorUnidadMedida;

    @FXML
    private JFXTextField txtFieldTemperatura;

    @FXML
    private Label lblAltaInsumo;

    @FXML
    private JFXButton btnCancelarInsumo;

    @FXML
    private Label lblMsjErrorReferencia;

    @FXML
    private JFXRadioButton radioBtnActivoInsumo;

    @FXML
    private Label lblModifInsumo;

    @FXML
    private JFXRadioButton radioBtnSuspendidoInsumo;

    @FXML
    private JFXTextField txtFieldPrecio;

    @FXML
    private JFXTextField txtFieldNroLote;

    @FXML
    private Label lblMsjErrorNroLote;

    @FXML
    private JFXTextField txtFieldRefInsumo;

    @FXML
    private JFXComboBox<String> cBoxInsumoACate;
    
    @FXML
    private JFXComboBox<String> cBoxSector;

    @FXML
    private Label lblMsjErrorFechaVto;

    @FXML
    private Label lblEstadoInsumo;

    @FXML
    private DatePicker datePickerInsumo;

    @FXML
    private Label lblMsjErrorNombre;

    @FXML
    private JFXButton btnGuardarModifInsumo;

    @FXML
    private JFXTextField txtFieldPDP;

    @FXML
    private Label lblMsjErrorPDP;

    @FXML
    private Label lblMsjErrorTemperatura;

    @FXML
    private JFXTextField txtFieldUnidadMedida;

    @FXML
    private JFXButton btnGuardarInsumo;
    
    @FXML
    private JFXTextField txtFieldNroArticulo;
    
    @FXML
    private Label lblMsjErrorNroArticulo;
    
    @FXML
    private JFXTextField txtFieldStockActual;
    
    @FXML
    private Label lblStockActual;
    
    @FXML
    private Label lblErrorStockActual;
    
    @FXML
    private JFXTextField txtFieldNroPedido;
    
    
    private boolean ingresoRemito; //me sirve para saber si vengo al "ALTA", desde "ingreso remito" o desde "admin"
    
    private boolean ingresoFactura; //me sirve para saber si vengo al "ALTA", desde "ingreso factura" o desde "admin"
    
    private boolean ingresoOrdenDeCompra; //me sirve para saber si vengo al "ALTA", desde "Orden de Compra" o desde "admin"
    
    private boolean cerrarModif;
    
    private boolean seguirModif;
    
    private String nroLoteAux;
    
    private Integer idInsumoAux;
    
    private Insumo insumoBD;
    
    private String nombreSector = null; //es para saber luego en q posicion setear el comboBoxSector (cuando se usa el modificar insumo)
    
    private String nombreCategoria = null; //es para saber luego en q posicion setear el comboBoxCategoria
    
	private boolean categoriaInsumoSuspendido; //para saber si la categoria del insumo a modificar es suspendido, para agregarlo al comboBoxCategoria (ya q solo se llena con activos)
    
	private String nroArticuloAux;
	
	private String nroReferenciaAux;
	
	private boolean dioBaja;
	
	private Insumo insumoNuevo;
	
	private Integer idCategoria;   //este valor se setea en el controlador tabla abm insumos (metodo cargar insumos)
	
	private Integer idSector;   //este valor se setea en el controlador tabla abm insumos (metodo cargar insumos)
	
	private String nombreSectorSecundariaInsumos; //valor q se necesita cuando se da alta insumo, y luego se regresa a pantalla secundaria-insumos
	
	private Integer stockActualInsumo;
	
	private String incrementoDecremento;
	
	private boolean mostrarDialogo;
    
	@FXML
    private JFXComboBox<String> cBoxProveedor;
	
	//Creamos una lista global, ya que la usamos en varios metodos
	private List<Proveedor> listaProveedores;
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorIAltaInsumo() {
	
    }
    
    
    
    
    /**************************** GET - SET **********************************/
    
    
    public Integer getIdSector() {
		return idSector;
	}
    
    public void setIdSector(Integer idSector) {
		this.idSector = idSector;
	}
    

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

    
    private boolean getIngresoRemito() {
		return ingresoRemito;
	}
    
	public void setIngresoRemito(boolean ingresoRemito) {
		this.ingresoRemito = ingresoRemito;
	}

	
	private boolean getIngresoFactura() {
		return ingresoFactura;
	}
	
	public void setIngresoFactura(boolean ingresoFactura) {
		this.ingresoFactura = ingresoFactura;
	}


	public Label getLblAltaInsumo() {
		return lblAltaInsumo;
	}


	public Label getLblModifInsumo() {
		return lblModifInsumo;
	}


	public JFXButton getBtnGuardarModifInsumo() {
		return btnGuardarModifInsumo;
	}


	public JFXButton getBtnGuardarInsumo() {
		return btnGuardarInsumo;
	}


	public JFXTextField getTxtFieldNroLote() {
		return txtFieldNroLote;
	}


	public JFXTextField getTxtFieldStockActual() {
		return txtFieldStockActual;
	}


	public Label getLblStockActual() {
		return lblStockActual;
	}

	
	public Integer getIdInsumoAux() {
		return idInsumoAux;
	}

	public void setIdInsumoAux(Integer idInsumoAux) {
		this.idInsumoAux = idInsumoAux;
	}


	public void setNroLoteAux(String nroLoteAux) {
		this.nroLoteAux = nroLoteAux;
	}


	public String getNroArticuloAux() {
		return nroArticuloAux;
	}
	
	public void setNroArticuloAux(String nroArticuloAux) {
		this.nroArticuloAux = nroArticuloAux;
	}

	
	public String getNroReferenciaAux() {
		return nroReferenciaAux;
	}
	
	public void setNroReferenciaAux(String nroReferenciaAux) {
		this.nroReferenciaAux = nroReferenciaAux;
	}




	public boolean isIngresoOrdenDeCompra() {
		return ingresoOrdenDeCompra;
	}

	public void setIngresoOrdenDeCompra(boolean ingresoOrdenDeCompra) {
		this.ingresoOrdenDeCompra = ingresoOrdenDeCompra;
	}




	/********************************** METODOS ***********************************/
    
    @FXML
    private void initialize() {
    	setearRadioActivo();
    	restringirFechas();
    	setearToolTipTxtFieldArticulo();
    }
    
    
    @FXML
    void guardarInsumo(ActionEvent event) {
    	boolean cerrarAlta = false;
		insumoNuevo = null;
		pintarUnFocusTxtFields();
		try {
			insumoNuevo = nuevoInsumo();
	
			if (insumoNuevo != null) {
				CRUD.guardarObjeto(insumoNuevo);
				cerrarAlta = true;
			} //sino entro es o xq algun campo obligatorio estaba vacio o xq alguno de sus campos tenia datos invalidos
			  //xq lo no creo ningun objeto y tampoco se guarda algo en la bd
			
			if (cerrarAlta) { //osea q se hizo todo correctamente, entonces debe volver a la pantalla "admin" o "ingreso-remito" o "ingreso-fac"
	    		limpiarCampos("");
	        	
	        	if (this.getIngresoFactura()) { //si entra entonces vuelve a secundaria-insumos
	        		
	        		ControladorICsd_Principal.controllerIIngresoFactura.getControllerSecundariaInsumos().
	        															agregarInsumoAFila(insumoNuevo, "InsumoNuevo", nombreSectorSecundariaInsumos);
	        		//y agrego insumo nuevo a la tabla de insumos
	        		//no hace falta actualizarla, xq si vino hasta aca, es xq no se encontraba en dicha tabla
	        		InsumoFX insFX = new InsumoFX(insumoNuevo);
	        		insFX.nombreSector.set(nombreSectorSecundariaInsumos);
	        		ControladorICsd_Principal.controllerIIngresoFactura.getControllerSecundariaInsumos().getObListInsumos().add(insFX);
	        		ControladorICsd_Principal.controllerIIngresoFactura.getControllerSecundariaInsumos().getObListInsumosAux().add(insFX);
	    			
				} else {
					
					if (this.getIngresoRemito()) { //si entra entonces vuelve a ingreso-remito

		        		ControladorICsd_Principal.controllerIIngresarRemito.getControllerSecundariaInsumosRemito().
		        															agregarInsumoAFila(insumoNuevo, "InsumoNuevo", nombreSectorSecundariaInsumos);
		        		//y agrego insumo nuevo a la tabla de insumos
		        		//no hace falta actualizarla, xq si vino hasta aca, es xq no se encontraba en dicha tabla
		        		InsumoFX insFX = new InsumoFX(insumoNuevo);
		        		insFX.nombreSector.set(nombreSectorSecundariaInsumos);
		        		ControladorICsd_Principal.controllerIIngresarRemito.getControllerSecundariaInsumosRemito().getObListInsumos().add(insFX);
		        		ControladorICsd_Principal.controllerIIngresarRemito.getControllerSecundariaInsumosRemito().getObListInsumosAux().add(insFX);
						
					} else { //sino vuelve a pantalla admin
					
						ControladorICsd_Principal.controllerPantAdmin.actualizarTablaInsumosGral();
						
					}
				}
			}
			
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
    }
    
    
    @FXML
    void cancelarAltaInsumo(ActionEvent event) {
    	if (this.getIngresoFactura()) { //esto me sirve para saber si tengo q volver a mostrar pantalla "secundaria-insumos" o "admin"
			ControladorICsd_Principal.controllerIIngresoFactura.getControllerSecundariaInsumos().volver();
		} else {
			
			if (this.getIngresoRemito()) { //para saber si tengo q regresar a pantalla ingreso-remito
				ControladorICsd_Principal.controllerIIngresarRemito.getControllerSecundariaInsumosRemito().volver();
			} 
			
			else {
				//me fijo si esta en "modif insumo", si es asi.. entonces vuelvo a la tabla q muestra a 
				//los insumos de un tipo en especifico
				if (lblModifInsumo.isVisible()) {
					ControladorICsd_Principal.controllerPantAdmin.actualizarTablaInsumos(this.getIdSector(), this.getIdCategoria());
					
				} else { //sino, es xq estaba en "alta", vuelve a tabla gral de insumos
					ControladorICsd_Principal.controllerPantAdmin.actualizarTablaInsumosGral();
				}
			}
		}
		limpiarCampos("");
    }
    
    
    @FXML
    void guardarModifInsumo(ActionEvent event) {
    	String estadoInsumoAux = null;
		cerrarModif = false;
		
		//utilizo un valor global, para saber luego de la actualizacion del insumo, si es necesario mostrar o no
		//el dialogo para q ingrese una observacion, ya q si no modifico el stock actual del insumo
		//no es necesario q lo muestre
		mostrarDialogo = false;
		
		pintarUnFocusTxtFields();
		try {
			Insumo insumoModif = leerCampos();
			
			if (insumoModif != null) {
				verificarDatosParaLaModificacion(insumoModif, estadoInsumoAux);
			}
			
			if (cerrarModif) {
				//luego verifico si estaba visible el campo stock
				//si es asi, tonce tengo q mostrar una ventana en donde se agregara una observacion, de xq hizo dicho cambio
				//el boolean "mostrarDialogo", se setea en el metodo leerCampos()
				if (mostrarDialogo) {
					mostrarDialogoIngreseObservacion();
				}
	    		limpiarCampos("");
	    		ControladorICsd_Principal.controllerPantAdmin.actualizarTablaInsumos(this.getIdSector(), this.getIdCategoria());
			}
			
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
    }
    
    
    @FXML
    void desactivarRadioSuspendido(ActionEvent event) {
    	radioBtnActivoInsumo.setSelectedColor(Color.web("#0098cd"));
    	radioBtnActivoInsumo.setSelected(true);
		radioBtnSuspendidoInsumo.setSelected(false);
    }

    
    @FXML
    void desactivarRadioActivo(ActionEvent event) {
    	radioBtnSuspendidoInsumo.setSelectedColor(Color.web("#0098cd"));
    	radioBtnSuspendidoInsumo.setSelected(true);
    	radioBtnActivoInsumo.setSelected(false);
    }
    
    
  //manejador del evento q genera el "ENTER" del textField NroArticulo
    //el cual..buscara dicho articulo en la bd..y si esta completa todos los campos automaticamente
    @FXML
    void enterTxtFieldNroArticulo(KeyEvent event) {
    	Insumo insumo = null;
		try {
			if (event.getCode().equals(KeyCode.ENTER)){	
	    		//1ero limpio todos los campos por las dudas..
	        	//como uso el mismo metodo para limpiar en general.. le aviso q voy del txtField nroArticulo
	        	//para q no limpie el texto nroArticulo
	    		limpiarCampos("ENTER");
	    		
	    		//1ero verifico q no este vacio y no tenga blanco
	    		if (!(txtFieldNroArticulo.getText().isEmpty())) {
	    			
	    			if (!(hayBlanco(txtFieldNroArticulo.getText()))) {
	    				//si entra, entonces busco dicho articulo, en la bd..si esta completo solo los campos q no varian
	    				
	    				insumo = CRUD.obtenerInsumoPorNroArticulo(txtFieldNroArticulo.getText());
	    				
	    				if (insumo != null) {
	    					llenarCamposNoVariantes(insumo);
						}
					}
	    		}
	    	}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    @FXML
	void llenarComboBoxCategoria() {
    	cBoxInsumoACate.setDisable(false);
		Sector sec = null;
		try {
			if (cBoxSector.getSelectionModel().getSelectedItem() != null) {
				
				ObservableList<String> itemsCombo = FXCollections.observableArrayList();
				
				sec = CRUD.obtenerSectorPorNombre(cBoxSector.getSelectionModel().getSelectedItem());
				
				List<Categoria> listaCategorias = CRUD.obtenerListaCategoriasActivasPorIdSector(sec.getIdSector());
				
				for (Categoria categoria : listaCategorias) {
					itemsCombo.add(categoria.getNombreCategoria());
				}
				
				cBoxInsumoACate.setItems(itemsCombo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
  //consiste en aplicar al datePicker, el formato de fecha "dia-mes-anio"
  	@FXML
  	void formatearFormatoFecha() {
    	datePickerInsumo.setConverter(new StringConverter<LocalDate>() {
   	     String pattern = "dd-MM-yyyy";
   	     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

   	     {
   	         datePickerInsumo.setPromptText(pattern.toLowerCase());
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
  	

	private void setearRadioActivo() {
    	if (lblAltaInsumo.isVisible()) { //si entra entonces seteo el radio Activos
			radioBtnActivoInsumo.setSelected(true);
			radioBtnActivoInsumo.setSelectedColor(Color.web("#0098cd"));
		}
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
		                           
		                            if (item.isBefore(
		                            		LocalDate.now().plusDays(1))
		                                ) {
		                                    setDisable(true);
		                                    setStyle("-fx-background-color: #ffc0cb;");
		                            }   
		                    }
		                };
		            }
		        };
		        
		        datePickerInsumo.setDayCellFactory(dayCellFactory);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	        
	}

    
    private void setearToolTipTxtFieldArticulo() {
		txtFieldNroArticulo.setTooltip(new Tooltip("Luego de ingresar Articulo" + "\n" 
													+ "Presione ENTER, para autocompletar los campos"));
	}
    
  	
  	public void llenarComboSector() {
		try {
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
			List<Sector> listaSectores = CRUD.obtenerListaSectoresActivos();
			
			for (Sector sector : listaSectores) {
				if (!(sector.getNombreSector().equals("Administracion"))) {
					itemsCombo.add(sector.getNombreSector());
				}
			}
			cBoxSector.setItems(itemsCombo);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
  	
  	
  //el argumento "enter", es para saber si vengo del evento q genera el completar del txtField nroArticulo
  //para q no limpie el nroArticulo	
  	public void limpiarCampos(String enter) {
  		if (!enter.equals("ENTER")) {
  			txtFieldNroArticulo.clear();
		}
  		txtFieldNombreInsumo.clear();
		txtFieldNroLote.clear();
		txtFieldPDP.clear();
		txtFieldPrecio.clear();
		txtFieldRefInsumo.clear();
		txtFieldTemperatura.clear();
		txtFieldUnidadMedida.clear();
		radioBtnActivoInsumo.setSelected(true);
		radioBtnSuspendidoInsumo.setSelected(false);
		cBoxSector.getSelectionModel().select(-1);
		cBoxInsumoACate.setDisable(true);
		cBoxInsumoACate.getSelectionModel().select(-1);
		datePickerInsumo.getEditor().clear();
		datePickerInsumo.setValue(null);
		pintarUnFocusTxtFields();
		txtFieldStockActual.clear();
		txtFieldStockActual.setVisible(false);
		lblStockActual.setVisible(false);
  		
		cBoxProveedor.getSelectionModel().select(-1);
		txtFieldNroPedido.clear();
  	}
  	
  	
  	private void pintarUnFocusTxtFields() {
		txtFieldNombreInsumo.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldNroLote.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldNroArticulo.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldPDP.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldPrecio.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldRefInsumo.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldTemperatura.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldUnidadMedida.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldStockActual.setUnFocusColor(Color.web("#4d4d4d"));
		cBoxSector.setUnFocusColor(Color.web("#4d4d4d"));
		cBoxInsumoACate.setUnFocusColor(Color.web("#4d4d4d"));
		setearVisibilidadLblMejError();
		
		cBoxProveedor.setUnFocusColor(Color.web("#4d4d4d"));
	}
  	
  	
  	private void setearVisibilidadLblMejError() {
		lblMsjErrorFechaVto.setVisible(false);
		lblMsjErrorNombre.setVisible(false);
		lblMsjErrorNroLote.setVisible(false);
		lblMsjErrorNroArticulo.setVisible(false);
		lblMsjErrorPDP.setVisible(false);
		lblMsjErrorReferencia.setVisible(false);
		lblMsjErrorTemperatura.setVisible(false);
		lblMsjErrorUnidadMedida.setVisible(false);
		lblErrorStockActual.setVisible(false);
	}
  	
  	
  	private boolean hayBlanco(String campo) {
		boolean hay = false;

		if (campo.substring(0, 1).equals(" ")) {
			hay = true;
		}
		return hay;
	}
  	
  	
  	private void llenarCamposNoVariantes(Insumo insumoIN) {   	
    	txtFieldNombreInsumo.setText(insumoIN.getNombreInsumo());
    	
    	// normalmente los insumos tienen esto:
    	// nroArticulo = referencia
    	// pero hay algunos q creo q no tienen esa igualdad de datos 
    	if (insumoIN.getReferencia() != null) {
    		txtFieldRefInsumo.setText(insumoIN.getReferencia());
		}
    	
    	//debo verificar q si son "null" sos valores, tonce q no haga nada
    	if (insumoIN.getPrecioInsumo() != null) {
    		txtFieldPrecio.setText(String.valueOf(insumoIN.getPrecioInsumo()));
		}
    	if (insumoIN.getUnidadMedida() != null) {
    		txtFieldUnidadMedida.setText(insumoIN.getUnidadMedida());
		}
    	if (insumoIN.getTemperatura() != null) {
    		txtFieldTemperatura.setText(insumoIN.getTemperatura());
		}
    	//con este campo no es necesario xq es un valor q no admite null
    	txtFieldPDP.setText(String.valueOf(insumoIN.getPdp()));
    	
    	buscarSector_CategoriaYsetearCombo(insumoIN);
    	
    	if (insumoIN.getNroPedido() != null) {
			txtFieldNroPedido.setText(String.valueOf(insumoIN.getNroPedido()));
		}
	}
  	
  	
  	private void buscarSector_CategoriaYsetearCombo(Insumo insumoModif) {
		buscarCategoria(insumoModif);
		buscarSector();
		if (categoriaInsumoSuspendido) {
			cBoxInsumoACate.getItems().add(nombreCategoria);
		}
		setearPosicionComboBoxCategoria();
		cBoxInsumoACate.setDisable(false);
	}
  	
  	
  	private void buscarCategoria(Insumo insumoModif) {
		categoriaInsumoSuspendido = false;
		Sector sec = null;
		try {
			Categoria categoria = CRUD.obtenerCategoriaPorId(insumoModif.getCategoria().getPkIdCategoria());
			
			nombreCategoria = categoria.getNombreCategoria();
			
			sec = CRUD.obtenerSectorPorId(categoria.getSector().getIdSector());
			nombreSector = sec.getNombreSector(); //este es para luego saber donde setear en el comboBoxSector
			//antes de salir del bucle, me fijo si el estado de esa categoria es "Suspendido"
			//xq si es asi, no estara dentro del comboBoxCategoria.. y dara error (xq solo lleno con los activos)
			//entonces.. debo agregarlo al comboBoxCategoria
			if (categoria.getEstadoCategoria().equals("Suspendido")) {
				categoriaInsumoSuspendido = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
  	
  	
  	private void buscarSector() {
		Integer c = 0;
		boolean encontro = false;
		try {
			//antes q nada, verifico si el estado del sector esta en "Suspendido",
			//si es asi, lo agrego al comboBoxSector (xq solamente meto los sectores "Activo")
			
			Sector sector = CRUD.obtenerSectorPorNombre(nombreSector);
			
			if (sector.getEstadoSector().equals("Suspendido")) {
				cBoxSector.getItems().add(nombreSector);
			}
			
			while ((c < cBoxSector.getItems().size()) && (!(encontro))) { //recorro el comboBox sector
				if (cBoxSector.getItems().get(c).equals(nombreSector)) { //busco el sector al q pertenece la categoria, y si esta, seteo el combo en esa direccion
					cBoxSector.getSelectionModel().select(c);
					//luego llenar el comboCategoria solo con las categorias corrospondientes al sector
					llenarComboBoxCategoria();
					encontro = true;
				} else {
					c = c + 1;
				}
			}
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}
  	
  	
  	private void setearPosicionComboBoxCategoria() {
		Integer c = 0;
		boolean encontro = false;
		try {
			while ((c < cBoxInsumoACate.getItems().size()) && (!(encontro))) { //recorro el comboBox categoria
				if (cBoxInsumoACate.getItems().get(c).equals(nombreCategoria)) { //busco la categoria a la q pertenece el insumo, y si esta, seteo el combo en esa direccion
					cBoxInsumoACate.getSelectionModel().select(c);
					encontro = true;
				} else {
					c = c + 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
  	
  	
  	private Insumo nuevoInsumo() {
		Categoria categoria = null; //es necesario, para determinar a q categoria pertenece dicho insumo
		String nombreCategoria = null;
		String estado = null;
		String nombreInsumo = null;
		String referencia = null;
		String nroLote = null;
		String nroArticulo = null;
		Float precio = null;
		String unidadMedida = null;
		String temperatura = null;
		Integer pdp = null;
		Date fechaVto = null;
		Date fechaIngreso = new Date();
		Integer nroPedido = null;
		
		Proveedor proveedor = null;
		
		if (!(txtFieldNombreInsumo.getText().isEmpty())) {
			nombreInsumo = txtFieldNombreInsumo.getText();
		}
		
		if (!(txtFieldRefInsumo.getText().isEmpty())) {
			referencia = txtFieldRefInsumo.getText();
		}
		
		if (!(txtFieldNroLote.getText().isEmpty())) {
			nroLote = txtFieldNroLote.getText();
		} else {
			nroLote = "NA"; //si el lote lo dejan vacio, x defecto le seteo NA(no aplica)
		}
		
		if (!(txtFieldNroArticulo.getText().isEmpty())) {
			nroArticulo = txtFieldNroArticulo.getText();
		}
		
		if (!(txtFieldPrecio.getText().isEmpty())) {
			precio = Float.parseFloat(txtFieldPrecio.getText());
		}
		
		if (!(txtFieldUnidadMedida.getText().isEmpty())) {
			unidadMedida = txtFieldUnidadMedida.getText();
		}
		
		if (!(txtFieldTemperatura.getText().isEmpty())) {
			temperatura = txtFieldTemperatura.getText();
		}
		
		if (!(txtFieldPDP.getText().isEmpty())) { 
			pdp = Integer.valueOf(txtFieldPDP.getText());
		}
		
		if (datePickerInsumo.getValue() != null) {
			fechaVto = java.sql.Date.valueOf(datePickerInsumo.getValue());  //conversion de LocalDate a Date
		}
	
		if (cBoxSector.getSelectionModel().isEmpty()) {
			cBoxSector.setUnFocusColor(Color.RED);
		}
		
		if (cBoxInsumoACate.getSelectionModel().isEmpty()) {//osea sino selecciono una categoria, entonces pinto el combo de rojo
			cBoxInsumoACate.setUnFocusColor(Color.RED);
		} else {
			nombreCategoria = cBoxInsumoACate.getSelectionModel().getSelectedItem().toString();
			categoria = obtenerCategoria(cBoxSector.getSelectionModel().getSelectedItem(), nombreCategoria);
		}
    	
    	if (radioBtnActivoInsumo.isSelected()) {
			estado = "Activo";
		}
    	if (radioBtnSuspendidoInsumo.isSelected()) {
			estado = "Suspendido";
		}
    	
    	if (!(txtFieldNroPedido.getText().isEmpty())) {
			nroPedido = Integer.valueOf(txtFieldNroPedido.getText());
		}
    	
    	if (camposObligatoriosLlenos(categoria, nombreInsumo, pdp)) {
    		
    	//creo objeto solo si completo los campos obligatorios y son validos
    	// (categoria y pdp) no es necesario validar xq, el valor de categoria viene de un comboBox correcto
    	// y pdp, es un txtField q solo admite numeros (no toma, blancos y letras), pero es necesario consistir
    	//	q no sea CERO el PDP
    		
    		if (datosValidos(nombreInsumo, pdp)) { //si todo correcto, entonces recien creo el objeto
    			insumoNuevo = new Insumo(categoria, nombreInsumo, pdp, fechaIngreso, estado);
    			
    			//seteo x defecto a "CantidadStock" y "StockActual" en cero
    			insumoNuevo.setStockActual(0);
        	
        		//luego verificar si completo algo de lo opcional para setearle al insumo nuevo
        		//y tambien verificar si son validos los datos
    			verificarCamposOpcionales(nroLote, nroArticulo, referencia, fechaVto, precio, unidadMedida, temperatura, nroPedido);
    			
    			if (insumoNuevo != null) {
    				//x ahora a insumo le asigno x defecto la SUCURSAL "DIAGNOS"
    				Sucursal sucursalDiagnos = CRUD.obtenerSucursal();
    				insumoNuevo.setSucursal(sucursalDiagnos);
    				
    				///////////////////////////////////////////////////////////////////////////
    				proveedor = CRUD.obtenerProveedorPorNombre(cBoxProveedor.getSelectionModel().getSelectedItem());
    				insumoNuevo.setProveedor(proveedor);
				}
				
    			//verifico si esta tratando de dar de alta un insumo repetido
    			verificarInsumoNuevo();
    			
			} //si algun datos invalido, no crea nada y muestra msj error
    		
		} //si algun campo obligatorio vacio, no crea nada y muestra msj error
    	
		return insumoNuevo;
	}
  	
  	
  	private Categoria obtenerCategoria(String nombreSectorIN, String nombreCategoriaIN) {
		Categoria categoria = null;
		try {
			Sector sec = CRUD.obtenerSectorPorNombre(nombreSectorIN);
			
			nombreSectorSecundariaInsumos = sec.getNombreSector();
			
			categoria = CRUD.obtenerCategoriaPorNombreCategoriaYPorIdSector3(nombreCategoriaIN, sec.getIdSector());
			
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return categoria;
	}
  	
  	
  	private boolean camposObligatoriosLlenos(Categoria categoria, String nombreInsumo,Integer pdp) {
		boolean validos = true;
		
		if (categoria == null) {
			validos = false;
		}
		if (nombreInsumo == null) {
			txtFieldNombreInsumo.setUnFocusColor(Color.RED);
			lblMsjErrorNombre.setText("Campo obligatorio");
			lblMsjErrorNombre.setVisible(true);
			validos = false;
		}
		if (pdp == null) {
			txtFieldPDP.setUnFocusColor(Color.RED);
			lblMsjErrorPDP.setText("Campo obligatorio");
			lblMsjErrorPDP.setVisible(true);
			validos = false;
		}
		if (this.cBoxProveedor.getSelectionModel().getSelectedIndex()==-1) {
			this.cBoxProveedor.setUnFocusColor(Color.RED);
			validos = false;
		}
		return validos;
	}
  	
  	
  	private boolean datosValidos(String nombreInsumo, Integer pdp) {
		boolean validos = true;
		
		if (hayBlanco(nombreInsumo)) {
			txtFieldNombreInsumo.setUnFocusColor(Color.RED);
			lblMsjErrorNombre.setText("No se admite espacios antes del Nombre");
			lblMsjErrorNombre.setVisible(true);
			validos = false;
		}
		
		if (pdp == 0) {
			lblMsjErrorPDP.setText("No se admite un PDP igual a cero");
			lblMsjErrorPDP.setVisible(true);
			validos = false;
		}
		
		return validos;
	}
  	
  	
  	private void verificarCamposOpcionales(String nroLote, String nroArticulo, String referencia,
			Date fechaVto, Float precio, String unidadMedida, String temperatura, Integer nroPedido) {

		Integer flag = 0;  //para saber si tengo q poner el insumo nuevo en null, 0-->valido, 1-->no valido
		
		if (hayBlanco(nroLote)) {
			txtFieldNroLote.setUnFocusColor(Color.RED);
			lblMsjErrorNroLote.setText("No se admite espacios antes del Nro de Lote");
			lblMsjErrorNroLote.setVisible(true);
			//insumoNuevo = null;
			flag = 1;
		} else {
			insumoNuevo.setNroLote(nroLote);
		}
		
		if (nroArticulo != null) {
			if (hayBlanco(nroArticulo)) {
				txtFieldNroArticulo.setUnFocusColor(Color.RED);
				lblMsjErrorNroArticulo.setText("No se admite espacios antes del Nro de Articulo");
				lblMsjErrorNroArticulo.setVisible(true);
				//insumoNuevo = null;
				flag = 1;
			} else {
				if (nroArticuloUsado(nroArticulo, insumoNuevo.getNombreInsumo())) {
					txtFieldNroArticulo.setUnFocusColor(Color.RED);
					lblMsjErrorNroArticulo.setText("Ya se encuentra en uso");
					lblMsjErrorNroArticulo.setVisible(true);
					//insumoNuevo = null;
					flag = 1;
				} else {
					insumoNuevo.setNroArticulo(nroArticulo);
				}
			}
		} 
		
		if (referencia != null) {
			if (hayBlanco(referencia)) {
				txtFieldRefInsumo.setUnFocusColor(Color.RED);
				lblMsjErrorReferencia.setText("No se admite espacios antes de la Referencia");
				lblMsjErrorReferencia.setVisible(true);
				//insumoNuevo = null;
				flag = 1;
			} else {
				if (nroReferenciaUsado(referencia, insumoNuevo.getNombreInsumo())) {
					txtFieldRefInsumo.setUnFocusColor(Color.RED);
					lblMsjErrorReferencia.setText("Ya se encuentra en uso");
					lblMsjErrorReferencia.setVisible(true);
					//insumoNuevo = null;
					flag = 1;
				} else {
					insumoNuevo.setReferencia(referencia);
				}
			}
		}
		
		//luego veo si completo articulo y no ref (le seteo a ref lo de articulo) o viceversa
		if ((nroArticulo == null) && (referencia == null)) {	//los campos estan vacios, entonces le seteo el nombre del insumo
			insumoNuevo.setNroArticulo(insumoNuevo.getNombreInsumo());
			insumoNuevo.setReferencia(insumoNuevo.getNombreInsumo());
		} else if(nroArticulo != null && referencia == null) {
			insumoNuevo.setReferencia(nroArticulo);
			}else if(nroArticulo == null && referencia != null){
				insumoNuevo.setNroArticulo(referencia);
				}
		
		if (fechaVto != null) {
			insumoNuevo.setFechaVencimiento(fechaVto);
		} 
		
		if (precio != null) { //este no es necesario verificar blancos, xq solo admitira numeros (no toma blancos y letras)
			insumoNuevo.setPrecioInsumo(precio);
		} else {
			insumoNuevo.setPrecioInsumo(Float.valueOf("0.0"));
		}
		
		if (unidadMedida != null) {
			if (hayBlanco(unidadMedida)) {
				txtFieldUnidadMedida.setUnFocusColor(Color.RED);
				lblMsjErrorUnidadMedida.setVisible(true);
				//insumoNuevo = null;
				flag = 1;
			} else {
				insumoNuevo.setUnidadMedida(unidadMedida);
			}
		}
		
		if (temperatura != null) {
			if (hayBlanco(temperatura)) {
				txtFieldTemperatura.setUnFocusColor(Color.RED);
				lblMsjErrorTemperatura.setVisible(true);
				//insumoNuevo = null;
				flag = 1;
			} else {
				insumoNuevo.setTemperatura(temperatura);
			}
		}
		
		if (nroPedido != null) { //este no es necesario verificar blancos, xq solo admitira numeros (no toma blancos y letras)
			insumoNuevo.setNroPedido(nroPedido);
		} else {
			insumoNuevo.setNroPedido(0);
		}
		
		if (flag == 1) {
			insumoNuevo = null;
		}
	}
  	

	///verfica q el nroArticulo, q se ingrese no este siendo usado x otro tipo de insumo..
  	//ya q x mas q se pueda repetir en la BD.. solo es posible para un solo tipo de insumo.
  	// xq ej, el NroArticulo "A1" --> hace referencia al "ARROZ",
  	// pero "ARROZ" puede tener distintos NroArticulo "A1, A2, A3, ..."
  	private boolean nroArticuloUsado(String nroArticuloIN, String nombreInsumoIN) {
  		boolean usado = false;
  		List<Insumo> listaInsumos = null;
  		try {
  			
  			listaInsumos = CRUD.obtenerListaInsumosPorNroArticulo(nroArticuloIN);
  			
  			if (listaInsumos != null) {
				
  				for (Insumo insumo : listaInsumos) {
  					
  					if (!(insumo.getNombreInsumo().equals(nombreInsumoIN))) {
  						
  						//si entro significa.. siguiendo el ejemplo de arriba de "ARROZ"
  						//es como si el user, hubiera querido asignar el NroArticulo "A1"---> a "FIDEOS"
  						//lo cual esta mal.. xq "A1" es unico para "ARROZ"
  						usado = true;
  						break;
  					}
  					
				}
			}
  			
  		} catch (Exception e) {
  			e.getMessage();
  			e.printStackTrace();
  		}
  		return usado;
  	}
  	
  	
  	private boolean nroReferenciaUsado(String nroReferenciaIN, String nombreInsumoIN) {
  		boolean usado = false;
  		List<Insumo> listaInsumos = null;
  		try {
  			listaInsumos = CRUD.obtenerListaInsumosPorNroReferencia(nroReferenciaIN);
  			
  			if (listaInsumos != null) {
				
  				for (Insumo insumo : listaInsumos) {
					
  					if (!(insumo.getNombreInsumo().equals(nombreInsumoIN))) {
  						usado = true;
  						break;
  					}
  					
				}
			}
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
  		return usado;
  	}
  	
  	
  	private void verificarInsumoNuevo() {
		String nombreInsumo = null;
		Insumo insumo = null;
		try {
			if (insumoNuevo != null) {
				nombreInsumo = insumoNuevo.getNombreInsumo();
				
				insumo = CRUD.obtenerInsumoPorNombrePorNroLotePorIdCategoria(insumoNuevo.getNombreInsumo(), 
																				insumoNuevo.getNroLote(), 
																				insumoNuevo.getCategoria().getPkIdCategoria());
				
				if (insumo != null) {	//si trae algo significa q ya existe dicho insumo en la BD
					insumoNuevo = null;
					mostrarMsjNuevoInsumo(nombreInsumo);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
  	
  	
  	private void mostrarMsjNuevoInsumo(String nombreInsumoIN) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
		alert.setHeaderText("El insumo: " + nombreInsumoIN);
		alert.setContentText("No puede darse de alta, porque ya se encuentra cargado en el sistema");

		alert.showAndWait();
	}
  	
  	
  	private Insumo leerCampos() {
		Insumo insumoModif = new Insumo();
		String estado = null;
		boolean valido = true;
		try {
			insumoModif.setNroLote(txtFieldNroLote.getText());
			
			if (!(txtFieldNombreInsumo.getText().isEmpty())) {
				insumoModif.setNombreInsumo(txtFieldNombreInsumo.getText());
			} else {
				insumoModif.setNombreInsumo(null);
			}
			
			
			if (!(datePickerInsumo.getEditor().getText().isEmpty())) {
				SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
				insumoModif.setFechaVencimiento(formato.parse(datePickerInsumo.getEditor().getText())); //conversion de String a Date
			}
			
			if (!(txtFieldPDP.getText().isEmpty())) {
				insumoModif.setPdp(Integer.valueOf(txtFieldPDP.getText()));
			} else {
				insumoModif.setPdp(null);
			}
			
			String nombreCategoria = cBoxInsumoACate.getSelectionModel().getSelectedItem().toString();
			Categoria categoria = obtenerCategoria(cBoxSector.getSelectionModel().getSelectedItem(), nombreCategoria);
			insumoModif.setCategoria(categoria);
			if (radioBtnActivoInsumo.isSelected()) {
				estado = "Activo";
			}
	    	if (radioBtnSuspendidoInsumo.isSelected()) {
				estado = "Suspendido";
			}
			insumoModif.setEstadoInsumo(estado);
			
			//verifico si hay campos opcionales con datos
			if (!(txtFieldNroArticulo.getText().isEmpty())) {
				insumoModif.setNroArticulo(txtFieldNroArticulo.getText());
			}
			if (!(txtFieldRefInsumo.getText().isEmpty())) {
				insumoModif.setReferencia(txtFieldRefInsumo.getText());
			}
			if (!(txtFieldPrecio.getText().isEmpty())) {
				insumoModif.setPrecioInsumo(Float.valueOf(txtFieldPrecio.getText()));
			}
			if (!(txtFieldUnidadMedida.getText().isEmpty())) {
				insumoModif.setUnidadMedida(txtFieldUnidadMedida.getText());
			}
			if (!(txtFieldTemperatura.getText().isEmpty())) {
				insumoModif.setTemperatura(txtFieldTemperatura.getText());
			}
			
			if (!(txtFieldNroPedido.getText().isEmpty())) {
				insumoModif.setNroPedido(Integer.valueOf(txtFieldNroPedido.getText()));
			} else {
				insumoModif.setNroPedido(null);
			}
			
			insumoModif.setProveedor(CRUD.obtenerProveedorPorNombre(cBoxProveedor.getSelectionModel().getSelectedItem()));
			
			//luego verifico si esta visible el campo stock actual
			if (txtFieldStockActual.isVisible()) {
				//utilizo un valor global, para saber luego de la actualizacion del insumo, si es necesario mostrar o no
				//el dialogo para q ingrese una observacion, ya q si no modifico el stock actual del insumo
				//no es necesario q lo muestre
				//mostrarDialogo
				
				//verifico si incremento o decremento
				//el valor "stockActualInsumo" se setea cuando se completa la pantalla modif insumo
				if (txtFieldStockActual.getText().isEmpty()) { //significa q borro el campo stock (lo cual no es aceptable xq dejaria el stock en null)
					
					txtFieldStockActual.setUnFocusColor(Color.RED);
					lblErrorStockActual.setText("Ingrese cantidad");
					lblErrorStockActual.setVisible(true);
					
					valido = false;
					
				} else {
					
					Integer nuevoStock = Integer.valueOf(txtFieldStockActual.getText());
					incrementoDecremento = null;
					
					if (nuevoStock > stockActualInsumo) { //si es mayor, significa q incremento el stock
						incrementoDecremento = "Incremento";
						mostrarDialogo = true;
						insumoModif.setStockActual(Integer.valueOf(txtFieldStockActual.getText()));
					} else {
						if (nuevoStock < stockActualInsumo) {
							incrementoDecremento = "Decremento";
							mostrarDialogo = true;
							insumoModif.setStockActual(Integer.valueOf(txtFieldStockActual.getText()));
						} else { //no cambio stock actual, entonces le dejo el valor q trajo de la bd
							insumoModif.setStockActual(stockActualInsumo);
						}
					}
					
				}
				
				//Agregue el else por que el if tiene condicion si el textField esta visible, pero al entrar un usuario 
				//no administrador no entra nunca ahi, por eso agregue el else que lo que hace es darle el mismo valor al stock (ya que nunca
				//lo va a modificar
			} else {
				insumoModif.setStockActual(stockActualInsumo);
			}
			
			if (valido) {
				
				if (!(camposObligatoriosLlenos(insumoModif.getCategoria(), insumoModif.getNombreInsumo(), insumoModif.getPdp()))) {
					insumoModif = null;
				} else {
					//luego verifico q no hayan blancos
					
					if (!(datosConBlancos(insumoModif))) { //si entra es xq habian datos con blancos
						insumoModif = null;
					}
				}
				
			} else {
				insumoModif = null;
			}
			
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return insumoModif;
	}
  	
  	
  	private boolean datosConBlancos(Insumo insumoModif) {
		boolean validos = true;
		try {
			if (hayBlanco(insumoModif.getNroLote())) {
				txtFieldNroLote.setUnFocusColor(Color.RED);
				lblMsjErrorNroLote.setText("No se admite espacios antes del Nro de Lote");
				lblMsjErrorNroLote.setVisible(true);
				validos = false;
			}
			
			if ((insumoModif.getNroArticulo() != null) && hayBlanco(insumoModif.getNroArticulo())) {
				txtFieldNroArticulo.setUnFocusColor(Color.RED);
				lblMsjErrorNroArticulo.setText("No se admite espacios antes del Nro de Articulo");
				lblMsjErrorNroArticulo.setVisible(true);
				validos = false;
			}
			
			if (hayBlanco(insumoModif.getNombreInsumo())) {
				txtFieldNombreInsumo.setUnFocusColor(Color.RED);
				lblMsjErrorNombre.setText("No se admite espacios antes del Nombre");
				lblMsjErrorNombre.setVisible(true);
				validos = false;
			}
			
			// Fecha vto, no necesita verificacion, xq modifique el componente para q no seleccione fecha menores e igual a la actual
			
			// Precio y PDP, no necesitan verificacion xq utilizan un componente modificado, q no deja ingresar espacios, solo numeros
			
			if ((insumoModif.getReferencia() != null) && hayBlanco(insumoModif.getReferencia())) { 
				//entra si, el campo referencia tiene valor y si tiene blanco
				txtFieldRefInsumo.setUnFocusColor(Color.RED);
				lblMsjErrorReferencia.setText("No se admite espacios antes del Nro de Referencia");
				lblMsjErrorReferencia.setVisible(true);
				validos = false;
			}
			
			if ((insumoModif.getUnidadMedida() != null) && hayBlanco(insumoModif.getUnidadMedida())) {
				txtFieldUnidadMedida.setUnFocusColor(Color.RED);
				lblMsjErrorUnidadMedida.setText("No se admite espacios antes de la Unidad de Medida");
				lblMsjErrorUnidadMedida.setVisible(true);
				validos = false;
			}
			
			if ((insumoModif.getTemperatura() != null) && hayBlanco(insumoModif.getTemperatura())) {
				txtFieldTemperatura.setUnFocusColor(Color.RED);
				lblMsjErrorTemperatura.setText("No se admite espacios antes de la Temperatura");
				lblMsjErrorTemperatura.setVisible(true);
				validos = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return validos;
	}
  	
  	
  	private void verificarDatosParaLaModificacion(Insumo insumoModif, String estadoInsumoAux) {
  		try {
  			seguirModif = true;
  			
  			insumoBD = CRUD.obtenerInsumoPorId1(this.getIdInsumoAux());
  			
  			estadoInsumoAux = insumoBD.getEstadoInsumo();
  			
  			insumoBD.setNroLote(insumoModif.getNroLote());
			insumoBD.setCategoria(insumoModif.getCategoria());
			
			actualizarNombre(insumoModif.getNombreInsumo());	//1ero actualizo el nombre de todos los insumos de igual nombre(antes de la modif)
			insumoBD.setNombreInsumo(insumoModif.getNombreInsumo());	//luego actualizo el nombre insumo del insumo seleccionado para la modif
			
			insumoBD.setFechaVencimiento(insumoModif.getFechaVencimiento());
	
//			insumoBD.setPdp(insumoModif.getPdp());
			
			insumoBD.setPrecioInsumo(insumoModif.getPrecioInsumo());
			
//			insumoBD.setUnidadMedida(insumoModif.getUnidadMedida());
			
//			insumoBD.setTemperatura(insumoModif.getTemperatura());

			insumoBD.setStockActual(insumoModif.getStockActual());
			
			insumoBD.setProveedor(insumoModif.getProveedor());
			
			if (!(insumoModif.getEstadoInsumo().equals(estadoInsumoAux))) { //si entra cambio los estados
				
				insumoBD.setEstadoInsumo(insumoModif.getEstadoInsumo());
				siReactivoEstado(estadoInsumoAux);
				if (dioBaja) { //verifico si aun tiene stock
					verificarStockInsumo2(nroLoteAux, idInsumoAux);
				}
				
			}
			
			if (seguirModif) {  //este boolean se setea en "verificarStockInsumo2"
				
				consistirNroArticulo(insumoModif);
				
				consistirNroReferencia(insumoModif);
				
				if (seguirModif) { //si entra actualizo insumo de la bd, ya q no ocurrio ningun tipo de error
					CRUD.actualizarObjeto(insumoBD);
					//ahora se debe actualizar el pdp,unidad de medida y temperatura a todos los insumos de igual nombre, ya no solo a un insumo
					actualizarPDPUnidadMedidaYTemperaturaYNroPedido(insumoBD, insumoModif.getPdp(), insumoModif.getUnidadMedida(), 
																	insumoModif.getTemperatura(), insumoModif.getNroPedido());
					
					//si modifica nombre, la tabla depue quedaba blanca, entonces hay q setearle nombre seleccionado con el nuevo nombre
					ControladorICsd_Principal.controllerPantAdmin.setNombreSeleccionado(insumoBD.getNombreInsumo());
					
					cerrarModif = true;
				}
				
			}
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
  	

	private void actualizarNombre(String nuevoNombreInsumoIN) {
		List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorNombre2(insumoBD.getNombreInsumo());	//este tiene el nombre antes de la modificacion
		for (Insumo insumo : listaInsumos) {
			insumo.setNombreInsumo(nuevoNombreInsumoIN);
			CRUD.actualizarObjeto(insumo);
		}
	}




	private void actualizarPDPUnidadMedidaYTemperaturaYNroPedido(Insumo insumoBDIn, Integer nuevoPDPIn, String nuevaUnidadMedida, 
																	String nuevaTemperatura, Integer nuevoNroPedido) {
//		List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorNombre2(insumoBDIn.getNombreInsumo());
		List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorNombreYSucursal(insumoBDIn.getNombreInsumo(), insumoBDIn.getSucursal().getPkIdSucursal());
		for (Insumo insumo : listaInsumos) {
			insumo.setPdp(nuevoPDPIn);
			insumo.setUnidadMedida(nuevaUnidadMedida);
			insumo.setTemperatura(nuevaTemperatura);
			insumo.setNroPedido(nuevoNroPedido);
			CRUD.actualizarObjeto(insumo);
		}
	}




	private void consistirNroArticulo(Insumo insumoModif) {
  		try {
			if ((this.getNroArticuloAux() == null) && (!insumoModif.getNroArticulo().isEmpty())) { //osea vino articulo vacio, pero ahora completo
					
				if (!(nroArticuloUsado(insumoModif.getNroArticulo(), insumoModif.getNombreInsumo()))) {
					
					insumoBD.setNroArticulo(insumoModif.getNroArticulo());
					
					seguirModif = true;		//para luego saber si actualizar o no el insumo de la bd
					
				} else {	//nro articulo en uso x otro tipo de insumo
					
					txtFieldNroArticulo.setUnFocusColor(Color.RED);
	  				lblMsjErrorNroArticulo.setText("Ya se encuentra en uso");
	  				lblMsjErrorNroArticulo.setVisible(true);
	  				
	  				seguirModif = false;
					
				}
			
			} else {
				
				if ((this.getNroArticuloAux() != null) && (insumoModif.getNroArticulo() == null)) { //osea vino con articulo, pero ahora borro
					insumoBD.setNroArticulo("");
					seguirModif = true;	
				
				} else {
					
					if ((this.getNroArticuloAux() != null) && (!insumoModif.getNroArticulo().isEmpty())) { //osea vino con articulo, y pudo haber modificado el articulo
						
						if (!this.getNroArticuloAux().equals(insumoModif.getNroArticulo())) { //cambio articulo
							
							if (!(nroArticuloUsado(insumoModif.getNroArticulo(), insumoModif.getNombreInsumo()))) {
								
								insumoBD.setNroArticulo(insumoModif.getNroArticulo());
								
								seguirModif = true;		//para luego saber si actualizar o no el insumo de la bd
								
							} else {	//nro articulo en uso x otro tipo de insumo
								
								txtFieldNroArticulo.setUnFocusColor(Color.RED);
				  				lblMsjErrorNroArticulo.setText("Ya se encuentra en uso");
				  				lblMsjErrorNroArticulo.setVisible(true);
				  				
				  				seguirModif = false;
								
							}
						}
					}	//si no a este if significa q no toco nada
				}	
			}
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}


	private void consistirNroReferencia(Insumo insumoModif) {
		try {
			if ((this.getNroReferenciaAux() == null) && (!insumoModif.getReferencia().isEmpty())) { //osea vino referencia vacio, pero ahora completo
					
				if (!(nroReferenciaUsado(insumoModif.getReferencia(), insumoModif.getNombreInsumo()))) {
					
					insumoBD.setReferencia(insumoModif.getReferencia());
					
					seguirModif = true;		//para luego saber si actualizar o no el insumo de la bd
					
				} else {	//nro referencia en uso x otro tipo de insumo
					
					txtFieldRefInsumo.setUnFocusColor(Color.RED);
	  				lblMsjErrorReferencia.setText("Ya se encuentra en uso");
	  				lblMsjErrorReferencia.setVisible(true);
	  				
	  				seguirModif = false;
					
				}
			
			} else {
				
				if ((this.getNroReferenciaAux() != null) && (insumoModif.getReferencia() == null)) { //osea vino con referencia, pero ahora borro
					insumoBD.setReferencia("");
					seguirModif = true;	
				
				} else {
					
					if ((this.getNroReferenciaAux() != null) && (!insumoModif.getReferencia().isEmpty())) { //osea vino con referencia, y pudo haber modificado la referencia
						
						if (!this.getNroReferenciaAux().equals(insumoModif.getReferencia())) { //cambio referencia
							
							if (!(nroReferenciaUsado(insumoModif.getReferencia(), insumoModif.getNombreInsumo()))) {
								
								insumoBD.setReferencia(insumoModif.getReferencia());
								
								seguirModif = true;		//para luego saber si actualizar o no el insumo de la bd
								
							} else {	//nro referencia en uso x otro tipo de insumo
								
								txtFieldRefInsumo.setUnFocusColor(Color.RED);
				  				lblMsjErrorReferencia.setText("Ya se encuentra en uso");
				  				lblMsjErrorReferencia.setVisible(true);
				  				
				  				seguirModif = false;
								
							}
						}
					}	//si no a este if significa q no toco nada
				}
			}
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}


	private void siReactivoEstado(String estadoInsumoAux) {
    	if (!(estadoInsumoAux.equals(insumoBD.getEstadoInsumo()))) { //si entra cambio los estados
			if (estadoInsumoAux.equals("Suspendido")) {
				//osea si el estado q tenia el sector era "suspendido", y llego hasta aca, entonces reactivo el sector
				//x lo tanto, borro la fecha baja
				insumoBD.setFechaBaja(null);
				dioBaja = false;
			} else { //dio de baja desde el modificar
				dioBaja = true;
			}
		} else {
			dioBaja = false;
		}
	}
  	
  	
  //verifico si el insumo a dar de baja, aun tiene stock
  //en dicho caso no da de baja
  //ACTUALIZA EL OBJETO INSUMO	
//  	private void verificarStockInsumo1(String nroLoteIN, Integer idInsumoIN) {
//      	boolean darBaja = false;
//      	try {
//      		Insumo ins = CRUD.obtenerInsumoPorId(idInsumoIN);
//      		
//      		if (ins.getStockActual() == 0) {
//  				darBaja = true;
//  			}
//  			if (darBaja) {
//  				Date fechaBaja = new Date();
//  		    	insumoBD.setFechaBaja(fechaBaja);
//  		    	CRUD.actualizarObjeto(insumoBD);
//  		    	cerrarModif = true;
//  			} else {
//  				//muestro msj q no se puede dar baja insumo
//  				mostrarMsjBajaInsumo(ins.getNombreInsumo());
//  				cerrarModif = false;
//  			}
//  			
//  		} catch (Exception e) {
//  			e.printStackTrace();
//  			e.getMessage();
//  		}
//  	}
  	
  	
  //verifico si el insumo a dar de baja, aun tiene stock
    //en dicho caso no da de baja
    //NO ACTUALIZA EL OBJETO INSUMO
  	private void verificarStockInsumo2(String nroLoteIN, Integer idInsumoIN) {
      	boolean darBaja = false;
      	try {
      		Insumo ins = CRUD.obtenerInsumoPorId1(idInsumoIN);
      		
      		if (ins.getStockActual() == 0) {
  				darBaja = true;
  			}
  			if (darBaja) {
  				Date fechaBaja = new Date();
  		    	insumoBD.setFechaBaja(fechaBaja);
//  		    	CRUD.actualizarObjeto(insumoBD);
  		    	seguirModif = true;
  			} else {
  				//muestro msj q no se puede dar baja insumo
  				mostrarMsjBajaInsumo(ins.getNombreInsumo());
  				seguirModif = false;
  			}
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  			e.getMessage();
  		}
  	}
  	
  	
  	private void mostrarMsjBajaInsumo(String nombreInsumoIN) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
		alert.setHeaderText("El insumo: " + nombreInsumoIN);
		alert.setContentText("No puede darse de Baja, porque aun cuenta con Stock disponible");

		alert.showAndWait();
	}
  	
  	
  	private void mostrarDialogoIngreseObservacion() {
  		String sucursal = null;
  		
		TextInputDialog dialogo = new TextInputDialog();
		dialogo.setTitle("OBSERVACION OPCIONAL");
		dialogo.setHeaderText("Desea agregar una observacion?");
		dialogo.setContentText("Observacion:");
		dialogo.initStyle(StageStyle.UTILITY);
		Optional<String> repuesta = dialogo.showAndWait();
		
		//ademas creo el movimiento para dejar registro acerca de la modificacion del stock
		Integer cantidad = Integer.valueOf(txtFieldStockActual.getText());
		
		//stockActualInsumo, se setea con el 1er valor q tenia el stock del insumo, cuando ingreso a la modif
		String observacion = "Se " + incrementoDecremento + " de " + stockActualInsumo + " a " + cantidad + " \n"; 

		if (repuesta.isPresent()) {
			observacion = observacion + repuesta.get();
		}
		
		//antes de guardar movimiento, es necesario saber de q sucursal es dicho insumo
		switch (ControladorICsd_Principal.controllerTablaInsumo.getLblSucursal().getText()) {
		case "Diagnos":
			sucursal = "Diagnos";
			break;
		case "Km3":
			sucursal = "Km3";
			break;
		case "Rada Tilly":
			sucursal = "Rada Tilly";
			break;
		}
		
		ControladorICsd_Principal.controllerTablaMovimientos.guardarMovimientoDesdeRemito(insumoBD, cantidad, sucursal, observacion);
	
  	}
  	
  	
  	public void llenarCampos(Integer idInsumoIN) {
  		try {
  			llenarComboSector();
  			
  			Insumo insumoModif = CRUD.obtenerInsumoPorId1(idInsumoIN); 
  			
  		// 1ero completo los campos q se q tienen valor, xq son los obligatorios 	
  			
  			txtFieldNombreInsumo.setText(insumoModif.getNombreInsumo());
  			txtFieldNroLote.setText(insumoModif.getNroLote());
  		
  		//antes de setear el textfield del calendario debo, cambiarle el formato a la fecha q trae el 
  		//	objeto de la BD "yyyy-MM-dd" pasarlo a "dd-MM-yyyy"
  			if (insumoModif.getFechaVencimiento() != null) {
  				SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
  				datePickerInsumo.getEditor().setText(formato.format(insumoModif.getFechaVencimiento()));
  			}
  			
  			txtFieldPDP.setText(String.valueOf(insumoModif.getPdp()));
  			
  			if (insumoModif.getEstadoInsumo().equals("Activo")) {
  				radioBtnActivoInsumo.setSelected(true);
  				radioBtnActivoInsumo.setSelectedColor(Color.web("#0098cd"));
  			} else {
  				radioBtnActivoInsumo.setSelected(false);
  				radioBtnSuspendidoInsumo.setSelected(true);
  				radioBtnSuspendidoInsumo.setSelectedColor(Color.web("#0098cd"));
  			}
  			
  			buscarSector_CategoriaYsetearCombo(insumoModif);
  			
  		//luego verifico si tienen valor los atributos opcionales
  			
  			if (insumoModif.getNroArticulo() != null) {
  				txtFieldNroArticulo.setText(insumoModif.getNroArticulo());
  				
  				this.setNroArticuloAux(insumoModif.getNroArticulo());           //////////////////////!!!!!!!!!!!!!!!!!!
  			}
  			
  			if (insumoModif.getReferencia() != null) {
  				txtFieldRefInsumo.setText(insumoModif.getReferencia());
  				
  				this.setNroReferenciaAux(insumoModif.getReferencia());			//////////////////////!!!!!!!!!!!!!!!!!!
  			}
  			
  			if (insumoModif.getPrecioInsumo() != null) {
  				txtFieldPrecio.setText(String.valueOf(insumoModif.getPrecioInsumo()));
  			}
  			
  			if (insumoModif.getUnidadMedida() != null) {
  				txtFieldUnidadMedida.setText(String.valueOf(insumoModif.getUnidadMedida()));
  			}
  			
  			if (insumoModif.getTemperatura() != null) {
  				txtFieldTemperatura.setText(insumoModif.getTemperatura());
  			}
  			
  			if (insumoModif.getNroPedido() != null) {
  				txtFieldNroPedido.setText(String.valueOf(insumoModif.getNroPedido()));
  			}
  			
  			this.setNroLoteAux(insumoModif.getNroLote());
  			this.setIdInsumoAux(insumoModif.getIdInsumo());
  			
//  			this.setNroArticuloAux(insumoModif.getNroArticulo());
//  			this.setNroReferenciaAux(insumoModif.getReferencia());
  			
  			//tambien completo x mas q este o no visible el campo stock
  			txtFieldStockActual.setText(String.valueOf(insumoModif.getStockActual()));
  			//dejo referencia al stock, para luego saber si incremento o decremento dicho valor
  			//para dejar registro del cambio, cuando se muestre el movimiento q se hizo
  			stockActualInsumo = 0;
  			stockActualInsumo = insumoModif.getStockActual();
  			
  			cargarProveedores();
  			setearComboBoxProvee(insumoModif.getProveedor().getPkIdProveedor());
  			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}


	public void cargarProveedores() {
		try {
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
			listaProveedores = CRUD.obtenerListaProveedoresActivos();
			
			for (Proveedor proveedor : listaProveedores) {
					itemsCombo.add(proveedor.getNombreProveedor());
			}
			cBoxProveedor.setItems(itemsCombo);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		
	}

	private void setearComboBoxProvee(Integer idProveedorIN) {
		boolean salir = false;
		Integer i = 0;
		try {
			//1ero verifico si reactivo el provee, de ser asi.. tengo q agregarlo al comboBox provee
//			if (this.getReactivoProveedor()) {
//				cBoxSeleccioneProvee.getItems().add(nombreProveedorIN);
//				this.setReactivoProveedor(false);						//reseteo el boolean, para q no agregue repetidos, una vez reactiva el provee
//			}
			
			while ((i < listaProveedores.size()) && (!(salir))) {
				if (listaProveedores.get(i).getPkIdProveedor().equals(idProveedorIN)) {
					cBoxProveedor.getSelectionModel().select(i);
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

  	
}
