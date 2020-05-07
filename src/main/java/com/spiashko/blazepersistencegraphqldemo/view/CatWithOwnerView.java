package com.spiashko.blazepersistencegraphqldemo.view;

import com.blazebit.persistence.view.EntityView;
import com.spiashko.blazepersistencegraphqldemo.model.Cat;


@EntityView(Cat.class)
public interface CatWithOwnerView extends CatSimpleView {

    PersonSimpleView getOwner();

}
