package neko.kuro.projectGorgonCrafting.entities;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.net.URI;
import java.util.UUID;

@MappedEntity
@Data
@Serdeable
public class Image {
    @Id
    public UUID id;
    public URI href;
}
