package com.woking.demo.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *  Fields :
 *  1. The 'to' field mean will be sent to the address.
 *  2. The 'subject' field mean title of mail.
 *  3. The 'content' field clearly is content.
 *  4. The 'props' filed using Map<T,O> will carry some necessary content(username,password,jwt, tokens,..ect)
 *  
 *    BONUT: 
 *    1. For now, I used props is content and it changes the user's status without using any tokens.
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataMailDto {
    private String to;
    private String subject;
    private String content;
    private Map<String, Object> props;
}
