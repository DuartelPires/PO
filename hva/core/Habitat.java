package hva.core;

import java.util.ArrayList;
import java.util.List;

public class Habitat implements Entity{
    private final String _chaveUnicaHabitat;
    private final String _nomeHabitat;
    private int _area;
    private List<String> _arvoresId = new ArrayList<>();
    private List<Animal> _animais = new ArrayList<>();


    public Habitat(String chaveUnica, String nome, int area){
        _chaveUnicaHabitat = chaveUnica;
        _nomeHabitat = nome;
        _area = area;
    }

    @Override
    public String getId(){
        return _chaveUnicaHabitat; 
    }

    public int getArea(){
        return _area;
    }

    public String getNomeHabitat(){
        return _nomeHabitat;
    }

    public Habitat getHabitat(){
        return this;
    }

    public int getNumeroArvores(){
        return _arvoresId.size();
    }

    public void removeAnimal(String id){
        _animais.removeIf(f -> f.getId().equals(id));
    }

    public String getArvoreIds() {
        if(_arvoresId.isEmpty()){
            return "";
        }
        return String.join(",", _arvoresId);
    }

    public List<Animal> getAnimals(){
        List<Animal> animais = new ArrayList<>(_animais);
        return animais;
    }
    
    public void setArea(int area){
        _area = area;
    }

    public void addAnimal(Animal animal){
        _animais.add(animal);
    }

    public void addArvore(String idArvore){
        _arvoresId.add(idArvore);
    }

    @Override
    public String toString() {
        if(getNumeroArvores() != 0){
            return "HABITAT|" + _chaveUnicaHabitat + "|" + _nomeHabitat + "|" + Integer.toString(_area) + "|" + getNumeroArvores();
        }
        return "HABITAT|" + _chaveUnicaHabitat + "|" + _nomeHabitat + "|" + Integer.toString(_area);
    }
}
