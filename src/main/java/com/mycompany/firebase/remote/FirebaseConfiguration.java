package com.mycompany.firebase.remote;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

@Operations(FirebaseOperations.class)
public class FirebaseConfiguration {
	@Parameter
	@DisplayName("Project Id")
	private String projectid;
	@Parameter
	@DisplayName("Base URL")
	private String baseurl;
	@Parameter
	@DisplayName("Remote Config Endpoint")
	private String remoteconfigendpoint;
	/*
	 * @Parameter
	 * 
	 * @DisplayName("Scopes") private String[] scopes;
	 */

	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	public String getBaseurl() {
		return baseurl;
	}

	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}

	public String getRemoteconfigendpoint() {
		return remoteconfigendpoint;
	}

	public void setRemoteconfigendpoint(String remoteconfigendpoint) {
		this.remoteconfigendpoint = remoteconfigendpoint;
	}

	/*
	 * public String[] getScopes() { return scopes; }
	 * 
	 * public void setScopes(String[] scopes) { this.scopes = scopes; }
	 */
}
