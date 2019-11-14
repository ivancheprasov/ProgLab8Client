package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.Main;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class TitleController implements Initializable {
    private static ResourceBundle resourceBundle;
    private Locale ru=new Locale("ru");
    private Locale pt = new Locale("pt");
    private Locale lt = new Locale("lt");
    private Locale cr = new Locale("es","CR");
    private ResourceBundle ruRes=ResourceBundle.getBundle("sample.Locales.Nation",ru);
    private ResourceBundle ptRes=ResourceBundle.getBundle("sample.Locales.Nation",pt);
    private ResourceBundle ltRes=ResourceBundle.getBundle("sample.Locales.Nation",lt);
    private ResourceBundle crRes=ResourceBundle.getBundle("sample.Locales.Nation",cr);
    private static Stage primaryStage;
    @FXML
    private Label welcomeText;
    @FXML
    private Button logButton;
    @FXML
    private Button regButton;
    @FXML
    private ChoiceBox<String> localeChoiceBox;
    public void initialize(URL locale, ResourceBundle resourceBundle){
        TitleController.resourceBundle=ruRes;
        Main main=new Main();
        loadLocaleChoiceBox();
        regButton.setOnAction(event-> {
            RegController.setResourceBundle(getResourceBundle());
            main.openNewWindow("FXMLFiles/reg.fxml",primaryStage);
        });
        logButton.setOnAction(event -> {
            LogController.setResourceBundle(getResourceBundle());
            main.openNewWindow("FXMLFiles/log.fxml",primaryStage);
        });
        localeChoiceBox.setOnAction(event -> {
            ResourceBundle res=null;
            switch (localeChoiceBox.getValue()){
                case "RU":
                    res=ruRes;
                    break;
                case "PT":
                    res=ptRes;
                    break;
                case "LT":
                    res=ltRes;
                    break;
                case "CR":
                    res=crRes;
                    break;
            }
            setTextLanguage(res);
        });
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("До встречи, до лучших времён -_-");
            System.exit(0);
        });
    }
    private void loadLocaleChoiceBox(){
        localeChoiceBox.getItems().addAll("RU","PT","LT","CR");
        String language=null;
        switch(resourceBundle.getLocale().getLanguage()){
            case "ru":
                language="RU";
                break;
            case "pt":
                language="PT";
                break;
            case "lt":
                language="LT";
                break;
            case "es":
                language="CR";
                break;
        }
        localeChoiceBox.setValue(language);
    }
    public static void setPrimaryStage(Stage primaryStage1) {
        primaryStage = primaryStage1;
    }
    private void setTextLanguage(ResourceBundle resourceBundle1){
        resourceBundle=resourceBundle1;
        Locale.setDefault(resourceBundle.getLocale());
        welcomeText.setText(resourceBundle.getString("welcomeText"));
        logButton.setText(resourceBundle.getString("logButton"));
        regButton.setText(resourceBundle.getString("regButton"));
    }

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
