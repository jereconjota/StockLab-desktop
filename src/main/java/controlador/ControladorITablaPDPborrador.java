package controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import modeloAux.InsumoFX;

public class ControladorITablaPDPborrador {

    @FXML
    private AnchorPane anchorPane_tablaPDPborrador;

    @FXML
    private TableView<InsumoFX> detalleOrdenDeCompra;

	@FXML
    private TableColumn<InsumoFX, String> detalleOrdenDeCompra_Insumo;
    
    @FXML
    private TableColumn<InsumoFX, Integer> tablaPDP_stockActual;

    @FXML
    private TableColumn<InsumoFX, Integer> tablaPDP_pdp;
    
    @FXML
    private TableColumn<InsumoFX, String> tablaPDP_proveedor;
    
    
    private ObservableList<InsumoFX> obListDetalleOrdenDeCompra = FXCollections.observableArrayList(); //para la tabla detalle orden de compra

    @FXML
    private Label lblBorradorOrdenCompra;
    

    /************************************  CONSTRUCTOR  ************************************/
    
    public ControladorITablaPDPborrador() {
	
    }

    
    
	/**************************** GET - SET **********************************/
    
    
    public TableView<InsumoFX> getDetalleOrdenDeCompra() {
		return detalleOrdenDeCompra;
	}
    public void setDetalleOrdenDeCompra(TableView<InsumoFX> detalleOrdenDeCompra) {
		this.detalleOrdenDeCompra = detalleOrdenDeCompra;
	}
	public TableColumn<InsumoFX, String> getDetalleOrdenDeCompra_Insumo() {
		return detalleOrdenDeCompra_Insumo;
	}
	public void setDetalleOrdenDeCompra_Insumo(TableColumn<InsumoFX, String> detalleOrdenDeCompra_Insumo) {
		this.detalleOrdenDeCompra_Insumo = detalleOrdenDeCompra_Insumo;
	}  
 
    public ObservableList<InsumoFX> getObListDetalleOrdenDeCompra() {
		return obListDetalleOrdenDeCompra;
	}
	public void setObListDetalleOrdenDeCompra(ObservableList<InsumoFX> obListDetalleOrdenDeCompra) {
		this.obListDetalleOrdenDeCompra = obListDetalleOrdenDeCompra;
	}


	/********************************** METODOS ***********************************/
	
    @FXML
    void initialize() {
    	definirTipoColumnas();
    	seleccionarFila();
    }
    
    private void definirTipoColumnas() {
    	detalleOrdenDeCompra_Insumo.setCellValueFactory(cellData -> cellData.getValue().nombreInsumo);
		tablaPDP_stockActual.setCellValueFactory(cellData -> cellData.getValue().stockGeneral);
		tablaPDP_pdp.setCellValueFactory(cellData -> cellData.getValue().pdp);
		tablaPDP_proveedor.setCellValueFactory(cellData -> cellData.getValue().proveedor);
		alinearContenidoColumnas();
	}
	
	private void alinearContenidoColumnas() {
		detalleOrdenDeCompra_Insumo.setStyle("-fx-alignment: CENTER;");
		tablaPDP_pdp.setStyle("-fx-alignment: CENTER;");
		tablaPDP_stockActual.setStyle("-fx-alignment: CENTER;");
		tablaPDP_proveedor.setStyle("-fx-alignment: CENTER;");
	}
	
	private void seleccionarFila(){
	//se le asigna una accion al click de cada fila de la tabla insumos
	detalleOrdenDeCompra.setRowFactory( tv -> {
	    TableRow<InsumoFX> row = new TableRow<>();
	    row.setOnMouseClicked(event -> {
	        if (event.getButton() == MouseButton.PRIMARY){
	        	
	        	if (event.getClickCount() == 2) { // DOBLE CLICK
	        		if (detalleOrdenDeCompra.getSelectionModel().getSelectedItem()!=null) {
	        			obListDetalleOrdenDeCompra.remove(detalleOrdenDeCompra.getSelectionModel().getSelectedItem());
		            	detalleOrdenDeCompra.setItems(obListDetalleOrdenDeCompra);
		            	
		            	//luego tengo q verificar q si no queda mas elementos en la tabla detalle 
		            	//entonces deshabilito los botones guardar y deshacer
			            	if (obListDetalleOrdenDeCompra.size() == 0) {
			            		ControladorICsd_Principal.controllerVistaPDP.getBtnExportarOC().setDisable(true);
			        		}
					}else{
						detalleOrdenDeCompra.getSelectionModel().clearSelection();
					}
	        		
				}
	        }
	    });
	    return row ;
		});
	}
}
