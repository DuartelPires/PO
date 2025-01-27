package hva.core;
import java.util.*;

public class Veterinarian extends Employee {
    private List<String> _especies = new ArrayList<>();
    private List<String> _atosMedicos = new ArrayList<>();
    
    public Veterinarian(String chaveUnicaFuncionario, String nome) {
        super(chaveUnicaFuncionario, nome, "VET");
    }

    @Override
    public void addResponsibility(String responsibilityKey){
        if (!_especies.contains(responsibilityKey)) {
            _especies.add(responsibilityKey);
        }
    }

    @Override
    public void removeResponsibility(String responsibilityKey){
       _especies.removeIf(f -> f.equals(responsibilityKey)); 
    }

    @Override
    public String getResponsibility(){
        return String.join(",", _especies);
    }

    @Override
    public int getResponsibilitySize(){
        return _especies.size();
    }

    @Override
    public boolean hasResponsibility(String id){
        return _especies.contains(id);
    }

    @Override
    public List<String> getAtos(){
        return _atosMedicos;
    }

    @Override
    public void addAtos(String ato){
        _atosMedicos.add(ato);
    }

    @Override
    public String toString(){
        if(getResponsibilitySize() > 0){
            return "VET|" + getId() + "|" + getNome() + "|" + getResponsibility();
          }else return "VET|" + getId() + "|" + getNome();
    }
}
