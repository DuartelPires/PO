package hva.core;

import hva.app.exception.UnknownHabitatKeyException;
import hva.core.exception.*;
import java.io.*;

// FIXME import classes

/**
 * Class representing the manager of this application. It manages the current
 * zoo hotel.
 **/
public class HotelManager {
  /** The current zoo hotel */ // Should we initialize this field?
  private Hotel _hotel = new Hotel();
  private String _name = "";
  
  /**
   * Saves the serialized application's state into the file associated to the current network.
   *
   * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
   * @throws MissingFileAssociationException if the current network does not have a file.
   * @throws IOException if there is some error while serializing the state of the network to disk.
   **/
  public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
    if(_hotel.getExistemAlteracoes()){
      if(_name.equals("")){
        throw new FileNotFoundException();
      }else{
        try(ObjectOutputStream saveFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(getFileName())))){
          saveFile.writeObject(_hotel);
          _hotel.setAlteracoesFalse();
        }
      }
    }
  }
  
  /**
   * Saves the serialized application's state into the specified file. The current network is
   * associated to this file.
   *
   * @param filename the name of the file.
   * @throws FileNotFoundException if for some reason the file cannot be created or opened.
   * @throws MissingFileAssociationException if the current network does not have a file.
   * @throws IOException if there is some error while serializing the state of the network to disk.
   **/
  public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
    setFileName(filename);
    save();
    _hotel.setAlteracoesFalse();
  }
  
  /**
   * @param filename name of the file containing the serialized application's state
   *        to load.
   * @throws UnavailableFileException if the specified file does not exist or there is
   *         an error while processing this file.
   **/
  public void load(String filename) throws UnavailableFileException {
    try{
      ObjectInputStream loadFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));

      _hotel = (Hotel) loadFile.readObject();
      loadFile.close();

      setFileName(filename);
    }catch (IOException | ClassNotFoundException e){
      throw new UnavailableFileException(filename + e.getMessage());
    }
  }
  
  /**
   * Read text input file and initializes the current zoo hotel (which should be empty)
   * with the domain entitiesi representeed in the import file.
   *
   * @param filename name of the text input file
   * @throws ImportFileException if some error happens during the processing of the
   * import file.
   **/
  public void importFile(String filename) throws ImportFileException {
    try {
      _hotel.importFile(filename);
    } catch (IOException | UnrecognizedEntryException /* FIXME maybe other exceptions */ | UnknownHabitatKeyException e) {
      throw new ImportFileException(filename, e);
    }
  } 
  
  /**
   * Returns the zoo hotel managed by this instance.
   *
   * @return the current zoo hotel
   **/
  public final Hotel getHotel() {
    return _hotel;
  }

  public String getFileName(){
    return _name;
  }

  public void setFileName(String name){
    _name = name;
  }

  public final void setHotel(){
    _hotel = new Hotel();
  }

  public boolean fileExists (){
    return !_name.equals("");
  }
}