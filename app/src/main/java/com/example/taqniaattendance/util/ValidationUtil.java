package com.example.taqniaattendance.util;

import android.text.TextUtils;
import android.util.Patterns;

import com.example.taqniaattendance.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Eng. Abdulmajid Alyafey on 12/25/2017.
 */

public class ValidationUtil {
    private static PhoneNumberUtil mPhoneUtil ;
    private static Phonenumber.PhoneNumber mPhoneNumber;
    private static PhoneNumberUtil.PhoneNumberType mPhoneNumberType;


    private static PhoneNumberUtil getPhoneNumberUtilInstance(){
        if(mPhoneUtil == null)
            mPhoneUtil = PhoneNumberUtil.createInstance(App.getInstance().getApplicationContext());

        return mPhoneUtil;
    }

    //---------------------------- National ID ------------------------------------
    //return true if the national id is correct
    public static boolean isValidNationalID(String nationalID) {
        return !TextUtils.isEmpty(nationalID.trim()) && nationalID.matches("[127][0-9]{9}");
    }

    public static boolean isValidChassisNumber(String chassisNumber) {
        return !TextUtils.isEmpty(chassisNumber.trim()) && chassisNumber.length() == 17;
    }

    /**
     * This method will return just the string or empty text to display it on the activity, to avoid display null text.
     */
    public static String getString(String string) {
        return !TextUtils.isEmpty(string) ? string : "";
    }

    /**
     * This wil validate the phone number and return validated phone as String format, otherwise will return null.
     * It used phone number util.
     * @param number
     */
    public static String getValidPhoneNumber(String countryCode, String number) {
        try {
            // Now check for the validity of the phone number using libphone library.
            mPhoneNumber = getPhoneNumberUtilInstance().parse(number, countryCode);
            mPhoneNumberType = getPhoneNumberUtilInstance().getNumberType(mPhoneNumber);
            if (!getPhoneNumberUtilInstance().isValidNumber(mPhoneNumber) || ( mPhoneNumberType != PhoneNumberUtil.PhoneNumberType.MOBILE && mPhoneNumberType != PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE)){
                return null;
            }
        } catch (NumberParseException e) { // catch is necessary for using the library, its methods throws exception and therefore you MUST handle it, or make this function THROW exception.
            LogsUtil.printErrorLog("NumberParseException was thrown: ",e.toString());
            return null;
        }
        // every thing is OK and the number is valid, so return true;
        return getPhoneNumberUtilInstance().format(mPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164).substring(1);
    }

    /**
     * use method if you have photo path and want to send it as multipart.
     * @param photoPath path of photo
     * @param photoKey key from the backend
     * @return photo as multiPartBody.part
     */
    public static MultipartBody.Part getPhotoMultiPart(String photoPath, String photoKey) {
        if (TextUtils.isEmpty(photoPath))
            return null;
        File file = new File(photoPath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image*"), file); // image* is static for all -- image* you open the photo in the backend immediate with no need to download it ..
        MultipartBody.Part body =  MultipartBody.Part.createFormData(photoKey, file.getName(), reqFile); //"national_id_photo" is the key of the file or body
        return body;
    }


    //------------- Email Validation ----------------------------------
    public static boolean isValidEmail(String email){
        return  Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //------------- English Name Validation -----------------------------------
    public static boolean isValidEnglishName(String name) {
        return !TextUtils.isEmpty(name) && name.matches("[a-zA-Z]{3,30}");
    }


    /**
     * return true if the email is empty or valid other wise it will return false
     * @param email email address
     * @return boolean
     */
    public static boolean isEmptyOrValidEmail(String email) {
        return TextUtils.isEmpty(email) || Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isEmpty(Object input) {
        if (input == null) return true;
        return input.toString().trim().isEmpty();

    }

    /**
     * return RequestBody object with MediaType "("multipart/form-data")" to use when send through API.
     * @param string to put into RequestBody object
     * @return RequestBody, if string parameter is null or empty, it will return null object
     */
    public static RequestBody getRequestBody (String string) {
        if (TextUtils.isEmpty(string))
            return null;
        return RequestBody.create(MediaType.parse("multipart/form-data"), string);
    }

    /**
     * get string from Json object
     * @param jsonObject
     * @param key
     * @return
     */
    public static String getStringFromJson(JSONObject jsonObject, String key) {
        String string = null;
        try {
            string = ValidationUtil.getString(jsonObject.getString(key));
        } catch (JSONException e) {
            e.printStackTrace();
//     todo       Bugsnag.notify(new Exception("error in the parsing of Key" + key));
            return null;
        }
        return getString(string);
    }

    //return json object that inside the json object
    public static JSONObject getJSONObject(JSONObject jsonObject, String key) {
        JSONObject innerJSONObject = new JSONObject();
        try {
            innerJSONObject = jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return innerJSONObject;
    }

    public static boolean isCharactersOnly(String string){
        return !TextUtils.isEmpty(string) && string.matches("^([\\u0621-\\u064A\\040 ]|[a-zA-Z ])*$");
    }


    public static boolean isValidPhoneNumber(String number) {
        return !TextUtils.isEmpty(number) && Patterns.PHONE.matcher(number).matches();
    }

    /**
     * check if the selectedDate is valid Claim Date by check if its not empty and meet the required format
     *
     * @param format
     * @param selectedDate
     * @return
     */
    public static boolean isValidDate(String format, String selectedDate) {
        if (TextUtils.isEmpty(selectedDate))
            return false;

        Date mDate;
        DateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            mDate = sdf.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        Calendar starDate = Calendar.getInstance();
        starDate.add(Calendar.YEAR,-10);
        return mDate.before(Calendar.getInstance().getTime()) && mDate.after(starDate.getTime());
    }

    public static List<String> validateFields(Map<String, String> fields) {
        List<String> invalidFields = new ArrayList<>();

        for (String key: fields.keySet()) {
            if (TextUtils.isEmpty(fields.get(key)))
                invalidFields.add(key);
        }
        return invalidFields;
    }

    /**
     *
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
