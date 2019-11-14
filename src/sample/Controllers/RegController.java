package sample.Controllers;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.*;

import java.net.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegController implements Initializable {
    private static ResourceBundle resourceBundle;
    private Locale ru=new Locale("ru");
    private Locale pt = new Locale("pt");
    private Locale lt = new Locale("lt");
    private Locale cr = new Locale("es","CR");
    private ResourceBundle ruRes=ResourceBundle.getBundle("sample.Locales.Nation",ru);
    private ResourceBundle ptRes=ResourceBundle.getBundle("sample.Locales.Nation",pt);
    private ResourceBundle ltRes=ResourceBundle.getBundle("sample.Locales.Nation",lt);
    private ResourceBundle crRes=ResourceBundle.getBundle("sample.Locales.Nation",cr);
    private static InetAddress serverAddress;
    private static int port;
    private DatagramSocket udpSocket;
    private static Stage primaryStage;
    @FXML
    private Button regButton;

    @FXML
    private Label regText;

    @FXML
    private ChoiceBox<String> localeChoiceBox;

    @FXML
    private TextField mailText;

    @FXML
    private TextField loginText;
    public void initialize(URL locale, ResourceBundle resourceBundle){
        try{
            udpSocket =new DatagramSocket();
        }catch (SocketException e){
            System.out.println("Упс");
        }
        Main main=new Main();
        regButton.setOnAction(event -> {
            if (mailText.getText().length() == 0 || loginText.getText().length() == 0) return;
            DatagramPacket regPacket = Client.createDatagramPacket(new Command("reg", mailText.getText(), loginText.getText(), ""), serverAddress, port);
            Client.sendDatagramPacket(regPacket, udpSocket);
            Response response = Client.serverResponse(8912, udpSocket);
            if (response == null)return;
                if (response.getMessage().equals(LocMessage.REG)) {
                    LogController.setResourceBundle(getResourceBundle());
                    main.openNewWindow("FXMLFiles/log.fxml", primaryStage);
                } else {
                    main.openNewWindow("FXMLFiles/title.fxml", primaryStage);
                }
            });
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("До встречи, до лучших времён -_-");
            System.exit(0);
        });
        setTextLanguage(RegController.resourceBundle);
        localeChoiceBox.setOnAction(event1 -> {
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
    loadLocaleChoiceBox();
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
    public static void setSending(InetAddress address,int port1){
        serverAddress=address;
        port=port1;
    }
    public static void setResourceBundle(ResourceBundle resourceBundle) {
       RegController.resourceBundle = resourceBundle;
    }
    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
    private void setTextLanguage(ResourceBundle resourceBundle){
        RegController.resourceBundle=resourceBundle;
        Locale.setDefault(RegController.resourceBundle.getLocale());
        regText.setText(resourceBundle.getString("regText"));
        regButton.setText(resourceBundle.getString("regButton"));
        mailText.setPromptText(resourceBundle.getString("mailText"));
        loginText.setPromptText(resourceBundle.getString("loginText"));
    }
}