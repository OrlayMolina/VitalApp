package co.edu.uniquindio.ing.soft.model;

import co.edu.uniquindio.ing.soft.enums.DocumentType;
import co.edu.uniquindio.ing.soft.enums.Gender;
import co.edu.uniquindio.ing.soft.enums.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Patient extends User{
    private Role role;
    private String firstname;
    private String lastname;
    private DocumentType documentType;
    private String documentNumber;
    private int age;
    private Gender gender;
    private String address;
    private List<Diagnostic> diagnostics;
}
