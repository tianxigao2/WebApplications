import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadFromDB{
    private static ArrayList<ArrayList<String>> book_record_list;

    public LoadFromDB() throws SQLException{
        LoadFromDB.book_record_list = LoadFromDB.load();
    }

    public ArrayList<ArrayList<String>> get(){
        return LoadFromDB.book_record_list;
    }

    private static ArrayList<ArrayList<String>> load() throws SQLException{
        ArrayList<ArrayList<String>> result = new ArrayList();
        Connection con = null;
        Statement st = null;
        Savepoint save = null;
        ResultSet rs = null;

        String url = "jdbc:derby://localhost:1527/bookRecord";
        String user = "JaneG";
        String password = "JaneG";

        con = DriverManager.getConnection(url, user, password);
        con.setAutoCommit(false);               //turn off auto-commit

        try {
            save = con.setSavepoint();          //create a save point
            
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM BOOK");
            // System.out.println("Loading Book Information from DB . . .");

            while(rs.next()){
                ArrayList<String> recordForABook = new ArrayList();
                recordForABook.add(rs.getString("ranking"));
                recordForABook.add(rs.getString("img_src"));
                recordForABook.add(rs.getString("title"));
                recordForABook.add(rs.getString("author"));
                recordForABook.add(rs.getString("price"));
                recordForABook.add(rs.getString("rating"));
                result.add(recordForABook);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoadFromDB.class.getName()).log(Level.SEVERE, ex.getMessage());
            //rollback if exception occurs
            con.rollback(save);
            //If no save point is provided, the rollback method roll back to the last committed state.
        } finally {
            //release the save point after use
            con.releaseSavepoint(save);
            con.commit();
        }

        con.close();
        return result;
    }
}
