package com.beo.app.android.pictures.copier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Hello world!
 *
 */
public class AndroidPicturesCopierApp {
	@NonNull
	public String getS() {
		return "";
	}

	public static void main(final String[] args) throws IOException {

		Files.list(Paths.get("/run/user")).filter(p -> {
			try {
				return Files.list(p).anyMatch(q -> q.getFileName().toString().contains("gvfs"));
			} catch (IOException e) {
				return false;
			}
		}).forEach(System.out::println);

	}
}
