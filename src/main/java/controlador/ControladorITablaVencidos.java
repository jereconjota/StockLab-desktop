package controlador;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DateStringConverter;
import main.AppMain;
import modelo.Insumo;
import modeloAux.InsumoFX;

public class ControladorITablaVencidos {
	
	@FXML
    private AnchorPane anchorPane_tablaVencimiento;

    @FXML
    private TableView<InsumoFX> tablaVencimiento;

    @FXML
    private TableColumn<InsumoFX, String> tablaVencidos_nroLote;

    @FXML
    private TableColumn<InsumoFX, String> tablaVencidos_insumo;

    @FXML
    private TableColumn<InsumoFX, Date> tablaVencimientos_fechaVto;
    
    public  ObservableList<InsumoFX> obListInsumo = FXCollections.observableArrayList();
    
    
    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorITablaVencidos() {
    	
	}

    
    /**************************** GET - SET **********************************/
    
    public TableView<InsumoFX> getTablaVencimiento() {
		return tablaVencimiento;
	}


    
    /********************************** METODOS ***********************************/
    
    @FXML
    void initialize() {
		definirTipoDatoColumnas();;
    }
    
    
    private void definirTipoDatoColumnas() {
		DateStringConverter convertirDaS = new DateStringConverter("dd-MM-yyyy");
		tablaVencidos_insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaVencidos_nroLote.setCellValueFactory(cellData -> cellData.getValue().pkNroLote);
		tablaVencimientos_fechaVto.setCellValueFactory(cellData -> cellData.getValue().fechaVencimiento);
		tablaVencimientos_fechaVto.setCellFactory(TextFieldTableCell.<InsumoFX, Date>forTableColumn(convertirDaS));
		alinearContenidoColumnas();
	}

	private void alinearContenidoColumnas() {
		tablaVencidos_insumo.setStyle("-fx-alignment: CENTER;");
		tablaVencidos_nroLote.setStyle("-fx-alignment: CENTER;");
		tablaVencimientos_fechaVto.setStyle("-fx-alignment: CENTER;");
		
	}
    
    
    
    public void llenarTablaVencidos() {
    	Transaction tx = null;
    	try {
			obListInsumo.clear();
			
			Date hoy = java.sql.Date.valueOf(LocalDate.now());
			
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query1 = appMain.getSession().createQuery("from Insumo "
					+ "where Fecha_Vencimiento <= \'" + hoy + "\' and Stock_Actual > " + 0);
			List<Insumo> listaInsumosVencidos = query1.list();
			
			for (Insumo insumo : listaInsumosVencidos) {
				final InsumoFX insumoFX = new InsumoFX(insumo);
				obListInsumo.add(insumoFX);
			}
			
			appMain.getSession().close();
			
			tablaVencidos_insumo.setSortType(TableColumn.SortType.ASCENDING);
			tablaVencimiento.setItems(obListInsumo);			
			tablaVencimiento.getSortOrder().add(tablaVencidos_insumo);
			} catch (Exception e) {
				e.printStackTrace();
				//rolling back to save the test data
				tx.rollback();
				e.getMessage();
			}
	}


	
    
}
