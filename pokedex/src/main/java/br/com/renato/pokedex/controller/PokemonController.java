package br.com.renato.pokedex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.renato.pokedex.model.Pokemon;
import br.com.renato.repository.PokemonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {

	@Autowired
	private PokemonRepository repository;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Pokemon> save(@RequestBody Pokemon pokemon) {
		return repository.save(pokemon);
	}

	@PutMapping("{id}")
    public Mono<ResponseEntity<Pokemon>> update(@PathVariable(value = "id") String id,
                                                       @RequestBody Pokemon pokemon) {
        return repository.findById(id)
                .flatMap(existePokemon -> {
                    existePokemon.setNome(pokemon.getNome());
                    existePokemon.setCategoria(pokemon.getCategoria());
                    existePokemon.setHabilidade(pokemon.getHabilidade());
                    existePokemon.setPeso(pokemon.getPeso());
                    return repository.save(existePokemon);
                })
                .map(update -> ResponseEntity.ok(update))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }
	
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deletePokemon(@PathVariable(value = "id") String id) {
        return repository.findById(id)
                .flatMap(existingPokemon ->
                        repository.delete(existingPokemon)
                                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<Void> deleteAllPokemons() {
        return repository.deleteAll();
    }

	@GetMapping
	public Flux<Pokemon> findAll() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Pokemon>> findById(@PathVariable String id) {
		return repository.findById(id).map(pokemon -> ResponseEntity.ok(pokemon))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
