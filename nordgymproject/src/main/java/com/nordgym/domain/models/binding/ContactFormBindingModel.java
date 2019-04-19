package com.nordgym.domain.models.binding;

import com.nordgym.constants.GlobalConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ContactFormBindingModel {
    private String name;
    private String email;
    private String subject;
    private String message;

    public ContactFormBindingModel() {
    }

    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @Pattern(regexp = "^[A-Za-zА-Яа-я\\s]+$",message = GlobalConstants.FIRST_NAME_MUST_BEGIN_WITH_CAPITAL_LETTER_AND_CONTAINS_ONLY_LETTERS)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])"
            ,message = GlobalConstants.YOU_HAVE_ENTERED_INVALID_EMAIL)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @NotBlank(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
