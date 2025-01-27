package hva.core;

import hva.app.exception.DuplicateAnimalKeyException;
import hva.app.exception.DuplicateEmployeeKeyException;
import hva.app.exception.DuplicateHabitatKeyException;
import hva.app.exception.DuplicateTreeKeyException;
import hva.app.exception.DuplicateVaccineKeyException;
import hva.app.exception.NoResponsibilityException;
import hva.app.exception.UnknownAnimalKeyException;
import hva.app.exception.UnknownEmployeeKeyException;
import hva.app.exception.UnknownHabitatKeyException;
import hva.app.exception.UnknownSpeciesKeyException;
import hva.app.exception.UnknownTreeKeyException;
import hva.app.exception.UnknownVaccineKeyException;
import hva.app.exception.UnknownVeterinarianKeyException;
import hva.app.exception.VeterinarianNotAuthorizedException;
import hva.core.exception.*;
import java.io.*;
import static java.lang.Math.log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotel implements Serializable {

  @Serial
  private static final long serialVersionUID = 202407081733L;
  
  /*
    TO DO LIST
      -ver parte confusao Normal etc
  
   */
  
  /**
   * Read text input file and create corresponding domain entities.
   * 
   * @param filename name of the text input file
   * @throws UnrecognizedEntryException if some entry is not correct
   * @throws IOException if there is an IO erro while processing the text file
   * @throws UnknownHabitatKeyException 
   **/
  void importFile(String filename) throws UnrecognizedEntryException, IOException /* FIXME maybe other exceptions */, UnknownHabitatKeyException  {
      Parser parser = new Parser(this);
      parser.parseFile(filename);
  }

  private EntityGroup<Animal> _animais;
  private EntityGroup<Species> _especies;
  private EntityGroup<Employee> _funcionarios;
  private EntityGroup<Habitat> _habitats;
  private EntityGroup<Vaccine> _vacinas;
  private EntityGroup<Tree> _arvores;
  private List<String> _historicoVacinas;
  private List<String> _historicoVacinasErradas;
  private Map<String, Map<String, String>> _influenciasHabitatEspecie;
  private EstacaoAno _estacaoAtual;
  private boolean _alteracoes;

  public Hotel(){
    _animais = new EntityGroup<>();
    _especies = new EntityGroup<>();
    _funcionarios = new EntityGroup<>();
    _habitats = new EntityGroup<>();
    _vacinas = new EntityGroup<>();
    _arvores = new EntityGroup<>();  
    _historicoVacinas = new ArrayList<>();
    _historicoVacinasErradas = new ArrayList<>();
    _influenciasHabitatEspecie = new HashMap<>();
    _estacaoAtual = EstacaoAno.Primavera;
  }
   

// Registos    

/**
 * Regista um novo habitat no sistema.
 *
 * @param habitatId O identificador único do habitat.
 * @param name O nome do habitat.
 * @param area A área total do habitat em metros quadrados.
 * @return O objeto {@link Habitat} recém-criado.
 * @throws DuplicateHabitatKeyException Se já existir um habitat com o mesmo identificador (habitatId).
 * 
 * O método verifica se um habitat com o mesmo identificador já existe. Se sim, lança uma exceção
 * {@link DuplicateHabitatKeyException}. Caso contrário, cria e regista um novo habitat.
 * Após o registro, marca o sistema como alterado, indicando que houve mudanças.
 */

  public Habitat registerHabitat(String habitatId, String name,int area)throws DuplicateHabitatKeyException{
    if(_habitats.checkExists(habitatId)){
      throw new DuplicateHabitatKeyException(habitatId);
    }
    Habitat novoHabitat = new Habitat(habitatId, name, area);
    _habitats.addEntity(novoHabitat);
    setAlteracoesTrue();
    return novoHabitat;
  }


/**
 * Regista uma nova espécie no sistema.
 *
 * @param speciesId O identificador único da espécie.
 * @param name O nome da espécie.
 * 
 * O método cria uma nova instância de {@link Species} usando o identificador e o nome fornecidos,
 * e a adiciona à lista de espécies no sistema. Após a adição, marca o sistema como alterado, 
 * indicando que houve modificações.
 */
  public void registerSpecies(String speciesId,String name) {
    Species novaEspecie = new Species(speciesId, name);
    _especies.addEntity(novaEspecie);
    setAlteracoesTrue();
  }


/**
 * Regista um novo animal no sistema.
 *
 * @param animalId O identificador único do animal.
 * @param name O nome do animal.
 * @param habitatId O identificador do habitat onde o animal será registado.
 * @param speciesId O identificador da espécie à qual o animal pertence.
 * @throws UnknownSpeciesKeyException Se a espécie especificada pelo identificador não for encontrada.
 * @throws UnknownHabitatKeyException Se o habitat especificado pelo identificador não for encontrado.
 * @throws DuplicateAnimalKeyException Se já existir um animal com o mesmo identificador (animalId).
 * 
 * O método verifica se já existe um animal com o mesmo identificador. Se sim, lança uma exceção
 * {@link DuplicateAnimalKeyException}. Em seguida, valida os identificadores de habitat e espécie, 
 * e cria um novo objeto {@link Animal}, associando-o ao habitat e à espécie correspondentes.
 * Caso o habitat ou a espécie não sejam encontrados, lança as exceções {@link UnknownHabitatKeyException} 
 * ou {@link UnknownSpeciesKeyException}, respectivamente.
 * Após o registro do animal, ele é adicionado ao habitat e à espécie correspondentes, e o sistema 
 * é marcado como alterado.
 */

  public void registerAnimal(String animalId, String name, String habitatId, String speciesId) throws UnknownSpeciesKeyException, UnknownHabitatKeyException, DuplicateAnimalKeyException{
    if(_animais.checkExists(animalId)){
      throw new DuplicateAnimalKeyException(animalId);
    }

    try{
      Habitat habitat = validarHabitat(habitatId);
      Species especie = validarSpecies(speciesId);
      Animal novoAnimal = new Animal(animalId, name, habitat, especie);
      habitat.addAnimal(novoAnimal);
      _animais.addEntity(novoAnimal);
      especie.addAnimals(novoAnimal);
      setAlteracoesTrue();
    } catch (UnknownSpeciesKeyException e) {
      throw new UnknownSpeciesKeyException(speciesId);
    } catch (UnknownHabitatKeyException e) {
      throw new UnknownHabitatKeyException(habitatId);
    }
    
  }


/**
 * Regista uma nova vacina no sistema, associada a uma ou mais espécies.
 *
 * @param vaccinesId O identificador único da vacina.
 * @param name O nome da vacina.
 * @param speciesId Um array contendo os identificadores das espécies às quais a vacina é aplicável.
 * @throws UnknownSpeciesKeyException Se alguma das espécies especificadas pelo identificador não for encontrada.
 * @throws DuplicateVaccineKeyException Se já existir uma vacina com o mesmo identificador (vaccinesId).
 * 
 * O método verifica se já existe uma vacina com o identificador fornecido. Se sim, lança uma exceção
 * {@link DuplicateVaccineKeyException}. Em seguida, para cada identificador de espécie fornecido no array,
 * valida se a espécie existe no sistema. Caso uma das espécies não seja encontrada, lança a exceção 
 * {@link UnknownSpeciesKeyException} para o respectivo identificador. Se todas as espécies forem válidas,
 * cria um novo objeto {@link Vaccine} associando-o à lista de espécies e o regista no sistema.
 * Após o registro, marca o sistema como alterado.
 */

  public void registerVaccine(String vaccinesId, String name, String[] speciesId) throws UnknownSpeciesKeyException, DuplicateVaccineKeyException{
      List<Species> lista = new ArrayList<>();
  
      if(_vacinas.getEntity(vaccinesId) != null){
        throw new DuplicateVaccineKeyException(vaccinesId);
      }
      
        for(String f: speciesId){
          try{
            Species especie = validarSpecies(f);
            lista.add(especie);
          } catch(UnknownSpeciesKeyException e) {
            throw new UnknownSpeciesKeyException(f);
          }
        }
  
        Vaccine novaVacina = new Vaccine(vaccinesId, name, lista);
        _vacinas.addEntity(novaVacina);
        setAlteracoesTrue();
  }

/**
 * Regista um novo funcionário no sistema, podendo ser um veterinário ou tratador.
 *
 * @param employeeId O identificador único do funcionário.
 * @param name O nome do funcionário.
 * @param empType O tipo de funcionário, que pode ser "VET" para veterinário ou "TRT" para tratador.
 * @throws DuplicateEmployeeKeyException Se já existir um funcionário com o mesmo identificador (employeeId).
 * 
 * O método verifica se um funcionário com o identificador fornecido já existe no sistema. Se sim, lança 
 * uma exceção {@link DuplicateEmployeeKeyException}. Dependendo do tipo de funcionário especificado pelo 
 * parâmetro empType, ele cria e regista um novo objeto {@link Veterinarian} (se empType for "VET") 
 * ou um novo objeto {@link ZooKeeper} (se empType for "TRT"). Após a adição, marca o sistema como alterado.
 */

  public void registerEmployee(String employeeId,String name,String empType) throws DuplicateEmployeeKeyException{
    
    if(_funcionarios.checkExists(employeeId))
        throw new DuplicateEmployeeKeyException(employeeId);

    if(empType.equals("VET")){
      Veterinarian veterinario = new Veterinarian(employeeId, name);
      _funcionarios.addEntity(veterinario);
    }
    if(empType.equals("TRT")){
      ZooKeeper tratador = new ZooKeeper(employeeId, name);
      _funcionarios.addEntity(tratador);
    }
    setAlteracoesTrue();
  }

/**
 * Regista a aplicação de uma vacina para uma espécie no sistema.
 *
 * @param lista A lista onde o registro da aplicação da vacina será armazenado.
 * @param idVacina O identificador da vacina aplicada.
 * @param idvet O identificador do veterinário responsável pela aplicação.
 * @param idEspecie O identificador da espécie à qual a vacina foi aplicada.
 * 
 * O método adiciona à lista um registro da aplicação de uma vacina no formato:
 * "REGISTO-VACINA|idVacina|idvet|idEspecie", representando a vacina aplicada, o veterinário responsável 
 * e a espécie vacinada.
 */

  public void registarVacinaApilcada(List<String> lista, String idVacina, String idvet, String idEspecie){
    lista.add("REGISTO-VACINA|"+idVacina+"|"+idvet+"|"+idEspecie);
  }


/**
 * Cria e regista uma nova árvore no sistema.
 *
 * @param TreeId O identificador único da árvore.
 * @param name O nome da árvore.
 * @param type O tipo da árvore (por exemplo, espécie ou classificação).
 * @param age A idade da árvore.
 * @param diff O nível de dificuldade de cuidar da árvore.
 * @throws DuplicateTreeKeyException Se já existir uma árvore com o mesmo identificador (TreeId).
 * 
 * O método verifica se uma árvore com o identificador fornecido já existe no sistema. Se sim, lança
 * uma exceção {@link DuplicateTreeKeyException}. Caso contrário, cria um novo objeto {@link Tree} com 
 * os detalhes fornecidos e o regista no sistema. Após o registro, marca o sistema como alterado.
 */

  public void createTree(String TreeId,String  name,String type,int age,int diff) throws DuplicateTreeKeyException{
    if(_arvores.checkExists(TreeId)){
      throw new DuplicateTreeKeyException(TreeId);
    }
    Tree arvore = new Tree(TreeId, name, type, age, diff);
    _arvores.addEntity(arvore);
    setAlteracoesTrue();
  }


/**
 * Adiciona uma responsabilidade a um funcionário, verificando o tipo de responsabilidade com base no cargo.
 *
 * @param employeeId O identificador do funcionário.
 * @param responsibilityId O identificador da responsabilidade, que pode ser uma espécie (para veterinários) 
 *        ou um habitat (para tratadores).
 * @throws UnknownSpeciesKeyException Se a responsabilidade for uma espécie e o identificador não for encontrado.
 * @throws NoResponsibilityException Se a responsabilidade não for válida.
 * @throws UnknownHabitatKeyException Se a responsabilidade for um habitat e o identificador não for encontrado.
 * @throws UnknownEmployeeKeyException Se o funcionário com o identificador fornecido não for encontrado.
 * 
 * O método recupera o funcionário com base no `employeeId`. Se o funcionário não existir, lança a exceção
 * {@link UnknownEmployeeKeyException}. Dependendo do tipo do funcionário, que pode ser "VET" (veterinário) 
 * ou "TRT" (tratador), o método verifica se a responsabilidade é uma espécie ou um habitat, respectivamente.
 * Se a espécie ou o habitat não forem encontrados, lança as exceções {@link UnknownSpeciesKeyException} ou
 * {@link UnknownHabitatKeyException}. Caso contrário, a responsabilidade é adicionada ao funcionário, 
 * e o sistema é marcado como alterado.
 */

  public void addResponsibility(String employeeId,String responsibilityId) throws UnknownSpeciesKeyException, NoResponsibilityException, UnknownHabitatKeyException, UnknownEmployeeKeyException{
    Employee funcionario = _funcionarios.getEntity(employeeId);

    if (funcionario == null) {
      throw new UnknownEmployeeKeyException(employeeId);
  }

    if(funcionario.getTipo().equals("VET")){
      if(!checkExistsSpecies(responsibilityId))
        throw new UnknownSpeciesKeyException(responsibilityId);
    }
    if(funcionario.getTipo().equals("TRT")){
      if(!checkExistsHabitat(responsibilityId))
        throw new UnknownHabitatKeyException(responsibilityId);
    }
    funcionario.addResponsibility(responsibilityId);
    setAlteracoesTrue();
  }


/**
 * Remove uma responsabilidade de um funcionário.
 *
 * @param employeeId O identificador do funcionário.
 * @param responsibilityId O identificador da responsabilidade a ser removida.
 * @throws NoResponsibilityException Se o funcionário não possui a responsabilidade especificada.
 * 
 * O método recupera o funcionário com base no `employeeId`. Em seguida, verifica se o funcionário possui 
 * a responsabilidade especificada. Se não, lança a exceção {@link NoResponsibilityException}. 
 * Caso contrário, a responsabilidade é removida do funcionário e o sistema é marcado como alterado.
 */

  public void removeResponsibility(String employeeId,String responsibilityId) throws NoResponsibilityException{
    Employee funcionario = _funcionarios.getEntity(employeeId);
    if(!funcionario.getResponsibility().contains(responsibilityId)){
      throw new NoResponsibilityException(employeeId, responsibilityId);
    }
    funcionario.removeResponsibility(responsibilityId);
    setAlteracoesTrue();
  }


/**
 * Adiciona uma árvore a um habitat específico.
 *
 * @param idHabitat O identificador do habitat ao qual a árvore será adicionada.
 * @param idArvore O identificador da árvore a ser adicionada.
 * @throws UnknownHabitatKeyException Se o habitat especificado pelo identificador não for encontrado.
 * 
 * O método valida se o habitat com o identificador fornecido existe. Se não existir, lança a exceção 
 * {@link UnknownHabitatKeyException}. Caso contrário, a árvore identificada por `idArvore` é adicionada ao 
 * habitat correspondente.
 */

  public void addTreeHabitat(String idHabitat, String idArvore) throws UnknownHabitatKeyException{
    Habitat f = validarHabitat(idHabitat);
    f.addArvore(idArvore);
  }


/**
 * Transfere um animal de um habitat para outro.
 *
 * @param animalId O identificador do animal a ser transferido.
 * @param habitatId O identificador do habitat para o qual o animal será transferido.
 * @throws UnknownAnimalKeyException Se o animal especificado pelo identificador não for encontrado.
 * @throws UnknownHabitatKeyException Se o habitat especificado pelo identificador não for encontrado.
 * 
 * O método valida se o animal com o identificador fornecido existe. Se não existir, lança a exceção 
 * {@link UnknownAnimalKeyException}. Em seguida, valida o habitat atual do animal e remove o animal 
 * desse habitat. Após isso, valida o novo habitat especificado. Se ambos os identificadores forem válidos, 
 * o habitat do animal é atualizado para o novo habitat e o animal é adicionado a este novo habitat.
 */

  public void transferAnimal(String animalId, String habitatId) throws UnknownAnimalKeyException, UnknownHabitatKeyException {
    Animal animal = validarAnimal(animalId);
    
    Habitat currentHabitat = validarHabitat(animal.getHabitat().getId());
    currentHabitat.removeAnimal(animalId);
    
    Habitat novo =validarHabitat(habitatId);
    
    animal.setHabitat(novo);
    novo.addAnimal(animal);
  }
   

// Get listas

/**
 * Obtém uma lista de representações em string de uma lista de animais.
 *
 * @param animais A lista de objetos {@link Animal} para os quais as representações em string serão geradas.
 * @return Uma lista de strings, onde cada string é a representação em string de um animal.
 * 
 * O método percorre a lista de animais fornecida e adiciona a representação em string de cada 
 * objeto {@link Animal} à lista resultante. Essa lista é então retornada, permitindo que os detalhes 
 * de cada animal sejam apresentados em formato legível.
 */
  
  public List<String> getAnimalsList(List<Animal> animais){
    List<String> lista = new ArrayList<>(); 
    for(Animal f: animais){
      lista.add(f.toString());
    }
    return lista;
  }


/**
 * Obtém uma lista de representações em string de todos os animais registados no sistema.
 *
 * @return Uma lista de strings, onde cada string é a representação em string de um animal.
 * 
 * O método recupera todos os animais registados no sistema utilizando o método {@link _animais.getEntities()} 
 * e, em seguida, chama o método {@link #getAnimalsList(List)} para gerar a lista de representações em string 
 * de cada animal. Essa lista é retornada, permitindo que todas as informações dos animais sejam apresentadas 
 * em um formato legível.
 */

  public List<String> getAllAnimalsList(){
    return getAnimalsList(_animais.getEntities());
  }


/**
 * Obtém uma lista de representações em string de todos os animais que habitam um habitat específico.
 *
 * @param idHabitat O identificador do habitat cujos animais serão listados.
 * @return Uma lista de strings, onde cada string é a representação em string de um animal que habita o habitat.
 * @throws UnknownHabitatKeyException Se o habitat especificado pelo identificador não for encontrado.
 * 
 * O método valida se o habitat com o identificador fornecido existe. Se não existir, lança a exceção 
 * {@link UnknownHabitatKeyException}. Caso contrário, obtém a lista de animais que habitam esse habitat 
 * utilizando o método {@link Habitat#getAnimals()} e chama o método {@link #getAnimalsList(List)} 
 * para gerar a lista de representações em string dos animais. A lista resultante é retornada.
 */

  public List<String> getAllAnimalsHabitat(String idHabitat) throws UnknownHabitatKeyException{
    Habitat habitat = validarHabitat(idHabitat);
    return getAnimalsList(habitat.getAnimals());
  }


/**
 * Obtém uma lista de informações sobre todos os habitats e suas respectivas árvores.
 *
 * @return Uma lista de strings contendo informações sobre cada habitat, incluindo seus detalhes 
 * e uma lista de árvores associadas.
 * 
 * O método percorre todos os habitats registados no sistema e gera uma representação em string 
 * para cada habitat, que inclui seu identificador, nome, área e número de árvores. Se o habitat 
 * possui árvores associadas, o método tenta obter informações sobre essas árvores utilizando 
 * o método {@link #getAllTreesInHabitat(String)}. As informações sobre cada habitat e suas 
 * árvores são armazenadas em um mapa. A lista resultante é ordenada com base no identificador do habitat 
 * e retorna uma lista contendo as informações de cada habitat seguidas pelas informações das árvores 
 * associadas a cada um deles.
 */

  public List<String> getAllHabitatsList() {
    List<String> listaHabitatsEArvores = new ArrayList<>();
    Map<String, List<String>> mapaHabitats = new HashMap<>();

    for (Habitat habitat : _habitats.getEntities()) {
        String habitatInfo = "HABITAT|" + habitat.getId() + "|" + habitat.getNomeHabitat() + "|" + habitat.getArea() + "|" + habitat.getNumeroArvores();
        List<String> arvoreInfo = new ArrayList<>();

        if (!habitat.getArvoreIds().equals("")) {
            try {
                arvoreInfo.addAll(getAllTreesInHabitat(habitat.getId()));
            } catch (UnknownHabitatKeyException | UnknownTreeKeyException e) {
                e.printStackTrace();
            }
        }
        mapaHabitats.put(habitatInfo, arvoreInfo);
    }
    List<String> listaHabitats = new ArrayList<>(mapaHabitats.keySet());

    listaHabitats.sort(Comparator.comparing(s -> s.split("\\|")[1].trim(), String::compareToIgnoreCase));

    for (String habitat : listaHabitats) {
        listaHabitatsEArvores.add(habitat);  
        listaHabitatsEArvores.addAll(mapaHabitats.get(habitat));
    }

    return listaHabitatsEArvores;
}


/**
 * Obtém uma lista de representações em string de todas as vacinas registadas no sistema.
 *
 * @return Uma lista de strings, onde cada string é a representação em string de uma vacina.
 * 
 * O método percorre todas as vacinas registadas no sistema, utilizando o método 
 * {@link _vacinas.getEntities()} para obter as vacinas, e adiciona a representação em 
 * string de cada objeto {@link Vaccine} à lista resultante. Essa lista é retornada, permitindo 
 * que as informações de todas as vacinas sejam apresentadas em um formato legível.
 */

  public List<String> getAllVaccinesList(){
    List<String> lista = new ArrayList<>(); 
    for(Vaccine v: _vacinas.getEntities()){
      lista.add(v.toString());
    }
    return lista;
  }


/**
 * Obtém uma lista de representações em string de todos os funcionários registados no sistema.
 *
 * @return Uma lista de strings, onde cada string é a representação em string de um funcionário.
 * 
 * O método percorre todos os funcionários registados no sistema utilizando o método 
 * {@link _funcionarios.getEntities()} para obter os funcionários e adiciona a representação em 
 * string de cada objeto {@link Employee} à lista resultante. Essa lista é retornada, permitindo 
 * que as informações de todos os funcionários sejam apresentadas em um formato legível.
 */
  
  public List<String> getAllEmployees(){
    List<String> lista = new ArrayList<>();
    for(Employee e: _funcionarios.getEntities()){
      lista.add(e.toString());
    }
    return lista; 
  }


/**
 * Obtém uma lista de representações em string de todas as árvores que habitam um habitat específico.
 *
 * @param habitatId O identificador do habitat cujas árvores serão listadas.
 * @return Uma lista de strings, onde cada string é a representação em string de uma árvore que habita o habitat.
 * @throws UnknownHabitatKeyException Se o habitat especificado pelo identificador não for encontrado.
 * @throws UnknownTreeKeyException Se uma árvore especificada pelo identificador não for encontrada.
 * 
 * O método valida se o habitat com o identificador fornecido existe. Se não existir, lança a exceção 
 * {@link UnknownHabitatKeyException}. Caso o habitat não contenha árvores associadas (ou seja, se 
 * o identificador de árvores estiver vazio), uma lista contendo uma string vazia é retornada. 
 * Se houver árvores, o método divide a string de identificadores de árvores em uma lista, 
 * e para cada identificador, obtém a representação em string da árvore correspondente utilizando 
 * o método {@link #getArvoreString(String)}. A lista resultante de representações em string das árvores 
 * é retornada.
 */

  public List<String> getAllTreesInHabitat(String habitatId) throws UnknownHabitatKeyException, UnknownTreeKeyException {
    
    Habitat habitat = validarHabitat(habitatId);
    List<String> listaArvores = new ArrayList<>();

    if(habitat.getArvoreIds().equals("")){
      listaArvores.add("");
      return listaArvores;
    }
    
    for (String idArvore : dividirStringParaLista(habitat.getArvoreIds())) {
        listaArvores.add(getArvoreString(idArvore));
    }
    return listaArvores;
  }


/**
 * Obtém uma lista do histórico de vacinas aplicadas.
 *
 * @return Uma lista de strings representando o histórico de vacinas.
 * 
 * O método retorna uma nova lista contendo todos os registros do histórico de vacinas 
 * armazenados na coleção {@link _historicoVacinas}. Essa lista pode ser utilizada para 
 * consultar informações sobre as vacinas aplicadas ao longo do tempo, permitindo acesso 
 * fácil aos dados do histórico.
 */

  public List<String> getHistoricoVacinas(){
    return new ArrayList<>(_historicoVacinas);
  }


/**
 * Obtém uma lista do histórico de vacinas aplicadas incorretamente.
 *
 * @return Uma lista de strings representando o histórico de vacinas erradas.
 * 
 * O método retorna uma nova lista contendo todos os registros do histórico de vacinas 
 * que foram aplicadas incorretamente, armazenados na coleção {@link _historicoVacinasErradas}. 
 * Essa lista pode ser utilizada para consultar informações sobre as vacinas que apresentaram 
 * problemas durante a aplicação, permitindo acesso fácil aos dados do histórico de erros.
 */

  public List<String> getHistoricoErradoVacinas(){
    return new ArrayList<>(_historicoVacinasErradas);
  }


  /**
 * Obtém a estação do ano atual.
 *
 * @return Um objeto {@link EstacaoAno} representando a estação do ano atual.
 * 
 * O método retorna a estação do ano que está atualmente em vigor, 
 * representada pelo atributo {@link _estacaoAtual}. Essa informação pode 
 * ser utilizada para determinar as condições sazonais e influenciar outras 
 * operações que dependem da estação do ano.
 */

  public EstacaoAno getEstacaoAtual() {
    return _estacaoAtual;
  }


/**
 * Obtém a lista de atos médicos realizados por um funcionário veterinário específico.
 *
 * @param funcionarioId O identificador do funcionário veterinário cujos atos médicos serão retornados.
 * @return Uma lista de strings representando os atos médicos realizados pelo veterinário.
 * @throws UnknownVeterinarianKeyException Se o funcionário veterinário especificado não for encontrado.
 * 
 * O método valida a existência do veterinário utilizando o identificador fornecido. Se o veterinário 
 * não for encontrado, lança a exceção {@link UnknownVeterinarianKeyException}. Caso contrário, o método 
 * retorna a lista de atos médicos registados pelo funcionário, acessando a informação através do 
 * método {@link Employee#getAtos()}.
 */

  public List<String> getAtosMedicosFuncionario(String funcionarioId) throws UnknownVeterinarianKeyException{
    Employee funcionario = validarVeterinario(funcionarioId);
    return funcionario.getAtos();
  }


/**
 * Obtém a lista de atos médicos realizados em um animal específico.
 *
 * @param animalId O identificador do animal cujos atos médicos serão retornados.
 * @return Uma lista de strings representando os atos médicos realizados no animal.
 * @throws UnknownAnimalKeyException Se o animal especificado não for encontrado.
 * 
 * O método valida a existência do animal utilizando o identificador fornecido. Se o animal 
 * não for encontrado, lança a exceção {@link UnknownAnimalKeyException}. Caso contrário, o método 
 * retorna a lista de atos médicos registados para o animal, acessando a informação através do 
 * método {@link Animal#getAtosMedicos()}.
 */

  public List<String> getAtosMedicosAnimal(String animalId) throws UnknownAnimalKeyException{
    Animal animal = validarAnimal(animalId);
    return animal.getAtosMedicos();
  }


  // Validar

/**
 * Valida a existência de uma árvore com o identificador fornecido.
 *
 * @param Id O identificador da árvore a ser validada.
 * @return Um objeto {@link Tree} correspondente ao identificador fornecido.
 * @throws UnknownTreeKeyException Se a árvore especificada não for encontrada.
 * 
 * O método busca a árvore no repositório utilizando o identificador fornecido. Se a árvore 
 * for encontrada, ela é retornada. Caso contrário, é lançada a exceção 
 * {@link UnknownTreeKeyException}, indicando que a árvore com o identificador fornecido não 
 * existe no sistema.
 */

  public Tree validarTree(String Id)  throws  UnknownTreeKeyException{
    Tree tree = _arvores.getEntity(Id);
    if(tree != null){
      return tree;
    }
    throw new UnknownTreeKeyException(Id);
  }


/**
 * Valida a existência de um veterinário com o identificador fornecido.
 *
 * @param idVeterinarian O identificador do veterinário a ser validado.
 * @return Um objeto {@link Employee} correspondente ao veterinário identificado.
 * @throws UnknownVeterinarianKeyException Se o veterinário especificado não for encontrado ou não for do tipo "VET".
 * 
 * O método busca o funcionário no repositório utilizando o identificador fornecido. Se o funcionário 
 * não for encontrado ou se o tipo do funcionário não for "VET", é lançada a exceção 
 * {@link UnknownVeterinarianKeyException}. Caso contrário, o objeto do veterinário é retornado, 
 * permitindo o acesso às informações específicas do veterinário.
 */

  public Employee validarVeterinario(String idVeterinarian) throws UnknownVeterinarianKeyException {
    Employee funcionario = _funcionarios.getEntity(idVeterinarian);
    if (funcionario == null || !funcionario.getTipo().equals("VET")) {
        throw new UnknownVeterinarianKeyException(idVeterinarian);
    }
    return funcionario;
  }


/**
 * Valida a existência de um animal com o identificador fornecido.
 *
 * @param idAnimal O identificador do animal a ser validado.
 * @return Um objeto {@link Animal} correspondente ao identificador fornecido.
 * @throws UnknownAnimalKeyException Se o animal especificado não for encontrado.
 * 
 * O método busca o animal no repositório utilizando o identificador fornecido. Se o animal 
 * não for encontrado, é lançada a exceção {@link UnknownAnimalKeyException}. Caso contrário, 
 * o objeto do animal é retornado, permitindo o acesso às informações específicas do animal.
 */

  public Animal validarAnimal(String idAnimal) throws UnknownAnimalKeyException {
    Animal animal = _animais.getEntity(idAnimal);
    if (animal == null) {
        throw new UnknownAnimalKeyException(idAnimal);
    }
    return animal;
  }


/**
 * Valida a existência de uma vacina com o identificador fornecido.
 *
 * @param idVaccine O identificador da vacina a ser validada.
 * @return Um objeto {@link Vaccine} correspondente ao identificador fornecido.
 * @throws UnknownVaccineKeyException Se a vacina especificada não for encontrada.
 * 
 * O método busca a vacina no repositório utilizando o identificador fornecido. Se a vacina 
 * não for encontrada, é lançada a exceção {@link UnknownVaccineKeyException}. Caso contrário, 
 * o objeto da vacina é retornado, permitindo o acesso às informações específicas da vacina.
 */

  public Vaccine validarVacina(String idVaccine) throws UnknownVaccineKeyException {
    Vaccine vacina = _vacinas.getEntity(idVaccine);
    if (vacina == null) {
        throw new UnknownVaccineKeyException(idVaccine);
    }
    return vacina;
  }


/**
 * Valida a existência de um habitat com o identificador fornecido.
 *
 * @param idHabitat O identificador do habitat a ser validado.
 * @return Um objeto {@link Habitat} correspondente ao identificador fornecido.
 * @throws UnknownHabitatKeyException Se o habitat especificado não for encontrado.
 * 
 * O método busca o habitat no repositório utilizando o identificador fornecido. Se o habitat 
 * não for encontrado, é lançada a exceção {@link UnknownHabitatKeyException}. Caso contrário, 
 * o objeto do habitat é retornado, permitindo o acesso às informações específicas do habitat.
 */

  public Habitat validarHabitat(String idHabitat) throws UnknownHabitatKeyException {
    Habitat habitat = _habitats.getEntity(idHabitat);
    if (habitat == null) {
        throw new UnknownHabitatKeyException(idHabitat);
    }
    return habitat;
  }

  
/**
 * Valida se um veterinário está autorizado a tratar um determinado tipo de animal.
 *
 * @param funcionario O funcionário do tipo Employee que representa o veterinário.
 * @param especieAnimal A espécie do animal que se deseja tratar.
 * @throws VeterinarianNotAuthorizedException Se o veterinário não está autorizado a tratar a espécie do animal.
 */

  public void validarAutorizacaoVeterinario(Employee funcionario, String especieAnimal) throws VeterinarianNotAuthorizedException {
    List<String> listaEspeciesResponsaveis = Arrays.asList(funcionario.getResponsibility().split(","));
    if (!listaEspeciesResponsaveis.contains(especieAnimal)) {
        throw new VeterinarianNotAuthorizedException(funcionario.getId(), especieAnimal);
    }
  }


/**
 * Valida se a chave de identificação de uma espécie é conhecida e retorna a espécie correspondente.
 *
 * @param Id A chave de identificação da espécie a ser validada.
 * @return A instância de Species correspondente ao Id fornecido.
 * @throws UnknownSpeciesKeyException Se a chave de identificação não corresponder a nenhuma espécie conhecida.
 */

  public Species validarSpecies(String Id) throws UnknownSpeciesKeyException{
    Species especie = _especies.getEntity(Id);
    if(especie != null){
      return especie;
    }
    throw new UnknownSpeciesKeyException(Id);
  }


/**
 * Verifica se uma árvore com o identificador especificado existe.
 *
 * @param id O identificador da árvore a ser verificada.
 * @return true se a árvore existir; false caso contrário.
 */

  public boolean checkExistsTree(String id){
    return _arvores.checkExists(id);
  }


/**
 * Verifica se uma espécie com o identificador especificado existe.
 *
 * @param id O identificador da espécie a ser verificada.
 * @return true se a espécie existir; false caso contrário.
 */

  public boolean checkExistsSpecies(String id){
    return _especies.checkExists(id);
  }


/**
 * Verifica se um habitat com o identificador especificado existe.
 *
 * @param id O identificador do habitat a ser verificado.
 * @return true se o habitat existir; false caso contrário.
 */

  public boolean checkExistsHabitat(String id){
    return _habitats.checkExists(id);
  }


/**
 * Adiciona um animal a uma espécie específica.
 *
 * @param id O identificador do animal a ser adicionado à espécie.
 * @param especie A instância de Species à qual o animal será adicionado.
 */

  public void addAnimalSpecie(String id, Species especie){
    Animal animal = _animais.getEntity(id);
    especie.addAnimals(animal);
  }


/**
 * Verifica se uma vacina é aplicável a uma espécie específica de animal.
 *
 * @param idVaccine O identificador da vacina a ser verificada.
 * @param idAnimal O identificador do animal cuja espécie será verificada.
 * @return true se a vacina é aplicável à espécie do animal; false caso contrário.
 * @throws UnknownVaccineKeyException Se a chave da vacina fornecida não for reconhecida.
 * @throws UnknownAnimalKeyException Se a chave do animal fornecida não for reconhecida.
 */

  public boolean checkVaccineHasSpecie(String idVaccine, String idAnimal) throws UnknownVaccineKeyException, UnknownAnimalKeyException{
    Animal animal = validarAnimal(idAnimal);
    String especie = animal.getChaveEspecie();
    Vaccine vacina = validarVacina(idVaccine);

    List<String> listaEspeciesVacinas = Arrays.asList(vacina.getChaveEspecies().split(","));

    return listaEspeciesVacinas.contains(especie);
  }
  

/**
 * Verifica se existem alterações registadas.
 *
 * @return true se houver alterações; false caso contrário.
 */

  public boolean getExistemAlteracoes(){
    return _alteracoes;
  }


/**
 * Conta o número de animais em um habitat que pertencem à mesma espécie que o animal fornecido, 
 * excluindo o próprio animal da contagem.
 *
 * @param animal O animal cuja espécie será comparada.
 * @param habitat O habitat no qual os animais serão verificados.
 * @return O número de animais no habitat que têm a mesma espécie que o animal fornecido.
 */

  public int especieIgual(Animal animal, Habitat habitat){
    int equalTotal = 0;
    String especie = animal.getChaveEspecie();
    for(Animal h : habitat.getAnimals()){
      if(h.getChaveEspecie().equals(especie) && !h.equals(animal)){
        equalTotal++;
      }
    }
    return equalTotal;
  }


/**
 * Conta o número de animais em um habitat que pertencem a espécies diferentes da do animal fornecido.
 *
 * @param animal O animal cuja espécie será utilizada como referência.
 * @param habitat O habitat no qual os animais serão verificados.
 * @return O número de animais no habitat que têm uma espécie diferente da do animal fornecido.
 */

  public int especieDiferente(Animal animal, Habitat habitat){
    int diftotal = 0;
    String Idespecie = animal.getChaveEspecie();
    for(Animal h : habitat.getAnimals()){
      if(!h.getChaveEspecie().equals(Idespecie)){
        diftotal++;
      }
    }
    return diftotal;
  }


  // Complementares

/**
 * Define o estado de alterações como verdadeiro.
 *
 * Esta função é usada para indicar que houve alterações em algum estado ou entidade relacionada.
 */

  public void setAlteracoesTrue(){
    _alteracoes = true;
  }


/**
 * Define o estado de alterações como falso.
 *
 * Esta função é usada para indicar que não há alterações em algum estado ou entidade relacionada.
 */
  
  public void setAlteracoesFalse(){
    _alteracoes = false;
  }


/**
 * Altera a área de um habitat especificado pelo identificador.
 *
 * @param id O identificador do habitat cuja área será alterada.
 * @param area O novo valor da área a ser definido para o habitat.
 * @throws UnknownHabitatKeyException Se a chave do habitat fornecida não for reconhecida.
 */

  public void changeHabitatArea(String id, int area) throws UnknownHabitatKeyException{
    Habitat habitat = validarHabitat(id);
    habitat.setArea(area);
  }


/**
 * Altera a influência de uma espécie em um habitat específico.
 * A influência pode ser "POS" (positiva), "NEG" (negativa) ou "NEU" (neutra).
 *
 * @param habitatId   Identificador do habitat onde a influência será alterada.
 * @param especieId   Identificador da espécie cuja influência será alterada.
 * @param influencia  Tipo de influência ("POS", "NEG" ou "NEU").
 * @throws UnknownHabitatKeyException  Se o identificador do habitat não for válido.
 * @throws UnknownSpeciesKeyException  Se o identificador da espécie não for válido.
 * @throws IllegalArgumentException    Se o valor de influência não for "POS", "NEG" ou "NEU".
 */

  public void alterarInfluenciaHabitat(String habitatId, String especieId, String influencia) throws UnknownHabitatKeyException, UnknownSpeciesKeyException {
    Habitat habitat = validarHabitat(habitatId);  
    Species especie = validarSpecies(especieId);  

    if (!influencia.equals("POS") && !influencia.equals("NEG") && !influencia.equals("NEU")) {
        throw new IllegalArgumentException("Resposta inválida. Por favor, insira POS, NEG ou NEU.");
    }
    
    _influenciasHabitatEspecie.putIfAbsent(habitatId, new HashMap<>());

    _influenciasHabitatEspecie.get(habitatId).put(especieId, influencia);
  }


/**
 * Consulta a influência de uma espécie em um habitat específico.
 * Retorna uma string representando a influência ("POS", "NEG" ou "NEU").
 *
 * @param habitatId   Identificador do habitat a ser consultado.
 * @param especieId   Identificador da espécie a ser consultada.
 * @return            A influência da espécie no habitat ("POS", "NEG" ou "NEU") ou uma string vazia se não houver influência registada.
 * @throws UnknownHabitatKeyException  Se o identificador do habitat não for válido.
 * @throws UnknownSpeciesKeyException  Se o identificador da espécie não for válido.
 */

  public String consultarInfluenciaHabitat(String habitatId, String especieId) throws UnknownHabitatKeyException, UnknownSpeciesKeyException {
    Habitat habitat = validarHabitat(habitatId);  
    Species especie = validarSpecies(especieId);  
    
    // ve se existe influencia registada
    if (_influenciasHabitatEspecie.containsKey(habitatId)) {
        String influencia = _influenciasHabitatEspecie.get(habitatId).get(especieId);
        if (influencia != null) {
            return influencia;
        }
    }
    return "";
  }


/**
 * Avança a estação atual do ano para a próxima, de acordo com a sequência: Inverno -> Primavera -> Verão -> Outono.
 * Retorna um código numérico representando a nova estação: 0 para Primavera, 1 para Verão, 2 para Outono e 3 para Inverno.
 *
 * @return O código numérico da nova estação do ano após o avanço.
 */

  public int avancarEstacao() {
    switch (_estacaoAtual) {
        case Inverno -> _estacaoAtual = EstacaoAno.Primavera;
        case Primavera -> _estacaoAtual = EstacaoAno.Verao;
        case Verao -> _estacaoAtual = EstacaoAno.Outono;
        case Outono -> _estacaoAtual = EstacaoAno.Inverno;
    }

    int estacaoCodigo = 0;
    switch (_estacaoAtual) {
        case Primavera -> estacaoCodigo = 0;
        case Verao -> estacaoCodigo = 1;
        case Outono -> estacaoCodigo = 2;
        case Inverno -> estacaoCodigo = 3;
    }
   
    adicionaMes();
    setAlteracoesTrue();
    return estacaoCodigo;
  }


/**
 * Retorna o número de animais presentes em um habitat específico.
 *
 * @param habitat  O habitat cuja população de animais será calculada.
 * @return         O número de animais no habitat.
 */

  public int populacao(Habitat habitat){
    return habitat.getAnimals().size();
  }


/**
 * Calcula a adequação de um animal a um habitat com base na influência da espécie no habitat.
 * A adequação é calculada como +20 para influência positiva, -20 para negativa e 0 para neutra.
 *
 * @param animal   O animal cuja adequação ao habitat será calculada.
 * @param habitat  O habitat onde a adequação será calculada.
 * @return         O valor numérico da adequação do animal ao habitat.
 * @throws UnknownHabitatKeyException  Se o identificador do habitat não for válido.
 * @throws UnknownSpeciesKeyException  Se o identificador da espécie do animal não for válido.
 */

  public int adequacao(Animal animal, Habitat habitat) throws UnknownHabitatKeyException, UnknownSpeciesKeyException{
      int num;
      num = switch (consultarInfluenciaHabitat(habitat.getId(), animal.getChaveEspecie())) {
          case "POS" -> 20;
          case "NEG" -> -20;
          case "NEU" -> 0;
          default -> 0;
      };
      return num;
  }


/**
 * Calcula o esforço de limpeza necessário para uma árvore com base no tipo de folha e na estação do ano atual.
 * O esforço varia dependendo da estação e do tipo de folha (PERENE ou CADUCA) e leva em consideração a idade da árvore.
 *
 * @param idArvore  O identificador da árvore para a qual o esforço de limpeza será calculado.
 * @return          O valor do esforço de limpeza para a árvore especificada.
 * @throws UnknownTreeKeyException  Se o identificador da árvore não for válido.
 */

  public double  getEsforcoLimpeza(String idArvore) throws UnknownTreeKeyException{
    if(!_arvores.checkExists(idArvore))
      throw new UnknownTreeKeyException(idArvore);

    Tree arvore = _arvores.getEntity(idArvore);

    String tipoFolha = arvore.getTipoFolha();
    int esforcoSazonal = 0;
    
    switch (_estacaoAtual) {
      case Primavera -> {
          if (tipoFolha.equals("PERENE")) {
            esforcoSazonal += 1; 
          } else if (tipoFolha.equals("CADUCA")) {
            esforcoSazonal += 1;
          }
          }

      case Verao -> {
          if (tipoFolha.equals("PERENE")) {
            esforcoSazonal += 1; 
          } else if (tipoFolha.equals("CADUCA")) {
            esforcoSazonal += 2;
          }
          }

      case Outono -> {
          if (tipoFolha.equals("PERENE")) {
            esforcoSazonal += 1; 
          } else if (tipoFolha.equals("CADUCA")) {
            esforcoSazonal += 5;
          }
          }

      case Inverno -> {
          if (tipoFolha.equals("PERENE")) {
            esforcoSazonal += 2;
          } else if (tipoFolha.equals("CADUCA")) {
            esforcoSazonal += 0;
          }
          }
    }
    return arvore.getDificuldadeLimpeza() * esforcoSazonal * log(arvore.getIdade() + 1);

  }


/**
 * Adiciona um mês à idade de todas as árvores da coleção.
 * A função itera por todas as árvores e aumenta a idade delas em um mês.
 */

  public void adicionaMes(){
    for(Tree f: _arvores.getEntities()){
      f.addMes();
    }
  }


/**
 * Calcula a satisfação de um animal com base em vários fatores, incluindo a área disponível no habitat,
 * a quantidade de espécies iguais e diferentes, e a adequação ao habitat.
 *
 * @param idAnimal  O identificador do animal cuja satisfação será calculada.
 * @return          O valor numérico da satisfação do animal arredondado para o inteiro mais próximo.
 * @throws UnknownHabitatKeyException  Se o identificador do habitat do animal não for válido.
 * @throws UnknownSpeciesKeyException  Se o identificador da espécie do animal não for válido.
 * @throws UnknownAnimalKeyException   Se o identificador do animal não for válido.
 */

  public int satisfacaoAnimal(String idAnimal) throws UnknownHabitatKeyException, UnknownSpeciesKeyException, UnknownAnimalKeyException{
    Animal animal = validarAnimal(idAnimal);
    Habitat habitat = animal.getHabitat();

    double areaPorPopulacao = (double) habitat.getArea() / populacao(habitat);
    
    double satisfacao = 20 + 3 * especieIgual(animal, habitat) - 2 * especieDiferente(animal, habitat) + areaPorPopulacao + adequacao(animal, habitat);
    
    return (int) Math.round(satisfacao);
  }


/**
 * Calcula o esforço de trabalho necessário para manter um habitat específico, com base na área do habitat,
 * a população de animais e o esforço de limpeza de árvores dentro do habitat.
 *
 * @param habitatId  O identificador do habitat cujo trabalho será calculado.
 * @return           O valor do trabalho necessário para manter o habitat.
 * @throws UnknownHabitatKeyException  Se o identificador do habitat não for válido.
 * @throws UnknownTreeKeyException     Se o identificador de uma árvore dentro do habitat não for válido.
 */

  public double  trabalhoHabitat(String habitatId) throws UnknownHabitatKeyException, UnknownTreeKeyException{
    Habitat habitat = validarHabitat(habitatId);
    int esforcolimpeza = 0;
    String arvores = habitat.getArvoreIds();

    if (!arvores.equals("")) {
      List<String> listaArvores = dividirStringParaLista(arvores);
      
      for (String f : listaArvores) {
          esforcolimpeza += getEsforcoLimpeza(f);
      }
    }
    
    return habitat.getArea() + 3*populacao(habitat) + esforcolimpeza;

  }


/**
 * Calcula a satisfação de um veterinário com base na sua carga de trabalho, que é determinada pelo número de espécies
 * sob sua responsabilidade e pela proporção de animais por funcionário.
 *
 * @param vetId  O identificador do veterinário cuja satisfação será calculada.
 * @return       O valor numérico da satisfação do veterinário.
 */

  public int satisfacaoVet(String vetId){
      Employee funcionario = _funcionarios.getEntity(vetId);
      List<String> responsibilidadesList = dividirStringParaLista(funcionario.getResponsibility());
  
      double sum = 0; 
  
      for(String d: responsibilidadesList){
          Species especie = _especies.getEntity(d);
          
  
          int quantidadeFuncionarios = 0;
  
          for(Employee c: _funcionarios.getEntities()){
              if(c.hasResponsibility(d)){
                  quantidadeFuncionarios++;
              }
          }
          
          if (quantidadeFuncionarios > 0) {
              sum += (double) especie.getQuantidadeAnimais() / quantidadeFuncionarios;
          }
      }
  
      int satisfacao = (int) Math.round(20 - sum);
  
      return satisfacao;
  
  }


/**
 * Calcula a satisfação de um tratador de árvores com base no esforço necessário para manter os habitats sob sua responsabilidade,
 * levando em consideração a quantidade de funcionários e a carga de trabalho.
 *
 * @param trtid  O identificador do tratador de árvores cuja satisfação será calculada.
 * @return       O valor numérico da satisfação do tratador.
 * @throws UnknownHabitatKeyException  Se o identificador de um habitat não for válido.
 * @throws UnknownTreeKeyException     Se o identificador de uma árvore dentro de um habitat não for válido.
 */

  public int satisfacaoTrt(String trtid) throws UnknownHabitatKeyException, UnknownTreeKeyException{
    Employee funcionario = _funcionarios.getEntity(trtid);


    List<String> responsibilidadesList = dividirStringParaLista(funcionario.getResponsibility());

    double sum = 0;

    for(String d: responsibilidadesList){      
      double trabalhohabitat = trabalhoHabitat(d);

      int quantidadeFuncionarios = 0;

      for(Employee c: _funcionarios.getEntities()){
        if(c.hasResponsibility(d))
          quantidadeFuncionarios++;
      }
      if (quantidadeFuncionarios > 0) {
            sum += trabalhohabitat / quantidadeFuncionarios;
      }
    }
    int satisfacao = (int) Math.round(300 - sum);

    return satisfacao;
  }


/**
 * Calcula a satisfação de um funcionário (veterinário ou tratador) com base em seu tipo de função.
 *
 * @param employeeId  O identificador do funcionário cuja satisfação será calculada.
 * @return            O valor numérico da satisfação do funcionário.
 * @throws UnknownEmployeeKeyException  Se o identificador do funcionário não for válido.
 * @throws UnknownHabitatKeyException   Se o identificador de um habitat não for válido.
 * @throws UnknownTreeKeyException      Se o identificador de uma árvore dentro de um habitat não for válido.
 */

  public int calcularSatisfacaoFuncionarios(String employeeId) throws UnknownEmployeeKeyException, UnknownHabitatKeyException, UnknownTreeKeyException{    
    Employee funcionario = _funcionarios.getEntity(employeeId);

    if (funcionario == null)
      throw new UnknownEmployeeKeyException(employeeId);
  
    String tipo = funcionario.getTipo();
    
    if(tipo.equals("VET")){
      return satisfacaoVet(employeeId);
    }
    return satisfacaoTrt(employeeId);
  }


/**
 * Aplica uma vacina a um animal, verificando se o veterinário está autorizado a realizar o procedimento.
 * Regista o histórico da aplicação da vacina e atualiza o estado de saúde do animal.
 * Se a vacina for incorreta, calcula o dano causado ao animal.
 *
 * @param idVaccine       O identificador da vacina a ser aplicada.
 * @param idVeterinarian  O identificador do veterinário responsável pela aplicação.
 * @param idAnimal        O identificador do animal a ser vacinado.
 * @throws UnknownVeterinarianKeyException  Se o identificador do veterinário não for válido.
 * @throws VeterinarianNotAuthorizedException  Se o veterinário não estiver autorizado a vacinar o animal.
 * @throws UnknownAnimalKeyException        Se o identificador do animal não for válido.
 * @throws UnknownVaccineKeyException       Se o identificador da vacina não for válido.
 */

  public void vacinarAnimal(String idVaccine, String idVeterinarian, String idAnimal) throws UnknownVeterinarianKeyException, VeterinarianNotAuthorizedException, UnknownAnimalKeyException, UnknownVaccineKeyException {
    Employee funcionario = validarVeterinario(idVeterinarian);
    Animal animal = validarAnimal(idAnimal);
    String especieAnimal = animal.getChaveEspecie();
    String nomeEspecieAnimal = _especies.getEntity(especieAnimal).getNome();

    validarAutorizacaoVeterinario(funcionario, especieAnimal);

    Vaccine vacina = validarVacina(idVaccine);
    List<String> listaEspeciesVacinas = dividirStringParaLista(vacina.getChaveEspecies());
    List<String> listaNomesEspecies = new ArrayList<>();
    for(String f: listaEspeciesVacinas){
      listaNomesEspecies.add(_especies.getEntity(f).getNome());
    }

    vacina.addUtilizacao();

    if (listaEspeciesVacinas.contains(especieAnimal)) {
        atualizarEstadoAnimal(animal, "NORMAL", idVaccine, idVeterinarian);
        registarVacinaAplicada(idVaccine, idVeterinarian, especieAnimal, funcionario, _historicoVacinas);
    } else {
        int danoTotal = calcularDanoTotal(nomeEspecieAnimal, listaNomesEspecies);
        atualizarEstadoAnimalComDano(animal, danoTotal);
        registarVacinaAplicada(idVaccine, idVeterinarian, especieAnimal, funcionario, _historicoVacinasErradas);
        registarVacinaAplicada(idVaccine, idVeterinarian, especieAnimal, funcionario, _historicoVacinas);
    }
  }


/**
 * Divide uma string separada por vírgulas em uma lista de strings.
 *
 * @param str  A string a ser dividida.
 * @return     Uma lista contendo os elementos da string separados por vírgula.
 */

  public List<String> dividirStringParaLista(String str){
    if (str != null && !str.isEmpty()) {
        return Arrays.asList(str.split(","));
    }
    return new ArrayList<>();
  }


/**
 * Atualiza o estado de saúde de um animal e regista a aplicação de uma vacina.
 *
 * @param animal         O animal cujo estado será atualizado.
 * @param estadoSaude    O novo estado de saúde do animal.
 * @param idVaccine      O identificador da vacina aplicada.
 * @param idVeterinarian O identificador do veterinário que aplicou a vacina.
 */

  public void atualizarEstadoAnimal(Animal animal, String estadoSaude, String idVaccine, String idVeterinarian) {
    animal.setEstadoSaude(estadoSaude);
    animal.addAtosMedicos("REGISTO-VACINA|" + idVaccine + "|" + idVeterinarian + "|" + animal.getChaveEspecie());
  }



/**
 * Regista a aplicação de uma vacina no histórico de um funcionário.
 *
 * @param idVaccine       O identificador da vacina aplicada.
 * @param idVeterinarian  O identificador do veterinário que aplicou a vacina.
 * @param especieAnimal   A espécie do animal vacinado.
 * @param funcionario     O funcionário responsável pela aplicação da vacina.
 * @param historico       O histórico onde o registro será adicionado.
 */

  public void registarVacinaAplicada(String idVaccine, String idVeterinarian, String especieAnimal, Employee funcionario, List<String> historico) {
    funcionario.addAtos("REGISTO-VACINA|" + idVaccine + "|" + idVeterinarian + "|" + especieAnimal);
    registarVacinaApilcada(historico, idVaccine, idVeterinarian, especieAnimal);
  }


/**
 * Calcula o dano total causado ao animal por uma vacina incorreta, baseado nas similaridades de caracteres
 * entre o nome da espécie do animal e as espécies listadas para a vacina.
 *
 * @param especieAnimal          O nome da espécie do animal.
 * @param listaEspeciesVacinas   A lista de nomes de espécies que a vacina cobre.
 * @return                       O dano total causado ao animal pela aplicação incorreta da vacina.
 */
 
  public int calcularDanoTotal(String especieAnimal, List<String> listaEspeciesVacinas) {
    int tamanhoMaximo = listaEspeciesVacinas.stream().mapToInt(String::length).max().orElse(0);
    int caracteresComunsTotal = calcularCaracteresComunsComTodasEspecies(especieAnimal, listaEspeciesVacinas);
    return tamanhoMaximo - caracteresComunsTotal;
  }


/**
 * Atualiza o estado de saúde de um animal com base no dano causado por uma vacina incorreta.
 *
 * @param animal     O animal cujo estado será atualizado.
 * @param danoTotal  O dano causado pela aplicação incorreta da vacina.
 */

  public void atualizarEstadoAnimalComDano(Animal animal, int danoTotal) {
    if (danoTotal == 0) {
        animal.setEstadoSaude("CONFUSÃO");
    } else if (danoTotal >= 1 && danoTotal <= 4) {
        animal.setEstadoSaude("ACIDENTE");
    } else {
        animal.setEstadoSaude("ERRO");
    }
  }


/**
 * Calcula o número máximo de caracteres em comum entre a espécie de um animal e todas as espécies de uma vacina.
 * 
 * @param especieAnimal  O nome da espécie do animal.
 * @param especiesVacina A lista de espécies cobertas pela vacina.
 * @return               O número máximo de caracteres comuns entre o nome da espécie do animal e as espécies da vacina.
 */

  public int calcularCaracteresComunsComTodasEspecies(String especieAnimal, List<String> especiesVacina) {
    int caracteresComunsMaximo = 0;

    for (String especieVacina : especiesVacina) {
        int caracteresComuns = calcularCaracteresComuns(especieAnimal, especieVacina);
        caracteresComunsMaximo = Math.max(caracteresComunsMaximo, caracteresComuns);
    }

    return caracteresComunsMaximo;
  }


/**
 * Calcula o número de caracteres comuns entre o nome da espécie de um animal e o nome da espécie coberta pela vacina.
 * 
 * @param especieAnimal  O nome da espécie do animal.
 * @param especieVacina  O nome da espécie coberta pela vacina.
 * @return               O número de caracteres comuns entre as duas espécies.
 */

  public int calcularCaracteresComuns(String especieAnimal, String especieVacina) {
    Map<Character, Integer> frequenciaAnimal = new HashMap<>();
    for (char c : especieAnimal.toCharArray()) {
        frequenciaAnimal.put(c, frequenciaAnimal.getOrDefault(c, 0) + 1);
    }

    Map<Character, Integer> frequenciaVacina = new HashMap<>();
    for (char c : especieVacina.toCharArray()) {
        frequenciaVacina.put(c, frequenciaVacina.getOrDefault(c, 0) + 1);
    }

    int caracteresComuns = 0;

    for (Map.Entry<Character, Integer> entry : frequenciaAnimal.entrySet()) {
        char caractere = entry.getKey();
        int frequenciaAnimalAtual = entry.getValue();

        if (frequenciaVacina.containsKey(caractere)) {
            int frequenciaVacinaAtual = frequenciaVacina.get(caractere);
            caracteresComuns += Math.min(frequenciaAnimalAtual, frequenciaVacinaAtual);
        }
    }

    return caracteresComuns;
}


/**
 * Calcula a satisfação global do hotel somando a satisfação de todos os funcionários e animais.
 * 
 * @return  A satisfação global, que é a soma da satisfação dos funcionários e dos animais.
 * @throws UnknownHabitatKeyException   Se o identificador de um habitat for inválido.
 * @throws UnknownTreeKeyException      Se o identificador de uma árvore for inválido.
 * @throws UnknownAnimalKeyException    Se o identificador de um animal for inválido.
 * @throws UnknownEmployeeKeyException  Se o identificador de um funcionário for inválido.
 * @throws UnknownSpeciesKeyException   Se o identificador de uma espécie for inválido.
 */

  public int satisfacaoGlobal() throws UnknownHabitatKeyException, UnknownTreeKeyException, UnknownAnimalKeyException, UnknownEmployeeKeyException, UnknownSpeciesKeyException{
    int satisfacaoTotalFuncionarios = 0;
    int satisfacaoTotalAnimais = 0;
    int satisfacaoGlobal = 0;

    for (Employee f : _funcionarios.getEntities()) {
        satisfacaoTotalFuncionarios += calcularSatisfacaoFuncionarios(f.getId());
    }

    for (Animal a : _animais.getEntities()) {
        satisfacaoTotalAnimais += satisfacaoAnimal(a.getId());
    }
    satisfacaoGlobal = satisfacaoTotalFuncionarios + satisfacaoTotalAnimais;

    return (int) Math.round(satisfacaoGlobal);
  }

  public String getArvoreString(String arvoreId) throws UnknownTreeKeyException{
    Tree arvore = validarTree(arvoreId);
    return arvore.toString() + arvore.getStatus(_estacaoAtual);
  }
}
