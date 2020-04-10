package tpmms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderHandler {
	private String currentLine;
	private BufferedReader br;

	FileReaderHandler(BufferedReader br) {
		this.br = br;
		readFromFile();
	}

	FileReaderHandler(String fileName) throws FileNotFoundException {
		br = new BufferedReader(new FileReader(fileName));
		readFromFile();
	}

	public void readFromFile() {
		try {
			currentLine = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCurrentLine() {
		return currentLine;
	}

	public String readAndRemoveFromFile() {
		String previousCurrentLine = getCurrentLine();
		readFromFile();
		return previousCurrentLine;
	}

	public void close() {
		try {
			if (br != null) {
				br.close();
			}
			br = null;
			currentLine = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isEmpty() {
		if (this.currentLine == null) {
			close();
			return true;
		}
		return false;
	}
}