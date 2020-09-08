package controlador;

import java.util.Date;
import java.util.List;

import org.hibernate.Transaction;

import com.jfoenix.controls.JFXButton;

import hibernate.util.CRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.AppMain;
import modelo.Categoria;
import modelo.Insumo;
import modelo.Proveedor;
import modelo.Sector;
import modelo.Usuario;

public class ControladorIPantallaAdmin {
	
	@FXML
    private Tab tabCategorias;

    @FXML
    private JFXButton btnModifProveedor;

    @FXML
    private JFXButton btnModifSector;

    @FXML
    private JFXButton btnBajaUsuario;

    @FXML
    private BorderPane borderABMinsumos;

    @FXML
    private JFXButton btnModifInsumo;

    @FXML
    private Tab tabSectores;

    @FXML
    private JFXButton btnAltaSector;

    @FXML
    private JFXButton btnBajaProveedor;

    @FXML
    private JFXButton btnAtras;

    @FXML
    private BorderPane borderABMcategorias;

    @FXML
    private JFXButton btnVerInsumo;

    @FXML
    private JFXButton btnBajaCategoria;

    @FXML
    private JFXButton btnAltaInsumo;

    @FXML
    private JFXButton btnBajaSector;

    @FXML
    private Tab tabInsumos;

    @FXML
    private Tab tabProveedores;

    @FXML
    private JFXButton btnModifUsuario;

    @FXML
    private JFXButton btnBajaInsumo;

    @FXML
    private BorderPane borderABMproveedores;

    @FXML
    private TabPane tabPaneAdmin;

    @FXML
    private BorderPane borderABMsectores;

    @FXML
    private JFXButton btnAltaUsuario;

    @FXML
    private Tab tabUsuarios;

    @FXML
    private JFXButton btnModifCategoria;

    @FXML
    private JFXButton btnAltaProveedor;

    @FXML
    private BorderPane borderABMusuario;

    @FXML
    private JFXButton btnAltaCategoria;
    
    private Integer idCategoriaSeleccionado;
    
    private String nombreSucursalSeleccionado;
    
    private String nombreSeleccionado; //referencia al nombre insumo, q se selecciono en tabla gral de insumos
										//xq luego cuando veo los insumos de un tipo.. y entro a "modif"
										// si apreto tanto "guardar" como "cancelar".. regreso a la tabla 
										//q muestra los insumos de acuerdo al "nombreSeleccionado"
										//y necesito mantener esa referencia.. y asi poder mostrar siempre los datos actualizados
    
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorIPantallaAdmin() {
    	
	}
    
    
    /**************************** GET - SET **********************************/	

	public Tab getTabUsuarios() {
		return tabUsuarios;
	}
    
	
    public JFXButton getBtnModifProveedor() {
		return btnModifProveedor;
	}


	public JFXButton getBtnModifSector() {
		return btnModifSector;
	}


	public JFXButton getBtnBajaUsuario() {
		return btnBajaUsuario;
	}


	public JFXButton getBtnModifInsumo() {
		return btnModifInsumo;
	}


	public JFXButton getBtnAltaSector() {
		return btnAltaSector;
	}


	public JFXButton getBtnBajaProveedor() {
		return btnBajaProveedor;
	}


	public JFXButton getBtnBajaCategoria() {
		return btnBajaCategoria;
	}


	public JFXButton getBtnAltaInsumo() {
		return btnAltaInsumo;
	}


	public JFXButton getBtnBajaSector() {
		return btnBajaSector;
	}


	public JFXButton getBtnModifUsuario() {
		return btnModifUsuario;
	}


	public JFXButton getBtnBajaInsumo() {
		return btnBajaInsumo;
	}


	public JFXButton getBtnAltaUsuario() {
		return btnAltaUsuario;
	}


	public JFXButton getBtnModifCategoria() {
		return btnModifCategoria;
	}


	public JFXButton getBtnAltaProveedor() {
		return btnAltaProveedor;
	}


	public JFXButton getBtnAltaCategoria() {
		return btnAltaCategoria;
	}

	
	public JFXButton getBtnAtras() {
		return btnAtras;
	}


	public JFXButton getBtnVerInsumo() {
		return btnVerInsumo;
	}

	
	public String getNombreSeleccionado() {
		return nombreSeleccionado;
	}

	public void setNombreSeleccionado(String nombreSeleccionado) {
		this.nombreSeleccionado = nombreSeleccionado;
	}


