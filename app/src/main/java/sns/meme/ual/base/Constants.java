package sns.meme.ual.base;

/**
 * Created by amyung on 10/20/14.
 */
public class Constants {
    public static final String GOOGLE_PLAY_LINK = "http://google.com";
    public static final int MAP_SEARCH_RADIUS = 15; // KILOMETERS
    public static final int USERS_NUMBER = 10000; // NUMBER OF USERS TO SHOW ON MAP
    //public static final String MAP_DEFAULT_MODE = MapManager.MODE_WALKING; // MODE FOR TRAVEL MEETING_TIME_AT
    public static final int MAP_DEFAULT_ZOOM = 15; // MODE FOR TRAVEL MEETING_TIME_AT
    public static final String SUPPORT_URL = "http://www.hellocafe.com/support"; // MODE FOR TRAVEL MEETING_TIME_AT
    public static final String ABOUT_URL = "http://www.hellocafe.com/about"; // MODE FOR TRAVEL MEETING_TIME_AT
    public static final String SUPPORT_EMAIL = "support@hellocafe.com";

    public static final boolean IS_SHOWLOG = true;
    public static final String LOG_TAG = "Hellocafe";
    public static final boolean IS_TESTMODE = true;
//    public static final String USER_LEAVE_TIME = "user_leave_time";


//    public static final int PRICE_STEP = 5000;
//    public static final int PRICE_DEFAULT = 50000;


    /**
     * this keyword is for SharedPreferences.
     */
//    public static final String PRE_CONFIG_START_TIME = "config_start_time";
//    public static final String PRE_CONFIG_END_TIME = "config_end_time";
//    public static final String PRE_CONFIG_COMPLETION_WAIT = "config_completion_wait";
//    public static final String PRE_CONFIG_MAX_PRICE = "config_max_price";
//    public static final String PRE_CONFIG_MIN_PRICE = "config_min_price";
//    public static final String PRE_CONFIG_MAX_REQUEST_DURATION = "config_max_request_duration";
//    public static final String PRE_CONFIG_CUSTOMER_SUPPORT = "config_customer_support";
    public static final String PRE_LOCATION_LATITUDE = "location_latitude";
    public static final String PRE_LOCATION_LONGITUDE = "location_longitude";
    public static final String PRE_LOCATION_TIME = "location_time";
    public static final String PRE_USERCHECKED_MEETID = "user_checked_id";
    public static final String PRE_USER_BALANCE = "user_balance";

    /**
     * Intent extra arguments
     */
    public static final String INTENT_EXTRA_OBJECT_ID = "objectId";
    public static final String INTENT_EXTRA_MEETING_ID = "meetingId";
    public static final String INTENT_EXTRA_MEETING_POSITION = "meetingPosition";
    public static final String INTENT_EXTRA_COACH_TAB_ID = "coachTabId";
    public static final String INTENT_EXTRA_STICKY_HEADER_ID = "stickyHeaderId";
    public static final String INTENT_EXTRA_USERNAME = "username";
    public static final String INTENT_EXTRA_CANCEL_TOO_LATE = "cancelTooLate";
    public static final String INTENT_EXTRA_WAITED_TOO_LONG = "waitedTooLong";
    public static final String INTENT_EXTRA_CANCEL_ON_SITE = "cancelOnSite";
    public static final String INTENT_EXTRA_REQUEST_UNAVAILABLE_REASON = "requestUnavailableReason";
    public static final String INTENT_EXTRA_FINISH_ACTIVITY_AFTERWARDS = "finishActivityAfterwards";
    public static final String INTENT_EXTRA_ERROR_DIALOG_TITLE = "errorDialogTitle";
    public static final String INTENT_EXTRA_ERROR_DIALOG_MESSAGE = "errorDialogMessage";
    public static final String INTENT_EXTRA_CLIENT_TIME = "clientTime";
    public static final String INTENT_EXTRA_AT_MEETING_LOCATION = "atMeetingLocation";
    public static final String INTENT_EXTRA_MEETING_TIME = "meetingTime";
    public static final String INTENT_EXTRA_CANCEL_IS_REQUEST = "cancelIsRequest";

    public static final String INTENT_FILTER_PUSH_NOTIFICATION = "intentFilterPushNotification";

    public static final int STATE_LOCATION_ALL = 0;
    public static final int STATE_LOCATION_ONLY_GPS = 1;
    public static final int STATE_LOCATION_ONLY_NETWORK = 2;
    public static final int STATE_LOCATION_NONE = 3;


