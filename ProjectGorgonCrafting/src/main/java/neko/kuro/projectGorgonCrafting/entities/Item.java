package neko.kuro.projectGorgonCrafting.entities;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@MappedEntity
@Serdeable
public class Item {
    @Id
    private UUID id;
    private String name;
    private List<Creature> droppedBy;
    private List<Person> barteredBy;
    private List<Person> tradedBy;
    private List<Recipe> usedIn;
    private int sellsFor;
}

//Item {
//    Name: <string>
//            DroppedBy: <List<Creature>>
//    BarteredBy: <List<Person>>
//    TradedBy: <List<Person>>
//    UsedIn<List<Recipe>>
//    SellsFor: <number>
//}