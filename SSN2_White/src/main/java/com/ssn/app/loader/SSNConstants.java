package com.ssn.app.loader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author vkvarma
 */
public interface SSNConstants {
    //app db version
    
    final public double APPLICATION_VERSION = 1.01;
    final public String SSN_CONFIG_FILE = "ssn-common.properties";
    final public String SSN_APPLICATION_DIRECTORY = ".ssn";
    final public String SSN_WORKSPACE_DIRECTORY = "SSN_WORKSPACE";
    final public String SSN_DB_DIRECTORY = "SSN_DB";
    final public String SSN_HIVE_DIRECTORY = "OurHive";
    final public String SSN_DEFAULT_DIRECTORY = "OurHive";

    final public String SSN_REMMEBER_ME_DIRECTORY = "Remember_Me";
    final public String SSN_IMAGE_SORT_ORDER_ASC = "ASC";
    final public String SSN_IMAGE_SORT_ORDER_DESC = "DESC";
    final public String SSN_TOOLBAR_HOME_LOGO = "Home_Logo";
    final public String SSN_TOOLBAR_HOME = "Home";
    final public String SSN_TOOLBAR_CREATE_ALBUM = "CreateAlbum";
    final public String SSN_TOOLBAR_UPLOAD_PHOTO = "UploadPhoto";
    final public String SSN_TOOLBAR_OPEN_CAMERA = "OpenCamera";
    final public String SSN_TOOLBAR_DELETE_PHOTO = "DeletePhoto";
    final public String SSN_TOOLBAR_SLIDE_SHOW = "SlideShow";
    final public String SSN_TOOLBAR_VIEW_ALL_ALBUM = "ViewAllAlbum";
    final public String SSN_TOOLBAR_ALL_UNTAGGED = "AllUntagged";
    final public String SSN_TOOLBAR_SHARE_MEDIA = "ShareMedia";
    final public String SSN_TOOLBAR_VOICE_SEARCH = "VoiceSearch";
    final public String SSN_TOOLBAR_TAGGED_FACES = "TaggedFaces";
    final public String SSN_TOOLBAR_RECENTLY_ADDED = "RecentlyAdded";
    final public String SSN_TOOLBAR_STARRED_MEDIA = "StarredMedia";
    final public String SSN_TOOLBAR_GIFT_SOUVENIR = "GiftSouvenir";
    final public String SSN_TOOLBAR_SYNC_MEDIA = "SynchronizeMedia";
    final public String SSN_FACEBOOK_TOKEN = "Token_Facebook";
    final public String SSN_TWITTER_TOKEN = "Token_Twitter";
    final public String SSN_INSTAGRAM_TOKEN = "Token_Instagram";

 
    final public String SSN_FACEBOOK_API_KEY = "455115731304500";
    final public String SSN_FACEBOOK_SECRET_KEY = "91e1cd0332d05e4914b5821e2336aed9";



    final public String SSN_TWITTER_API_KEY = "l9W1kE2Y6k4dMheo4ecLQ7t0C";
    final public String SSN_TWITTER_SECRET_KEY = "FiPDZJuc1AGjKOTJG8iqnBtXsOOzSM4uxOabloBnH3yQsjjYc9";

    final public String SSN_INSTAGRAM_CLIENT_ID = "7220227ed12a43c397cdb0575b128909";
    final public String SSN_INSTAGRAM_CLIENT_SECRET = "1dd2501103ba44aa9024cdf8f8788fec";

