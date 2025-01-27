package hva.app.search;
import java.util.*;
import hva.core.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show all vaccines applied to animals belonging to an invalid species.
 **/
class DoShowWrongVaccinations extends Command<Hotel> {

  DoShowWrongVaccinations(Hotel receiver) {
    super(Label.WRONG_VACCINATIONS, receiver);
    //FIXME add command fields
  }

  @Override
  protected void execute() throws CommandException {
    List<String> list;
      list = _receiver.getHistoricoErradoVacinas();
    list.sort(Comparator.comparing(s -> s.split("\\|")[1].trim()));
        
    for(String f: list){
      _display.addLine(f);
    }
    _display.display();
  }
}
