package hva.app.habitat;

import hva.core.Hotel;
import java.util.List;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show all trees in a given habitat.
 **/
class DoShowAllTreesInHabitat extends Command<Hotel> {

  DoShowAllTreesInHabitat(Hotel receiver) {
    super(Label.SHOW_TREES_IN_HABITAT, receiver);
    addStringField("habitatId", Prompt.habitatKey());
  }
  
  @Override
  protected void execute() throws CommandException {
    Form arvore = new Form("Arvores");
    arvore.parse();
    String habitatKey = stringField("habitatId");
    
    List<String> list = _receiver.getAllTreesInHabitat(habitatKey);

    for(String f: list){
      _display.addLine(f);
    }
    _display.display();
  }
}
