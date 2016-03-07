package com.ch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * Created by Aaron.Witter on 07/03/2016.
 */
public class Saying {
  private long id;

  @Length(max = 3)
  private String content;

  public Saying() {
    // Jackson deserialization
  }

  public Saying(long id, String content) {
    this.id = id;
    this.content = content;
  }

  @JsonProperty
  public long getId() {
    return id;
  }

  @JsonProperty
  public String getContent() {
    return content;
  }
}