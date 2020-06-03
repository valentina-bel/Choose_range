import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
       authorization();
    }

    public static void authorization() {
        System.out.println("Авторизация\n\nВведите логин: ");
        Scanner in = new Scanner(System.in);
        String login = in.nextLine();
        System.out.println("Введите пароль: ");
        Console console = System.console();
        char[] pass = console.readPassword();
        String password = new String(pass);
        ArrayList<String> user = new ArrayList<>();
        user.add(login);
        user.add(password);
        writeArrayServer(user);
        String response = readMessage();
        switch(response) {
            case "Не верно введены данные!!!" :
                System.out.println(response);
                authorization();
                break;
            case "user":
                String userFullName = readMessage();
                userMenu(userFullName);
                break;
            case "admin":
                String adminFullName = readMessage();
                adminMenu(adminFullName);
                break;
        }
    }

    public static String readMessage () {
        String response = "";
        try (Socket socket = new Socket("127.0.0.1", 9000);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static void sendMessage(String message) {
        try (Socket socket = new Socket("127.0.0.1", 9000);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeArrayServer (ArrayList<String> list) {
        ListIterator<String> listIterator = list.listIterator();
        for(int i = 0; i < list.size(); i++) {
            try (Socket socket = new Socket("127.0.0.1", 9000);
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

    public static void readArrayServer (ArrayList<String> list, int responseQuantity) {

        for(int i = 0; i < responseQuantity; i++) {
            try (Socket socket = new Socket("127.0.0.1", 9000);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                list.add(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void userMenu(String userFullName) {
        while(true) {
            cls();
            printUserMenu();
            System.out.println("");
            int choice = 0;
            while (true) {
                try {
                    choice = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 6");
                    if (choice < 1 || choice > 6) {
                        throw new InputIsOutOfBounds("Нужно ввести целое число от 1 до 6");
                    } else
                        break;
                } catch (InputIsOutOfBounds ex) {
                    System.out.println(ex.getMessage());
                }
            }
            sendMessage(Integer.toString(choice));
            switch (choice) {
                case 1:
                    printDetails();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    addGroupDetails();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    deleteGroupDetails();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    editGroupDetails();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    chooseSolution(userFullName);
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    System.exit(0);
                    break;
            }
        }
    }

    public static void printUserMenu() {
        System.out.println("Меню пользователя:\n\n1. Вывести на экран информацию о группах деталей");
        System.out.println("2. Добавить группу деталей\n3. Удалить группу деталей");
        System.out.println("4. Редактировать группу деталей\n5. Выбрать ассортимент");
        System.out.println("6. Выйти из программы");
    }

    public static void cls() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println(E);
        }
    }

    private  static int checkInt(String inputMessage, String errorMessage) {
        Scanner in = new Scanner(System.in);
        int input = 0;
        while (true) {
            System.out.println(inputMessage);
            if (in.hasNextInt()) {
                input = in.nextInt();
                break;
            }
            else {
                System.out.println(errorMessage);
                in.nextLine();
            }
        }
        return input;
    }

    public static int printDetails() {
        cls();
        System.out.println("\nМеню вывода\n1. Вывести группы деталей по возрастанию id");
        System.out.println("2. Вывести группы деталей по транспортному средству(по алфавиту)");
        System.out.println("");
        int choice = 0;
        while (true) {
            try {
                choice = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 2");
                if (choice < 1 || choice > 2) {
                    throw new InputIsOutOfBounds("Нужно ввести целое число от 1 до 2");
                } else
                    break;
            } catch (InputIsOutOfBounds ex) {
                System.out.println(ex.getMessage());
            }
        }
        sendMessage(Integer.toString(choice));
        int size = Integer.parseInt(readMessage());
        if(size != 0) {
            ArrayList<String> details = new ArrayList<>();
            readArrayServer(details, size);
            String str = "";
            for (int i = 0; i < 76; i++)
                str = str + '-';
            System.out.println(str);
            System.out.printf("|%3s|%21s|%13s|%34s|\n", "id", "Транспортное средство", "Тип двигателя", "Изготовитель транспртного средства");
            System.out.println(str);
            for (String det : details) {
                System.out.println(det);
            }
            System.out.println(str);
            return 1;
        }
        else {
            System.out.println("Нет информации о группах деталей");
            return 0;
        }
    }

    public static void addGroupDetails() {
        cls();
        Scanner in = new Scanner(System.in);
        System.out.println("Введите название транспортного средства:");
        String machine = in.nextLine();
        System.out.println("Введите тип двигателя:");
        String engine = in.nextLine();
        System.out.println("Введите изготовителя транспортного средства:");
        String machineManufacturer = in.nextLine();
        int id = Integer.parseInt(readMessage()) + 1;
        String groupDetails = String.format("%d|%s|%s|%s", id, machine, engine, machineManufacturer);
        sendMessage(groupDetails);
        System.out.println(readMessage());
        System.out.println("\nХотите продолжить: \n1. Добавить еще одну группу деталей");
        System.out.println("2. Вернуться в основное меню");
        int choice = checkInt("Выберите пункт: ", "Нужно ввести целое число 1 или 2");
        while(true) {
            if (choice < 1 || choice > 2) {
                System.out.println("Нужно ввести целое число 1 или 2");
                choice = checkInt("Выберите пункт: ", "Нужно ввести целое число 1 или 2");
            }
            else
                break;
        }
        sendMessage(Integer.toString(choice));
        if (choice == 1)
            addGroupDetails();
    }

    public static void deleteGroupDetails() {
        if (printDetails() == 0) {
            return;
        }
        System.out.println("");
        int id = checkInt("Введите id группы, которую вы хотите удалить: ", "Нужно ввести существующее id!");
        sendMessage(Integer.toString(id));
        String exists = readMessage();
        while(true) {
            if (exists.equals("no such id")) {
                System.out.println("Нужно ввести существующее id!");
                id = checkInt("Введите id группы, которую вы хотите удалить: ", "Нужно ввести существующее id!");
                sendMessage(Integer.toString(id));
                exists = readMessage();
            }
            else
                break;
        }
        System.out.println("\nВы действительно хотите удалить эту группу деталей: \n1. Да\n2. Нет");
        System.out.println("");
        int choice = 0;
        while (true) {
            try {
                choice = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 2");
                if (choice < 1 || choice > 2) {
                    throw new InputIsOutOfBounds("Нужно ввести целое число от 1 до 2");
                } else
                    break;
            } catch (InputIsOutOfBounds ex) {
                System.out.println(ex.getMessage());
            }
        }
        sendMessage(Integer.toString(choice));
        if (choice == 1)
            System.out.println("Группа деталей удалена!");
        else
            System.out.println("Удаление отменено!");
    }

    public static void editGroupDetails() {
        if (printDetails() == 0) {
            return;
        }
        System.out.println("");
        int id = checkInt("Введите id группы, которую вы хотите изменить: ", "Нужно ввести существующее id!");
        sendMessage(Integer.toString(id));
        String exists = readMessage();
        while(true) {
            if (exists.equals("no such id")) {
                System.out.println("Нужно ввести существующее id!");
                id = checkInt("Введите id группы, которую вы хотите удалить: ", "Нужно ввести существующее id!");
                sendMessage(Integer.toString(id));
                exists = readMessage();
            }
            else
                break;
        }
        System.out.println("Выберите, какой пункт вы хотите изменить:\n1. Транспортное средство");
        System.out.println("2. Тип двигателя\n3. Изготовитель\n4. Все пункты сразу");
        System.out.println("");
        int item = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 4");
        while (true) {
            if (item < 1 || item > 4) {
                System.out.println("Нужно ввести целое число от 1 до 4");
                item = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 4");
            } else
                break;
        }
        sendMessage(Integer.toString(item));
        String machine, engine, machineManufacturer;
        Scanner in = new Scanner(System.in);
        switch (item) {
            case 1:
                System.out.println("Введите название транспортного средства:");
                machine = in.nextLine();
                sendMessage(machine);
                break;
            case 2:
                System.out.println("Введите тип двигателя:");
                engine = in.nextLine();
                sendMessage(engine);
                break;
            case 3:
                System.out.println("Введите изготовителя транспортного средства:");
                machineManufacturer = in.nextLine();
                sendMessage(machineManufacturer);
                break;
            case 4:
                System.out.println("Введите название транспортного средства:");
                machine = in.nextLine();
                System.out.println("Введите тип двигателя:");
                engine = in.nextLine();
                System.out.println("Введите изготовителя транспортного средства:");
                machineManufacturer = in.nextLine();
                String groupDetails = String.format("%s|%s|%s", machine, engine, machineManufacturer);
                sendMessage(groupDetails);
                break;
        }
        System.out.println("\nВы действительно хотите изменить эту группу деталей: \n1. Да\n2. Нет");
        int choice = 0;
        while (true) {
            try {
                choice = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 2");
                if (choice < 1 || choice > 2) {
                    throw new InputIsOutOfBounds("Нужно ввести целое число от 1 до 2");
                } else
                    break;
            } catch (InputIsOutOfBounds ex) {
                System.out.println(ex.getMessage());
            }
        }
        sendMessage(Integer.toString(choice));
        if (choice == 1)
            System.out.println("Группа деталей изменена!");
        else
            System.out.println("Изменение отменено!");
    }

    public static void chooseSolution(String userFullName) {
        cls();
        if (printDetails() == 0)
            return;
        System.out.println("");
        int amountVariants = checkInt("Введите количество вариантов для сравнения (не менее 2): ", "Нужно ввести количество вариантов не менее 2!");
        while(true) {
            if (amountVariants < 2) {
                System.out.println("Нужно ввести количество вариантов не менее 2!");
                amountVariants = checkInt("Введите количество вариантов для сравнения (не менее 2): ", "Нужно ввести количество вариантов не менее 2!");
            }
            else
                break;
        }
        String [] arrVariants = new String[amountVariants];
        sendMessage(Integer.toString(amountVariants));
        int quantityGroups = Integer.parseInt(readMessage());
        for (int i =0; i < amountVariants; i++) {
            System.out.println(String.format("Вариант %d", i+1));
            arrVariants[i] = inputVariant(quantityGroups);
        }
        Scanner in = new Scanner(System.in);
        cls();
        System.out.println("Введите фамилию и имя эксперта: ");
        String expert = in.nextLine();
        for(int i =0; i < arrVariants.length; i++) {
            System.out.println(String.format("Вариант %d", i + 1));
            System.out.println("------------------");
            System.out.println(String.format("|%9s|%6s|", "id группы", "%"));
            System.out.println("------------------");
            System.out.println(arrVariants[i]);
            System.out.println("------------------");
        }
        printScale();
        int mark = 0;
        float lessMark = 0f;
        float [][] matrixMarks = new float[arrVariants.length][arrVariants.length];
        System.out.println("");
        for(int i = 0; i < arrVariants.length; i++) {
            matrixMarks[i][i] = 1;
            for (int j = i + 1; j < arrVariants.length; j++) {
                mark = checkInt(String.format("Сравните вариант %d и вариант %d: ", i +1, j + 1), "Нужно ввести целое значение от -5 до 5, не включая 0 (см. шкалу)");
                while(true) {
                    if (mark < -5 || mark > 5 || mark == 0) {
                        System.out.println("Нужно ввести целое значение от -5 до 5, не включая 0 (см. шкалу)");
                        mark = checkInt(String.format("Сравните вариант %d и вариант %d: ", i +1, j + 1), "Нужно ввести целое значение от -5 до 5, не включая 0 (см. шкалу)");
                    }
                    else
                        break;
                }
                if (mark > 0) {
                    matrixMarks[i][j] = mark;
                    matrixMarks[j][i] = (float) 1/mark;
                }
                else {
                    lessMark = 1 / (float)mark * (-1);
                    matrixMarks[i][j] = lessMark;
                    matrixMarks[j][i] = mark * (-1);
                }
            }
        }
        ArrayList<String> var = new ArrayList<>();
        for (int i = 0; i < arrVariants.length; i++) {
            var.add(arrVariants[i].replace('\n', '*'));
        }
        writeArrayServer(var);
        for (int i = 0; i < arrVariants.length; i++) {
            String row = "";
            for (int j = 0; j < arrVariants.length; j++) {
                row = row + matrixMarks[i][j] + '|';
            }
            sendMessage(row);
        }
        int bestQuantity = Integer.parseInt(readMessage());
        ArrayList<String> bestVariants = new ArrayList<>();
        readArrayServer(bestVariants, bestQuantity);
        ListIterator<String> bestIter = bestVariants.listIterator();
        System.out.println("Лучший(е) вариант(ы): ");
        while(bestIter.hasNext()) {
            System.out.println("------------------");
            System.out.println(String.format("|%9s|%6s|", "id группы", "%"));
            System.out.println("------------------");
            System.out.println(bestIter.next().replace('*','\n'));
            System.out.println("------------------");
        }
        makeReport(userFullName, expert);
    }

    public static String inputVariant(int max) {
        int maxQuantity = max;
        int quantity = checkInt("Введите сколько групп деталей будет в варианте ассортимента: ", "Нужно ввести количество групп > 0 и не больше всех групп!");
        while(true) {
            if (quantity < 1 || quantity > maxQuantity) {
                System.out.println("Нужно ввести количество групп > 0 и не больше всех групп!");
                quantity = checkInt("Введите сколько групп деталей будет в ассортименте: ", "Нужно ввести количество групп > 0 и не больше всех групп!");
            }
            else
                break;
        }
        sendMessage(Integer.toString(quantity));
        int id, flag = 0, added = 0;
        float percent = 0f;
        int [] arrIds = new int[quantity];
        float [] arrPercents = new float[quantity];
        float full = 100;
        for(int i = 0; i < quantity; i++) {
            id = checkInt("Введите id группы: ", "Нужно ввести существующее id!");
            sendMessage(Integer.toString(id));
            String exists = readMessage();
            flag = 0;
            while(true) {
                    for (int j = 0; j < i; j++) {
                        if (id == arrIds[j])
                            flag = 1;
                    }
                    if (flag == 1 || exists.equals("no such id")) {
                        if (flag == 1)
                            sendMessage("error: the same id");
                        System.out.println("Нужно ввести существующее id без повтора!");
                        id = checkInt("Введите id группы: ", "Нужно ввести существующее id без повтора!");
                        sendMessage(Integer.toString(id));
                        exists = readMessage();
                        flag = 0;
                    }
                    else {
                        sendMessage("good id");
                        break;
                    }
            }
            while(true) {
                try {
                    percent = checkFloat("Введите % выбранной группы:", "Неверный ввод: ");
                    full = full - percent;
                    if (percent > 0 && full >= 0) {
                        break;
                    }
                    else
                        throw new InputIsOutOfBounds("Сумма процентов всех групп варианта не должна быть более 100%");
                } catch (InputIsOutOfBounds ex) {
                    System.out.println(ex.getMessage());
                    full = full + percent;
                }
            }
                arrIds[i] = id;
                arrPercents[i] = percent;
        }
        String variant = "";
        for(int i = 0; i < arrIds.length - 1; i++) {
            variant = variant + String.format("|%9d|%6.2f|\n", arrIds[i], arrPercents[i]);
        }
        variant = variant + String.format("|%9d|%6.2f|", arrIds[arrIds.length - 1], arrPercents[arrIds.length - 1]);
        return variant;
    }

    public static float checkFloat(String inputMessage, String errorMessage) {
        Scanner in = new Scanner(System.in);
        float input = 0f;
        while (true) {
            System.out.println(inputMessage);
            if (in.hasNextFloat()) {
                input = in.nextFloat();
                break;
            }
            else {
                System.out.println(errorMessage);
                in.nextLine();
            }
        }
        return input;
    }

    public static void printScale() {
        System.out.println("Шкала оценок:");
        System.out.println("Левый и правый вариант имеют одинаковую важность: 1");
        System.out.println("Левый(правый) вариант имеет небольшое превосходство: 2 (-2)");
        System.out.println("Левый(правый) вариант имеет умеренное превосходство: 3 (-3)");
        System.out.println("Левый(правый) вариант сильное превосходство: 4 (-4)");
        System.out.println("Левый(правый) вариант значительное превосходство: 5 (-5)");
    }

    public static void makeReport(String userFullName, String expertFullName) {
        System.out.println("");
        System.out.println("Вы хотите получить отчет по выбору варианта ассортимента:\n1. получить отчет\n2. отчет не нужен ");
        int choice = 0;
        while (true) {
            try {
                choice = checkInt("Выберите пункт меню: ", "Нужно ввести 1 или 2!");
                if (choice < 1 || choice > 2) {
                    throw new InputIsOutOfBounds("Нужно ввести 1 или 2!");
                } else
                    break;
            } catch (InputIsOutOfBounds ex) {
                System.out.println(ex.getMessage());
            }
        }
        sendMessage(Integer.toString(choice));
        if (choice == 1) {
            sendMessage(userFullName);
            sendMessage(expertFullName);
            System.out.println("Путь к отчёту:" + readMessage());
            System.out.println("Название отчёта: " + readMessage());
        }
    }

    public static void printAdminMenu() {
        System.out.println("Меню администратора:\n\n1. Вывести на экран информацию о группах деталей");
        System.out.println("2. Добавить группу деталей\n3. Удалить группу деталей");
        System.out.println("4. Редактировать группу деталей\n5. Выбрать ассортимент");
        System.out.println("6. Работа с учётными записями");
        System.out.println("7. Выйти из программы");
    }

    public static void adminMenu(String adminFullName) {
        while(true) {
            cls();
            printAdminMenu();
            System.out.println("");
            int choice = 0;
            while (true) {
                try {
                    choice = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 7");
                    if (choice < 1 || choice > 7) {
                        throw new InputIsOutOfBounds("Нужно ввести целое число от 1 до 7");
                    } else
                        break;
                } catch (InputIsOutOfBounds ex) {
                    System.out.println(ex.getMessage());
                }
            }
            sendMessage(Integer.toString(choice));
            switch (choice) {
                case 1:
                    printDetails();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    addGroupDetails();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    deleteGroupDetails();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    editGroupDetails();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    chooseSolution(adminFullName);
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    controlUsers();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    System.exit(0);
                    break;
            }
        }
    }

    public static void printControlUsersMenu() {
        System.out.println("Меню работы с учётными записями:\n ");
        System.out.println("1. Вывести учетные записи на экран");
        System.out.println("2. Добавить пользователя");
        System.out.println("3. Редактировать пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println("5. Вернуться в основное меню");
    }

    public static void controlUsers() {
        while (true) {
            cls();
            printControlUsersMenu();
            System.out.println("");
            int choice = 0;
            while (true) {
                try {
                    choice = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 5");
                    if (choice < 1 || choice > 5) {
                        throw new InputIsOutOfBounds("Нужно ввести целое число от 1 до 5");
                    } else
                        break;
                } catch (InputIsOutOfBounds ex) {
                    System.out.println(ex.getMessage());
                }
            }
            sendMessage(Integer.toString(choice));
            switch (choice) {
                case 1:
                    printUsers();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    addUsers();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    editUser();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    deleteUser();
                    System.out.println("Нажмите enter, чтобы вернуться в меню: ");
                    try {
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    return;
            }
        }
    }

    public static int printUsers() {
        cls();
        int size = Integer.parseInt(readMessage());
        ArrayList<String> users = new ArrayList<>();
        readArrayServer(users, size);
        String str = "";
        for (int i = 0; i < 64; i++)
            str = str + '-';
        System.out.println(str);
        System.out.printf("|%3s|%10s|%10s|%10s|%10s|%14s|\n", "id", "Фамилия", "Имя", "Логин", "Пароль","Роль");
        System.out.println(str);
        for (String user : users) {
            System.out.println(user);
        }
        System.out.println(str);
        if (size == 1)
            return 0;
        else
            return 1;
    }

    public static void addUsers() {
        cls();
        Scanner in = new Scanner(System.in);
        System.out.println("Введите фамилию:");
        String lastName = in.nextLine();
        System.out.println("Введите имя:");
        String firstName = in.nextLine();
        System.out.println("Введите логин (не более 10 символов): ");
        String login = in.nextLine();
        while (true) {
            if (login.length() > 10) {
                System.out.println("Введите логин (не более 10 символов):");
                login = in.nextLine();
            } else
                break;
        }
        System.out.println("Введите пароль (не более 10 символов): ");
        String password = in.nextLine();
        while (true) {
            if (password.length() > 10) {
                System.out.println("Введите пароль (не более 10 символов):");
                password = in.nextLine();
            } else
                break;
        }
        sendMessage(password);
        password = readMessage();
        int id = Integer.parseInt(readMessage()) + 1;
        String user = String.format("%s|%s|%d|%s|%s|%s", lastName, firstName, id, login, password, "user");
        sendMessage(user);
        System.out.println(readMessage());
        System.out.println("\nХотите продолжить: \n1. Добавить еще одного пользователя");
        System.out.println("2. Вернуться в меню работы с учётными записями");
        int choice = 0;
        while (true) {
            try {
                choice = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 2");
                if (choice < 1 || choice > 2) {
                    throw new InputIsOutOfBounds("Нужно ввести целое число от 1 до 2");
                } else
                    break;
            } catch (InputIsOutOfBounds ex) {
                System.out.println(ex.getMessage());
            }
        }
        sendMessage(Integer.toString(choice));
        if (choice == 1)
            addUsers();
    }

    public static void deleteUser() {
        if (printUsers() == 0) {
            System.out.println("Нельзя редактировать и удалять администратора"); // ждать ввода клавиши
            return;
        }
        int id = checkInt("Введите id пользователя, которого вы хотите удалить: ", "Нужно ввести существующее id!");
        while (true) {
            if (id < 1) {
                System.out.println("Нужно ввести существующее id, нельзя выбирать администратора!");
                id = checkInt("Выберите пункт меню: ", "Нужно ввести существующее id, нельзя выбирать администратора!");
            } else
                break;
        }
        sendMessage(Integer.toString(id));
        String exists = readMessage();
        while(true) {
            if (exists.equals("no such id")) {
                System.out.println("Нужно ввести существующее id!");
                id = checkInt("Введите id пользователя, которого вы хотите удалить: ", "Нужно ввести существующее id, нельзя выбирать администратора!");
                while (true) {
                    if (id < 1) {
                        System.out.println("Нужно ввести существующее id, нельзя выбирать администратора!");
                        id = checkInt("Введите id пользователя, которого вы хотите удалить: ", "Нужно ввести существующее id, нельзя выбирать администратора!");
                    } else
                        break;
                }
                sendMessage(Integer.toString(id));
                exists = readMessage();
            }
            else
                break;
        }
        System.out.println("\nВы дейтсвительно хотите удалить эту группу деталей: \n1. Да\n2. Нет");
        int choice = 0;
        while (true) {
            try {
                choice = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 2");
                if (choice < 1 || choice > 2) {
                    throw new InputIsOutOfBounds("Нужно ввести целое число от 1 до 2");
                } else
                    break;
            } catch (InputIsOutOfBounds ex) {
                System.out.println(ex.getMessage());
            }
        }
        sendMessage(Integer.toString(choice));
        if (choice == 1)
            System.out.println("Пользователь удален!");
        else
            System.out.println("Удаление отменено!");
    }

    public static void editUser() {
        if (printUsers() == 0) {
            System.out.println("Нельзя редактировать и удалять администратора"); // ждать ввода клавиши
            return;
        }
        int id = checkInt("Введите id пользователя, которого вы хотите изменить: ", "Нужно ввести существующее id!");
        while (true) {
            if (id < 1) {
                System.out.println("Нужно ввести существующее id, нельзя выбирать администратора!");
                id = checkInt("Выберите пункт меню: ", "Нужно ввести существующее id, нельзя выбирать администратора!");
            } else
                break;
        }
        sendMessage(Integer.toString(id));
        String exists = readMessage();
        while(true) {
            if (exists.equals("no such id")) {
                System.out.println("Нужно ввести существующее id!");
                id = checkInt("Введите id пользователя, которого вы хотите изменить: ", "Нужно ввести существующее id, нельзя выбирать администратора!");
                while (true) {
                    if (id < 1) {
                        System.out.println("Нужно ввести существующее id, нельзя выбирать администратора!");
                        id = checkInt("Введите id пользователя, которого вы хотите изменить: ", "Нужно ввести существующее id, нельзя выбирать администратора!");
                    } else
                        break;
                }
                sendMessage(Integer.toString(id));
                exists = readMessage();
            }
            else
                break;
        }
        System.out.println("Выберите, какой пункт вы хотите изменить:\n1. Фамилию");
        System.out.println("2. Имя\n3. Все пункты");
        int item = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 3");
        while (true) {
            if (item < 1 || item > 3) {
                System.out.println("Нужно ввести целое число от 1 до 3");
                item = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 3");
            } else
                break;
        }
        sendMessage(Integer.toString(item));
        String lastName, firstName;
        Scanner in = new Scanner(System.in);
        switch (item) {
            case 1:
                System.out.println("Введите фамилию:");
                lastName = in.nextLine();
                sendMessage(lastName);
                break;
            case 2:
                System.out.println("Введите имя:");
                firstName = in.nextLine();
                sendMessage(firstName);
                break;
            case 3:
                System.out.println("Введите фамилию:");
                lastName = in.nextLine();
                System.out.println("Введите имя:");
                firstName = in.nextLine();
                String fullName = String.format("%s|%s", lastName, firstName);
                sendMessage(fullName);
                break;
        }
        System.out.println("\nВы дейтсвительно хотите изменить эту группу деталей: \n1. Да\n2. Нет");
        int choice = 0;
        while (true) {
            try {
                choice = checkInt("Выберите пункт меню: ", "Нужно ввести целое число от 1 до 2");
                if (choice < 1 || choice > 2) {
                    throw new InputIsOutOfBounds("Нужно ввести целое число от 1 до 2");
                } else
                    break;
            } catch (InputIsOutOfBounds ex) {
                System.out.println(ex.getMessage());
            }
        }
        sendMessage(Integer.toString(choice));
        if (choice == 1)
            System.out.println("Пользователь изменен!");
        else
            System.out.println("Изменение отменено!");
    }
}
