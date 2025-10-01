package neko.kuro.projectGorgonCrafting.entities;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.util.UUID;

@Data
@Serdeable
@MappedEntity
public class ItemAmount {
    @Id
    private UUID id;
    private Item item;
    private int amount;
}
