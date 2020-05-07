package com.spiashko.blazepersistencegraphqldemo.view;

import com.blazebit.persistence.view.EntityView;
import com.spiashko.blazepersistencegraphqldemo.model.Person;


@EntityView(Person.class)
public interface PersonSimpleView extends PersonIdView {

    String getName();

}
