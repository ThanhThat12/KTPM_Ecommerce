package com.example.ecommerce.user.model;

import com.example.ecommerce.common.model.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User extends BaseDocument {

    @Indexed(unique = true)
    private String email;

    private String passwordHash;

    private String fullName;

    private String phone;

    @Builder.Default
    private UserRole role = UserRole.CUSTOMER;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private List<Address> addresses = new ArrayList<>();
}
