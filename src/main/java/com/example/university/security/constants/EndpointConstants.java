package com.example.university.security.constants;

public final class EndpointConstants {

  public static final String SINGLE_PATH = "/*";
  public static final String ALL_PATHS = "/**";
  public static final String HEALTH_ENDPOINT = "/actuator/health";
  public static final String API_UNIVERSITY = "/universities/**";
  public static final String API_CLASSIFICATION = "/classifications";
  public static final String AUTHENTICATE = "/authenticate/**";
  public static final String CHECK_USER = "/user/me";
  public static final String DEPARTMENT = "/department/**";
  public static final String MAJOR = "/major/**";
  public static final String USER = "/users/**";

  private EndpointConstants() {
  }
}
