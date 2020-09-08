package controlador;

import java.util.Date;
import java.util.List;

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
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import modelo.Categoria;
import modelo.Insumo;
import modelo.Sector;

public class ControladorIAltaCategoria {
	
	@FXML
    private JFXButton btnGuardarCate;

    @FXML
    private JFXRadioButton radioBtnSuspendidoCat;

    @FXML
    private JFXButton btnGuardarModifCate;

    @FXML
    private JFXTextField txtFieldCodigoCate;

    @FXML
    private JFXTextField txtFieldNombreCate;

    @FXML
    private Label lblEstadoCate;

    @FXML
    private JFXComboBox<String> cBoxCateASector;

    @FXML
    private JFXButton btnCancelarCate;

    @FXML
    private JFXRadioButton radioBtnActivoCat;

    @FXML
    private Label lblAltaCategoria;

    @FXML
    private Label lblModifiCate;
    
    @FXML
    private Label lblMsjErrorNombreCat;
    
    
    private boolean cerrarModif;
    
    private Integer idCategoriaAux;
    
    private String nombreCategoriaAux;
    
    private Categoria categoriaBD;
    
    private String sectorCategoriaAux;
    
    private boolean dioBaja;
 
    
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorIAltaCategoria() {
    	
	}
    
    
    /**************************** GET - SET **********************************/
    
    public Label getLblAltaCategoria() {
		return lblAltaCategoria;
	}


	public Label getLblModifiCate() {
		return lblModifiCate;
	}
    
	
    public JFXButton getBtnGuardarCate() {
		return btnGuardarCate;
	}


	public JFXButton getBtnGuardarModifCate() {
		return btnGuardarModifCate;
	}

	
	public String getNombreCategoriaAux() {
		return nombreCategoriaAux;
	}

	public void setNombreCategoriaAux(String nombreCategoriaAux) {
		this.nombreCategoriaAux = nombreCategoriaAux;
	}

	
	public Integer getIdCategoriaAux() {
		return idCategoriaAux;
	}
	
	public void setIdCategoriaAux(Integer idCategoriaAux) {
		this.idCategoriaAux = idCategoriaAux;
	}

	
	public String getSectorCategoriaAux() {
		return sectorCategoriaAux;
	}

	public void setSectorCategoriaAux(String sectorCategoriaAux) {
		this.sectorCategoriaAux = sectorCategoriaAux;
	}


	/********************************** METODOS ***********************************/
    
    
    @FXML
    private void initialize() {
    	setearRadioActivo();
    }


