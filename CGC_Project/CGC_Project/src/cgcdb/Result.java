package cgcdb;

public enum Result { 
	VOID(false, "Void"),
	NA(false, "N/A"), 
	A(true, "A"), 
	R(true, "R"), 
	M(true, "M"), 
	O1(true, "O.1"), 
	O2(true, "O.2"), 
	O4(true, "O.4"),
	O(true, "O");
	
	
//-------------------------------------------------
	boolean value;
	String symbol;

	Result(final boolean value, final String symbol){
		this.value = value;
		this.symbol = symbol;
	}
	
	public boolean getValue(){
		return value;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public static Result BySymbol(final String str) {
        for (Result e : Result.values()) {
            if (str.contains(e.symbol)) {
                return e;
            }
        }
        return null;
    }
}