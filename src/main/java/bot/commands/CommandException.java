package bot.commands;

public class CommandException extends Exception{
    private String errorTitle;
    private String errorDesc;

    public CommandException(String errorTitle, String errorDesc){
        this.errorTitle = errorTitle;
        this.errorDesc = errorDesc;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
}
