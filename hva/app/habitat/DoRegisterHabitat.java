package hva.app.habitat;

import hva.core.Hotel;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Add a new habitat to this zoo hotel.
 **/
class DoRegisterHabitat extends Command<Hotel> {

  DoRegisterHabitat(Hotel receiver) {
    super(Label.REGISTER_HABITAT, receiver);
    addStringField("habitatId", Prompt.habitatKey());
    addStringField("name", Prompt.habitatName());
    addIntegerField("area", Prompt.habitatArea());
  }
  
  @Override
  protected void execute() throws CommandException {
    Form habitat = new Form("Habitat");
    habitat.parse();
    
    String habitatKey = stringField("habitatId");
    String habitatName = stringField("name");
    Integer habitatArea = integerField("area");

    _receiver.registerHabitat(habitatKey, habitatName, habitatArea);
   // _display.popup("Habitat registado");
  }
}
