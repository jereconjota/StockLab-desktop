package controlador;

import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import modelo.Categoria;
import modelo.Insumo;
import modelo.Sector;
import modeloAux.DetalleFacturaFX;
import modeloAux.InsumoFX;

public class ControladorISecundariaInsumosFactura {
	
	@FXML
    private JFXButton btnDeshacer;

    @FXML
    private JFXButton btnLimpiarFiltros;

    @FXML
    private JFXComboBox<String> cBoxSeleccione;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXComboBox<String> cBoxSector;

    @FXML
    private JFXTextField txtFieldBuscar;
    
    @FXML
    private JFXComboBox<String> cBoxCategoria;

    @FXML
    private TableView<InsumoFX> tablaInsumos;

    @FXML
    private TableColumn<InsumoFX, String> tablaInsumos_Categoria;

    @FXML
    private TableColumn<InsumoFX, String> tablaInsumos_Sector;
	
    @FXML
    private TableColumn<InsumoFX, String> tablaInsumos_Referencia;

    @FXML
    private TableColumn<InsumoFX, String> tablaInsumos_Articulo;
    
    @FXML
    private TableColumn<InsumoFX, String> tablaInsumos_Insumo;
    
    @FXML
    private TableView<InsumoFX> detalleFactura;
    
    @FXML
    private TableColumn<InsumoFX, String> detalleFactura_Articulo;
    
    @FXML
    private TableColumn<InsumoFX, String> detalleFactura_Sector;
    
    @FXML
    private TableColumn<InsumoFX, String> detalleFactura_Referencia;
    
    @FXML
    private TableColumn<InsumoFX, String> detalleFactura_Categoria;
    
    @FXML
    private TableColumn<InsumoFX, String> detalleFactura_Insumo;
    
    @FXML
    private JFXButton btnNuevo;
    
    @FXML
    private JFXButton btnCancelar;
    
    @FXML
    private JFXButton btnActualizar;
    
    @FXML
    private Label lblDetalleFactura;
    
    private ObservableList<InsumoFX> obListInsumos = FXCollections.observableArrayList(); //para la tabla gral (no se le aplica clear)
    
    private ObservableList<InsumoFX> obListInsumosAux = FXCollections.observableArrayList(); //para la tabla gral (se le aplica clear)
    
    private ObservableList<InsumoFX> obListInsumosPorSector = FXCollections.observableArrayList(); //para aplicar filtro a insumos x sector
    
    private ObservableList<InsumoFX> obListInsumosPorCategoria = FXCollections.observableArrayList(); //para aplicar filtro a insumos x categoria
    
    private ObservableList<InsumoFX> obListDetalleFactura = FXCollections.observableArrayList(); //para la tabla detalle factura
    
    private Insumo insumoBD;
    
    private Integer idSector;
    
    private FilteredList<InsumoFX> filtrarDatos;
    
    
    
    /**************************** CONSTRUCTOR *********************************/
    
    public ControladorISecundariaInsumosFactura() {
    	
	}
    
    
    
    /**************************** GET - SET **********************************/
    
    public Label getLblDetalleFactura() {
		return lblDetalleFactura;
	}
    
    
    public ObservableList<InsumoFX> getObListInsumos() {
		return obListInsumos;
	}


	public Insumo getInsumoBD() {
		return insumoBD;
	}

	public void setInsumoBD(Insumo insumoBD) {
		this.insumoBD = insumoBD;
	}


	public ObservableList<InsumoFX> getObListInsumosAux() {
		return obListInsumosAux;
	}



	/********************************** METODOS ***********************************/
    
    
    @FXML
    private void initialize() {
    	definirTipoDatosColumnas();
    	limpiarCampos();
//    	llenarCombos();				/////// lo dividi en 2 metodos (llenarcomboseleccione y llenarcombosector)
    	llenarComboSeleccione();
//    	llenarTablaInsumos();		////////!!!!!!!!!!!!!!!!! ahora lo llamo desde controlador ingresofactura
//    	setearOrdenDatosEnTabla();	////////!!!!!!!!!!!!!!!!! ahora lo llamo desde controlador ingresofactura
    	seleccionarFila();
    }


