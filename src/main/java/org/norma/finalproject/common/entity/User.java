package org.norma.finalproject.common.entity;


import lombok.*;
import org.norma.finalproject.customer.entity.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userNumber;

    private String password;


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();


}
