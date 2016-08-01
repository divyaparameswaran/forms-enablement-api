package com.ch.conversion.validation;

import com.ch.conversion.config.ITransformConfig;

/**
 * Created by Aaron.Witter on 21/07/2016.
 */
public interface XmlValidator {
  void validate(ITransformConfig config, String xml);
}
