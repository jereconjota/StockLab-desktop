package controlador;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import main.AppMain;
import modelo.Insumo;
import modeloAux.InsumoFX;

public class ControladorITablaPDP {

	@FXML
    private AnchorPane anchorPane_tablaPDP;
	
	@FXML
    private TableView<InsumoFX> tablaPDP;
	
	@FXML
    private TableColumn<InsumoFX, String> tablaPDP_insumo;
	
	@FXML
    private TableColumn<InsumoFX, Integer> tablaPDP_stockActual;

    @FXML
    private TableColumn<InsumoFX, Integer> tablaPDP_pdp;
    
    @FXML
    private TableColumn<InsumoFX, String> tablaPDP_proveedor;

    public  ObservableList<InsumoFX> obListInsumo = FXCollections.observableArrayList();
    
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorITablaPDP() {
	
    }
    
    
    /**************************** GET - SET **********************************/
    
    public TableView<InsumoFX> getTablaPDP() {
		return tablaPDP;
	}
    
    
    
    /********************************** METODOS ***********************************/
    
    @FXML
    void initialize() {
		definirTipoColumnas();
    }
    
    
    private void definirTipoColumnas() {
		tablaPDP_insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaPDP_stockActual.setCellValueFactory(cellData -> cellData.getValue().stockGeneral);
		tablaPDP_pdp.setCellValueFactory(cellData -> cellData.getValue().pdp);
		tablaPDP_proveedor.setCellValueFactory(cellData -> cellData.getValue().proveedor);
		alinearContenidoColumnas();
	}
	
	private void alinearContenidoColumnas() {
		tablaPDP_insumo.setStyle("-fx-alignment: CENTER;");
		tablaPDP_pdp.setStyle("-fx-alignment: CENTER;");
		tablaPDP_stockActual.setStyle("-fx-alignment: CENTER;");
		tablaPDP_proveedor.setStyle("-fx-alignment: CENTER;");
	}
	
	
	
	
	public void llenarTablaPDP() {
		Transaction tx = null;
		try {
			obListInsumo.clear();
			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			//el valor dentro de la clausula where '1' es para q lo haga solo con insumos de sucursal diagnos
			
			Query query1 = appMain.getSession().createQuery("select new Insumo(ins.nombreInsumo, sum(ins.stockActual), ins.pdp, pro.nombreProveedor, "
					+ "sec.nombreSector, ins.nroArticulo , ins.referencia, ins.categoria, cat.nombreCategoria) "
					+ "from Insumo ins "
					+ "inner join ins.proveedor pro "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where ins.sucursal = '1' " 
					+ "group by ins.nombreInsumo "
					+ "having sum(ins.stockActual) <= ins.pdp");
    		
    		List<Insumo> listaInsumos = query1.list();
			
    		for (Insumo insumo : listaInsumos) {	
    			//llamamos al vacio xq solamente necesitamos mostrar nombre,pdp y el stockGral la sumatoria de los insumos
    			//q cumplen con la condicion de estar x debajo o igual al pdp
    			final InsumoFX insumoFX = new InsumoFX();
    			
    			insumoFX.nombreInsumo.set(insumo.getNombreInsumo());
    			insumoFX.pdp.set(insumo.getPdp());
    			
    			Integer stockGral = (int) (long) insumo.getStockGeneral();
    			insumoFX.stockGeneral.set(stockGral);
    			
    			insumoFX.proveedor.set(insumo.getNombreProveedor());
    			
    			//Lo necesitamos a la hr de exportar a orden de compra
    			insumoFX.articulo.set(insumo.getNroArticulo());
    			insumoFX.referencia.set(insumo.getReferencia());
    			insumoFX.nombreCategoria.set(insumo.getNombreCategoria());
    			insumoFX.nombreSector.set(insumo.getNombreSector());
    			
    			obListInsumo.add(insumoFX);    //1ero lleno toda la lista observable con todos los insumos de la BD
    		}
    		
    		appMain.getSession().close();
			
    		//finalmente le seteo a la tabla coincidencias la lista observable q no tiene insumos repetidos x nombre, y ordenamos por insumo
    		tablaPDP_insumo.setSortType(TableColumn.SortType.ASCENDING);
    		tablaPDP.setItems(obListInsumo);
    		tablaPDP.getSortOrder().add(tablaPDP_insumo);
    		
    		pintarFilas();
    		
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
	
	
	private void pintarFilas() {
		try {
		tablaPDP.setRowFactory(tv -> new TableRow<InsumoFX>() {
		    @Override
		    public void updateItem(InsumoFX item, boolean empty) {
		        super.updateItem(item, empty);
		 
		        if (item == null) {
		            setStyle("");
		        } else if (item.stockGeneral.get() == 0) {
		        	setStyle("-fx-background-color: #ff7f8a;");   // rojo clarito = #FE2E2E 
		        } else {
		        	setStyle("-fx-background-color: #ffbac0;");	  // rosado claro = #ffadb5 
		        }
		    }
		    
		});
		
		}catch (Exception e) {
			e.getMessage();
		}
	}
	
}
