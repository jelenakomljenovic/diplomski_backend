package com.example.university.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class ErrorMessage {

  private final int statusCode;
  private final Instant timestamp;
  private final String message;
  private final String description;

}
