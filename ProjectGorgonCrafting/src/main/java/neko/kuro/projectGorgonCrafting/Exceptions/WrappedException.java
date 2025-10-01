package neko.kuro.projectGorgonCrafting.Exceptions;

import java.net.URISyntaxException;

public class WrappedException extends Exception {
    public WrappedException(String msg, Exception ex) {
        super(msg, ex);
    }
}