    final public String[] SSN_VIDEO_FORMAT_SUPPORTED = {"WMV", "MP4", "AVI", "MOV", "3GP"};
    final public String[] SSN_UPLOAD_FILE_FORMAT_SUPPORTED = {"WMV", "MP4", "AVI", "MOV", "3GP", "JPG", "JPEG", "PNG", "GIF"};
    final public String[] SSN_SLIDESHOW_IMAGE_FILE_FORMAT_SUPPORTED = {"JPG", "JPEG", "BMP", "PNG", "TIFF", "GIF"};
    final public String[] SSN_METADATA_FORMAT_SUPPORTED = {"JPG", "JPEG","PNG","MP4", "MOV", "3GP","WMV"};
    final public String SSN_TOOLBAR_SCHEDULE_TAG = "ScheduleTag";
    final public String SSN_VOICE_NOTE_DIRECTORY = "Voice_Notes";
    final public String SSN_TEMP_DIRECTORY = ".temp";
    final public String SSN_FACEBOOK_PHOTOS_DIRECTORY = "Facebook";
    final public String SSN_INSTAGRAM_PHOTOS_DIRECTORY = "Instagram Media";
    final public String SSN_FACE_RECOGNITION = "FaceRecognition";
    final public String SSN_CLOUD_SYNC = "CLoudSync";
    final public String SSN_SCHEDULED = "ScheduledTag";
    final public String SSN_VOICE_COMMAND = "VoiceCommand";
    final public String SSN_IMAGE_PREFIX = "imagePrefix";
    final public String SSN_VIDEO_PREFIX = "videoPrefix";
    public int SSN_IMAGE_THUMBNAIL_HEIGHT = 122;
    public int SSN_IMAGE_THUMBNAIL_WIDTH = 145;

    public Color SSN_WHITE_BACKGROUND_COLOR = new Color(225, 225, 225);
    public Color SSN_FONT_COLOR = new Color(71, 71, 71);
    public Color SSN_TOOLBAR_FONT_COLOR = new Color(116, 116, 116);
    public Color SSN_BORDER_COLOR = new Color(237, 237, 237);
    public Color SSN_PANEL_BARS_COLOR = new Color(237, 237, 237);
    public Color SSN_BUTTON_COLOR = new Color(214, 214, 214);
    public Color SSN_MENU_BAR_COLOR = new Color(214, 214, 214);
    public Color SSN_TOOL_BAR_COLOR = new Color(223, 223, 223);
    public Color SSN_TOOL_BAR_BORDER_COLOR = new Color(214, 214, 214);
    public Color SSN_MENU_BAR_BORDER_COLOR = new Color(198, 198, 198);
    public Color SSN_GALLERY_THUMBNAIL_COLOR = new Color(143, 143, 143);
    public Color SSN_IMAGE_TOOLBAR_COLOR = new Color(143, 143, 143);

    public Color SSN_BLACK_BACKGROUND_COLOR = new Color(49, 49, 49);
    public Color SSN_WHITE_FONT_COLOR = new Color(225, 225, 225);
    public Color SSN_TOOLBAR_WHITE_FONT_COLOR = new Color(225, 225, 225);
    public Color SSN_BLACK_BORDER_COLOR = new Color(49, 49, 49);
    public Color SSN_PANEL_BLACK_BARS_COLOR = new Color(49, 49, 49);
    public Color SSN_BUTTON_BLACK_COLOR = new Color(49, 49, 49);
    public Color SSN_MENU_BAR_BLACK_COLOR = new Color(77, 77, 77);
    public Color SSN_TOOL_BAR_BLACK_COLOR = new Color(77, 77, 77);
    public Color SSN_TOOL_BAR_BORDER_BLACK_COLOR = new Color(49, 49, 49);
    public Color SSN_MENU_BAR_BORDER_BLACK_COLOR = new Color(77, 77, 77);
    public Color SSN_GALLERY_THUMBNAIL_BLACK_COLOR = new Color(77, 77, 77);
    public Color SSN_IMAGE_TOOLBAR_BLACK_COLOR = new Color(77, 77, 77);
    // final public String SSN_WEB_HOST = "http://162.209.99.244/";
    //Local environment
       // final public String SSN_WEB_HOST = "http://ssn.xavient.com/our_hive/";
    //UAT environment
        //final public String SSN_WEB_HOST = "http://162.209.99.244/our_hive/";
        final public String SSN_WEB_HOST =  "http://staging.getourhive.com/our_hive/";
    //prod enviornment
       // final public String SSN_WEB_HOST = "https://app.getourhive.com/";
      
