package hva.app.main;

import hva.core.HotelManager;
import hva.core.exception.UnavailableFileException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command to open a file.
 */
class DoOpenFile extends Command<HotelManager> {
  DoOpenFile(HotelManager receiver) {
    super(Label.OPEN_FILE, receiver);
  }

  @Override
  protected final void execute() throws CommandException {  
    
    Form file = new Form("File");
    file.addStringField("nameFile", Prompt.openFile());
    file.parse();

    try{
      if(_receiver.getHotel().getExistemAlteracoes() && !_receiver.fileExists()){
        if(Form.confirm(Prompt.saveBeforeExit()))
          new DoSaveFile(_receiver).execute();
      }

      String NameOfFile = file.stringField("nameFile");
      _receiver.load(NameOfFile);
    } catch (UnavailableFileException e){
      e.printStackTrace();
    }
  }
}