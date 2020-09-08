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
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import modelo.DetalleFactura;
import modelo.Proveedor;

public class ControladorIAltaProveedor {
	
	@FXML
    private JFXTextField txtFieldNombreProvee;

    @FXML
    private JFXRadioButton radioBtnSuspendidoProveedor;

    @FXML
    private JFXButton btnCancelarAltaProvee;

    @FXML
    private Label lblAltaProveedor;

    @FXML
    private JFXButton btnGuardarModifProvee;

    @FXML
    private Label lblMsjErrorCuitProveedor;

    @FXML
    private Label lblMsjErrorTelefono;

    @FXML
    private JFXTextField txtFieldNroProvee;

    @FXML
    private JFXTextField txtFieldNroCUIT;

    @FXML
    private Label lblMsjErrorDireccion;

    @FXML
    private Label lblEstadoProveedor;

    @FXML
    private Label lblMsjErrorNombre;

    @FXML
    private Label lblModifProveedor;

    @FXML
    private JFXTextField txtFieldDirecProvee;

    @FXML
    private JFXButton btnGuardarAltaProvee;

    @FXML
    private JFXRadioButton radioBtnActivoProveedor;
    
    @FXML
    private JFXRadioButton radioBtnConRemito;

	@FXML
    private JFXRadioButton radioBtnSinRemito;
	
	private ToggleGroup group = new ToggleGroup();
	
	private boolean cerrarModif;
    
    private String nroCUITAux;
    
    private Integer idProveedorAux;
    
    private Proveedor proveedorBD;
    
    private boolean ingresoRemito;  //me sirve para saber si vengo al "ALTA", desde "ingreso remito"
    
    private boolean ingresoFactura;  //me sirve para saber si vengo al "ALTA", desde "ingreso factura"
    
    private List<DetalleFactura> detallesFacturasBD = new ArrayList<DetalleFactura>(); //sirve para guardas los "detallesFacturas", q tienen el "CUIT" viejo del proveedor q se le cambio la PK
    
    
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorIAltaProveedor() {
	
    }
    
    
    
    /**************************** GET - SET **********************************/
    
    public JFXRadioButton getRadioBtnConRemito() {
		return radioBtnConRemito;
	}
    

