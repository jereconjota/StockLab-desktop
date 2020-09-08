package controlador;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import hibernate.util.CRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import modelo.Insumo;
import modelo.Sucursal;

public class ControladorIActualizarStockInsumo {
	
	@FXML
    private JFXButton btnDecrementar;
    
    @FXML
    private JFXButton btnCancelar;
    
    @FXML
    private JFXButton btnEnviar;
  
	@FXML
    private BorderPane borderPaneStockInsumo;

    @FXML
    private JFXTextField textFied_referencia;

    @FXML
    private JFXTextField textFied_fechaVencimiento;

    @FXML
    private JFXTextField textField_cantidadUsada;

    @FXML
    private JFXTextField textField_pdp;

    @FXML
    private JFXTextField textFied_fechaDeUso;

    @FXML
    private JFXTextField textField_nroLote;
    
    @FXML
    private JFXTextField textField_stockActual;
    
    @FXML
    private JFXTextField textFied_unidadMedida;

	@FXML
    private Label errorLabel;
    
    @FXML
    private Label label_NombreInsumo;
    
    @FXML
    private JFXComboBox<String> comboBox_sucursal;
    
    @FXML
    private TextArea textArea_observacion;
    
	private ObservableList<String> comboSucursales = FXCollections.observableArrayList("Diagnos","Cedig","Km3","Rada Tilly","Pediatrico");
	
	private Integer stockGeneral;
	
	private Date date;

	private int id;
	
	private static String flag = "No enviar"; //se utiliza para saber si en el decrementar stock, estan enviando o no insumos a otra sucursal
	
	
	/************************************  CONSTRUCTOR  ************************************/
	
	public ControladorIActualizarStockInsumo() {
		
	}
	
	/**************************** GET - SET **********************************/
	
	
	public JFXTextField getTextField_cantidadUsada() {
		return textField_cantidadUsada;
	}


	public TextArea getTextArea_observacion() {
		return textArea_observacion;
	}
	
