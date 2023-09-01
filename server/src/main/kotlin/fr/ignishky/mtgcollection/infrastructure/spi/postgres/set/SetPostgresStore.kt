package fr.ignishky.mtgcollection.infrastructure.spi.postgres.set

import fr.ignishky.mtgcollection.domain.set.model.Set
import fr.ignishky.mtgcollection.domain.set.model.SetId
import fr.ignishky.mtgcollection.domain.set.port.SetStorePort
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.mapper.SetEntityMapper.fromSetEntity
import fr.ignishky.mtgcollection.infrastructure.spi.postgres.set.model.mapper.SetEntityMapper.toSetEntity
import jakarta.inject.Named

@Named
class SetPostgresStore(
    private val setRepository: SetRepository,
) : SetStorePort {

    override fun store(set: Set) {
        setRepository.save(toSetEntity(set))
    }

    override fun getAll(): List<Set> {
        return setRepository.findAll()
            .map { fromSetEntity(it) }
    }

    override fun get(id: SetId): Set {
        return setRepository.findById(id.value)
            .map { fromSetEntity(it) }
            .get()
    }

}
