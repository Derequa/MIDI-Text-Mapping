package frontside;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 * This is a cool class I found online at:
 * http://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea
 * That lets you use a JTextArea like an output stream
 * This is how we get our all the parts of our server to print to the GUI
 * @author Derek Batts
 *
 */
public class JTextOutputStream extends OutputStream {

	private JTextArea textArea;
	
	public JTextOutputStream(JTextArea textArea){
		this.textArea = textArea;
	}
	
	public void write(int b) throws IOException {
		// redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
	}

}
