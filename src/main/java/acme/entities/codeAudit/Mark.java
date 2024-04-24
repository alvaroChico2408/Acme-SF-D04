
package acme.entities.codeAudit;

public enum Mark {

	F_MINUS("F-"), F("F"), C("C"), B("B"), A("A"), A_PLUS("A+");


	private String markValue;


	Mark(final String mark) {
		this.markValue = mark;
	}

	public String getMark() {
		return this.markValue;
	}

	public static Mark parse(final String mark) {
		return Mark.valueOf(mark);
	}

}
