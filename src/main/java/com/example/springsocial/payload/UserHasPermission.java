package com.example.springsocial.payload;

import javax.validation.constraints.NotBlank;

public class UserHasPermission {
 	@NotBlank
    private String form;

    @NotBlank
    private String action;
    
    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
