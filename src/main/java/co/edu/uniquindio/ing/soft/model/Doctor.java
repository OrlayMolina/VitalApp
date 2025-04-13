package co.edu.uniquindio.ing.soft.model;

import co.edu.uniquindio.ing.soft.enums.DocumentType;
import co.edu.uniquindio.ing.soft.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Doctor extends User{
    private Role role;
    private String firstname;
    private String lastname;
    private DocumentType documentType;
    private String documentNumber;
    private String professionalNumber;

    public Doctor(String email, String password, Role role, String firstname, String lastname,
                  DocumentType documentType, String documentNumber, String professionalNumber) {
        super(email, password);
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.professionalNumber = professionalNumber;
    }
}
