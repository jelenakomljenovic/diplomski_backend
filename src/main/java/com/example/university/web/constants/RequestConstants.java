package com.example.university.web.constants;

public final class RequestConstants {

  public static final String LOG_FORMAT = "Request failed with '{}'. RequestURI: {}, IP: {}, {}, {}.";

  public static final String HEADER_KEY_VALUE = "%s = %s";

  public static final String HEADERS_PREFIX = "Headers: (";

  public static final String HEADERS_DELIMITER = ", ";

  public static final String HEADERS_SUFFIX = ")";

  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final String X_FORWARDED_FOR_HEADER = "X-FORWARDED-FOR";

  public static final String AUTHORIZATION_HEADER_PREFIX = "Authorization header: ";

  public static final String NULL = "null";

  public static final String EMPTY = "empty";

  public static final String PRESENT = "present";

  private RequestConstants() {
  }
}
