package co.edu.uniquindio.ing.soft.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Diagnostic {
    private String code;
    private String diagnostic;
    private String medicalRecordId;
}
