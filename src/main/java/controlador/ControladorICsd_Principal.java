package controlador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Transaction;
import hibernate.util.HibernateUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.AppMain;
import modelo.Sector;
import modelo.Usuario;
import modelo.UsuarioSector;
import javafx.scene.layout.AnchorPane;

public class ControladorICsd_Principal {
	
    @FXML
    private MenuBar menuBarPrincipal;

	@FXML
    private javafx.scene.control.MenuItem botonCerrar;
	
    @FXML
    private MenuItem botonCerrarSesion;

	@FXML
    private Menu menuGestion;
	
	@FXML
	private MenuItem gestionConfiguracion;

    @FXML
    private Menu menuAyuda;
    
    @FXML
    private MenuItem ayudaManualDeUsuario;
    
    @FXML
	private MenuItem ayudaAcercaDe;

    @FXML
    private Menu menuStock;
    
	@FXML
	private MenuItem stockActualizarStock;
	
	@FXML
	private MenuItem stockActualizarStock1;
	
    @FXML
    private MenuItem stockVerMovimientos;
    
	@FXML
    private MenuItem stockVerPDP;
	
	@FXML
    private MenuItem stockOrdenDeCompra;

	@FXML
	private Menu menuFacturacion;
	
	@FXML
	private MenuItem facturacionIngresarFactura;

	@FXML
	private MenuItem facturacionVerFacturas;

	@FXML
    private MenuItem facturacionVerRemitos;

    @FXML
    private MenuItem facturacionIngresarRemito;

    @FXML
    private ImageView logo;

    @FXML
    private BorderPane borderPaneContenedor;

	@FXML
    private BorderPane borderPanePpal;  //para tener referencia a la pantallaPpal, y asi poder setearles sus regiones a gusto

    @FXML
    private MenuItem facturacionModificarFactura;;

    @FXML
    private MenuItem facturacionModificarRemito;
    
    @FXML
    private MenuItem facturacionVerMovimientos;
    
    @FXML
    private MenuItem stockVerOrdenesDeCompra;
    
    private Stage primaryStage;
    public static Usuario user;
    private String sectorDeUsuario;
    public static String nombrePantalla;

	public static ControladorIVistaMovimientos controllerVistaMovimientos;
	public static BorderPane iVistaMovimientos;
	public static ControladorITablaMovimientos controllerTablaMovimientos;
	public static AnchorPane iTablaMovimientos;
	
	public static ControladorIVistaInsumos controllerVistaInsumos;
	public static BorderPane iVistaInsumos;
	public static ControladorITablaDetalleInsumo controllerTablaDetalleInsumo;
	public static AnchorPane iTablaDetalleInsumo;
	
	public static ControladorIVistaPDP controllerVistaPDP;
	public static BorderPane iVistaPDP;
	public static ControladorITablaPDP controllerTablaPDP;
	private AnchorPane iTablaPDP;
	
	public static ControladorIVistaVencidos controllerVistaVencidos;
	public static BorderPane iVistaVencidos;
	public static ControladorITablaVencidos controllerTablaVencidos;
	public static AnchorPane iTablaVencidos;
	
	public static ControladorIPantallaAdmin controllerPantAdmin;
	private BorderPane vistaAdmin;
	
	public static ControladorIAltaUsuario controllerAltaUser;
	public static AnchorPane iAltaUser;
	public static ControladorITablaAbmUsuarios controllerTablaUser;
	public static AnchorPane iTablaUser;
	
	public static ControladorIAltaSector controllerAltaSector;
	public static AnchorPane iAltaSector;
	public static ControladorITablaAbmSectores controllerTablaSector;
	public static AnchorPane iTablaSector;
	
	public static ControladorIAltaCategoria controllerAltaCategoria;
	public static AnchorPane iAltaCategoria;
	public static ControladorITablaAbmCategorias controllerTablaCategoria;
	public static AnchorPane iTablaCategoria;
	
	public static ControladorIAltaInsumo controllerAltaInsumo;
	public static AnchorPane iAltaInsumo;
	public static ControladorITablaAbmInsumos controllerTablaInsumo;
	public static AnchorPane iTablaInsumo;
	
	public static ControladorITablaInsumosGeneral controllerTablaInsumoGral;
	public static AnchorPane iTablaInsumoGral;
	
	public static ControladorITablaInsumosGeneralStock controllerTablaInsumoGralStock;
	public static AnchorPane iTablaInsumoGralStock;
	
	public static ControladorIAltaProveedor controllerAltaProveedor;
	public static AnchorPane iAltaProveedor;
	public static ControladorITablaAbmProveedor controllerTablaProveedor;
	public static AnchorPane iTablaProveedor;
	
	public static ControladorIActualizarStockInsumo controllerActualizarStockInsumo;
	public static BorderPane iActualizarStockInsumo;
	
	public static ControladorIIngresoFactura controllerIIngresoFactura;
	public static BorderPane iIngresoFactura;
	
	public static ControladorIModificarFactura controllerModificarFactura;
	public static BorderPane iModificarFactura;
	
	public static ControladorIFacturas controllerIFacturas;
	public static BorderPane iFacturas;
	
	public static ControladorIIngresarRemito controllerIIngresarRemito;
	public static BorderPane iIngresarRemito;
	
	public static ControladorIModificarRemito controllerModificarRemito;
	public static BorderPane iModificarRemito;
	
	public static ControladorIRemitos controllerIRemitos;
	public static BorderPane iRemitos;
	
	public static ControladorIVistaOrdenDeCompra controllerIVistaOrdenDeCompra;
	public static BorderPane iVistaOrdenDeCompra;
	
	public static ControladorIOrdenesDeCompra controllerIOrdenesDeCompra;
	public static BorderPane iOrdenesDeCompra;
	
	public Stage stageProgress;
	
	public static ControladorIVistaInsumosStock controllerVistaInsumosStock;
	public static BorderPane iVistaInsumosStock;
	
	public static ControladorITablaPDPborrador controllerTablaPDPborrador;
	private AnchorPane iTablaPDPborrador;
	
	
//	public static String dirUrl;
	
