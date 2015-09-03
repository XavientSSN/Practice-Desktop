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
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pkumar2
 */
public class SSNVideoMetadata {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNVideoMetadata.class);
    public static String readMetaDataForTitle(File f) {
        String mData = "";
         IsoFile isoFile = null;
          FileChannel fc =null;
        try {
            fc = new RandomAccessFile(f, "r").getChannel();;

            isoFile = new IsoFile(fc);
    

            MovieBox moov = isoFile.getMovieBox();
            UserDataBox udta = null;
            if(moov!=null){
            
                if(moov.getBoxes(UserDataBox.class).size() > 0){    
                    udta = moov.getBoxes(UserDataBox.class).get(0);

                    MetaBox mdta = udta.getBoxes(MetaBox.class).get(0);

                    AppleItemListBox adta = mdta.getBoxes(AppleItemListBox.class).get(0);

                    AppleTrackTitleBox title = null;
                    if (adta.getBoxes(AppleTrackTitleBox.class) != null && adta.getBoxes(AppleTrackTitleBox.class).size() > 0) {
                        title = adta.getBoxes(AppleTrackTitleBox.class).get(0);
                        mData = title.getValue().trim();
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(SSNVideoMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(isoFile != null)
                    isoFile.close();
                if(fc!=null)
                    fc.close();
            } catch (IOException ex) {
                Logger.getLogger(SSNVideoMetadata.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return mData;
    }

    public static boolean writeVideoMetadata(final File f, String vdTitle, String vdComments) {
        boolean flag = false;
        long sizeBefore;
        long sizeAfter;
        File changedMetadataVideoFile = null;
        FileChannel outPutChannel = null;
        FileChannel fc = null;
        IsoFile isoFile = null;
        FileLock lock = null; // The lock object we hold
        try {
            fc = new RandomAccessFile(f, "r").getChannel();

            isoFile = new IsoFile(fc);

            String str = f.getAbsolutePath();

            MovieBox moov = isoFile.getMovieBox();
            UserDataBox udta = moov.getBoxes(UserDataBox.class).get(0);
            sizeBefore = udta.getSize();
            MetaBox mdta = udta.getBoxes(MetaBox.class).get(0);
            AppleItemListBox adta = mdta.getBoxes(AppleItemListBox.class).get(0);
            AppleTrackTitleBox title = null;
            if (adta.getBoxes(AppleTrackTitleBox.class) != null && adta.getBoxes(AppleTrackTitleBox.class).size() > 0) {
                title = adta.getBoxes(AppleTrackTitleBox.class).get(0);
                title.setValue(vdTitle);
            } else {
                title = new AppleTrackTitleBox();
                title.setValue(vdTitle);
                adta.addBox(title);
            }

            AppleCommentBox comment = null;
            if (adta.getBoxes(AppleCommentBox.class) != null && adta.getBoxes(AppleCommentBox.class).size() > 0) {
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
            
            //changedMetadataVideoFile = new File(f);
            outPutChannel = new RandomAccessFile(f, "rw").getChannel();
            //outPutChannel = new FileOutputStream(changedMetadataVideoFile).getChannel();

            isoFile.getBox(outPutChannel);
            fc.force(true);
            outPutChannel.force(true);

            flag = true;
        } catch (IOException ex) {
            ex.printStackTrace();
            //Logger.getLogger(SSNVideoMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception ee){
            ee.printStackTrace();
        }finally {

            try {

                if (outPutChannel != null) {
                    outPutChannel.close();
                    outPutChannel = null;
                }
                if(isoFile !=null )
                    isoFile.close();
                    
                isoFile = null;
                if (fc != null) {
                    fc.close();
                    fc = null;
                }
            } catch (IOException ex) {
                Logger.getLogger(SSNVideoMetadata.class.getName()).log(Level.SEVERE, null, ex);
            }

            return flag;
        }

    }

    public static SSNGalleryMetaData readVideoMetadata(File f) {
        SSNGalleryMetaData data = new SSNGalleryMetaData();
        IsoFile isoFile = null;
         FileChannel fc=null;
        try {

            data.setMediaType(getFileExtension(f));

            fc= new RandomAccessFile(f, "r").getChannel();;

            

            isoFile = new IsoFile(fc);

            MovieBox moov = isoFile.getMovieBox();
            if(moov!=null){
                UserDataBox udta = null;
                MetaBox mdta      =   null;
                AppleItemListBox adta =null;
                if(moov.getBoxes(UserDataBox.class).size() > 0)  {
                     udta= moov.getBoxes(UserDataBox.class).get(0);

                if(udta.getBoxes(MetaBox.class).size() > 0)  
                     mdta = udta.getBoxes(MetaBox.class).get(0);
                
                if(udta.getBoxes(MetaBox.class).size() > 0)  
                   adta = mdta.getBoxes(AppleItemListBox.class).get(0);

                    AppleTrackTitleBox title = null;
                    if (adta.getBoxes(AppleTrackTitleBox.class) != null && adta.getBoxes(AppleTrackTitleBox.class).size() > 0) {
                        title = adta.getBoxes(AppleTrackTitleBox.class).get(0);
                        data.setTitle(title.getValue());


                        if (title.getValue() == null && title.getValue().equals("")) {
                            data.setTitle(f.getName());
                        } else {
                            data.setTitle(title.getValue());
                        }

                    } else {
                        data.setTitle(f.getName());
                    }

                    AppleCommentBox comment = null;

                    if (adta.getBoxes(AppleCommentBox.class) != null && adta.getBoxes(AppleCommentBox.class).size() > 0) {

                        comment = adta.getBoxes(AppleCommentBox.class).get(0);
                        data.setUserComments(comment.getValue().trim());
                    }
                }
          }
        } catch (IOException ex) {
            Logger.getLogger(SSNVideoMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
               if(isoFile != null)
                    isoFile.close();
                if(fc!=null)
                    fc.close();
            } catch (IOException ex) {
                Logger.getLogger(SSNVideoMetadata.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return data;
    }

    private static String getFileExtension(File file) {
        String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());

        return fileExtension.toLowerCase();
    }

    public static boolean needsOffsetCorrection(IsoFile isoFile) {

        if (Path.getPaths(isoFile, "mdat").size() > 1) {
            // throw new RuntimeException("There might be the weird case that a file has two mdats. One before" +
            //        " moov and one after moov. That would need special handling therefore I just throw an " +
            //      "exception here. ");
        }

        if (Path.getPaths(isoFile, "moof").size() > 0) {
            //  throw new RuntimeException("Fragmented MP4 files need correction, too. (But I would need to look where)");
        }

        for (Box box : isoFile.getBoxes()) {
            if ("mdat".equals(box.getType())) {
                return false;
            }
            if ("moov".equals(box.getType())) {
                return true;
            }
        }
        throw new RuntimeException("Hmmm - shouldn't happen");
    }

    private static void correctChunkOffsets(IsoFile tempIsoFile, long correction) {
        List<Box> chunkOffsetBoxes = Path.getPaths(tempIsoFile, "/moov[0]/trak/mdia[0]/minf[0]/stbl[0]/stco[0]");
        for (Box chunkOffsetBox : chunkOffsetBoxes) {

            LinkedList<Box> stblChildren = new LinkedList<Box>(chunkOffsetBox.getParent().getBoxes());
            stblChildren.remove(chunkOffsetBox);

            long[] cOffsets = ((ChunkOffsetBox) chunkOffsetBox).getChunkOffsets();
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
