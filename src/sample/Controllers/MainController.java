package sample.Controllers;

import java.io.File;
import java.io.FileReader;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.*;
import sample.Story.*;

public class MainController implements Initializable {
    private static ResourceBundle resourceBundle;
    private Locale ru=new Locale("ru");
    private Locale pt = new Locale("pt");
    private Locale lt = new Locale("lt");
    private Locale cr = new Locale("es","CR");
    private ResourceBundle ruRes=ResourceBundle.getBundle("sample.Locales.Nation",ru);
    private ResourceBundle ptRes=ResourceBundle.getBundle("sample.Locales.Nation",pt);
    private ResourceBundle ltRes=ResourceBundle.getBundle("sample.Locales.Nation",lt);
    private ResourceBundle crRes=ResourceBundle.getBundle("sample.Locales.Nation",cr);
    private DatagramSocket udpSocket;
    private static Stage primaryStage;
    private static InetAddress serverAddress;
    private static int port;
    private static String login;
    private static String password;
    private static List<InteractWithThings> myCollection;
    private String typeFilterValue="All";
    private String stateFilterValue="All";
    @FXML
    private ChoiceBox<String> localeChoiceBox;

    @FXML
    private ChoiceBox<String> tableChoiceBox;

    @FXML
    private Button exitButton;

    @FXML
    private Text loginText;

    @FXML
    private Button importButton;

    @FXML
    private Button informationButton;

    @FXML
    private Button addButton;

    @FXML
    private Button add_if_minButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button removeButton;

    @FXML
    private Slider ySlider;

    @FXML
    private Slider xSlider;

    @FXML
    private Slider costSlider;

    @FXML
    private TextField nameText;

    @FXML
    private RadioButton goodRadioButton;

    @FXML
    private RadioButton badRadioButton;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    @FXML
    private CheckBox openedCheckBox;

    @FXML
    private Text locationText;

    @FXML
    private Text cost;

    @FXML
    private Text xLocation;

    @FXML
    private Text type;

    @FXML
    private Text state;

    @FXML
    private TextArea informationText;

    @FXML
    private Text yLocation;

    @FXML
    private TableView<InteractWithThings> table;

    @FXML
    private TableColumn<InteractWithThings, String> nameColumn;

    @FXML
    private TableColumn<InteractWithThings, String> typeColumn;

    @FXML
    private TableColumn<InteractWithThings, String> stateColumn;

    @FXML
    private TableColumn<InteractWithThings, Integer> costColumn;

    @FXML
    private TableColumn<InteractWithThings, Integer> xColumn;

    @FXML
    private TableColumn<InteractWithThings, Integer> yColumn;

    @FXML
    private TableColumn<InteractWithThings, String> dateColumn;

    @FXML
    private TableColumn<InteractWithThings, String> optionalColumn;

    @FXML
    private Canvas canvas;

    @FXML
    private TextField nameFilter;

    @FXML
    private ChoiceBox<String> typeFilter;

    @FXML
    private ChoiceBox<String> stateFilter;

    @FXML
    private TextField costFilter;

    @FXML
    private TextField xFilter;

    @FXML
    private TextField yFilter;

    @FXML
    private TextField dateFilter;

