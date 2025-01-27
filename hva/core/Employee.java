package hva.core;

import hva.app.exception.NoResponsibilityException;
import java.util.*;

public abstract class Employee implements Entity{
    private final String _chaveUnicaFuncionario;
    private final String _funcionarioNome;
    private final String _funcionarioTipo;
    private int _satisfacaoNivel;
    private int _atribuido;

    public Employee(String id, String name, String type){
        _chaveUnicaFuncionario = id;
        _funcionarioNome = name;
        _funcionarioTipo = type;
    }

    public String getNome() {
        return _funcionarioNome;
    }

    public String getTipo() {
        return _funcionarioTipo;
    }

    @Override
    public String getId(){
        return _chaveUnicaFuncionario;
    }

    public abstract String getResponsibility();

    public abstract void addResponsibility(String responsibilityKey) throws NoResponsibilityException;

    public abstract void removeResponsibility(String responsibilityKey);

    public abstract int getResponsibilitySize();

    public abstract boolean hasResponsibility(String id);

    public abstract void addAtos(String string);

    public abstract List<String> getAtos();
}
