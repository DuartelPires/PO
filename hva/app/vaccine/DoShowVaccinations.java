package hva.app.vaccine;

import hva.core.Hotel;
import java.util.*;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed

/**
 * Show all applied vacines by all veterinarians of this zoo hotel.
 **/
class DoShowVaccinations extends Command<Hotel> {

  DoShowVaccinations(Hotel receiver) {
    super(Label.SHOW_VACCINATIONS, receiver);
  }
  
  @Override
  protected final void execute() {
    List<String> list = _receiver.getHistoricoVacinas();
    list.sort(Comparator.comparing(s -> s.split("\\|")[1].trim()));

    for(String f: list){
      _display.addLine(f);
    }
    _display.display();
  }
}
