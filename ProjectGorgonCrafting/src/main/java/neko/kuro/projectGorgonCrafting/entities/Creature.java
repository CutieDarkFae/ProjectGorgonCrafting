package neko.kuro.projectGorgonCrafting.entities;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.util.UUID;

@Data
@Serdeable
@MappedEntity
public class Creature {
    @Id
    private UUID id;
    private String name;
    private String location;
}
