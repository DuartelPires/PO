package hva.app.animal;

import hva.core.Hotel;
import java.util.Comparator;
import java.util.List;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed

/**
 * Show all animals registered in this zoo hotel.
 */
class DoShowAllAnimals extends Command<Hotel> {

  DoShowAllAnimals(Hotel receiver) {
    super(Label.SHOW_ALL_ANIMALS, receiver);
  }
  
  @Override
  protected final void execute() {
    List<String> list = _receiver.getAllAnimalsList();
    list.sort(Comparator.comparing(s -> s.split("\\|")[1].trim()));
        
    for(String f: list){
      _display.addLine(f);
    }
    _display.display();
  }
}
