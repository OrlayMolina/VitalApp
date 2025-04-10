package co.edu.uniquindio.ing.soft.model;

import co.edu.uniquindio.ing.soft.enums.DocumentType;
import co.edu.uniquindio.ing.soft.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Doctor {
    private Role role;
    private String firstname;
    private String lastname;
    private DocumentType documentType;
    private String documentNumber;
    private String professionalNumber;
}
