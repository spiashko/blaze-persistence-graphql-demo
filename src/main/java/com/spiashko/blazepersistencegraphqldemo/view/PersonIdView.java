package com.spiashko.blazepersistencegraphqldemo.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.spiashko.blazepersistencegraphqldemo.model.Person;


@EntityView(Person.class)
public interface PersonIdView {

    @IdMapping
    Long getId();

}
