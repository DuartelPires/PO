package hva.core;

import java.util.ArrayList;
import java.util.List;

public class Species implements Entity{
    private final String _chaveUnicaEspecie;
    private final String _nomeEspecie;
    private List<Animal> _animais = new ArrayList<>();

    public Species(String chaveUnica, String nome){
        _chaveUnicaEspecie = chaveUnica;
        _nomeEspecie = nome;
    }

    public void addAnimals(Animal animal){
        _animais.add(animal);
    }

    @Override
    public String getId(){
        return _chaveUnicaEspecie; 
    }

    public Species getEspecie(){
        return this;
    }

    public int getQuantidadeAnimais(){
        return _animais.size();
    }

    public String getNome(){
        return _nomeEspecie;
    }
}
