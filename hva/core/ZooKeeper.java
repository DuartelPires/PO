package hva.core;
import java.util.*;

public class ZooKeeper extends Employee{
    private List<String> _atos = new ArrayList<>();
    private List<String> _habitats = new ArrayList<>();

    public ZooKeeper(String chaveUnicaFuncionario, String nome) {
        super(chaveUnicaFuncionario, nome, "TRT");
    }

    @Override
    public void addResponsibility(String responsibilityKey){
        if (!_habitats.contains(responsibilityKey)) {
            _habitats.add(responsibilityKey);
        }
    }

    @Override
    public void removeResponsibility(String responsibilityKey){
       _habitats.removeIf(f -> f.equals(responsibilityKey)); 
    }

    @Override
    public String getResponsibility(){
        return String.join(",", _habitats);
    }

    @Override
    public int getResponsibilitySize(){
        return _habitats.size();
    }

    @Override
    public boolean hasResponsibility(String id){
        for(String f: _habitats){
            if(f.equals(id))
                return true;
        }
        return false;
    }

    @Override
    public void addAtos(String string) {
    }

    @Override
    public List<String> getAtos() {
        return _atos;
    }

    @Override
    public String toString(){
        if(getResponsibilitySize() > 0){
            return "TRT|" + getId() + "|" + getNome() + "|" + getResponsibility();
          }else return "TRT|" + getId() + "|" + getNome();
    }
}
