
package com.ssn.ui.custom.component;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.ChunkOffsetBox;
import com.coremedia.iso.boxes.MetaBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import com.coremedia.iso.boxes.UserDataBox;
import com.coremedia.iso.boxes.apple.AppleCommentBox;
import com.coremedia.iso.boxes.apple.AppleItemListBox;
import com.coremedia.iso.boxes.apple.AppleTrackTitleBox;
import com.googlecode.mp4parser.util.Path;
import com.ssn.app.loader.SSNConstants;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNAddressConverterHelper;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.model.TaggedFace;
import static com.ssn.ui.custom.component.SSNVideoMetadata.needsOffsetCorrection;
import com.ssn.ui.form.SSNHomeForm;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Painter;
import javax.swing.UIDefaults;
import org.apache.log4j.Logger;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.constants.TiffFieldTypeConstants;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputField;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;

/**
 *
 * @author pkumar2
 */
public class SSMMediaGalleryPanel {
    
    private static TiffField t;
    final    private static Logger log = Logger.getLogger(SSNGalleryHelper.class);
    
   
    
    public static JPanel populateMediaSummaryPanel(File file, SSNHomeForm homeform) {
        SSNGalleryMetaData data = null;
        if (file != null) {
            boolean check = false;
            try {
                
                List<String> videoSupportedList = Arrays.asList(SSNConstants.SSN_METADATA_FORMAT_SUPPORTED);
                String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
                if (videoSupportedList.contains(fileExtension.toUpperCase())) {
                    check = SSNDao.checkMediaExist(file.getAbsolutePath());
                    if (check) {
                        String mTitle = "";
                        if (checkVideo(file)) {
                            mTitle = SSNVideoMetadata.readMetaDataForTitle(file);
                        } else {
                            mTitle = readMetaDataForTitle(file);
                        }
                        data = SSNDao.getSSNMetaData(file.getAbsolutePath());
                        data.setTitle(mTitle);
                    } else {
                        if (checkVideo(file)) {
                            data = SSNVideoMetadata.readVideoMetadata(file);
                            data.setMediaLocation("");
                        } else {
                            data = readMetaData(file);

                            
                        }
                    }
                } else {
                    data = new SSNGalleryMetaData();
                    data.setMediaType(fileExtension.toUpperCase());
                    data.setTitle(file.getName().trim());
                }
                data.setMediaFileName(file.getName());
                
            } catch (SQLException ex) {
                log.error(ex);
            }            
        } else {
            data = new SSNGalleryMetaData();
        }
        
        List<TaggedFace> faces=null;
        if(file.getAbsolutePath() != null){
            try {
                if(SSNDao.getTaggedFaces(file.getAbsolutePath())!= null){
                    faces = SSNDao.getTaggedFaces(file.getAbsolutePath());
                }else{
                    faces = new ArrayList<>();
                }
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(SSMMediaGalleryPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(faces != null){
            if(faces.size()>0){
                int counter = 0;
             StringBuilder sb = new StringBuilder("");
                for(TaggedFace tf : faces){
                    if(!tf.getTags().equals(""))
                    {
                        if(counter>=1){
                            sb.append(",");
                            sb.append(tf.getTags());
                        }
                        else
                        {
                             sb.append(tf.getTags());
                        }
                    }
                    counter++;
                }

                data.setFaceTags(sb.toString());
            }
        }
        
        data.setMediaFileLocation(file.getAbsolutePath());
        
        SSNMetaDataPanel mPanel = new SSNMetaDataPanel(data, false, homeform);
        mPanel.setBorder((BorderFactory.createEmptyBorder(0, 10, 10, 10)));
        mPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        
        ArrayList<JTextField> jtxt = mPanel.getAllTextField();
        UIDefaults d = getUidefaults();
        for (JTextField tx : jtxt) {
            tx.setEnabled(false);
            tx.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
           tx.putClientProperty("Nimbus.Overrides", d);
        }
        
        JPanel mainPanel = new JPanel();        
        JPanel bottomPanel = new JPanel();        
        bottomPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        // JLabel iconLabel=new JLabel(new ImageIcon(data.getClass().getResource("/images/right-bottom-bg.jpg")), SwingConstants.HORIZONTAL);
        //bottomPanel.add(iconLabel);
        bottomPanel.setBorder((BorderFactory.createEmptyBorder(70, 0, 0, 0)));
        
        BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS);
        mainPanel.setLayout(boxLayout);
        mainPanel.add(mPanel);
        mainPanel.add(bottomPanel);
        
        return mainPanel;
        
    }
    
    public static boolean checkVideo(File file) {
     
        List<String> videoSupportedList = Arrays.asList(SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED);
        String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
        return videoSupportedList.contains(fileExtension.toUpperCase());
    }
    
    public static UIDefaults getUidefaults() {
        
        UIDefaults overrides = new UIDefaults();
        
        overrides.put("TextField[Disabled].backgroundPainter", new Painter<JTextField>() {
            
            @Override
            public void paint(Graphics2D g, JTextField field, int width, int height) {
                g.setColor(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                Insets insets = field.getInsets();
                g.fill(new Rectangle(
                        insets.left,
                        insets.top,
                        width - (insets.left + insets.right),
                        height - (insets.top + insets.bottom)));
            }
            
        });
        
        return overrides;
    }
    
    public static JPanel populateMediaFolderPanel(SSNGalleryMetaData gData) {
        SSNGalleryMetaData data = null;
        if (gData != null) {
            
            data = gData;            
        } else {
            data = new SSNGalleryMetaData();
        }
        
        SSNMetaDataPanel mPanel = new SSNMetaDataPanel(data, true, null);
        mPanel.setBorder((BorderFactory.createEmptyBorder(0, 10, 10, 10)));
        
        mPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);        
        JPanel mainPanel = new JPanel();        
    
        mainPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS);
        mainPanel.setLayout(boxLayout);
        mainPanel.add(mPanel);
       
        return mainPanel;
        
    }
    
    public static SSNGalleryMetaData readMetaData(File file) {
        SSNGalleryMetaData mData = new SSNGalleryMetaData();
        
        IImageMetadata metadata = null;
       // String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
        List<String> videoSupportedList = Arrays.asList(SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED);
        String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
        if (videoSupportedList.contains(fileExtension.toUpperCase())) {
            mData.setMediaType(fileExtension.toLowerCase());
        } else {
            
            try {
                metadata = Sanselan.getMetadata(file);
            } catch (ImageReadException | IOException e) {
                log.error(e);
            }
            
            try {
                //ImageInfo imageInfo = Sanselan.getImageInfo(file);
                mData.setMediaType(fileExtension.toLowerCase());
                
            } catch (Exception ex) {
                
                 log.error(ex);
            }            
            
            long flByt = file.length();
            double flKB = (double) flByt / 1024;
            double flMB = flKB / 1024;
            String s = String.format("%.2f", flMB);
            
            mData.setSize(s + " MB");
            
            if (metadata instanceof JpegImageMetadata) {
                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                
                mData.setMediaLocation(file.getParentFile().getPath());
                
                String mdDate = printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_DATE_TIME);
                mData.setModiFied(mdDate);
                
                String crDate = printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                mData.setCreated(crDate);
                
                String photoGrapher = printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_ARTIST);
                mData.setPhotoGrapher(photoGrapher.trim());
             
                
                String title = printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_IMAGE_DESCRIPTION);
                
                if (title == null || title.equals("")) {
                    mData.setTitle(file.getName().trim());
                } else {
                    mData.setTitle(title);
                }
                
                String usrCmnt = printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_USER_COMMENT);
                mData.setUserComments(usrCmnt);
                
