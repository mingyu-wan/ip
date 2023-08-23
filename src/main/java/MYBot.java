public class MYBot {

    private String bot_Name;
    private TaskList task_List;

    public MYBot(String bot_Name){
        this.bot_Name = bot_Name;
        this.task_List = new TaskList();
    }

    public void openGreeting(){
        System.out.println("____________________________________________________________");
        System.out.println("Hello! I'm " + bot_Name + ":)");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");
    }

    public void closeGreeting(){
        System.out.println("____________________________________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    public void analyseInput(String input) {

        try {
            if (input.equals("list")) {
                listTasks();
            } else if (input.startsWith("mark ")) {
                int task_index = Integer.parseInt(input.substring(5));
                markTasks(task_index);
            } else if (input.startsWith("unmark ")) {
                int task_index = Integer.parseInt(input.substring(7));
                unmarkTasks(task_index);
            } else if (input.isEmpty()) {
                throw new MYBotExceptions.UnknownCommandException();
            } else if (!input.startsWith("todo ") && !input.startsWith("deadline ") && !input.startsWith("event ")) {
                throw new MYBotExceptions.InvalidTaskException();
            } else {
                addTask(input);
            }
        } catch (MYBotExceptions e) {
            System.out.println("____________________________________________________________");
            System.out.println(e.getMessage());
            System.out.println("____________________________________________________________");
        }
    }

    public void addTask(String input) {

        System.out.println("____________________________________________________________");

        try {
            if (input.startsWith("todo ")) {

                String description = input.substring(5);
                if (!description.isBlank()) {
                    Task task = new Todo(description);
                    task_List.addTask(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(task.toString());
                } else {
                    throw new MYBotExceptions.EmptyDetailsException("description", "todo");
                }

            } else if (input.startsWith("deadline ")) {
                if(!input.contains(" /by")) {
                    throw new MYBotExceptions.InvalidInputException("deadline", "duedate");
                }

                String description = input.substring(9, input.indexOf(" /by "));
                String by = input.substring(input.indexOf(" /by ") + 4);

                if (description.isBlank()) {
                    throw new MYBotExceptions.EmptyDetailsException("description", "deadline");
                } else if (by.isBlank()) {
                    throw new MYBotExceptions.EmptyDetailsException("duedate", "deadline");
                } else {
                    Task task = new Deadline(description, by);
                    task_List.addTask(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(task.toString());
                }
            } else if ((input.startsWith("event "))) {

                if(!input.contains(" /from")) {
                    throw new MYBotExceptions.InvalidInputException("event", "start time");
                } else if (!input.contains(" /to")) {
                    throw new MYBotExceptions.InvalidInputException("event", "end time");
                }

                String description = input.substring(6, input.indexOf(" /from "));
                String from = input.substring(input.indexOf(" /from ") + 6, input.indexOf(" /to"));
                String to = input.substring(input.indexOf(" /to ") + 4);

                if (description.isBlank()) {
                    throw new MYBotExceptions.EmptyDetailsException("description", "event");
                } else if (from.isBlank()) {
                    throw new MYBotExceptions.EmptyDetailsException("start time", "event");
                } else if (to.isBlank()) {
                    throw new MYBotExceptions.EmptyDetailsException("end time", "event");
                } else {
                    Task task = new Event(description, from, to);
                    task_List.addTask(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(task.toString());
                }
            }
        } catch (MYBotExceptions e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Now you have " + task_List.getTask_Count() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    public void listTasks(){
        Task[] tasks = task_List.getTask_List();
        int taskCount = task_List.getTask_Count();

        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++){
            System.out.println((i+1) + "." + tasks[i].toString());
        }
        System.out.println("____________________________________________________________");
    }

    public void markTasks(int task_number) {
        System.out.println("____________________________________________________________");

        try {
            if (task_number > 0 && task_List.getTask(task_number) != null) {
                Task taskTobeMarked = task_List.getTask(task_number);
                taskTobeMarked.taskDone();

                System.out.println("Good job completing! I've marked these task as done:):");
                System.out.println(taskTobeMarked.toString());
            } else {
                throw new MYBotExceptions.NoSuchTaskException();
            }
        } catch (MYBotExceptions e) {
            System.out.println(e.getMessage());
        }

        System.out.println("____________________________________________________________");
    }


    public void unmarkTasks(int task_number){
        System.out.println("____________________________________________________________");

        try {
            if (task_number > 0 && task_List.getTask(task_number) != null) {
                Task taskTobeMarked = task_List.getTask(task_number);
                taskTobeMarked.undoTask();

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(taskTobeMarked.toString());
            } else {
                throw new MYBotExceptions.NoSuchTaskException();
            }
        } catch (MYBotExceptions e) {
            System.out.println(e.getMessage());
        }

        System.out.println("____________________________________________________________");
    }
}
