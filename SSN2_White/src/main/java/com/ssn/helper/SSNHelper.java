package com.ssn.helper;

import com.ssn.app.loader.SSNConstants;
import static com.ssn.app.loader.SSNConstants.SSN_APPLICATION_DIRECTORY;
import static com.ssn.app.loader.SSNConstants.SSN_DEFAULT_DIRECTORY;
import static com.ssn.app.loader.SSNConstants.SSN_DB_DIRECTORY;
import static com.ssn.app.loader.SSNConstants.SSN_FACEBOOK_PHOTOS_DIRECTORY;
import static com.ssn.app.loader.SSNConstants.SSN_HIVE_DIRECTORY;
import static com.ssn.app.loader.SSNConstants.SSN_TEMP_DIRECTORY;
import static com.ssn.app.loader.SSNConstants.SSN_VOICE_NOTE_DIRECTORY;
import static com.ssn.app.loader.SSNConstants.SSN_WORKSPACE_DIRECTORY;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.event.controller.SSNLoginController;
import com.ssn.ui.custom.component.SSNHyperlinkLabel;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNHomeForm;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.oauth2.AccessGrant;

/**
 *
 * @author vkvarma
 */
public class SSNHelper {

    private static SSNLoginController ssnLoginController = null;
    private static Cipher ecipher;
    private static Cipher dcipher;
    private static byte[] key = {34, -125, 112, -88, 76, -126, 121, -66};
    static Logger logger = Logger.getLogger(SSNHelper.class);
    public static double APPLICATION_VERSION = 1.0;
    public static String lastAlbum = null;
   
