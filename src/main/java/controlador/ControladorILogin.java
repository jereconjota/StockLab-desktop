package controlador;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import hibernate.util.CRUD;
import hibernate.util.HibernateUtil;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.input.KeyEvent;

import main.AppMain;
import modelo.Usuario;

public class ControladorILogin {
	
	@FXML
    private JFXButton btnSalir;

    @FXML
    private Label lblError1;

    @FXML
    private JFXButton btnIngresarLogin;

    @FXML
    private ImageView logo;

    @FXML
    private JFXPasswordField passFieldLogin;

    @FXML
    private Label lblError2;

    @FXML
    private JFXTextField txtFieldUserLogin;
    
    private Stage primaryStage;
    public static ControladorICsd_Principal controllerPpal;
	public static BorderPane ppal;
    
    /**************************** CONSTRUCTOR **********************************/
    
	public ControladorILogin() {
		
	}
    
    
    /**************************** GET - SET **********************************/
    
	public Stage getPrimaryStage() {
		return primaryStage;
	}


	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}


	/********************************** METODOS ***********************************/
    
	
	@FXML
    private void initialize() {
    	cargarLogo();
    	btnSalir.requestFocus();
    }
	
	
	private void cargarLogo() {
    	FileInputStream input;
		try {
			input = new FileInputStream("img/iconoStockLab.png");
			Image image = new Image(input);
	    	logo.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	

    @FXML
    void ingresarEnter(KeyEvent event) {
    	if (event.getCode().equals(KeyCode.ENTER)){
    		verificarUser();
            }
    }


	@FXML
    void verificarUser() {
		Transaction tx = null;
    	try {
    		Usuario usuarioBD = null;
    		
    		String user = txtFieldUserLogin.getText();
        	String pass = passFieldLogin.getText();
    		//obtener la tabla de usuarios que estan en la BD
        	
        	AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
        	Query query = appMain.getSession().createQuery("from Usuario where User= :usuario and Password= :password");
    		query.setParameter("usuario", user);
    		query.setParameter("password", pass);
    		usuarioBD = (Usuario) query.uniqueResult();
    		
    		appMain.getSession().close();
    		
    		if (usuarioBD == null) {
    			lblError1.setVisible(true);
    			lblError2.setVisible(true);
			} else {
				//mandamos el usuario y la dirIP al Controlador Principal, asi sabemos quien esta usando el programa y en q sucursal
				
//				cambiarDeEscena(usuarioBD, CRUD.LeeXML().getUrl());
				cambiarDeEscena(usuarioBD);
				lblError1.setVisible(false);
				lblError2.setVisible(false);
				
			}
    		    		
//    		appMain.getSession().close();
    		
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
    }
    
	
	
//	private void cambiarDeEscena(Usuario usr, String dirURL) {
	private void cambiarDeEscena(Usuario usr) {
		try {
			this.getPrimaryStage().close();
	    	
	    	Stage stage = new Stage();
	    	
	    	FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AppMain.class.getResource("/vista/ICsd_principal.fxml"));
		
			ppal = loader.load();		
			controllerPpal = loader.getController();
			
		//esto no estoy del todo seguro si lo vamos a necesitar de nuevo, la referencia al primaryStage.
		//asi q x las dudas lo dejamos seteado dentro del controlador ppal y no perder referencia aunq no
		//lo usemos (o tal vez si)		
			
			controllerPpal.setPrimaryStage(stage);
			
			//Mandamos el usuario q ingreso al software
			controllerPpal.setUser(usr);
			
			//Mandamos la dir IP para saber en q sucursal esta dicho usuario
//			controllerPpal.setDirUrl(dirURL);
			
			
			Scene escena = new Scene(ppal);
			stage.setScene(escena);
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>(){ //controla el evento q se genera cuando se cierra la ventana desde la X
	            @Override public void handle(WindowEvent event) {
	            	
	            Alert alert = new Alert(AlertType.CONFIRMATION);
            	alert.setTitle("CERRAR STOCKLAB");
        		alert.setHeaderText("Desea salir de la aplicacion?");

	    		Optional<ButtonType> result = alert.showAndWait();
	    		if (result.get() == ButtonType.OK){
	    			Runtime.getRuntime().gc();  // GARBAGE COLLECTOR
	    				            	
//	            	if (usuarioLogueado != null) {
//	            		usuarioLogueado.setEstado("Desconectado");
//	            		ControladorILogin.opCRUD.actualizarObj(usuarioLogueado);
//	        		}
	    			
	    			//cerramos la sessionFactory de hibernate
	            	HibernateUtil.getSessionFactory().close();
	            	
	            	Platform.exit();    
	            	System.exit(0);
	            } else if (result.get() == ButtonType.CANCEL){
	            	event.consume();
	            }
	    		}
	        });
			
			stage.show();
			setearTitulo(stage, "STOCK-LAB // "+usr.getNombre()+" "+usr.getApellido());
			
			//x el momento desactivamos (xq quieren q el de administracion tambien de baja insumos)
			//es decir, tendra habilitado el menu "Stock"
//			controllerPpal.verificarUser(usr);  
			
			
			//Abrimos la ventana maximizada y desactivamos cambiar el tamanio.
			stage.setMaximized(true);
		//	stage.setResizable(false);
			
			//leo el tamanio de pantalla 
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			
			stage.setMinWidth(screenSize.getWidth());
			stage.setMinHeight(777);
		//	stage.setMinHeight(screenSize.getHeight());
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	public void setearTitulo(Stage stage, String titulo) {
		stage.setTitle(titulo);
	}
	

	@FXML
    void CerrarApp() {
    	Platform.exit();    
    	System.exit(0);
    	
    	//cerramos la sessionFactory de hibernate
    	HibernateUtil.getSessionFactory().close();
    }
	
	
    @FXML
    void cerrarEnter(KeyEvent event){
    	if (event.getCode().equals(KeyCode.ENTER)){
    		CerrarApp();//
        }
    }

}
