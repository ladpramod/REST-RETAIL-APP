package com.retail.management.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "retailer")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Retailer {

    @Id

    private String retailId;
    private String name;
    private String address;
    private String city;
    private String state;
    private String mobile;
    private String email;

}
