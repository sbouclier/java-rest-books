package com.github.sbouclier.javarestbooks.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Embeddable;

/**
 * Author embeddable entity
 *
 * @author Stéphane Bouclier
 *
 */
@Embeddable
public class Author {

    private String firstName;

    private String lastName;

    private Author() {
        // Default constructor for Jackson
    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .toString();
    }
}
