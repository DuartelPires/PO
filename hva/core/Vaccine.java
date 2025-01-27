package hva.core;

import java.util.*;

public class Vaccine implements Entity{
    private final String _chaveUnicaVacina;
    private final String _nomeVacina;  
    private List<Species> _especies = new ArrayList<>(); 
    private int _registoVacinas;
    public Vaccine(String chaveUnica, String nome, List<Species> especies){
        _chaveUnicaVacina = chaveUnica;
        _nomeVacina = nome;
        _especies = especies;
    }


    public String getNomeVacina(){
        return _nomeVacina;
    }

    public String getChaveEspecies(){
        _especies.sort(Comparator.comparing(Species::getId));
        StringBuilder result = new StringBuilder();       

        for (Species species : _especies) {
            if (result.length() > 0) {
                result.append(",");
            }
            result.append(species.getId());
        }
    
        return result.toString();
    }

    @Override
    public String getId(){
        return _chaveUnicaVacina;
    }

    public Vaccine getVacina(){
        return this;
    }

    public int getNumeroAplicacoes(){
        return _registoVacinas;
    }

    public void addUtilizacao(){
        _registoVacinas++;
    }

    @Override
    public String toString() {
        if(getChaveEspecies().equals("")){
            return ("VACINA|" + getId() + "|" + getNomeVacina() + "|" + Integer.toString(getNumeroAplicacoes()));
          }else  return "VACINA|" + getId() + "|" + getNomeVacina() + "|" + Integer.toString(getNumeroAplicacoes()) + "|" +  getChaveEspecies();
    }
}