    /**
     * Checks whether the browsing is supported or not
     * @return
     */
    public static boolean isBrowsingSupported() {
        if (!Desktop.isDesktopSupported()) {
            return false;
        }
        boolean result = false;
        Desktop desktop = java.awt.Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            result = true;
        }
        return result;
    }

    /**
     * Return the instance of login controller
     * @return
     */
    public static SSNLoginController getLoginController() {
        if (ssnLoginController != null) {
            return ssnLoginController;
        } else {
            return new SSNLoginController();
        }
    }

    /**
     * Returns the hyperlink label 
     * @param displayText
     * @param elementId
     * @param uri
     * @param color
     * @param form
     * @return
     */
    public static SSNHyperlinkLabel getHyperlinkLabel(String displayText, String elementId, String uri, String color, String form) {
        SSNHyperlinkLabel ssnHyperlinkLabel = new SSNHyperlinkLabel(displayText, elementId, uri, color, form);
        return ssnHyperlinkLabel;
    }

    /**
     * This method will create all the required directories and folders on the file system
     * and copy the necessary files required to load the application
     */
    public static void createAndloadSSNDirs() {
        String ssnDirs = getSsnDefaultDirPath();
        String ssnDbDir = getSsnDBDirPath();
        try {
            if (ssnDirs != null && (!ssnDirs.equals("")) && ssnDirs.length() > 0) {
                File theDir = new File(ssnDirs);
                if (!theDir.exists()) {
                    boolean result = theDir.mkdirs();
                    if (result) {
                    } else {
                        //JOptionPane.showMessageDialog(null, "", "Error in creating SSN Media Workspace ", JOptionPane.ERROR_MESSAGE);
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Error in creating SSN Media Workspace");
                    }
                }
            }
            if (ssnDbDir != null && (!ssnDbDir.equals("")) && ssnDbDir.length() > 0) {
                File theDirDB = new File(ssnDbDir);
                if (!theDirDB.exists()) {
                    boolean result = theDirDB.mkdir();
                    if (result) {
                        createDB(ssnDbDir);
//                        try {
//                            URL fileURL = SSNHelper.class.getResource("/config/OurHiveDatabase.sqlite");
//                            FileUtils.copyURLToFile(fileURL, new File(ssnDbDir + File.separator + "OurHiveDatabase.sqlite"));
//                            //File(file, new File(ssnDbDir + File.separator + "OurHiveDatabase.sqlite"));
//                        } catch (IOException ex) {
//                            logger.error(ex);
//                        }
                    } else {
                        //JOptionPane.showMessageDialog(null, "", "Error in creating SSN DB Dir ", JOptionPane.ERROR_MESSAGE);
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Error in creating SSN DB Dir");
                    }
                } else {
                    if (theDirDB.list() == null || theDirDB.list().length == 0) {
                        createDB(ssnDbDir);
//                        try {
//
//                            URL fileURL = SSNHelper.class.getResource("/config/OurHiveDatabase.sqlite");
//                            FileUtils.copyURLToFile(fileURL, new File(ssnDbDir + File.separator + "OurHiveDatabase.sqlite"));
//                        } catch (IOException ex) {
//                            logger.error(ex);
//                        }
                    } else {

                          if(SSNDao.isTableExist("SSNVERSION")){
                              if(SSNDao.isVersionChanged()){
                                  createDB(ssnDbDir);
                                //  System.out.println(" version change");
                              }

                          }else{
                            createDB(ssnDbDir);

                          }
//                        File oldFile = new File(ssnDbDir + File.separator + "OurHiveDatabase.sqlite");
//                        InputStream fileURL = SSNHelper.class.getResourceAsStream("/config/OurHiveDatabase.sqlite");
//                        URL fileURL2 = SSNHelper.class.getResource("/config/OurHiveDatabase.sqlite");
//                        File newFile = new File(ssnDbDir + File.separator + "OurHiveDatabaseTemp.sqlite");
//                        FileOutputStream out = new FileOutputStream(newFile);
//                        IOUtils.copy(fileURL, out);
//                        out.close();
//                        //logger.info(fileURL.getFile());
//                        if (oldFile.length() < newFile.length()) {
//                            try {
//                                oldFile.delete();
//
//                                FileUtils.copyURLToFile(fileURL2, new File(ssnDbDir + File.separator + "OurHiveDatabase.sqlite"));
//
//                            } catch (Exception ex) {
//                                logger.error(ex);
//                            }
//
//                        }
//                        FileUtils.forceDelete(newFile);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static boolean createDB(String directory){
        boolean result  =false;
        try {
                URL fileURL = SSNHelper.class.getResource("/config/OurHiveDatabase.sqlite");
                File file   =   new File(directory + File.separator + "OurHiveDatabase.sqlite");
                FileUtils.copyURLToFile(fileURL,file);
                result  =   file.exists();
                    //File(file, new File(ssnDbDir + File.separator + "OurHiveDatabase.sqlite"));
        } catch (IOException ex) {
            logger.error(ex);
        }
        return result;
    }
    
    /**
     * 
     * Return the application directory path 
     * @return
     */
    public static String getSsnApplicationDirPath() {
        return System.getProperty("user.home") + File.separator + SSN_APPLICATION_DIRECTORY + File.separator;
    }

    /**
     * Return the workspace directory path
     * @return
     */
    public static String getSsnWorkSpaceDirPath() {
        return getSsnApplicationDirPath() + SSN_WORKSPACE_DIRECTORY + File.separator;
    }

    /**
     * Return the database directory path
     * @return
     */
    public static String getSsnDBDirPath() {
        return getSsnWorkSpaceDirPath() + SSN_DB_DIRECTORY + File.separator;
    }

    /**
     * Return the application ourhive directory path
     * @return
     */
    public static String getSsnHiveDirPath() {
        return getSsnWorkSpaceDirPath() + SSN_HIVE_DIRECTORY + File.separator;
    }

    /**
     * Return the application camera directory path
     * @return
     */
    public static String getSsnDefaultDirPath() {
        return getSsnHiveDirPath() + SSN_DEFAULT_DIRECTORY + File.separator;
    }

    /**
     * Return the application voice note directory path
     * @return
     */
    public static String getSsnVoiceNoteDirPath() {
        return getSsnWorkSpaceDirPath() + SSN_VOICE_NOTE_DIRECTORY + File.separator;
    }

    /**
     * Return the application temporary directory path
     * @return
     */
    public static String getSsnTempDirPath() {
        return getSsnApplicationDirPath() + SSN_TEMP_DIRECTORY + File.separator;
    }

    /**
     * Return the application facebook photos directory path
     * @return
     */
    public static String getFacebookPhotosDirPath() {
        return getSsnWorkSpaceDirPath() + SSN_FACEBOOK_PHOTOS_DIRECTORY + File.separator;
    }

    /**
     * Return the application instagram photos directory path
     * @return
     */
    public static String getInstagramPhotosDirPath() {
        return getSsnWorkSpaceDirPath() + SSNConstants.SSN_INSTAGRAM_PHOTOS_DIRECTORY + File.separator;
    }

    /**
     * This method will resize the image based on the give input parameter
     * @param image
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        int type = 0;
        type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
         
        return resizedImage;
    }

    /**
     * Returns the list of files available in a particular directory
     * @param file
     * @param returnList
     * @return
     */
    public static List<File> listFiles(File file, List<File> returnList) {
        File[] fileList = file.listFiles();
        if(fileList!= null)
        {
            for (File fileInList : fileList) {
                if (fileInList.isDirectory()) {
                    listFiles(fileInList, returnList);
                } else {
                    returnList.add(fileInList);
                }
            }
        }
        return returnList;
    }

    /**
     * Returns the credentials saved for remember me option
     * @return
     */
    public static String[] getRememberMeCredentials() {
        String rootPath = SSNHelper.getSsnWorkSpaceDirPath();
        String RememberMePath = rootPath + File.separator + SSNConstants.SSN_REMMEBER_ME_DIRECTORY;
        File file = new File(RememberMePath);
        String usernamePasswordEncrypted = "";
        String usernamePasswordString = "";
        String[] usernamePasswordArray = null;
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length != 0) {
                BufferedReader br = null;
                try {
                    File file2 = files[0];

                    br = new BufferedReader(new FileReader(file2.getAbsolutePath()));
                    usernamePasswordEncrypted = br.readLine();
                    usernamePasswordString = decrypt(usernamePasswordEncrypted);
                } catch (Exception ex) {
                    logger.error(ex);

                } finally {

                    try {
                        if(br != null)
                            br.close();
                        
                    } catch (IOException ex) {
                        logger.error(ex);
                    }

                }
            }
        }
        if (usernamePasswordString != null && usernamePasswordString.contains(" ")) {
            usernamePasswordArray = usernamePasswordString.split(" ");
        }
        return usernamePasswordArray;
    }

    /**
     * Retrieves the album name from the path
     * @param path
     * @return
     */
    public static String getAlbumNameFromPath(String path) {
        String albumName = "";
        if (StringUtils.isNotBlank(path)) {
            if (!path.equalsIgnoreCase(SSN_HIVE_DIRECTORY)) {
                int indexOfSeparator = path.lastIndexOf(File.separator);
                albumName = path.substring(indexOfSeparator + 1, path.length());
                if(albumName.equals("viewAllAlbums"))
                 {
                     albumName = "All Media";
                 }
                else if(albumName.equals("instagramMedia"))
                {
                    albumName = "Instagram Media";
                }
                else if(albumName.equals("tagUnTaggedMedia"))
                {
                    albumName = "OurHive Media";
                }
                else if(path.contains(getFacebookPhotosDirPath()))
                {
                    lastAlbum = path;
                }
            } else {
                albumName = "All Media";
                //System.out.println("All Media:::"+albumName);
            }
        }
        
        return albumName;
    }

    /**
     * This method will toggle the delete and share icons
     * @param isActive
     * @param ssnHomeForm
     */
    public static void toggleDeleteAndShareImages(boolean isActive, SSNHomeForm ssnHomeForm) {
        String deleteImagePath = "";
        String shareImagePath = "";
        if (isActive) {
            deleteImagePath = "/icon/delete-active.png";
            shareImagePath = "/icon/share-active.png";
        } else {
            deleteImagePath = "/icon/delete-deactive.png";
            shareImagePath = "/icon/share-deactive.png";
        }
        for (Component comp : ssnHomeForm.getSsnHomeToolBar().getComponents()) {
            if (comp.getName() != null && comp.getName().equalsIgnoreCase("deletePhoto")) {
                JLabel jLbl = (JLabel) comp;
                jLbl.setIcon(new ImageIcon(SSNHelper.class.getResource(deleteImagePath)));
            } else if (comp.getName() != null && comp.getName().equalsIgnoreCase("share")) {
                JLabel jLbl = (JLabel) comp;
                jLbl.setIcon(new ImageIcon(SSNHelper.class.getResource(shareImagePath)));
            }
        }

    }

    /**
     * This method will toggle the delete icon
     * @param isActive
     * @param ssnHomeForm
     */
    public static void toggleDeleteImage(boolean isActive, SSNHomeForm ssnHomeForm) {
        String deleteImagePath = "";
        if (isActive) {
            deleteImagePath = "/icon/delete-active.png";
        } else {
            deleteImagePath = "/icon/delete-deactive.png";
        }
        for (Component comp : ssnHomeForm.getSsnHomeToolBar().getComponents()) {
            if (comp.getName() != null && comp.getName().equalsIgnoreCase("deletePhoto")) {
                JLabel jLbl = (JLabel) comp;
                jLbl.setIcon(new ImageIcon(SSNHelper.class.getResource(deleteImagePath)));
            }
        }

    }

    /**
     * This method will encrypt plain text and return the encrypted value
     * @param str
     * @return
     */
    public static String encrypt(String str) {

        try {
            //key = KeyGenerator.getInstance("DES").generateKey();
            SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
            ecipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] utf8 = str.getBytes("UTF8");

            byte[] enc = ecipher.doFinal(utf8);
            enc = BASE64EncoderStream.encode(enc);

            return new String(enc);

        } catch (Exception e) {

            logger.error(e);

        }

        return null;

    }

    /**
     * This method will decrypt plain text and return the decrypted value
     * @param str
     * @return
     */
    public static String decrypt(String str) {

        try {

            // decode with base64 to get bytes
            //key = KeyGenerator.getInstance("DES").generateKey();
            SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
            dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] dec = BASE64DecoderStream.decode(str.getBytes());

            byte[] utf8 = dcipher.doFinal(dec);

// create new string based on the specified charset
            return new String(utf8, "UTF8");

        } catch (Exception e) {

            logger.error(e);

        }

        return null;

    }

    /**
     *
     * @return
     */
    public static boolean isLoggedInWithSocial() {
        String rootPath = SSNHelper.getSsnWorkSpaceDirPath();
        String RememberMePath = rootPath + File.separator + SSNConstants.SSN_REMMEBER_ME_DIRECTORY;

        File RememberMe = new File(RememberMePath);
        if (!RememberMe.exists()) {
            return false;
        }

        File[] listFiles = RememberMe.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().startsWith("Token_");

            }
        });

        if (listFiles.length > 0) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether user is logged in with social media account or not
     * @return
     */
    public static Map<String, Object> deserializeAccessToken() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String rootPath = SSNHelper.getSsnWorkSpaceDirPath();
            String RememberMePath = rootPath + File.separator + SSNConstants.SSN_REMMEBER_ME_DIRECTORY;

            File RememberMe = new File(RememberMePath);
            File[] listFiles = RememberMe.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    return file.getName().startsWith("Token_");

                }
            });

            for (File tokenFile : listFiles) {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(tokenFile));

                if (tokenFile.getName().equals(SSNConstants.SSN_FACEBOOK_TOKEN + ".ser")) {
                    AccessGrant facebookAccessGrant = (AccessGrant) obj.readObject();

                    // homeForm.setFacebookAccessGrant(facebookAccessGrant);
                    map.put("Facebook", facebookAccessGrant);
                    return map;
                } else if (tokenFile.getName().equals(SSNConstants.SSN_TWITTER_TOKEN + ".ser")) {
                    OAuthToken twitterOAuthToken = (OAuthToken) obj.readObject();

                    // homeForm.setTwitterOAuthToken(twitterOAuthToken);
                    map.put("Twitter", twitterOAuthToken);
                    return map;
                } else if (tokenFile.getName().equals(SSNConstants.SSN_INSTAGRAM_TOKEN + ".ser")) {
                    AccessGrant instagramAccessGrant = (AccessGrant) obj.readObject();

                    // homeForm.setTwitterOAuthToken(twitterOAuthToken);
                    map.put("Instagram", instagramAccessGrant);
                    return map;
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }

        return null;
    }

    /**
     * This method will create a zip file with set of files need to be shared 
     * @param files
     * @return
     */
    public static String createZipFileFromMultipleFiles(Set<String> files) {
        boolean success = false;

        String temp = getSsnTempDirPath();
        String zipFile = temp + File.separator + "shareImages.zip";

        File tempDir = new File(temp);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }

        String[] srcFiles = files.toArray(new String[0]);

        try {

            // create byte buffer
            byte[] buffer = new byte[1024];

            FileOutputStream fos = new FileOutputStream(zipFile);

            ZipOutputStream zos = new ZipOutputStream(fos);

            for (int i = 0; i < srcFiles.length; i++) {

                File srcFile = new File(srcFiles[i]);

                FileInputStream fis = new FileInputStream(srcFile);

                // begin writing a new ZIP entry, positions the stream to the start of the entry data
                zos.putNextEntry(new ZipEntry(srcFile.getName()));

                int length;

                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();

                // close the InputStream
                fis.close();

            }
            // close the ZipOutputStream
            zos.close();
            success = true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return success ? zipFile : "";
    }

     /**
     * This method will create a zip file with set of files need to be shared 
     * @param files
     * @return
     */
    public static String createZipFileFromMultipleFiles(Set<String> files,String filePath) {
        boolean success = false;

        String temp = getSsnTempDirPath();
        String zipFile = filePath;
        if(files.size()>1){
            if(!zipFile.contains(".zip")){
                zipFile=zipFile+".zip";
            }
            File tempDir = new File(temp);
            if (!tempDir.exists()) {
                tempDir.mkdir();
            }

            String[] srcFiles = files.toArray(new String[0]);

            try {

                // create byte buffer
                byte[] buffer = new byte[1024];

                FileOutputStream fos = new FileOutputStream(zipFile);

                ZipOutputStream zos = new ZipOutputStream(fos);

                for (int i = 0; i < srcFiles.length; i++) {

                    File srcFile = new File(srcFiles[i]);

                    FileInputStream fis = new FileInputStream(srcFile);

                    // begin writing a new ZIP entry, positions the stream to the start of the entry data
                    zos.putNextEntry(new ZipEntry(srcFile.getName()));

                    int length;

                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();

                    // close the InputStream
                    fis.close();

                }
                // close the ZipOutputStream
                zos.close();
                success = true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }else{
            Iterator<String> selectedFile   =   files.iterator();
          
            File srcFile = new File(selectedFile.next());
            String fileType = srcFile.getName().substring(srcFile.getName().lastIndexOf(".")+1,srcFile.getName().length());
            zipFile = filePath.contains(".")?filePath:filePath+"."+fileType;
            System.out.println("zipFile "+ zipFile);
            File destinationFile = new File(zipFile);
            try {
                FileUtils.copyFile(srcFile , destinationFile);
                success = true;
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(SSNHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

        return success ? zipFile : "";
    }
    /**
     * This method is to scale an image in the custom size 
     * @param image
     * @param ssnHomeForm
     * @param rotate
     * @param multiplier
     * @return
     */
    public static Image getScaledImage(BufferedImage image, SSNHomeForm ssnHomeForm, boolean rotate, double multiplier) {
        double imageHeight = image.getHeight();
        double imageWidth = image.getWidth();
        double screenWidth;
        double screenHeight;
        if (rotate) {
            screenWidth = ssnHomeForm.getSsnHomeCenterPanel().getHeight();
            screenHeight = ssnHomeForm.getSsnHomeCenterPanel().getWidth();
        } else {
            screenHeight = ssnHomeForm.getSsnHomeCenterPanel().getHeight();
            screenWidth = ssnHomeForm.getSsnHomeCenterPanel().getWidth();
        }
        double aspectRatio;
        double finalHeight;
        double finalWidth;
        if (imageWidth < screenWidth && imageHeight < screenHeight) {
            int finalImageWidth = (int) (imageWidth * multiplier);
            int finalImageHeight = (int) (imageHeight * multiplier);
            if(finalImageWidth>screenWidth)
            {
                finalImageWidth = (int) screenWidth;
            }
            if(finalImageHeight>screenHeight)
            {
                finalImageHeight = (int) screenHeight;
            }
            Image background = image.getScaledInstance(finalImageWidth, finalImageHeight, Image.SCALE_SMOOTH);
            return background;
        } else {

            double maintainedRatioforWidth = screenWidth / imageWidth;
            double maintainedRationforheigh = screenHeight / imageHeight;
            if (maintainedRatioforWidth < maintainedRationforheigh) {
                finalHeight = imageHeight * maintainedRatioforWidth;
                finalWidth = imageWidth * maintainedRatioforWidth;
            } else {
                finalHeight = imageHeight * maintainedRationforheigh;
                finalWidth = imageWidth * maintainedRationforheigh;
            }

        }
        int finalImageWidth = (int) (finalWidth * multiplier);
        int finalImageHeight = (int) (finalHeight * multiplier);

         if(finalImageWidth>screenWidth)
            {
                finalImageWidth = (int) screenWidth;
            }
         if(finalImageHeight>screenHeight)
            {
                finalImageHeight = (int) screenHeight;
            }
        Image background = image.getScaledInstance(finalImageWidth, finalImageHeight, Image.SCALE_SMOOTH);
        return background;
    }

    /**
     * Get scaled co-ordinates of tagged image if the image required to be fit in gallery
     * @param x
     * @param y
     * @param width
     * @param height
     * @param image
     * @param ssnHomeForm
     * @return
     */
    public static float[] getScaledCoordinatesofTaggedFace(float x, float y, float width, float height, BufferedImage image, SSNHomeForm ssnHomeForm) {
        double imageHeight = image.getHeight();
        double imageWidth = image.getWidth();
        double screenWidth = ssnHomeForm.getSsnHomeCenterPanel().getWidth();
        double screenHeight = ssnHomeForm.getSsnHomeCenterPanel().getHeight();
        
        if (imageWidth < screenWidth && imageHeight < screenHeight) {
            
            return null;
        } else {
            float[] dimension = new float[4];
            double maintainedRatioforWidth = screenWidth / imageWidth;
            double maintainedRationforheigh = screenHeight / imageHeight;
            //System.out.println("maintainedRatioforWidth : " + maintainedRatioforWidth);
            //System.out.println("maintainedRationforheigh : " + maintainedRationforheigh);
            if (maintainedRatioforWidth < maintainedRationforheigh) {
                dimension[0] = (float) (x * maintainedRatioforWidth);
                dimension[1] = (float) (y * maintainedRatioforWidth);
                dimension[2] = (float) (width * maintainedRatioforWidth);
                dimension[3] = (float) (height * maintainedRatioforWidth);
            } else {
                dimension[0] = (float) (x * maintainedRationforheigh);
                dimension[1] = (float) (y * maintainedRationforheigh);
                dimension[2] = (float) (width * maintainedRationforheigh);
                dimension[3] = (float) (height * maintainedRationforheigh);
            }
            return dimension;
        }
    }

    /**
     * Converts the image to a buffered image instance
     * @param img
     * @return
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    public static String getDeviceType(){
         String OS = System.getProperty("os.name").toLowerCase();
  
         String deviceType = "";
  
            if(OS.contains("windows") )
                deviceType = "Desktop-Win";
            else
                deviceType = "Desktop-MAC";
            return deviceType;
    }

    public static String formatDate(Date date){
        String dateString = "";
        try{
            SimpleDateFormat dateFormat =   new SimpleDateFormat("MM-dd-yy hh:mm s");
            dateString  =   dateFormat.format(date);
                    
        }catch(Exception ee){
            ee.printStackTrace();
        }
        return dateString;
    }
    public static String formatDate(String date){
        String dateString = "";
        try{
            SimpleDateFormat dateFormat =   new SimpleDateFormat("MM-dd-yy hh:mm s");
            dateString  =   dateFormat.format(date);
                    
        }catch(Exception ee){
            ee.printStackTrace();
        }
        return dateString;
    }
     public static String formatDateInMMDDYYYY(String inputDate){
        String dateString = "";
        try{
            //System.out.println("inputDate " + inputDate);
              SimpleDateFormat dateFormat1 =   new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormat =   new SimpleDateFormat("MM-dd-yyyy");
            dateString  =   dateFormat.format(dateFormat1.parse(inputDate));
                    
        }catch(Exception ee){
            ee.printStackTrace();
        }
        return dateString;
    }
    
    public static String getContentType(String extension){
        String contentType = "";
        switch(extension){
            case "JPG" : contentType = "image/jpg";
                        break;
            case "PNG" : contentType = "image/png";
                        break;
            case "JPEG" : contentType = "image/jpeg";
                        break;
            case "GIF" : contentType = "image/gif";
                        break;
            case "MP4" : contentType = "video/mp4";
                        break;
            case "WMV" : contentType = "video/x-ms-wmv";
                        break;
            case "qt" : contentType = "video/quicktime";
                        break;
            case "flv" : contentType = "video/x-flv";
                        break;
            case "mpeg" : contentType = "application/x-mpegURL";
                        break;
            case "MP2" : contentType = "video/MP2T";
                        break;
            case "3gp" : contentType = "video/3gpp";
                        break;
            case "msvideo" : contentType = "video/x-msvideo";
                        break;
                
        }
        return contentType;
    }
    public static boolean isInternetReachable()
        {
            try {
                //make a URL to a known source
                URL url = new URL(SSNConstants.SSN_WEB_HOST);

                //open a connection to that source
                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

                //trying to retrieve data from the source. If there
                //is no connection, this line will fail
                Object objData = urlConnect.getContent();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            return true;
        }
    
}