                String keywords = printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_XPKEYWORDS);
                
              
                    mData.setSsnKeywords(keywords.trim());
              
                
                String rating = printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_RATING);
                mData.setSsnRatings(rating);

                // simple interface to GPS data
                TiffImageMetadata exifMetadata = jpegMetadata.getExif();
                if (exifMetadata != null) {
                    try {
                        TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                        if (null != gpsInfo) {
                            double longitude = gpsInfo.getLongitudeAsDegreesEast();
                            double latitude = gpsInfo.getLatitudeAsDegreesNorth();
                            mData.setLatitude(String.valueOf(latitude));
                            mData.setLongitude(String.valueOf(longitude));
                        }
                    } catch (ImageReadException e) {
                        log.error(e);
                    }
                }
                
            }
        }
        
        return mData;
    }
    
    public static String readMetaDataForTitle(File file) {
        String mData = "";
        
        IImageMetadata metadata = null;
        try {
            metadata = Sanselan.getMetadata(file);
        } catch (ImageReadException | IOException ex) {
            log.error(ex);
        }
        
        if (metadata instanceof JpegImageMetadata) {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            
            mData = printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_IMAGE_DESCRIPTION);
        }
        
        return mData;
    }
    
    public static boolean writeImageMetaData(File file, String title, String keyword, String comments, String place, String album, String attachedVid, String ratings, String photoGrapher) {
        boolean flag = false;
        try {
            String userTitleFieldData = title;            
            String userKeywordFieldData = keyword;            
            String userCommentFieldData = comments;            
          //  String userPlaceFieldData = place;            
          //  String userAlbumFieldData = album;            
          //  String userAttachedVidFieldData = attachedVid;            
            String userRatingsFieldData = ratings;            
            String artistFieldData = photoGrapher;
            
            IImageMetadata metadata = null;
            JpegImageMetadata jpegMetadata = null;
            TiffImageMetadata exif = null;
            
            TiffOutputSet outputSet = new TiffOutputSet();
            
            try {
                metadata = Sanselan.getMetadata(file);
            } catch (ImageReadException | IOException e) {
           
                 log.error(e);
            }
            
            if (metadata != null) {
                jpegMetadata = (JpegImageMetadata) metadata;
            }
            
            if (jpegMetadata != null) {
                exif = jpegMetadata.getExif();
            }
            
            if (exif != null) {
                try {
                    outputSet = exif.getOutputSet();
                } catch (ImageWriteException e) {
                    
                     log.error(e);
                }
            }
            
            if (outputSet != null) {                
                TiffOutputField tiffOutputField = null;
                if (userTitleFieldData != null) {
                    tiffOutputField = getTiffOutputFieldTitle(outputSet, userTitleFieldData);
                    changeImageMetadata(file, outputSet, tiffOutputField, "Title");
                } 
                if (userKeywordFieldData != null) {
                    
                    tiffOutputField = getTiffOutputFieldKeyword(outputSet, userKeywordFieldData);
                    changeImageMetadata(file, outputSet, tiffOutputField, "Keyword");
                } 
                if (userCommentFieldData != null) {
                    tiffOutputField = getTiffOutputFieldComment(outputSet, userCommentFieldData);
                    changeImageMetadata(file, outputSet, tiffOutputField, "Comment");
                }
                
                if (artistFieldData != null) {
                    tiffOutputField = getTiffOutputFieldPhotographer(outputSet, artistFieldData);
                    changeImageMetadata(file, outputSet, tiffOutputField, "Artist");
                } 
                
                
                tiffOutputField = getTiffOutputFieldRating(outputSet, userRatingsFieldData);
                changeImageMetadata(file, outputSet, tiffOutputField, "Rating");

                
            }
            flag = true;
        } catch (Exception e) {
            
          log.error(e);
        } finally {
            return flag;
        }
        
    }
    
    public static void changeImageMetadata(File file, TiffOutputSet outputSet, TiffOutputField tiffOutputField, String tag) {        
        
        File changedMetadataImageFile = null;
        try {
            
            OutputStream outputStream = null;
            
            TiffOutputDirectory exifDirectory;
            try {
                
                if (tag.equals("Comment")) {
                    exifDirectory = outputSet.getOrCreateExifDirectory();                    
                } else {
                    exifDirectory = outputSet.getOrCreateRootDirectory();                    
                }
                exifDirectory.add(tiffOutputField);

            } catch (ImageWriteException e) {
                log.error(e);
            }
            
            try {
                
                String str = file.getAbsolutePath();
                str = str.substring(0, str.lastIndexOf(File.separator) + 1);
                String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator) + 1,file.getAbsolutePath().length());
                
                changedMetadataImageFile = new File(str + "SSNTMP."+extension);

                outputStream = new FileOutputStream(changedMetadataImageFile);
                outputStream = new BufferedOutputStream(outputStream);
            } catch (IOException e) {
                log.error(e);
            }
            //updateExifMetadataLossless
            try {
                new ExifRewriter().updateExifMetadataLossy(file, outputStream, outputSet);
            } catch (ImageReadException e) {
               log.error(e);
               log.error(file.getAbsolutePath());
            } catch (ImageWriteException e) {
               log.error(e);
               log.error(file.getAbsolutePath());
            } catch (IOException e) {
               log.error(e);
               log.error(file.getAbsolutePath());
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
            }
            
        } catch (Exception e) {
           log.error(e);
        } finally {
            file.delete();
            if(changedMetadataImageFile != null)
                changedMetadataImageFile.renameTo(file);            
            
        }
        
    }
    
    public static TiffOutputField getTiffOutputFieldComment(TiffOutputSet outputSet, String metaDataToChange) {
        TiffOutputField imageHistoryPre = outputSet.findField(TiffConstants.EXIF_TAG_USER_COMMENT);
        if (imageHistoryPre != null) {
            outputSet.removeField(TiffConstants.EXIF_TAG_USER_COMMENT);
        }        
        
        TiffOutputField tiffOutputField = new TiffOutputField(TiffConstants.EXIF_TAG_USER_COMMENT, TiffFieldTypeConstants.FIELD_TYPE_ASCII,
                metaDataToChange.length(),
                metaDataToChange.getBytes());        
        
        return tiffOutputField;
    }
    
    public static TiffOutputField getTiffOutputFieldTitle(TiffOutputSet outputSet, String metaDataToChange) {
        TiffOutputField imageHistoryPre = outputSet.findField(TiffConstants.EXIF_TAG_IMAGE_DESCRIPTION);
        if (imageHistoryPre != null) {
            outputSet.removeField(TiffConstants.EXIF_TAG_IMAGE_DESCRIPTION);
        }        
        
        TiffOutputField tiffOutputField = new TiffOutputField(TiffConstants.EXIF_TAG_IMAGE_DESCRIPTION,
                TiffFieldTypeConstants.FIELD_TYPE_ASCII,
                metaDataToChange.length(),
                metaDataToChange.getBytes());

		
        return tiffOutputField;
    }
    
    public static TiffOutputField getTiffOutputFieldKeyword(TiffOutputSet outputSet, String metaDataToChange) {
        TiffOutputField imageHistoryPre = outputSet.findField(TiffConstants.EXIF_TAG_XPKEYWORDS);
        if (imageHistoryPre != null) {
            outputSet.removeField(TiffConstants.EXIF_TAG_XPKEYWORDS);
        }        
        
        TiffOutputField tiffOutputField = new TiffOutputField(TiffConstants.EXIF_TAG_XPKEYWORDS,
                TiffFieldTypeConstants.FIELD_TYPE_ASCII,
                metaDataToChange.length(),
                metaDataToChange.getBytes());
        
        return tiffOutputField;
    }
    
    public static TiffOutputField getTiffOutputFieldPhotographer(TiffOutputSet outputSet, String metaDataToChange) {
        TiffOutputField imageHistoryPre = outputSet.findField(TiffConstants.EXIF_TAG_ARTIST);
        if (imageHistoryPre != null) {
            outputSet.removeField(TiffConstants.EXIF_TAG_ARTIST);
        }        
        
        TiffOutputField tiffOutputField = new TiffOutputField(TiffConstants.EXIF_TAG_ARTIST,
                TiffFieldTypeConstants.FIELD_TYPE_ASCII,
                metaDataToChange.length(),
                metaDataToChange.getBytes());
        
        return tiffOutputField;
    }
    
    public static TiffOutputField getTiffOutputFieldRating(TiffOutputSet outputSet, String metaDataToChange) {
        TiffOutputField imageHistoryPre = outputSet.findField(TiffConstants.EXIF_TAG_RATING);
        if (imageHistoryPre != null) {
            outputSet.removeField(TiffConstants.EXIF_TAG_RATING);
        }        
        
        TiffOutputField tiffOutputField = new TiffOutputField(TiffConstants.EXIF_TAG_RATING,
                TiffFieldTypeConstants.FIELD_TYPE_ASCII,
                1,
                metaDataToChange.getBytes());
        
        return tiffOutputField;
    }
    
    public static String printTagValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) {
        String tval = "";
        TiffField field = jpegMetadata.findEXIFValue(tagInfo);
        if (field == null) {
        } else {
            try {
                tval = field.getValue().toString();
            } catch (ImageReadException ex) {
               log.error(ex);
            }
        }
        return tval;
    }
    
    public static SSNGalleryMetaData readMetaData1(File file) {
        SSNGalleryMetaData mData = new SSNGalleryMetaData();
        IImageMetadata metadata = null;
        String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
        List<String> videoSupportedList = Arrays.asList(videoSupported);
        String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
        if (videoSupportedList.contains(fileExtension.toUpperCase())) {
            mData.setMediaType(fileExtension.toLowerCase());
        } else {
            
            try {
                metadata = Sanselan.getMetadata(file);
            } catch (ImageReadException | IOException e) {
               log.error(e);
            }
            
            try {
                ImageInfo imageInfo = Sanselan.getImageInfo(file);
                if (imageInfo.getFormatName().length() > 4) {
                    mData.setMediaType(imageInfo.getFormatName().substring(0, 4));
                } else {
                    mData.setMediaType(imageInfo.getFormatName());
                }

            } catch (ImageReadException | IOException ex) {
                log.error(ex);
            }            
            
            long flByt = file.length();
            double flKB = (double) flByt / 1024;
            double flMB = flKB / 1024;
            String s = String.format("%.2f", flMB);
            
            mData.setSize(s + " MB");
            
            if (metadata instanceof JpegImageMetadata) {
                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                
                mData.setMediaLocation(file.getParentFile().getPath());
                
                
                String title = printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_IMAGE_DESCRIPTION);
                
                String mdDate = printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_DATE_TIME);
                mData.setModiFied(mdDate);
                
                String crDate = printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                mData.setCreated(crDate);
                
                String usrCmnt = printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_USER_COMMENT);
                mData.setUserComments(usrCmnt);
                
                printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_CREATE_DATE);
                printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_ISO);
                printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_SHUTTER_SPEED_VALUE);
                printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_APERTURE_VALUE);
                printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_BRIGHTNESS_VALUE);

                // simple interface to GPS data
                TiffImageMetadata exifMetadata = jpegMetadata.getExif();
                if (exifMetadata != null) {
                    try {
                        TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                        if (null != gpsInfo) {
                            double longitude = gpsInfo.getLongitudeAsDegreesEast();
                            double latitude = gpsInfo.getLatitudeAsDegreesNorth();
                            
                            
                            mData.setLatitude(String.valueOf(latitude));
                            mData.setLongitude(String.valueOf(longitude));
                        }
                    } catch (ImageReadException e) {
                        log.error(e);
                    }
                }
                
                ArrayList<?> items = jpegMetadata.getItems();
                for (int i = 0; i < items.size(); i++) {
                    try {
                        Object item = items.get(i);
                        
                        TagInfo tagInfo = ((TiffImageMetadata.Item) item).getTiffField().tagInfo;
                        TiffField field = jpegMetadata.findEXIFValue(tagInfo);
                        if (field == null) {
                        } else {
                            
                            if (tagInfo.name.equals("Rating")) {
                                mData.setSsnRatings(field.getValueDescription());
                            }
                           
                            
                        }
                        
                    } catch (Exception ex) {
                        log.error(ex);
                    }
                }
                
            }
        }
        
        return mData;
    }
    @SuppressWarnings("resource")
    public static boolean writeVideoMetadata(final File f, String vdTitle,String vdComments) {
            boolean flag = false;
            long sizeBefore;
            long sizeAfter;
            FileChannel outPutChannel = null;
            FileChannel fc = null;
            IsoFile isoFile = null;
            try {
                    fc = new RandomAccessFile(f, "r").getChannel();
                    isoFile = new IsoFile(fc);
                    String str = f.getAbsolutePath();
                    MovieBox moov = isoFile.getMovieBox();
                    UserDataBox udta = moov.getBoxes(UserDataBox.class).get(0);
                    sizeBefore = udta.getSize();
                    MetaBox mdta = udta.getBoxes(MetaBox.class).get(0);
                    AppleItemListBox adta = mdta.getBoxes(AppleItemListBox.class)
                                    .get(0);
                    AppleTrackTitleBox title = null;
                    if (adta.getBoxes(AppleTrackTitleBox.class) != null
                                    && adta.getBoxes(AppleTrackTitleBox.class).size() > 0) {
                            title = adta.getBoxes(AppleTrackTitleBox.class).get(0);
                            title.setValue(vdTitle);
                    } else {
                            title = new AppleTrackTitleBox();
                            title.setValue(vdTitle);
                            adta.addBox(title);
                    }

                    AppleCommentBox comment = null;
                    if (adta.getBoxes(AppleCommentBox.class) != null
                                    && adta.getBoxes(AppleCommentBox.class).size() > 0) {
                            comment = adta.getBoxes(AppleCommentBox.class).get(0);
                            comment.setValue(vdComments);
                    } else {
                            comment = new AppleCommentBox();
                            comment.setValue(vdComments);
                            adta.addBox(comment);
                    }

                    sizeAfter = udta.getSize();
                    if (needsOffsetCorrection(isoFile)) {
                            correctChunkOffsets(isoFile, sizeAfter - sizeBefore);
                    }

                    str = str.substring(0, str.lastIndexOf(File.separator) + 1);
                    
                    outPutChannel = new RandomAccessFile(f, "rw").getChannel();

                    isoFile.getBox(outPutChannel);
                    fc.force(true);
                    outPutChannel.force(true);

                    flag = true;
            } catch (IOException ex) {
                    ex.printStackTrace();
            }
            return flag;
    }
    /**
	 * use to get correct chunk of {@link IsoFile} 
	 * @param tempIsoFile
	 * 			the iso file in which correction is needed
	 * @param correction
	 * 			the correction data
	 */
	private static void correctChunkOffsets(IsoFile tempIsoFile, long correction) {
		List<Box> chunkOffsetBoxes = Path.getPaths(tempIsoFile,
				"/moov[0]/trak/mdia[0]/minf[0]/stbl[0]/stco[0]");
		for (Box chunkOffsetBox : chunkOffsetBoxes) {
			LinkedList<Box> stblChildren = new LinkedList<Box>(chunkOffsetBox
					.getParent().getBoxes());
			stblChildren.remove(chunkOffsetBox);
			long[] cOffsets = ((ChunkOffsetBox) chunkOffsetBox)
					.getChunkOffsets();
			for (int i = 0; i < cOffsets.length; i++) {
				cOffsets[i] += correction;
			}
			StaticChunkOffsetBox cob = new StaticChunkOffsetBox();
			cob.setChunkOffsets(cOffsets);
			stblChildren.add(cob);
			chunkOffsetBox.getParent().setBoxes(stblChildren);
		}
	}
}
