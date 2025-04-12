package co.edu.uniquindio.ing.soft.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Diagnostic implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String diagnostic;
}
