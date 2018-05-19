package Interfaces;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;

public interface RunnerInterface {
	public OutputStream iniciation() throws ConnectException,IOException;
}
