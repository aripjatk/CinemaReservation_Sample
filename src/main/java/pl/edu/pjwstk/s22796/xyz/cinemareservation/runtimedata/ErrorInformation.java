package pl.edu.pjwstk.s22796.xyz.cinemareservation.runtimedata;

public class ErrorInformation {
    private String error;

    public ErrorInformation(String error) {
        this.setError(error);
    }

    @SuppressWarnings("unused")
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