    @FXML
    void guardarCategoria(ActionEvent event) {
    	pintarUnFocusTxtFields();
		boolean cerrarAlta = false;
		Categoria categoriaNueva = null;
		try {
			categoriaNueva = nuevaCategoria();
			if (categoriaNueva != null) {
				if (datosCategoriaValidos(categoriaNueva)) {
					CRUD.guardarObjeto(categoriaNueva);
					cerrarAlta = true;
				}
			}
			
			if (cerrarAlta) { //osea q se hizo todo correctamente, entonces debe volver a la pantalla admin
	    		limpiarCampos();
	        	ControladorICsd_Principal.controllerPantAdmin.actualizarTablaCategorias("Nuevo");
	        	ControladorICsd_Principal.controllerTablaCategoria.resetearComboBoxSector();
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    @FXML
    void cancelarAltaCate(ActionEvent event) {
    	ControladorICsd_Principal.controllerPantAdmin.actualizarTablaCategorias("Cancelar");
    	ControladorICsd_Principal.controllerTablaCategoria.resetearComboBoxSector();
		limpiarCampos();
    }

    
    @FXML
    void guardarmodifCate(ActionEvent event) {
    	String estadoCategoriaAux = null;
		cerrarModif = false;
		pintarUnFocusTxtFields();
		Categoria categoriaModif = null;
		try {
			categoriaModif = nuevaCategoria();
			
			if (categoriaModif != null) {
				verificarDatosParaLaModificacion(categoriaModif, estadoCategoriaAux);
			}
			
			if (cerrarModif) {
	    		limpiarCampos();
	        	ControladorICsd_Principal.controllerPantAdmin.actualizarTablaCategorias("Modif");
	        	ControladorICsd_Principal.controllerTablaCategoria.resetearComboBoxSector();
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    @FXML
    void desactivarRadioSuspendido(ActionEvent event) {
    	radioBtnActivoCat.setSelectedColor(Color.web("#0098cd"));
		radioBtnSuspendidoCat.setSelected(false);
    }
    
    
    @FXML
    void desactivarRadioActivo(ActionEvent event) {
    	radioBtnSuspendidoCat.setSelectedColor(Color.web("#0098cd"));
    	radioBtnActivoCat.setSelected(false);
    }
    
    
	private void setearRadioActivo() {
		if (lblAltaCategoria.isVisible()) { //si entra entonces seteo el radio Activos
			radioBtnActivoCat.setSelected(true);
			radioBtnActivoCat.setSelectedColor(Color.web("#0098cd"));
		}
	}
    
    
    public void llenarComboSector() {
		try {
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
			List<Sector> listaSectores = CRUD.obtenerListaSectoresActivos();
			
			for (Sector sector : listaSectores) {
				//antes de agregar los sectores al combo
				//tengo q evitar q meta el sector "administracion"
				//y tambien aquellos q esten "suspendidos"  (YA NO NECESTIO VERIFICAR XQ LA OP CRUD YA ME TRAE SOLO LOS ACTIVOS)
				if (!(sector.getNombreSector().equals("Administracion"))) {
					itemsCombo.add(sector.getNombreSector());
				}
			}
			cBoxCateASector.setItems(itemsCombo);
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    public void limpiarCampos() {
    	txtFieldCodigoCate.clear();
    	txtFieldNombreCate.clear();
    	radioBtnActivoCat.setSelected(true);
    	radioBtnSuspendidoCat.setSelected(false);
    	cBoxCateASector.getSelectionModel().select(-1);
		pintarUnFocusTxtFields();
	}
    
    
    private void pintarUnFocusTxtFields() {
		txtFieldNombreCate.setUnFocusColor(Color.web("#4d4d4d"));
		cBoxCateASector.setUnFocusColor(Color.web("#4d4d4d"));
		setearVisibilidadLblMejError();
	}
    
    
    private void setearVisibilidadLblMejError() {
		lblMsjErrorNombreCat.setVisible(false);
	}
    
    
    private Categoria nuevaCategoria() {
		Categoria categoriaNueva = null;
		Sector sector = null; //es necesario, para determinar a q sector pertenece dicha categoria
		String nombreCat = txtFieldNombreCate.getText();
		String estado = null;
		Date fechaAlta = new Date();
		String nombreSector = null;
		
		if (cBoxCateASector.getSelectionModel().getSelectedIndex() == -1) {  //osea sino selecciono un sector, entonces pinto el combo de rojo
			cBoxCateASector.setUnFocusColor(Color.RED);
		} else {
			nombreSector = cBoxCateASector.getSelectionModel().getSelectedItem().toString();
			sector = CRUD.obtenerSectorPorNombre(nombreSector);
		}
    	
    	if (radioBtnActivoCat.isSelected()) {
			estado = "Activo";
		}
    	if (radioBtnSuspendidoCat.isSelected()) {
			estado = "Suspendido";
		}
    
    	if (nombreSector != null) {  //creo objeto categoria, solo si selecciono sector
    		categoriaNueva = new Categoria(nombreCat, sector, fechaAlta, estado);
		}
    	
		return categoriaNueva;
	}
    
    
    private boolean datosCategoriaValidos(Categoria categoriaNueva) {
		boolean validos = true;
		boolean nombreCategoriaUsada = false;
		 
		 try { // si no hay blancos en los campos, entonces verifico q no este en uso el nombre de la categoria
			 	//para el sector q haya elegido
			 
			 if (noHayBlancosEnTxtFields(categoriaNueva)) {
				 
				 Categoria categoriaBD = CRUD.obtenerCategoriaPorNombreCategoriaYPorIdSector1(categoriaNueva.getNombreCategoria(), categoriaNueva.getSector().getIdSector());
				 
				 if (categoriaBD != null) { //si entra significa q encontro una categoria perteneciente a un sector , q esta tratando de crear nuevamente
					 nombreCategoriaUsada = true;
				 }
				 
				 if (nombreCategoriaUsada) {
					txtFieldNombreCate.setUnFocusColor(Color.RED);
					lblMsjErrorNombreCat.setText("Ya se encuentra en uso");
					lblMsjErrorNombreCat.setVisible(true);
					validos = false;
				}
				 
			 } else {	//si hubo algun blanco entonces avisa en pantalla
				 validos = false;
			 }
			 	 	 
			} catch (Exception e) {
				e.printStackTrace();
				e.getMessage();
			}
		 
		 return validos;
	}
    
    
    private boolean noHayBlancosEnTxtFields(Categoria categoriaNueva) {
		boolean valido = true;
		if (!categoriaNueva.getNombreCategoria().isEmpty()) {
			if (categoriaNueva.getNombreCategoria().substring(0, 1).equals(" ")) {
				txtFieldNombreCate.setUnFocusColor(Color.RED);
				lblMsjErrorNombreCat.setText("No se admite espacios antes del Nombre de Categoria");
				lblMsjErrorNombreCat.setVisible(true);
				valido = false;
			}
		} else {
			txtFieldNombreCate.setUnFocusColor(Color.RED);
			lblMsjErrorNombreCat.setText("Campo obligatorio");
			lblMsjErrorNombreCat.setVisible(true);
			valido = false;
		}
		
		return valido;
	}
    
    
    private void verificarDatosParaLaModificacion(Categoria categoriaModif, String estadoCategoriaAux) {
		Sector sec = null;
		try {
			if (noHayBlancosEnTxtFields(categoriaModif)) {
				categoriaBD = CRUD.obtenerCategoriaPorId(this.getIdCategoriaAux()); //este idCategoriaAux, se setea en el metodo llenarCampos()
				estadoCategoriaAux = categoriaBD.getEstadoCategoria();
				sec = CRUD.obtenerSectorPorId(categoriaModif.getSector().getIdSector());
				
				if (this.getNombreCategoriaAux().equals(categoriaModif.getNombreCategoria())
						&& this.getSectorCategoriaAux().equals(sec.getNombreSector())) { 	//si entra es xq no cambio ni nombre ni sector, quizas cambio estado
						
					categoriaBD.setEstadoCategoria(categoriaModif.getEstadoCategoria());
					siReactivoEstado(estadoCategoriaAux);
					
					if (dioBaja) { //verifico si tiene insumos activos
						verificarInsumos(categoriaModif.getNombreCategoria(), sec.getNombreSector());
					} else {
						CRUD.actualizarObjeto(categoriaBD);
						cerrarModif = true;
					}
				
				} else { //si entra es xq cambio nombre o sector o estado, o ambos
					
					if (!(nombreCategoriaEnUso(categoriaModif))) {
						categoriaBD.setNombreCategoria(categoriaModif.getNombreCategoria());
						categoriaBD.setSector(categoriaModif.getSector());
						
						if (!(categoriaModif.getEstadoCategoria().equals(estadoCategoriaAux))) { //si cambio los estados
							
							categoriaBD.setEstadoCategoria(categoriaModif.getEstadoCategoria());
							siReactivoEstado(estadoCategoriaAux); 					//verifico si reactivo o dio baja
							if (dioBaja) { //verifico si tiene insumos activos
								
								//mando el nombre q tenia la categoria antes del cambio
								//y el nombre sector antes del cambio
								verificarInsumos(nombreCategoriaAux, sectorCategoriaAux);
							} else { 												//si viene x aca, es xq reactivo
								CRUD.actualizarObjeto(categoriaBD);
								cerrarModif = true;
							}
						} else {													//si viene x aca, es xq cambio nombre o sector
							CRUD.actualizarObjeto(categoriaBD);
							cerrarModif = true;
						}
					
					} else {
						txtFieldNombreCate.setUnFocusColor(Color.RED);
						lblMsjErrorNombreCat.setText("Ya se encuentra en uso");
						lblMsjErrorNombreCat.setVisible(true);
						cerrarModif = false;
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    public void llenarCampos(Integer idCategoriaSeleccionadaIN) {
		try {
			Sector sec = null;
			llenarComboSector();
			Categoria categoriaModif = CRUD.obtenerCategoriaPorId(idCategoriaSeleccionadaIN);
			txtFieldNombreCate.setText(categoriaModif.getNombreCategoria());
			if (categoriaModif.getEstadoCategoria().equals("Activo")) {
				radioBtnActivoCat.setSelected(true);
				radioBtnActivoCat.setSelectedColor(Color.web("#0098cd"));
			} else {
				radioBtnActivoCat.setSelected(false);
				radioBtnSuspendidoCat.setSelected(true);
				radioBtnSuspendidoCat.setSelectedColor(Color.web("#0098cd"));
			}
			sec = CRUD.obtenerSectorPorId(categoriaModif.getSector().getIdSector());
			buscarSectorYsetearCombo(categoriaModif, sec);
			this.setNombreCategoriaAux(categoriaModif.getNombreCategoria());
			this.setIdCategoriaAux(categoriaModif.getPkIdCategoria());
			this.setSectorCategoriaAux(sec.getNombreSector());
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    private void buscarSectorYsetearCombo(Categoria categoriaModif, Sector sectorIN) {
		Integer c = 0;
		boolean encontro = false;
		try {
			while ((c < cBoxCateASector.getItems().size()) && (!(encontro))) { //recorro el comboBox sector 
				if (cBoxCateASector.getItems().get(c).equals(sectorIN.getNombreSector())) { //busco el sector al q pertenece la categoria, y si esta, seteo el combo en esa direccion
					cBoxCateASector.getSelectionModel().select(c);
					encontro = true;
				} else {
					c = c + 1;
				}
			}  //fin recorrer el comboBox
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    private void siReactivoEstado(String estadoCategoriaAux) {
		if (!(estadoCategoriaAux.equals(categoriaBD.getEstadoCategoria()))) { //si entra cambio los estados
			if (estadoCategoriaAux.equals("Suspendido")) {
				//osea si el estado q tenia el sector era "suspendido", y llego hasta aca, entonces reactivo el sector
				//x lo tanto, borro la fecha baja
				categoriaBD.setFechaBaja(null);
				dioBaja = false;
			} else { //dio de baja desde el modificar
				dioBaja = true;
			}
		} else {
			dioBaja = false;
		}
	}
    
    
    private void verificarInsumos(String nombreCategoriaIN, String nombreSectorIN) {
		Integer idCategoriaBaja = buscarCategoria(nombreCategoriaIN, nombreSectorIN); //1ero busco el id de la categoria q se quiere dar de baja
		insumosSuspendidos(idCategoriaBaja, nombreCategoriaIN);
	}
    
    
    private Integer buscarCategoria(String nombreCategoriaIN, String nombreSectorIN) {
    	Integer idCatBaja = null;
    	Sector sec = null;
    	try {
    		sec = CRUD.obtenerSectorPorNombre(nombreSectorIN);
    		Categoria categoriaBD = CRUD.obtenerCategoriaPorNombreCategoriaYPorIdSector1(nombreCategoriaIN, sec.getIdSector());
    		idCatBaja = categoriaBD.getPkIdCategoria();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    	return idCatBaja;
	}
    
    
    private void insumosSuspendidos(Integer idCategoriaBajaIN, String nombreCategoriaIN) {
		boolean darBaja = true;
		try {
			List<Insumo> listaInsumos = CRUD.obtenerListaInsumosPorIdCategoria1(idCategoriaBajaIN);
			
			for (Insumo insumo : listaInsumos) {
				if (insumo.getEstadoInsumo().equals("Activo")) {
					darBaja = false;
					break;
				} 
			}
			
			if (darBaja) { //si entra entonces todos sus insumos estaban "suspendidos" y "stock" CERO
		    	Date fechaBaja = new Date();
		    	categoriaBD.setFechaBaja(fechaBaja);
		    	CRUD.actualizarObjeto(categoriaBD);
		    	cerrarModif = true;
			} else {
				//muestro msj q no se puede dar baja categoria
				mostrarMsjBajaCategoria(nombreCategoriaIN);
				cerrarModif = false;
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
    
    
    private boolean nombreCategoriaEnUso(Categoria categoriaModif) {
		boolean enUso = false;
		Sector sec = null;
		try {
			sec = CRUD.obtenerSectorPorId(categoriaModif.getSector().getIdSector());
			
			Categoria categoria = CRUD.obtenerCategoriaPorNombreCategoriaYPorIdSector1(categoriaModif.getNombreCategoria(), sec.getIdSector());
			
			if (categoria != null) { //significa q el nombre de categoria q esta tratando de usar esta en uso
				enUso = true;
			}
						
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return enUso;
	}
    
}
