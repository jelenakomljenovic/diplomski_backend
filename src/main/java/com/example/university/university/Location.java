package com.example.university.university;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class Location {
    private String lat;
    private String lon;
    private Address address;


}
