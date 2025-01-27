package hva.app.search;

import java.util.*;

import hva.core.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show all animals of a given habitat.
 **/
class DoShowAnimalsInHabitat extends Command<Hotel> {

  DoShowAnimalsInHabitat(Hotel receiver) {
    super(Label.ANIMALS_IN_HABITAT, receiver);
    addStringField("habitatId", hva.app.habitat.Prompt.habitatKey());
  }

  @Override
  protected void execute() throws CommandException {
    //getAllAnimalsHabitat

    String habitatKey = stringField("habitatId");

    List<String>list = _receiver.getAllAnimalsHabitat(habitatKey);
    list.sort(Comparator.comparing(s -> s.split("\\|")[1].trim()));
        
    for(String f: list){
      _display.addLine(f);
    }
    _display.display();
  }
}
