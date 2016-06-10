package com.ch.exception.mapper;

import org.json.JSONObject;

/**
 * Created by elliott.jenkins on 10/06/2016.
 */
public class ExceptionHelper {
  public static String getJsonError(Exception exception){
    JSONObject error = new JSONObject();
    error.put("message", exception.getMessage());
    return error.toString();
  }
}
