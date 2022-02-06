package com.spiashko.blazepersistencegraphqldemo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_best_friend")
    private Person bestFriend;

    @OneToMany(mappedBy = "owner")
    private Set<Cat> kittens = new HashSet<>();

    @OneToMany(mappedBy = "bestFriend")
    private Set<Person> bestFriendForPeople = new HashSet<>();

}
