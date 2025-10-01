package neko.kuro.projectGorgonCrafting.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import neko.kuro.projectGorgonCrafting.entities.Creature;

import java.util.UUID;

@Repository
public interface CreatureRepository extends CrudRepository<Creature, UUID> {
}
