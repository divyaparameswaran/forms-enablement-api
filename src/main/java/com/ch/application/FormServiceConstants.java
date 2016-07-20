package com.ch.application;

import com.ch.model.FormStatus;

/**
 * Created by Aaron.Witter on 15/07/2016.
 */
public class FormServiceConstants {

  public static final String DATE_TIME_FORMAT_DB = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_TIME_FORMAT_SUBMISSION = "yyyyMMdd";

  public static final String PACKAGE_SUBMISSION_NUMBER_KEY = "submissionNumber";
  public static final String PACKAGE_STATUS_DEFAULT = FormStatus.PENDING.toString().toUpperCase();

  public static final String DATABASE_OBJECTID_KEY = "_id";
}