	public JFXRadioButton getRadioBtnSinRemito() {
		return radioBtnSinRemito;
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


	public Label getLblAltaProveedor() {
		return lblAltaProveedor;
	}


	public Label getLblModifProveedor() {
		return lblModifProveedor;
	}


	public JFXButton getBtnGuardarModifProvee() {
		return btnGuardarModifProvee;
	}


	public JFXButton getBtnGuardarAltaProvee() {
		return btnGuardarAltaProvee;
	}


	public ToggleGroup getGroup() {
		return group;
	}


	public JFXTextField getTxtFieldNroCUIT() {
		return txtFieldNroCUIT;
	}


	public Integer getIdProveedorAux() {
		return idProveedorAux;
	}

	public void setIdProveedorAux(Integer idProveedorAux) {
		this.idProveedorAux = idProveedorAux;
	}


	public String getNroCUITAux() {
		return nroCUITAux;
	}

	public void setNroCUITAux(String nroCUITAux) {
		this.nroCUITAux = nroCUITAux;
	}



	/********************************** METODOS ***********************************/
    
    @FXML
    private void initialize() {
    	setearRadioActivo();
    }

    
	@FXML
    void guardarProveedor(ActionEvent event) {
		boolean cerrarAlta = false;
		pintarUnFocusTxtFields();
		Proveedor proveedorNuevo = null; 
		try {
			proveedorNuevo = nuevoProveedor();
			
			if (proveedorNuevo != null) {
				
			/// verificar si esta en uso ese nro de proveedor	
				if (!(nroCUITEnUso(proveedorNuevo))) { //si no esta en uso, guardo en la bd
					CRUD.guardarObjeto(proveedorNuevo);
					cerrarAlta = true;
				} else { //significa q ese nroProveedor taba en uso, tonce dejo al objeto creado en null
					proveedorNuevo = null;
				}
				
			}
			
			if (cerrarAlta) { //osea q se hizo todo correctamente, entonces debe volver a la pantalla "admin" o "ingreso-remito" o "ingreso-factura"
	    		limpiarCampos();
	    		
	    		if (this.getIngresoFactura()) { //si entra entonces vuelve a ingreso-factura
	    			
	    			ControladorICsd_Principal.controllerIIngresoFactura.llenarCamposProveedor(proveedorNuevo);
	    			
				} else {	//sino verifico si tengo q volver a ingreso-remito
					
					if (this.getIngresoRemito()) {
						
//						ControladorICsd_Principal.controllerIIngresoRemito.llenarCamposProveedor(proveedorNuevo);
						
					} else {
						//sino vuelve a pantalla admin
						ControladorICsd_Principal.controllerPantAdmin.actualizarTablaProveedores();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}    
    
    
	@FXML
    void cancelarAltaProveedor(ActionEvent event) {
		if (this.getIngresoFactura()) { //esto me sirve para saber si tengo q volver a mostrar pantalla "ingreso-factura"
			ControladorILogin.controllerPpal.mostrarPantIngresoFactura();	
			} else {
				if (this.getIngresoRemito()) { //para saber si tengo q volver a pantalla ingreso-remito
					ControladorILogin.controllerPpal.mostrarPantIngresoRemito();
				} else { //sino vuelve a pantalla gestion
					ControladorICsd_Principal.controllerPantAdmin.actualizarTablaProveedores();
				}
			}
		limpiarCampos();
	}
    
    
	@FXML
    void guardarModifProveedor(ActionEvent event) {
		String estadoProveedorAux = null;
		cerrarModif = false;
		pintarUnFocusTxtFields();
		try {
			Proveedor proveedorModif = nuevoProveedor();
			if (proveedorModif != null) {
				verificarDatosParaLaModificacion(proveedorModif, estadoProveedorAux);
			}
			
			if (cerrarModif) {
	    		limpiarCampos();
	        	ControladorICsd_Principal.controllerPantAdmin.actualizarTablaProveedores();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
	@FXML
    void desactivarRadioSuspendido(ActionEvent event) {
    	radioBtnActivoProveedor.setSelectedColor(Color.web("#0098cd"));
    	radioBtnActivoProveedor.setSelected(true);
		radioBtnSuspendidoProveedor.setSelected(false);
    }

	
    @FXML
    void desactivarRadioActivo(ActionEvent event) {
    	radioBtnSuspendidoProveedor.setSelectedColor(Color.web("#0098cd"));
    	radioBtnSuspendidoProveedor.setSelected(true);
    	radioBtnActivoProveedor.setSelected(false);
    }
    
    
    private void setearRadioActivo() {
    	if (lblAltaProveedor.isVisible()) { //si entra entonces seteo el radio Activos
			radioBtnActivoProveedor.setSelected(true);
			radioBtnActivoProveedor.setSelectedColor(Color.web("#0098cd"));
		}
    	
    	this.getRadioBtnConRemito().setToggleGroup(group);
		this.getRadioBtnSinRemito().setToggleGroup(group);
		this.getRadioBtnConRemito().setSelectedColor(Color.web("#0098cd"));
		this.getRadioBtnSinRemito().setSelectedColor(Color.web("#0098cd"));
		group.selectToggle(this.getRadioBtnSinRemito());
	}
    
    
    public void limpiarCampos() {
    	txtFieldDirecProvee.clear();
    	txtFieldNombreProvee.clear();
    	txtFieldNroCUIT.clear();
    	txtFieldNroProvee.clear();
    	radioBtnActivoProveedor.setSelected(true);
    	radioBtnSuspendidoProveedor.setSelected(false);
		group.selectToggle(this.getRadioBtnSinRemito());
		pintarUnFocusTxtFields();
	}
    
    
    private void pintarUnFocusTxtFields() {
    	txtFieldDirecProvee.setUnFocusColor(Color.web("#4d4d4d"));
    	txtFieldNombreProvee.setUnFocusColor(Color.web("#4d4d4d"));
    	txtFieldNroCUIT.setUnFocusColor(Color.web("#4d4d4d"));
    	txtFieldNroProvee.setUnFocusColor(Color.web("#4d4d4d"));
    	setearVisibilidadLblMejError();
	}
    
    
    private void setearVisibilidadLblMejError() {
		lblMsjErrorDireccion.setVisible(false);
		lblMsjErrorNombre.setVisible(false);
		lblMsjErrorCuitProveedor.setVisible(false);
		lblMsjErrorTelefono.setVisible(false);
	}
    
    
    private Proveedor nuevoProveedor() {
		Proveedor proveedorNuevo = new Proveedor();
		String estado = null;
		Date fechaAlta = new Date();
		Integer flag = 0;      //este flag es para verificar q campos estan incompletos, 0 -> valido, 1 -> invalido
		
		try {
			proveedorNuevo.setFechaAlta(fechaAlta);
			
	    	if (radioBtnActivoProveedor.isSelected()) {
				estado = "Activo";
			}
	    	if (radioBtnSuspendidoProveedor.isSelected()) {
				estado = "Suspendido";
			}
	    	
	    	proveedorNuevo.setEstadoProveedor(estado);
	    	
	    	if (this.getGroup().getSelectedToggle().equals(getRadioBtnConRemito())) {
	    		proveedorNuevo.setTrabajaConRemito("Si");
			} else {
	    		proveedorNuevo.setTrabajaConRemito("No");
			}
		
	    	if (!(txtFieldNroProvee.getText().isEmpty())) {
				//con este campo no es necesario verificar xq solo tomara numeros (no toma letras y espacios)
	    		proveedorNuevo.setNroProveedor(txtFieldNroProvee.getText());
			}
			
	    /////////////////////        CAMPOS OBLIGATORIOS        ////////////////	
			if (!(txtFieldNroCUIT.getText().isEmpty())) {
			//como su PK es integer, utilizo un txtfield customizado, para q solo tome numeros (letras y espacios no toma)
			//entonces no es necesario verificar blancos	
				proveedorNuevo.setNroCuit(txtFieldNroCUIT.getText());
			} else { //osea si no tiene datos el campo nro proveedor
				txtFieldNroCUIT.setUnFocusColor(Color.RED);
				lblMsjErrorCuitProveedor.setText("Campo obligatorio");
				lblMsjErrorCuitProveedor.setVisible(true);
				flag = 1;
			}
			
			if (!(txtFieldNombreProvee.getText().isEmpty())) {
				
				if (!(hayBlanco(txtFieldNombreProvee.getText()))) {
					proveedorNuevo.setNombreProveedor(txtFieldNombreProvee.getText());
				} else {
					txtFieldNombreProvee.setUnFocusColor(Color.RED);
					lblMsjErrorNombre.setText("No se admite espacios antes del Nombre");
					lblMsjErrorNombre.setVisible(true);
					flag = 1;
				}
				
			} else {
				txtFieldNombreProvee.setUnFocusColor(Color.RED);
				lblMsjErrorNombre.setText("Campo Obligatorio");
				lblMsjErrorNombre.setVisible(true);
				flag = 1;
			}	
		
		//verificacion de campo opcional
			if (!(txtFieldDirecProvee.getText().isEmpty())) {
				if (!(hayBlanco(txtFieldDirecProvee.getText()))) {
					proveedorNuevo.setDireccionProveedor(txtFieldDirecProvee.getText());
				} else {
					txtFieldDirecProvee.setUnFocusColor(Color.RED);
					lblMsjErrorDireccion.setVisible(true);
					flag = 1;
				}
				
			}
			
			if (flag == 1) {
				proveedorNuevo = null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		
    	return proveedorNuevo;
	}
    
    
    private boolean hayBlanco(String campo) {
		boolean hay = false;

		if (campo.substring(0, 1).equals(" ")) {
			hay = true;
		}
		return hay;
	}
    
    
    private boolean nroCUITEnUso(Proveedor proveedorIN) {
		boolean enUso = false;
		try {
			Proveedor proveedor = CRUD.obtenerProveedorPorNroCuit(proveedorIN.getNroCuit());
			
			if (proveedor != null) { //significa q el cuitIN estaba en uso en la BD
				enUso = true;
				getTxtFieldNroCUIT().setUnFocusColor(Color.RED);
				lblMsjErrorCuitProveedor.setText("Ya se encuentra en uso");
				lblMsjErrorCuitProveedor.setVisible(true);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return enUso;
	}
    
    
    private void verificarDatosParaLaModificacion(Proveedor proveedorModif, String estadoProveedorAux) {
    	try {
    		proveedorBD = CRUD.obtenerProveedorPorId(this.getIdProveedorAux());
			estadoProveedorAux = proveedorBD.getEstadoProveedor();
			
			if (!(this.getNroCUITAux().equals(proveedorModif.getNroCuit()))) { //si entra es xq cambio la PK
			
				if (!(nroCUITEnUso(proveedorModif))) {
					
					proveedorBD.setNroCuit(proveedorModif.getNroCuit());
					proveedorBD.setNombreProveedor(proveedorModif.getNombreProveedor());
					proveedorBD.setDireccionProveedor(proveedorModif.getDireccionProveedor());
					proveedorBD.setNroProveedor(proveedorModif.getNroProveedor());
					proveedorBD.setEstadoProveedor(proveedorModif.getEstadoProveedor());
					proveedorBD.setTrabajaConRemito(proveedorModif.getTrabajaConRemito());
					
					siReactivoEstado(estadoProveedorAux);
					CRUD.actualizarObjeto(proveedorBD);
					
					cerrarModif = true;
					
				} else {	//osea q estaba en uso la PK
					proveedorModif = null;  //esto de dejarlos en null, es pa q con el "GARBAGE COLLECTOR", sea mas rapido limpiar la basura
					proveedorBD = null;
				}
				
			} else {
				proveedorBD.setNombreProveedor(proveedorModif.getNombreProveedor());
				proveedorBD.setDireccionProveedor(proveedorModif.getDireccionProveedor());
				proveedorBD.setNroProveedor(proveedorModif.getNroProveedor());
				proveedorBD.setEstadoProveedor(proveedorModif.getEstadoProveedor());
				proveedorBD.setTrabajaConRemito(proveedorModif.getTrabajaConRemito());
				
				siReactivoEstado(estadoProveedorAux);
				CRUD.actualizarObjeto(proveedorBD);
				
				cerrarModif = true;
			}
    		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

    
    private void siReactivoEstado(String estadoProveedorAux) {
		if (!(estadoProveedorAux.equals(proveedorBD.getEstadoProveedor()))) { //si entra cambio los estados
			if (estadoProveedorAux.equals("Suspendido")) {
				//osea si el estado q tenia el sector era "suspendido", y llego hasta aca, entonces reactivo el sector
				//x lo tanto, borro la fecha baja
				proveedorBD.setFechaBaja(null);
			} else { //dio de baja desde el modificar
				Date fechaBaja = new Date();
				proveedorBD.setFechaBaja(fechaBaja);
			}
		}
	}
    
    
    public void llenarCampos(Integer idProveedorSeleccionadoIN) {
    	Proveedor proveedorModif = CRUD.obtenerProveedorPorId(idProveedorSeleccionadoIN);
		
	// 1ero completo los campos q se q tienen valor, xq son los obligatorios 	
		txtFieldNombreProvee.setText(proveedorModif.getNombreProveedor());
		txtFieldNroCUIT.setText(proveedorModif.getNroCuit());
	
		if (proveedorModif.getEstadoProveedor().equals("Activo")) {
			radioBtnActivoProveedor.setSelected(true);
			radioBtnActivoProveedor.setSelectedColor(Color.web("#0098cd"));
		} else {
			radioBtnActivoProveedor.setSelected(false);
			radioBtnSuspendidoProveedor.setSelected(true);
			radioBtnSuspendidoProveedor.setSelectedColor(Color.web("#0098cd"));
		}
		
	//luego verifico si tienen valor los atributos opcionales
		
		if (proveedorModif.getDireccionProveedor() != null) {
			txtFieldDirecProvee.setText(proveedorModif.getDireccionProveedor());
		}
		
		if (proveedorModif.getNroProveedor() != null) {
			txtFieldNroProvee.setText(proveedorModif.getNroProveedor());
		}
		
		this.setNroCUITAux(proveedorModif.getNroCuit());
		this.setIdProveedorAux(proveedorModif.getPkIdProveedor());
		
		if (proveedorModif.getTrabajaConRemito().equals("Si")) {
			group.selectToggle(this.getRadioBtnConRemito());
		}else {
			group.selectToggle(this.getRadioBtnSinRemito());
		}
	}
    
    
}
