package controlador;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import main.AppMain;
import modelo.Sector;
import modelo.Usuario;
import modeloAux.UsuarioFX;

public class ControladorITablaAbmUsuarios {
	
	@FXML
    private TableView<UsuarioFX> tablaAbmUsers;
	
	@FXML
    private TableColumn<UsuarioFX, Number> tablaAbmUsers_DNI;
	
	@FXML
    private TableColumn<UsuarioFX, String> tablaAbmUsers_NombreUser;
	
	@FXML
    private TableColumn<UsuarioFX, String> tablaAbmUsers_Apellido;

	@FXML
    private TableColumn<UsuarioFX, String> tablaAbmUsers_Nombre;
	
    @FXML
    private TableColumn<UsuarioFX, String> tablaAbmUsers_Pass;
    
    @FXML
    private TableColumn<UsuarioFX, String> tablaAbmUsers_Admin;

    @FXML
    private TableColumn<UsuarioFX, String> tablaAbmUsers_Estado;

    @FXML
    private JFXTextField txtFieldBuscarUser;

    @FXML
    private JFXComboBox<String> cBoxSeleccioneUser;

    @FXML
    private JFXComboBox<String> cBoxFiltroSectorUser;

    private ObservableList<UsuarioFX> obListUsuarios = FXCollections.observableArrayList(); //para mostrar todos los users
    
    private ObservableList<UsuarioFX> obListItemsFiltro = FXCollections.observableArrayList(); //para tabla  gral, pero filtrada x sector
    
    private FilteredList<UsuarioFX> filtrarDatos;

    
    
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorITablaAbmUsuarios() {
	
    }
    
    
    /**************************** GET - SET **********************************/
    
    public TableView<UsuarioFX> getTablaAbmUsers() {
		return tablaAbmUsers;
	}
    
    
    
    /********************************** METODOS ***********************************/

    @FXML
    public void initialize() {
    	definirTipoDatoColumnas();
    	escuchadorEventoTabla();
    	llenarComboBoxSeleccione();
//    	llenarComboBoxSector();      //lo llamo desde el principal, para q no aumente la sobrecarga al inicio
    	setearToolTipComboSector();
    	limpiarTxtFieldBuscar();
    }
    


	private void definirTipoDatoColumnas() {
		tablaAbmUsers_Nombre.setCellValueFactory(cellData -> cellData.getValue().nombre);
		tablaAbmUsers_Apellido.setCellValueFactory(cellData -> cellData.getValue().apellido);
		tablaAbmUsers_DNI.setCellValueFactory(cellData -> cellData.getValue().dni);
		tablaAbmUsers_NombreUser.setCellValueFactory(cellData -> cellData.getValue().user);
		tablaAbmUsers_Pass.setCellValueFactory(cellData -> cellData.getValue().password);
		tablaAbmUsers_Admin.setCellValueFactory(cellData -> cellData.getValue().admin);
		tablaAbmUsers_Estado.setCellValueFactory(cellData -> cellData.getValue().estadoUsuario);
		
		alinearContenidoColumnas();
	}


