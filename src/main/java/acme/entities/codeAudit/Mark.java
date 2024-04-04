
package acme.entities.codeAudit;

public enum Mark {

	A_PLUS("A+"), A("A"), B("B"), C("C"), F("F"), F_MINUS("F-");


	private String markValue;


	Mark(final String mark) {
		this.markValue = mark;
	}

	public String getMark() {
		return this.markValue;
	}

}
