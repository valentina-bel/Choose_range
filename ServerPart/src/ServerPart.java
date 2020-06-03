import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ListIterator;

public class ServerPart {

    public static void main(String[] args){
        try{
            Server server = new Server(9000);
            System.out.println("Сервер начал работу!" + "\tпорт: " + server.getPort());
            authorization(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void authorization(Server server) {
        ArrayList<String> user = new ArrayList<>();
        server.readArrayClient(user, 2);
        User testUser = new User();
        testUser.setLogin(user.get(0));
        testUser.setPassword(user.get(1));
        Pair<Integer, String> result = checkUser(testUser);
        String message = "";
        switch(result.getFirst()) {
            case -1 :
                server.sendMessage("Не верно введены данные!!!");
                authorization(server);
                break;
            case 0:
                server.sendMessage("user");
                message = result.getSecond();
                server.sendMessage(message);
                userMenu(server);
                break;
            case 1:
                server.sendMessage("admin");
                message = result.getSecond();
                server.sendMessage(message);
                adminMenu(server);
                break;
        }
    }

    public static Pair<Integer, String> checkUser(User testUser) {
        ArrayList<User> users = new ArrayList<User>(readFileUsers());
        int flag = -1;
        for(User temp : users) {
            if(temp.equals(testUser))
            {
                testUser = temp;
                if (testUser.getRole().equals("admin"))
                    flag = 1;
                else
                    flag = 0;
                break;
            }
        }
        String userFullName = testUser.getLastName() + ' ' + testUser.getFirstName();
        Pair<Integer, String> pair= new Pair<>(flag, userFullName);
        return pair;
    }

    public static ArrayList<User> readFileUsers() {
        ArrayList<User> users = new ArrayList<>();
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("D://choose_range//users.txt")));
            while((line = reader.readLine()) != null) {
                users.add(new User(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static ArrayList<GroupDetails> readFileDetails() {
        ArrayList<GroupDetails> details = new ArrayList<>();
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("D://choose_range//details.txt")));
            while((line = reader.readLine()) != null) {
                details.add(new GroupDetails(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return details;
    }

    public static void userMenu(Server server) {
        while (true) {
            int choice = Integer.parseInt(server.readMessage()); /// читаем пункт меню
            switch (choice) {
                case 1:
                    sendDetails(server);
                    break;
                case 2:
                    addGroupDetails(server);
                    break;
                case 3:
                    deleteGroupDetails(server);
                    break;
                case 4:
                    editGroupDetails(server);
                    break;
                case 5:
                    chooseSolution(server);
                    break;
                case 6:
                    authorization(server);
                    break;
            }
        }
    }

    public static void sendDetails(Server server) {
        ArrayList<GroupDetails> details = new ArrayList<>(readFileDetails());
        String response = server.readMessage(); // способ сортировки
        if(response.equals("2")){
            Collections.sort(details);
        }
        ArrayList<String> detailsStrings = new ArrayList<String>();
        ListIterator<GroupDetails> detIter = details.listIterator();
        while(detIter.hasNext()) {
            detailsStrings.add(detIter.next().toString());
        }
        server.sendMessage(Integer.toString(detailsStrings.size()));
        server.writeArrayServer(detailsStrings);
    }

    public static int getMaxIdDetails() {
        ArrayList<GroupDetails> details = new ArrayList<>(readFileDetails());
        int temp = 0, maxId = 0;
        if (!details.isEmpty()) {
            ListIterator<GroupDetails> iter = details.listIterator();
            while (iter.hasNext()) {
                temp = iter.next().getGroupId();
                if (temp > maxId)
                    maxId = temp;
            }
        }
        return maxId;
    }

    public static void addGroupDetails(Server server) {
        server.sendMessage(Integer.toString(getMaxIdDetails()));
        String newGroup = server.readMessage();
        GroupDetails newGroupDetails = new GroupDetails(newGroup);
        ArrayList<GroupDetails> details = new ArrayList<>(readFileDetails());
        ListIterator<GroupDetails> detailsIter = details.listIterator();
        int flag = 1;
        while(detailsIter.hasNext()) {
            if(newGroupDetails.equals(detailsIter.next())) {
                server.sendMessage("Такая группа деталей уже есть!");
                flag = 0;
            }
        }
        if (flag == 1) {
            writeFile(newGroup, "D://choose_range", "details.txt");
            server.sendMessage("Группа добавлена в файл");
        }
        if (server.readMessage().equals("1"))
            addGroupDetails(server);
    }

    public static void writeFile(String str, String path, String fileName) {
        File file = new File(path, fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            String line;
            if ((line = br.readLine()) != null)
                bufferWriter.newLine();
            bufferWriter.write(str);
            bufferWriter.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteGroupDetails(Server server) {
        sendDetails(server);
        ArrayList<GroupDetails> details = new ArrayList<>(readFileDetails());
        if (details.isEmpty())
            return;
        ListIterator<GroupDetails> iter;
        int id;
        while(true) {
            id = Integer.parseInt(server.readMessage());
            iter = details.listIterator();
            int flag = 0;
            while(iter.hasNext()) {
                if (iter.next().getGroupId() == id) {
                    server.sendMessage("exists");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0)
                server.sendMessage("no such id");
            else {
                break;
            }
        }
        String response = server.readMessage();
        if (response.equals("1")) {
            iter.previous();
            iter.remove();
            iter = details.listIterator();
            ArrayList<String> detailsString = new ArrayList<>();
            while(iter.hasNext()) {
                detailsString.add(iter.next().stringFile());
            }
            rewriteFile(detailsString,"D://choose_range", "details.txt");
        }
    }

    public static void rewriteFile(ArrayList<String> arrayList, String path, String fileName) {
        File file = new File(path, fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter writer = new FileWriter(file, false);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            String line;
            if (!arrayList.isEmpty()) {
                if ((line = br.readLine()) != null)
                    bufferWriter.newLine();
                int i = 0;
                for (; i < arrayList.size() - 1; i++) {
                    bufferWriter.write(arrayList.get(i));
                    bufferWriter.newLine();
                }
                bufferWriter.write(arrayList.get(arrayList.size() - 1));
            }
            bufferWriter.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editGroupDetails(Server server) {
        sendDetails(server);
        ArrayList<GroupDetails> details = new ArrayList<>(readFileDetails());
        if (details.isEmpty())
            return;
        ListIterator<GroupDetails> iter;
        int id;
        while(true) {
            id = Integer.parseInt(server.readMessage());
            iter = details.listIterator();
            int flag = 0;
            while(iter.hasNext()) {
                if (iter.next().getGroupId() == id) {
                    server.sendMessage("exists");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0)
                server.sendMessage("no such id");
            else {
                break;
            }
        }
        iter.previous();
        GroupDetails temp = iter.next();
        iter.previous();
        String machine, engine, machineManufacturer, full;
        int item = Integer.parseInt(server.readMessage());
        switch (item) {
            case 1:
                machine = server.readMessage();
                temp.setMachine(machine);
                iter.set(temp);
                break;
            case 2:
                engine = server.readMessage();
                temp.setEngine(engine);
                iter.set(temp);
                break;
            case 3:
                machineManufacturer = server.readMessage();
                temp.setMachineManufacturer(machineManufacturer);
                iter.set(temp);
                break;
            case 4:
                full = server.readMessage();
                String [] attributes = full.split("\\|");
                for(int i = 0; i < attributes.length; i++) {
                    attributes[i] = attributes[i].trim();
                }
                machine = attributes[0];
                engine = attributes[1];
                machineManufacturer = attributes[2];
                temp.setMachine(machine);
                temp.setEngine(engine);
                temp.setMachineManufacturer(machineManufacturer);
                break;
        }
        String response = server.readMessage();
        if (response.equals("1")) {
            iter = details.listIterator();
            ArrayList<String> detailsString = new ArrayList<>();
            while(iter.hasNext()) {
                detailsString.add(iter.next().stringFile());
            }
            rewriteFile(detailsString, "D://choose_range","details.txt");
        }
    }

    public static void chooseSolution(Server server) {
        sendDetails(server);
        ArrayList<GroupDetails> details = new ArrayList<>(readFileDetails());
        if (details.isEmpty())
            return;
        ListIterator<GroupDetails> iter;
        int quantityGroups, quantityVariants = Integer.parseInt(server.readMessage());
        server.sendMessage(Integer.toString(details.size())); //кол-во групп
        for (int i = 0; i < quantityVariants; i++) {
            quantityGroups = Integer.parseInt(server.readMessage());
            for (int j = 0 ; j < quantityGroups; j++ ) {
                int id;
                while (true) {
                    id = Integer.parseInt(server.readMessage());
                    iter = details.listIterator();
                    int flag = 0;
                    while (iter.hasNext()) {
                        if (iter.next().getGroupId() == id) {
                            server.sendMessage("exists");
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0)
                        server.sendMessage("no such id");
                    else {
                        String response = server.readMessage();
                        if (!response.equals("error: the same id"))
                            break;
                    }
                }
            }
        }
        ArrayList<String> variants = new ArrayList<>();
        server.readArrayClient(variants, quantityVariants);
        String[] rows = new String[quantityVariants];
        for (int i = 0; i < quantityVariants; i++) {
            rows[i] = server.readMessage();
        }
        Human user = new Human();
        Human expert = new Human();
        Solution solution = new Solution(variants, makeMatrix(rows), user, expert);
        solution.findBestVariants();
        ArrayList<String> bestVariants = solution.getBestVariants();
        server.sendMessage(Integer.toString(bestVariants.size()));
        server.writeArrayServer(bestVariants);
        makeReport(server, solution);
    }

    public static float[][] makeMatrix(String[] rows) {
        float [][] matrix = new float[rows.length][rows.length];
        for (int i = 0; i < rows.length; i++) {
            String[] marks = rows[i].split("\\|");
            for(int j =0; j < rows.length ;j++) {
                matrix[i][j] = Float.parseFloat(marks[j]);
            }
        }
        return matrix;
    }

    public static void makeReport(Server server, Solution solution) { // формирование отчета
        int choice = Integer.parseInt(server.readMessage());
        if (choice == 1) {
            solution.setUser(server.readMessage());
            solution.setExpert(server.readMessage());
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm"); // формируем формат даты и времени
            Date date = new Date(); // получаем дату и время
            String dateTime = dateFormat.format(date);  // получаем строку даты и времени в нужном формате
            String name = "report_" + dateTime + ".txt";  // формируем название отчета на основе даты и времени
            ArrayList<String> report = solution.createReport(dateTime); // получаем массив строк, которые нужно записать в отчёт
            rewriteFile(report,"D://reports_SAIPIS", name); // записываем информацию в отчёт
            server.sendMessage("D://reports_SAIPIS"); // отправляем клиенту путь к отчёту
            server.sendMessage(name);  // отправляем клиенту название отчёта
        }
    }

    public static void adminMenu(Server server) {
        while (true) {
            int choice = Integer.parseInt(server.readMessage()); /// читаем пункт меню
            switch (choice) {
                case 1:
                    sendDetails(server);
                    break;
                case 2:
                    addGroupDetails(server);
                    break;
                case 3:
                    deleteGroupDetails(server);
                    break;
                case 4:
                    editGroupDetails(server);
                    break;
                case 5:
                    chooseSolution(server);
                    break;
                case 6:
                    controlUsers(server);
                    break;
                case 7:
                    authorization(server);
                    break;
            }
        }
    }

    public static void controlUsers(Server server) {
        while (true) {
            int choice = Integer.parseInt(server.readMessage());
            switch (choice) {
                case 1:
                    sendUsers(server);
                    break;
                case 2:
                    addUser(server);
                    break;
                case 3:
                    editUser(server);
                    break;
                case 4:
                    deleteUser(server);
                    break;
                case 5:
                    return;
            }
        }
    }

    public static void sendUsers(Server server) {
        ArrayList<User> users = new ArrayList<>(readFileUsers());
        ArrayList<String> usersStrings = new ArrayList<String>();
        ListIterator<User> usersIter = users.listIterator();
        while(usersIter.hasNext()) {
            usersStrings.add(usersIter.next().toString());
        }
        server.sendMessage(Integer.toString(usersStrings.size()));
        server.writeArrayServer(usersStrings);
    }

    public static String encrypt(String str) {
        char[] charArray = str.toCharArray();
        int key = 3;
        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) ((int)charArray[i] + key);
        }
        return (new String(charArray));
    }

    public static String decrypt(String str) {
        char[] charArray = str.toCharArray();
        int key = 3;
        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) ((int)charArray[i] - key);
        }
        return (new String(charArray));
    }

    public static void addUser(Server server) {
        String password = server.readMessage();
        password = encrypt(password);
        server.sendMessage(password);
        server.sendMessage(Integer.toString(getMaxIdUsers()));
        String newUserString = server.readMessage();
        User newUser = new User(newUserString);
        ArrayList<User> users = new ArrayList<>(readFileUsers());
        ListIterator<User> usersIter = users.listIterator();
        int flag = 1;
        while(usersIter.hasNext()) {
            if(newUser.equals(usersIter.next())) {
                server.sendMessage("Такой пользователь уже есть!");
                flag = 0;
            }
        }
        if (flag == 1) {
            writeFile(newUserString, "D://choose_range","users.txt");
            server.sendMessage("Пользователь добавлена в файл");
        }
        if (server.readMessage().equals("1"))
            addUser(server);
    }

    public static int getMaxIdUsers() {
        ArrayList<User> users = new ArrayList<>(readFileUsers());
        int temp = 0, maxId = 0;
        if (!users.isEmpty()) {
            ListIterator<User> iter = users.listIterator();
            while (iter.hasNext()) {
                temp = iter.next().getUserId();
                if (temp > maxId)
                    maxId = temp;
            }
        }
        return maxId;
    }

    public static void deleteUser(Server server) {
        sendUsers(server);
        ArrayList<User> users = new ArrayList<>(readFileUsers());
        if (users.size() == 1)
            return;
        ListIterator<User> iter;
        int id;
        while(true) {
            id = Integer.parseInt(server.readMessage());
            iter = users.listIterator();
            int flag = 0;
            while(iter.hasNext()) {
                if (iter.next().getUserId() == id) {
                    server.sendMessage("exists");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0)
                server.sendMessage("no such id");
            else {
                break;
            }
        }
        String response = server.readMessage();
        if (response.equals("1")) {
            iter.previous();
            iter.remove();
            iter = users.listIterator();
            ArrayList<String> usersString = new ArrayList<>();
            while(iter.hasNext()) {
                usersString.add(iter.next().stringFile());
            }
            rewriteFile(usersString, "D://choose_range","users.txt");
        }
    }

    public static void editUser(Server server) {
        sendUsers(server);
        ArrayList<User> users = new ArrayList<>(readFileUsers());
        if (users.size() == 1)
            return;
        ListIterator<User> iter;
        int id;
        while(true) {
            id = Integer.parseInt(server.readMessage());
            iter = users.listIterator();
            int flag = 0;
            while(iter.hasNext()) {
                if (iter.next().getUserId() == id) {
                    server.sendMessage("exists");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0)
                server.sendMessage("no such id");
            else {
                break;
            }
        }
        iter.previous();
        User temp = iter.next();
        iter.previous();
        String lastName, firstNamer, full;
        int item = Integer.parseInt(server.readMessage());
        switch (item) {
            case 1:
                lastName = server.readMessage();
                temp.setLastName(lastName);
                iter.set(temp);
                break;
            case 2:
                firstNamer = server.readMessage();
                temp.setFirstName(firstNamer);
                iter.set(temp);
                break;
            case 3:
                full = server.readMessage();
                String [] attributes = full.split("\\|");
                for(int i = 0; i < attributes.length; i++) {
                    attributes[i] = attributes[i].trim();
                }
                lastName = attributes[0];
                firstNamer = attributes[1];
                temp.setLastName(lastName);
                temp.setFirstName(firstNamer);
                break;
        }
        String response = server.readMessage();
        if (response.equals("1")) {
            iter = users.listIterator();
            ArrayList<String> usersString = new ArrayList<>();
            while(iter.hasNext()) {
                usersString.add(iter.next().stringFile());
            }
            rewriteFile(usersString, "D://choose_range","users.txt");
        }
    }
}