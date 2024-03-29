package com.spiashko.blazepersistencegraphqldemo.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.spiashko.blazepersistencegraphqldemo.model.Person;

import java.util.Set;


@EntityView(Person.class)
public interface PersonView {

    @IdMapping
    Long getId();

    String getName();

    Set<BestFriendView> getBestFriendForPeople();

    Set<KittenView> getKittens();

}
