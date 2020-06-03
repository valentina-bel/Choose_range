import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

public class Server {
    private ServerSocket server;

    public Server(int portNumber) throws IOException {
        this.server = new ServerSocket(portNumber);
    }

    public int getPort() {
        return server.getLocalPort();
    }

    public void sendMessage(String message) {  // метод класса Server, отправляет строку клиенту
        try (Socket socket = server.accept();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readMessage () { // метод класса Server, получает строку от клиенту
        String response = "";
        try (Socket socket = server.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void readArrayClient(ArrayList<String> list, int responseQuantity) {
        for(int i = 0; i < responseQuantity; i++) {
            try (Socket socket = server.accept();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                list.add(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeArrayServer(ArrayList<String> list) {
        ListIterator<String> listIterator = list.listIterator();
        for(int i = 0; i < list.size(); i++) {
            try (Socket socket = server.accept();
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                if(listIterator.hasNext()) {
                    writer.write(listIterator.next());
                    writer.newLine();
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
