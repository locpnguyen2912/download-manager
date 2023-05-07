package com.dm;

import com.dm.dto.FileInfo;
import com.dm.dto.FileList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * <p>The main app class to interact as the CLI with the user.</p>
 */
public class App {

    private static final String APP_TITLE =
            "==================================\n" +
                    "         Download Manager         \n" +
                    "==================================\n";

    private static final String COMMAND_NOT_RECOGNISED_ERR = "Command not recognised.";
    private static final String PROMPT = "$";

    /**
     * <p>The Scanner to ask for user input.</p>
     */
    private Scanner input;

    /**
     * <p>Constructor.</p>
     */
    private App() {
        this.input = new Scanner(System.in);
    }

    /**
     * <p>Checks if the Menu contains a particular choice.</p>
     *
     * @param choice the Menu selection.
     * @return true if the choice is in the Menu enum, false otherwise.
     */
    private boolean menuContains(String choice) {
        for (Menu m : Menu.values()) {
            if (m.getMenuItemName().equals(choice)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Prints the welcome message.</p>
     */
    private void welcomeMessage() {
        System.out.printf("%s\n", APP_TITLE);
    }

    /**
     * <p>Clears the console by sending ANSI escape codes.</p>
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * <p>Prints the menu to the console.</p>
     */
    private void printMenu() {
        welcomeMessage();
        Arrays.asList(Menu.values()).forEach(item -> System.out.printf("%-5s- %s\n", item.getMenuItemName(), item.getMenuItemDesc()));
    }

    /**
     * <p>Prints the menu.</p>
     */
    private void help() {
        clearScreen();
        printMenu();
    }

    /**
     * <p>Prints all available files on server.</p>
     */
    private void list() {
        FileServerHttpClient client = new FileServerHttpClient();
        FileList fileList = client.index();
        // Print message to say there's no file found
        if (fileList.getFileInfo().isEmpty()) {
            System.out.println("No file found!");
        }
        // If there are files in the server then print them.
        else {
            for (FileInfo fileInfo : fileList.getFileInfo()) {
                System.out.printf("%-4s %s\n", ("[" + fileInfo.getFilePath() + "]"), fileInfo.getSize());
            }
        }
    }

    /**
     * <p></p>
     */
    private void get(List<String> args) {
        FileServerHttpClient client = new FileServerHttpClient();
        client.parallelDownload(args);
        Notification notification = new Notification();
        notification.notification();
    }


    /**
     * <p>Handles input from the user.</p>
     *
     * @throws IllegalArgumentException if user inputs an invalid command.
     */
    private void menuHandler() throws IllegalArgumentException {
        System.out.printf("\n%s ", PROMPT);
        String[] userInput = this.input.nextLine().trim().split("\\s+");

        // Make sure nothing is going on with the split array.
        if (userInput.length < 1) {
            return;
        }
        String command = userInput[0];

        // Check if user wants to exit.
        if (command.equals(Menu.EXIT.getMenuItemName())) {
            System.out.println("Bye!");
            System.exit(0);
        }

        // Check for valid command.
        if (!menuContains(command)) {
            throw new IllegalArgumentException();
        }

        // Get any args the user has passed in
        List<String> userArgs = new ArrayList<>();
        if (userInput.length > 1) {
            userArgs.addAll(Arrays.asList(userInput).subList(1, userInput.length));
        }

        // Unfortunately cannot use a switch statement to check what user wants to do
        // because enum does not produce a constant expression which the switch needs.

        // HELP
        if (command.equals(Menu.HELP.getMenuItemName())) {
            help();
            return;
        }


        // LIST
        if (command.equals(Menu.LIST.getMenuItemName())) {
            list();
        }
        // ADD
        else if (command.equals(Menu.GET.getMenuItemName())) {
            get(userArgs);
        }
    }

    public static void main(String[] args) {
        App app = new App();
        app.clearScreen();
        app.printMenu();

        while (true) {
            try {
                app.menuHandler();
            } catch (Exception e) {
                System.err.println(COMMAND_NOT_RECOGNISED_ERR);
            }
        }
    }
}
