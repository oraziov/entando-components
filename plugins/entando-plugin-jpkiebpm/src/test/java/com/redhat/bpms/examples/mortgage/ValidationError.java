package com.redhat.bpms.examples.mortgage;

/**
 * This class was automatically generated by the data modeler tool.
 */
@org.kie.api.definition.type.Label(value = "Validation Error")
public class ValidationError  implements java.io.Serializable {

static final long serialVersionUID = 1L;

    @org.kie.api.definition.type.Label(value = "Cause of Error")
    @org.kie.api.definition.type.Position(value = 0)
    private java.lang.String cause;

    public ValidationError() {
    }

    public ValidationError(java.lang.String cause) {
        this.cause = cause;
    }



    public java.lang.String getCause() {
        return this.cause;
    }

    public void setCause(  java.lang.String cause ) {
        this.cause = cause;
    }




}
