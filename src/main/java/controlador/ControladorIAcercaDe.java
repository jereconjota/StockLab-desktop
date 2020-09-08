package controlador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ControladorIAcercaDe {
	
	@FXML
    private ImageView logoStockLab;

    @FXML
    private ImageView logoFiGo;

    @FXML
    private JFXButton btnAceptar;
    
    private Stage otroStage;


    /**********************  CONSTRUCTOR  ******************************/
    
	public ControladorIAcercaDe() {
	
	}
	    
    /**********************  GET y SET  ******************************/
    
	public Stage getOtroStage() {
		return otroStage;
	}
	public void setOtroStage(Stage otroStage) {
		this.otroStage = otroStage;
	}
	
	
	/**********************  METODOS  ******************************/
	
    @FXML
    void initialize() {
       cargarLogos();
    }
    
    
    @FXML
    void aceptar(ActionEvent event) {
    	this.getOtroStage().close();
    }

    
    private void cargarLogos() {
		FileInputStream input;
		FileInputStream inp;
		try {
			input = new FileInputStream("img/iconoStockLab.png");
			inp = new FileInputStream("img/logofigo.png");
			Image logofigo = new Image(inp);
			Image logostocklab = new Image(input);
			logoStockLab.setImage(logostocklab);
			logoFiGo.setImage(logofigo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
	
}