	public Integer getStockGeneral() {
		return stockGeneral;
	}
	public void setStockGeneral(Integer stockGeneral) {
		this.stockGeneral = stockGeneral;
	}

	
	public Label getLabel_NombreInsumo() {
		return label_NombreInsumo;
	}

	
	public JFXTextField getTextField_nroLote() {
		return textField_nroLote;
	}

	
	public JFXTextField getTextFied_referencia() {
		return textFied_referencia;
	}

	
	public JFXTextField getTextFied_fechaVencimiento() {
		return textFied_fechaVencimiento;
	}

	
	public JFXTextField getTextField_pdp() {
		return textField_pdp;
	}

	
	public JFXTextField getTextFied_fechaDeUso() {
		return textFied_fechaDeUso;
	}

	
	public JFXTextField getTextField_stockActual() {
		return textField_stockActual;
	}

	
	public JFXComboBox<String> getComboBox_sucursal() {
		return comboBox_sucursal;
	}

	
	public JFXTextField getTextFied_unidadMedida() {
		return textFied_unidadMedida;
	}

	
	public Label getErrorLabel() {
		return errorLabel;
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	
	public JFXButton getBtnEnviar() {
		return btnEnviar;
	}

	public void setBtnEnviar(JFXButton btnEnviar) {
		this.btnEnviar = btnEnviar;
	}

	/********************************** METODOS ***********************************/

	@FXML
    void initialize() {
    	comboBox_sucursal.setItems(comboSucursales);
	}
	
	
	@FXML
//    void decrementarStock(ActionEvent event) {
	void decrementarStock() {
		Insumo insumo = null;
		try {
			//traemos el insumo
			Insumo insumoBD = CRUD.obtenerInsumoPorId1(this.getId());
			//Si no introdujo un numero para decrementar, marca error
			if (textField_cantidadUsada.getText().equals("")) {
				textField_cantidadUsada.setUnFocusColor(Color.RED);
				errorLabel.setText("No introdujo un valor para decrementar");
				errorLabel.setVisible(true);
				
			//Si el numero a decrementar comienza con cero, o es cero, marca error
			}else if(textField_cantidadUsada.getText().charAt(0) == '0'){
				textField_cantidadUsada.setUnFocusColor(Color.RED);
				errorLabel.setText("Introdujo un valor incorrecto");
				errorLabel.setVisible(true);
				
			//Si intenta decrementar un valor mayor a lo que queda en stock, marca error
			}else if (insumoBD.getStockActual() < Integer.valueOf(this.getTextField_cantidadUsada().getText())) {
				textField_cantidadUsada.setUnFocusColor(Color.RED);
				errorLabel.setText("No se puede decrementar el valor indicado");
				errorLabel.setVisible(true);
			}else{
				//actualizamos el stock, restamos el valor unidad que se utilizo
				insumoBD.setStockActual(insumoBD.getStockActual() - Integer.valueOf(this.getTextField_cantidadUsada().getText()));				
				this.setDate(java.sql.Date.valueOf(LocalDate.now()));
				insumoBD.setFechaUso(this.getDate());
				//Actualizamos el insumo en la base
				CRUD.actualizarObjeto(insumoBD);
				
//				/** luego si entra aca desde el "btn enviar" entonces verifico el flag global **/
				if (flag.toString().equals("Enviar")) {
					Sucursal sucursal = CRUD.obtenerSucursalPorNombre(comboBox_sucursal.getSelectionModel().getSelectedItem().toString());
					
					//verifico si existe insumo auxiliar en sucursal para extraer su pdp
					Insumo insumoPDP = null;
					
					insumoPDP = CRUD.obtenerPdpInsumoPorNombreInsumoYIdSucursal(insumoBD.getNombreInsumo(), sucursal.getPkIdSucursal());
				
					//antes de copiar el insumo, verifico si ya existe dicho insumo (lote) para dicha sucursal (en ese caso solo actualizo su stock)
					insumo = CRUD.obtenerInsumoPorNombrePorNroLotePorIdCategoriaPorSucursal(insumoBD.getNombreInsumo(), insumoBD.getNroLote(), 
																				insumoBD.getCategoria().getPkIdCategoria(), sucursal.getPkIdSucursal());
					if (insumo != null) {	//si trae algo significa q ya existe dicho insumo en la BD, entonces solo se actualiza su stock
						
						insumo.setStockActual(insumo.getStockActual() + Integer.valueOf(this.getTextField_cantidadUsada().getText()));
						CRUD.actualizarObjeto(insumo);
						
					} else { //si entra aca significa o q es un nuevo insumo en la sucursal, o uno q ya existe pero con otro nro lote
						
						//entonces verifico si se encuentra un insumo de igual nombre en la sucursal para extraer su pdp
						//y setearselo al nuevo q se esta enviando
						if (insumoPDP != null) {
							insumoBD.setPdp(insumoPDP.getPdp());
						} //si no entro, es un insumo q todavia no existia en dicha sucursal
						  //entonces ese insumo tendra los mismos valores q en diagnos (incluido pdp)
						
//						insumoBD.setSucursal(CRUD.obtenerSucursalPorNombre(comboBox_sucursal.getSelectionModel().getSelectedItem().toString()));
						insumoBD.setSucursal(sucursal);
						insumoBD.setStockActual(Integer.valueOf(this.getTextField_cantidadUsada().getText()));
						CRUD.guardarObjeto(crearInsumo(insumoBD));
					}
				}
				
				//Volvemos a la vistaInsumos con la tabla y los datos actualizados.
				ControladorICsd_Principal.controllerTablaDetalleInsumo.cargarInsumos(ControladorICsd_Principal.controllerVistaInsumos.getListviewCategorias().getSelectionModel().getSelectedItem());
				ControladorICsd_Principal.controllerVistaInsumos.getBorderPaneTablaInsumos().setCenter(ControladorICsd_Principal.iTablaDetalleInsumo);
				//Limpiamos focus
				ControladorICsd_Principal.controllerTablaDetalleInsumo.getTablaInsumos().getSelectionModel().clearSelection();
				
				//Agregamos el movimiento a la tabla de movimientos realizados
				if (flag.toString().equals("Enviar")) {
					ControladorICsd_Principal.controllerTablaMovimientos.guardarMovimientoEnviar(insumoBD,
																				getComboBox_sucursal().getSelectionModel().getSelectedItem(),
																				getTextArea_observacion().getText());
				} else {
					ControladorICsd_Principal.controllerTablaMovimientos.guardarMovimiento(insumoBD,
																				getComboBox_sucursal().getSelectionModel().getSelectedItem(),
																				getTextArea_observacion().getText());
				}
				
				errorLabel.setVisible(false);
				textField_cantidadUsada.setUnFocusColor(Color.rgb(77, 77, 77));
				//Si el insumo llego o paso su PDP, lo recuerda.
					if ( (stockGeneral - Integer.valueOf(this.getTextField_cantidadUsada().getText())) == insumoBD.getPdp()) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("ATENCION!");
						alert.setHeaderText("PDP ALCANZADO" );
						alert.setContentText(insumoBD.getNombreInsumo()+" alcanzo el punto de pedido de stock, realice el pedido correspondiente");
						alert.showAndWait();
					}else if( (stockGeneral - Integer.valueOf(this.getTextField_cantidadUsada().getText())) < insumoBD.getPdp()) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("ATENCION!");
						alert.setHeaderText("PDP ALCANZADO" );
						alert.setContentText(insumoBD.getNombreInsumo()+" sobrepaso el punto de pedido de stock, realice el pedido correspondiente");
						alert.showAndWait();
					}
				//Tambien si el insumo llega a 0 - dicho caso lo dejamos suspendido al lote (no al insumo en gral)
					if (insumoBD.getStockActual() == 0) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("ATENCION!");
						alert.setHeaderText("STOCK EN CERO" );
						alert.setContentText("Se utilizaron todas las unidades de "+ insumoBD.getNombreInsumo() +
						" con lote Nro " + insumoBD.getNroLote());
						alert.showAndWait();
						
//						insumoBD.setEstadoInsumo("Suspendido");
						
						
						CRUD.actualizarObjeto(insumoBD);
					}
					//volvemos a activar comboSector y list de categorias
					ControladorICsd_Principal.controllerVistaInsumos.getComboboxSector().setDisable(false);
					ControladorICsd_Principal.controllerVistaInsumos.getListviewCategorias().setDisable(false);
					ControladorICsd_Principal.controllerVistaInsumos.getBtnActualizar().setDisable(false);
					ControladorICsd_Principal.controllerVistaInsumos.getTextField_Buscar().setDisable(false);
					//Tambien activamos el menu principal
					ControladorILogin.controllerPpal.getMenuBarPrincipal().setDisable(false);
					ControladorICsd_Principal.controllerVistaInsumos.getBtnExportar().setDisable(false);
				}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	

