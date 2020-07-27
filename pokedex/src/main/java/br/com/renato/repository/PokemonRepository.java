package br.com.renato.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import br.com.renato.pokedex.model.Pokemon;

public interface PokemonRepository extends ReactiveMongoRepository<Pokemon, String> {

}