	private void alinearContenidoColumnas() {
		tablaAbmUsers_Nombre.setStyle("-fx-alignment: CENTER;");
		tablaAbmUsers_Apellido.setStyle("-fx-alignment: CENTER;");
		tablaAbmUsers_DNI.setStyle("-fx-alignment: CENTER;");
		tablaAbmUsers_NombreUser.setStyle("-fx-alignment: CENTER;");
		tablaAbmUsers_Pass.setStyle("-fx-alignment: CENTER;");
		tablaAbmUsers_Admin.setStyle("-fx-alignment: CENTER;");
		tablaAbmUsers_Estado.setStyle("-fx-alignment: CENTER;");
	}
	
	
	private void escuchadorEventoTabla() {
			tablaAbmUsers.setRowFactory( tv -> {
				TableRow<UsuarioFX> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getButton() == MouseButton.PRIMARY) {
						ControladorICsd_Principal.controllerPantAdmin.mostrarBtnBajaModifUser();
					}
				});
				return row;
			});
		}
	
	
	private void llenarComboBoxSeleccione() {
		ObservableList<String> itemsCombo = FXCollections.observableArrayList();
		itemsCombo.add("DNI");
		cBoxSeleccioneUser.setItems(itemsCombo);
		
		itemsCombo.add("NOMBRE");
		cBoxSeleccioneUser.setItems(itemsCombo);
		
		itemsCombo.add("APELLIDO");
		cBoxSeleccioneUser.setItems(itemsCombo);
		
		itemsCombo.add("USER");
		cBoxSeleccioneUser.setItems(itemsCombo);
		
		cBoxSeleccioneUser.getSelectionModel().select(0);
	}
	
	
	public void llenarComboBoxSector() {
		Transaction tx = null;
		ObservableList<String> itemsCombo = FXCollections.observableArrayList();
		itemsCombo.add("SECTOR");
		try {			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sector");
			List<Sector> listaSector = query.list();
			
			for (Sector sector : listaSector) {
				itemsCombo.add(sector.getNombreSector());
			}
			
			appMain.getSession().close();
			
			cBoxFiltroSectorUser.setItems(itemsCombo);
			
			cBoxFiltroSectorUser.getSelectionModel().select(0); //q aparezca x defecto en "SECTOR"
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
	
	
	private void setearToolTipComboSector() {
		cBoxFiltroSectorUser.setTooltip(new Tooltip("Filtra Usuarios por Sector"));
	}
	
	
	public void limpiarTxtFieldBuscar() {
		txtFieldBuscarUser.clear();
	}
    
    
  //borra de la tabla el user q selecciono para darle la baja o modificar o (alta, caso de error)
  	//ya q sino, quedaba en la tabla el user actualizado y el viejo
  	public void removerDuplicadosEnTabla() {
  		obListUsuarios.clear();
  	}
  	
  	
  	public void cargarUsers() {
  		Transaction tx = null;
		try {			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Usuario");
			List<Usuario> listaUsuarios = query.list();
			
    		for (Usuario usuario : listaUsuarios) {	
    			final UsuarioFX userFX = new UsuarioFX(usuario);
    			obListUsuarios.add(userFX);
    		}
    		
    		appMain.getSession().close();
			
    		tablaAbmUsers.setItems(obListUsuarios);
    		definirOrdenDatos();
    		
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
  	
  	
  	private void definirOrdenDatos() {
		tablaAbmUsers_DNI.setSortType(TableColumn.SortType.ASCENDING);
		tablaAbmUsers.getSortOrder().add(tablaAbmUsers_DNI);
	}

  	
  	public void limpiarSeleccion() {
		tablaAbmUsers.getSelectionModel().clearSelection();
	}
  	
  	
  	//menejador del txtField buscar usuario
  	//el cual filtrara la tabla de acuerdo lo q se vaya ingresando en el textfield
  	@FXML
    void filtrarUsuarios() {
  	//antes q nada tengo q verificar si el cBoxSector, esta en:
	// "SECTOR" , si es asi, usa el "obListUsuarios"
	// sino usa, "obListItemsFiltro"
	
    txtFieldBuscarUser.setOnKeyReleased(e ->{
        txtFieldBuscarUser.textProperty().addListener((observableValue, oldValue, newValue) ->{
            filtrarDatos.setPredicate((java.util.function.Predicate<? super UsuarioFX>) usuarioFX->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                
                switch (cBoxSeleccioneUser.getSelectionModel().getSelectedItem()) {
				case "DNI":
					if(String.valueOf(usuarioFX.dni.get()).contains(newValue)){
                        return true;
                    } else if (String.valueOf(usuarioFX.dni.get()).toLowerCase().contains(lowerCaseFilter)) {
                    	return true;
                    }
					break;

				case "NOMBRE":
					if(usuarioFX.nombre.get().contains(newValue)){
                        return true;
                    } else if (usuarioFX.nombre.get().toLowerCase().contains(lowerCaseFilter)) {
                    	return true;
                    }
					break;
					
				case "APELLIDO":
					if(usuarioFX.apellido.get().contains(newValue)){
                        return true;
                    } else if (usuarioFX.apellido.get().toLowerCase().contains(lowerCaseFilter)) {
                    	return true;
                    }
					break;
				
				case "USER":
					if(usuarioFX.user.get().contains(newValue)){
                        return true;
                    } else if (usuarioFX.user.get().toLowerCase().contains(lowerCaseFilter)) {
                    	return true;
                    }
					break;
				}

                return false;
            });
        });
        SortedList<UsuarioFX> sortedData = new SortedList<>(filtrarDatos);
        sortedData.comparatorProperty().bind(tablaAbmUsers.comparatorProperty());
        tablaAbmUsers.setItems(sortedData);
        
    });
    }

    
  	
  	//manejador de click sobre el txtField buscar usuario, el cual se encarga de
  	//setear al "filtrarDatos", la correspondiente lista observable
  	//ya q si el "cBoxSector", no esta en "SIN FILTRO"
  	//debe usar la lista observable "obListItemsFiltro", la cual filtrara a los usuarios
  	//por dicho sector
  	//  y si esta en "SIN FILTRO", entonces le setea la lista observable "obListUsuarios"
  	// el "filtrarDatos", es el valor q se utiliza en el metodo "filtrarInsumos"
  	// necesario para cuando vaya escribiendo sobre el txtField, empieze a filtrar la tabla
    @FXML
    void manejadorObList() {
    	if (cBoxFiltroSectorUser.getSelectionModel().getSelectedIndex() == 0) { // SECTOR
			filtrarDatos = new FilteredList<>(obListUsuarios, e -> true);
		} else {
			filtrarDatos = new FilteredList<>(obListItemsFiltro, e -> true);
		}
    }

    
    //manejador de cBoxSector
  	//el cual filtrara la tabla gral de usuarios, de acuerdo al sector q se elija
    @FXML
    void filtrarUsuariosPorSector(ActionEvent event) {
    	try {
			if (cBoxFiltroSectorUser.getSelectionModel().getSelectedIndex() == 0) { // SECTOR
				//muestro tabla sin filtro
				tablaAbmUsers.setItems(obListUsuarios);
			} else {
				String nombreSector = cBoxFiltroSectorUser.getSelectionModel().getSelectedItem();
				//antes limpio la lista observable
				obListItemsFiltro.clear();
				
				/** este if es una hardcodeada, para evitar el bucle automatico de actions provocados
				 * 		x el comboBox, ya q al cambiar de pantalla y luego volver a pantalla gestion
				 * 		se setea el indice del comboBox a cero(x defecto), lo q parece provocar
				 * 		la activacion de la accion y entra a este metodo sin haber
				 * 		tocado el combo
				 */
				if ((cBoxFiltroSectorUser.getSelectionModel().getSelectedIndex() != -1) &&
						(!cBoxFiltroSectorUser.getSelectionModel().getSelectedItem().equals("SECTOR"))) {
					
					filtrarTabla(nombreSector);
					
				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    private void filtrarTabla(String nombreSectorIN) {
		Sector sec = null;
		Transaction tx = null;
		try {		
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query1 = appMain.getSession().createQuery("from Sector where Nombre_Sector= :id");  //1ero obtengo el sector de la bd, para saber su ID
			query1.setString("id", nombreSectorIN);
			sec = (Sector) query1.uniqueResult();
			
			
			/**este query se encarga de traer un listado de usuarios dependiendo del sector q se haya elegido en el comboBox
			 * 		el "Us.usuario" equivale a = Us.FK_DNI_Usuario
			 * 		el "Us.sector" equivale a = Us.FK_Id_Sector
			 * 	ya q asi esta detallado en el archivo .xml de mapping de la clase UsuarioSector
			 */
			
			Query query2 = appMain.getSession().createQuery("select new Usuario(u.pkDniUsuario, u.nombre, u.apellido, u.user, u.password, u.admin, u.estadoUsuario) "
					+ "from UsuarioSector uS "
					+ "inner join uS.usuario u "
					+ "where uS.sector= :id and uS.usuario = u.pkDniUsuario");
			
			query2.setInteger("id", sec.getIdSector());
			
			List<Usuario> listaUsuarios = query2.list();
			
			for (Usuario user : listaUsuarios) {
				UsuarioFX userFX = new UsuarioFX(user);
				
				obListItemsFiltro.add(userFX);
			}
			
			
			appMain.getSession().close();
			
			tablaAbmUsers.setItems(obListItemsFiltro);
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
    
    
    
    public Integer dniUserSeleccionado() {
		Integer dniUser = tablaAbmUsers.getSelectionModel().getSelectedItem().dni.get();
		return dniUser;
	}
    
    
    
    public void resetearComboBoxFiltroSector() {
    	cBoxFiltroSectorUser.getSelectionModel().clearAndSelect(0);
    }
    
}
