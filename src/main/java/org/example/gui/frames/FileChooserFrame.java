package org.example.gui.frames;

import org.apache.log4j.Logger;
import org.example.errors.NullDirectoryException;
import org.example.errors.TwoPointsInFileNameException;

import javax.swing.*;

import static org.example.gui.frames.InnerFrame.RECORDING_MODE;

/**
 * window for file/directory selection
 * <p>
 * used to select the path when converting the database to the XML format
 * and when importing data from the XML format into the database
 * </p>
 */
public class FileChooserFrame extends JFileChooser {
    private static final Logger logger = Logger.getLogger(FileChooserFrame.class.getName());
    private static String path;
    private static String mode;

    /**
     * file chooser frame constructor
     *
     * @param mode  contains the type of operation being performed - import or export
     */
    public FileChooserFrame(String mode) {
        logger.info("FileChooserFrame was created");

        FileChooserFrame.mode = mode;
        if (mode.equals(RECORDING_MODE)) setDialogTitle("Save file");
        else setDialogTitle("Choose file");
        setFileSelectionMode(FILES_AND_DIRECTORIES);
        setDialogType(1);
        if (showOpenDialog(this) == APPROVE_OPTION) path = String.valueOf(getSelectedFile());
    }

    /**
     * returns the path to the required file
     *
     * @return  path export/import file
     * @throws NullDirectoryException  if no directory is selected
     * @throws TwoPointsInFileNameException  if the name of the path to the file contains 2 dots
     */
    public static String getPath() throws NullDirectoryException, TwoPointsInFileNameException {
        checkPath(path, mode);
        logger.info("Path was got");
        return path;
    }

    /**
     * checks if the selected path is correct
     *
     * @param path  path export/import file
     * @param mode  contains the type of operation being performed - import or export
     * @throws NullDirectoryException  if no directory is selected
     * @throws TwoPointsInFileNameException  if the name of the path to the file contains 2 dots
     */
    public static void checkPath(String path, String mode) throws NullDirectoryException, TwoPointsInFileNameException {
        if (path == null) throw new NullDirectoryException("No directory to save the file");
        if (path.contains(".") && mode.equals(RECORDING_MODE)) throw new TwoPointsInFileNameException("Two points were used in the name of file");
        logger.info("Path " + path + " was checked");
    }
}
