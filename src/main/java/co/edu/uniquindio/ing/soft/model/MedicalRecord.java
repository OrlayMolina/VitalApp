package co.edu.uniquindio.ing.soft.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MedicalRecord {
    private String medicalRecordId;
    private String appointmentId;
    private String resumen;
    private String observations;
}
