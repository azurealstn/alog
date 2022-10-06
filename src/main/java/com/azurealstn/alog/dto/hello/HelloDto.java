package com.azurealstn.alog.dto.hello;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HelloDto {

    private final String name;
    private final int age;
}