	/************************************  CONSTRUCTOR  ************************************/
	
	public ControladorICsd_Principal() {
		
	}
	
	
	/**************************** GET - SET **********************************/	
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	
	public static Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		ControladorICsd_Principal.user = user;
	}
	
	
	public String getSectorDeUsuario() {
		return sectorDeUsuario;
	}
	public void setSectorDeUsuario(String sectorDeUsuario) {
		this.sectorDeUsuario = sectorDeUsuario;
	}

	
	public MenuBar getMenuBarPrincipal() {
		return menuBarPrincipal;
	}
	
	
	public BorderPane getBorderPanePpal() {
		return borderPanePpal;
	}
	
	
//	public static String getDirUrl() {
//		return dirUrl;
//	}
//	public void setDirUrl(String url) {
//		this.dirUrl = url;
//	}


	/********************************** METODOS ***********************************/
	
	
	@FXML
    private void initialize() {
		cargarLogo();
		cargarControladores();
		//Si el usuario que se loguea no es un admin, este campo despues se completa con su sector
    	this.setSectorDeUsuario("");
    	
//    	suspenderVencidos();
    }

	
	private void cargarLogo() {
		FileInputStream input;
		try {
			input = new FileInputStream("img/diagnosdahinten.png");
			Image image = new Image(input);
	    	logo.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	//Suspende insumos que pasaron su fecha de vencimiento y no tienen stock
//    private void suspenderVencidos() {
//    	Date hoy = java.sql.Date.valueOf(LocalDate.now());
//
//    	List<Object> listaObj = ControladorILogin.opCRUD.SuspenderInsumosVencidos(hoy);
//    	List<Insumo> listaInsumos = ControladorILogin.opCRUD.convertLista(listaObj, new Insumo());
//    	System.out.println(listaInsumos);
//    	if (!listaInsumos.isEmpty()) {
//    		for (Insumo insumo : listaInsumos) {
//    			insumo.setEstadoInsumo("Suspendido");
//    			ControladorILogin.opCRUD.actualizarObj(insumo);
//    		}
//		}
//    }
	
	
	/******************************** Carga de controladores **************************/
	

	private void cargarControladores() {
		
	/********************  PANTALLA GESTION (antes se llamaba administrar)  ********************/	
		
		FXMLLoader loaderAdmin = new FXMLLoader();
		loaderAdmin.setLocation(AppMain.class.getResource("/vista/IPantallaAdministrar.fxml"));
		vistaAdmin = null;
		controllerPantAdmin = null;
		
		/********************  USER   ********************/	
		
		FXMLLoader loaderUser = new FXMLLoader();
		loaderUser.setLocation(AppMain.class.getResource("/vista/IAltaUsuario.fxml"));
		iAltaUser = null;
		controllerAltaUser = null;

		FXMLLoader loaderTablaUser = new FXMLLoader();
		loaderTablaUser.setLocation(AppMain.class.getResource("/vista/ITablaAbmUsuarios.fxml"));
		iTablaUser = null;
		controllerTablaUser = null;
		
		/********************  SECTOR   ********************/
		
		FXMLLoader loaderSector = new FXMLLoader();
		loaderSector.setLocation(AppMain.class.getResource("/vista/IAltaSector.fxml"));
		iAltaSector = null;
		controllerAltaSector = null;

		FXMLLoader loaderTablaSector = new FXMLLoader();
		loaderTablaSector.setLocation(AppMain.class.getResource("/vista/ITablaAbmSectores.fxml"));
		iTablaSector = null;
		controllerTablaSector = null;
		
		/********************  CATEGORIA   ********************/
		
		FXMLLoader loaderCategoria = new FXMLLoader();
		loaderCategoria.setLocation(AppMain.class.getResource("/vista/IAltaCategoria.fxml"));
		iAltaCategoria = null;
		controllerAltaCategoria = null;

		FXMLLoader loaderTablaCategoria = new FXMLLoader();
		loaderTablaCategoria.setLocation(AppMain.class.getResource("/vista/ITablaAbmCategorias.fxml"));
		iTablaCategoria = null;
		controllerTablaCategoria = null;
		
		/********************  INSUMOS   ********************/
		
		FXMLLoader loaderInsumo = new FXMLLoader();
		loaderInsumo.setLocation(AppMain.class.getResource("/vista/IAltaInsumo.fxml"));
		iAltaInsumo = null;
		controllerAltaInsumo = null;

		FXMLLoader loaderTablaInsumo = new FXMLLoader();
		loaderTablaInsumo.setLocation(AppMain.class.getResource("/vista/ITablaAbmInsumos.fxml"));
		iTablaInsumo = null;
		controllerTablaInsumo = null;
		
		/********************  INSUMOS  GRAL  ********************/
		
		FXMLLoader loaderTablaInsumoGral = new FXMLLoader();
		loaderTablaInsumoGral.setLocation(AppMain.class.getResource("/vista/ITablaInsumosGeneral.fxml"));
		iTablaInsumoGral = null;
		controllerTablaInsumoGral = null;
		
		/********************  PROVEEDORES   ********************/
		
		FXMLLoader loaderProveedor = new FXMLLoader();
		loaderProveedor.setLocation(AppMain.class.getResource("/vista/IAltaProveedor.fxml"));
		iAltaProveedor = null;
		controllerAltaProveedor = null;

		FXMLLoader loaderTablaProveedor = new FXMLLoader();
		loaderTablaProveedor.setLocation(AppMain.class.getResource("/vista/ITablaAbmProveedor.fxml"));
		iTablaProveedor = null;
		controllerTablaProveedor = null;
		
	/********************  INGRESO FACTURAS   *********************/
		
		FXMLLoader loaderIngresoFactura = new FXMLLoader();
		loaderIngresoFactura.setLocation(AppMain.class.getResource("/vista/IIngresoFactura.fxml"));
		iIngresoFactura = null;
		controllerIIngresoFactura = null;
		
	/********************  MODIFICAR FACTURAS   *********************/

		FXMLLoader loaderModificarFactura = new FXMLLoader();
		loaderModificarFactura.setLocation(AppMain.class.getResource("/vista/IModificarFactura.fxml"));
		iModificarFactura = null;
		controllerModificarFactura = null;
		
	/********************  VER FACTURAS   *********************/

		FXMLLoader loaderIFacturas = new FXMLLoader();
		loaderIFacturas.setLocation(AppMain.class.getResource("/vista/IFacturas.fxml"));
		iFacturas = null;
		controllerIFacturas = null;	
		
	/********************  INGRESO REMITOS   *********************/

		FXMLLoader loaderIngresarRemito = new FXMLLoader();
		loaderIngresarRemito.setLocation(AppMain.class.getResource("/vista/IIngresarRemito.fxml"));
		iIngresarRemito = null;
		controllerIIngresarRemito = null;
		
	/********************  MODIFICAR REMITOS   *********************/

		FXMLLoader loaderModificarRemito = new FXMLLoader();
		loaderModificarRemito.setLocation(AppMain.class.getResource("/vista/IModificarRemito.fxml"));
		iModificarRemito = null;
		controllerModificarRemito = null;
		
	/********************  VER REMITOS   *********************/
		
		FXMLLoader loaderIRemitos = new FXMLLoader();
		loaderIRemitos.setLocation(AppMain.class.getResource("/vista/IRemitos.fxml"));
		iRemitos = null;
		controllerIRemitos = null;
		
	/********************  MOVIMIENTOS   ********************/
		
		FXMLLoader loaderVistaMovimientos = new FXMLLoader();
		loaderVistaMovimientos.setLocation(AppMain.class.getResource("/vista/IVistaMovimientos.fxml"));
		iVistaMovimientos= null;
		controllerVistaMovimientos = null;
		
		FXMLLoader loaderTablaMovimientos = new FXMLLoader();
		loaderTablaMovimientos.setLocation(AppMain.class.getResource("/vista/ITablaMovimientos.fxml"));
		iTablaMovimientos= null;
		controllerTablaMovimientos = null;
		
	/**************** TABLA ACTUALIZAR STOCK   ******************/
		
		FXMLLoader loaderVistaInsumos = new FXMLLoader();
		loaderVistaInsumos.setLocation(AppMain.class.getResource("/vista/IVistaInsumos.fxml"));
		iVistaInsumos = null;
		controllerVistaInsumos = null;
		
		FXMLLoader loaderTablaDetalleInsumo = new FXMLLoader();
		loaderTablaDetalleInsumo.setLocation(AppMain.class.getResource("/vista/ITablaDetalleInsumo.fxml"));
		iTablaDetalleInsumo = null;
		controllerTablaDetalleInsumo = null;
		
		/******* VENTANA ACTUALIZAR STOCK INSUMO ********/
		
		FXMLLoader loaderActualizarStockInsumo = new FXMLLoader();
		loaderActualizarStockInsumo.setLocation(AppMain.class.getResource("/vista/IActualizarStockInsumo.fxml"));
		iActualizarStockInsumo = null;
		controllerActualizarStockInsumo = null;
		
	/****************  PDP   ******************/
		
		FXMLLoader loaderVistaPDP = new FXMLLoader();
		loaderVistaPDP.setLocation(AppMain.class.getResource("/vista/IVistaPDP.fxml"));
		iVistaPDP= null;
		controllerVistaPDP = null;
		
		FXMLLoader loaderTablaPDP = new FXMLLoader();
		loaderTablaPDP.setLocation(AppMain.class.getResource("/vista/ITablaPDP.fxml"));
		iTablaPDP= null;
		controllerTablaPDP = null;
		
		FXMLLoader loaderTablaPDPborrador = new FXMLLoader();
		loaderTablaPDPborrador.setLocation(AppMain.class.getResource("/vista/ITablaPDPborrador.fxml"));
		iTablaPDPborrador= null;
		controllerTablaPDPborrador = null;
		
	/****************  VENCIDOS   ******************/
		
		FXMLLoader loaderVistaVencidos = new FXMLLoader();
		loaderVistaVencidos.setLocation(AppMain.class.getResource("/vista/IVistaVencidos.fxml"));
		iVistaVencidos= null;
		controllerVistaVencidos = null;

		FXMLLoader loaderTablaVencidos = new FXMLLoader();
		loaderTablaVencidos.setLocation(AppMain.class.getResource("/vista/ITablaVencidos.fxml"));
		iTablaVencidos= null;
		controllerTablaVencidos = null;
	
		/****************  ORDEN DE COMPRA   ******************/
		
		FXMLLoader loaderVistaOrdenDeCompra = new FXMLLoader();
		loaderVistaOrdenDeCompra.setLocation(AppMain.class.getResource("/vista/IVistaOrdenDeCompra.fxml"));
		iVistaOrdenDeCompra= null;
		controllerIVistaOrdenDeCompra = null;
		
				/*********  ORDENES DE COMPRA   **********/
		
		FXMLLoader loaderIOrdenesDeCompra = new FXMLLoader();
		loaderIOrdenesDeCompra.setLocation(AppMain.class.getResource("/vista/IOrdenesDeCompra.fxml"));
		iOrdenesDeCompra= null;
		controllerIOrdenesDeCompra = null;

		/****************  INSUMOS  GRAL STOCK   ******************/
		
		FXMLLoader loaderTablaInsumoGralStock = new FXMLLoader();
		loaderTablaInsumoGralStock.setLocation(AppMain.class.getResource("/vista/ITablaInsumosGeneralStock.fxml"));
		iTablaInsumoGralStock = null;
		controllerTablaInsumoGralStock = null;	
		
		/**************** TABLA ACTUALIZAR STOCK   ******************/
		
		FXMLLoader loaderVistaInsumosStock = new FXMLLoader();
		loaderVistaInsumosStock.setLocation(AppMain.class.getResource("/vista/IVistaInsumosStock.fxml"));
		iVistaInsumosStock = null;
		controllerVistaInsumosStock = null;
		
	/****************  AHORA SI CARGO CONTROLADORES   ******************/	
		
		cargarVistaControlador_PantallaGestion(loaderAdmin);
		cargarVistaControlador_User(loaderUser, loaderTablaUser);
		cargarVistaControlador_Sector(loaderSector, loaderTablaSector);
		cargarVistaControlador_Categoria(loaderCategoria, loaderTablaCategoria);
		cargarVistaControlador_Insumo(loaderInsumo, loaderTablaInsumo);
		cargarVistaControlador_InsumoGral(loaderTablaInsumoGral);
		cargarVistaControlador_Proveedor(loaderProveedor, loaderTablaProveedor);
		cargarVistaControlador_IngresoFactura(loaderIngresoFactura);
		cargarVistaControlador_ModificarFactura(loaderModificarFactura);
		cargarVistaControlador_VerFacturas(loaderIFacturas);
		cargarVistaControlador_IngresoRemito(loaderIngresarRemito);
		cargarVistaControlador_ModificarRemito(loaderModificarRemito);
		cargarVistaControlador_VerRemitos(loaderIRemitos);
		cargarVistaControlador_VistaMovimientos(loaderVistaMovimientos, loaderTablaMovimientos);
		cargarVistaControlador_TablaActualizarStock(loaderVistaInsumos, loaderTablaDetalleInsumo);
		cargarVistaControlador_ActualizarStockInsumo(loaderActualizarStockInsumo);
		cargarVistaControlador_PDP(loaderVistaPDP, loaderTablaPDP, loaderTablaPDPborrador);
		cargarVistaControlador_Vencidos(loaderVistaVencidos, loaderTablaVencidos);
		cargarVistaControlador_OrdenDeCompra(loaderVistaOrdenDeCompra);
		cargarVistaControlador_OrdenesDeCompra(loaderIOrdenesDeCompra);
		cargarVistaControlador_InsumoGralStock(loaderTablaInsumoGralStock);
		cargarVistaControlador_VistaInsumosStock(loaderVistaInsumosStock);

		
	}


	private void cargarVistaControlador_VistaInsumosStock(FXMLLoader loaderVistaInsumosStock) {
		try {
			iVistaInsumosStock = loaderVistaInsumosStock.load();
			controllerVistaInsumosStock = loaderVistaInsumosStock.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}


	private void cargarVistaControlador_OrdenDeCompra(FXMLLoader loaderVistaOrdenDeCompra) {
		try {
			iVistaOrdenDeCompra = loaderVistaOrdenDeCompra.load();
			controllerIVistaOrdenDeCompra = loaderVistaOrdenDeCompra.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}		
	}
	
	
	private void cargarVistaControlador_OrdenesDeCompra(FXMLLoader loaderIOrdenesDeCompra) {
		try {
			iOrdenesDeCompra = loaderIOrdenesDeCompra.load();
			controllerIOrdenesDeCompra = loaderIOrdenesDeCompra.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}		
	}


	private void cargarVistaControlador_PantallaGestion(FXMLLoader loaderAdmin) {
		try {
			vistaAdmin = loaderAdmin.load();
			controllerPantAdmin = loaderAdmin.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_User(FXMLLoader loaderUser, FXMLLoader loaderTablaUser) {
		try {
			iAltaUser = loaderUser.load();
			controllerAltaUser = loaderUser.getController();
			
			iTablaUser = loaderTablaUser.load();
			controllerTablaUser = loaderTablaUser.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_Sector(FXMLLoader loaderSector, FXMLLoader loaderTablaSector) {
		try {
			iAltaSector = loaderSector.load();
			controllerAltaSector = loaderSector.getController();
			
			iTablaSector = loaderTablaSector.load();
			controllerTablaSector = loaderTablaSector.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	
	private void cargarVistaControlador_Categoria(FXMLLoader loaderCategoria, FXMLLoader loaderTablaCategoria) {
		try {
			iAltaCategoria = loaderCategoria.load();
			controllerAltaCategoria = loaderCategoria.getController();
			
			iTablaCategoria = loaderTablaCategoria.load();
			controllerTablaCategoria = loaderTablaCategoria.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_Insumo(FXMLLoader loaderInsumo, FXMLLoader loaderTablaInsumo) {
		try {
			iAltaInsumo = loaderInsumo.load();
			controllerAltaInsumo = loaderInsumo.getController();
			
			iTablaInsumo = loaderTablaInsumo.load();
			controllerTablaInsumo = loaderTablaInsumo.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_InsumoGral(FXMLLoader loaderTablaInsumoGral) {
		try {
			iTablaInsumoGral = loaderTablaInsumoGral.load();
			controllerTablaInsumoGral = loaderTablaInsumoGral.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	
	private void cargarVistaControlador_Proveedor(FXMLLoader loaderProveedor, FXMLLoader loaderTablaProveedor) {
		try {
			iAltaProveedor = loaderProveedor.load();
			controllerAltaProveedor = loaderProveedor.getController();
			
			iTablaProveedor = loaderTablaProveedor.load();
			controllerTablaProveedor = loaderTablaProveedor.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_VerFacturas(FXMLLoader loaderIFacturas) {
		try {
			iFacturas = loaderIFacturas.load();
			controllerIFacturas = loaderIFacturas.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_IngresoFactura(FXMLLoader loaderIngresoFactura) {
		try {
			iIngresoFactura = loaderIngresoFactura.load();
			controllerIIngresoFactura = loaderIngresoFactura.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_ModificarFactura(FXMLLoader loaderModificarFac) {
		try {
			iModificarFactura = loaderModificarFac.load();
			controllerModificarFactura = loaderModificarFac.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_IngresoRemito(FXMLLoader loaderIngresarRemito) {
		try {
			iIngresarRemito = loaderIngresarRemito.load();
			controllerIIngresarRemito = loaderIngresarRemito.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}		
	}
	
	
	private void cargarVistaControlador_ModificarRemito(FXMLLoader loaderModificarRemito) {
		try {
			iModificarRemito = loaderModificarRemito.load();
			controllerModificarRemito = loaderModificarRemito.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_VerRemitos(FXMLLoader loaderIRemitos) {
		try {
			iRemitos = loaderIRemitos.load();
			controllerIRemitos = loaderIRemitos.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_VistaMovimientos(FXMLLoader loaderVistaMovimientos,
															FXMLLoader loaderTablaMovimientos) {
		
		try {
			iVistaMovimientos= loaderVistaMovimientos.load();
			controllerVistaMovimientos = loaderVistaMovimientos.getController();
			
			iTablaMovimientos= loaderTablaMovimientos.load();
			controllerTablaMovimientos = loaderTablaMovimientos.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		
	}
	
	
	private void cargarVistaControlador_TablaActualizarStock(FXMLLoader loaderVistaInsumos,
															FXMLLoader loaderTablaDetalleInsumo) {
		try {
			iVistaInsumos = loaderVistaInsumos.load();
			controllerVistaInsumos = loaderVistaInsumos.getController();
			
			iTablaDetalleInsumo = loaderTablaDetalleInsumo.load();
			controllerTablaDetalleInsumo = loaderTablaDetalleInsumo.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_ActualizarStockInsumo(FXMLLoader loaderActualizarStockInsumo) {
		try {
			iActualizarStockInsumo = loaderActualizarStockInsumo.load();
			controllerActualizarStockInsumo = loaderActualizarStockInsumo.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_PDP(FXMLLoader loaderVistaPDP, FXMLLoader loaderTablaPDP, FXMLLoader loaderTablaPDPborrador) {
		try {
			iVistaPDP = loaderVistaPDP.load();
			controllerVistaPDP = loaderVistaPDP.getController();
			
			iTablaPDP = loaderTablaPDP.load();
			controllerTablaPDP = loaderTablaPDP.getController();
			
			iTablaPDPborrador = loaderTablaPDPborrador.load();
			controllerTablaPDPborrador = loaderTablaPDPborrador.getController();
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_Vencidos(FXMLLoader loaderVistaVencidos, FXMLLoader loaderTablaVencidos) {
		try {
			iVistaVencidos = loaderVistaVencidos.load();
			controllerVistaVencidos = loaderVistaVencidos.getController();
			
			iTablaVencidos = loaderTablaVencidos.load();
			controllerTablaVencidos = loaderTablaVencidos.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	
	private void cargarVistaControlador_InsumoGralStock(FXMLLoader loaderTablaInsGralStock) {
		try {
			iTablaInsumoGralStock = loaderTablaInsGralStock.load();
			controllerTablaInsumoGralStock = loaderTablaInsGralStock.getController();
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
	/********************************* Manejo de opciones ventana principal *******************************/
	
	@FXML
    void mostrarMovimientos() {
		//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);
		borderPanePpal.setCenter(iVistaMovimientos);
		
		//Al abrir la pantalla movimientos por primera vez, muestra los movimientos de los ultimos 30 dias
		//estas lineas obtienen los datos del dia y los 30 anteriores
		LocalDate hoy = LocalDate.now();
		Date hoyDate = java.sql.Date.valueOf(hoy);
		LocalDate treintaDiasAntes = LocalDate.now().minusDays(30);
		Date treintaDiasAntesDate = java.sql.Date.valueOf(treintaDiasAntes);
		
		controllerVistaMovimientos.getComboboxSucursal().getSelectionModel().clearAndSelect(0);
		
		controllerVistaMovimientos.getComoboxFiltro().getSelectionModel().clearAndSelect(-1);
		
		controllerVistaMovimientos.getTextField_busqueda().clear();
		controllerVistaMovimientos.getTextField_busqueda().setPromptText(null);
		controllerVistaMovimientos.getTextField_busqueda().setDisable(true);
		
		controllerVistaMovimientos.limpiarObList();
		
		controllerTablaMovimientos.botonBuscarMovimientos(treintaDiasAntesDate, hoyDate);
		
		controllerVistaMovimientos.getBorderPane_paraTabla().setCenter(iTablaMovimientos);
		
		controllerVistaMovimientos.verificarDatosSobreTablaMovimiento();
		
		controllerVistaMovimientos.getFechaInicio().setValue(treintaDiasAntes);
		controllerVistaMovimientos.getFechaFinal().setValue(hoy);
		controllerVistaMovimientos.getBtnFiltrar().setDisable(false);

		controllerVistaMovimientos.getLabelInicio().setTextFill(Color.web("BLACK"));
		controllerVistaMovimientos.getLabelFinal().setTextFill(Color.web("BLACK"));
				
		nombrePantalla = "MOVIMIENTOS";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+nombrePantalla);
		
		controllerTablaMovimientos.getTablaMovimientos().requestFocus();
    }
	
	
	@FXML
    void cerrarSesion(ActionEvent event) {
    	this.getPrimaryStage().close();
    	Stage stage = new Stage();
    	AnchorPane login = null;
		ControladorILogin controllerLogin = null;
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(AppMain.class.getResource("/vista/ILogin.fxml"));

		try {
			login = loader.load();
			controllerLogin = loader.getController();

		//esto no estoy del todo seguro si lo vamos a necesitar de nuevo, la referencia al primaryStage.
		//asi q x las dudas lo dejamos seteado dentro del controlador ppal y no perder referencia aunq no
		//lo usemos (o tal vez si)

			controllerLogin.setPrimaryStage(stage);

			Scene escena = new Scene(login);
			stage.setScene(escena);
			stage.initStyle(StageStyle.UNDECORATED);  //para q no tenga marco la ventana
			stage.show();
			stage.setTitle("LOGIN STOCK-LAB");
		//	stage.setResizable(false);
			
			Runtime.getRuntime().gc();  // GARBAGE COLLECTOR
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }

	
    @FXML
    void cerrarApp(ActionEvent event) {
    	Runtime.getRuntime().gc();  // GARBAGE COLLECTOR
  
    	Platform.exit();
    	
    	//Esta bien usado? al pasar el primary stage de login a controladorPpal al parecer se ejecutan
    	// 2 hilos que extienden de .Application - revisar.
    	System.exit(0);
    	
    	//cerramos la sessionFactory de hibernate
    	HibernateUtil.getSessionFactory().close();
    }
    
    
    @FXML
    void mostrarPantallaStock() {
    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);
		
		//Si el usuario es admin, no tiene sector asignado
		if (user.getAdmin().equals("No")) {
			setearSector();
		}
		
		//setea al centro de la vista pricipal, la vista de stock
		borderPanePpal.setCenter(iVistaInsumos);
		
		//este flag es para evitar error de auto action del combobox
		ControladorIVistaInsumos.flag = 0;
		
		controllerVistaInsumos.setearVistaInsumos(this.getSectorDeUsuario());
		//setea la tabla de los insumos para que este visible
		controllerVistaInsumos.getBorderPaneTablaInsumos().setCenter(iTablaDetalleInsumo);

		nombrePantalla = "ACTUALIZAR STOCK";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+nombrePantalla);

		controllerVistaInsumos.getBtnExportar().setDisable(true);
		
		controllerVistaInsumos.getTextField_Buscar().clear();
		controllerVistaInsumos.getTextField_Buscar().setDisable(true);
		
		controllerVistaInsumos.getLabelVencidos().setVisible(false);
		
    }

    @FXML
    void mostrarPantallaStock2() {
    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
    			borderPaneContenedor.setCenter(null);
    			
    			//Si el usuario es admin, no tiene sector asignado
    			if (user.getAdmin().equals("No")) {
    				setearSector();
    			}
    			
    			//setea al centro de la vista pricipal, la vista de stock
    			borderPanePpal.setCenter(iVistaInsumosStock);
    			
    			//este flag es para evitar error de auto action del combobox
    			ControladorIVistaInsumos.flag = 0;
    			
    			controllerVistaInsumosStock.setearVistaInsumos(this.getSectorDeUsuario());
    			//setea la tabla de los insumos para que este visible
    			controllerVistaInsumosStock.getBorderPaneTablaInsumos().setCenter(iTablaInsumoGralStock);

    			ControladorICsd_Principal.controllerTablaInsumoGralStock.initialize();
//    			ControladorICsd_Principal.controllerTablaInsumoGralStock.cargarInsumosGral();
//    			ControladorICsd_Principal.controllerTablaInsumoGralStock.removerDuplicadosEnTabla();

    			nombrePantalla = "ACTUALIZAR STOCK";
    			this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+nombrePantalla);

    			controllerVistaInsumosStock.getBtnExportar().setDisable(true);
    			
    			controllerVistaInsumosStock.getTextField_Buscar().clear();
    			controllerVistaInsumosStock.getTextField_Buscar().setDisable(true);
    			
    			controllerVistaInsumosStock.getLabelVencidos().setVisible(false);
    			
    }
    
    @SuppressWarnings("unchecked")
	private void setearSector() {
    	Transaction tx = null;
		long queryStart = 0;
    	try {
    		boolean encontrado = false;   
    		int i = 0;
    		
    		AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			queryStart = System.currentTimeMillis();
			
			Query query1 = appMain.getSession().createQuery("from UsuarioSector");
			List<UsuarioSector> ListaUsuarioEnSector = query1.list();
			
			long queryEnd = System.currentTimeMillis(); 
			System.out.println("Tabla UsuarioSector / Actualizar Stock = " + " query time: "+ (queryEnd-queryStart));
			
    		//en la tabla Usuario no tenemos el sector, entonces tenemos que recorrer la tabla relacion de UsuarioSector
    	   //para verificar a que sector esta ligado X usuario.
    		while (i <= ListaUsuarioEnSector.size() && !encontrado) {
    			
    			if (user.getPkDniUsuario() == ListaUsuarioEnSector.get(i).getUsuario().getPkDniUsuario()) {
    				
    				Query query2 = appMain.getSession().createQuery("from Sector where id= :id");
    				query2.setLong("id", ListaUsuarioEnSector.get(i).getSector().getIdSector());
    				Sector sector = (Sector) query2.uniqueResult();
    				
    				this.setSectorDeUsuario(sector.getNombreSector());
    				encontrado=true;
    			}else{
    				i++;
    			}
    		}
    		
    		appMain.getSession().close();
    		
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
 	}
    
    
    @FXML
    void mostrarPDP() {
    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);

		borderPanePpal.setCenter(iVistaPDP);
		
		controllerVistaPDP.getComboBoxSector().getItems().clear();
		
//		controllerVistaPDP.cargarSectoresYcategorias();
		controllerVistaPDP.llenarComboSector();
		controllerVistaPDP.llenarComboProveedor();
		
//		controllerVistaPDP.getComboBoxSector().getSelectionModel().clearAndSelect(0);
		
		controllerTablaPDP.llenarTablaPDP();
		controllerVistaPDP.getBorderTabla().setTop(iTablaPDP);
//		controllerTablaPDP.getTablaPDP().requestFocus();
//		controllerVistaPDP.getTextFieldBuscar().clear();
		controllerVistaPDP.getBorderTabla().setBottom(iTablaPDPborrador);
		
		
		controllerVistaPDP.addOblistAPDPGralYAux(ControladorICsd_Principal.controllerTablaPDP.obListInsumo);
//		controllerVistaPDP.setObListPDPGral(ControladorICsd_Principal.controllerTablaPDP.obListInsumo);
//		controllerVistaPDP.setObListPDPAux(ControladorICsd_Principal.controllerTablaPDP.obListInsumo);
		
		controllerVistaPDP.limpiarFiltros2();
		
		nombrePantalla = "PUNTOS DE PEDIDO ALCANZADOS";
		
		controllerVistaPDP.getBtnExportarOC().setDisable(true);
		controllerVistaPDP.getBtnAgregarAorden().setDisable(false);
		controllerVistaPDP.limpiarTablaDetalle();
		
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+nombrePantalla);
    }
    
    
    @FXML
    void mostrarOrdenDeCompra() {
    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);

		borderPanePpal.setCenter(iVistaOrdenDeCompra);
		
		controllerIVistaOrdenDeCompra.llenarComboProveedores();
		controllerIVistaOrdenDeCompra.getLblNombreUsuarioOrdenCompra().setText(getUser().getNombre());;
		controllerIVistaOrdenDeCompra.setearFechaCarga();
		
		controllerIVistaOrdenDeCompra.limpiarCampos();
		controllerIVistaOrdenDeCompra.desdePDP(false);
		
		nombrePantalla = "ORDEN DE COMPRA";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+nombrePantalla);
    }
    
    
    @FXML
    void mostrarVencidos() {
    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);

		borderPanePpal.setCenter(iVistaVencidos);
		
		controllerVistaVencidos.getComboBoxSector().getItems().clear();
		
		controllerVistaVencidos.cargarSectoresYcategorias();
		
		controllerVistaVencidos.getComboBoxSector().getSelectionModel().clearAndSelect(0);
		
		controllerTablaVencidos.llenarTablaVencidos();
		controllerVistaVencidos.getBorderTabla().setCenter(iTablaVencidos);
		controllerTablaVencidos.getTablaVencimiento().requestFocus();
		controllerVistaVencidos.getTextFieldBuscar().clear();

		nombrePantalla = "INSUMOS VENCIDOS";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+nombrePantalla);
    }

    
    @FXML
    void mostrarPantIngresoFactura() {
//    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);

		borderPanePpal.setCenter(iIngresoFactura);
		controllerIIngresoFactura.limpiarCampos();
		controllerIIngresoFactura.llenarComboProveedores();
		controllerIIngresoFactura.setearFechaCarga();
		
		//reseteo la listOb q utilizo para la pant secundaria, pa evitar la carga de insumos reiteradamente y q se
		//ponga lenta la app (en el caso de estar siempre en esta pantalla)
		//si viene de otra pestania, es necesario resetear la list ob, x si se actualizo algun valor de los insumos en la BD
		controllerIIngresoFactura.getObListInsumosPantSecundaria().clear();

		nombrePantalla = "INGRESAR FACTURA";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+ nombrePantalla);
    }

    
    @FXML
    public void modificarFactura() {
//    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
    	borderPaneContenedor.setCenter(null);

		borderPanePpal.setCenter(iModificarFactura);
		
		controllerModificarFactura.limpiarCampos();
		controllerModificarFactura.deshabilitarBotones();
		controllerModificarFactura.deshabilitarCampos();
		
		controllerModificarFactura.llenarComboProveedores();

		nombrePantalla = "MODIFICAR FACTURA";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+ nombrePantalla);
    }

    
    @FXML
    void mostrarFacturas(ActionEvent event) {
//    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);

		borderPanePpal.setCenter(iFacturas);
		controllerIFacturas.limpiarSeleccionEnTablaFactura();
		controllerIFacturas.limpiarTablaDetalle();
		controllerIFacturas.removerDuplicadosTablaFactura();
		controllerIFacturas.deshabilitarColNroRemito();       //oculto columna nro remito de la tabla detalle fac (x no todas fac tienen esta col llena)
		controllerIFacturas.limpiarTxtAreaObservacion();
		controllerIFacturas.cargarFacturas();
		controllerIFacturas.setearOrdenDatos();
		
		controllerIFacturas.getBtnModificar().setDisable(true);
		controllerIFacturas.getTablaFactura().getSelectionModel().clearSelection();
		controllerIFacturas.getTxtFieldNroFactura().clear();
		
		nombrePantalla = "MOSTRAR FACTURAS";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+ nombrePantalla);
    }

    
    @FXML
    void mostrarPantIngresoRemito() {
    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);
		
		borderPanePpal.setCenter(iIngresarRemito);

		controllerIIngresarRemito.limpiarCampos();
		controllerIIngresarRemito.llenarComboProveedores();
		controllerIIngresarRemito.getTxtFieldNroRemito().requestFocus();
		
		//reseteo la listOb q utilizo para la pant secundaria, pa evitar la carga de insumos reiteradamente y q se
		//ponga lenta la app (en el caso de estar siempre en esta pantalla)
		//si viene de otra pestania, es necesario resetear la list ob, x si se actualizo algun valor de los insumos en la BD
		controllerIIngresarRemito.getObListInsumosPantSecundaria().clear();
		
		nombrePantalla = "INGRESAR REMITO";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+ nombrePantalla);
    }

    
    @FXML
    void modificarRemito() {
//    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
	    borderPaneContenedor.setCenter(null);
	    
		borderPanePpal.setCenter(iModificarRemito);
		
		controllerModificarRemito.setearFechaCargaRemito();
		controllerModificarRemito.limpiarCampos();
		controllerModificarRemito.deshabilitarBotones();
		controllerModificarRemito.deshabilitarCampos();
		controllerModificarRemito.llenarComboProveedores();
		controllerModificarRemito.setDesdeVerRemitos(false);
		
		nombrePantalla = "MODIFICAR REMITO";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+ nombrePantalla);
    }
    
    

    
    @FXML
    void mostrarRemitos() {
//    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);

		borderPanePpal.setCenter(iRemitos);
		controllerIRemitos.limpiarSeleccionEnTablaRemito();
		controllerIRemitos.limpiarTablaDetalle();
		controllerIRemitos.removerDuplicadosTablaRemito();
		controllerIRemitos.limpiarTxtAreaObservacion();
		controllerIRemitos.cargarRemitos();
		controllerIRemitos.setearOrdenDatos();
		controllerIRemitos.resetearBtnModificar();
		
		controllerIRemitos.getTxtFieldNroRemito().clear();

		nombrePantalla = "MOSTRAR REMITOS";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+ nombrePantalla);
    }
    
    

    
    @FXML
    void verMovimientosFacturacion(ActionEvent event) {

    }
    
    
    @FXML
    void mostrarPantAdmin() {
    	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);
		
		//////////// USER
		//si el user logeado no es "admin", no podra usar pestania "usuarios"
		if (user.getAdmin().equals("Si")) {
			controllerPantAdmin.getTabUsuarios().setDisable(false);
			controllerPantAdmin.getBtnAltaUsuario().setDisable(false);
			controllerPantAdmin.getBtnBajaUsuario().setDisable(true);
			controllerPantAdmin.getBtnModifUsuario().setDisable(true);
			controllerTablaUser.llenarComboBoxSector(); //ahora lo llamo desde aca, ya no lo hacemos mas desde el initialize del controladorITablaAbmUsuarios
		} else {
			controllerPantAdmin.getTabUsuarios().setDisable(true);
		}

		//////////// SECTOR
		controllerPantAdmin.getBtnAltaSector().setDisable(false);
		controllerPantAdmin.getBtnBajaSector().setDisable(true);
		controllerPantAdmin.getBtnModifSector().setDisable(true);
		//////////// CATEGORIA
		controllerPantAdmin.getBtnAltaCategoria().setDisable(false);
		controllerPantAdmin.getBtnBajaCategoria().setDisable(true);
		controllerPantAdmin.getBtnModifCategoria().setDisable(true);
		controllerTablaCategoria.llenarComboBoxSector(); //ahora lo llamo desde aca, ya no lo hacemos mas desde el initialize del controladorITablaAbmCategorias
		//////////// INSUMO
		controllerPantAdmin.getBtnAltaInsumo().setDisable(false);
		controllerPantAdmin.getBtnBajaInsumo().setDisable(true);
		controllerPantAdmin.getBtnModifInsumo().setDisable(true);
		controllerPantAdmin.getBtnVerInsumo().setDisable(true);
		controllerPantAdmin.getBtnAtras().setDisable(true);
		
		//////////// PROVEEDOR
		controllerPantAdmin.getBtnAltaProveedor().setDisable(false);
		controllerPantAdmin.getBtnBajaProveedor().setDisable(true);
		controllerPantAdmin.getBtnModifProveedor().setDisable(true);
		
		borderPanePpal.setCenter(vistaAdmin);

		controllerPantAdmin.setearTablasEnPestanias(iTablaUser, iTablaSector, iTablaCategoria,
				iTablaInsumoGral, iTablaProveedor); ///adentro de este metodo, tambien actualizo sus datos

		//tambien necesito limpiar las selecciones q hayan quedado en las tablas
		controllerPantAdmin.limpiarSeleccionesEnTablas();

		//limpio los txtField de cada pestania q se utiliza pa los filtros
		controllerPantAdmin.limpiarTxtFieldsBuscar();

		nombrePantalla = "GESTION";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+nombrePantalla);
    }
    
    
    @FXML
    void abrirManual(ActionEvent event) {
//    	String fileLocal = new String("manualUser/Manual.pdf");
//    	try{
//    	   File path = new File (fileLocal);
//    	   Desktop.getDesktop().open(path);
//    	} catch (Exception e){
//	       e.printStackTrace();
//	       e.getMessage();
//    	}
    }

    
    @FXML
    void acercaDe(ActionEvent event) {
    	Stage otroStage = new Stage();

    	AnchorPane acercaDe = null;
		ControladorIAcercaDe controllerAcercaDe = null;
    	FXMLLoader l = new FXMLLoader();
    	l.setLocation(AppMain.class.getResource("/vista/IAcercaDe.fxml"));
    	try {
    		acercaDe = l.load();
    		controllerAcercaDe = l.getController();

    		controllerAcercaDe.setOtroStage(otroStage);
    		Scene escena = new Scene(acercaDe);
    		otroStage.setScene(escena);
//    		otroStage.show();
    		otroStage.setTitle("Acerca de...");
    		otroStage.setResizable(false);

    		otroStage.initOwner(ControladorILogin.controllerPpal.getPrimaryStage());
    		otroStage.initModality(Modality.APPLICATION_MODAL); 
    		otroStage.showAndWait();
    		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    public void mostrarProgressIndicator() {
    	stageProgress = new Stage();
    	
    	AnchorPane progressIndicator = null;
    	ControladorProgressIndicator controllerProgressIndicator = null;
    	controllerProgressIndicator = null;
	
    	FXMLLoader l = new FXMLLoader();
    	l.setLocation(AppMain.class.getResource("/vista/ProgressIndicator.fxml"));
    	try {
    		progressIndicator = l.load();
    		controllerProgressIndicator = l.getController();

    		controllerProgressIndicator.setOtroStage(stageProgress);
    		Scene escena = new Scene(progressIndicator);
    		
    		//lineas para hacer la ventana trasparente
    		escena.setFill(Color.TRANSPARENT);    		
    		progressIndicator.setBackground(Background.EMPTY);
    		stageProgress.initStyle(StageStyle.TRANSPARENT);
    		
    		stageProgress.setScene(escena);
    		stageProgress.show();
//    		stageProgress.setTitle("Cargando...");
    		stageProgress.setResizable(false);
    		
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
    }
    
    
    public void cerrarStageProgress() {
    	stageProgress.close();
//		user.setEstado("Desconectado");
//		ControladorILogin.opCRUD.actualizarObj(user);
    }
    
    
    @FXML
    void mostrarOrdenesDeCompra() {
	//SE SETEA NULO PARA Q LA IMAGEN NO APAREZCA EN OTRAS PANTALLAS
		borderPaneContenedor.setCenter(null);

		borderPanePpal.setCenter(iOrdenesDeCompra);
		
		controllerIOrdenesDeCompra.limpiarSeleccionEnTablaOrdenes();
		controllerIOrdenesDeCompra.limpiarTablaDetalle();
		controllerIOrdenesDeCompra.limpiarTablaOrdenes();
		controllerIOrdenesDeCompra.ocultarColDetalleUnidades();
		controllerIOrdenesDeCompra.desactivarBtnExportar();
		controllerIOrdenesDeCompra.limpiarTxtAreaObservacion();
		controllerIOrdenesDeCompra.limpiarComboYtxtField();
		controllerIOrdenesDeCompra.cargarOrdenesDeCompra();
		controllerIOrdenesDeCompra.setearOrdenDatosTablaOrdenes();
		controllerIOrdenesDeCompra.llenarComboProveedor();
		
		nombrePantalla = "VER ORDENES DE COMPRA";
		this.getPrimaryStage().setTitle("STOCK-LAB // "+user.getNombre()+" "+user.getApellido()+" // "+nombrePantalla);
    }


}
