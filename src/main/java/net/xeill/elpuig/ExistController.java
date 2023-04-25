/**

 This class provides a connection to the eXist database and implements methods for executing queries,
 deleting, inserting, and updating books in the database.
 */
package net.xeill.elpuig;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;
import net.xqj.exist.ExistXQDataSource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import java.util.Scanner;

public class ExistController {
    private XQConnection connection;
    /**
     * Constructs an instance of the ExistController class, establishing a connection to the eXist database.
     * @throws RuntimeException if an XQException is thrown
     */
    public ExistController() {
        try {
            XQDataSource xqs = new ExistXQDataSource();
            xqs.setProperty("serverName", "localhost");
            xqs.setProperty("port", "8080");
            xqs.setProperty("user","admin");
            xqs.setProperty("password","");

            connection = xqs.getConnection();

        } catch (XQException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Deletes all books with a specified field value in the database.
     * @param field the field to search for books to delete
     * @throws XQException if an XQuery expression is invalid or execution of a command fails
     */
    public void deleteBook(String field) throws XQException {
        System.out.print("Enter " + field + " to search: ");
        Scanner scanner = new Scanner(System.in);
        String searchValue = scanner.nextLine();
        String query = "update delete /library/book[" + field + "='" + searchValue + "']";
        executeCommand(query);
        System.out.println("All books with " + field + "='" + searchValue + "' deleted successfully!");
    }
    /**
     * Executes an XQuery expression and returns the result sequence.
     * @param query the XQuery expression to execute
     * @return the result sequence of the XQuery expression
     * @throws RuntimeException if an XQException is thrown
     */

    public XQResultSequence executeQuery(String query) {
        try {
            XQExpression xqe = connection.createExpression();
            XQResultSequence xqrs = xqe.executeQuery(query);
            return xqrs;
        } catch (XQException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Executes an XQuery command.
     * @param command the XQuery command to execute
     * @throws RuntimeException if an XQException is thrown
     */
    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------
    //  ----------------PRUEBA executeCommand-----------------------------PRUEBA -----------------------------PRUEBA -------------
public void executeCommand(String command) {
    try {
        XQExpression xqe = connection.createExpression();
        xqe.executeCommand(command);
        System.out.println("Command executed successfully!");
    } catch (XQException e) {
        e.printStackTrace();
    }
}
    /**
     * Inserts a book into the database.
     * @param id the id of the book to insert
     * @param title the title of the book to insert
     * @param author the author of the book to insert
     * @param year the year of the book to insert
     * @throws XQException if an XQuery expression is invalid or execution of a command fails
     */
    //INsert Example
    //------------------INsert Example---------------------------------------------------------
    //--------------------------------INsert Example-------------------------------------------
    //----------------------------------------------INsert Example-----------------------------
    public void insertBook(String id, String title, String author, String year) throws XQException {
        XQExpression xqe = connection.createExpression();
        String query = "update insert " +
                "<book>" +
                "<id>" + id + "</id>" +
                "<name>" + title + "</name>" +
                "<author>" + author + "</author>" +
                "<year>" + year + "</year>" +
                "</book>" +
                "into doc('/db/book/books1.xml')/library";
        xqe.executeCommand(query);
        System.out.println("Book inserted successfully!");
    }
    /**
     * Actualiza un libro en la base de datos.
     *
     * @param id       el id del libro a actualizar
     * @param newTitle el nuevo título del libro a actualizar
     * @param newAuthor el nuevo autor del libro a actualizar
     * @param newYear   el nuevo año de publicación del libro a actualizar
     * @throws XQException si ocurre un error en la conexión XQuery
     */
    //-----------------UPDATE_BOOK-------------------------------------------
    //---------------------------------------------------------------------------
    //---------------------------------------------------------------------------
    public void updateBook(String id, String newTitle, String newAuthor, String newYear) throws XQException {
        XQExpression xqe = connection.createExpression();
        String query = "update replace " +
                "doc('/db/book/books1.xml')/library/book[id = '" + id + "'] " +
                "with " +
                "<book>" +
                "<title>" + newTitle + "</title>" +
                "<author>" + newAuthor + "</author>" +
                "<year>" + newYear + "</year>" +
                "</book>";
        xqe.executeCommand(query);
        System.out.println("Book updated successfully!");
    }
    /**
     * Ejecuta una actualización en la base de datos.
     *
     * @param query    la consulta a ejecutar
     * @param username el nombre de usuario para la conexión a la base de datos
     * @param password la contraseña para la conexión a la base de datos
     * @throws Exception si ocurre un error en la conexión a la base de datos
     */
    // -----------------------------------------------------------------------------------------------------------
    // --------------------Execute Update---------------------Execute Update---------------------------Execute Update---------------------------------------
    // -----------------------------------------------------------------------------------------------------------
    private void executeUpdate(String query, String username, String password) throws Exception {
        String serverUri = "http://localhost:8080/exist/xmlrpc";
        String driver = "org.exist.xmldb.DatabaseImpl";

        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        DatabaseManager.registerDatabase(database);

        Collection collection = DatabaseManager.getCollection(serverUri + "/db", username, password);
        XPathQueryService service = (XPathQueryService) collection.getService("XPathQueryService", "1.0");
        service.setProperty("indent", "yes");

        try {
            ResourceSet result = service.query(query);
            ResourceIterator i = result.getIterator();
            while (i.hasMoreResources()) {
                Resource r = i.nextResource();
                System.out.println(r.getContent());
            }
        } finally {
            if (collection != null) {
                collection.close();
            }
        }
    }
    /**
     Prints the result sequence of an XQuery expression to the console.
     @param xqrs The result sequence to print.
     */
    public void printResultSequence(XQResultSequence xqrs) {
        try {
            while (xqrs.next()) {
                System.out.println(xqrs.getItemAsString(null));
            }
        } catch (XQException e) {
            throw new RuntimeException(e);
        }
    }
}