    @FXML
    private TextField optionalFilter;
    private Timeline timeline;
    private Thread thread=new Thread(this::getMessage);
    private GraphicsContext graphicsContext;
    @FXML
    public void initialize(URL location,ResourceBundle resources) {
        goodRadioButton.setSelected(true);
        try{
            udpSocket =new DatagramSocket();
        }catch (SocketException e){
            System.out.println("Упс");
        }
        canvas.setVisible(false);
        graphicsContext=canvas.getGraphicsContext2D();
        Main main=new Main();
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0,0,431,454);
        getOwnerAndPaint();
        loadLocaleChoiceBox();
        loadTableChoiceBox();
        loadTypeChoiceBox();
        loadTypeFilter();
        loadStateFilter();
        setTextLanguage(MainController.resourceBundle);
        loginText.setText(login);
        informationText.setEditable(false);
        makeToggleGroups();
        makeTable();
        exitButton.setOnAction(event -> {
            timeline.stop();
            thread.interrupt();
            main.openNewWindow("FXMLFiles/title.fxml",primaryStage);
        });
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("До встречи, до лучших времён -_-");
            System.exit(0);
        });
        informationButton.setOnAction(event -> {
            DatagramPacket datagramPacket=Client.createDatagramPacket(new Command("info","",login,password),serverAddress,port);
            Client.sendDatagramPacket(datagramPacket,udpSocket);
        });
        addButton.setOnAction(event -> {
            InteractWithThings element=elementCreation();
                if(element!=null) {
                    DatagramPacket datagramPacket = Client.createDatagramPacket(new Command("add", element, login, password), serverAddress, port);
                    Client.sendDatagramPacket(datagramPacket, udpSocket);
                }
        });
        removeButton.setOnAction(event->{
            InteractWithThings element=elementCreation();
            if(element!=null) {
                DatagramPacket datagramPacket = Client.createDatagramPacket(new Command("remove_element", element, login, password), serverAddress, port);
                Client.sendDatagramPacket(datagramPacket, udpSocket);
            }
        });
        timeline=new Timeline(new KeyFrame(Duration.seconds(1),event -> {
            getCollection();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        thread.start();
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
        importButton.setOnAction(event ->{
            FileChooser fileChooser=new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV","*.csv"));
            File file=fileChooser.showOpenDialog(primaryStage);
            try{
                StringBuilder stringBuilder=new StringBuilder();
                String filepath=file.getAbsolutePath();
                FileReader fileReader=new FileReader(filepath);
                Scanner scanner=new Scanner(fileReader);
                while(scanner.hasNextLine()){
                    stringBuilder.append(scanner.nextLine()+"\n");
                }
                fileReader.close();
                DatagramPacket datagramPacket=Client.createDatagramPacket(new Command("import",stringBuilder.toString(),login,password),serverAddress,port);
                Client.sendDatagramPacket(datagramPacket,udpSocket);
            }catch(Exception e){
                System.out.println("Не удалось выполнить импорт.");
            }
        });
        tableChoiceBox.setOnAction(event -> {
            try{
                if(tableChoiceBox.getValue().equals("Canvas")){
                    table.setVisible(false);
                    nameFilter.setVisible(false);
                    typeFilter.setVisible(false);
                    stateFilter.setVisible(false);
                    costFilter.setVisible(false);
                    xFilter.setVisible(false);
                    yFilter.setVisible(false);
                    dateFilter.setVisible(false);
                    optionalFilter.setVisible(false);
                    canvas.setVisible(true);
                }else{
                    table.setVisible(true);
                    nameFilter.setVisible(true);
                    typeFilter.setVisible(true);
                    stateFilter.setVisible(true);
                    costFilter.setVisible(true);
                    xFilter.setVisible(true);
                    yFilter.setVisible(true);
                    dateFilter.setVisible(true);
                    optionalFilter.setVisible(true);
                    canvas.setVisible(false);
                }
            }catch (NullPointerException e){
            }
        });
        xSlider.setOnMouseClicked(event ->
            locationText.setText(MainController.resourceBundle.getString("location")+": ("+(int)xSlider.getValue()+";"+(int)ySlider.getValue()+")"));
        ySlider.setOnMouseClicked(event ->
                locationText.setText(MainController.resourceBundle.getString("location")+": ("+(int)xSlider.getValue()+";"+(int)ySlider.getValue()+")"));
        costSlider.setOnMouseClicked(event->
                cost.setText(MainController.resourceBundle.getString("cost1")+": "+(int)costSlider.getValue()));
        add_if_minButton.setOnAction(event->{
            InteractWithThings element=elementCreation();
            if(element!=null) {
                DatagramPacket datagramPacket = Client.createDatagramPacket(new Command("add_if_min", element, login, password), serverAddress, port);
                Client.sendDatagramPacket(datagramPacket, udpSocket);
            }
        });
        canvas.setOnMouseClicked(event -> {
            InteractWithThings element=findObject((int)event.getX(),(int)event.getY());
            if(element!=null){
                graphicsContext.setFill(Color.WHITE);
                graphicsContext.fillRect(0,0,431,454);
                myCollection.stream().filter(e->!e.getThing().equals(element.getThing())).forEach(e->makeObject(e));
                makeObject(element);
                setInformation(element);
                makeSelected(element);
            }
        });
        nameFilter.setOnAction(event -> makeFilters());
        typeFilter.setOnAction(event -> makeFilters());
        stateFilter.setOnAction(event -> makeFilters());
        xFilter.setOnAction(event -> makeFilters());
        yFilter.setOnAction(event -> makeFilters());
        costFilter.setOnAction(event -> makeFilters());
        dateFilter.setOnAction(event -> makeFilters());
        optionalFilter.setOnAction(event -> makeFilters());
        updateButton.setOnAction(event->{
            InteractWithThings element=elementCreation();
            if(element!=null){
                DatagramPacket datagramPacket =Client.createDatagramPacket(new Command("update",element,login,password),serverAddress,port);
                Client.sendDatagramPacket(datagramPacket,udpSocket);
            }
        });
    }
    private void getCollection(){
        DatagramPacket datagramPacket=Client.createDatagramPacket(new Command("show","",login,password),serverAddress,port);
        Client.sendDatagramPacket(datagramPacket,udpSocket);
    }
    private InteractWithThings findObject(int x,int y){
        for(int i=0;i<myCollection.size();i++){
            InteractWithThings element=myCollection.get(i);
            if(x>=element.getX()&&x<=element.getX()+100&&y>=element.getY()-25&&y<=element.getY()+75)return element;
        }
        return null;
    }
    private void makeTable(){
        table.setOnMouseClicked(event -> selectObjectFromTable());
        makeFilters();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("thing"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state1"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("y"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("time2"));
        optionalColumn.setCellValueFactory(new PropertyValueFactory<>("optional"));
        updateTable();
    }
    private void makeToggleGroups(){
        ToggleGroup stateGroup=new ToggleGroup();
        goodRadioButton.setToggleGroup(stateGroup);
        badRadioButton.setToggleGroup(stateGroup);
    }
    public InteractWithThings elementCreation() {
        InteractWithThings element = null;
        if (nameText.getText().equals("")) {
            informationText.setText(MainController.resourceBundle.getString("blankName"));
        } else {
            State state = null;
            if (goodRadioButton.isSelected()) state = State.GOOD;
            if (badRadioButton.isSelected()) state = State.BAD;
            switch (typeChoiceBox.getValue()) {
                case "Корж":
                case "Korzh":
                    element = new CakeLayer(nameText.getText(), state, new Location((int) xSlider.getValue(), (int) ySlider.getValue()),
                            (int) costSlider.getValue());
                    break;
                case "Картошка":
                case "Papa":
                case "Bulvė":
                case "Batata":
                    element = new Potato(nameText.getText(), state, new Location((int) xSlider.getValue(), (int) ySlider.getValue()),
                            (int) costSlider.getValue());
                    break;
                case "Суп":
                case "Sopa":
                case "Sriuba":
                    element = new Soup(nameText.getText(), state, new Location((int) xSlider.getValue(), (int) ySlider.getValue()),
                            (int) costSlider.getValue());
                    break;
                case "Окно":
                case "Ventana":
                case "Langas":
                case "Janela":
                    element = new Window(nameText.getText(), state, new Location((int) xSlider.getValue(), (int) ySlider.getValue()),
                            (int) costSlider.getValue(), openedCheckBox.isSelected());
                    break;
                case "Дверь":
                case "Puerta":
                case "Durys":
                case "Porta":
                    element = new Door(nameText.getText(), state, new Location((int) xSlider.getValue(), (int) ySlider.getValue()),
                            (int) costSlider.getValue(), openedCheckBox.isSelected());
                    break;
                case "Носки":
                case "Calcetines":
                case "Meias":
                case "Kojinės":
                    element = new Socks(nameText.getText(), state, new Location((int) xSlider.getValue(), (int) ySlider.getValue()),
                            (int) costSlider.getValue());
                    break;
                case "Штаны":
                case "Pantalones":
                case "Kelnės":
                case "Calças":
                    element = new Pants(nameText.getText(), state, new Location((int) xSlider.getValue(), (int) ySlider.getValue()),
                            (int) costSlider.getValue(), openedCheckBox.isSelected());
                    break;
            }
        }
        if(element!=null)element.setOwner(login);
        return element;
    }
    private void updateTable(){
        ObservableList<InteractWithThings> collForTable=FXCollections.observableArrayList(myCollection);
        try{
            table.setItems(collForTable);
        }catch (IllegalStateException e){
            //e.printStackTrace();
        }
    }
    public void makeObject(InteractWithThings element){
        Color color=getColour(element.getOwner());
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setLineWidth(2);
        graphicsContext.fillRect(element.getX(),element.getY(),100,75);
        graphicsContext.fillRect(element.getX()-8,element.getY()-5.5,8,16);
        graphicsContext.setFill(color);
        graphicsContext.fillRect(element.getX()+10,element.getY()+10,80,55);
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.fillRect(element.getX()-7.5,element.getY()-5,7.5,15);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(element.getX(),element.getY()-25,100,25);
        graphicsContext.setFill(color);
        graphicsContext.fillRect(element.getX()+10,element.getY()-15,80,15);
        graphicsContext.strokeLine(element.getX()+10,element.getY()+10,element.getX()+20,element.getY()+65);
        graphicsContext.strokeLine(element.getX()+20,element.getY()+10,element.getX()+30,element.getY()+65);
        graphicsContext.strokeLine(element.getX()+30,element.getY()+10,element.getX()+40,element.getY()+65);
        graphicsContext.strokeLine(element.getX()+40,element.getY()+10,element.getX()+50,element.getY()+65);
        graphicsContext.strokeLine(element.getX()+50,element.getY()+10,element.getX()+60,element.getY()+65);
        graphicsContext.strokeLine(element.getX()+60,element.getY()+10,element.getX()+70,element.getY()+65);
        graphicsContext.strokeLine(element.getX()+70,element.getY()+10,element.getX()+80,element.getY()+65);
        graphicsContext.strokeLine(element.getX()+80,element.getY()+10,element.getX()+90,element.getY()+65);
        graphicsContext.strokeLine(element.getX()+10,element.getY()-7.5,element.getX()+90,element.getY()-7.5);
    }
    private void makeChestOpened(InteractWithThings element) {
        Color color = getColour(element.getOwner());
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setLineWidth(2);
        graphicsContext.fillRect(element.getX(), element.getY(), 100, 75);
        graphicsContext.fillRect(element.getX() - 8, element.getY() - 5.5, 8.5, 16);
        graphicsContext.setFill(color);
        graphicsContext.fillRect(element.getX() + 10, element.getY() + 10, 80, 55);
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.fillRect(element.getX() - 7.5, element.getY() - 5, 7.5, 15);
        //graphicsContext.strokeLine(element.getX(),element.getY()-5,element.getX(),element.getY()+7.5);
        graphicsContext.strokeLine(element.getX() + 10, element.getY() + 10, element.getX() + 20, element.getY() + 65);
        graphicsContext.strokeLine(element.getX() + 20, element.getY() + 10, element.getX() + 30, element.getY() + 65);
        graphicsContext.strokeLine(element.getX() + 30, element.getY() + 10, element.getX() + 40, element.getY() + 65);
        graphicsContext.strokeLine(element.getX() + 40, element.getY() + 10, element.getX() + 50, element.getY() + 65);
        graphicsContext.strokeLine(element.getX() + 50, element.getY() + 10, element.getX() + 60, element.getY() + 65);
        graphicsContext.strokeLine(element.getX() + 60, element.getY() + 10, element.getX() + 70, element.getY() + 65);
        graphicsContext.strokeLine(element.getX() + 70, element.getY() + 10, element.getX() + 80, element.getY() + 65);
        graphicsContext.strokeLine(element.getX() + 80, element.getY() + 10, element.getX() + 90, element.getY() + 65);
        graphicsContext.setFill(Color.BLACK);
        int x = element.getX();
        int y = element.getY();
        graphicsContext.fillPolygon(new double[]{x + 50, x + 50 + 25 * Math.sqrt(3) / 2, x + 100 + 25 * Math.sqrt(3) / 2, x + 100},
                new double[]{y - 100 * Math.sqrt(3) / 2, y - 100 * Math.sqrt(3) / 2 - 25 / 2,
                y - 25 / 2, y}, 4);
        graphicsContext.setFill(color);
        graphicsContext.fillPolygon(new double[]{x+55,x+100-32.00961894,x+100+(25-10*Math.sqrt(3)/2)/2,x+95},new double[]{y-90*Math.sqrt(3)/2,
                y-85.44228634,
              y-10*Math.sqrt(3)/2-(25-10*Math.sqrt(3)/2)/2,y-10*Math.sqrt(3)/2},4);
        graphicsContext.strokeLine((x+55+x+100-32.00961894)/2,(y-90*Math.sqrt(3)/2+
                y-85.44228634)/2,(x+100+(25-10*Math.sqrt(3)/2)/2+x+95)/2,(y-10*Math.sqrt(3)/2-(25-10*Math.sqrt(3)/2)/2+y-10*Math.sqrt(3)/2)/2);
        graphicsContext.setLineWidth(0.5);
        graphicsContext.strokeLine(x+55,y-90*Math.sqrt(3)/2,x+95,y-10*Math.sqrt(3)/2);
    }
    private void makeDiamond(InteractWithThings element){
        int x=element.getX();
        int y=element.getY();
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillPolygon(new double[]{x+45,x+30,x+45,x+60},new double[]{
                y-15,y-45,y-65,y-45},4);
        graphicsContext.setStroke(Color.LIGHTBLUE);
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeLine(x+30,y-45,x+40,y-40);
        graphicsContext.strokeLine(x+40,y-40,x+50,y-40);
        graphicsContext.strokeLine(x+50,y-40,x+60,y-45);
        graphicsContext.strokeLine(x+45,y-65,x+40,y-40);
        graphicsContext.strokeLine(x+45,y-65,x+50,y-40);
        graphicsContext.strokeLine(x+45,y-15,x+40,y-40);
        graphicsContext.strokeLine(x+45,y-15,x+50,y-40);
    }
    private void makeShine(InteractWithThings element){
        int x=element.getX();
        int y=element.getY();
        graphicsContext.setStroke(Color.YELLOW);
        graphicsContext.setLineWidth(2);
        graphicsContext.strokeArc(x+20,y-55,50,30,135,175, ArcType.OPEN);
    }
    private void makeSelected(InteractWithThings element){
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeLine(element.getX()-15,element.getY()+90,element.getX()+35,element.getY()+90);
        graphicsContext.strokeLine(element.getX()-15,element.getY()+90,element.getX()-15,element.getY()+50);
        graphicsContext.strokeLine(element.getX()+115,element.getY()+90,element.getX()+115,element.getY()+50);
        graphicsContext.strokeLine(element.getX()+115,element.getY()+90,element.getX()+75,element.getY()+90);
        graphicsContext.strokeLine(element.getX()+115,element.getY()-40,element.getX()+75,element.getY()-40);
        graphicsContext.strokeLine(element.getX()+115,element.getY()-40,element.getX()+115,element.getY());
        graphicsContext.strokeLine(element.getX()-15,element.getY()-40,element.getX()+35,element.getY()-40);
        graphicsContext.strokeLine(element.getX()-15,element.getY()-40,element.getX()-15,element.getY());
    }
    private void makeAnimation(InteractWithThings element){
        Color color=getColour(element.getOwner());
        Timeline timeline=new Timeline(
          new KeyFrame(Duration.seconds(0.5),event -> {
              graphicsContext.setFill(Color.WHITE);
              graphicsContext.fillRect(0,0,431,454);
              myCollection.stream().filter(e->!e.getThing().equals(element.getThing())).forEach(e->makeObject(e));
              makeObject(element);
          }),
          new KeyFrame(Duration.seconds(1.5),event -> {
              graphicsContext.setFill(Color.WHITE);
              graphicsContext.fillRect(0,0,431,454);
              myCollection.stream().filter(e->!e.getThing().equals(element.getThing())).forEach(e->makeObject(e));
              makeChestOpened(element);
          }),
          new KeyFrame(Duration.seconds(2.5), event -> {
              makeDiamond(element);
          }),
          new KeyFrame(Duration.seconds(3.5),event -> {
              makeShine(element);
          }),
          new KeyFrame(Duration.seconds(4.5), event -> {
              graphicsContext.setFill(Color.WHITE);
              graphicsContext.fillRect(0,0,431,454);
              myCollection.stream().filter(e->!e.getThing().equals(element.getThing())).forEach(e->makeObject(e));
              makeDiamond(element);
              makeChestOpened(element);
          }),
          new KeyFrame(Duration.seconds(5.5),event -> {
               graphicsContext.setFill(Color.WHITE);
               graphicsContext.fillRect(0,0,431,454);
               myCollection.stream().filter(e->!e.getThing().equals(element.getThing())).forEach(e->makeObject(e));
               makeChestOpened(element);
          }),
          new KeyFrame(Duration.seconds(6.5),event -> {
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.fillRect(0,0,431,454);
            myCollection.stream().filter(e->!e.getThing().equals(element.getThing())).forEach(e->makeObject(e));
            makeObject(element);
          })
        );
        timeline.play();
    }
    public void getOwnerAndPaint(){
        myCollection.forEach(e->makeObject(e));
    }
    private Color getColour(String name){
        int a=name.hashCode()/10000000;
        int b=(name+"salt").hashCode()/10000000;
        Color color=Color.rgb(name.length()*20>255?255:name.length()*20,Math.abs(a)*2>255?165:Math.abs(a),Math.abs(b)*2>255?255:Math.abs(b));
        return color;
    }
    private void makeFilters(){
        try {
            List<InteractWithThings> list = myCollection;
            updateTable();
            if (nameFilter.getText().length() != 0) {
                ObservableList<InteractWithThings> collForTable = FXCollections.observableArrayList(list);
                filterByName(nameFilter.getText(), collForTable);
                list = table.getItems();
            }
            if (!typeFilter.getValue().equals(MainController.resourceBundle.getString("All"))) {
                if (typeFilter.getValue().equals(MainController.resourceBundle.getString("CakeLayer")))
                    typeFilterValue = "CakeLayer";
                if (typeFilter.getValue().equals(MainController.resourceBundle.getString("Potato")))
                    typeFilterValue = "Potato";
                if (typeFilter.getValue().equals(MainController.resourceBundle.getString("Soup")))
                    typeFilterValue = "Soup";
                if (typeFilter.getValue().equals(MainController.resourceBundle.getString("Pants")))
                    typeFilterValue = "Pants";
                if (typeFilter.getValue().equals(MainController.resourceBundle.getString("Socks")))
                    typeFilterValue = "Socks";
                if (typeFilter.getValue().equals(MainController.resourceBundle.getString("Window")))
                    typeFilterValue = "Window";
                if (typeFilter.getValue().equals(MainController.resourceBundle.getString("Door")))
                    typeFilterValue = "Door";
                ObservableList<InteractWithThings> collForTable = FXCollections.observableArrayList(list);
                filterByType(typeFilter.getValue(), collForTable);
                list = table.getItems();
            }else{
                typeFilterValue="All";
            }
            if (!stateFilter.getValue().equals(MainController.resourceBundle.getString("All"))) {
                if (stateFilter.getValue().equals(MainController.resourceBundle.getString("GOOD")))
                    stateFilterValue = "GOOD";
                if (stateFilter.getValue().equals(MainController.resourceBundle.getString("BAD")))
                    stateFilterValue = "BAD";
                ObservableList<InteractWithThings> collForTable = FXCollections.observableArrayList(list);
                filterByState(stateFilter.getValue(), collForTable);
                list = table.getItems();
            }else{
                stateFilterValue="All";
            }
            if (costFilter.getText().length() != 0) {
                ObservableList<InteractWithThings> collForTable = FXCollections.observableArrayList(list);
                int x = 0;
                try {
                    x = Integer.parseInt(costFilter.getText());
                } catch (Exception e) {
                }
                filterByCost(x, collForTable);
                list = table.getItems();
            }
            if (xFilter.getText().length() != 0) {
                ObservableList<InteractWithThings> collForTable = FXCollections.observableArrayList(list);
                int x = 0;
                try {
                    x = Integer.parseInt(xFilter.getText());
                } catch (Exception e) {
                }
                filterByX(x, collForTable);
                list = table.getItems();
            }
            if (yFilter.getText().length() != 0) {
                ObservableList<InteractWithThings> collForTable = FXCollections.observableArrayList(list);
                int x = 0;
                try {
                    x = Integer.parseInt(yFilter.getText());
                } catch (Exception e) {
                }
                filterByY(x, collForTable);
                list = table.getItems();
            }
            if (dateFilter.getText().length() != 0) {
                ObservableList<InteractWithThings> collForTable = FXCollections.observableArrayList(list);
                filterByDate(dateFilter.getText(), collForTable);
                list = table.getItems();
            }
            if (optionalFilter.getText().length() != 0) {
                ObservableList<InteractWithThings> collForTable = FXCollections.observableArrayList(list);
                filterByOptional(optionalFilter.getText(), collForTable);
                list = table.getItems();
            }
        }catch (NullPointerException e){
        }
    }
    private void filterByName(String name, ObservableList<InteractWithThings> collForTable){
        FilteredList<InteractWithThings> filteredList=new FilteredList<>(collForTable);
        filteredList.setPredicate(e-> e.getThing().contains(name));
        table.setItems(filteredList);
    }
    private void filterByType(String type,ObservableList<InteractWithThings>collForTable){
            FilteredList<InteractWithThings> filteredList=new FilteredList<>(collForTable);
            filteredList.setPredicate(e-> e.getType().equals(type));
            table.setItems(filteredList);
    }
    private void filterByState(String state,ObservableList<InteractWithThings>collForTable){
            FilteredList<InteractWithThings> filteredList=new FilteredList<>(collForTable);
            filteredList.setPredicate(e-> e.getState1().equals(state));
            table.setItems(filteredList);
    }
    private void filterByCost(int cost, ObservableList<InteractWithThings> collForTable) {
        FilteredList<InteractWithThings> filteredList = new FilteredList<>(collForTable);
        filteredList.setPredicate(e -> e.getCost() >= cost);
        table.setItems(filteredList);
    }
    private void filterByX(int x,ObservableList<InteractWithThings> collForTable) {
        FilteredList<InteractWithThings> filteredList = new FilteredList<>(collForTable);
        filteredList.setPredicate(e -> e.getLocation().getX() >= x);
        table.setItems(filteredList);
    }
    private void filterByY(int y,ObservableList<InteractWithThings> collForTable) {
        FilteredList<InteractWithThings> filteredList = new FilteredList<>(collForTable);
        filteredList.setPredicate(e -> e.getLocation().getY() >= y);
        table.setItems(filteredList);
    }
    private void filterByDate(String date,ObservableList<InteractWithThings> collForTable) {
        FilteredList<InteractWithThings> filteredList = new FilteredList<>(collForTable);
        filteredList.setPredicate(e -> e.getTime().toLocalTime().toString().contains(date));
        table.setItems(filteredList);
    }
    private void filterByOptional(String optional,ObservableList<InteractWithThings> collForTable) {
        FilteredList<InteractWithThings> filteredList = new FilteredList<>(collForTable);
        filteredList.setPredicate(e -> e.getOptional().contains(optional));
        table.setItems(filteredList);
    }
    private void selectObjectFromTable(){
        InteractWithThings element=table.getSelectionModel().getSelectedItem();
        table.setOnMouseClicked(event -> {
            table.getSelectionModel().clearSelection();
            table.setOnMouseClicked(event1 -> selectObjectFromTable());
        });
        if(element==null)return;
        setInformation(element);
    }
    private void setInformation(InteractWithThings element){
        String type=element.getClass().toString().replace("class sample.Story.","");
        typeChoiceBox.setValue(MainController.resourceBundle.getString(type));
        if(element.getState().toString().contains("GOOD")){
            goodRadioButton.setSelected(true);
        }else{
            badRadioButton.setSelected(true);
        }
        nameText.setText(element.getThing());
        xSlider.setValue(element.getX());
        ySlider.setValue(element.getY());
        costSlider.setValue(element.getCost());
        switch(type){
            case "Pants":
                openedCheckBox.setSelected(((Pants)element).zipper.checkFasteness());
                break;
            case "Window": case "Door":
                openedCheckBox.setSelected(((House)element).getOpenness());
                break;
        }
        locationText.setText(MainController.resourceBundle.getString("location")+": ("+(int)xSlider.getValue()+";"+(int)ySlider.getValue()+")");
        cost.setText(MainController.resourceBundle.getString("cost1")+": "+(int)costSlider.getValue());
    }
    private void loadLocaleChoiceBox(){
        ObservableList<String>list=FXCollections.observableArrayList("RU","PT","LT","CR");
        localeChoiceBox.setItems(list);
        String language=null;
        switch(MainController.resourceBundle.getLocale().getLanguage()){
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
    private void loadTableChoiceBox(){
        String value=tableChoiceBox.getValue();
        ObservableList<String>list=FXCollections.observableArrayList(
       resourceBundle.getString("Table"),"Canvas");
        tableChoiceBox.setItems(list);
        if(value!=null){
            switch(value){
                case "Таблица": case "Mesa": case "Lentelė": case "Tabela":
                    tableChoiceBox.setValue(resourceBundle.getString("Table"));
                    break;
                default:
                    tableChoiceBox.setValue("Canvas");
            }
        }else{
            tableChoiceBox.setValue(resourceBundle.getString("Table"));
        }
    }
    private void loadTypeChoiceBox(){
        ObservableList<String>list=FXCollections.observableArrayList(resourceBundle.getString("CakeLayer"),
                resourceBundle.getString("Potato"),resourceBundle.getString("Soup"),
                resourceBundle.getString("Pants"),resourceBundle.getString("Socks"),
                resourceBundle.getString("Window"),resourceBundle.getString("Door"));
        typeChoiceBox.setItems(list);
        typeChoiceBox.setValue(resourceBundle.getString("CakeLayer"));
    }
    private void loadTypeFilter(){
        ObservableList<String>list=FXCollections.observableArrayList(resourceBundle.getString("All"), resourceBundle.getString("CakeLayer"),
                resourceBundle.getString("Potato"),resourceBundle.getString("Soup"),
                resourceBundle.getString("Pants"),resourceBundle.getString("Socks"),
                resourceBundle.getString("Window"),resourceBundle.getString("Door"));
        typeFilter.setItems(list);
        typeFilter.setValue(resourceBundle.getString(typeFilterValue));
    }
    private void loadStateFilter(){
        ObservableList<String> list=FXCollections.observableArrayList(resourceBundle.getString("All"),
                resourceBundle.getString("GOOD"),
                resourceBundle.getString("BAD"));
        stateFilter.setItems(list);
        stateFilter.setValue(resourceBundle.getString(stateFilterValue));
    }
    public static void setPrimaryStage(Stage primaryStage1) {
        primaryStage = primaryStage1;
    }
    public static void setSending(InetAddress address, int port1){
        serverAddress=address;
        port=port1;
    }
    public static void setLoginAndPassword(String login1,String password1){
        login=login1;
        password=password1;
    }
    private void setTextLanguage(ResourceBundle resourceBundle){
        MainController.setResourceBundle(resourceBundle);
        Locale.setDefault(MainController.resourceBundle.getLocale());
        exitButton.setText(resourceBundle.getString("exitButton"));
        informationButton.setText(resourceBundle.getString("informationButton"));
        addButton.setText(resourceBundle.getString("addButton"));
        add_if_minButton.setText(resourceBundle.getString("add_if_minButton"));
        removeButton.setText(resourceBundle.getString("removeButton"));
        updateButton.setText(resourceBundle.getString("updateButton"));
        importButton.setText(resourceBundle.getString("importButton"));
        nameText.setPromptText(resourceBundle.getString("name"));
        type.setText(resourceBundle.getString("type"));
        state.setText(resourceBundle.getString("state"));
        locationText.setText(resourceBundle.getString("location")+": ("+(int)xSlider.getValue()+";"+(int)ySlider.getValue()+")");
        cost.setText(resourceBundle.getString("cost1")+": "+(int)costSlider.getValue());
        goodRadioButton.setText(resourceBundle.getString("GOOD"));
        badRadioButton.setText(resourceBundle.getString("BAD"));
        openedCheckBox.setText(resourceBundle.getString("Opened"));
        nameColumn.setText(resourceBundle.getString("name"));
        typeColumn.setText(resourceBundle.getString("type"));
        stateColumn.setText(resourceBundle.getString("state"));
        costColumn.setText(resourceBundle.getString("cost2"));
        dateColumn.setText(resourceBundle.getString("date"));
        optionalColumn.setText(resourceBundle.getString("opt"));
        loadStateFilter();
        loadTableChoiceBox();
        loadTypeChoiceBox();
        loadTypeFilter();
        makeFilters();
    }
    private void getMessage(){
        while(true){
            Response response=Client.serverResponse(8192,udpSocket);
            if(response!=null){
                if(response.getMessage().equals(LocMessage.INFO)){
                    String information=(String)response.getResponse();
                    String[]informationArray=information.split(",");
                    String[]languageArray=MainController.resourceBundle.getString("infoText").split(",");
                    informationText.setText(languageArray[0]+"\n"+languageArray[1]
                            +informationArray[0]+
                            "\n"+languageArray[2]+informationArray[1]+"\n"+
                            languageArray[3]+informationArray[2]+"\n");
                }
                if(response.getMessage().equals(LocMessage.IMPORT)){
                    String elementText=(String)response.getResponse();
                    String addText=MainController.resourceBundle.getString("addText");
                    String[]elementArray=elementText.split(",");
                    String addInfo="";
                    for (String element:elementArray) {
                        addInfo=addInfo.concat(element+addText+"\n");
                    }
                    if(elementText.equals(""))addInfo=MainController.resourceBundle.getString("importNot");
                    informationText.setText(addInfo);
                }
                if(response.getMessage().equals(LocMessage.COLLECTION)){
                    long size=myCollection.size();
                    long newSize=0;
                    List<InteractWithThings>list=(List<InteractWithThings>)response.getResponse();
                    if(myCollection.size()==list.size()){
                        for (int i = 0; i < list.size() ; i++) {
                        InteractWithThings element = (InteractWithThings) list.toArray()[i];
                            myCollection.stream().filter(e -> e.getThing().equals(element.getThing()))
                                    .filter(e -> !e.equals(element)).forEach(e -> {
                                myCollection = list;
                                updateTable();
                                makeFilters();
                                graphicsContext.setFill(Color.WHITE);
                                graphicsContext.fillRect(0, 0, 431, 454);
                                myCollection.stream().filter(e1->!e1.getThing().equals(element)).forEach(e1->makeObject(e1));
                                makeAnimation(element);
                            });
                        }
                    }
                    if(myCollection.size()>list.size()){
                        myCollection = list;
                        updateTable();
                        makeFilters();
                        graphicsContext.setFill(Color.WHITE);
                        graphicsContext.fillRect(0, 0, 431, 454);
                        getOwnerAndPaint();
                    }
                    if(myCollection.size()<list.size()) {
                        list.stream().filter(e -> !myCollection.contains(e)).forEach(e->
                        {
                            myCollection.add(e);
                            updateTable();
                            makeFilters();
                            graphicsContext.setFill(Color.WHITE);
                            graphicsContext.fillRect(0, 0, 431, 454);
                            myCollection.stream().filter(e1->!e1.getThing().equals(e.getThing())).forEach(e1->makeObject(e1));
                            makeAnimation(e);
                        });
                    }
                }
                if(response.getMessage().equals(LocMessage.ADD)){
                    informationText.setText(response.getResponse().toString()+MainController.resourceBundle.getString("addText"));
                }
                if(response.getMessage().equals(LocMessage.ADD_NOT)){
                    informationText.setText(response.getResponse().toString()+MainController.resourceBundle.getString("addNotText"));
                }
                if(response.getMessage().equals(LocMessage.REMOVE)){
                    informationText.setText(response.getResponse().toString()+MainController.resourceBundle.getString("removeText"));
                }
                if(response.getMessage().equals(LocMessage.REMOVE_NOT)){
                    informationText.setText(response.getResponse().toString()+MainController.resourceBundle.getString("removeNotText"));
                }
                if(response.getMessage().equals(LocMessage.ADD_IF_MIN_NOT)){
                    informationText.setText(MainController.resourceBundle.getString("add_if_minNotText").replace("Object",response.getResponse().toString()));
                }
                if(response.getMessage().equals(LocMessage.FOREIGN)){
                    informationText.setText(MainController.resourceBundle.getString("foreignText").replace("Object",response.getResponse().toString()));
                }
                if(response.getMessage().equals(LocMessage.UPDATE)){
                    informationText.setText(response.getResponse().toString()+MainController.resourceBundle.getString("update"));
                }
            }
        }
    }
    public static void setResourceBundle(ResourceBundle resourceBundle) {
        MainController.resourceBundle = resourceBundle;
    }

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public static void setMyCollection(List<InteractWithThings> collection){
        MainController.myCollection=collection;
    }
    private static void sort(List<InteractWithThings> myCollection){
        Collections.sort(myCollection, new Comparator<InteractWithThings>() {
            @Override
            public int compare(InteractWithThings o1, InteractWithThings o2) {
                return o1.compareTo(o2);
            }
        });
    }
}