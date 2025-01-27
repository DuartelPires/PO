package hva.app.habitat;

import hva.core.Hotel;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Change the area of a given habitat.
 **/
class DoChangeHabitatArea extends Command<Hotel> {

  DoChangeHabitatArea(Hotel receiver) {
    super(Label.CHANGE_HABITAT_AREA, receiver);

    addStringField("habitatId", Prompt.habitatKey());
    addIntegerField("area", Prompt.habitatArea());
  }
  
  @Override
  protected void execute() throws CommandException {
    Form area = new Form("Area");
    area.parse();

    String habitatKey = stringField("habitatId");
    Integer habitatArea = integerField("area");

    _receiver.changeHabitatArea(habitatKey, habitatArea);
    //_display.popup("Area trocada");
  }
}
