package sample.Controllers;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.*;
import sample.Story.InteractWithThings;

import java.net.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogController implements Initializable {
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
    private Button logButton;
    @FXML
    private Label logText;
    private static InetAddress serverAddress;
    private static int port;
    private DatagramSocket udpSocket;
    @FXML
    private ChoiceBox<String> localeChoiceBox;

    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField loginText;
    public void initialize(URL locale, ResourceBundle resourceBundle){
        try{
            udpSocket =new DatagramSocket();
        }catch (SocketException e){
            System.out.println("Упс");
        }
        Main main=new Main();
        logButton.setOnAction(event -> {
            if(loginText.getText().length()==0||passwordText.getText().length()==0)return;
            DatagramPacket logPacket=Client.createDatagramPacket(new Command("log","",loginText.getText(),passwordText.getText()),serverAddress,port);
            Client.sendDatagramPacket(logPacket,udpSocket);
            Response response=Client.serverResponse(8192,udpSocket);
            if(response==null){
                main.openNewWindow("FXMLFiles/title.fxml",primaryStage);
            }else{
                if(response.getMessage().equals(LocMessage.LOG)){
                    MainController.setLoginAndPassword(loginText.getText(),passwordText.getText());
                    MainController.setMyCollection((List<InteractWithThings>)response.getResponse());
                    MainController.setResourceBundle(LogController.resourceBundle);
                    main.openNewWindow("FXMLFiles/main.fxml",primaryStage);
                }else{
                    main.openNewWindow("FXMLFiles/title.fxml",primaryStage);
                }
            }
        });
        loadLocaleChoiceBox();
        setTextLanguage(LogController.resourceBundle);
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
    private void setTextLanguage(ResourceBundle resourceBundle){
        LogController.setResourceBundle(resourceBundle);
        Locale.setDefault(LogController.resourceBundle.getLocale());
        loginText.setPromptText(resourceBundle.getString("loginText"));
        logButton.setText(resourceBundle.getString("logButton"));
        logText.setText(resourceBundle.getString("logText"));
        passwordText.setPromptText(resourceBundle.getString("passwordText"));
    }
    public static void setPrimaryStage(Stage primaryStage1) {
        primaryStage = primaryStage1;
    }
    public static void setSending(InetAddress address,int port1){
        serverAddress=address;
        port=port1;
    }
    public static void setResourceBundle(ResourceBundle resourceBundle) {
        LogController.resourceBundle = resourceBundle;
    }
}
