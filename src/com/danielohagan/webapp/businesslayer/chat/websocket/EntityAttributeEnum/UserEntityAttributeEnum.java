package com.danielohagan.webapp.businesslayer.chat.websocket.EntityAttributeEnum;

public enum UserEntityAttributeEnum implements IEntityAttributeEnum {

    USERNAME("userUsername"),
    STATUS("userStatus");

    private String mAttributeString;

    UserEntityAttributeEnum(String attributeString) {
        mAttributeString = attributeString;
    }

    @Override
    public String getAttributeString() {
        return mAttributeString;
    }
}
