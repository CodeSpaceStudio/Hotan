import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Shell {
    static String currentDirectory;
    private static final String MODEL_FILE = "file";
    private static final String FILE_ACTIONS_CHANGE_DIRECTORY = "$changeDirectory";
    private static final String FILE_ACTIONS_SHOW = "$show";

    private static final String MODEL_SYSTEM = "system";

    private static final String MODEL_WEB = "web";

    public Shell() {
        this.currentDirectory = Paths.get("").toAbsolutePath().toString();
    }

    public static void ExecuteCommand(String command) {
        try {
            String[] cmdParts = command.split(" ");
            final String MODEL = cmdParts[0];
            final String ACTION = cmdParts[1];
            switch (MODEL) {
                case MODEL_FILE:
                    switch (ACTION) {
                        case FILE_ACTIONS_CHANGE_DIRECTORY:
                            changeDirectory(cmdParts, currentDirectory);
                            break;
                        default:
                            System.out.println("Shell: Unknown action in " + MODEL_FILE + " model : " + ACTION);
                            break;
                    }
                    break;
                case MODEL_SYSTEM:
                    break;
                case MODEL_WEB:
                    break;
                default:
                    System.out.println("Shell: Unknown model: " + MODEL);
            }
        }
        catch (Exception e) {
            System.out.println("Error in executing command: " + e.getMessage());
            System.out.println("\nAn error occurred while executing the command: " + command);
            System.out.println("Error message: " + e.getMessage());
        }
    }

    private static void changeDirectory(String[] cmdParts, String currentDirectory) {
        try {
            // 将cmdParts的子数组传递给Paths.get()
            Path newDirPath = Paths.get(currentDirectory, Arrays.copyOfRange(cmdParts, 2, cmdParts.length)).normalize();

            File dir = newDirPath.toFile();
            if (dir.isDirectory()) {
                currentDirectory = dir.getAbsolutePath();
            } else {
                System.out.printf("No such directory: '%s'.%n", newDirPath);
            }
        } catch (Exception e) {
            System.out.println("Error in changing directory: " + e.getMessage());
        }
        System.out.printf("Current working directory: %s%n", currentDirectory);
    }

    private void Run() {
        try (Scanner scanner = new Scanner(System.in)) { // 使用try-with-resources确保资源关闭
            while (true) {
                System.out.print("~ " + currentDirectory + "$ "); // 提示符
                String command = scanner.nextLine();

                if (command.isEmpty()) {
                    System.out.println("Empty command entered. Please enter a valid command.");
                }
                else if (command.split(" ").length < 2) {
                    System.out.println("Invalid command format. Please enter a valid command.");
                }
                else if (command.equalsIgnoreCase("exit")) {
                    System.out.println("\nUser interrupted. Exiting...");
                    break;
                }
                else {
                    ExecuteCommand(command);
                }
            }
        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred. Exiting...");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        LaunchShell();
    }

    static void LaunchShell() {
        Shell shell = new Shell();
        shell.Run();
    }
}
