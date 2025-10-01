package neko.kuro.projectGorgonCrafting.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import neko.kuro.projectGorgonCrafting.entities.Item;

import java.util.UUID;

@Repository
public interface ItemRepository extends CrudRepository<Item, UUID> {
}
