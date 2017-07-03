package main;

import java.util.Vector;

public class Trace {

	private String name;
	private Vector<String> trace_alphabet;
	private Vector<String> trace_content_vector;
	private int trace_length;
	private StringBuffer trace_textual_content;

	public Trace(String trace_name) {
		name = trace_name;
		trace_content_vector = new Vector<String>();
		trace_textual_content = new StringBuffer();
	}

	public String getTraceName() {
		return name;
	}

	public void setTraceName(String trace_name) {
		name = trace_name;
	}

	public Vector<String> getTraceContentVector() {
		return trace_content_vector;
	}

	public void setTraceContentVector(Vector<String> trace_content) {
		trace_content_vector = trace_content;
	}

	public Vector<String> getTraceAlphabet() {
		return trace_alphabet;
	}

	public void setTraceAlphabet(Vector<String> trace_alphabet) {
		this.trace_alphabet = trace_alphabet;
	}

	public String getTraceNumber() {
		String[] split = this.getTraceName().split("#");
		return split[1];		
	}

	public StringBuffer getTrace_textual_content() {
		return trace_textual_content;
	}

	public void setTrace_textual_content(StringBuffer trace_textual_content) {
		this.trace_textual_content = trace_textual_content;
	}

	public int getTraceLength() {
		return trace_length;
	}

	public void setTraceLength(int trace_lenght) {
		this.trace_length = trace_lenght;
	}

}