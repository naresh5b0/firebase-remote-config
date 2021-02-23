package com.mycompany.firebase.remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.springframework.core.io.ClassPathResource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
@Xml(prefix="firebase-remote")
@Extension(name="Firebase Remote Config")
@Configurations(FirebaseConfiguration.class)
public class FirebaseExtension {
	
	public final static String[] SCOPES = { "https://www.googleapis.com/auth/firebase.remoteconfig" };


	/**
	 * Create HttpURLConnection that can be used for both retrieving and publishing.
	 *
	 * @return Base HttpURLConnection.
	 * @throws IOException
	 */
	public static HttpURLConnection getCommonConnection(String endpoint) throws IOException {
		URL url = new URL(endpoint);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
		httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
		return httpURLConnection;
	}

	/**
	 * Retrieve a valid access token that can be use to authorize requests to the
	 * Remote Config REST API.
	 *
	 * @return Access token.
	 * @throws IOException
	 */
	public static String getAccessToken() throws IOException {
		//File file =  new ClassPathResource("service-account.json").getFile();
		InputStream file =  new ClassPathResource("service-account.json").getInputStream();
		GoogleCredential googleCredential = GoogleCredential.fromStream(file)
				.createScoped(Arrays.asList(SCOPES));
		googleCredential.refreshToken();
		return googleCredential.getAccessToken();
	}

	/**
	 * Read contents of InputStream into String.
	 *
	 * @param inputStream InputStream to read.
	 * @return String containing contents of InputStream.
	 * @throws IOException
	 */
	public static String inputstreamToString(InputStream inputStream) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		Scanner scanner = new Scanner(inputStream);
		while (scanner.hasNext()) {
			stringBuilder.append(scanner.nextLine());
		}
		return stringBuilder.toString();
	}
	
	public static String readConfig() throws FileNotFoundException {
		    File file = new File("config.json");
		    Scanner scanner = new Scanner(file);

		    StringBuilder stringBuilder = new StringBuilder();
		    while (scanner.hasNext()) {
		      stringBuilder.append(scanner.nextLine());
		    }
		    return stringBuilder.toString();
		  }

}
