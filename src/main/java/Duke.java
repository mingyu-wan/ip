import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Duke  {

    private Storage storage;
    private Parser parser;
    private TaskList tasks;
    private Commands command;
    private Ui ui;

    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        try {
            tasks = new TaskList(storage.loadTaskFromFile());
        } catch (NullPointerException e){
            tasks = new TaskList();
        } catch (Exception e) {
            ui.printFileError();
            new File(filePath);
            tasks = new TaskList();
        }
        command = new Commands(ui,storage,tasks);
        parser = new Parser(ui, storage, tasks, command);
    }

    public void run() {
        ui.showGreeting();

        Scanner scanner = new Scanner(System.in);

        while(true){
            String input = scanner.nextLine();

            if(input.equals("bye")) {
                ui.closeGreeting();
                break;
            } else {
                parser.analyseInput(input);
            }
        }
    }
    public static void main(String[] args) {
        new Duke("src/main/MYBOT.txt").run();
    }
}

