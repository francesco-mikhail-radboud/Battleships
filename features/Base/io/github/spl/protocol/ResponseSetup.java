package io.github.spl.protocol;

public class ResponseSetup implements Command {
    private final boolean isSuccess;

    public ResponseSetup(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
	public String serialize() {
		return "SETUP:" + (isSuccess ? "Y" : "N") + ".";
	}
}
