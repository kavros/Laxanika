package Model;

public class Constants {

	//constants filePaths
	private static final String _databaseCredentialsFilePath = "cfg\\database.xml";
	private static final String _productsFilePath = "cfg\\products.xml";
	private static final String _historyFilePath  = "history\\history.txt";
	private static final String _historyDatabaseFilePath = "cfg\\history.xml";

	//JTable columns
	public static final Object[] VF_TABLE_HEADER = { "Προιόν","Ποσοστό Κέρδους" ,"Τιμή Χονδρικής",
			"Τιμή Kefalaio", "Νεα Τιμή","Κέρδος σε €", "Ενημέρωσε το Κεφάλαιο" };
	public static final Object[] PRODUCTS_TABLE_HEADER={"Προιόν","Κέρδος","Κωδικός στο Kefalaio 5"};
	public static final Object[] HISTORY_TABLE_HEADER={"Προιόν","Τιμή 1","Τιμή 2","Τιμή 3"};
	public static final Object[] LABELS_TABLE_HEADER={"Όνομα","Προέλευση","Τιμή","Κωδικός Τιμολογίου"};

	//getters
	public static String getHistoryFilePath(){
		return _historyFilePath;
	}

	public static String getProductsFilePath(){
		return _productsFilePath;
	}

	public static String getCredentialsFilePath(){
		return _databaseCredentialsFilePath;
	}

	public static String getHistoryDatabaseFilePath() {
		return _historyDatabaseFilePath;
	}
}
