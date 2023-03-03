package xyz.rajik.lfsr;

public class LFSRException extends RuntimeException {
    public LFSRException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "LSFR Exception:\n" + super.getMessage();
    }
}