    //schedule a tag
    final public String SSN_SCHEDULAR_PARENT_TAG = "P";
    final public String SSN_SCHEDULAR_CHILD_TAG = "c";
    final public String SSN_SCHEDULAR_TAG_HEAD_ALBUM = "Album";
    final public String SSN_SCHEDULAR_TAG_HEAD_TITLE = "Photo Caption";
    final public String SSN_SCHEDULAR_TAG_HEAD_ST_DATE = "Start Date";
    final public String SSN_SCHEDULAR_TAG_HEAD_END_DATE = "End Date";
    final public String SSN_SCHEDULAR_TAG_HEAD_ST_TIME = "Time";
    final public String SSN_SCHEDULAR_TAG_HEAD_LOC = "Location";
    final public String SSN_SCHEDULAR_TAG_HEAD_KEY = "Keywords";
    final public String SSN_SCHEDULAR_TAG_HEAD_DURA = "Duration";
    final public String SSN_SCHEDULAR_TAG_HEAD_COMMENTS = "Comments";
    final public String SSN_SCHEDULAR_TAG_SEPERATOR = "`";
    public Color SSN_SCHEDULAR_TBL_HEADIN_BLACK_BRG_COLOR = new Color(49, 49, 49);
    public Color SSN_SCHEDULAR_TBL_WHITE_HEADIN_FRG_COLOR = new Color(225, 225, 225);
    public Color SSN_SCHEDULAR_TBL__DATA_COLOR = new Color(77, 77, 77);
    public Color SSN_SCHEDULAR_TREE__LEAF_COLOR = new Color(103, 103, 103);
    public Color SSN_SCHEDULAR_TREE__NODE_COLOR = new Color(155, 109, 0);
    final public String SSN_SCHEDULE_TAG_DATE_PATTERN = "MM-dd-yyyy";
    final public String SSN_SCHEDULE_TAG_TIME_PATTERN = "HH:mm";

    final public String SSN_STT_SERVICE_LINK = "http://www.google.com/speech-api/v2/recognize?output=json&lang=en-us&key=AIzaSyClGUml9qjbP5bZmG_dkTEGlAw8f8Y0ibc";
    final public String SSN_SUBCRTIPTION_URL = SSN_WEB_HOST + "subscription_transactions/my_transactions";

    // constants for Google Analytics
    final public String SSN_APP_NAME_IN_GOOGLE_ANALYTICS = "OurHive Website";
    final public String SSN_APP_VERSION_IN_GOOGLE_ANALYTICS = "1.3.2";
    final public String SSN_APP_TRACKING_ID_IN_GOOGLE_ANALYTICS = "UA-55450373-1";
    final public String SSN_APP_EVENT_METADATA_TAGGING = "Event : METADATA TAGGING";
    final public String SSN_APP_EVENT_SHARING = "Event : SHARING";

    // constants for custom text field rounded circle
    public static final Color TEXT_FIELD_BORDER_COLOR = new Color(255, 255, 255);
    public static int DEFAULT_TEXT_FIELD_HEIGHT = 6;
    public static int DEFAULT_TEXT_FIELD_WIDTH = 30;

    public Color SSN_VALIDATION_LABEL_FORE_COLOR = new Color(255, 255, 255);
    public static final Color SSN_TEXT_LABEL_YELLOW_COLOR = new Color(255, 215, 0);
    public static final String SSN_LOGIN_FAILED_MESSAGE = "Username and Password is required for login.";
    public static int SSN_CHECKBOX_IS_CHECKED = 1;
    public static int SSN_CHECKBOX_IS_UNCHECKED = 2;
    public static Dimension SSN_SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final String SSN_SHARE_WITH_MAIL_SUBJECT = "Share via OurHive";
    
    
}
