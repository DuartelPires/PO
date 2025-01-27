package hva.core;

import java.util.*;

public class Tree implements Entity{
    private final String _chaveUnicaArvore;
    private final String _nomeArvore;
    private int _idade;
    private final int _dificuldadeLimpeza;
    private final String _tipoFolha;
    private int _meses;
    private Map<EstacaoAno, String> _cicloBiologico;

    public Tree(String chave,String nome,String folha ,int idade,int dificuldade){
        _chaveUnicaArvore = chave;
        _nomeArvore = nome;
        _idade = idade;
        _dificuldadeLimpeza = dificuldade;
        _tipoFolha = folha;
        _cicloBiologico = new HashMap<>();
        inicializarCicloBiologico();
    }

    private void inicializarCicloBiologico() {
        if (_tipoFolha.equals("CADUCA")) {
            _cicloBiologico.put(EstacaoAno.Inverno, "SEMFOLHAS");
            _cicloBiologico.put(EstacaoAno.Primavera, "GERARFOLHAS");
            _cicloBiologico.put(EstacaoAno.Verao, "COMFOLHAS");
            _cicloBiologico.put(EstacaoAno.Outono, "LARGARFOLHAS");
        } else if (_tipoFolha.equals("PERENE")) {
            _cicloBiologico.put(EstacaoAno.Inverno, "LARGARFOLHAS");
            _cicloBiologico.put(EstacaoAno.Primavera, "GERARFOLHAS");
            _cicloBiologico.put(EstacaoAno.Verao, "COMFOLHAS");
            _cicloBiologico.put(EstacaoAno.Outono, "COMFOLHAS");
        }
    }

    public String getStatus(EstacaoAno estacao) {
        return _cicloBiologico.get(estacao);
    }
    
    @Override
    public String getId(){
        return _chaveUnicaArvore;
    }

    public Tree getTree(){
        return this;
    }

    public String getNomeArvore(){
        return _nomeArvore;
    }

    public int getIdade(){
        return _idade;
    }

    public int getDificuldadeLimpeza(){
        return _dificuldadeLimpeza;
    }

    public String getTipoFolha(){
        return _tipoFolha;
    }

    public void addMes(){
        if(_meses == 3){
            _meses = 0;
            _idade++;
            return;
        }
        _meses++;
    }

    @Override
    public String toString(){
        return "ÁRVORE|" + getId() + "|" + getNomeArvore() + "|" + getIdade() + "|" + getDificuldadeLimpeza() + "|" + getTipoFolha() + "|";
    }
}
