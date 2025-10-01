package neko.kuro.projectGorgonCrafting.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import neko.kuro.projectGorgonCrafting.entities.Person;

import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<Person, UUID> {
}