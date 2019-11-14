package sample;
import sample.Story.InteractWithThings;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Client{
    private DatagramSocket udpSocket;
    private InetAddress serverAddress;
    private int port;
    private InteractWithThings element;
    private String login="";
    private String password="";
    public Client(String address, int port) throws IOException {
        this.serverAddress=InetAddress.getByName(address);
        this.port=port;
        this.udpSocket=new DatagramSocket();
    }
    public static void showUsage(){
        System.out.println("Чтобы подключиться к серверу и запустить интерактивный режим, введите адрес и порт сервера.");
        System.out.println("java18 - jar Client.jar <host> <port>");
        System.exit(1);
    }
    public void testConnection() throws IOException{
        System.out.println("Идёт попытка установить соединение.");
        boolean connection=false;
        udpSocket.setSoTimeout(1000);
        byte[] buffer =new byte[256];
        DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length);
        LocMessage message=LocMessage.COLLECTION;
        for(int i=1;i<11;i++){
            System.out.println("Очередая попытка... (Номер "+i+", если быть точным)");
            sendDatagramPacket(createDatagramPacket(new Command("connecting"),serverAddress,port),udpSocket);
            try {
                udpSocket.receive(datagramPacket);
            }catch (SocketTimeoutException e){
                continue;
            }
            try(ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buffer))){
                Response response = (Response) objectInputStream.readObject();
                message=response.getMessage();
            }catch (IOException | ClassNotFoundException e) {
                System.out.println("Упс...");
            }
            if(message.equals(LocMessage.CONNECTED)){
                connection=true;
                break;
            }
        }
        if(!connection){
            System.out.println("Соединение не устнановлено.");
            System.exit(1);
        }
    }
    public void exit(){
        System.out.println("До встречи, до лучших времён -_-");
        System.exit(0);
    }
    public static DatagramPacket createDatagramPacket(Command command,InetAddress serverAddress,int port){
        byte[] sending;
        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        try(ObjectOutputStream objectOutputStream=new ObjectOutputStream(byteArrayOutputStream)){
            objectOutputStream.writeObject(command);
            objectOutputStream.flush();
            sending = byteArrayOutputStream.toByteArray();
            return new DatagramPacket(sending,sending.length, serverAddress, port);
        }
        catch (IOException e){
            //e.printStackTrace();
            System.out.println("Не удалось создать поток и записать объект.");
            return null;
        }
    }
    public static boolean sendDatagramPacket(DatagramPacket datagramPacket,DatagramSocket udpSocket){
        boolean success=false;
        if(datagramPacket!=null) {
            try {
                udpSocket.send(datagramPacket);
                success=true;
            } catch (IOException e) {
                System.out.println("Не удалось послать пакет.");
            }
        }
        return success;
    }
    public static Response serverResponse(int size,DatagramSocket udpSocket){
        byte[] buffer =new byte[size];
        DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length);
        try{
            udpSocket.receive(datagramPacket);
        } catch (IOException e) {
            System.out.println("Не удалось получить пакет.");
        }
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buffer))){
            return (Response)objectInputStream.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("Не удалось получить ответ сервера.");
            return null;
        }
    }

    public int getPort() {
        return port;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }
}
