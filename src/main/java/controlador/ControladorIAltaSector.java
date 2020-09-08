package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import hibernate.util.CRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import modelo.Categoria;
import modelo.Sector;
import modelo.Usuario;
import modelo.UsuarioSector;

public class ControladorIAltaSector {

	@FXML
    private Label lblModifSector;

    @FXML
    private Label lblMsjErrorNombreSector;

    @FXML
    private JFXTextField txtFieldCodigoSector;

    @FXML
    private JFXButton btnCancelarSector;

    @FXML
    private JFXButton btnGuardarSector;

    @FXML
    private JFXTextField txtFieldNombreSector;

    @FXML
    private JFXButton btnGuardarModifSector;

    @FXML
    private Label lblAltaSector;

    @FXML
    private Label lblEstadoSector;

    @FXML
    private JFXRadioButton radioBtnSuspendidoSector;

    @FXML
    private JFXRadioButton radioBtnActivoSector;

    @FXML
    private Label lblMsjErrorCodigo;
    
    private String nombreSectorAux;
    private Integer idSectorAux;
    
    private Sector sectorBD;
    private UsuarioSector userSectorBD;
    private Integer dniUserBD;
    
    private List<Categoria> categoriasBD = new ArrayList<Categoria>(); //es para tener referencias a las categorias q tiene asignado el sector (al cual se le cambio su PK)
    
	private boolean dioBaja;
	
	private boolean cerrarModif;
    
	
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorIAltaSector() {
    	
	}
	
    
    
    
    /**************************** GET - SET **********************************/
    
    public Label getLblModifSector() {
		return lblModifSector;
	}


	public Label getLblAltaSector() {
		return lblAltaSector;
	}
	
	    
    public JFXButton getBtnGuardarSector() {
		return btnGuardarSector;
	}


	public JFXButton getBtnGuardarModifSector() {
		return btnGuardarModifSector;
	}


	public JFXTextField getTxtFieldNombreSector() {
		return txtFieldNombreSector;
	}


	public void setNombreSectorAux(String nombreSectorAux) {
		this.nombreSectorAux = nombreSectorAux;
	}


	public void setIdSectorAux(Integer idSectorAux) {
		this.idSectorAux = idSectorAux;
	}


	public String getNombreSectorAux() {
		return nombreSectorAux;
	}


	/********************************** METODOS ***********************************/
    
    @FXML
    private void initialize() {
    	setearRadioActivo();
    }
    
    
    
	private void setearRadioActivo() {
		if (lblAltaSector.isVisible()) { //si entra entonces seteo el radio Activos
			radioBtnActivoSector.setSelected(true);
			radioBtnActivoSector.setSelectedColor(Color.web("#0098cd"));
		}
	}
    
    
    
