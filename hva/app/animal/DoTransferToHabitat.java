package hva.app.animal;

import hva.core.Hotel;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Transfers a given animal to a new habitat of this zoo hotel.
 */
class DoTransferToHabitat extends Command<Hotel> {

  DoTransferToHabitat(Hotel hotel) {
    super(Label.TRANSFER_ANIMAL_TO_HABITAT, hotel);
    addStringField("idAnimal", Prompt.animalKey());
    addStringField("idHabitat", hva.app.habitat.Prompt.habitatKey());


  }
  
  @Override
  protected final void execute() throws CommandException {
    Form transfer = new Form("Transfer animal");
    transfer.parse();

    String animalKey = stringField("idAnimal");
    String habitatKey = stringField("idHabitat");

    _receiver.transferAnimal(animalKey, habitatKey);
    //_display.popup("Animal transferido");

  }
}

