package controlador;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import hibernate.util.CRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import main.AppMain;
import modelo.Sector;
import modelo.Sucursal;
import modelo.Usuario;
import modelo.UsuarioSector;
import modelo.UsuarioSectorId;

public class ControladorIAltaUsuario {
	
	@FXML
    private JFXTextField txtFieldApellido;

    @FXML
    private JFXButton btnCancelarUser;

    @FXML
    private JFXButton btnGuardarUser;

    @FXML
    private Label lblModifUser;

    @FXML
    private JFXTextField txtFieldPass;

    @FXML
    private JFXButton btnGuardarModifUser;

    @FXML
    private JFXTextField txtFieldNombre;

    @FXML
    private JFXTextField txtFieldNombreUser;

    @FXML
    private Label lblEstadoUser;

    @FXML
    private Label lblAltaUser;

    @FXML
    private JFXTextField txtFieldDNI;
    
    @FXML
    private JFXComboBox<String> cBoxUserASector;
    
    @FXML
    private JFXRadioButton radioBtnSuspendido;
    
    @FXML
    private JFXRadioButton radioBtnActivo;
    
    @FXML
    private JFXCheckBox checkBoxAdmin;
    
    @FXML
    private Label lblMsjErrorUser;
    
    @FXML
    private Label lblMsjErrorNombre;
    
    @FXML
    private Label lblMsjErrorApellido;

    @FXML
    private Label lblMsjErrorDni;
    
    @FXML
    private Label lblMsjErrorContrasena;
    
    @FXML
    private Label lblSuperUser;
    
    private String nombreSectorDelComboBox; //necesario para saber a q sector vincular el usuario
    
    /* estos dos atributos me sirven para la verificacion de datos cuando realizo una modificacion,
     * para evitar q realize una comparacion con la misma persona y de error de duplicacion de datos  
     */
   private String nomUserAux;  
   private Integer dniAux;  
   
   /*va a contener el usuario de la bd, q se saca para la actualizacion de datos, mediante
    * la pantalla modificacion  
    */
   private Usuario userBD;
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorIAltaUsuario() {
    	
	}
    
    /**************************** GET - SET **********************************/
    
    public Label getLblAltaUser() {
		return lblAltaUser;
	}
    
    
    public Label getLblModifUser() {
		return lblModifUser;
	}
    

	public JFXButton getBtnGuardarUser() {
		return btnGuardarUser;
	}

	
	public JFXButton getBtnGuardarModifUser() {
		return btnGuardarModifUser;
	}
	

	public String getNomUserAux() {
		return nomUserAux;
	}

	public void setNomUserAux(String nomUserAux) {
		this.nomUserAux = nomUserAux;
	}

	
	public Integer getDniAux() {
		return dniAux;
	}

	public void setDniAux(Integer dniAux) {
		this.dniAux = dniAux;
	}
	
	
	public String getNombreSectorDelComboBox() {
		return nombreSectorDelComboBox;
	}

	public void setNombreSectorDelComboBox(String nombreSectorDelComboBox) {
		this.nombreSectorDelComboBox = nombreSectorDelComboBox;
	}

