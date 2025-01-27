package hva.core;

import hva.app.exception.DuplicateTreeKeyException;
import hva.app.exception.UnknownHabitatKeyException;
import hva.core.exception.UnrecognizedEntryException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// FIXME add other imports if needed

/**
 * Esta solução assume que a classe Hotel já tem a seguinte funcionalidade

public class Hotel {
  public void registerAnimal(animalId, name, habitatId, speciesId) throws OneOrMoreCoreExceptions { ... }
  public void registerSpecies(speciesId, name) throws OneOrMoreCoreExceptions { ... }
  public void registerEmployee(employeeId, name, empType) throws OneOrMoreCoreExceptions { ... }
  public void addResponsibility(employeeId, responsibility) throws OneOrMoreCoreExceptions { ... }
  public void registerVaccine(vaccineId, name, String[] speciesIds) throws someCoreExceptionsOneOrMoreCoreExceptions { ... }
  public void createTree(TreeId, name, type, age, diff) throws OneOrMoreCoreExceptions { ... }
  public Habitat registerHabitat(habitatId, name, area) throws OneOrMoreCoreExceptions { ... }

Note-se que esta funcionalidade pode ser utilizada na concretização de alguns dos comandos.
Caso Hotel não tenha esta funcionalidade, então deverão substituir a invocação destes métodos
na classe Parser por uma ou mais linhas com uma funcionalidade semelhante.
Cada um destes métodos pode lançar uma ou mais excepções que irão corresponder aos erros que
podem acontecer ao nível do domínio surante a concretização da funcionalidade em causa.
**/


public class Parser {
  private Hotel _hotel;

  Parser(Hotel h) {
    _hotel = h;
  }

  public void parseFile(String filename) throws IOException, UnrecognizedEntryException, UnknownHabitatKeyException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;

      while ((line = reader.readLine()) != null)
        parseLine(line);
    }
  }

  private void parseLine(String line) throws UnrecognizedEntryException, UnknownHabitatKeyException {
    String[] components = line.split("\\|");
    switch(components[0]) {
    case "ESPÉCIE" -> parseSpecies(components);
    case "ANIMAL" -> parseAnimal(components);
    case "ÁRVORE" -> parseTree(components, line);
    case "HABITAT" -> parseHabitat(components, line);
    case "TRATADOR" -> parseEmployee(components, "TRT");
    case "VETERINÁRIO" -> parseEmployee(components, "VET");
    case "VACINA" -> parseVaccine(components, line);
    default -> throw new UnrecognizedEntryException ("tipo de entrada inválido: " + components[0]);
    }
  }

  // Parse a line with format ANIMAL|id|nome|idEspécie|idHabitat
  private void parseAnimal(String[] components) throws UnrecognizedEntryException {
    try {
      String id = components[1];
      String name = components[2];
      String habitatId = components[4];
      String speciesId = components[3];

      _hotel.registerAnimal(id, name, habitatId, speciesId);
    } catch ( Exception e) {
      throw new UnrecognizedEntryException("Invalid entry: " + e.getMessage());
    }
  }

  // Parse a line with format ESPÉCIE|id|nome
  private void parseSpecies(String[] components) throws UnrecognizedEntryException {
    try {
      String id = components[1];
      String name = components[2];

      _hotel.registerSpecies(id, name);
    } catch (Exception e) {
      throw new UnrecognizedEntryException("Invalid entry: " + e.getMessage());
    }
  }
  
  // Parse a line with format TRATADOR|id|nome|idHabitat1,...,idHabitatN or
  // VETERINÁRIO|id|nome|idEspécie1,...,idEspécieN
  private void parseEmployee(String[] components, String empType) throws UnrecognizedEntryException {
    try {
      String id = components[1];
      String name = components[2];

      _hotel.registerEmployee(id, name, empType);

      if (components.length == 4) {
        for(String responsibility : components[3].split(","))
          _hotel.addResponsibility(components[1], responsibility);
      }
    } catch (Exception e) {
      throw new UnrecognizedEntryException("Invalid entry: " + e.getMessage());
    }
  }

  // Parse a line with format VACINA|id|nome|idEspécie1,...,idEspécieN
  private void parseVaccine(String[] components, String empType) throws UnrecognizedEntryException {
    try {
      String id = components[1];
      String name = components[2];
      String[] speciesIds = components.length == 4 ? components[3].split(",") : new String[0];
      _hotel.registerVaccine(id, name, speciesIds);
    } catch (Exception e) {
      throw new UnrecognizedEntryException("Invalid entry: " + e.getMessage());
    }
  }

  // Parse a line with format ÁRVORE|id|nome|idade|dificuldade|tipo
  private void parseTree(String[] components, String line) throws UnrecognizedEntryException {
    try {
      String id = components[1];
      String name = components[2];
      int age = Integer.parseInt(components[3]);
      int diff = Integer.parseInt(components[4]);
      String type = components[5];

      _hotel.createTree(id, name, type, age, diff);
    } catch ( DuplicateTreeKeyException e){
      throw new UnrecognizedEntryException("Invalid entry: " + e.getMessage());
    }
  }

  // Parse a line with format HABITAT|id|nome|área|idÁrvore1,...,idÁrvoreN
  private void parseHabitat(String[] components, String line) throws UnrecognizedEntryException{
    try {
      String id = components[1];
      String name = components[2];
      int area = Integer.parseInt(components[3]);

      Habitat hab = _hotel.registerHabitat(id, name, area);

      if (components.length == 5) {
        String[] listOfTree = components[4].split(",");
        for (String treeKey : listOfTree){
          Tree arvore = _hotel.validarTree(treeKey);
          if(arvore != null){
            Habitat habitat = _hotel.validarHabitat(id);
            habitat.addArvore(treeKey);
          }
      }
    } 
    }catch (Exception e) {
      throw new UnrecognizedEntryException("Invalid entry: " + e.getMessage());
    }
  }
}
/**
 * Nota: O bloco catch presente nos vários métodos parse desta classe deverá ter em conta
 * as várias excepções que podem acontecer no contexto do bloco try em questão.
 * Estas excepções do domínio têm que ser definidas por cada grupo e devem representar situações
 * de erro específicas do domínio.
 **/

