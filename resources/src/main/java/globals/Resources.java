package globals;

public class Resources {
    public static final int CA_PORT = 10000;
    public static final String CA_NAME = "CA";
    public static final int RSU_PORT = 11000;

    public static String ERROR_MSG(String msg) { return "[\033[0;31m] ERROR \033[0m]]"+ msg; }
    public static String WARNING_MSG(String msg) { return "[\033[0;33m]WARNING\033[0m]]" + msg; }
    public static String OK_MSG(String msg) { return "[\033[0;32m]  OK  \033[0m]]" + msg; }
}