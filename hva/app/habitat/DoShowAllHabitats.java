package hva.app.habitat;

import hva.core.Hotel;
import java.util.List;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed

/**
 * Show all habitats of this zoo hotel.
 **/
class DoShowAllHabitats extends Command<Hotel> {

  DoShowAllHabitats(Hotel receiver) {
    super(Label.SHOW_ALL_HABITATS, receiver);
  }
  
  @Override
  protected void execute() {
    List<String> list = _receiver.getAllHabitatsList();
    //list.sort(Comparator.comparing(s -> s.split("\\|")[1].trim(), String::compareToIgnoreCase));
    for(String f: list){
      _display.addLine(f);
    }
    _display.display();
  }
}
