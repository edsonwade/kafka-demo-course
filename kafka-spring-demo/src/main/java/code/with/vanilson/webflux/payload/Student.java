package code.with.vanilson.webflux.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Student
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-24
 */
@Getter
@Setter
@ToString
public class Student {
    private int id;
    private String firstName;
    private String lastName;

}