    @FXML
    void guardarSector(ActionEvent event) {
    	pintarUnFocusTxtFields();
		boolean cerrarAlta = false;
    	try {
    		Sector sectorNuevo = nuevoSector();
    		if (noHayBlancosEnTxtFields(sectorNuevo)) {
    			if (!(nombreSectorEnUso(sectorNuevo))) {
					CRUD.guardarObjeto(sectorNuevo);
					cerrarAlta = true;
				}
    		} 
    		if (cerrarAlta) { 
        		limpiarCampos();
            	ControladorICsd_Principal.controllerPantAdmin.actualizarTablaSectores("Nuevo");
    		}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    
    @FXML
    void cancelarAltaSector(ActionEvent event) {
    	ControladorICsd_Principal.controllerPantAdmin.actualizarTablaSectores("Cancelar");
		limpiarCampos();
    }
    
    
    
    @FXML
    void guardarModifSector(ActionEvent event) {
    	String estadoSectorAux = null; //para saber si cambio el estado de baja a alta
		cerrarModif = false;
		pintarUnFocusTxtFields();
		try {
			Sector sectorModif = nuevoSector();  
			
			if (noHayBlancosEnTxtFields(sectorModif)) { 
				
				sectorBD = CRUD.obtenerSectorPorNombre(this.getNombreSectorAux());
				
				estadoSectorAux = sectorBD.getEstadoSector(); //le asigno el valor actual de la bd
			
				if (!(this.getNombreSectorAux().equals(sectorModif.getNombreSector()))) { //cambio nombre de sector
				
					if (!(nombreSectorEnUso(sectorModif))) {
						
						sectorBD.setNombreSector(sectorModif.getNombreSector());
						sectorBD.setEstadoSector(sectorModif.getEstadoSector());
						//verificar si reactivo (de suspendido a activo), entonces borrar fecha baja de tabla
						siReactivoEstado(estadoSectorAux);
						if (dioBaja) { //verifico si tiene categorias activas y usuarios activos
							verificarCategoriasYusuarios(sectorBD);
						} else {
							CRUD.actualizarObjeto(sectorBD);
							cerrarModif = true;
						}
						
					}
					
				} else { //significa q no cambio nombre, cambio los estados
					sectorBD.setEstadoSector(sectorModif.getEstadoSector());
					//verificar si reactivo (de baja a alta), entonces borrar fecha baja de tabla
					siReactivoEstado(estadoSectorAux);
					if (dioBaja) { //verifico si tiene categorias activas y usuarios activos
						verificarCategoriasYusuarios(sectorBD);
					} else {
						CRUD.actualizarObjeto(sectorBD);
						cerrarModif = true;
					}
				}
				
			}
			
			if (cerrarModif) {
	    		limpiarCampos();
	        	ControladorICsd_Principal.controllerPantAdmin.actualizarTablaSectores("Modif");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    
    @FXML
    void desactivarRadioSuspendido(ActionEvent event) {
		radioBtnActivoSector.setSelectedColor(Color.web("#0098cd"));
		radioBtnActivoSector.setSelected(true);
		radioBtnSuspendidoSector.setSelected(false);
    }

	
    @FXML
    void desactivarRadioActivo(ActionEvent event) {
    	radioBtnSuspendidoSector.setSelectedColor(Color.web("#0098cd"));
    	radioBtnSuspendidoSector.setSelected(true);
    	radioBtnActivoSector.setSelected(false);
    }
    
    
    
    private void pintarUnFocusTxtFields() {
		txtFieldNombreSector.setUnFocusColor(Color.web("#4d4d4d"));
		setearVisibilidadLblMejError();
	}
    
    
    private void setearVisibilidadLblMejError() {
		lblMsjErrorNombreSector.setVisible(false);
	}
    
    
    private Sector nuevoSector() {
		Sector sectorNuevo = null;
		String estado = null;
		String nombreSector = txtFieldNombreSector.getText();
		Date fechaActual = new Date();
		if (radioBtnActivoSector.isSelected()) {
			estado = "Activo";
		}
    	if (radioBtnSuspendidoSector.isSelected()) {
			estado = "Suspendido";
		}
		
    	sectorNuevo = new Sector(nombreSector, fechaActual, estado);
    	
		return sectorNuevo;
	}
    
    
    
    private boolean noHayBlancosEnTxtFields(Sector sectorNuevo) {
		boolean valido = true;
		if (!sectorNuevo.getNombreSector().isEmpty()) {
			if (sectorNuevo.getNombreSector().substring(0, 1).equals(" ")) {
				txtFieldNombreSector.setUnFocusColor(Color.RED);
				lblMsjErrorNombreSector.setText("No se admite espacios antes del Nombre de Sector");
				lblMsjErrorNombreSector.setVisible(true);
				valido = false;
			}
		} else {
			txtFieldNombreSector.setUnFocusColor(Color.RED);
			lblMsjErrorNombreSector.setText("Campo obligatorio");
			lblMsjErrorNombreSector.setVisible(true);
			valido = false;
		}
		
		return valido;
	}
    
    
    
    private boolean nombreSectorEnUso(Sector sectorNuevo) {
		boolean enUso = false;
		Sector sectorBD = null;
		try {
			sectorBD = CRUD.obtenerSectorPorNombre(sectorNuevo.getNombreSector()); //si esto trae un sector, significa q ya esta creado en la BD
			
			if (sectorBD != null) {
				enUso = true;
				txtFieldNombreSector.setUnFocusColor(Color.RED);
				lblMsjErrorNombreSector.setText("Ya se encuentra en uso");
				lblMsjErrorNombreSector.setVisible(true);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return enUso;
	}
    
    
    
    public void limpiarCampos() {
		txtFieldCodigoSector.clear();
		txtFieldNombreSector.clear();
		radioBtnActivoSector.setSelected(true);
		radioBtnSuspendidoSector.setSelected(false);
		pintarUnFocusTxtFields();
	}
    
    
    
    public void llenarCampos(String nombreSectorSeleccionado) {
    	try {
			Sector sectorModif = CRUD.obtenerSectorPorNombre(nombreSectorSeleccionado);
			txtFieldNombreSector.setText(sectorModif.getNombreSector());
			if (sectorModif.getEstadoSector().equals("Activo")) {
				radioBtnActivoSector.setSelected(true);
				radioBtnActivoSector.setSelectedColor(Color.web("#0098cd"));
			} else {
				radioBtnActivoSector.setSelected(false);
				radioBtnSuspendidoSector.setSelected(true);
				radioBtnSuspendidoSector.setSelectedColor(Color.web("#0098cd"));
			}
			this.setNombreSectorAux(sectorModif.getNombreSector());
			
		//necesito guardar referencia al id q tiene el sector, ya q si llega el caso de modificar el
		//nombre (su clave primaria (PK)), necesito borrar el de la bd, para guardarlo nuevamente
		//con su nueva (PK), lo q significa q el ID como es autoincrement, cambiara tambien su valor
		//lo cual seria incorrecto, xq basicamente es el mismo objeto	
			this.setIdSectorAux(sectorModif.getIdSector());
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}	
	}
    
    
    
    //si reactivo (de baja a alta) entonces borro la fecha baja
    private void siReactivoEstado(String estadoSectorAux) {
    	if (!(estadoSectorAux.equals(sectorBD.getEstadoSector()))) { //si entra entonces cambio los estados
			
    		if (estadoSectorAux.equals("Suspendido")) { 
    			//osea si el estado q tenia el sector era "suspendido", y llego hasta aca, entonces reactivo el sector
				//x lo tanto, borro la fecha baja
    			sectorBD.setFechaBaja(null);
    			dioBaja = false;
			} else { //significa q dio de baja desde el modificar
				dioBaja = true;
			}
    		
		} else {
			dioBaja = false;
		}
	}
    
    
    
  //verifico si las categorias de dicho sector estan suspendidas
  	//y si los usuarios asosiados a el, tambien estan suspendidos
  	private void verificarCategoriasYusuarios(Sector sectorIN) {
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
			    	cerrarModif = true;
				} else {
					//muestro msj q no se puede dar baja sector, xq le quedan usuarios activos a dicho sector
					mostrarMsjBajaSector(sectorIN.getNombreSector(), "user");
					cerrarModif = false;
				}
				
			} else {
				//muestro msj q no se puede dar baja categoria
				mostrarMsjBajaSector(sectorIN.getNombreSector(), "categoria");
				cerrarModif = false;
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
  	
  	
}
