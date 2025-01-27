package hva.app.habitat;

import hva.core.Hotel;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Add a new tree to a given habitat of the current zoo hotel.
 **/
class DoAddTreeToHabitat extends Command<Hotel> {

  DoAddTreeToHabitat(Hotel receiver) {
    super(Label.ADD_TREE_TO_HABITAT, receiver);
    addStringField("habitatId", Prompt.habitatKey());
    addStringField("treeId", Prompt.treeKey());
    addStringField("Nametree", Prompt.treeName());
    addIntegerField("Agetree", Prompt.treeAge());
    addIntegerField("Difftree", Prompt.treeDifficulty());
    addStringField("Typetree", Prompt.treeType());
    
  }
  
  @Override
  protected void execute() throws CommandException {
    // idhabitat, idarvore, nomearvore, idadearvore, dificuldade limpeza, tipoarvore,

    Form tree = new Form("Tree");
    tree.parse();
    
    String habitatKey = stringField("habitatId");
    String treeKey = stringField("treeId");
    String treeName = stringField("Nametree");
    Integer treeAge = integerField("Agetree");
    Integer treeDiff = integerField("Difftree");
    String treeType = stringField("Typetree");

    while (!treeType.equals("CADUCA") && !treeType.equals("PERENE")) {
      _display.popup("Tipo de árvore inválido. Deve ser 'CADUCA' ou 'PERENE'.");
      treeType = Form.requestString(Prompt.treeType());
    }

    _receiver.createTree(treeKey, treeName, treeType, treeAge, treeDiff);
    _receiver.addTreeHabitat(habitatKey, treeKey);
    _display.popup(_receiver.getArvoreString(treeKey).toString());
  }
}
