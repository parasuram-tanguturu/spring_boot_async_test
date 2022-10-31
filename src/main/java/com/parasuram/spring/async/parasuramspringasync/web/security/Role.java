package com.parasuram.spring.async.parasuramspringasync.web.security;

public enum Role {
    DEVELOPER("Developer"),
    OPERATOR("Operator"),
    USER("User");


    /*
        Second, our value field is final.
        While fields of an enum do not have to be final, in most cases we don't want our values to change.
        In the spirit of enum values being constant, this makes sense.
    */
    public final String value;
    private Role(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Role{" +
                "value='" + value + '\'' +
                '}';
    }

/*
notice the special syntax in the declaration list.
This is how a constructor is invoked for enum types.
Although it's illegal to use the new operator for an enum, we can pass constructor arguments in the declaration list.
 */
    //Ref:https://www.baeldung.com/java-enum-values
}
