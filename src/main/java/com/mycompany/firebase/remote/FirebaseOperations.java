package com.mycompany.firebase.remote;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.springframework.core.io.ClassPathResource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class FirebaseOperations {

	@DisplayName("Get Template")
	@MediaType(value = ANY, strict=false)
	public String getTemplate(@Config FirebaseConfiguration c) {

		String endpoint = c.getBaseurl() + c.getRemoteconfigendpoint();
		String jsonStr = null;

		HttpURLConnection httpURLConnection;
		try {
			httpURLConnection = FirebaseExtension.getCommonConnection(endpoint);

			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");

			int code = httpURLConnection.getResponseCode();
			
			if (code == 200) {
				InputStream inputStream = new GZIPInputStream(httpURLConnection.getInputStream());
				String response = FirebaseExtension.inputstreamToString(inputStream);

				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse(response);

				Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
				jsonStr = gson.toJson(jsonElement);

				System.out.println("Template retrieved and has been written to config.json");

				// Print ETag
				String etag = httpURLConnection.getHeaderField("ETag");
				System.out.println("ETag from server: " + etag);
				// return jsonStr;
			} else {
				System.out.println(FirebaseExtension.inputstreamToString(httpURLConnection.getErrorStream()));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonStr;

	}
	
	@DisplayName("Update Template")
	@MediaType(value = ANY, strict=false)
	public void publishTemplate(@Config FirebaseConfiguration c,@Optional(defaultValue="#[payload]") @Content Map<String, Object> content) throws IOException {
		
		String endpoint = c.getBaseurl() + c.getRemoteconfigendpoint();

		    System.out.println("Publishing template...");
		    HttpURLConnection httpURLConnection = FirebaseExtension.getCommonConnection(endpoint);
		    httpURLConnection.setDoOutput(true);
		    httpURLConnection.setRequestMethod("PUT");
		    httpURLConnection.setRequestProperty("If-Match", "*");
		    httpURLConnection.setRequestProperty("Content-Encoding", "gzip");

		    //String configStr = FirebaseExtension.readConfig();
		    String configStr = content.toString();

		    GZIPOutputStream gzipOutputStream = new GZIPOutputStream(httpURLConnection.getOutputStream());
		    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(gzipOutputStream);
		    outputStreamWriter.write(configStr);
		    outputStreamWriter.flush();
		    outputStreamWriter.close();

		    int code = httpURLConnection.getResponseCode();
		    if (code == 200) {
		      System.out.println("Template has been published.");
		    } else {
		      System.out.println(FirebaseExtension.inputstreamToString(httpURLConnection.getErrorStream()));
		    }

		  }

}
