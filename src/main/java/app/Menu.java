package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static app.InputHelper.scanString;

public class Menu {
    String menuHeader, menuFooter;
    String exitString;
    public List<MenuOption> menuOptions;

    public Menu(String menuHeader, String menuFooter, String exitString) {
        this.menuHeader = menuHeader;
        this.menuFooter = menuFooter;
        this.exitString = exitString;
        this.menuOptions = new ArrayList<>();
    }

    public void addMenuOption(String menuString, String menuTitle, Action command){
        menuOptions.add(new MenuOption(menuString, menuTitle, command));
    }

    public void run(){
        String userChoice;
        do{
            System.out.print(menuHeader == null ? "" : menuHeader);
            for(MenuOption menuOption : menuOptions){
                System.out.println(menuOption.getMenuString() + ".  " + menuOption.getMenuTitle());
            }
            System.out.print(menuFooter == null ? "" : menuFooter);

            userChoice = scanString("");
            boolean IsUserChoiceValidated = false;

            for(MenuOption menuOption : menuOptions){
                if (userChoice.equals(menuOption.getMenuString())){
                    try {
                        IsUserChoiceValidated = true;
                        menuOption.execute();
                        System.out.println("Uitgevoerd.");
                    }catch (FeedbackToUserException ex){
                        System.out.println("Feedback: " + ex.getMessage());
                    } catch(Exception ex){
                        ex.printStackTrace();
                        System.out.println("Algemeen probleem: " + ex.toString() + Arrays.toString(ex.getStackTrace()));
                    }
                }
            }
            if (!IsUserChoiceValidated){
                System.out.println("Geen geldige menukeuze.");
            }
        } while (!userChoice.equals(exitString));
    }

    public class MenuOption {
        private String menuString;
        private String menuTitle;
        private Action action;

        MenuOption(String menuString, String menuTitle, Action action) {
            this.menuString = menuString;
            this.menuTitle = menuTitle;
            this.action = action;
        }

        String getMenuString() {
            return menuString;
        }

        String getMenuTitle() {
            return menuTitle;
        }

        void execute(){
            action.execute();
        }
    }

    public interface Action{
        void execute();
    }

    // Example usage.
    public static void main(String[] args) {
        Menu menu = new Menu("Hoofdmenu\n", "Keuze? ", "0");
        menu.addMenuOption("1", "Doe A direct", () -> doA() );
        menu.addMenuOption("2", "Naar Submenu", () -> menuB() );
        menu.addMenuOption("0", "Exit", () -> {} );
        menu.run();
    }

    private static void menuB(){
        Menu menu = new Menu("Submenu\n", "Keuze? ", "0");
        menu.addMenuOption("1", "submenu doe A", () -> doA() );
        menu.addMenuOption("2", "submenu doe B", () -> doB() );
        menu.addMenuOption("0", "Exit", () -> {} );
        menu.run();
    }

    private static void doA(){
        System.out.println("-------------> method doA");
    }

    private static void doB(){
        System.out.println("-------------> method doB");
    }

}
