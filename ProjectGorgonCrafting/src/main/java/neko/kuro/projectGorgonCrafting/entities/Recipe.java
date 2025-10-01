package neko.kuro.projectGorgonCrafting.entities;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Serdeable
@MappedEntity
public class Recipe {
    @Id
    private UUID id;
    private String name;
    private int level;
    private int xpForFirstCrafting;
    private int xpForSubsequentCrafting;
    private List<ItemAmount> ingredients;
    private List<ItemAmount> craftingResults;
    private String description;
}
//Recipe {
//    Name: <string>
//            XpForFirstCrafting: <number>
//            XpForSubsequentCrafts: <number>
//            Ingredients: <List<Item>>
//    CraftingResults: <List<Item>>
//    Description: <string>
//            Source: <List<Source>>
//}