package neko.kuro.projectGorgonCrafting.entities;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Data
@MappedEntity
@Serdeable
public class Item {
    @Id
    private UUID id;
    private String internalId;
    private String name;
    private String description;
    @Relation(Relation.Kind.ONE_TO_MANY)
    private List<Creature> droppedBy;
    @Relation(Relation.Kind.ONE_TO_MANY)
    private List<Person> barteredBy;
    @Relation(Relation.Kind.ONE_TO_MANY)
    private List<Person> tradedBy;
    @Relation(Relation.Kind.ONE_TO_MANY)
    private List<Recipe> usedIn;
    private int sellsFor;
    private URI location;
    private URI image;
}