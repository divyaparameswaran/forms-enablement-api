package com.ch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Aaron.Witter on 07/03/2016.
 */
@XmlRootElement(name = "HelloWorld")
@XmlType(propOrder = {"greeting"})
public class HelloWorld {
  @JsonProperty
  private String greeting;

  public HelloWorld() {
    // Deliberately empty constructor. (Not having a comment here is a PMD violation.)
  }

  public String getGreeting() {
    return greeting;
  }

  @XmlElement(name = "Greeting")
  public void setGreeting(String greeting) {
    this.greeting = greeting;
  }
}