	/********************************** METODOS ***********************************/
	
	
	@FXML
    private void initialize() {
   
    }
	
	
	public void setearTablasEnPestanias(AnchorPane iTablaUser, AnchorPane iTablaSector, AnchorPane iTablaCategoria, 
										AnchorPane iTablaInsumoGral, AnchorPane iTablaProveedor) { /////lo llamo desde el controlador ppal
		borderABMusuario.setCenter(iTablaUser);
		borderABMsectores.setCenter(iTablaSector);
		borderABMcategorias.setCenter(iTablaCategoria);
		borderABMinsumos.setCenter(iTablaInsumoGral);
		borderABMproveedores.setCenter(iTablaProveedor);
		
		//luego deberia actualizar todas las tablas..asi siempre se vera la info actual
		actualizarDatosEnTablas();
	}
    
	
	private void actualizarDatosEnTablas() {
		ControladorICsd_Principal.controllerTablaUser.removerDuplicadosEnTabla();
		ControladorICsd_Principal.controllerTablaUser.initialize();
		ControladorICsd_Principal.controllerTablaUser.cargarUsers();
		
		ControladorICsd_Principal.controllerTablaSector.removerDuplicadosEnTabla();
		ControladorICsd_Principal.controllerTablaSector.initialize();
		ControladorICsd_Principal.controllerTablaSector.cargarSectores();
		
		ControladorICsd_Principal.controllerTablaCategoria.removerDuplicadosEnTabla();
		ControladorICsd_Principal.controllerTablaCategoria.initialize();
		ControladorICsd_Principal.controllerTablaCategoria.cargarCategorias();
		
		ControladorICsd_Principal.controllerTablaInsumoGral.removerDuplicadosEnTabla();
		ControladorICsd_Principal.controllerTablaInsumoGral.initialize();
		ControladorICsd_Principal.controllerTablaInsumoGral.cargarInsumosGral();
		ControladorICsd_Principal.controllerTablaInsumoGral.getcBoxSector().setDisable(false);
		ControladorICsd_Principal.controllerTablaInsumoGral.llenarComboSector();
		ControladorICsd_Principal.controllerTablaInsumoGral.limpiarFiltros();
		
		ControladorICsd_Principal.controllerTablaProveedor.removerDuplicadosEnTabla();
		ControladorICsd_Principal.controllerTablaProveedor.initialize();
		ControladorICsd_Principal.controllerTablaProveedor.cargarProveedores();
	}
	
	
	public void limpiarSeleccionesEnTablas() {
		//desde aca tengo q decirle a cada controlador de las tablas ABM, limpiar las selecciones de insumos
		ControladorICsd_Principal.controllerTablaUser.limpiarSeleccion();
		ControladorICsd_Principal.controllerTablaSector.limpiarSeleccion();
		ControladorICsd_Principal.controllerTablaCategoria.limpiarSeleccion();
		ControladorICsd_Principal.controllerTablaInsumoGral.limpiarSeleccion();
		ControladorICsd_Principal.controllerTablaProveedor.limpiarSeleccion();
	}
	
	
	public void limpiarTxtFieldsBuscar() {
		//desde aca tengo q decirle a cada controlador de las tablas ABM, limpiar las selecciones de insumos
		ControladorICsd_Principal.controllerTablaUser.limpiarTxtFieldBuscar();
		ControladorICsd_Principal.controllerTablaSector.limpiarTxtFieldBuscar();
		ControladorICsd_Principal.controllerTablaCategoria.limpiarTxtFieldBuscar();
		ControladorICsd_Principal.controllerTablaInsumoGral.limpiarTxtFieldBuscar();
		ControladorICsd_Principal.controllerTablaProveedor.limpiarTxtFieldBuscar();
	}
	
	
	/****************************************************************************************************/
    //////////////////////////////////////////  U S E R  /////////////////////////////////////////////////
    /****************************************************************************************************/
	
	
	@FXML
    void mostrarAltaUser(ActionEvent event) {
    	//debo deshabilitar opciones "baja" y "modif"
		btnBajaUsuario.setDisable(true);
		btnModifUsuario.setDisable(true);
    	
    	borderABMusuario.setCenter(ControladorICsd_Principal.iAltaUser);
    	ControladorICsd_Principal.controllerAltaUser.getLblAltaUser().setVisible(true);
    	ControladorICsd_Principal.controllerAltaUser.getLblModifUser().setVisible(false);
    	ControladorICsd_Principal.controllerAltaUser.getBtnGuardarUser().setVisible(true);
    	ControladorICsd_Principal.controllerAltaUser.getBtnGuardarModifUser().setVisible(false);
    	
    	ControladorICsd_Principal.controllerAltaUser.llenarComboSector();
    	
    	ControladorICsd_Principal.controllerAltaUser.limpiarCampos();
    }

	
    @FXML
    void darBajaUser(ActionEvent event) {
    	Transaction tx = null;
    	try {
    		///tengo q leer q usuario selecciono de la tabla y setearle el estado a suspendido
        	
        	//1ero obtengo el dni del user q selecciono en la tabla
        	Integer dniUser = ControladorICsd_Principal.controllerTablaUser.dniUserSeleccionado();
        
        	AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			String hqlUpdate = "update Usuario set estadoUsuario = :estado "
					+ "where pkDniUsuario = :dni";
			
			int updatedEntities = appMain.getSession().createQuery( hqlUpdate )
			        .setString( "estado", "Suspendido" )
			        .setInteger( "dni", dniUser )
			        .executeUpdate();
			
			tx.commit();
			
//			appMain.getSession().close();  PARECE Q EL COMMIT AUTOMATICAMENTE CIERRA LA SESION
        	
        	actualizarTablaUsers();
        	
        	ControladorICsd_Principal.controllerTablaUser.resetearComboBoxFiltroSector();
    		
		} catch (Exception e) {
			e.printStackTrace();
			
			//rolling back to save the test data
			tx.rollback();
			
			e.getMessage();
		} 
    }


