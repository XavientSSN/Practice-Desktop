package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import com.ssn.helper.SSNHelper;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JLabel;

/**
 *
 * @author vkvarma
 */
public final class SSNHyperlinkLabel extends JLabel {

    private static final long   serialVersionUID = 1L;    
    private              URI    uri              = null;
    private              String color            = null;
    private              String lblText          = null;
    private              String lblId            = null;
    private              String uiForm           = null;
    
    /**
     * 
     * Constructor
     * 
     */
    public SSNHyperlinkLabel() {        
    }
    
    public SSNHyperlinkLabel(String text,String lblId,String uri, String color,String form) {
       
         this.setText("<html><font color='" + color + "'>" + text + "</font></html>");
        try {  
            this.setUri(new URI(uri));
            this.setColor(color);
            this.setLblText(text);
            this.setLblId(lblId);
            this.setUiForm(form);
        } 
        catch(URISyntaxException uriex) {           
        }
        catch(Exception ex) {            
        }
        this.initialize(this.getUiForm());
    }

    /**
     * This method initializes the link.
     */
    private void initialize(String form) {
        this.setFont(new Font("open sans",Font.BOLD,10)); 
        this.setBackground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        this.setOpaque(false);
        
        this.setToolTipText(getUri().toString());
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  
        if(form.equals("login"))
           this.addMouseListener(SSNHelper.getLoginController());
    }

    /**
     * @return the uri
     */
    public URI getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * @return the lblId
     */
    public String getLblId() {
        return lblId;
    }

    /**
     * @param lblId the lblId to set
     */
    public void setLblId(String lblId) {
        this.lblId = lblId;
    }

    /**
     * @return the uiForm
     */
    public String getUiForm() {
        return uiForm;
    }

    /**
     * @param uiForm the uiForm to set
     */
    public void setUiForm(String uiForm) {
        this.uiForm = uiForm;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the lblText
     */
    public String getLblText() {
        return lblText;
    }

    /**
     * @param lblText the lblText to set
     */
    public void setLblText(String lblText) {
        this.lblText = lblText;
    }
}