	/********************************** METODOS ***********************************/
    
    
    @FXML
    private void initialize() {
    	txtFieldDNI.setOnKeyTyped(e->limitarIngresoCaracter(e));
    	setearRadioActivo(); 
    	agregarToolTipLabel();
    }
    
    
    @FXML
    void cancelarAltaUser(ActionEvent event) {
    	ControladorICsd_Principal.controllerPantAdmin.actualizarTablaUsers();
    	//como utilizo el mismo cancelar tanto pa "alta" y "modificar", debo limpiar los campos
    	limpiarCampos();
    	//luego reseteo la posicion del comboFiltroSector (x si antes de ir tanto a alta o modif
    	//se habia utilizado el filtro)
    	ControladorICsd_Principal.controllerTablaUser.resetearComboBoxFiltroSector();
    }
    
    
    @FXML
    void guardarAltaUser(ActionEvent event) {
    	pintarUnFocusTxtFields();
	
		boolean cerrarAlta = false;
		Transaction tx = null;	
    	try {
    		Usuario userNuevo = nuevoUser();
    		
    		if (userNuevo != null) {
    			
    			//antes de guardar verificar (blanco en nom-ape, longitud dni, user en uso
        		
        		if (datosUserValidos(userNuevo)) {
        			
        			AppMain appMain = AppMain.getSingletonSession();
        			tx = appMain.getSession().beginTransaction();
        			
        			appMain.getSession().save(userNuevo);
        			tx.commit();
        			
        			if (!(checkBoxAdmin.isSelected())) { // luego, si no es administrador, tonce lleno la relacion usuario-sector				
        				crearRelacionUserSector(userNuevo);
    				}
            
                	cerrarAlta = true; 
                	
    			} else { //sino son validos los datos 
    				userNuevo = null;
    			}
			}
    		
    		if (cerrarAlta) { //osea q se hizo todo correctamente, entonces debe volver a la pantalla admin
        		limpiarCampos();
            	ControladorICsd_Principal.controllerPantAdmin.actualizarTablaUsers();
            	ControladorICsd_Principal.controllerTablaUser.resetearComboBoxFiltroSector();
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
    }
    
    
    
    private Usuario nuevoUser() {
		
		Usuario userNuevo = new Usuario();
    	String estado = null;
    	Integer flag = 0;      //este flag es para verificar q campos estan incompletos, 0 -> valido, 1 -> invalido
    	Sucursal sucursal = null;
    	
    	if (radioBtnActivo.isSelected()) {
    		estado = "Activo";
		} else if (radioBtnSuspendido.isSelected()) {
			estado = "Suspendido";
		}
    	
    	userNuevo.setEstadoUsuario(estado);
    	
    // Verifico si completo los campos obligatorios
    	
    	if (!(txtFieldDNI.getText().isEmpty())) {	
			userNuevo.setPkDniUsuario(Integer.valueOf(txtFieldDNI.getText()));
    	} else { //osea si no tiene datos el campo 
			txtFieldDNI.setUnFocusColor(Color.RED);
			lblMsjErrorDni.setText("Campo obligatorio");
			lblMsjErrorDni.setVisible(true);
			flag = 1;
    	}
    	
    	if (!(txtFieldNombre.getText().isEmpty())) {	
			userNuevo.setNombre(txtFieldNombre.getText());
		} else { //osea si no tiene datos el campo
			txtFieldNombre.setUnFocusColor(Color.RED);
			lblMsjErrorNombre.setText("Campo obligatorio");
			lblMsjErrorNombre.setVisible(true);
			flag = 1;
		}
    	
    	if (!(txtFieldApellido.getText().isEmpty())) {	
			userNuevo.setApellido(txtFieldApellido.getText());
		} else { //osea si no tiene datos el campo
			txtFieldApellido.setUnFocusColor(Color.RED);
			lblMsjErrorApellido.setText("Campo obligatorio");
			lblMsjErrorApellido.setVisible(true);
			flag = 1;
		}
    	
    	if (!(txtFieldNombreUser.getText().isEmpty())) {	
			userNuevo.setUser(txtFieldNombreUser.getText());
		} else { //osea si no tiene datos el campo
			txtFieldNombreUser.setUnFocusColor(Color.RED);
			lblMsjErrorUser.setText("Campo obligatorio");
			lblMsjErrorUser.setVisible(true);
			flag = 1;
		}
    	
    	if (!(txtFieldPass.getText().isEmpty())) {	
			userNuevo.setPassword(txtFieldPass.getText());
		} else { //osea si no tiene datos el campo
			txtFieldPass.setUnFocusColor(Color.RED);
			lblMsjErrorContrasena.setText("Campo obligatorio");
			lblMsjErrorContrasena.setVisible(true);
			flag = 1;
		}
    	
    	if (checkBoxAdmin.isSelected()) {    
			userNuevo.setAdmin("Si");
		} else {
			userNuevo.setAdmin("No");  //si no es admin, entonce se habilita combo Sector, pero verifico q haya seleccionado algo
			
			if (cBoxUserASector.getSelectionModel().isEmpty()) {//osea sino selecciono un Sector, entonces pinto el combo de rojo
				cBoxUserASector.setUnFocusColor(Color.RED);
				flag = 1;
			} else {
				//cree una instancia para el nombre sector, para luego tener referencia a lo q selecciono en el combo
				//y usarlo para luego hacer la relacion usuario-sector
				this.setNombreSectorDelComboBox(cBoxUserASector.getSelectionModel().getSelectedItem().toString());
			}
		}
    	
    	sucursal = CRUD.obtenerSucursal();
    	userNuevo.setSucursal(sucursal);
    	
    	if (flag == 1) {
    		userNuevo = null;
		}
    	
		return userNuevo;
	}
    
    
    
 // verifica blancos en (nom,ape,user) , longitud (dni) y q no este en uso (dni,user)
 	 private boolean datosUserValidos(Usuario userNuevo) {
		 boolean validos = true;
		 boolean userUsado = false;
		 boolean dniUsado = false;
		 Transaction tx = null;
		 try { 
			 
			if (noHayBlancosEnTxtFields(userNuevo)) {  // si no hay blancos en los campos, entonces... 
				 
				AppMain appMain = AppMain.getSingletonSession();
				tx = appMain.getSession().beginTransaction();
				
				Query query1 = appMain.getSession().createQuery("from Usuario where pkDniUsuario= :id");
				query1.setInteger("id", userNuevo.getPkDniUsuario());
				Usuario usuarioDNI = (Usuario) query1.uniqueResult();
				
				if (usuarioDNI != null) { //si la consulta me trajo un usuario, significa q metio un dni de un usuario q ya existe en la BD
					dniUsado = true;
				}
				
				if (dniUsado) {
					txtFieldDNI.setUnFocusColor(Color.RED);
					lblMsjErrorDni.setText("Ya se encuentra en uso");
					lblMsjErrorDni.setVisible(true);
					validos = false;
				}
				
				
				Query query2 = appMain.getSession().createQuery("from Usuario where user= :id");
				query2.setString("id", userNuevo.getUser());
				Usuario usuarioUSER = (Usuario) query2.uniqueResult();
				 
				if (usuarioUSER != null) { //si la consulta me trajo un usuario, significa q metio un user de un usuario q ya existe en la BD
					userUsado = true;
				} 
					
				if (userUsado) {   
					txtFieldNombreUser.setUnFocusColor(Color.RED);
					lblMsjErrorUser.setText("Ya se encuentra en uso");
					lblMsjErrorUser.setVisible(true);
					validos = false;
				}
				
				
				appMain.getSession().close();
			
			} else {  //si hubo algun blanco entonces avisa en pantalla
	 			validos = false;
	 		}
			 
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		} 
		
		 return validos;
 	}

 	 
 	 
 	private boolean noHayBlancosEnTxtFields(Usuario userModif) {
		boolean validos = true;
		
		if (userModif.getNombre().substring(0, 1).equals(" ")) {
			 txtFieldNombre.setUnFocusColor(Color.RED);
			 lblMsjErrorNombre.setText("No se admite espacios antes del Nombre");
			 lblMsjErrorNombre.setVisible(true);
			 validos = false;
		 }
	 
		 if (userModif.getApellido().substring(0, 1).equals(" ")) {
			 txtFieldApellido.setUnFocusColor(Color.RED);
			 lblMsjErrorApellido.setText("No se admite espacios antes del Apellido");
			 lblMsjErrorApellido.setVisible(true);
			 validos = false;
		 }
		 
		 String dni = String.valueOf(userModif.getPkDniUsuario());
		 
		 if ((dni.length() < 8) || (dni.length() > 8)) { //1ero verifico q tenga 8 digitos 
			txtFieldDNI.setUnFocusColor(Color.RED);
			lblMsjErrorDni.setText("El DNI debe contener 8 digitos");
			lblMsjErrorDni.setVisible(true);
			validos = false;
		 }
		 
		 if (userModif.getUser().substring(0, 1).equals(" ")) {
			txtFieldNombreUser.setUnFocusColor(Color.RED);
			lblMsjErrorUser.setText("No se admite espacios antes del Nombre de Usuario");
			lblMsjErrorUser.setVisible(true);
			validos = false;
		 }
		 
		 if (userModif.getPassword().substring(0, 1).equals(" ")) {
			txtFieldPass.setUnFocusColor(Color.RED);
			lblMsjErrorContrasena.setText("No se admite espacios antes de la Contrasena");
			lblMsjErrorContrasena.setVisible(true);
			validos = false;
		 }
		
		return validos;
	} 

 	
 	
 	/*se encarga de crear la relacion entre el usuario y el sector q se le asigno */
    private void crearRelacionUserSector(Usuario userIN) {
    	//el this.getNombreSector() toma valor cuando se llama al metodo nuevoUser(), q lo llamo al principio del modificar o del alta
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query1 = appMain.getSession().createQuery("from Sector where nombreSector= :id");  //1ero obtengo el sector de la bd, para saber su ID
			query1.setString("id", this.getNombreSectorDelComboBox());
			Sector sector = (Sector) query1.uniqueResult();
			
			UsuarioSectorId userSecID = new UsuarioSectorId(userIN.getPkDniUsuario(), sector.getIdSector()); 
			String nom = userIN.getNombre();
			String ape = userIN.getApellido();
			UsuarioSector userSector = new UsuarioSector(userSecID, sector, userIN, nom, ape);
			
			appMain.getSession().save(userSector);
			
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
    
    
	
	@FXML
    void guardarModifUser(ActionEvent event) {
		//antes de guardar, hacer todas las vereficaciones q hago con el alta
    	pintarUnFocusTxtFields();
		boolean cerrarModif = false;
    	try {
			Usuario userModif = nuevoUser();  //creo un user con todos los datos de los campos para luego setearle al user 
												//de la BD y realizar el guardado de la actualizacion
			
			if (userModif != null) {
				
				if (noHayBlancosEnTxtFields(userModif)) { //sino hay blancos en los txtFields
					
					obtenerUserBDPorDNI();
					
					if (!(userYdniEnUso(userModif))) { 
		//si el dni y nombre user (en el caso de q los alla cambiado) no estan en uso, entonces realiza la actualizacion de datos
				
						userBD.setAdmin(userModif.getAdmin());
						userBD.setApellido(userModif.getApellido());
						userBD.setEstadoUsuario(userModif.getEstadoUsuario());
						userBD.setNombre(userModif.getNombre());
						userBD.setPassword(userModif.getPassword());
						
						if (this.getDniAux() != userBD.getPkDniUsuario()) { //significa q cambio el dni(clave primaria)
							
							borrarReferenciasYcrearNueva();
							
						} else { //sino significa q no modifico el dni, xq lo q se puede implementar la actualizacion de hibernate
							
							actualizarUserBD(userBD);
							
							//luego verificar si se esta relacionado con algun sector y si cambio
							//nombre o apellido, tambien actualizar en la tabla relacion Usuario-Sector
							actualizarTablaRelacionUserSector();
							
						}
						
						if (!(checkBoxAdmin.isSelected())) { //basicamente, si entra es xq dejo de ser admin para otorgarsele un sector
							//o lo cambio de sector
							
							crearOmodificarRelacionUserSector(userBD);
							
						}
						
						cerrarModif = true;
						
					}
					
				}	 
				
				if (cerrarModif) {
		    		limpiarCampos();
		        	ControladorICsd_Principal.controllerPantAdmin.actualizarTablaUsers();
		        	ControladorICsd_Principal.controllerTablaUser.resetearComboBoxFiltroSector();
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	
	private void actualizarUserBD(Usuario userBD) {
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			appMain.getSession().update(userBD);
			
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}

	
	
	private void obtenerUserBDPorDNI() {
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query1 = appMain.getSession().createQuery("from Usuario where pkDniUsuario= :id");
			query1.setInteger("id", this.getDniAux());
			userBD = (Usuario) query1.uniqueResult();
			
			appMain.getSession().close();
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}

	
	
	/* ademas de verificar si el dni o user (si fueron cambiados) estan ocupados x otra persona
   * 	tambien evita, q compare los datos dni y user (si no fueron cambiados) con la misma persona q esta guardada en la bd
   * 	es decir, q se compare a si mismo  
   */
    private boolean userYdniEnUso(Usuario userModif) {
    	boolean enUso = false;
    	boolean dniUsado = false;
    	boolean userUsado = false;
    	Transaction tx = null;
    	
    	try {
    		
    		AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
    		
        	if (!(this.getDniAux() == userModif.getPkDniUsuario())) { //si entra es xq cambio el dni del user
        		
				Query query1 = appMain.getSession().createQuery("from Usuario where pkDniUsuario= :id");
				query1.setInteger("id", userModif.getPkDniUsuario());
				Usuario usuarioDNI = (Usuario) query1.uniqueResult();
				
				if (usuarioDNI != null) { //si la consulta me trajo un usuario, significa q metio un dni de un usuario q ya existe en la BD
					dniUsado = true;
				}
				
				if (dniUsado) {
					txtFieldDNI.setUnFocusColor(Color.RED);
					lblMsjErrorDni.setText("Ya se encuentra en uso");
					lblMsjErrorDni.setVisible(true);
					enUso = true;
				} else {  //si entra es xq metio un nuevo dni
        			//entonces le seteo esos atributos al user de la bd, q saque previamente para la actualizacion
					userBD.setPkDniUsuario(userModif.getPkDniUsuario());
				}  
        		
    		}
        	
        	if (!(this.getNomUserAux().equals(userModif.getUser()))) { //si entra es xq cambio el nombre de user
				
        		Query query2 = appMain.getSession().createQuery("from Usuario where user= :id");
				query2.setString("id", userModif.getUser());
				Usuario usuarioUSER = (Usuario) query2.uniqueResult();
				 
				if (usuarioUSER != null) { //si la consulta me trajo un usuario, significa q metio un user de un usuario q ya existe en la BD
					userUsado = true;
				} 
					
				if (userUsado) {   
					txtFieldNombreUser.setUnFocusColor(Color.RED);
					lblMsjErrorUser.setText("Ya se encuentra en uso");
					lblMsjErrorUser.setVisible(true);
					enUso = true;
				} else {//si entra es xq metio un nuevo nombre de user
					//entonces le seteo esos atributos al user de la bd, q saque previamente (en el metodo guardarModifUser)  
					//para la actualizacion
					userBD.setUser(userModif.getUser());
				}                          
        		
			} 
        	
        	appMain.getSession().close();
        	
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
    	
		return enUso;
	}
	
    
    
    /* la actualizacion de hibernate, no permite modificar la clave primaria (en este caso el dni)
     *  x lo q es necesario, borrar la referencia de ese user de las tablas Usuario y UsuarioSector
     *   y crearlo nuevamente   
     */
       private void borrarReferenciasYcrearNueva() {
    	   Transaction tx = null;
    	   try {
   		//1ero borrar la referencia de la tabla relacion UsuarioSector 
    		   
	    		AppMain appMain = AppMain.getSingletonSession();
	   			tx = appMain.getSession().beginTransaction();
   			
				String hqlDelete1 = "delete from UsuarioSector where usuario= :id";
				
				//this.getDniAux, es la referencia q me queda del verdadero dni q tenia el user antes de la modificacion
				int updatedEntities1 = appMain.getSession().createQuery( hqlDelete1 )
				        .setInteger("id", this.getDniAux())	
				        .executeUpdate();
				
				
				String hqlDelete2 = "delete from Usuario where pkDniUsuario= :id";
				
				//this.getDniAux, es la referencia q me queda del verdadero dni q tenia el user antes de la modificacion
				int updatedEntities2 = appMain.getSession().createQuery( hqlDelete2 )
				        .setInteger("id", this.getDniAux())	
				        .executeUpdate();
       		
       		//finalmente guardo el usuario con su dni modificado
       		//y la tabla relacion se lleva a cabo luego de haberse llamado a este metodo
   			
       		appMain.getSession().save(userBD);
       		
       		tx.commit();
       		
   		} catch (Exception e) {
   			e.printStackTrace();
   		//rolling back to save the test data
			tx.rollback();
			e.getMessage();
   		}
   	}
       
       
    private void actualizarTablaRelacionUserSector() {
       	Transaction tx = null;
   		try {
   			AppMain appMain = AppMain.getSingletonSession();
   			tx = appMain.getSession().beginTransaction();
   			
   			String hqlUpdate = "update UsuarioSector "
   					+ "set nombreUsuario = :nombre, apellidoUsuario = :apellido "
					+ "where usuario = :dni";
			
			int updatedEntities = appMain.getSession().createQuery( hqlUpdate )
			        .setString( "nombre", userBD.getNombre() )
			        .setString( "apellido", userBD.getApellido() )
			        .setInteger( "dni", userBD.getPkDniUsuario() )
			        .executeUpdate();
   			
			tx.commit();
   			
   		} catch (Exception e) {
   			e.printStackTrace();
   		//rolling back to save the test data
			tx.rollback();
			e.getMessage();
   		}
   	}   
	
    
    /*se encarga de crear relacion usuario-sector cuando el user deja de ser admin
     * y se le asigna un sector.
     *  Y en el caso de q se le cambie de sector al user, realiza la actualizacion
     */
    private void crearOmodificarRelacionUserSector(Usuario userBD) {
    	boolean encontro = false;
    	UsuarioSector usuarioSector = null;
    	Sector sector = null;
		try {
			
			usuarioSector = obtenerUserSectorPorDNI();
			
			if (usuarioSector != null) {
				
				sector = obtenerSectorPorNombre();
				
				if (!(usuarioSector.getSector().getIdSector() == sector.getIdSector())) {
					
					//si entra significa q se le cambio de sector al user
					
					borrarUsuarioSector(usuarioSector);
					
					crearRelacionUserSector(userBD);
					
					encontro = true;
					
				} else {
					
					//significa q no hizo cambios, entonces no hace nada
					encontro = true;
					
				}
			} 
   				
			if (!(encontro)) {
				
				/** VARIANTES:
				 * 1- significa q el user era admin y ahora se le asigno un sector
				 * 2- ya tenia un sector asignado (no se modif) pero como cambio su dni
				 * 		anteriormente se borro su referencia de tabla userSector, x lo q
				 * 		es necesaria crearlo de nuevo vinculada al nuevo dni
				 */
		
				crearRelacionUserSector(userBD);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
    
    
    
	private void borrarUsuarioSector(UsuarioSector usuarioSector) {
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
   			tx = appMain.getSession().beginTransaction();
   			
			appMain.getSession().delete(usuarioSector);
			
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}

	
	
	private Sector obtenerSectorPorNombre() {
		Transaction tx = null;
		Sector sector = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
   			tx = appMain.getSession().beginTransaction();
   			
			Query query2 = appMain.getSession().createQuery("from Sector where nombreSector= :nombre");
			query2.setString("nombre", this.getNombreSectorDelComboBox());	//el this.getNombreSector() toma valor cuando se llama al metodo nuevoUser(), q lo llamo al principio del modificar
																				//el cual le asigna el nombre del sector q le selecciono del combo	
			sector = (Sector) query2.uniqueResult();
			
			appMain.getSession().close();
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return sector;
	}

	private UsuarioSector obtenerUserSectorPorDNI() {
		Transaction tx = null;
		UsuarioSector usuarioSector = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
   			tx = appMain.getSession().beginTransaction();
   			
   			Query query1 = appMain.getSession().createQuery("from UsuarioSector where usuario= :id");
			query1.setInteger("id", userBD.getPkDniUsuario());
			usuarioSector = (UsuarioSector) query1.uniqueResult();
			
			appMain.getSession().close();
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		
		return usuarioSector;
	}

	
	
	@FXML
    void habilitarComboSector(ActionEvent event) {
    	if (checkBoxAdmin.isSelected()) {
			cBoxUserASector.setDisable(true);
		} else {
			cBoxUserASector.setDisable(false);
		}
    }
	
	
	
	@FXML
    void desactivarRadioSuspendido(ActionEvent event) {
		radioBtnActivo.setSelectedColor(Color.web("#0098cd"));
		radioBtnActivo.setSelected(true);
		radioBtnSuspendido.setSelected(false);
    }

    @FXML
    void desactivarRadioActivo(ActionEvent event) {
    	radioBtnSuspendido.setSelectedColor(Color.web("#0098cd"));
    	radioBtnSuspendido.setSelected(true);
    	radioBtnActivo.setSelected(false);
    }

	/* limita el ingreso de caracteres al txtFieldDNI a 8
     * en el caso de q ponga menos, se avisa con label de error
     */
	private void limitarIngresoCaracter(KeyEvent e) {
		if (txtFieldDNI.getText().length() == 8) {
		     e.consume();
		}
	}
    
    
    
	/* setea x defecto el radioBtn "Activo", para el caso de "Alta"
	 * si viene por "Modificar", entonces no hace nada
	 */
	private void setearRadioActivo() {
		if (lblAltaUser.isVisible()) { //si entra entonces seteo el radio Activos
			radioBtnActivo.setSelected(true);
			radioBtnActivo.setSelectedColor(Color.web("#0098cd"));
		}
	}
    
    
    
	private void agregarToolTipLabel() {
		lblSuperUser.setTooltip(new Tooltip("Usuario con completo acceso a todas las funciones"));
	}
    
   
    public void llenarComboSector() {
    	Transaction tx = null;
		try {
			ObservableList<String> itemsCombo = FXCollections.observableArrayList();
			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sector where Estado_Sector= \'Activo\'");
			List<Sector> listaSectores = query.list();
			
			for (Sector sector : listaSectores) {
				itemsCombo.add(sector.getNombreSector());
			}
			
			appMain.getSession().close();
			
			cBoxUserASector.setItems(itemsCombo);
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	 }
    
    
    
  //se encarga de llenar los componentes, con los datos del user q selecciono previamente en la tabla
    //para llevar a cabo la modificacion de sus datos
	public void llenarCampos(Integer dniUserSeleccionado) {
		Usuario userModif = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			//busco y obtengo user de la bd, mediante su PK (q es el dni)
			Query query = appMain.getSession().createQuery("from Usuario where pkDniUsuario= :id");
			query.setInteger("id", dniUserSeleccionado);
			userModif = (Usuario) query.uniqueResult();
			
			appMain.getSession().close();
			
			txtFieldNombre.setText(userModif.getNombre());
	    	txtFieldApellido.setText(userModif.getApellido());
	    	txtFieldDNI.setText(String.valueOf(userModif.getPkDniUsuario()));
	    	txtFieldNombreUser.setText(userModif.getUser());
	    	txtFieldPass.setText(userModif.getPassword());
	    	
	    	llenarComboSector();
	    	
	    	if (userModif.getAdmin().equals("Si")) {
				checkBoxAdmin.setSelected(true);
				cBoxUserASector.setDisable(true);
			} else { //sino entra entonces, tengo q seleccionar el sector al q pertenece en el combobox
				buscarSectorYsetearCombo(userModif);
			}
	    	
	    	if (userModif.getEstadoUsuario().equals("Activo")) {
				radioBtnActivo.setSelected(true);
				radioBtnActivo.setSelectedColor(Color.web("#0098cd"));
				
			} else {
				radioBtnActivo.setSelected(false);
				radioBtnSuspendido.setSelected(true);
				radioBtnSuspendido.setSelectedColor(Color.web("#0098cd"));
			}
	    	
	    	/* y luego de llenar los campos, guardo registro del nombre de usuario y dni 
	    	 * para luego usar, en la verificacion de datos q se lleva a cabo cuando se quiere
	    	 * guardar la modificacion*/
	    	this.setNomUserAux(userModif.getUser());
	    	this.setDniAux(userModif.getPkDniUsuario());
	    	
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
	
	
	
	//busca el sector del usuario y lo setea en el combo
	private void buscarSectorYsetearCombo(Usuario userModif) {
		Integer c = 0;
		boolean encontro = false;
		Transaction tx = null;
		Sector sec = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			/** busco en tabla usuarioSector el dni del user, luego veo a q idSector esta relacionado
			 * 		y con ese idSector, busco en la tabla sector, el nombre del sector
			 * 		y lo retorno para luego buscar ese nombre en el comboBox y setearlo
			 * 	LOS  "ON" en el "INNER JOIN" ====> no lo toma HQL, en vez de eso, se utiliza el "AND" en el "WHERE"
			 */
			Query query = appMain.getSession().createQuery("select new Sector(s.nombreSector) "
					+ "from UsuarioSector uS "
					+ "inner join uS.usuario u "
					+ "inner join uS.sector s "
					+ "where uS.usuario= :id "
					+ "and uS.usuario = u.pkDniUsuario "
					+ "and uS.sector = s.idSector");
			query.setInteger("id", userModif.getPkDniUsuario());
			sec = (Sector) query.uniqueResult();
			
			appMain.getSession().close();
			
			while ((c < cBoxUserASector.getItems().size()) && (!(encontro))) { //recorro el comboBox sector
				if (cBoxUserASector.getItems().get(c).equals(sec.getNombreSector())) { //busco el sector al q pertenece el user, y si esta, seteo el combo en esa direccion
					cBoxUserASector.getSelectionModel().select(c);
					encontro = true;
				} else {
					c = c + 1;
				}
			}  //fin recorrer el comboBox
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
	
	
	
	public void limpiarCampos() {
		txtFieldNombre.clear();
		txtFieldApellido.clear();
		txtFieldDNI.clear();
		txtFieldNombreUser.clear();
		txtFieldPass.clear();
		cBoxUserASector.getSelectionModel().select(-1);
		cBoxUserASector.setDisable(false);
		checkBoxAdmin.setSelected(false);
		radioBtnActivo.setSelected(true);
		radioBtnSuspendido.setSelected(false);
		pintarUnFocusTxtFields();
	}
	
	
	/*pinto los unfocus de los txtField a su color x defecto (negro)
     *  ya q si se ingresa datos erroneos los pinta de rojo
     *  Entonces luego de darle a "guardar", resetea colores y visibilidad
     */
    private void pintarUnFocusTxtFields() {
		txtFieldNombre.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldApellido.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldDNI.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldNombreUser.setUnFocusColor(Color.web("#4d4d4d"));
		txtFieldPass.setUnFocusColor(Color.web("#4d4d4d"));
		cBoxUserASector.setUnFocusColor(Color.web("#4d4d4d"));
		setearVisibilidadLblMejError();
	}
    
    
    
    private void setearVisibilidadLblMejError() {
		lblMsjErrorNombre.setVisible(false);
		lblMsjErrorApellido.setVisible(false);
		lblMsjErrorDni.setVisible(false);
		lblMsjErrorUser.setVisible(false);
		lblMsjErrorContrasena.setVisible(false);
	}
    
}
