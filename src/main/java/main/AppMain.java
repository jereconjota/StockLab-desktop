package main;

import org.hibernate.Session;

import controlador.ControladorILogin;
import hibernate.util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

	/*****************************************************************************************/
	/*****************************************************************************************/
	/*************                                                   *************************/
	/*************  CUANDO SE CIERRE LA PANTALLA ACORDARSE DE PONER  *************************/
	/*************                                                   *************************/
 	/*************    HibernateUtil.getSessionFactory().close()      *************************/
	/*************                                                   *************************/
	/*****************************************************************************************/
	/*****************************************************************************************/


public class AppMain extends Application {
	
	private Stage primaryStage;
	
	/*****************************  Para patron Singleton  ************************************/
	
	private static Session session;
	private static AppMain appMain;
	
	// El constructor es privado, no permite que se genere un constructor por defecto.
	
	public AppMain() {
		AppMain.session = HibernateUtil.getSessionFactory().getCurrentSession();
//		AppMain.session = HibernateUtil.getSessionFactory().openSession();
	}
	
	
	public static AppMain getSingletonSession() {
        if ((appMain == null) || (!session.isConnected())){
            appMain = new AppMain();
        }
        return appMain;
    }
	

	
	/******************************************************************************************/

	/////////////////////  GET y SET  ////////////////////////////////////////
	
	
	public Session getSession() {
		return session;
	}


	public void setSession(Session session) {
		AppMain.session = session;
	}
	
	
	/////////////////////////// METODOS ///////////////////////////////
	
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("LOGIN STOCK-LAB");
		iniciarLogin();
	}
	
	
	private void iniciarLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AppMain.class.getResource("/vista/ILogin.fxml"));
			
			AnchorPane login = loader.load();
			ControladorILogin controllerLogin = loader.getController();
				 
			controllerLogin.setPrimaryStage(primaryStage);  //para luego de q haga el login, la escena cambie a pantalla ppal
			Scene escena = new Scene(login);
			primaryStage.setScene(escena);
			primaryStage.initStyle(StageStyle.UNDECORATED); //para q no tenga marco la ventana
			primaryStage.show();
		//	primaryStage.setResizable(false);
		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	//Metodo que se ejecuta SIEMPRE al cerrarse la aplicacion.
	//Si detecta que se serro inesperadamente (usuario aun logueado) antes de cerrar lo "desloguea"
    static{ 
        Runtime.getRuntime().addShutdownHook(new Thread() { 
	        @Override 
	        public void run() { 
	        	closeWindow(); 
	        }
	
			private void closeWindow() {
				//si el usuario el NULL, quiere decir que aun no se habia logueado nadie al cerrarse la app (ventana login)
				//Si el estado del usuario es "Conectado", quiere decir que se trata de un cierre inesperado
				try {
					Runtime.getRuntime().gc();  // GARBAGE COLLECTOR
	//					if ((ControladorICsd_Principal.getUser()!=null) && (ControladorICsd_Principal.getUser().getEstado().equals("Conectado"))) {
	//						
	//						ControladorILogin.controllerPpal.desconectarUsuario();
	//						System.out.println("Usuario: "+ControladorICsd_Principal.getUser().getUser()+" - Sesion cerrada");
	//					}
					
					//cerramos la sessionFactory de hibernate
			    	HibernateUtil.getSessionFactory().close();
				} catch(Exception e) {
					e.getMessage();
				}
			} 
        }); 
    }
	    

	public static void main(String[] args) {
		launch(args);
	}
	
}
