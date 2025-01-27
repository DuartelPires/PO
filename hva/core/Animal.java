package hva.core;
import java.util.*;


public class Animal implements Entity{
    private final String _chaveUnicaAnimal;
    private final String _nomeAnimal;
    private final Species _especie;
    private Habitat _habitat;
    private StringBuilder _historialSaude = new StringBuilder("");
    private List<String> _atosMedicos = new ArrayList<>();
    public Animal(String chaveUnica, String nome, Habitat habitat, Species especie){
        _chaveUnicaAnimal = chaveUnica;
        _nomeAnimal = nome;
        _especie = especie;
        _habitat = habitat;
    }

    @Override
    public String getId(){
        return _chaveUnicaAnimal;
    }

    public String getNomeAnimal(){
        return _nomeAnimal;
    }

    public String getChaveEspecie(){
        return _especie.getId();
    }

    public Species getEspecie(){
        return _especie;
    }

    public String getChaveHabitat(){
        return _habitat.getId();
    }
    
    public Habitat getHabitat(){
        return _habitat;
    }

    public void setHabitat(Habitat habitat){
        _habitat = habitat;
    }

    public void setEstadoSaude(String estadoSaude){
        if (_historialSaude.length() > 0) {
            _historialSaude.append(",");
        }
        _historialSaude.append(estadoSaude);
    }

    public String getHistorialSaude(){
        String historial = _historialSaude.toString();
        if(historial.equals("")){
            return "VOID";
        }

        if (historial.endsWith(",")) {
            return historial.substring(0, historial.length() - 1);
        }
        return historial;
    }

    public void addAtosMedicos(String ato){
        _atosMedicos.add(ato);
    }

    public List<String> getAtosMedicos(){
        return _atosMedicos;
    }

    @Override
    public String toString() {
        return "ANIMAL|" + _chaveUnicaAnimal + "|" + _nomeAnimal + "|" + getChaveEspecie() + "|" + getHistorialSaude() + "|" + getChaveHabitat();
    }
}
