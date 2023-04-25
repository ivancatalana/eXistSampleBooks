/**
 * Esta clase representa un menú que permite al usuario interactuar con una base de datos de libros.
 */
package net.xeill.elpuig;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;
import java.util.Scanner;

public class Menu {
    // Atributos de la clase
    /**
     * Controlador de la base de datos Exist.
     */
    private ExistController existController;
    /**
     * Scanner para leer entrada de usuario.
     */
    private Scanner scanner;
    /**
     * Constructor de la clase Menu.
     * Crea una nueva instancia del controlador de Exist y del Scanner.
     */
    public Menu() {
        existController = new ExistController();
        scanner = new Scanner(System.in);
    }
    /**
     * Inicia el menú y muestra las opciones disponibles.
     * Permite al usuario seleccionar una opción y ejecutar una acción correspondiente.
     *
     * @throws XQException si hay un error en la ejecución de la consulta XQuery.
     */
    public void start() throws XQException {
        while (true) {
            printMenu();
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    executeQuery();
                    break;
                case 2:
                    insertBook();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    updateBookValue();
                    break;
                case 5:
                    System.out.print("Delete by (id, name, year, author): ");
                    String deleteField = scanner.nextLine().toLowerCase();
                    switch (deleteField) {
                        case "id":
                        case "name":
                        case "year":
                        case "author":
                            existController.deleteBook(deleteField);
                            break;
                        default:
                            System.out.println("Invalid field!");
                            break;
                    }
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    /**
     * Muestra el menú de opciones disponibles.
     */
    private void printMenu() {
        System.out.println("----- MENU -----");
        System.out.println("1. Execute query");
        System.out.println("2. Insert book");
        System.out.println("3. Update book");
        System.out.println("4. Update book field");
        System.out.println("5. Delete book");
        System.out.println("0. Exit");
        System.out.print("Select an option: ");
    }
    /**
     * Ejecuta una consulta de búsqueda de libros en la base de datos.
     * Permite al usuario seleccionar un campo de búsqueda y un valor a buscar.
     * Luego ejecuta la consulta y muestra los resultados en la consola.
     */
    private void executeQuery() {
        System.out.print("Search by (name, year, author): ");
        String field = scanner.nextLine().toLowerCase();
        String searchValue;
        switch (field) {
            case "name":
                System.out.print("Enter name to search: ");
                searchValue = scanner.nextLine();
                break;
            case "year":
                System.out.print("Enter year to search: ");
                searchValue = scanner.nextLine();
                break;
            case "author":
                System.out.print("Enter author to search: ");
                searchValue = scanner.nextLine();
                break;
            default:
                System.out.println("Invalid field!");
                return;
        }
        String query = "for $book in doc('/db/book/books1.xml')/library/book where $book/"+field+" = \""+searchValue+"\" return $book";
        XQResultSequence result = existController.executeQuery(query);
        System.out.println("Query result:");
        try {
            while (result.next()) {
                System.out.println(result.getItemAsString(null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Elimina un libro de la base de datos por su ID.
     *
     * @param id el ID del libro a eliminar.
     */
    public void deleteBook(int id) {

        String query = "update delete doc('/db/book/books1.xml')/library/book[id="+id+"]";
        existController.executeCommand(query);
        System.out.println("Book deleted successfully!");
    }
    /**
     * Inserta un nuevo libro en la base de datos.
     * Permite al usuario ingresar los detalles del libro a insertar.
     *
     * @throws Exception si hay un error en la ejecución de la consulta XQuery.
     */
    private void insertBook() {
        System.out.print("Enter book id: ");
        String id = scanner.nextLine();
        System.out.print("Enter book name: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book year: ");
        String year = scanner.nextLine();
        try {
            existController.insertBook(id, title, author, year);
            System.out.println("Book inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Actualiza un libro existente en la base de datos por su ID.
     * Permite al usuario ingresar los nuevos detalles del libro a actualizar.
     *
     * @throws Exception si hay un error en la ejecución de la consulta XQuery.
     */
    private void updateBook() {
        System.out.print("Enter book id to update: ");
        String id = scanner.nextLine();
        System.out.print("Enter new book title: ");
        String newTitle = scanner.nextLine();
        System.out.print("Enter new book author: ");
        String newAuthor = scanner.nextLine();
        System.out.print("Enter new book year: ");
        String newYear = scanner.nextLine();
        try {
            existController.updateBook(id, newTitle, newAuthor, newYear);
            System.out.println("Book updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Elimina todos los libros de la base de datos que coincidan con un campo y un valor específicos.
     *
     * @param field el campo de los libros a eliminar.
     * @param value el valor del campo a buscar y eliminar.
     * @throws XQException si hay un error en la ejecución de la consulta XQuery.
     */
    public void deleteBooksByField(String field, String value) throws XQException {
        String query = "update delete /library/book[" + field + "='" + value + "']";
        existController.executeCommand(query);
        System.out.println("Books with " + field + " '" + value + "' deleted successfully!");
    }
    /**
     * Actualiza el valor de un campo específico de un libro en la base de datos por su ID.
     * Permite al usuario seleccionar el campo a actualizar y el nuevo valor.
     *
     * @throws Exception si hay un error en la ejecución de la consulta XQuery.
     */
    public void updateBookValue() {
        System.out.print("Enter ID of the book to update: ");
        String id = scanner.nextLine();
        System.out.print("Enter field to update (name, author, year): ");
        String field = scanner.nextLine().toLowerCase();
        System.out.print("Enter new value: ");
        String newValue = scanner.nextLine();
        String query = "update value " +
                "doc('/db/book/books1.xml')/library/book[id='" + id + "']/" + field +
                " with '" + newValue + "'";
        try {
            existController.executeCommand(query);
            System.out.println("Book updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
