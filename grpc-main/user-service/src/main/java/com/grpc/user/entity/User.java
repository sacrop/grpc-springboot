package com.grpc.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Generated;
import lombok.ToString;

@Entity
@Data
@ToString
public class User {
    @Id
    private String login;

    private String name;
    private String genre;
}
