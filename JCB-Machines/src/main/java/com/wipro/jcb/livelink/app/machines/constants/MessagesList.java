package com.wipro.jcb.livelink.app.machines.constants;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
public class MessagesList {

    public static final String MISSING_TOKEN_ID = "TokenId is mandatory in the header.";
    //Exceptional or Error messages
    public static final String INSUFFICIENT_CREDIT_POINTS = "Due to insufficient credits points in your account, the request is cancelled.";
    public static final String SERVER_DOWN_MESSAGE="Server is under maintenance, please try later.";
    public static final String INVALID_USER_TYPE_MESSAGE = "User is not authorized for this information, please check selected user type.";
    public static final String REQUEST_PROCESSING_FAILED="Failed to process request";
    public static final String USER_ACCOUNT_LOCKED ="User account is temporary locked,Please try again tomorrow.";
    public static final String APP_REQUEST_PROCESSING_FAILED="App server has failed to process request, kindly check after some time.";
    public static final String SERVER_REQUEST_PROCESSING_FAILED="Server has failed to process request, kindly check after some time.";
    public static final String NO_ACTIVE_NODES="No Active Nodes";
    public static final String WIPRO_INVALID_LOGIN_DETAILS= "Invalid loginId or password";
    public static final String APP_SERVER_INVALID_LOGIN_DETAILS = "Please enter valid username / password.";
    public static final String APP_SERVER_INVALID_LOGIN_FROM_CHACHE = "Please enter valid password.";
    public static final String VIN_LIST_IS_EMPTY="Please select vin numbers to get reports";
    public static final String NO_RECORDS="No records found";
    public static final String FEEDBACK_NOT_AVAIL="Machine not yet eligible for SSI Feedback.";
    public static final String ANDROID_PUSH_ERROR="Test Android Push Error";

    //Informative messages
    public static final String USER_LOGOUT_MESSAGE= "User Logged Out Successfully";
    public static final String USER_LOGOUT_MACHINE_ZERO= "User Logged Out Successfully With Machines Zero";
    public static final String SCHEDULER_RUNNING="Previous scheduler is running.";
    public static final String SCHEDULER_REPORT_SUCCESS= "SUCCESS";
    public static final String SCHEDULER_REPORT_FAILED= "FAILED";
    public static final String MACHINE_SCHEDULER_RESPONSE="Unexpected Response for  machines for user";

    public static final String ADVANCE_REPORT_ERROR="Error while fetching advance report data for machines";
    public static final String ADVANCE_REPORT_NO_MACHINE_EXIST="No data exist for such vin";
    public static final String APP_REQUEST_PROCESSING_ALERT = "No Alert or Machine data exist";
    public static final String BREAKDOWN_MESSAGE="No data exits";
    public static final String SESSION_EXPIRED = "Session Expired";

    public static final String RENEWAL_ALERT ="The selected VIN not renewed, so please renewal to view the details.";
    
    public static final String LOGGED_IN_USER_ROLE = "LoggedInUserRole";
	public static final String NO_FIRMWARE = "No Firmware";
	public static final String LL2 = "LL2";
	public static final String LL4 = "LL4";
	public static final String SUCCESS = "SUCCESS";
	public static final String UPDATESUCCESS = "UPDATE-SUCCESS";
	public static final String FAILURE = "FAILURE";
	public static final String TOKEN_ID_INVALID = "TokenId is null or Invalid";
	public static final String NO_DATA_AVAILABLE = "No data available";
	
	//Wise URL
	public static final String SET_LANDMARK_DETAILS = "/v05/WorkManagement/geoFenceService/SetLandmarkDetails";
	public static final String GET_LANDMARK_DETAILS = "/v05/WorkManagement/geoFenceService/GetLandmarkDetails";
	public static final String DELETE_LANDMARK_DETAILS = "/v02/WorkManagement/geoFenceService/DeleteLandmark";
	public static final String GET_LANDMARK_DETAILS_FOR_USER = "/v05/WorkManagement/geoFenceService/GetLandmarkDetailsForUser";
	public static final String SET_TIME_FENCE = "/v04/WorkManagement/timeFenceService/SetTimeFence";
	public static final String GET_TIME_FENCE = "/v04/WorkManagement/timeFenceService/GetTimeFence";
}
