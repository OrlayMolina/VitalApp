package co.edu.uniquindio.ing.soft.model;

import co.edu.uniquindio.ing.soft.enums.AppointmentStatus;
import co.edu.uniquindio.ing.soft.enums.Day;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Appointment {
    private String appointmentId;
    private String patientDocumentNumber;
    private Doctor doctorDocumentNumber;
    private Day day;
    private String horario;
    private AppointmentStatus status;
}
