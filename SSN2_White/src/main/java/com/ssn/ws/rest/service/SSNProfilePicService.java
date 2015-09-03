
package com.ssn.ws.rest.service;

/**
 *
 * @author RDiwakar
 */

    



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNLoginForm;
import com.ssn.ws.rest.request.SSNLoginRequest;
import com.ssn.ws.rest.response.SSNLoginResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author vkvarma
 */
public class SSNProfilePicService {

    private static String REST_ENDPOINT = "";
    private HttpURLConnection con = null;
    private SSNLoginRequest request = null;
    private SSNLoginResponse response = null;
    private SSNLoginForm loginForm = null;
    private String hostName = "";
    private String serviceName = "";
    private String restURI = "";
    private Logger logger = Logger.getLogger(SSNProfilePicService.class);
    
    public SSNProfilePicService(String userProfilePicEndPoint) {
         REST_ENDPOINT = userProfilePicEndPoint;
    }

    public void initWSConnection() {
  
        this.setCon(this.getHTTPConnectionObject());
        if (this.getCon() == null) {
            //JOptionPane.showMessageDialog(this.getLoginForm(), "Connection Down", "Connection Failed", JOptionPane.ERROR_MESSAGE);
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Connection Failed", "", "Connection Down");
        }
    }

    private HttpURLConnection getHTTPConnectionObject() {
        URL endpointUrl = null;
        HttpURLConnection con = null;
        try {
            endpointUrl = new URL(REST_ENDPOINT);
            con = (HttpURLConnection) endpointUrl.openConnection();

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return con;
    }

    public void prepareRequest(SSNLoginRequest request) {
        try {
            if (request != null) {
                getCon().setRequestMethod("POST");
                getCon().setDoOutput(true);
                getCon().setDoInput(true);
                getCon().setUseCaches(false);
                getCon().setAllowUserInteraction(false);
                getCon().setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                // con.setRequestProperty("Content-Type", "application/json");

                OutputStream out = getCon().getOutputStream();
                Writer writer = new OutputStreamWriter(out, "UTF-8");

                for (Map.Entry<String, String> entry : request.getRequestParameters().entrySet()) {
                    writer.write(entry.getKey());
                    writer.write("=");
                    writer.write(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    writer.write("&");
                }
                writer.close();
                out.close();
                this.sendRequest();
            } else {
                //JOptionPane.showMessageDialog(this.getLoginForm(), "Invalid Request", "Not Enough Request Data", JOptionPane.ERROR_MESSAGE);
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Invalid Request", "", "Not Enough Request Data");
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (ProtocolException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                getCon().disconnect();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

    }

    private void sendRequest() {
        try {
            if (getCon().getResponseCode() != 200) {
                //JOptionPane.showMessageDialog(this.getLoginForm(), getCon().getResponseMessage(), "No Response From Service", JOptionPane.ERROR_MESSAGE);
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "No Response From Service", "", getCon().getResponseMessage());
            } else {
                this.processResponse();
            }
        } catch (IOException e) {
            //JOptionPane.showMessageDialog(this.getLoginForm(), "Null Response", "No Response From Service", JOptionPane.ERROR_MESSAGE);
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "No Response From Service", "", "Null Response");
        }
    }

    private void processResponse() {

        BufferedReader bufferReader = null;
        StringBuilder response = new StringBuilder();
        String line = null;

        try {
            bufferReader = new BufferedReader(new InputStreamReader(getCon().getInputStream()));
            while ((line = bufferReader.readLine()) != null) {
                response.append(line);
                response.append("\n");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException ex) {
                }
                bufferReader = null;
            }
        }

        String jsonResponseStr = response.toString();
       
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        this.setResponse(gson.fromJson(jsonResponseStr, SSNLoginResponse.class));
    }

    /**
     * @return the loginForm
     */
    public SSNLoginForm getLoginForm() {
        return loginForm;
    }

    /**
     * @param loginForm the loginForm to set
     */
    public void setLoginForm(SSNLoginForm loginForm) {
        this.loginForm = loginForm;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the restURI
     */
    public String getRestURI() {
        return restURI;
    }

    /**
     * @param restURI the restURI to set
     */
    public void setRestURI(String restURI) {
        this.restURI = restURI;
    }

    public HttpURLConnection getCon() {
        return con;
    }

    public void setCon(HttpURLConnection con) {
        this.con = con;
    }

    public SSNLoginRequest getRequest() {
        return request;
    }

    public void setRequest(SSNLoginRequest request) {
        this.request = request;
    }

    public SSNLoginResponse getResponse() {
        return response;
    }

    public void setResponse(SSNLoginResponse response) {
        this.response = response;
    }


}
