package hva.app.search;

import hva.core.Hotel;
import java.util.*;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show all medical acts applied to a given animal.
 **/
class DoShowMedicalActsOnAnimal extends Command<Hotel> {

  DoShowMedicalActsOnAnimal(Hotel receiver) {
    super(Label.MEDICAL_ACTS_ON_ANIMAL, receiver);
    addStringField("animalId", hva.app.animal.Prompt.animalKey());
  }

  @Override
  protected void execute() throws CommandException {
    String animalKey = stringField("animalId");

    List<String>list = _receiver.getAtosMedicosAnimal(animalKey);
    list.sort(Comparator.comparing(s -> s.split("\\|")[1].trim()));
        
    for(String f: list){
      _display.addLine(f);
    }
    _display.display();
  }
}
