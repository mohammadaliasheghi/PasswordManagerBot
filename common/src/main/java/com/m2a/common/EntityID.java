package com.m2a.common;

import java.io.Serializable;

public interface EntityID<ID extends Serializable> extends Serializable {

    ID getId();

    void setId(ID id);
}