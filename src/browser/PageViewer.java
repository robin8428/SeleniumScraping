package browser;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class PageViewer {
	public static void viewPage(HtmlPage page){
		String pageSource = page.getWebResponse().getContentAsString();
		File tempFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "temp-" + System.currentTimeMillis() + ".html");
		try {
			FileUtils.writeByteArrayToFile(tempFile, pageSource.getBytes());
			Desktop.getDesktop().browse(tempFile.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
