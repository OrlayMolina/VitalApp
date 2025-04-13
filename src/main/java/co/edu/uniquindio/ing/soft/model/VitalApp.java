package co.edu.uniquindio.ing.soft.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VitalApp implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Diagnostic> diagnostics = new ArrayList<>();
    private List<Doctor> doctors = new ArrayList<>();
    private List<Patient> patients = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();

}