	@FXML
    void mostrarModifUser(ActionEvent event) {
//		//debo deshabilitar opciones "alta" y "baja"
		btnAltaUsuario.setDisable(true);
		btnBajaUsuario.setDisable(true);
		
    	//// hago visible btn "guardar modificacion" y label "modificar user" en pantalla "alta Usuario"
		/// y escondo el btn "guardar alta user" y lable "alta user"
		//	ya q para "alta" y "modificacion" uso la mismo vista
    	borderABMusuario.setCenter(ControladorICsd_Principal.iAltaUser);
    	ControladorICsd_Principal.controllerAltaUser.getLblAltaUser().setVisible(false);
    	ControladorICsd_Principal.controllerAltaUser.getLblModifUser().setVisible(true);
    	ControladorICsd_Principal.controllerAltaUser.getBtnGuardarUser().setVisible(false);
    	ControladorICsd_Principal.controllerAltaUser.getBtnGuardarModifUser().setVisible(true);
    	
    	//necesito guardar una referencia del user (minimo el dni) q selecciono de la tabla, para luego
    	//cargar los datos en la pantalla modif user
    	Integer dniUserSeleccionado = ControladorICsd_Principal.controllerTablaUser.dniUserSeleccionado();
    	
    	//luego busco sus datos en la bd y lleno los campos de la pantalla modif user, en su respectivo controlador.
    	//para modificar y alta user, uso la misma vista, pero diferentes componentes..x eso uso el controllerAltaUser
    	ControladorICsd_Principal.controllerAltaUser.llenarCampos(dniUserSeleccionado);
    }
	
	
	public void mostrarBtnBajaModifUser() {
    	//basicamente, habilita el btn "baja" solo si el user esta "activo"
	    try {
	    	if (!ControladorICsd_Principal.controllerTablaUser.getTablaAbmUsers().
	    			getSelectionModel().isEmpty()) {  //esto es para q no tire nullpointer cuando apreta en un lugar vacio de la tabla
	    	
	    		if (ControladorICsd_Principal.controllerTablaUser.getTablaAbmUsers().
		    			getSelectionModel().getSelectedItem().estadoUsuario.get().equals("Activo")) {
		    		
					btnBajaUsuario.setDisable(false);
					btnModifUsuario.setDisable(false);
				} else {
					btnBajaUsuario.setDisable(true);
					btnModifUsuario.setDisable(false);
				}
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}	
    }
	
	
	public void actualizarTablaUsers() {
		//como se trata de la tabla, manejo todo en el controlador de dicha tabla
		//de esta forma evito hacer lineas largas de codigo y modificar visibilidades
		
		ControladorICsd_Principal.controllerTablaUser.removerDuplicadosEnTabla();
		//este initialize, se encarga verdaderamente de actualizar la tabla,
		//pero sino llamo al procedure de arriba, queda en la tabla el
		//user ACTIVO y el SUSPENDIDO
		ControladorICsd_Principal.controllerTablaUser.initialize();
		ControladorICsd_Principal.controllerTablaUser.cargarUsers();
		//y finalmente vuelvo a setear la tabla actualizada en el centro de la pantalla admin
		borderABMusuario.setCenter(ControladorICsd_Principal.iTablaUser);
		//luego debo deshabilitar nuevamente los botones "baja" y "modificar"
		deshabilitarBtnBajaModifUser();
	}
	
	
	private void deshabilitarBtnBajaModifUser() {
    	btnAltaUsuario.setDisable(false);
		btnBajaUsuario.setDisable(true);
		btnModifUsuario.setDisable(true);
	}
	
	
	/****************************************************************************************************/
	//////////////////////////////////////  S E C T O R   ////////////////////////////////////////////////
	/****************************************************************************************************/
	
	
	@FXML
    void mostrarAltaSector(ActionEvent event) {
    	btnBajaSector.setDisable(true);
    	btnModifSector.setDisable(true);
    	borderABMsectores.setCenter(ControladorICsd_Principal.iAltaSector);
    	ControladorICsd_Principal.controllerAltaSector.getLblAltaSector().setVisible(true);
    	ControladorICsd_Principal.controllerAltaSector.getLblModifSector().setVisible(false);
    	ControladorICsd_Principal.controllerAltaSector.getBtnGuardarSector().setVisible(true);
    	ControladorICsd_Principal.controllerAltaSector.getBtnGuardarModifSector().setVisible(false);
    	ControladorICsd_Principal.controllerAltaSector.getTxtFieldNombreSector().setDisable(false);
    	ControladorICsd_Principal.controllerAltaSector.limpiarCampos();
    }
	
	
	@FXML
    void darBajaSector(ActionEvent event) {
		Sector sec = null;
		try {
			String nombreSector = ControladorICsd_Principal.controllerTablaSector.nombreSectorSeleccionado();
			sec = CRUD.obtenerSectorPorNombre(nombreSector);
			suspenderSector(sec);
	    	actualizarTablaSectores("Baja");
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }


	//solamente suspende al sector, si todas sus "categorias" y "usuarios", estan "suspendidas"
	public void suspenderSector(Sector sectorIN) {
		boolean categoriasInactivas = true;
		try {
			List<Categoria> listaCategorias = CRUD.obtenerListaCategoriasPorIdSector(sectorIN.getIdSector());
			
			for (Categoria categoria : listaCategorias) {
				//si el estado de la categoria de dicho sector es "Activo", entonces no sigo recorriendo
				//y salgo..xq no se puede dar baja el sector xq aun tiene categorias con insumos activos
				if (categoria.getEstadoCategoria().equals("Activo")) {
					categoriasInactivas = false;
					break;
				}
			}
			
			if (categoriasInactivas) { //si entra entonces todas sus categorias estaban "suspendidas"
				
				//luego debo veriicar q los users q esten asignados a dicho sector tambien esten 
				//dado de bajas
				if (usersInactivos(sectorIN.getIdSector())) { //entonces puedo dar baja Sector
					
			    	sectorIN.setEstadoSector("Suspendido");
			    	Date fechaBaja = new Date();
			    	sectorIN.setFechaBaja(fechaBaja);
			    	CRUD.actualizarObjeto(sectorIN);
				} else {
					//muestro msj q no se puede dar baja sector, xq le quedan usuarios activos a dicho sector
					mostrarMsjBajaSector(sectorIN.getNombreSector(), "user");
				}
				
			} else {
				//muestro msj q no se puede dar baja categoria
				mostrarMsjBajaSector(sectorIN.getNombreSector(), "categoria");
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
		
		
	private boolean usersInactivos(Integer idSectorIN) {
		boolean userInactivo = true;
		try {
			
			List<Usuario> listaUser = CRUD.obtenerListaUsuariosPorIdSectorJoinUsuarioSector(idSectorIN);
			
			for (Usuario usuario : listaUser) {
				if (usuario.getEstadoUsuario().equals("Activo")) {
					userInactivo = false;
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return userInactivo;
	}
	
	
	private void mostrarMsjBajaSector(String nombreSectorIN, String tipo) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
		alert.setHeaderText("El Sector: " + nombreSectorIN);
		if (tipo.equals("user")) {
			alert.setContentText("No puede darse de Baja, porque aun posee usuarios Activos");
		} else {
			alert.setContentText("No puede darse de Baja, porque aun posee categorias Activas");
		}
		alert.showAndWait();
	}
	
	
	//el argumento tipo, me indica si viene del cancelar o si hizo una modif
	//para saber si tengo q actualizar los combos en las demas pestanias
	public void actualizarTablaSectores(String tipo) {
		ControladorICsd_Principal.controllerTablaSector.removerDuplicadosEnTabla();
		ControladorICsd_Principal.controllerTablaSector.initialize();
		ControladorICsd_Principal.controllerTablaSector.cargarSectores();
		borderABMsectores.setCenter(ControladorICsd_Principal.iTablaSector);
		deshabilitarBtnBajaModifSector();
		
		if (!(tipo.equals("Cancelar"))) {
			//luego actualizo en las demas pestanias los combos de sector
			ControladorICsd_Principal.controllerTablaUser.llenarComboBoxSector();
			ControladorICsd_Principal.controllerTablaCategoria.llenarComboBoxSector();
			ControladorICsd_Principal.controllerTablaInsumoGral.verficarComboSectorCategoria();
			ControladorICsd_Principal.controllerTablaInsumoGral.llenarComboSector();
			ControladorICsd_Principal.controllerTablaInsumoGral.getcBoxSector().setDisable(false);
		}
	}
	
	
	@FXML
    void mostrarModifSector(ActionEvent event) {
		btnAltaSector.setDisable(true);
		btnBajaSector.setDisable(true);
    	borderABMsectores.setCenter(ControladorICsd_Principal.iAltaSector);
    	ControladorICsd_Principal.controllerAltaSector.getLblAltaSector().setVisible(false);
    	ControladorICsd_Principal.controllerAltaSector.getLblModifSector().setVisible(true);
    	ControladorICsd_Principal.controllerAltaSector.getBtnGuardarSector().setVisible(false);
    	ControladorICsd_Principal.controllerAltaSector.getBtnGuardarModifSector().setVisible(true);
    	String nombreSectorSeleccionado = ControladorICsd_Principal.controllerTablaSector.nombreSectorSeleccionado();
    	ControladorICsd_Principal.controllerAltaSector.llenarCampos(nombreSectorSeleccionado);
    }
	
	
	public void mostrarBtnBajaModifSector() {
	    try {
	    	if (!ControladorICsd_Principal.controllerTablaSector.getTablaAbmSectores().
	    			getSelectionModel().isEmpty()) {  //esto es para q no tire nullpointer cuando apreta en un lugar vacio de la tabla
	    		
	    		if (ControladorICsd_Principal.controllerTablaSector.getTablaAbmSectores().
		    			getSelectionModel().getSelectedItem().estadoSector.get().equals("Activo")) {
					btnBajaSector.setDisable(false);
					btnModifSector.setDisable(false);
				} else {
					btnBajaSector.setDisable(true);
					btnModifSector.setDisable(false);
				}
	    		
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}	
    }
	
	
	private void deshabilitarBtnBajaModifSector() {
		btnAltaSector.setDisable(false);
		btnBajaSector.setDisable(true);
		btnModifSector.setDisable(true);
	}
	
	
	
	/****************************************************************************************************/
    ////////////////////////////////////////  C A T E G O R I A  ////////////////////////////////////////
	/****************************************************************************************************/
	
	@FXML
    void mostrarAltaCategoria(ActionEvent event) {
		btnBajaCategoria.setDisable(true);
		btnModifCategoria.setDisable(true);
    	borderABMcategorias.setCenter(ControladorICsd_Principal.iAltaCategoria);
    	ControladorICsd_Principal.controllerAltaCategoria.getLblAltaCategoria().setVisible(true);
    	ControladorICsd_Principal.controllerAltaCategoria.getLblModifiCate().setVisible(false);
    	ControladorICsd_Principal.controllerAltaCategoria.getBtnGuardarCate().setVisible(true);
    	ControladorICsd_Principal.controllerAltaCategoria.getBtnGuardarModifCate().setVisible(false);
    	ControladorICsd_Principal.controllerAltaCategoria.llenarComboSector();
    	ControladorICsd_Principal.controllerAltaCategoria.limpiarCampos();
    }
	
	
	@FXML
    void darBajaCategoria(ActionEvent event) { 
		try {
			String nombreCategoria = ControladorICsd_Principal.controllerTablaCategoria.nombreCategoriaSeleccionado();
	    	String sectorCategoria = ControladorICsd_Principal.controllerTablaCategoria.sectorCategoriaSeleccionado();
	    	Sector sec = CRUD.obtenerSectorPorNombre(sectorCategoria);
	    	Integer idCategoriaBaja = buscarCategoria(nombreCategoria, sec.getIdSector());
	    	suspenderCategoria(idCategoriaBaja, nombreCategoria);
	    	actualizarTablaCategorias("Baja");
	    	ControladorICsd_Principal.controllerTablaCategoria.resetearComboBoxSector();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
	
	
	@FXML
    void mostrarModifCategoria(ActionEvent event) {
		try {
			Sector sec = null;
			btnAltaCategoria.setDisable(true);
			btnBajaCategoria.setDisable(true);
	    	borderABMcategorias.setCenter(ControladorICsd_Principal.iAltaCategoria);
	    	ControladorICsd_Principal.controllerAltaCategoria.getLblAltaCategoria().setVisible(false);
	    	ControladorICsd_Principal.controllerAltaCategoria.getLblModifiCate().setVisible(true);
	    	ControladorICsd_Principal.controllerAltaCategoria.getBtnGuardarCate().setVisible(false);
	    	ControladorICsd_Principal.controllerAltaCategoria.getBtnGuardarModifCate().setVisible(true);
	    	String nombreCategoriaSeleccionada = ControladorICsd_Principal.controllerTablaCategoria.nombreCategoriaSeleccionado();
	    	String sectorCategoria = ControladorICsd_Principal.controllerTablaCategoria.sectorCategoriaSeleccionado();
	    	sec = CRUD.obtenerSectorPorNombre(sectorCategoria);
	    	Integer idCategoriaBaja = buscarCategoria(nombreCategoriaSeleccionada, sec.getIdSector());
	    	ControladorICsd_Principal.controllerAltaCategoria.llenarCampos(idCategoriaBaja);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
	
	
	public void mostrarBtnBajaModifCategoria() {
    	try {
    		if (!ControladorICsd_Principal.controllerTablaCategoria.getTablaAbmCategorias().
	    			getSelectionModel().isEmpty()) {  //esto es para q no tire nullpointer cuando apreta en un lugar vacio de la tabla
    		
    			if (ControladorICsd_Principal.controllerTablaCategoria.getTablaAbmCategorias().
    					getSelectionModel().getSelectedItem().estadoCategoria.get().equals("Activo")) {
    				btnBajaCategoria.setDisable(false);
    				btnModifCategoria.setDisable(false);
    			} else {
    				btnBajaCategoria.setDisable(true);
    				btnModifCategoria.setDisable(false);
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	//como categoria ahora tiene de PK un ID, xq admite categorias de igual nombre
	//para saber con q categoria en concreto se quiere trabajar es necesario saber
	//su (nombre-sector), para obtener su ID de la BD
    private Integer buscarCategoria(String nombreCategoriaIN, Integer idSectorCategoriaIN) {
    	Integer idCatBaja = null;
    	try {
    		Categoria categoriaBD = CRUD.obtenerCategoriaPorNombreCategoriaYPorIdSector1(nombreCategoriaIN, idSectorCategoriaIN);
    		
    		idCatBaja = categoriaBD.getPkIdCategoria();
    		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    	return idCatBaja;
	}
	    
    
	private void suspenderCategoria(Integer idCategoriaBajaIN, String nombreCategoriaIN) {
		boolean darBaja = true;
		List<Insumo> listaInsumos = null;
		try {
			//verifica si todos los insumos de dicha categoria, estan "suspendido"
		    //lo cual significa q tambien estan con el "stock" en CERO
			listaInsumos = CRUD.obtenerListaInsumosPorIdCategoria1(idCategoriaBajaIN);
			
			for (Insumo insumo : listaInsumos) {
				//si el estado del insumo de dicha categoria es "Activo", entonces no sigo recorriendo
				//y salgo..xq no se puede dar baja la categoria xq aun tiene insumos con "stock"
				if (insumo.getEstadoInsumo().equals("Activo")) {
					darBaja = false;
					break;
				}
			}
			
			if (darBaja) { //si entra entonces todos sus insumos estaban "suspendidos" y "stock" CERO
				Categoria categoriaBaja = CRUD.obtenerCategoriaPorId(idCategoriaBajaIN);
		    	categoriaBaja.setEstadoCategoria("Suspendido");
		    	Date fechaBaja = new Date();
		    	categoriaBaja.setFechaBaja(fechaBaja);
		    	CRUD.actualizarObjeto(categoriaBaja);
			} else {
				//muestro msj q no se puede dar baja categoria
				mostrarMsjBajaCategoria(nombreCategoriaIN);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void mostrarMsjBajaCategoria(String nombreCategoriaIN) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AVISO");
		alert.setHeaderText("La Categoria: " + nombreCategoriaIN);
		alert.setContentText("No puede darse de Baja, porque aun posee insumos Activos");

		alert.showAndWait();
	}
	
	
	public void actualizarTablaCategorias(String tipo) {
		ControladorICsd_Principal.controllerTablaCategoria.removerDuplicadosEnTabla();
		ControladorICsd_Principal.controllerTablaCategoria.initialize();
		ControladorICsd_Principal.controllerTablaCategoria.cargarCategorias();
		borderABMcategorias.setCenter(ControladorICsd_Principal.iTablaCategoria);
		deshabilitarBtnBajaModifCategoria();
		
		if (!(tipo.equals("Cancelar"))) {
			//luego actualizo en la pestania insumo, el combo categoria
			//pero como depende del comboSector, se actualiza este y no el de categoria
			ControladorICsd_Principal.controllerTablaInsumoGral.verficarComboSectorCategoria();
			ControladorICsd_Principal.controllerTablaInsumoGral.llenarComboSector();
			ControladorICsd_Principal.controllerTablaInsumoGral.getcBoxSector().setDisable(false);
		}
	}
	
	
	private void deshabilitarBtnBajaModifCategoria() {
    	btnAltaCategoria.setDisable(false);
		btnBajaCategoria.setDisable(true);
		btnModifCategoria.setDisable(true);
	}
	
	
	/****************************************************************************************************/
    ////////////////////////////////////////  I N S U M O  ///////////////////////////////////////////////
    /****************************************************************************************************/
	
	@FXML
    void mostrarAltaInsumo(ActionEvent event) {
    	btnBajaInsumo.setDisable(true);
    	btnModifInsumo.setDisable(true);
    	btnVerInsumo.setDisable(true);
    	btnAtras.setDisable(true);
    	borderABMinsumos.setCenter(ControladorICsd_Principal.iAltaInsumo);
    	ControladorICsd_Principal.controllerAltaInsumo.setIngresoRemito(false); //aca tambien tengo q avisar desde voy a ir al alta, si de "ingreso-remito" o de "admin"
    	ControladorICsd_Principal.controllerAltaInsumo.setIngresoFactura(false);
    	ControladorICsd_Principal.controllerAltaInsumo.getLblAltaInsumo().setVisible(true);
    	ControladorICsd_Principal.controllerAltaInsumo.getLblModifInsumo().setVisible(false);
    	ControladorICsd_Principal.controllerAltaInsumo.getBtnGuardarInsumo().setVisible(true);
    	ControladorICsd_Principal.controllerAltaInsumo.getBtnGuardarModifInsumo().setVisible(false);
    	ControladorICsd_Principal.controllerAltaInsumo.llenarComboSector();
    	ControladorICsd_Principal.controllerAltaInsumo.getTxtFieldNroLote().setDisable(false);
    	ControladorICsd_Principal.controllerAltaInsumo.getTxtFieldStockActual().setVisible(false);
    	ControladorICsd_Principal.controllerAltaInsumo.getLblStockActual().setVisible(false);
    	ControladorICsd_Principal.controllerAltaInsumo.limpiarCampos("");
    	ControladorICsd_Principal.controllerAltaInsumo.cargarProveedores();
    }
	
	
	@FXML
    void darBajaInsumo(ActionEvent event) {   	
    	String nombreSeleccionado = ControladorICsd_Principal.controllerTablaInsumoGral.nombreSeleccionado();
    	suspenderInsumo(nombreSeleccionado);
    	actualizarTablaInsumosGral();
    }
	
	
	@FXML
    void mostrarModifInsumo(ActionEvent event) {
		btnAltaInsumo.setDisable(true);
		btnBajaInsumo.setDisable(true);
		btnVerInsumo.setDisable(true);
		btnAtras.setDisable(true);
    	borderABMinsumos.setCenter(ControladorICsd_Principal.iAltaInsumo);
    	ControladorICsd_Principal.controllerAltaInsumo.setIngresoRemito(false); //aca tambien tengo q avisar desde voy a ir al alta, si de "ingreso-remito" o de "admin"
    	ControladorICsd_Principal.controllerAltaInsumo.setIngresoFactura(false);
    	ControladorICsd_Principal.controllerAltaInsumo.getLblAltaInsumo().setVisible(false);
    	ControladorICsd_Principal.controllerAltaInsumo.getLblModifInsumo().setVisible(true);
    	ControladorICsd_Principal.controllerAltaInsumo.getBtnGuardarInsumo().setVisible(false);
    	ControladorICsd_Principal.controllerAltaInsumo.getBtnGuardarModifInsumo().setVisible(true);
    	Integer idInsumo = ControladorICsd_Principal.controllerTablaInsumo.idInsumoSeleccionado();
    	ControladorICsd_Principal.controllerAltaInsumo.llenarCampos(idInsumo);
    	ControladorICsd_Principal.controllerTablaInsumo.limpiarTxtFieldBuscar();
    	
    	//reseteo idCategoria q utilizo para mostrar insumos, ya sea si aplico o no filtro
//    	ControladorICsd_Principal.controllerAltaInsumo.setIdCategoria(null);
    	
    	//verifico si el user loggeado es "superuser"
    	//si es asi, entonces puede ver el stock del insumo para modificarlo
    	if (ControladorICsd_Principal.user.getAdmin().equals("Si")) {
    		ControladorICsd_Principal.controllerAltaInsumo.getTxtFieldStockActual().setVisible(true);
        	ControladorICsd_Principal.controllerAltaInsumo.getLblStockActual().setVisible(true);
		} else {
			ControladorICsd_Principal.controllerAltaInsumo.getTxtFieldStockActual().setVisible(false);
        	ControladorICsd_Principal.controllerAltaInsumo.getLblStockActual().setVisible(false);
		}
    }
	
	
	//muestra todos los insumos de un tipo de insumo (q haya seleccionado), sin importar sector/categoria
    //o si aplico filtro sector, muestra todos los insumos de ese sector
    //o si aplico filtro categoria, muestra todos los insumos de un tipo solo de esa categoria
    @FXML
    void verInsumos(ActionEvent event) {
    	try {
    		Integer idSectorSeleccionado = null;
        	idCategoriaSeleccionado = null;
        	nombreSucursalSeleccionado = null;
        	setearTablaEnPestania(ControladorICsd_Principal.iTablaInsumo);
        	ControladorICsd_Principal.controllerTablaInsumo.limpiarSeleccion();
        	this.setNombreSeleccionado(ControladorICsd_Principal.controllerTablaInsumoGral.nombreSeleccionado());
        	idSectorSeleccionado = ControladorICsd_Principal.controllerTablaInsumoGral.sectorSeleccionado();
        	nombreSucursalSeleccionado = ControladorICsd_Principal.controllerTablaInsumoGral.sucursalSeleccionado();
        	
        	if (idSectorSeleccionado != null) {
        		idCategoriaSeleccionado = ControladorICsd_Principal.controllerTablaInsumoGral.categoriaSeleccionada(idSectorSeleccionado);
    		}

        	ControladorICsd_Principal.controllerTablaInsumo.cargarInsumos2(this.getNombreSeleccionado(), 
        																	idSectorSeleccionado, ControladorICsd_Principal.controllerTablaInsumoGral.getListIdCategorias(), 
        																	idCategoriaSeleccionado, nombreSucursalSeleccionado);
        	ControladorICsd_Principal.controllerTablaInsumo.setearLblSucursal(nombreSucursalSeleccionado);
        	deshabilitarBotones2();
        	ControladorICsd_Principal.controllerTablaInsumo.limpiarTxtFieldBuscar();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
  //manejador del evento del btn Atras
    @FXML
    void mostrarTablaInsumoGral(ActionEvent event) {
    	actualizarTablaInsumosGral();
    }
    
    
	public void mostrarBtnBajaVer() {
		if (!ControladorICsd_Principal.controllerTablaInsumoGral.getTablaInsumosGral().
    			getSelectionModel().isEmpty()) {  //esto es para q no tire nullpointer cuando apreta en un lugar vacio de la tabla
			
			btnVerInsumo.setDisable(false);
	    	if (ControladorICsd_Principal.controllerTablaInsumoGral.getTablaInsumosGral().
					getSelectionModel().getSelectedItem().estadoInsumo.get().equals("Activo")) {
	    		
	    		if (ControladorICsd_Principal.controllerTablaInsumoGral.stockGralCero()) {
	    			btnBajaInsumo.setDisable(false);
	    		} else {
	    			btnBajaInsumo.setDisable(true);
	    		}
	    	} 
		}  	
	}
    
    
    public void deshabilitarBotones1() {
    	btnAltaInsumo.setDisable(false);
		btnBajaInsumo.setDisable(true);
		btnModifInsumo.setDisable(true);
		btnVerInsumo.setDisable(true);
		btnAtras.setDisable(true);
	}
    
    
    public void deshabilitarBotones2() {
    	btnAltaInsumo.setDisable(true);
		btnBajaInsumo.setDisable(true);
		btnModifInsumo.setDisable(true);
		btnVerInsumo.setDisable(true);
		btnAtras.setDisable(false);
	}
    
    
    public void mostrarBtnBajaModifInsumo() {
    	btnModifInsumo.setDisable(false);
	}
    
    
    private void suspenderInsumo(String nombreSeleccionadoIN) {
		try {
			Insumo insumoBD = CRUD.obtenerInsumoPorNombre(nombreSeleccionadoIN);
			
			insumoBD.setEstadoInsumo("Suspendido");
			Date fechaBaja = new Date();
	    	insumoBD.setFechaBaja(fechaBaja);
	    	CRUD.actualizarObjeto(insumoBD);
	    	
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    public void actualizarTablaInsumosGral() {    	
    	ControladorICsd_Principal.controllerTablaInsumoGral.removerDuplicadosEnTabla();
		ControladorICsd_Principal.controllerTablaInsumoGral.initialize();
		ControladorICsd_Principal.controllerTablaInsumoGral.getcBoxSector().setDisable(false);
		ControladorICsd_Principal.controllerTablaInsumoGral.llenarComboSector();
		ControladorICsd_Principal.controllerTablaInsumoGral.resetearComboBoxCategoria();
		ControladorICsd_Principal.controllerTablaInsumoGral.cargarInsumosGral();
		borderABMinsumos.setCenter(ControladorICsd_Principal.iTablaInsumoGral);
		deshabilitarBotones1();
	}
    
   
    public void actualizarTablaInsumos(Integer idSectorIN, Integer idCategoriaIN) {
    	setearTablaEnPestania(ControladorICsd_Principal.iTablaInsumo);
    	ControladorICsd_Principal.controllerTablaInsumo.limpiarSeleccion();
    	ControladorICsd_Principal.controllerTablaInsumo.cargarInsumos2(this.getNombreSeleccionado(), 
    																   idSectorIN, ControladorICsd_Principal.controllerTablaInsumoGral.getListIdCategorias(),
    																   idCategoriaIN, nombreSucursalSeleccionado);
    	deshabilitarBotones2();
	}
    
    
	
	/****************************************************************************************************/
    //////////////////////////////////////  P R O V E E D O R  //////////////////////////////////////////
    /****************************************************************************************************/
	
    
    @FXML
    void mostrarAltaProveedor(ActionEvent event) {
    	btnBajaProveedor.setDisable(true);
    	btnModifProveedor.setDisable(true);
    	borderABMproveedores.setCenter(ControladorICsd_Principal.iAltaProveedor);
    	ControladorICsd_Principal.controllerAltaProveedor.setIngresoRemito(false); //aca tambien tengo q avisar desde voy a ir al alta, si de "ingreso-remito" o de "admin"
    	ControladorICsd_Principal.controllerAltaProveedor.setIngresoFactura(false);
    	ControladorICsd_Principal.controllerAltaProveedor.getLblAltaProveedor().setVisible(true);
    	ControladorICsd_Principal.controllerAltaProveedor.getLblModifProveedor().setVisible(false);
    	ControladorICsd_Principal.controllerAltaProveedor.getBtnGuardarAltaProvee().setVisible(true);
    	ControladorICsd_Principal.controllerAltaProveedor.getBtnGuardarModifProvee().setVisible(false);
    	ControladorICsd_Principal.controllerTablaProveedor.limpiarTxtFieldBuscar();
    	ControladorICsd_Principal.controllerAltaProveedor.limpiarCampos();
    }
    
    
    @FXML
    void darBajaProveedor(ActionEvent event) {
    	try {
    		Integer idProveedorSeleccionado = ControladorICsd_Principal.controllerTablaProveedor.idSeleccionado();
    		Proveedor proveedorBaja = CRUD.obtenerProveedorPorId(idProveedorSeleccionado);
        	proveedorBaja.setEstadoProveedor("Suspendido");
        	Date fechaBaja = new Date();
        	proveedorBaja.setFechaBaja(fechaBaja);
        	CRUD.actualizarObjeto(proveedorBaja);
        	actualizarTablaProveedores();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    @FXML
    void mostrarModifProveedor(ActionEvent event) {
		btnAltaProveedor.setDisable(true);
		btnBajaProveedor.setDisable(true);
    	borderABMproveedores.setCenter(ControladorICsd_Principal.iAltaProveedor);
    	ControladorICsd_Principal.controllerAltaProveedor.setIngresoRemito(false); //aca tambien tengo q avisar desde voy a ir al alta, si de "ingreso-remito" o de "admin"
    	ControladorICsd_Principal.controllerAltaProveedor.setIngresoFactura(false);
    	ControladorICsd_Principal.controllerAltaProveedor.getLblAltaProveedor().setVisible(false);
    	ControladorICsd_Principal.controllerAltaProveedor.getLblModifProveedor().setVisible(true);
    	ControladorICsd_Principal.controllerAltaProveedor.getBtnGuardarAltaProvee().setVisible(false);
    	ControladorICsd_Principal.controllerAltaProveedor.getBtnGuardarModifProvee().setVisible(true);
    	Integer idProveedorSeleccionado = ControladorICsd_Principal.controllerTablaProveedor.idSeleccionado();
    	ControladorICsd_Principal.controllerAltaProveedor.llenarCampos(idProveedorSeleccionado);
    	ControladorICsd_Principal.controllerTablaProveedor.limpiarTxtFieldBuscar();
    }
    
	
    public void mostrarBtnBajaModifProveedor() {
		try {
			if (!ControladorICsd_Principal.controllerTablaProveedor.getTablaAbmProvee().
	    			getSelectionModel().isEmpty()) {  //esto es para q no tire nullpointer cuando apreta en un lugar vacio de la tabla
				
				if (ControladorICsd_Principal.controllerTablaProveedor.getTablaAbmProvee().
						getSelectionModel().getSelectedItem().estadoProveedor.get().equals("Activo")) {
					btnBajaProveedor.setDisable(false);
					btnModifProveedor.setDisable(false);
				} else {
					btnBajaProveedor.setDisable(true);
					btnModifProveedor.setDisable(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    public void actualizarTablaProveedores() {
		ControladorICsd_Principal.controllerTablaProveedor.removerDuplicadosEnTabla();
		ControladorICsd_Principal.controllerTablaProveedor.initialize();
		ControladorICsd_Principal.controllerTablaProveedor.cargarProveedores();
		borderABMproveedores.setCenter(ControladorICsd_Principal.iTablaProveedor);
		deshabilitarBtnBajaModifProveedor();
	}
	

    private void deshabilitarBtnBajaModifProveedor() {
    	btnAltaProveedor.setDisable(false);
		btnBajaProveedor.setDisable(true);
		btnModifProveedor.setDisable(true);
	}
    

    
    /****************************************************************************************************/
    //////////////////////////////////////  O T R O S  //////////////////////////////////////////
    /****************************************************************************************************/
    
    
    public void setearTablaEnPestania(AnchorPane iTablaInsumo) {
		borderABMinsumos.setCenter(iTablaInsumo);
		//luego deberia actualizar todas las tablas..asi siempre se vera la info actual
		actualizarDatosEnTabla();
	}
    
    
    private void actualizarDatosEnTabla() {
		ControladorICsd_Principal.controllerTablaInsumo.removerDuplicadosEnTabla();
		ControladorICsd_Principal.controllerTablaInsumo.initialize();
	}
    
    
    
    
    
}