	@FXML
	public void cancelarActualizarStock(){
		ControladorICsd_Principal.controllerTablaDetalleInsumo.getTablaInsumos().getSelectionModel().clearSelection();
		ControladorICsd_Principal.controllerVistaInsumos.getBorderPaneTablaInsumos().setCenter(ControladorICsd_Principal.iTablaDetalleInsumo);
		//Refrescamos la tabla de insumos
		ControladorICsd_Principal.controllerTablaDetalleInsumo.cargarInsumos(ControladorICsd_Principal.controllerVistaInsumos.getListviewCategorias().getSelectionModel().getSelectedItem());
		//volvemos a activar comboSector y list de categorias
		ControladorICsd_Principal.controllerVistaInsumos.getComboboxSector().setDisable(false);
		ControladorICsd_Principal.controllerVistaInsumos.getListviewCategorias().setDisable(false);
		ControladorICsd_Principal.controllerVistaInsumos.getBtnActualizar().setDisable(false);
		//Tambien activamos el menu principal
		ControladorILogin.controllerPpal.getMenuBarPrincipal().setDisable(false);
		ControladorICsd_Principal.controllerVistaInsumos.getBtnExportar().setDisable(false);
		ControladorICsd_Principal.controllerVistaInsumos.getTextField_Buscar().setDisable(false);
		//ocultamos btn enviar
		btnEnviar.setVisible(false);
	}
	
	
	/**
	 * copia dicho insumo de la bd (todos sus datos) y se guarda nuevamente en la BD (con otra sucursal q se haya elegido)
	 */
	@FXML
	public void enviarInsumo() {
		Insumo insumoBD = CRUD.obtenerInsumoPorId1(this.getId());
		//Si no introdujo un numero para decrementar, marca error
		if (textField_cantidadUsada.getText().equals("")) {
			textField_cantidadUsada.setUnFocusColor(Color.RED);
			errorLabel.setText("No introdujo un valor para enviar");
			errorLabel.setVisible(true);
			
		//Si el numero a decrementar comienza con cero, o es cero, marca error
		}else if(textField_cantidadUsada.getText().charAt(0) == '0'){
			textField_cantidadUsada.setUnFocusColor(Color.RED);
			errorLabel.setText("Introdujo un valor incorrecto");
			errorLabel.setVisible(true);
			
		//Si intenta decrementar un valor mayor a lo que queda en stock, marca error
		}else if (insumoBD.getStockActual() < Integer.valueOf(this.getTextField_cantidadUsada().getText())) {
			textField_cantidadUsada.setUnFocusColor(Color.RED);
			errorLabel.setText("No se puede enviar el valor indicado");
			errorLabel.setVisible(true);
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
	    	alert.setTitle("ENVIAR INSUMO");
			alert.setHeaderText("Enviar " + this.getTextField_cantidadUsada().getText() + " " + this.getLabel_NombreInsumo().getText() + " a " 
								+ comboBox_sucursal.getSelectionModel().getSelectedItem());

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				flag = "Enviar";
				//utilizo el metodo decrementar, solo q utilizare el flag para indicarle q ademas de decrementar (y todo lo demas), copie el insumo en la bd con otra sucursal
				decrementarStock();
			}
			flag = "No enviar";
		}
		
	}
	
	
	@FXML
	public void habilitarBtnEnviar( ) {
		if (!comboBox_sucursal.getSelectionModel().getSelectedItem().equals("DIAGNOS")) { //si es distinto de DIAGNOS
			btnEnviar.setVisible(true);
		} else {
			btnEnviar.setVisible(false);
		}
	}
	
	
	private Insumo crearInsumo(Insumo insumoIN) {
		Insumo insumoNuevo = new Insumo(insumoIN.getCategoria(), insumoIN.getSucursal(), insumoIN.getProveedor(), insumoIN.getNroLote(), insumoIN.getNombreInsumo(), 
										insumoIN.getNroArticulo(), insumoIN.getReferencia(), insumoIN.getFechaVencimiento(), insumoIN.getUbicacion(), 
										insumoIN.getTemperatura(), insumoIN.getPrecioInsumo(), insumoIN.getFechaUso(), 
										insumoIN.getUnidadMedida(), insumoIN.getEstadoInsumo(), insumoIN.getPdp(), insumoIN.getStockActual(), 
										insumoIN.getFechaIngreso(), insumoIN.getFechaBaja());
		return insumoNuevo;
	}
	
}