    public static final long TIME_SEC = 1000l;
    public static final long TIME_3SEC = 3 * TIME_SEC;
    public static final long TIME_5SEC = 5 * TIME_SEC;
    public static final long TIME_10SEC = 10 * TIME_SEC;
    public static final long TIME_MIN = 60 * TIME_SEC;
    public static final long TIME_2MIN = 2 * TIME_MIN;
    public static final long TIME_10MIN = 10 * TIME_MIN;
    public static final long TIME_15MIN = 15 * TIME_MIN;
    public static final long TIME_30MIN = 30 * TIME_MIN;
//    public static final long TIME_HOUR = 60 * TIME_MIN;
//    public static final long TIME_2HOUR = 2 * TIME_HOUR;
//    public static final long TIME_4HOUR = 4 * TIME_HOUR;


    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;


    public static final int BUS_LOCATION_UPDATED = 7000;
    public static final int BUS_PLACES_UPDATED = 7001;
    public static final int BUS_PLACE_SELECTED = 7002;
    public static final int BUS_MEETING_TIME_OVER = 7003;
    public static final int BUS_BALANCE_UPDATED = 7004;
    public static final int BUS_TOTAL_MEETINGS_UPDATED = 7005;
    public static final int BUS_TOTAL_MEETINGS_NOT_AVAILABLE = 7006;


    /**
     * this key word is for common dialog
     */
    public static final String DIALOG_FRAGMENT_TAG = "dialog";

    public static final int DIA_WAITING = 0;
    public static final int DIA_NETWORK_ENABLE = 1;
    public static final int DIA_LOCATION_ENABLE = 2;
    public static final int DIA_RECHARGE = 3;
    public static final int DIA_AFTER_HOUR = 4;
    public static final int DIA_CANCEL_POLICY = 5;
    public static final int DIA_CANCEL_REQUEST = 6;
    public static final int DIA_CANCEL_MEETING = 7;
    public static final int DIA_TOO_EARLY = 8;
    public static final int DIA_BECOME_TEACHER = 9;
    public static final int DIA_NETWORK_ERROR = 10;
    public static final int DIA_APOLOGIZE = 11;
    public static final int DIA_NO_COACH = 12;
    public static final int DIA_MONEY_EARNED = 13;
    public static final int DIA_REQUEST_UNAVAILABLE = 14;
    public static final int DIA_PAYOUT = 15;
    public static final int DIA_ERROR_GENERIC = 16;


    public static final String DIA_BUNDLE_TYPE = "dia_type";
    public static final String DIA_BUNDLE_TITLE = "dia_title";
    public static final String DIA_BUNDLE_MSG1 = "dia_msg1";
    public static final String DIA_BUNDLE_MSG2 = "dia_msg2";
    public static final String DIA_BUNDLE_BTNCNT = "dia_btnCnt";
    public static final String DIA_BUNDLE_BTN1STYLE = "dia_btn1style";
    public static final String DIA_BUNDLE_BTN2STYLE = "dia_btn2style";
    public static final String DIA_BUNDLE_BTN1MSG = "dia_btn1msg";
    public static final String DIA_BUNDLE_BTN2MSG = "dia_btn2msg";


    public static final int DIA_RESULT_LEFT = 0;
    public static final int DIA_RESULT_RIGHT = 1;

    public static final String REQUEST_UNAVAILABLE_GENERIC = "request_unavailable_generic";
    public static final String REQUEST_UNAVAILABLE_ACCEPTED = "request_unavailable_accepted";
    public static final String REQUEST_UNAVAILABLE_CANCELED = "request_unavailable_canceled";

    public static final String RESPONSE_ERROR_TIME_OUT_OF_SYNC = "client_time_out_of_sync";
    public static final String RESPONSE_ERROR_PENALTY_HAS_OCCURRED = "penalty_has_occurred";

    public static final int PAYOUT_ACCOUNT_INCOMPLETE = 0;
    public static final int PAYOUT_MINIMUM_REQUIRED = 1;
    public static final int PAYOUT_REQUEST_SUBMITTED = 2;
    public static final int PAYOUT_REQUEST_EXISTS = 3;
    public static final int PAYOUT_REQUEST_FAILED = 4;
    public static final int PAYOUT_NO_UNPAID_TRANS = 5;
    public static final int PAYOUT_EMAIL_NOT_SENT = 6;

    public static final int STUDENT_ACTIVE = 0;
    public static final int STUDENT_PENDING = 1;
    public static final int STUDENT_INACTIVE = 2;
    public static final int STUDENT_SUSPENDED = 3;
    public static final int TEACHER_ACTIVE = 4;
    public static final int TEACHER_PENDING = 5;
    public static final int TEACHER_INACTIVE = 6;
    public static final int TEACHER_SUSPENDED = 7;
}
