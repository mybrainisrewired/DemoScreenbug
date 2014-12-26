package mobi.vserv.android.ads;

public class ViewNotEmptyException extends Exception {
    private static final long serialVersionUID = 1;

    public ViewNotEmptyException(String message) {
        super(message);
    }

    public ViewNotEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewNotEmptyException(Throwable cause) {
        super(cause);
    }
}