	@FXML
    void eventoComboSector() {
	//pasos: 
	//1- limpiar tabla
	//2- llenar tabla
	//3- llenar combo categoria
		try {
			idSector = null;
			List<Integer> listIdCategorias = new ArrayList<>();
			
			obListInsumosAux.clear();  //aca limpiamos el contenido de la ob list aux
			
			if (cBoxCategoria.getSelectionModel().getSelectedIndex() != -1) {
				cBoxCategoria.getSelectionModel().clearSelection();
				cBoxCategoria.getSelectionModel().select(-1); //y vuelvo a dejarlo en la posicion x defecto
				cBoxCategoria.getItems().clear();
				obListInsumosPorCategoria.clear(); //tambien limpiamos el oblist de categoria (x si aplico un filtro x categoria)
			}
			
			if (cBoxSector.getSelectionModel().getSelectedItem() != null) {
				Sector secBD = CRUD.obtenerSectorPorNombre(cBoxSector.getSelectionModel().getSelectedItem());
				idSector = secBD.getIdSector();
				llenarComboCategoria(idSector, listIdCategorias);
				filtrarTablaPorSector(listIdCategorias);
				
				setearOrdenDatosEnTabla();
			}

			for (InsumoFX insumoFX : obListInsumos) {
				obListInsumosAux.add(insumoFX);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    @FXML
    void eventoComboCategoria() {
	//pasos: 
	//1- limpiar tabla
	//2- llenar tabla
		try {
			obListInsumosPorSector.clear();
			
			if (cBoxCategoria.getSelectionModel().getSelectedItem() != null) {
				//el valor de "idSector", se setea cuando se selecciona un sector en el comboSector		
				Categoria catBD = CRUD.obtenerCategoriaPorNombreCategoriaYPorIdSector3(cBoxCategoria.getSelectionModel().getSelectedItem(), idSector);
				Integer idCategoria = catBD.getPkIdCategoria();
				filtrarTablaPorCategoria(idCategoria);
				
				setearOrdenDatosEnTabla();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
	

    @FXML
    void limpiarFiltros(ActionEvent event) {    	
    	obListInsumosPorCategoria.clear();
    	obListInsumosPorSector.clear();
    	
    	cBoxCategoria.getSelectionModel().select(-1);
		cBoxCategoria.getItems().clear();
		
		cBoxSector.getSelectionModel().select(-1);
		
		cBoxSeleccione.getSelectionModel().select(0);
		
		txtFieldBuscar.clear();
		
		obListInsumosAux.clear();
		for (InsumoFX insumoFX : obListInsumos) {
			obListInsumosAux.add(insumoFX);
		}
		
		tablaInsumos.setItems(obListInsumosAux);
		
		setearOrdenDatosEnTabla();
		
		lblDetalleFactura.requestFocus();
    }
    
    
    //muestra pantalla alta insumo
    @FXML
    void nuevoInsumo(ActionEvent event) {
    	ControladorICsd_Principal.controllerAltaInsumo.setIngresoFactura(true); //es para avisar q voy al alta, desde "secundaria-insumos" 
    	ControladorICsd_Principal.controllerAltaInsumo.setIngresoRemito(false);	
		//y no de "admin"
		ControladorICsd_Principal.controllerAltaInsumo.getLblAltaInsumo().setVisible(true);
		ControladorICsd_Principal.controllerAltaInsumo.getLblModifInsumo().setVisible(false);
		ControladorICsd_Principal.controllerAltaInsumo.getBtnGuardarInsumo().setVisible(true);
		ControladorICsd_Principal.controllerAltaInsumo.getBtnGuardarModifInsumo().setVisible(false);
		ControladorICsd_Principal.controllerAltaInsumo.llenarComboSector();
		ControladorICsd_Principal.controllerAltaInsumo.getTxtFieldStockActual().setVisible(false);
    	ControladorICsd_Principal.controllerAltaInsumo.getLblStockActual().setVisible(false);
		
    	ControladorICsd_Principal.controllerAltaInsumo.cargarProveedores();
    	
		ControladorICsd_Principal.controllerIIngresoFactura.getiPpalSecundaria().setCenter(ControladorICsd_Principal.iAltaInsumo);
    }
    
    
    @FXML
    void actualizarPantalla(ActionEvent event) {
    	//antes de llamar al initialize, debo resetear la listOb del controlador ingreso fac
    	//q me sirve, pa evitar andar cargando los insumos en la tabla y se ponga lenta la app
    	tablaInsumos.getSelectionModel().clearSelection();
    	llenarComboSeleccione();
    	limpiarCombosSectorCategoria();
    	llenarComboSector();
    	obListInsumos.clear();
    	obListInsumosAux.clear();
    	cargarInsumos();
		tablaInsumos.setItems(obListInsumosAux);
		setearOrdenDatosEnTabla();
    }
    
    
    @FXML
    public void cancelar() {
    	//1ero guardo los datos de la lista observable actual, en la list ob del controlador
    	//ingreso fac, para cuando vuelva ingresar a esta pant no sea necesario pasar
    	//x el cargar insumos
    	ControladorICsd_Principal.controllerIIngresoFactura.setObListInsumosPantSecundaria(this.getObListInsumos());
    	
    	//le digo al contenedor ppal q se cierre (controlador iPpalSecundariaInsumos)
    	ControladorICsd_Principal.controllerIIngresoFactura.getControllerPpalSecundariaInsumos().getOtroStage().close();
    }
    
    
  //borra la ultima fila agregada a la tabla coincidencias,es decir, borra el ultimo insumo agregado
  	@FXML
    void deshacer(ActionEvent event) {
  		obListDetalleFactura.remove(obListDetalleFactura.size() - 1);
    	detalleFactura.setItems(obListDetalleFactura);
    	
    	//luego tengo q verificar q si no queda mas elementos en la tabla detalle 
    	//entonces deshabilito los botones guardar y deshacer
    	if (obListDetalleFactura.size() == 0) {
			btnGuardar.setDisable(true);
			btnDeshacer.setDisable(true);
		}
    }
    
    
  //se encarga de traspasar los datos de la tabla detalle actual (secundaria-insumos)
  	//a la verdadera tabla detalle factura (ingreso-factura)
  	@FXML
    void guardar(ActionEvent event) {
  		try {
  			ObservableList<DetalleFacturaFX> obListDF = FXCollections.observableArrayList();
  			for (InsumoFX insumoFX : obListDetalleFactura) {
  				DetalleFacturaFX dFacFX = new DetalleFacturaFX();
  				dFacFX.insumo.set("");
  				dFacFX.nombreInsumo.set(insumoFX.nombreInsumo.get());
  				dFacFX.nroArticulo.set(insumoFX.articulo.get());
  				dFacFX.unidadMedida.set(insumoFX.unidadMedida.get());
  				dFacFX.cantidad.set(0);
  				dFacFX.precio.set(insumoFX.precioInsumo.get());
  				dFacFX.importe.set(Float.valueOf("0"));
  				dFacFX.nombreCategoria.set(insumoFX.nombreCategoria.get());
  				dFacFX.idCategoria = insumoFX.idCategoria;
  				dFacFX.nombreSector.set(insumoFX.nombreSector.get());
  				
  				dFacFX.nroReferencia = insumoFX.referencia.get();
  				
  				obListDF.add(dFacFX);
  			}
  			
  			//1ero tengo q verificar si la tabla detalle fac(ingreso-fac) esta vacia
  			//si es asi, a su obList le seteo la de este controlador
  			if (ControladorICsd_Principal.controllerIIngresoFactura.getObListDetalleFactura().isEmpty()) {
  				
  				ControladorICsd_Principal.controllerIIngresoFactura.setObListDetalleFactura(obListDF);
  			
  			} else { 
  				for (DetalleFacturaFX dFFX : obListDF) {
  					ControladorICsd_Principal.controllerIIngresoFactura.getObListDetalleFactura().add(dFFX);
  				}
  			}
  			
  			ControladorICsd_Principal.controllerIIngresoFactura.getTablaDetalleFactura().
  			setItems(ControladorICsd_Principal.controllerIIngresoFactura.getObListDetalleFactura());
  			
  			//esto es para, si vuelve a ingresar a pant secundari-insumos, no vuelva a cargar todos los insumos
  			//y se ponga lenta la app
  			ControladorICsd_Principal.controllerIIngresoFactura.setObListInsumosPantSecundaria(this.getObListInsumos());

  	    	//le digo al contenedor ppal q se cierre (controlador iPpalSecundariaInsumos)
  	    	ControladorICsd_Principal.controllerIIngresoFactura.getControllerPpalSecundariaInsumos().getOtroStage().close();
  			
  	    	//habilito botones de pant ingreso-fac, guardar y deshacer
  	    	ControladorICsd_Principal.controllerIIngresoFactura.habilitarBotones();
  	    	
  		} catch (Exception e) {
  			e.printStackTrace();
  			e.getMessage();
  		}
    }
  	
  	
  	//manejador de click sobre el txtField buscar
  	//se encargara de setear el oblist correcto, para q no cause errores cuando filtre
  	@FXML
    void manejadorObList() {
  		// si combo sector y categoria (ambos) estan sin seleccionar, entonces se aplicara filtro sobre los insumos en gral
  		if ((cBoxSector.getSelectionModel().getSelectedIndex() == -1) && 
  				(cBoxCategoria.getSelectionModel().getSelectedIndex() == -1)) { 
  			
  			filtrarDatos = new FilteredList<>(obListInsumosAux, e -> true);
  			
  		} else {
  			//si se selecciono algun sector, pero no se selecciono ninguna categoria, el filtro se aplicara sobre los insumos de dicho sector
  			if ((cBoxSector.getSelectionModel().getSelectedIndex() >= 0) &&
  					(cBoxCategoria.getSelectionModel().getSelectedIndex() == -1)) {
  				
  				filtrarDatos = new FilteredList<>(obListInsumosPorSector, e -> true);
  			
  			} else {
  				//significa q selecciono alguna categoria, entonces se aplica filtro a insumos x categoria
  				filtrarDatos = new FilteredList<>(obListInsumosPorCategoria, e -> true);
  			
  			}
  			
  		}
    }
  	
  	
  	//manejador de evento, del txtfield buscar..el cual ira filtrando la tabla
  	//a medida q se vayan ingresando caracteres
  	@FXML
	void filtroTxtFieldBuscar() {
		//antes q nada tengo q verificar si el cBoxCategoria, esta en:
				// "SELECCIONE" , si es asi, usa el "obListInsumosGral"
				// sino usa, "obListItemsFiltro"
	
	      txtFieldBuscar.setOnKeyReleased(e ->{
	          txtFieldBuscar.textProperty().addListener((observableValue, oldValue, newValue) ->{
	              filtrarDatos.setPredicate((java.util.function.Predicate<? super InsumoFX>) insumoFX->{
	                  if(newValue == null || newValue.isEmpty()){
	                      return true;
	                  }
	                  String lowerCaseFilter = newValue.toLowerCase();
	                  
	              	//filtra busqueda por "NOMBRE"
	                  if (cBoxSeleccione.getSelectionModel().getSelectedItem().equals("NOMBRE")) {
					
	                  	if(insumoFX.nombreInsumo.get().contains(newValue)){
	                        return true;
	                    } else if (insumoFX.nombreInsumo.get().toLowerCase().contains(lowerCaseFilter)) {
	                    	return true;
	                    }
	                  	
					} else {
						
						//filtra busqueda por "ARTICULO"
	                    if (cBoxSeleccione.getSelectionModel().getSelectedItem().equals("NRO ARTICULO")) {
	                    	
	                    	System.out.println(insumoFX.articulo.get());
	                    	System.out.println(newValue);
	                    	
	                    	if(insumoFX.articulo.get().contains(newValue)){
		                        return true;
		                    } else if (insumoFX.articulo.get().toLowerCase().contains(lowerCaseFilter)) {
		                    	return true;
		                    }
	                    	
						} else {
							
							//filtra busqueda por "REFERENCIA"
		                    if (cBoxSeleccione.getSelectionModel().getSelectedItem().equals("NRO REFERENCIA")) {
							
		                    	if(insumoFX.referencia.get().contains(newValue)){
			                        return true;
			                    } else if (insumoFX.referencia.get().toLowerCase().contains(lowerCaseFilter)) {
			                    	return true;
			                    }
		                    	
							}
							
						}
						
					}
	                 
	                  return false;
	              });
	          });
	          SortedList<InsumoFX> sortedData = new SortedList<>(filtrarDatos);
	          sortedData.comparatorProperty().bind(tablaInsumos.comparatorProperty());
	          tablaInsumos.setItems(sortedData);
	          
	      });
	}
    
    
  	private void definirTipoDatosColumnas() {
    	tablaInsumos_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaInsumos_Articulo.setCellValueFactory(cellData -> cellData.getValue().articulo);
		tablaInsumos_Referencia.setCellValueFactory(cellData -> cellData.getValue().referencia);
		tablaInsumos_Sector.setCellValueFactory(cellData -> cellData.getValue().nombreSector);
		tablaInsumos_Categoria.setCellValueFactory(cellData -> cellData.getValue().nombreCategoria);
		
		detalleFactura_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		detalleFactura_Articulo.setCellValueFactory(cellData -> cellData.getValue().articulo);
		detalleFactura_Referencia.setCellValueFactory(cellData -> cellData.getValue().referencia);
		detalleFactura_Sector.setCellValueFactory(cellData -> cellData.getValue().nombreSector);
		detalleFactura_Categoria.setCellValueFactory(cellData -> cellData.getValue().nombreCategoria);
		
		alinearContenidoColumnas();
	}


	private void alinearContenidoColumnas() {
		tablaInsumos_Insumo.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_Articulo.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_Referencia.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_Sector.setStyle("-fx-alignment: CENTER;");
		tablaInsumos_Categoria.setStyle("-fx-alignment: CENTER;");
		
		detalleFactura_Insumo.setStyle("-fx-alignment: CENTER;");
		detalleFactura_Articulo.setStyle("-fx-alignment: CENTER;");
		detalleFactura_Referencia.setStyle("-fx-alignment: CENTER;");
		detalleFactura_Sector.setStyle("-fx-alignment: CENTER;");
		detalleFactura_Categoria.setStyle("-fx-alignment: CENTER;");
	}
	
	
	public void limpiarCampos() {
		txtFieldBuscar.clear();
		tablaInsumos.getSelectionModel().clearSelection();
		btnGuardar.setDisable(true);
		btnDeshacer.setDisable(true);
	}
	
	
//	public void llenarCombos() {		/////////// sigo usando los metodos q estan adentro x separado
//    	llenarComboSeleccione();
//    	llenarComboSector();			////////////////!!!!!!!!!!!!!!!!!!! llamarlo desde afuera no desde el initialize!! (lo llamo desde controleringresofactura)
//	}

	
    private void llenarComboSeleccione() {
    	ObservableList<String> itemsCombo = FXCollections.observableArrayList();
    	itemsCombo.add("NOMBRE");
		itemsCombo.add("NRO ARTICULO");
		itemsCombo.add("NRO REFERENCIA");
		cBoxSeleccione.setItems(itemsCombo);
		cBoxSeleccione.getSelectionModel().select(0);
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
			cBoxSector.getSelectionModel().select(-1);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	public void setearOrdenDatosEnTabla() {
		tablaInsumos_Insumo.setSortType(TableColumn.SortType.ASCENDING);
		tablaInsumos.getSortOrder().add(tablaInsumos_Insumo);
	}
	
	
	private void seleccionarFila(){
	//se le asigna una accion al click de cada fila de la tabla insumos
	tablaInsumos.setRowFactory( tv -> {
	    TableRow<InsumoFX> row = new TableRow<>();
	    row.setOnMouseClicked(event -> {
	        if (event.getButton() == MouseButton.PRIMARY){
	        	
	        	if (event.getClickCount() == 2) { // DOBLE CLICK
	        		if (tablaInsumos.getSelectionModel().isSelected(row.getIndex(), tablaInsumos_Insumo)) {
		        		InsumoFX rowData = row.getItem();
		        		
		        		convertirObjetoFXaInsumo(rowData);
		        		
		    			agregarInsumoAFila(this.getInsumoBD(), "InsumoYaCargado", rowData.nombreSector.get());//se encargara de crear fila en la tabla, y meter el insumo
		        		
					}else{
		        		tablaInsumos.getSelectionModel().clearSelection();
					}
				}
	        }
	    });
	    return row ;
		});
	}
	
	
	public void llenarTablaInsumos() {	
		if (ControladorICsd_Principal.controllerIIngresoFactura.getObListInsumosPantSecundaria().isEmpty()) {
			cargarInsumos();
//			setearColNombreSector();  ahora el nombre sector lo obtengo dentro del query en cargarInsumos() -- CRUD.obtenerListaInsumosActivosAgrupadosPorArticulo()
		} else {
			for (InsumoFX insumoFX : ControladorICsd_Principal.controllerIIngresoFactura.getObListInsumosPantSecundaria()) {
				obListInsumos.add(insumoFX);
				obListInsumosAux.add(insumoFX);
			}
		}
		//finalmente le seteo a la tabla insumos la lista observable q no tiene insumos repetidos x categoria y nombre
		tablaInsumos.setItems(obListInsumosAux);
	}
	
	
	private void cargarInsumos() {
		try {
			
			//antes solo agrupabamos dejando solo los q tengan distintos nombres de acuerdo a una categoria!
			//pero hay situaciones en la q un mismo insumo(nombre) tiene distinto articulo, x lo tanto deben figurar ambos (x mas q tengan igual nombre)
			List<Insumo> listaInsumos = CRUD.obtenerListaInsumosActivosAgrupadosPorArticulo();  
			
    		for (Insumo insumo : listaInsumos) {	
//    			System.out.println(insumo.getProveedor().getPkIdProveedor());
    			final InsumoFX insumoFX = new InsumoFX(insumo);
    			
    			insumoFX.nombreSector.set(insumo.getNombreSector());
    			
    			obListInsumos.add(insumoFX);    //1ero lleno toda la lista observable (la verdadera, q no se le aplica clear) 
    											//con todos los insumos de la BD
    											//basicamente se utilizara para tener ref a todos los insumos de la BD.. 
    											//y no volver a traer la tabla de la bd, para volver a llenarla
    			
    			obListInsumosAux.add(insumoFX); // x eso se usa una auxiliar a la cual se le puede aplicar el clear
    	
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void llenarComboCategoria(Integer idSectorIN, List<Integer> listIdCategorias) {
		try {
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
//			List<Categoria> listaCategorias = CRUD.obtenerListaCategoriasPorIdSector(idSectorIN); //esto lleva a un error, ya q si una categoria suspendida aparece
																								  //dentro del combo cat, y lo selecciona va a quedar vacia la tabla
																								//xq sus insumos relacionados tambien estaran suspendidos
																								//y la tabla solo muestra insumos activos
			
			List<Categoria> listaCategorias = CRUD.obtenerListaCategoriasActivasPorIdSector(idSectorIN); 

			for (Categoria categoria : listaCategorias) {
				itemsCombo.add(categoria.getNombreCategoria());
				listIdCategorias.add(categoria.getPkIdCategoria()); //guardo en un list aux, los id de las categorias q perteneces a dicho sector
			}
			
			cBoxCategoria.setItems(itemsCombo);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	//el argumento, me trae todos los id de las categorias, entonces solo me queda
	//buscar los insumos q pertenecen a dichas categorias
	private void filtrarTablaPorSector(List<Integer> listIdCategorias) {
		obListInsumosPorSector.clear();
		for (Integer idCategoriaList : listIdCategorias) {
			
			//en vez de buscar a los insumos en la bd, q se vinculan a dichas categorias
			//los busco sobre los insumos (q ya estan filtrados) de la tabla 
			for (InsumoFX insFX : obListInsumos) {
				
				if (idCategoriaList == insFX.idCategoria) {
					obListInsumosPorSector.add(insFX);
				}	
			}
		}
		tablaInsumos.setItems(obListInsumosPorSector);
	}
	
	
	private void filtrarTablaPorCategoria(Integer idCategoriaIN) {
		obListInsumosPorCategoria.clear();
		for (InsumoFX insFX : obListInsumos) {
			
			if (idCategoriaIN == insFX.idCategoria) {
				obListInsumosPorCategoria.add(insFX);
			}	
		}
		tablaInsumos.setItems(obListInsumosPorCategoria);
	}
	
	
	public void limpiarCombosSectorCategoria() {
		obListInsumosPorCategoria.clear();
    	obListInsumosPorSector.clear();
    	
    	cBoxCategoria.getSelectionModel().select(-1);
		cBoxCategoria.getItems().clear();
		
		cBoxSector.getSelectionModel().select(-1);
	}
	
	
	private void convertirObjetoFXaInsumo(InsumoFX rowData) {
		try {
			Integer idInsumoBD = CRUD.obtenerIdInsumoPrecioActual(rowData.nombreInsumo.get(), rowData.articulo.get(), rowData.referencia.get());
			
			Insumo ins = CRUD.obtenerInsumoPorId2(idInsumoBD);
			
			this.setInsumoBD(ins); //para tener una referencia al insumo, y luego agreagar fila a la tabla
			this.getInsumoBD().setFechaVencimiento(null); //este valor se encargaran de asignarselo las bioquimicas
			this.getInsumoBD().setFechaUso(null); //x las dudas lo reseteo x si tenia algun valor
			this.getInsumoBD().setEstadoInsumo("Activo"); //x si estaba en "suspendido"
			this.getInsumoBD().setStockActual(0);
			this.getInsumoBD().setFechaIngreso(null);
			this.getInsumoBD().setFechaBaja(null);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
	
	//el argumento "tipo", me indica si el insumoIn, es uno recien creado o si es un insumo q ya estaba cargado
  	public void agregarInsumoAFila(Insumo insumoIN, String tipo, String nombreSector) {
  		///aca tengo q agregar el insumoIn a la fila de la tabla detalleFactura
  		meterInsumoADetalleFactura(insumoIN, nombreSector);
  		
  		if (tipo.equals("InsumoNuevo")) { //solamente ingresa al if, si viene desde el alta insumo
  				
  			//luego mostrar nuevamente pantalla secundaria insumos
  			ControladorICsd_Principal.controllerIIngresoFactura.getiPpalSecundaria().
  																setCenter(ControladorICsd_Principal.controllerIIngresoFactura.getiSecundariaInsumos());
  				
  		}
  		
  		//habilito btn guardar y deshacer
  		btnDeshacer.setDisable(false);
  		btnGuardar.setDisable(false);
  		
  		//reseteo el objeto de referencia a la pantalla a la cual volver
  		ControladorICsd_Principal.controllerAltaInsumo.setIngresoFactura(false);
  	}
  	
  	
  	private void meterInsumoADetalleFactura(Insumo insumoIN, String nombreSectorIN) {
  		InsumoFX insFX = new InsumoFX(insumoIN);
  		insFX.nombreSector.set(nombreSectorIN);
  		obListDetalleFactura.add(insFX);
  		detalleFactura.setItems(obListDetalleFactura);
	}
  	
  	
  //metodo q es invocado desde el alta insumo, cuando se apreta en "cancelar"
  	//x lo q muestra nuevamente pantalla "secundaria-insumos"
  	//volviendo al estado q tenia antes de ir a pantalla "alta insumo"
	public void volver() {
		ControladorICsd_Principal.controllerIIngresoFactura.getiPpalSecundaria().
		setCenter(ControladorICsd_Principal.controllerIIngresoFactura.getiSecundariaInsumos());

		//reseteo el objeto de referencia a la pantalla a la cual volver
		ControladorICsd_Principal.controllerAltaInsumo.setIngresoFactura(false);
	}
  	
  	

}
