package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controllers.LogController;
import sample.Controllers.MainController;
import sample.Controllers.RegController;
import sample.Controllers.TitleController;

import java.io.IOException;
import java.net.InetAddress;

public class Main extends Application {
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage1) throws Exception{
        primaryStage=primaryStage1;
        setControllerStage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLFiles/title.fxml"));
        primaryStage.setTitle("Lab8");
        primaryStage.setScene(new Scene(root, 800, 525));
        primaryStage.show();
    }
    public void setControllerStage(){
        TitleController.setPrimaryStage(getPrimaryStage());
        LogController.setPrimaryStage(getPrimaryStage());
        RegController.setPrimaryStage(getPrimaryStage());
        MainController.setPrimaryStage(getPrimaryStage());
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void openNewWindow(String url, Stage primaryStage){

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(url));
        System.out.println();
        try{
            loader.load();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Не удалось загрузить FXMLLoader");
        }
        Parent root = loader.getRoot();
        primaryStage.setScene(new Scene(root));
    }
    public static void main(String[] args) {
        if(args.length!=2){
            Client.showUsage();
        }
        try{
            Client client =new Client(args[0],Integer.parseInt(args[1]));
            System.out.println("Запуск клиента "+ InetAddress.getLocalHost());
            System.out.println("Введённый InetAddress: "+args[0]+" Port: "+args[1]+" .");
            client.testConnection();
            LogController.setSending(client.getServerAddress(),client.getPort());
            RegController.setSending(client.getServerAddress(),client.getPort());
            MainController.setSending(client.getServerAddress(),client.getPort());
            launch(args);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Короче, Меченый, я тебе программу написал и в благородство играть не буду: введёшь для меня нужные данные - и мы в расчёте.");
            System.out.println("Я в чужие дела не лезу, хочешь запустить, значит есть за что...");
            Client.showUsage();
        }
    }
}
