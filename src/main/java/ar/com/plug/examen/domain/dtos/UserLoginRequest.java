package ar.com.plug.examen.domain.dtos;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @NotNull
    private String userName;
}
