package service;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.geosolutions.geoserver.rest.HTTPUtils;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStyleManager;

/**
 * Class to predict using the Geoserver REST Style Manager of GeoSolutions with CSS extends class {@link GeoServerRESTStyleManager}
 * 
 * @author Daniel Faria, LabSIG (ESTG-IPVC)
 * @verion 1.1 [05/12/2016]
 *
 */
public class GeoServerStyleManager extends GeoServerRESTStyleManager {
	private static final String MIME_TYPE = "application/vnd.geoserver.geocss+css";
	private final static Logger LOGGER = LoggerFactory.getLogger(GeoServerStyleManager.class);
	
	private String gsUrl, gsUser, gsPass;
	
    /**
     * Constructor of GeoServerStyleManager
     * 
     * @param gsUrl URL address of geoserver
     * @param gsUser Username of user to use REST services
     * @param gsPass User password
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     * 
     */
	public GeoServerStyleManager(String gsUrl, String gsUser, String gsPass) throws IllegalArgumentException, MalformedURLException{
		super(new URL(gsUrl), gsUser, gsPass);
		this.gsUrl = gsUrl;
		this.gsUser = gsUser;
		this.gsPass = gsPass;
	}
	
	private String getGsUrl() {return gsUrl;}
	private String getGsUser() {return gsUser;}
	private String getGsPass() {return gsPass;}
	
    /**
     * Publish a Style in GeoServer, with CSS.
     *
     * @param geoCss the css style as a String.
     * @param nameStyle the Style name.
     *
     * @return <TT>true</TT> if the operation completed successfully.
     * @throws Exception if something gets wrong.
     */
	public boolean publishCssStyle(String cssBody, String nameStyle) throws Exception{
		if(nameStyle==null || nameStyle.isEmpty()) throw new NullPointerException("Name style is empty/null.");
		return publishCssStyle(cssBody, nameStyle, null);
	}
	
	/**
	 * Publish a Style in GeoServer, with CSS.
	 *
	 * @param geoCss the css style as a String.
	 * @param nameStyle the Style name.
	 * @param workspace the workspace name.
	 *
	 * @return <TT>true</TT> if the operation completed successfully.
	 * @throws Exception if something gets wrong.
	 */
	public boolean publishCssStyle(String cssBody, String nameStyle, String workspace) throws Exception{
		try{    		
			if(existsStyle(nameStyle)) throw new IllegalArgumentException(String.format("Style with name %s already exists.",nameStyle));
			if (cssBody == null || cssBody.isEmpty()) throw new IllegalArgumentException("The style may not be null or empty");

			StringBuilder sUrl = new StringBuilder(getGsUrl());
			sUrl.append("/rest")
				.append(workspace!=null ? String.format("/workspaces/%s",nameStyle) : "")
				.append("/styles")
				.append("?name=").append(URLEncoder.encode(nameStyle,java.nio.charset.StandardCharsets.UTF_8.toString()))
				.append("&raw=true");

			final String result = HTTPUtils.post(sUrl.toString(), new StringRequestEntity(cssBody, MIME_TYPE, "UTF-8"),getGsUser(), getGsPass());
			LOGGER.debug("Publish new style " + nameStyle + " to " + sUrl);
			if(result==null) throw new Exception("Style not uploaded.");
			return result.isEmpty();
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Update a Style, with CSS.
	 *
	 * @param geoCss the css style as a String.
	 * @param nameStyle the Style name.
	 *
	 * @return <TT>true</TT> if the operation completed successfully.
	 * @throws Exception if something gets wrong.
	 */
	public boolean updateCssStyle(String cssBody, String nameStyle) throws Exception{
		return updateCssStyle(cssBody, nameStyle, null);
	}
	
	/**
	 * Update a Style, with CSS.
	 *
	 * @param geoCss the css style as a String.
	 * @param nameStyle the Style name.
	 * @param workspace the workspace name.
	 *
	 * @return <TT>true</TT> if the operation completed successfully.
	 * @throws Exception if something gets wrong.
	 */
	public boolean updateCssStyle(String cssBody, String nameStyle, String workspace) throws Exception {
		try{   
		     	if(nameStyle==null || !existsStyle(nameStyle)) throw new IllegalArgumentException(String.format("Style with name %s not exists or style name is wrong/empty.",nameStyle));
	  	      	if (cssBody == null || cssBody.isEmpty()) throw new IllegalArgumentException("The style may not be null or empty");
			
			StringBuilder sUrl = new StringBuilder(getGsUrl());
			sUrl.append("/rest")
				.append(workspace!=null ? String.format("/workspaces/%s",nameStyle) : "")
				.append("/styles")
				.append(String.format("/%s.css",URLEncoder.encode(nameStyle,java.nio.charset.StandardCharsets.UTF_8.toString())))
				.append("?raw=true");

				final String result = HTTPUtils.put(sUrl.toString(), new StringRequestEntity(cssBody, MIME_TYPE, "UTF-8"), gsuser, gspass);
			LOGGER.debug("Updating style " + nameStyle + " on " + sUrl);
			if(result==null) throw new Exception("Style not updated.");
			return result.isEmpty();
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Remove a Style.
	 * <P>
	 * Delete the style and file.
	 *
	 * @param nameStyle the Style name.
	 * @param workspace the workspace name.
	 * @param recurse removes references to the specified style in existing layers.
	 * @param purge specifies whether the underlying file for the style should be deleted on disk. 
	 *
	 * @return <TT>true</TT> if the operation completed successfully.
	 * @throws Exception if something gets wrong.
	 */
	public boolean removeStyle(String nameStyle, String workspace, boolean recurse, boolean purge) throws Exception {
		try{   
			if(nameStyle==null || !existsStyle(nameStyle)) throw new IllegalArgumentException(String.format("Style with name %s not exists or style name is wrong/empty.",nameStyle));
			
			StringBuilder sUrl = new StringBuilder(getGsUrl());
			sUrl.append("/rest")
				.append(workspace!=null ? String.format("/workspaces/%s",nameStyle) : "")
				.append("/styles")
				.append(String.format("/%s.css",URLEncoder.encode(nameStyle,java.nio.charset.StandardCharsets.UTF_8.toString())))
				.append(String.format("?purge=%b",purge))
				.append(String.format("&recurse=%b",recurse));

				final boolean result = HTTPUtils.delete(sUrl.toString(), gsuser, gspass);
			LOGGER.debug("Removing style " + nameStyle + " on " + sUrl);
			if(!result) throw new Exception("Style not deleted. Check if the style are assigned to some layer.");
			return result;
    		}catch(Exception e){
    			throw new Exception(e.getMessage());
    		}
	}
}
