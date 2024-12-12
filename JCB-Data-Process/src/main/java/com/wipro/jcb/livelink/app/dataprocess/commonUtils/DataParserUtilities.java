package com.wipro.jcb.livelink.app.dataprocess.commonUtils;

import com.wipro.jcb.livelink.app.dataprocess.constants.AppServerConstants;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Component
public class DataParserUtilities {

    /**
     * convert string to date
     *
     * @param date is string date in "yyyy-MM-dd" format
     * @return date object
     */
    public Date getDate(String date) {
        final DateFormat parseFormat = new SimpleDateFormat(AppServerConstants.DateFormat);
        Date dt = null;
        try {
            dt = parseFormat.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public String getStartDate(int daysBefore) {
        return LocalDate.now().minusDays(daysBefore).toString();
    }

    /**
     * convert string to date
     *
     * @param date is string date in "yyyy-MM-dd HH:mm:ss" format
     * @return date object
     */
    public Date getDateTime(String date) {
        final DateFormat parseFormat = new SimpleDateFormat(AppServerConstants.DateTimeFormat);
        Date dt = null;
        try {
            dt = parseFormat.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

}
