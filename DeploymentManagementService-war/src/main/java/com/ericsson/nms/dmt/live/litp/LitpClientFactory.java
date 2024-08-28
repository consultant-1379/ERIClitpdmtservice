/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.nms.dmt.live.litp;

import static org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

import java.security.cert.X509Certificate;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.ericsson.nms.dmt.error.UnexpectedException;
import com.ericsson.nms.dmt.live.litp.error.LitpErrorInterceptor;
import com.ericsson.nms.dmt.live.litp.parsing.*;

/**
 * CDI producer that is responsible for creating and setting up
 * {@link LitpServiceClient} proxy instances that are injected anywhere it is
 * needed inside the application.
 */
public class LitpClientFactory {

	@Resource(name = "litpRestBaseUri")
	private String litpBaseUri;

	@Resource(name = "litpUsername")
	private String litpUsername;

	@Resource(name = "litpPassword")
	private String litpPassword;

	@Inject
	private LitpErrorInterceptor clientErrorInterceptor;

	@Inject
	private NodeBodyReader nodeBodyReader;

	@Inject
	private ItemTypeBodyReader itemTypeBodyReader;

	@Inject
	private ItemTypeListBodyReader itemTypeListBodyReader;

	/**
	 * Produces a new proxy instance of {@link LitpServiceClient} and sets up
	 * the security properties.
	 * 
	 * @return a proxied {@link LitpServiceClient} instance
	 */
	@Produces
	public LitpClient produces() {
		ResteasyProviderFactory pf = ResteasyProviderFactory.getInstance();
		pf.addClientErrorInterceptor(clientErrorInterceptor);
		pf.addMessageBodyReader(nodeBodyReader);
		pf.addMessageBodyReader(itemTypeBodyReader);
		pf.addMessageBodyReader(itemTypeListBodyReader);

		ApacheHttpClient4Executor executor = new ApacheHttpClient4Executor();
		HttpClient httpClient = executor.getHttpClient();
		configureSSL(httpClient);
		configureAuthentication(httpClient);
		ClientRequestFactory crf = new ClientRequestFactory(executor, pf,
				UriBuilder.fromUri(litpBaseUri).build());
		return crf.createProxy(LitpClient.class);
	}

	private void configureAuthentication(HttpClient httpClient) {
		CredentialsProvider provider = ((DefaultHttpClient) httpClient)
				.getCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				litpUsername, litpPassword);
		provider.setCredentials(AuthScope.ANY, credentials);
	}

	private void configureSSL(HttpClient httpClient) {
		SchemeRegistry schemeRegistry = httpClient.getConnectionManager()
				.getSchemeRegistry();
		schemeRegistry.register(buildScheme());
	}

	private Scheme buildScheme() {
		try {
			SSLContext sslcontext = SSLContext.getInstance("SSL");
			sslcontext.init(null, getTrustManager(),
					new java.security.SecureRandom());
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext,
					ALLOW_ALL_HOSTNAME_VERIFIER);
			return new Scheme("https", 443, sf);
		} catch (Exception e) {
			throw UnexpectedException.wrap(e);
		}
	}

	private javax.net.ssl.TrustManager[] getTrustManager() {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };
		return trustAllCerts;
	}
}
