package neko.kuro.projectGorgonCrafting.dtos;

import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.util.List;

@Serdeable
@Data
@JsonSchema
public class Item {
    private String title;
    private String dynamicCraftingTitle;
    private List<Behaviour> behaviours;
    private String description;
    private String droppedAppearance;
    private Long iconId;
    private String internalName;
    private List<String> keywords;
    private Integer maxStackSize;
    private String name;
    private Integer numUses;
    private Integer value;
}
