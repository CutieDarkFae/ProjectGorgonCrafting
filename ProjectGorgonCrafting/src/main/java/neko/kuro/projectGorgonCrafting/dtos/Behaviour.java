package neko.kuro.projectGorgonCrafting.dtos;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.util.List;

@Serdeable
@Data
public class Behaviour {
    private List<String> requirements;
    private String useVerb;
}
