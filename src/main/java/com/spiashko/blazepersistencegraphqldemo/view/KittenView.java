package com.spiashko.blazepersistencegraphqldemo.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.spiashko.blazepersistencegraphqldemo.model.Cat;


@EntityView(Cat.class)
public interface KittenView {

    @IdMapping
    Long getId();

    String getName();

}
