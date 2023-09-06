package fr.ignishky.mtgcollection.domain.card.exception

import fr.ignishky.mtgcollection.domain.set.model.SetCode

class NoCardFoundException(setCode: SetCode) : Exception("unknown set '${setCode.value}'")
