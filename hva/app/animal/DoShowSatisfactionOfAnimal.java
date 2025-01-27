package hva.app.animal;

import hva.core.Hotel;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Shows the satisfaction of a given animal.
 */
class DoShowSatisfactionOfAnimal extends Command<Hotel> {

  DoShowSatisfactionOfAnimal(Hotel receiver) {
    super(Label.SHOW_SATISFACTION_OF_ANIMAL, receiver);
    addStringField("idAnimal", Prompt.animalKey());
  }
  
  @Override
  protected final void execute() throws CommandException {
    Form satisfaction = new Form("Satisfaction");
    satisfaction.parse();

    String animalKey = stringField("idAnimal");
      
    _display.popup(_receiver.satisfacaoAnimal(animalKey));

  }
}
